package com.blackbeard.listener;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.client.kline.MP4Client;
import com.blackbeard.socket.server.PCServer;
import com.blackbeard.socket.service.ActivemqService;
import com.blackbeard.socket.service.BaseThread;

/**
 * 这个类可以注入一些 dao 工具类 在 afterPropertiesSet 方法中执行数据库连接级别的操作 Author: 刘博 Created:
 * 2016-08-03 下午2:41
 */
public class InitOnLoadListener implements InitializingBean {

	@Autowired
	private ActivemqService activemqService;

	@Autowired
	private MP4Client mP4Client;

	public ActivemqService getActivemqService() {
		return activemqService;
	}

	public void setActivemqService(ActivemqService activemqService) {
		this.activemqService = activemqService;
	}

	public void close() {
	}

	public void setmP4Client(MP4Client mP4Client) {
		this.mP4Client = mP4Client;
	}

	public void afterPropertiesSet() throws Exception {
		// 服务器启动时初始化application.xml里的bean对象
		SocketUtils.initApplicationContext();
		// 服务器启动时初始化MQ对象
		activemqService.initServerActiveObject();
		new initClientThread().start(); // 客户端k线线程启动
		new initServerThread().start(); // 服务端用户登录，K线推送，历史订单推送线程启动

	}

	class initClientThread extends BaseThread {
		@Override
		public void run() {
			try {
				mP4Client.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class initServerThread extends BaseThread {
		@Override
		public void run() {
			try {
				// 启动服务端的线程:K线推送，历史订单推送
				startServerThread();
				new PCServer().start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}