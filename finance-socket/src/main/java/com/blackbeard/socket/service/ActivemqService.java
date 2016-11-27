package com.blackbeard.socket.service;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.blackbeard.common.constant.KlineConstants;

/**
 * activemq工具类
 * 
 * @author 刘博
 *
 */
@Component
public class ActivemqService {

	private static MessageProducer producer;// 生产者

	private static MessageProducer orderProducer;// 订单生产者

	private static Destination orderDestination; // 订单消息的目的地

	private static Session orderSession; // (订单)会话 接受或者发送消息的线程

	private static Session session; // 会话 接受或者发送消息的线程

	private static Destination destination; // 消息的目的地

	private static ConnectionFactory connectionFactory;

	public static Connection connection = null;// 连接
	public static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
	public static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
	public static final String BROKEURL = "tcp://0.0.0.0:61616?wireFormat.maxInactivityDuration=0"; // 默认的连接地址

	private static final Logger logger = Logger
			.getLogger(ActivemqService.class);

	// 初始化生产者

	public void initServerActiveObject() {
		// 实例化连接工厂
		try {
			logger.info("-=============-重连MT4:-然后初始化activemq数据-====================-");
			logger.info("-=============-重连MT4:-然后初始化activemq数据-====================-");
			logger.info("-=============-重连MT4:-然后初始化activemq数据-====================-");
			// 初始化连接
			getMyActiveMQConnection();
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE); // 创建Session
			// K线目的地
			destination = session.createTopic(KlineConstants.KLINE_TOPIC_NAME);
			// K线生产者
			producer = session.createProducer(destination); // 创建消息生产者
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // 设置不持久化

			// ===============订单对象开始============================
			orderSession = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE); // 创建Session
			// 历史订单目的地
			orderDestination = orderSession
					.createTopic(KlineConstants.HISTORY_ORDER_QUEUE_NAME);
			// 历史订单生产者
			orderProducer = orderSession.createProducer(orderDestination);
			// 历史订单设置不持久化
			orderProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ConnectionFactory getMyActiveMQConnectionFactory() {
		if (null == connectionFactory) {
			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, BROKEURL);
		}
		return connectionFactory;
	}

	// 获取mq连接工厂对象
	public static Connection getMyActiveMQConnection() {
		getMyActiveMQConnectionFactory();
		if (null == connection) {
			try {
				connection = connectionFactory.createConnection();
				connection.start(); // 启动连接
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 通过连接工厂获取连接
		}
		return connection;
	}

	// 初始化客户端需要的MQ对象
	public static Map<String, Object> initClientActiveObject() {
		Map<String, Object> activeMap = new HashMap<String, Object>();

		// 消费者，消息接收者
		MessageConsumer consumer;
		Session session;

		Destination destination;
		try {
			// 打开MQ连接
			getMyActiveMQConnection();
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE); // 创建Session
			destination = session.createTopic(KlineConstants.KLINE_TOPIC_NAME);
			consumer = session.createConsumer(destination);
			if (consumer != null) {
				activeMap.put("consumer", consumer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activeMap;
	}

	// 生产者发送消息
	public static void sendMessage(String text) {
		try {
			producer.send(session.createTextMessage(text));
//			logger.info("--messageProducer--=" + producer);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
//			logger.info("ActivemqService MQ存入异常:" + e.getMessage());
		}
	}

	// 历史订单生产者发送消息
	public static void orderQueueSendMessage(String text) {
		try {
			orderProducer.send(orderSession.createTextMessage(text));
			orderSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ActivemqService orderQueueSendMessage存入异常:"
					+ e.getMessage());
		}
	}

	// 消费者获取消息
	public static String getMessage(String topicName,
			Map<String, Object> activeMap) {
		// 消费者，消息接收者
		MessageConsumer consumer = null;
		try {
			consumer = (MessageConsumer) activeMap.get("consumer");
			// 设置接收者接收消息的时间，为了便于测试，这里谁定为100s
			TextMessage message = (TextMessage) consumer.receive(0);
			if (null != message) {
				System.out.println("收到消息" + message.getText());
				return message.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
