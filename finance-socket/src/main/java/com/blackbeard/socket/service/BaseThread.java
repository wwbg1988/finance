package com.blackbeard.socket.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.blackbeard.socket.server.PCServer;
import com.blackbeard.socket.server.kline.SendKlineToUserThread;
import com.blackbeard.socket.server.order.HistoryOrderHandleThread;
import com.blackbeard.socket.server.order.HistoryOrderSendPCThread;
import com.blackbeard.util.PropertiesUtils;

@Component
public abstract class BaseThread extends Thread {
	
	protected static final Log logger = LogFactory.getLog(BaseThread.class);
	// 线程运行方法
	public abstract void run();

	/**
	 * 启动服务端的所有线程
	 */
	public void startServerThread() {
		logger.debug("---------------startServerThread线程开始---------------------");
		// 服务端推送k线线程
		new SendKlineToUserThread().start();
		// 服务端处理结单数据
		new HistoryOrderHandleThread().start();
		// 服务端推送历史订单线程
		new HistoryOrderSendPCThread().start();
		logger.debug("---------------startServerThread线程结束---------------------");
	}

}
