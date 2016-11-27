package com.blackbeard.socket.server.order;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.service.BaseThread;
import com.blackbeard.socket.service.HistoryOrderHandleService;

/**
 * 历史订单发送线程： 死循环扫描临时订单表(下单表):t_next_order 如果订单存在,去跟k线的当前数据去比较，拼装一条历史订单数据结构，存入数据库
 * 发送历史数据到MQ队列
 * 
 * @author 刘博
 *
 */
public class HistoryOrderHandleThread extends BaseThread {

	private static final Logger logger = Logger
			.getLogger(HistoryOrderHandleThread.class);

	@Override
	public void run() {
		logger.debug("---------------HistoryOrderHandleThread线程开始---------------------");
		// 获取历史订单Service;
		INextOrderService nextOrderService = SocketUtils.getNextOrderService();
		try {
			NextOrderDto nextOrderDto = new NextOrderDto();
			List<NextOrderDto> listOrder = new ArrayList<NextOrderDto>();
			while (true) {
				Thread.sleep(1000);
				// 下单结束时间：取比当前系统时间 相等或小的订单即可
				nextOrderDto.setCloseTime(String.valueOf(System
						.currentTimeMillis()));
				listOrder = nextOrderService.findBy(nextOrderDto);
				if (CollectionUtils.isEmpty(listOrder)) {// 空则返回
					continue;
				}
				logger.error("---------收到订单结果，推送队列-----------");
				// 处理下单数据，保存数据库,然后推送到MQ队列中
				HistoryOrderHandleService.handleNextOrder(listOrder);
				// 清除集合对象
				listOrder.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("HistoryOrderSendThread 发送异常:" + e.getMessage());
		} finally {
			// 断线重连
			reConnnOrder();
		}
	}

	private void reConnnOrder() {
		new HistoryOrderHandleThread().start();
		logger.error("----HistoryOrderHandleThread 重连成功----");
	}
}
