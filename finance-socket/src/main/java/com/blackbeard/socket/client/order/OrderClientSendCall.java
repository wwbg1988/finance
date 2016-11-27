package com.blackbeard.socket.client.order;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.concurrent.Callable;

import com.blackbeard.common.dto.OrderClientDto;

public class OrderClientSendCall implements Callable{

	private OrderClientDto orderClientDto;
	
	private Socket socket;
	
	private String temp = "";
	
	public OrderClientSendCall(OrderClientDto orderClientDto,Socket socket){
		this.orderClientDto = orderClientDto;
		this.socket = socket;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		try {
			String send = orderClientDto.getPc_to_mt4_message();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			pw.write(send);
			pw.flush();
			Reader reader = new InputStreamReader(socket.getInputStream());
			CharBuffer charBuffer = CharBuffer.allocate(8192);
			if ((reader.read(charBuffer)) != -1) {
				charBuffer.flip();
				temp += charBuffer.toString();
				if (temp.indexOf("{") != -1 && temp.indexOf("}") != -1) {
					// System.out.println("temp="+temp);
					temp = temp.replace("\n", "");
					orderClientDto.setMt4_to_pc_message(temp);
					temp = "";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderClientDto;
	}

}
