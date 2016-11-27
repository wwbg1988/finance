package com.blackbeard.socket.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.blackbeard.common.constant.StatusConstants;
import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.util.HistoryOrderUtils;


/**
 * HistoryOrderHandleService:历史订单处理Serivce :处理历史订单相关的方法
 * 
 * @author 刘博
 *
 */
public class HistoryOrderHandleService {

	private static final Logger logger = Logger
			.getLogger(HistoryOrderHandleService.class);

	/**
	 * 处理下单数据 然后保存到数据库
	 * 
	 * @param listOrder
	 *            下单集合
	 * @return
	 */
	public static void handleNextOrder(List<NextOrderDto> listOrder) {
		// 获取13组货币k线数据
		List<KlineRecordDto> list = KlineHandleService.list;
		if (!CollectionUtils.isEmpty(list)) {
			for (NextOrderDto orderDto : listOrder) {// 每一个订单都在在13组K线数据里面循环判断，查找下单货币
				for (KlineRecordDto klineDto : list) {
					if (orderDto.getCurrencyType().equalsIgnoreCase(
							klineDto.getCurrency())) {// 如果存在下单货币
						// 插入历史订单到数据库
						insertHistoryOrder(klineDto.getClosePrice(),
								klineDto.getDecimals(), orderDto);
					}
				}
			}
		}
	}

	/**
	 * insertHistoryOrder:插入历史订单到数据库
	 * 
	 * @param klinePrice
	 *            K线结束价格
	 * @param decimals
	 *            K线小数位
	 * @param orderDto
	 *            订单对象
	 * @return String
	 */
	public static void insertHistoryOrder(String klinePrice, String decimals,
			NextOrderDto orderDto) {
		IHistoryOrderService historyOrderService = SocketUtils
				.getHistoryOrderService();
		float profit = 0;
		int decimal = Integer.valueOf(decimals);
		// 获取结束价格
		double closePrice = Double.valueOf(Integer.valueOf(klinePrice)
				/ Math.pow(10, decimal));
		// 获取盈亏价格
		profit = HistoryOrderUtils.findProfit(orderDto, closePrice);

		// 获取下单service
		INextOrderService nextOrderService = SocketUtils.getNextOrderService();
		// 删除下订单表中的这个订单
		StatusConstants.nextOrderDto.setTick(orderDto.getTick());
		nextOrderService.reduceNextOrder(StatusConstants.nextOrderDto);

		// 插入历史订单表
		orderDto.setClosePrice(String.valueOf(closePrice));
		orderDto.setProfit(profit);
		historyOrderService.inserByNextOrderDto(orderDto);

		// 更新用户余额和订单量
		HistoryOrderUtils.updateUserBlanceAndOrderTotal(orderDto, profit);

		// 然后发送数据到队列中
		ActivemqService.orderQueueSendMessage(HistoryOrderUtils
				.getOrderMessage(orderDto));
	}

}
