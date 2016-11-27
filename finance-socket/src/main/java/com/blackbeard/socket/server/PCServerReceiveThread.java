package com.blackbeard.socket.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Map;

import javax.tools.Tool;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.socket.server.dto.PCServerDto;
import com.blackbeard.socket.service.BaseThread;
import com.blackbeard.util.Tools;
import com.blackbeard.util.orderUtil.GroupErrorMessageInfo;
import com.blackbeard.util.orderUtil.LoginOutUserDetailAction;
import com.blackbeard.util.orderUtil.UserOperateAction;
import com.ssic.util.StringUtils;

public class PCServerReceiveThread extends BaseThread {

	private PCServerDto pcServerDto;
	private Map<Long, PCUserDto> mapUserLogin;
	private PCUserDto initPCUser;
	private Socket socket;

	public PCServerReceiveThread(PCServerDto pcServerDto) {
		this.pcServerDto = pcServerDto;
		this.mapUserLogin = pcServerDto.getMapUserLogin();
		this.initPCUser = pcServerDto.getPcUserDto();
		this.socket = pcServerDto.getSocket();
	}

	private static GroupErrorMessageInfo groupErrorMessageInfo = new GroupErrorMessageInfo();
	private static final Logger logger = Logger
			.getLogger(PCServerReceiveThread.class);

	@Override
	public void run() {
		try {
			UserOperateAction userOperateAction = new UserOperateAction(
					pcServerDto);
			String temp = "";
			if (!socket.isClosed()) {
				Reader reader = new InputStreamReader(socket.getInputStream(),
						KlineConstants.ENCODEING_TYPE);
				Writer writer = new PrintWriter(
						new OutputStreamWriter(socket.getOutputStream(),
								KlineConstants.ENCODEING_TYPE));
				CharBuffer charBuffer = CharBuffer.allocate(8192);
				while (reader.read(charBuffer) != -1) {
					pcServerDto.setBlanceIndex(0);
					charBuffer.flip();
					temp += charBuffer.toString();
					if (temp.indexOf("[") != -1 && temp.indexOf("]") != -1) {
						// 每次收到pc信息都创建出一个新的结果集
						//logger.error("get_pc_message=" + temp);
						// 获取用户的返回对象
						List<ResultMessageDto> listresult = userOperateAction
								.UserOperToStr(temp);
						if (!CollectionUtils.isEmpty(listresult)) {
							for (ResultMessageDto resultMessageDto : listresult) {
								opertResultMessage(resultMessageDto, writer);
							}
						}
						temp = "";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginOutNoSafe();
		}
	}

	// 非安全登出
	public void loginOutNoSafe() {
		try {
			logger.debug("-------非安全登出socket==-------" + socket);
			// 非安全登出
			if (socket == null) {
				new LoginOutUserDetailAction(initPCUser, mapUserLogin)
						.pcUserLoginOutNoSocket(initPCUser);
			} else {
				new LoginOutUserDetailAction(initPCUser, mapUserLogin)
						.pcUserLoginOut(initPCUser, socket);
				socket.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 将错误对象解析成字符串返回给pc端
	public void opertResultMessage(ResultMessageDto resultMessageDto,
			Writer writer) throws IOException {
		if (resultMessageDto.getResultNum() != 200) {
			// 将错误对象包装成发给pc的错误码
			Tools.writeInString(writer,
					groupErrorMessageInfo.groupHandleBody(resultMessageDto));
			return;
		}
		// 如果返回信息body为空则不写入socket
		if (resultMessageDto.getBody() != null) {
			logger.error("send_to_pc_str=" + resultMessageDto.getBody());
			Tools.writeInString(writer, resultMessageDto.getBody().toString());
		}
		// 如果是登出操作，直接退出
		if ("14".equals(resultMessageDto.getFunctionId())) {
			return;
		}
		// 如果是下订单操作，发给其他socket
		if ("15".equals(resultMessageDto.getFunctionId())) {
			nextOrderSendSocket(resultMessageDto);
		}
	}

	// 将该条消息发送给这个用户的所有socket
	public void nextOrderSendSocket(ResultMessageDto resultMessageDto) {
		PCUserDto pcu = mapUserLogin.get(initPCUser.getMt4Id());
		List<Socket> listsocket = pcu.getListSockets();
		if (CollectionUtils.isEmpty(listsocket)) {
			return;
		}
		for (Socket socketl1 : listsocket) {
			if (socketl1 != null && socketl1 != socket && !socketl1.isClosed()) {
				Tools.writeSocketInString(socketl1, resultMessageDto.getBody()
						.toString());
			}
		}
	}

}
