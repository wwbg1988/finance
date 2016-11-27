package com.blackbeard.socket.server.kline;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;

public class SendKlineSocketToUserThread implements Runnable {
	// 新开线程，每有一条text单独起一条线程推送这条

	private static final Logger logger = Logger
			.getLogger(SendKlineSocketToUserThread.class);

	private TextMessage message;

	public SendKlineSocketToUserThread(TextMessage message) {
		this.message = message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		List<Socket> allusersocket = KlineConstants.SOCKET_ALLUSER;
		// logger.error("当前的allusersocket.size="+allusersocket.size());
		// 遍历allusersocket，为每一个socket发送
		try {
			logger.info("-------------获取socket写入对象------------");
			// logger.error("mqtext="
			// +
			// message.getText()+"allusersocket.size="+allusersocket.size()+",thread="+Thread.currentThread().getName());
			if (!CollectionUtils.isEmpty(allusersocket)) {
				////迭代时，阻塞其他线程调用add或remove等方法修改元素
				synchronized (allusersocket) {
					Iterator<Socket> iteall = allusersocket.iterator();
					while (iteall.hasNext()) {
						Socket thisAllSocket = iteall.next();
						if (thisAllSocket == null || thisAllSocket.isClosed()) {
							logger.error("-------------thisAllSocket is null------------");
						} else {
							Writer writer = new PrintWriter(
									new OutputStreamWriter(
											thisAllSocket.getOutputStream(),
											"utf-8"));
							writer.write(message.getText());
							writer.flush();
							writer = null;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
