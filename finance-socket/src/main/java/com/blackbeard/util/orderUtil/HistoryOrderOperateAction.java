package com.blackbeard.util.orderUtil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.OrderResultDto;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.service.IUserLoginStateService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.util.Tools;

public class HistoryOrderOperateAction {
	// 历史订单操作表

	private static final Logger logger = Logger
			.getLogger(HistoryOrderOperateAction.class);

	private ResultMessageDto resultMessageDto;

	public HistoryOrderOperateAction(ResultMessageDto resultMessageDto) {
		this.resultMessageDto = resultMessageDto;
	}

	private UserLoginStateDto userLoginStateDto = new UserLoginStateDto();

	// 将mt4订单结果解析，closetime为1的对象插入数据库，组装数据发送pc
	@Transactional
	public ResultMessageDto operHistoryOrder(String order_msg) {

		// 将mt4客户端传过来的数据解析成订单结果对象
		OrderResultDto orderResultDto = ResolveMt4OrderResultInfo
				.getOrderResultDto(order_msg);

		if (orderResultDto == null) {
			logger.debug("---------mt4订单结果不存在---------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_ORDER_NO_EXIST, "mt4订单结果不存在");
			return resultMessageDto;
		}
		// mt4订单结果中订单号为负数表示这个订单失败
		if (orderResultDto.getTick() < 0) {
			logger.error("---------mt4订单号为负数---------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_ORDER_RESULT_ERROR, "mt4订单号为负数");
			return resultMessageDto;
		}

		if ("0000".equals(orderResultDto.getClosetime())) {
			resultMessageDto.setBody("");
			resultMessageDto.setResultNum(200);
			return resultMessageDto;
		}
		// closetime=0000为下订单的数据，不是历史订单数据
		String to_pc_msg = handleHistoryOrder(orderResultDto);
		resultMessageDto.setBody(to_pc_msg);
		resultMessageDto.setResultNum(200);
		return resultMessageDto;
	}

	// 处理历史订单数据
	public String handleHistoryOrder(OrderResultDto orderResultDto) {
		// 获取历史订单数据
		// 计算当前的余额和订单余额,插入数据库
		Map<Long, PCUserDto> map_user = KlineConstants.mapUserLogin;
		IHistoryOrderService historyOrderService = SocketUtils
				.getHistoryOrderService();
		INextOrderService nextOrderService = SocketUtils.getNextOrderService();
		Long order_total = sendToAllClient(orderResultDto, map_user);
		// 删除下订单表中的这个订单
		NextOrderDto nextOrderDto = new NextOrderDto();
		nextOrderDto.setTick(orderResultDto.getTick());
		nextOrderService.reduceNextOrder(nextOrderDto);
		// 插入历史订单表
		HistoryOrderDto historyOrderDto = new HistoryOrderDto();
		historyOrderDto.setClosePrice(orderResultDto.getCloseprice());
		historyOrderDto.setCloseTime(orderResultDto.getClosetime());
		historyOrderDto.setLoginId(orderResultDto.getLoginID());
		historyOrderDto.setOpenPrice(orderResultDto.getOpenprice());
		historyOrderDto
				.setOpenTime(String.valueOf(orderResultDto.getOpentime()));
		historyOrderDto.setCmd(orderResultDto.getCmd());
		historyOrderDto.setProfit(orderResultDto.getProfit());
		historyOrderDto.setSymbol(orderResultDto.getSymbol());
		historyOrderDto.setTick(orderResultDto.getTick());
		historyOrderDto.setTotal(Integer.valueOf(order_total.toString()));
		historyOrderDto.setCreateTime(new Date());
		historyOrderService.addHistoryOrder(historyOrderDto);
		// 将mt4历史订单组装成发送pc字符串
		String to_pc_msg = new OrderResultPcMessageInfo().groupHandleBody(
				orderResultDto, order_total);
		// 开始为每个socket分发消息
		getScoketToSend(map_user, orderResultDto, to_pc_msg);
		return to_pc_msg;
	}

	// 开始为每个socket分发消息
	public void getScoketToSend(Map<Long, PCUserDto> map_user,
			OrderResultDto orderResultDto, String to_pc_msg) {
		PCUserDto pcuser = map_user.get(orderResultDto.getLoginID());
		if (pcuser == null) {
			return;
		}
		List<Socket> sockets = pcuser.getListSockets();
		if (CollectionUtils.isEmpty(sockets)) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_USE_NO_EXIST, "用户登录socket不存在");
			logger.error("---------用户登录socket不存在-----------");
			return;
		}
		// 开始为每个用户socket推送信息
		for (Socket user_socket : sockets) {
			sendPcClient(user_socket, to_pc_msg);
		}
		logger.error("to_pc_msg=" + to_pc_msg);
	}

	public Long sendToAllClient(OrderResultDto orderResultDto,
			Map<Long, PCUserDto> map_user) {
		Long mt4_id = orderResultDto.getLoginID();
		long order_total = 0;
		if (map_user == null) {
			logger.error("-------用户登录集合为空--------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_USE_NO_EXIST, "用户登录集合为空");
			return order_total;
		}
		// 从用户登录集合中查询到当前用户的登录信息
		PCUserDto pcuser = map_user.get(mt4_id);

		if (pcuser == null) {
			logger.error("-------用户登录信息不存在--------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_USE_NO_EXIST, "用户登录信息不存在");
			return order_total;
		}

		// 获取这个mt4id对应用户余额，需要每次从用户登录表中查询余额保证用户余额的实时性
		long blance = getBlance(mt4_id);
		if (resultMessageDto.getResultNum() == 500) {
			return order_total;
		}
		pcuser.setBlance(blance);
		// 获取当前这个订单的订单量,mt4每次都会传过来这个订单的数量
		order_total = orderResultDto.getTotal();
		// 如果total=0,返回错误
		if (order_total == 0) {
			logger.error("------------历史订单拼装数据total为0-------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_ORDER_NO_EXIST,
					"历史订单拼装数据total为0");
			return order_total;
		}

		// 计算当前的余额
		long thisblance = pcuser.getBlance() + orderResultDto.getProfit() * 100;
		pcuser.setBlance(thisblance);
		// 计算当前的订单余额
		if (pcuser.getOrderBlance() < order_total * 100) {
			logger.error("------获取历史订单错误：订单量小于0-----------");
			logger.error("------pcuser.getOrderBlance():"
					+ pcuser.getOrderBlance() + "----,order_total*100:"
					+ order_total * 100 + "-----,thread"
					+ Thread.currentThread().getName() + "---------");
			pcuser.setOrderBlance(0);
		} else {
			pcuser.setOrderBlance(pcuser.getOrderBlance() - order_total * 100);
		}
		// 把最新的余额更新到数据库
		IUserLoginStateService userLoginStateService = SocketUtils
				.getUserLoginStateService();
		userLoginStateDto.setBlance(thisblance);
		userLoginStateDto.setMt4Id(mt4_id);
		userLoginStateService.updateBlance(userLoginStateDto);
		return order_total;

	}

	// 获取blance
	public long getBlance(Long mt4_id) {
		PCUserDto pcUserDto2 = new PCUserDto();
		pcUserDto2.setMt4Id(mt4_id);
		long blance = new UserBlanceAction().getUserBlance(pcUserDto2,
				resultMessageDto);
		return blance;
	}

	// 为每个socket发信息
	public void sendPcClient(Socket user_socket, String to_pc_msg) {
		logger.error("发消息：socket=" + user_socket + ",内容=" + to_pc_msg);
		if (user_socket != null && !user_socket.isClosed()) {
			try {
				Writer writer = new PrintWriter(new OutputStreamWriter(
						user_socket.getOutputStream(),
						KlineConstants.ENCODEING_TYPE));
				System.out.println("send_to_pc_order_result=" + to_pc_msg);
				writer.write(to_pc_msg);
				writer.flush();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_USE_NO_EXIST, "用户登录信息不存在");
			return;
		}
	}

}
