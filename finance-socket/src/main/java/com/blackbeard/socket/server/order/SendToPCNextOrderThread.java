package com.blackbeard.socket.server.order;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.util.orderUtil.BaseMessageInfo;

public class SendToPCNextOrderThread implements Runnable{

	private static final Log logger = LogFactory
			.getLog(SendToPCNextOrderThread.class);
	
	private Socket user_socket;
	private String to_pc_msg;
	
	public SendToPCNextOrderThread(Socket user_socket,String to_pc_msg){
		this.user_socket=user_socket;
		this.to_pc_msg = to_pc_msg;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		logger.error("发消息：socket=" + user_socket + ",内容=" + to_pc_msg);
		if (user_socket != null && !user_socket.isClosed()) {
			try {
				Writer writer = new PrintWriter(new OutputStreamWriter(
						user_socket.getOutputStream(),
						KlineConstants.ENCODEING_TYPE));
				logger.error("send_to_pc_order_result=" + to_pc_msg +",thread="+Thread.currentThread().getName());
			    //base64加密
				writer.write( BaseMessageInfo.getBase64Str(to_pc_msg));
				writer.flush();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	
	}

}
