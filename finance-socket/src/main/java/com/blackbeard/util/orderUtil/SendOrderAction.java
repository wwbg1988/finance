package com.blackbeard.util.orderUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.Mt4DownOrderDto;
import com.blackbeard.common.dto.Mt4OrderDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.OrderClientDto;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.service.IUserLoginStateService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.client.order.OrderClient;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssic.util.StringUtils;

public class SendOrderAction {

	// 下订单

	// 用户下订单 1,将pc的订单数据解析转为mt4的订单数据 2,发送到mt4获取到订单返回数据 3,将mt4订单返回数据解析为pc订单数据
	private static final Logger logger = Logger
			.getLogger(SendOrderAction.class);

	public static void pcUserOrder(String body, Socket socket, PCUserDto pcUserDto,
			ResultMessageDto resultMessageDto) {

		if (StringUtils.isEmpty(body)) {
			logger.error("----------body不能为空-----------");
			resultMessageDto.setResultNum(500);
			resultMessageDto.setResultMessage("body不能为空");
			resultMessageDto
					.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
			return ;
		}
		if (!body.contains(",")) {
			logger.error("-----------订单数据字符错误------------");
			resultMessageDto.setResultNum(500);
			resultMessageDto.setResultMessage("订单数据字符错误");
			resultMessageDto
					.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
			return ;
		}
		long blance = new UserBlanceAction().getUserBlance(pcUserDto,
				resultMessageDto);

		if (resultMessageDto.getResultNum() == 500) {
			return ;
		}

		pcUserDto.setBlance(blance);
		System.out.println("当前余额：" + pcUserDto.getBlance());
		// 将pc的订单数据解析转为mt4的订单数据
		getToMt4OrderMsg(body, pcUserDto, resultMessageDto);
		String to_mt4_order_msg = "";
		if (resultMessageDto.getResultNum() == 200) {
			to_mt4_order_msg = (String) resultMessageDto.getBody();
		} else {
			return ;
		}
		// 发送到mt4获取到订单返回数据
		System.out.println("pc_order=" + to_mt4_order_msg);

		if (StringUtils.isEmpty(to_mt4_order_msg)) {
			logger.error("----------发送mt4订单数据为空-----------");
			resultMessageDto.setResultNum(500);
			resultMessageDto.setResultMessage("发送mt4订单数据为空");
			resultMessageDto
					.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
			return ;
		} else {
			OrderClientDto orderClientDto = new OrderClientDto();
			orderClientDto.setPc_to_mt4_message(to_mt4_order_msg);

			orderClientDto = OrderClient.calling(orderClientDto);
			System.out.println("mt4_to_pc="
					+ orderClientDto.getMt4_to_pc_message());
			// 将mt4订单返回数据解析为pc订单数据
			// {"comment":"UP5min","currencyType":"EURUSD","direction":1,"loginId":18030158,"price":1.129740,"tick":71,"time":1467347591,"total":100}
			// 将mt4订单返回数据解析为pc订单数据
			if (StringUtils.isEmpty(orderClientDto.getMt4_to_pc_message())) {
				logger.error("--------------发送PC订单数据为空---------------");
				resultMessageDto.setResultNum(500);
				resultMessageDto.setResultMessage("发送mt4订单数据为空");
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return ;
			} else {
				String to_pc_order_msg = getToPCOrderMsg(
						orderClientDto.getMt4_to_pc_message(), pcUserDto,
						resultMessageDto);
				if (StringUtils.isEmpty(to_pc_order_msg)) {
					return ;
				} else {
					resultMessageDto.setBody(to_pc_order_msg);
					resultMessageDto.setResultNum(200);
					return ;
				}
			}
		}
	}

	public static void getToMt4OrderMsg(String body, PCUserDto pcUserDto,
			ResultMessageDto resultMessageDto) {
		// get pc : EURUSDDB,1.12974,10000,1,up,5
		// to mt4 :
		// 00100120{"loginId":"170","currencyType":"EURUSDDB","price":"1.12974","total":"10000","direction":"1","comment":"UP1min"}
		String result_msg = "";
		String[] orders = body.split(",");
		if (orders != null && orders.length == 6) {
			Mt4OrderDto mt4OrderDto = new Mt4OrderDto();
			String order_total = orders[2];
			String order_direction = orders[3];
			String order_buyorsale = orders[4];
			String order_min_no = orders[5];
			// 对pc的订单数据校验
			if (!StringUtils.isNumeric(order_total)) {
				logger.error("--------resolve pc order error:total必须是数字------");
				resultMessageDto.setResultMessage("total必须是数字");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if (StringUtils.isEmpty(orders[1])) {
				logger.error("--------resolve pc order error:价格不能为空------");
				resultMessageDto.setResultMessage("价格不能为空");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if (!"1".equals(order_direction) && !"0".equals(order_direction)) {
				logger.error("--------resolve pc order error:direction必须是1或0------");
				resultMessageDto.setResultMessage("direction必须是1或0");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if (!"B".equals(order_buyorsale) && !"S".equals(order_buyorsale)) {
				logger.error("--------resolve pc order error:buyorsale必须是B或S------");
				resultMessageDto.setResultMessage("buyorsale必须是B或S");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if ("1".equals(order_direction) && "B".equals(order_buyorsale)
					|| "0".equals(order_direction)
					&& "S".equals(order_buyorsale)) {
				logger.error("--------resolve pc order error:buyorsale和direction不统一------");
				resultMessageDto.setResultMessage("buyorsale和direction不统一");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if (!"1".equals(order_min_no) && !"5".equals(order_min_no)
					&& !"15".equals(order_min_no)) {
				logger.error("--------resolve pc order error:order_min_no必须是1或5或15------");
				resultMessageDto.setResultMessage("order_min_no必须是1或5或15");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if (!StringUtils.isNumeric(order_min_no)) {
				logger.error("--------resolve pc order error:分钟数必须是数字------");
				resultMessageDto.setResultMessage("分钟数必须是数字");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
				return;
			}
			if (pcUserDto.getBlance() < 0) {
				logger.error("------------resolve pc order error:用户余额不能为负--------------");
				resultMessageDto.setResultMessage("用户余额不能为负");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_BLANCE_SHORT);
				return;
			}
			// 如果余额小于订单金额+ total*100 不能下单
			// 必须查询当前用户登录信息的余额
			Map<Long, PCUserDto> pcmap = KlineConstants.mapUserLogin;
			PCUserDto pcall = pcmap.get(pcUserDto.getMt4Id());
			if (pcall == null) {
				logger.error("------------resolve pc order error:用户登录集合为空--------------");
				resultMessageDto.setResultMessage("用户登录集合为空");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_USE_NO_EXIST);
				return;
			}

			if (pcUserDto.getBlance() < (Long.valueOf(order_total) * 100 + pcall
					.getOrderBlance())) {
				logger.error("----------------当前的订单下单数量="
						+ Long.valueOf(order_total) * 100);
				logger.error("----------------当前的订单金额="
						+ pcUserDto.getOrderBlance());
				logger.error("----------------当前的用户余额=" + pcUserDto.getBlance());
				logger.error("--------resolve pc order error:total不能大于blance------");
				resultMessageDto.setResultMessage("total不能大于blance");
				resultMessageDto.setResultNum(500);
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_BLANCE_SHORT);
				return;
			}
			// System.out.println("user.blance=" + pcUserDto.getBlance());

			mt4OrderDto.setLoginId(pcUserDto.getMt4Id());
			mt4OrderDto.setCurrencyType(orders[0]);
			mt4OrderDto.setPrice(Double.valueOf(orders[1]));
			mt4OrderDto.setTotal(Long.valueOf(order_total));
			mt4OrderDto.setDirection(Integer.valueOf(order_direction));
			mt4OrderDto.setComment(order_buyorsale + "." + order_min_no + "."
					+ "min");
			result_msg = Mt4OrderMessageInfo
					.sendBt4OrderHandleBody(mt4OrderDto);
			resultMessageDto.setResultNum(200);
			resultMessageDto.setBody(result_msg);
		} else {
			result_msg = "";
			logger.error("-------------订单解析对象不够--------------");
			resultMessageDto.setBody("订单解析对象不够");
			resultMessageDto.setResultNum(500);
			resultMessageDto
					.setResultCode(KlineConstants.RESULT_ERROR_RESOLVE_ERROR);
			return;
		}
	}

	// public static void main(String[] args) {
	// PCUserDto pcUserDto = new PCUserDto();
	// pcUserDto.setMt4Id(1234);
	// String body = "EURUSDDB,1.12974,100,1,up,5";
	// String str = getToMt4OrderMsg(body, pcUserDto);
	// System.out.println("str=" + str);
	// }

	@Transactional
	public static String getToPCOrderMsg(String mt4OrderMsg,
			PCUserDto pcUserDto, ResultMessageDto resultMessageDto) {
		// {"comment":"UP5min","currencyType":"EURUSD","direction":1,"loginId":18030158,"price":1.129740,"tick":71,"time":1467347591,"total":100}
		String to_pc_order_msg = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Mt4DownOrderDto mt4DownOrderDto = objectMapper.readValue(
					mt4OrderMsg, Mt4DownOrderDto.class);
			// 获取登录集合，把订单余额set订单集合中
			Map<Long, PCUserDto> pcusermap = KlineConstants.mapUserLogin;
			PCUserDto pcumap = pcusermap.get(pcUserDto.getMt4Id());
			if (pcumap == null) {
				logger.error("------------该用户订单集合不存在----------------");
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_NEXT_ORDER_ERROR);
				resultMessageDto.setResultMessage("该用户订单集合不存在");
				resultMessageDto.setResultNum(500);
				return "";
			}
			// [224310101500042EURUSD,1.12974,100,1,UP,5]
			// price /10000
			if (!StringUtils.isEmpty(mt4DownOrderDto.getPrice())
					&& !"0".equals(mt4DownOrderDto.getPrice())) {
				double price = Double.valueOf(mt4DownOrderDto.getPrice()) / 100000;
				mt4DownOrderDto.setPrice(String.valueOf(price));
			} else {
				mt4DownOrderDto.setPrice("0");
			}
			// mt4返回的订单编号不为-1则表示下单成功
			if (mt4DownOrderDto != null) {
				if (mt4DownOrderDto.getTick() != -1
						&& mt4DownOrderDto.getTick() != 0) {
					// 插入下订单表
					INextOrderService nextOrderService = SocketUtils
							.getNextOrderService();
					NextOrderDto nextOrderDto = new NextOrderDto();
					nextOrderDto.setComment(mt4DownOrderDto.getComment());
					nextOrderDto.setCurrencyType(mt4DownOrderDto
							.getCurrencyType());
					nextOrderDto.setDirection(mt4DownOrderDto.getDirection());
					nextOrderDto.setLoginId(mt4DownOrderDto.getLoginId());

					nextOrderDto.setPrice(mt4DownOrderDto.getPrice());
					nextOrderDto.setTick(mt4DownOrderDto.getTick());
					nextOrderDto.setOpenTime(mt4DownOrderDto.getTime());
					String comment = mt4DownOrderDto.getComment();
					String[] commentArray = comment.split("\\.");
					Long closeTime = Long.valueOf(commentArray[1]) * 1000 * 60;
					closeTime += System.currentTimeMillis();
					// 下单结束时间：取当前系统时间戳
					nextOrderDto.setCloseTime(String.valueOf(closeTime));
					nextOrderDto.setTotal(mt4DownOrderDto.getTotal());
					nextOrderDto.setCreateTime(new Date());
					nextOrderService.addNextOrder(nextOrderDto);

					// 更新当前用户的订单余额
					if (pcumap.getOrderBlance() == 0) {
						// pcUserDto.setOrderBlance(mt4DownOrderDto.getTotal()*100);
						pcumap.setOrderBlance(mt4DownOrderDto.getTotal() * 100);
					} else {
						// pcUserDto.setOrderBlance(pcumap.getOrderBlance()+mt4DownOrderDto.getTotal()*100);
						pcumap.setOrderBlance(pcumap.getOrderBlance()
								+ mt4DownOrderDto.getTotal() * 100);
						// pcumap.setOrderBlance(pcumap.getOrderBlance()+mt4DownOrderDto.getTotal()*100);
					}
				} else {
					logger.error("--this.tick="+mt4DownOrderDto.getTick()+"---,mt4str="+mt4OrderMsg+"-------订单号为0或-1----------------");
					resultMessageDto
							.setResultCode(KlineConstants.RESULT_ERROR_NEXT_ORDER_ERROR);
					resultMessageDto.setResultMessage("订单号为0或-1");
					resultMessageDto.setResultNum(500);
					return "";
				}
			} else {
				logger.error("------------mt4下订单数据为空------------");
				resultMessageDto
						.setResultCode(KlineConstants.RESULT_ERROR_NEXT_ORDER_ERROR);
				resultMessageDto.setResultMessage("mt4下订单数据为空");
				resultMessageDto.setResultNum(500);
				return "";
			}
			// 组装数据
			GroupToPcDownOrderInfo groupToPcDownOrderInfo = new GroupToPcDownOrderInfo();
			to_pc_order_msg = groupToPcDownOrderInfo
					.groupHandleBody(mt4DownOrderDto);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return to_pc_order_msg;
	}
}
