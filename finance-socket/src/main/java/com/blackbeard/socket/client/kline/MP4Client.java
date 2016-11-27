package com.blackbeard.socket.client.kline;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackbeard.common.constant.KlineConstants;

@Component
public class MP4Client {

	@Autowired
	private ReceiveThread receiveThread;
	protected static final Log logger = LogFactory.getLog(MP4Client.class);

	public void start() throws UnknownHostException, IOException {
		Socket socket = new Socket(KlineConstants.MP4_KLINE_ADDRESS,
				KlineConstants.MP4_KLINE_PORT);
		new SendThread(socket).start();
		receiveThread.setSocket(socket);
		receiveThread.start();

	}

}
