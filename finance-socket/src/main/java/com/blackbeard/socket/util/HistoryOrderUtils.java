package com.blackbeard.socket.util;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.constant.StatusConstants;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.service.IUserLoginStateService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.service.HistoryOrderHandleService;
import com.blackbeard.util.orderUtil.OrderResultPcMessageInfo;

/**
 * 历史订单工具类
 * 
 * @author 刘博
 *
 */
@Component
public class HistoryOrderUtils {

	private static final Logger logger = Logger
			.getLogger(HistoryOrderUtils.class);

	/**
	 * 查询盈亏价格
	 * 
	 * @param orderDto
	 * @param closePrice
	 * @return profit
	 */
	public static float findProfit(NextOrderDto orderDto, double closePrice) {
		float profit = 0;
		// 盈利率
		float profitRatio = KlineConstants.NEXT_ORDER_PROFIT_RATIO * 100;
		double orderPrice = Double.valueOf(orderDto.getPrice());
		switch (orderDto.getDirection()) {
		// 规则: 0买,1卖
		// 如果是买：买入价格<K线价格,赚,买入价格>K线价格,亏;
		// 如果是卖: 卖出价格<K线价格,亏,卖出价格>K线价格,赚;
		case 1:
			if (orderPrice - closePrice < 0) {// 亏损
				profit = Float.parseFloat("-" + orderDto.getTotal());
			} else if (orderPrice - closePrice > 0) {// 盈利
				profit = profitRatio * orderDto.getTotal() / 100;
			}
			break;
		case 0:
			if (orderPrice - closePrice < 0) {// 盈利
				profit = profitRatio * orderDto.getTotal() / 100;
			} else if (orderPrice - closePrice > 0) {// 亏损
				profit = Float.parseFloat("-" + orderDto.getTotal());
			}
			break;
		default:
			break;
		}
		return profit;
	}

	/**
	 * 根据历史订单对象组装信息
	 * 
	 * @param historyOrderDto
	 * @return String
	 */
	public static String getOrderMessage(NextOrderDto nextOrderDto) {
		// 开盘时间
		String openTime = nextOrderDto.getOpenTime().substring(0, 10);
		// 结束时间
		String closeTime = nextOrderDto.getCloseTime().substring(0, 10);
		// 内容
		String content = new StringBuffer()
				.append(nextOrderDto.getCurrencyType()).append(",")
				.append(nextOrderDto.getClosePrice()).append(",")
				.append(closeTime).append(",")
				.append(nextOrderDto.getDirection()).append(",")
				.append(nextOrderDto.getLoginId()).append(",")
				.append(nextOrderDto.getPrice()).append(",").append(openTime)
				.append(",").append(nextOrderDto.getProfit()).append(",")
				.append(nextOrderDto.getTotal()).append(",")
				.append(nextOrderDto.getTick()).toString();
		// 头消息
		String head = new OrderResultPcMessageInfo().getHandleStr(content
				.length());
		System.out.println("返回历史订单数据---" + "[" + head + content + "]");
		return "[" + head + content + "]";
	}

	/**
	 * 更新用户余额和订单量
	 * 
	 * @param orderDto
	 * @param profit
	 */
	public static void updateUserBlanceAndOrderTotal(NextOrderDto orderDto,
			float profit) {
		// 计算当前的余额
		Map<Long, PCUserDto> mapUserLogin = KlineConstants.mapUserLogin;
		PCUserDto pcmapuser = mapUserLogin.get(orderDto.getLoginId());
		long thisblance = 0;
		if (!StringUtils.isEmpty(String.valueOf(pcmapuser.getBlance()))) {
			thisblance = (long) (pcmapuser.getBlance() + profit * 100);
			pcmapuser.setBlance(thisblance);
			// 把最新的余额更新到数据库
			IUserLoginStateService userLoginStateService = SocketUtils
					.getUserLoginStateService();
			StatusConstants.userLoginStateDto.setBlance(thisblance);
			StatusConstants.userLoginStateDto.setMt4Id(orderDto.getLoginId());
			userLoginStateService
					.updateBlance(StatusConstants.userLoginStateDto);
			// 计算当前的订单余额
			if (pcmapuser.getOrderBlance() < orderDto.getTotal() * 100) {
				logger.error("------获取历史订单错误：订单量小于0-----------");
				logger.error("------pcuser.getOrderBlance():"
						+ pcmapuser.getOrderBlance() + "----,order_total*100:"
						+ orderDto.getTotal() * 100 + "-----,thread"
						+ Thread.currentThread().getName() + "---------");
				pcmapuser.setOrderBlance(0);
			} else {
				pcmapuser.setOrderBlance(pcmapuser.getOrderBlance()
						- orderDto.getTotal() * 100);
			}
		}
	}
}
