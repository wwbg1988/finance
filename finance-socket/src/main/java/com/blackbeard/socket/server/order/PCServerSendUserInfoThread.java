package com.blackbeard.socket.server.order;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Map;

import org.apache.log4j.Logger;

import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.socket.server.dto.PCServerDto;
import com.blackbeard.socket.service.BaseThread;
import com.blackbeard.util.orderUtil.GroupUserBlanceInfo;
import com.blackbeard.util.orderUtil.LoginOutUserDetailAction;
import com.blackbeard.util.orderUtil.UserBlanceAction;
import com.blackbeard.util.orderUtil.UserOperateAction;
import com.ssic.util.StringUtils;

public class PCServerSendUserInfoThread extends BaseThread {
	// 每隔5秒发送用户余额和订单余额
	private Socket socket;
	private PCUserDto pcUserDto;
	private PCServerDto pcServerDto ;
	private Map<Long, PCUserDto> mapUserLogin;
	
	public PCServerSendUserInfoThread(PCServerDto pcServerDto) {
		this.socket = pcServerDto.getSocket();
		this.pcUserDto = pcServerDto.getPcUserDto();
		this.mapUserLogin = pcServerDto.getMapUserLogin();
		this.pcServerDto = pcServerDto;
	}

	private static final Logger logger = Logger
			.getLogger(PCServerSendUserInfoThread.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Writer writer;
		try {
			writer = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(), "utf-8"));
			GroupUserBlanceInfo groupUserBlanceInfo = new GroupUserBlanceInfo();
			UserOperateAction userOperateAction = new UserOperateAction(pcServerDto);
			UserBlanceAction userBlanceAction = new UserBlanceAction();
			long blance=0;
			while (true) {
				Thread.sleep(1000);
               logger.debug("开始获取金额----thread="+Thread.currentThread().getName());
				if(pcServerDto.getBlanceIndex()>10){
					// 非安全登出
					logger.error("------senduserinfo非安全登出------thread="+Thread.currentThread().getName());
					new LoginOutUserDetailAction(pcUserDto, mapUserLogin).pcUserLoginOut(pcUserDto,socket);
					break;
				}
				pcServerDto.setBlanceIndex(pcServerDto.getBlanceIndex()+1);
				if (socket.isClosed()) {
					break;
				} else {
					if (pcUserDto != null
							&& pcUserDto.getLoginFlag() == 1
							&& !StringUtils.isEmpty(pcUserDto.getName())) {
						ResultMessageDto resultMessageDto = new ResultMessageDto();
						logger.debug("-----获取金额1：-"+pcUserDto.getName()+"--mt4id="+pcUserDto.getMt4Id()+"----"+pcUserDto.getBlance()+"----thread="+Thread.currentThread().getName());
						blance = userBlanceAction.getUserBlance(pcUserDto,resultMessageDto);
						if(resultMessageDto.getResultNum()!=500){
							pcUserDto.setBlance(blance);
							//获取订单余额
							PCUserDto pcuser2 = mapUserLogin.get(pcUserDto.getMt4Id());
							pcuser2.setBlance(blance);
						//	logger.error("####name="+pcuser2.getName()+"###mt4id="+pcuser2.getMt4Id()+"###blance="+pcuser2.getBlance()+"###orderblance="+pcuser2.getOrderBlance()+"----thread="+Thread.currentThread().getName());
							String blancestr = groupUserBlanceInfo
									.groupHandleBody(pcuser2);
//							logger.error("blancestr=" + blancestr + ",thread="
//									+ Thread.currentThread().getName());
							writer.write(blancestr);
							writer.flush();
						}
						resultMessageDto=null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
