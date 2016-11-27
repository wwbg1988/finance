package com.blackbeard.socket.client.kline;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.blackbeard.socket.service.BaseThread;

public class SendThread extends BaseThread {
	// 往mt4k线socket发送心跳数据
	private Socket socket;

	public SendThread(Socket socket) {
		this.socket = socket;

	}

	@Override
	public void run() {
		while (true) {
			String send;
			try {
				if (socket.isClosed()) {
					break;
				}
				send = getSend();
				PrintWriter pw;
				pw = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream(), "utf-8"));
				pw.write(send);
				pw.flush();
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}

	}

	public String getSend() throws InterruptedException {
		Thread.sleep(5000);
		return "<SOAP-ENV:Envelope>------" + System.currentTimeMillis()
				+ "</SOAP-ENV:Envelope>";

	}
}
