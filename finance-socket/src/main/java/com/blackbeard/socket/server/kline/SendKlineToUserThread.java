package com.blackbeard.socket.server.kline;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.socket.service.ActivemqService;
import com.blackbeard.socket.service.BaseThread;

public class SendKlineToUserThread extends BaseThread {

	private static final Logger logger = Logger
			.getLogger(SendKlineToUserThread.class);

	public static final String BROKEURL = "tcp://0.0.0.0:61616?wireFormat.maxInactivityDuration=0"; // 默认的连接地址
	private static Connection connection = null; // 连接

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Session session; // 会话 接受或者发送消息的线程
		Destination destination; // 消息的目的地
		MessageConsumer messageConsumer; // 消息的消费者
		try {
			ConnectionFactory connectionFactory = ActivemqService
					.getMyActiveMQConnectionFactory();
			logger.debug("-------------创建mq链接-----------");
			connection = connectionFactory.createConnection(); // 打开MQ连接
			connection.start(); // 启动连接
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE); // 创建Session
			destination = session.createTopic(KlineConstants.KLINE_TOPIC_NAME);
			messageConsumer = session.createConsumer(destination); // 创建消息消费者
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);  //创建线程池,设置线程的最大值20
			while (true) {
				TextMessage	message = (TextMessage) messageConsumer.receive(0);
				// logger.info("--------------------获取mq对象成功----------------------");
				if (message != null) {
					fixedThreadPool.execute(new SendKlineSocketToUserThread(message));
				}
			}

		} catch (Exception e) {
			logger.info("SendKlineToUserThread MQ发送异常:" + e.getMessage());
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException eco) {
					eco.printStackTrace();
				}
			}
			e.printStackTrace();
		}finally{
			//如果推出死循环，重新连接
			logger.error("--------------发送k线失败重新连接-------------");
			new SendKlineToUserThread().start();
		}

	}

}
