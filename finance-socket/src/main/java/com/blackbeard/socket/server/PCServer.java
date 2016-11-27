package com.blackbeard.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.socket.server.dto.PCServerDto;
import com.blackbeard.socket.server.order.PCServerSendUserInfoThread;

@Component
public class PCServer {

	protected static final Log logger = LogFactory.getLog(PCServer.class);

	public void start() throws IOException {
		logger.debug("启动的时候执行了PCServer：----------");
		ServerSocket serverSocket = new ServerSocket(
				KlineConstants.JAVA_SERVER_PORT);		
		while (true) {
			Socket socket = serverSocket.accept();
			System.out.println("#################socket1=" + socket);
			//服务端15秒超时
			socket.setSoTimeout(15000);
			PCUserDto pcUserDto = new PCUserDto(); // 当前的用户信息
			pcUserDto.setLoginFlag(0);
			// 服务端参数对象dto 把所有需要的参数都放入该对象中
			PCServerDto pcServerDto = new PCServerDto();
			pcServerDto.setSocket(socket);
			pcServerDto.setPcUserDto(pcUserDto);
			pcServerDto.setMapUserLogin(KlineConstants.mapUserLogin);
			pcServerDto.setBlanceIndex(0);
			logger.debug("------------读数据进程开始-------------------");
			// 读数据
			new PCServerReceiveThread(pcServerDto).start();
			logger.debug("---------------发送k线进程开始---------------------");
		    // 写数据 发送用户余额
			logger.debug("---------------发送余额进程开始-------------------");
			new PCServerSendUserInfoThread(pcServerDto).start();
		}
	}

	public static void main(String[] args) {
		try {
			new PCServer().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
