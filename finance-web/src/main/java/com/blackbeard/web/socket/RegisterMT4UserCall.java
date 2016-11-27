package com.blackbeard.web.socket;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.concurrent.Callable;

public class RegisterMT4UserCall implements Callable {

	private String paramString;

	private Socket socket;

	private String message = "";

	public RegisterMT4UserCall(String paramString, Socket socket) {
		this.paramString = paramString;
		this.socket = socket;
	}

	@Override
	public Object call() throws Exception {
		try {

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			pw.write(paramString);
			pw.flush();
			Reader reader = new InputStreamReader(socket.getInputStream());
			CharBuffer charBuffer = CharBuffer.allocate(8192);
			if ((reader.read(charBuffer)) != -1) {
				charBuffer.flip();
				message += charBuffer.toString();
				System.out.println("注册用户MT4Config返回的消息:=======" + message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

}
