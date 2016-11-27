package com.blackbeard.socket.server.order;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.socket.service.ActivemqService;

public class HistoryOrderSendPCThread extends Thread{
	
	protected static final Log logger = LogFactory
			.getLog(HistoryOrderSendPCThread.class);

	private TextMessage message = null;
	
	public void run(){		
		logger.debug("-------------orderhistory创建mq链接-----------");
		try {
			Session session; // 会话 接受或者发送消息的线程
			Destination destination; // 消息的目的地
			MessageConsumer messageConsumer; // 消息的消费者
			ConnectionFactory connectionFactory = ActivemqService
					.getMyActiveMQConnectionFactory();
			Connection connection = connectionFactory.createConnection(); // 打开MQ连接
			connection.start(); // 启动连接
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE); // 创建Session
			// destination=session.createQueue("FirstQueue1"); // 创建连接的消息队列
			destination = session
					.createTopic(KlineConstants.HISTORY_ORDER_QUEUE_NAME);
			messageConsumer = session.createConsumer(destination); // 创建消息消费者
			Map<Long, PCUserDto> mapUserLogin ;
			PCUserDto pcuser;
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);  //创建线程池,设置线程的最大值20
			while (true) {
				message = (TextMessage) messageConsumer.receive(0);
				logger.error("--message----------=" + message+",thread="+Thread.currentThread().getName());
				if (message != null) {
					String text = message.getText();
					logger.error("--history_text----------=" + text+",thread="+Thread.currentThread().getName());
					//分发给该用户的所有客户端
					String[] texts = text.split(",");
					String mt4id = texts[4];
					// 开始为每个socket分发消息
					mapUserLogin = KlineConstants.mapUserLogin;
					pcuser = mapUserLogin.get(Long.valueOf(mt4id) );
					if (pcuser != null) {
						List<Socket> sockets = pcuser.getListSockets();
						if (!CollectionUtils.isEmpty(sockets)) {
							for (Socket user_socket : sockets) {
							//	sendPcClient(user_socket, text);
								//为每个用户发送下订单信息使用多线程发送
								fixedThreadPool.execute(new SendToPCNextOrderThread(user_socket, text));
							}
						}
						logger.error("to_pc_msg=" + text);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 为每个socket发信息
//		public void sendPcClient(Socket user_socket, String to_pc_msg) {
//			logger.error("发消息：socket=" + user_socket + ",内容=" + to_pc_msg);
//			if (user_socket != null && !user_socket.isClosed()) {
//				try {
//					Writer writer = new PrintWriter(new OutputStreamWriter(
//							user_socket.getOutputStream(),
//							KlineConstants.ENCODEING_TYPE));
//					System.out.println("send_to_pc_order_result=" + to_pc_msg);
//					writer.write(to_pc_msg);
//					writer.flush();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} 
//		}
	

}
