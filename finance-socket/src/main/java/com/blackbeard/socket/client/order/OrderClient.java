package com.blackbeard.socket.client.order;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.OrderClientDto;
import com.blackbeard.util.orderUtil.SendOrderAction;


public class OrderClient {
    //短连接 ，  获取mt4下订单信息
	public static final Object locked = new Object();
	public static final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(
			1024 * 100);
    
    private static final Logger logger = Logger.getLogger(SendOrderAction.class);
    
	public static OrderClientDto calling(OrderClientDto orderClientDto) {
		try {
			Socket socket = new Socket(KlineConstants.MT4_NEXT_ORDER_ADDRESS, KlineConstants.MT4__NEXT_ORDER_PORT);
			ExecutorService pool = Executors.newFixedThreadPool(100);
			Callable c1 = new OrderClientSendCall(orderClientDto, socket);
			Future f1 = pool.submit(c1);
			orderClientDto = (OrderClientDto) f1.get();
			pool.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("-------------获取下订单结果失败-------------");
			e.printStackTrace();
		}
		return orderClientDto;

	}

	
	
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		String ids = "{\"loginId\":18030158,\"currencyType\":\"EURUSD\","
				+ "\"price\":1.1117,\"total\":100,\"direction\":1,\"comment\":\"UP1min\"}";
		OrderClientDto orderClientDto = new OrderClientDto();
		orderClientDto.setPc_to_mt4_message(ids);
		System.out.println("pc_to_mt4=" + ids);
		orderClientDto = calling(orderClientDto);
		System.out.println("mt4_to_pc=" + orderClientDto.getMt4_to_pc_message());
	}

}
