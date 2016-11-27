package com.blackbeard.util.orderUtil;

import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.util.Tools;

public class LoginOutUserDetailAction {

	private static final Logger logger = Logger
			.getLogger(LoginOutUserDetailAction.class);
	private PCUserDto initPCUser;
	private Map<Long, PCUserDto> mapUserLogin;

	public LoginOutUserDetailAction(PCUserDto pcuserdto,
			Map<Long, PCUserDto> mapuser) {
		this.initPCUser = pcuserdto;
		this.mapUserLogin = mapuser;
	}

	@Transactional
	public void pcUserLoginOut(String body, Socket socket,
			ResultMessageDto resultMessageDto) {
		// 用户登出
		// 解析pc的信息返回用户对象
		try {
			PCUserDto resolve_pc_user = ResolvePCUserInfo
					.resolvePCUser_loginOut(body, resultMessageDto);
			// 如果登出的用户不是当前登录的用户，返回用户错误
			if (!initPCUser.getName().equals(resolve_pc_user.getName())) {
				logger.error("---------登出的用户不是登录的用户----------");
				Tools.setResultMessageDto(resultMessageDto, 500,
						KlineConstants.RESULT_NOT_LOGINGED, "登出的用户不是登录的用户");
				return;
			}
			// 进行登出操作
			PCUserDto is_pc_user_loginout = new LoginOutUserAction().loginOut(
					resolve_pc_user, resultMessageDto);
			if (resultMessageDto.getResultNum() == 500) {
				logger.error("-----------用户登出失败-----------");
				return;
			}
			// 把用户对象转为发送pc的字符串
			String send_to_pc_str = LoginMessageInfo.getHandleBody(
					is_pc_user_loginout, resultMessageDto);
			if (200==resultMessageDto.getResultNum()) {
				// 登出成功,把用户的登录状态修改为未登录
				initPCUser.setLoginFlag(0);
				// 将登陆集合中的socket删掉
				reduceMapUserLogin(initPCUser, socket);
				// 关闭socket
				socket.close();
				resultMessageDto.setResultNum(200);
				resultMessageDto.setBody(send_to_pc_str);
				resultMessageDto.setPcUserDto(null); // 登出之后删掉用户的登录信息
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 从用户登录集合中删掉当前用户的socket
	public void reduceMapUserLogin(PCUserDto pcUserDto, Socket socket) {
		// 查询登录集合中是否存在这个用户信息
		if (pcUserDto.getMt4Id() != null) {
			PCUserDto login_user = mapUserLogin.get(pcUserDto.getMt4Id());
			if (login_user != null) {
				// 如果存在删除掉这个用户的socket
				List<Socket> sockets = login_user.getListSockets();
				Iterator<Socket> iter = sockets.iterator();
				while (iter.hasNext()) {
					Socket thissocket = iter.next();
					if (socket == thissocket) {
						iter.remove();
					}
				}
			}
		}
		// 删除alluser.socket中的这个socket
		List<Socket> alluser = KlineConstants.SOCKET_ALLUSER;
		if (!CollectionUtils.isEmpty(alluser)) {
			Iterator<Socket> iterall = alluser.iterator();
			while (iterall.hasNext()) {
				Socket thissocketsAll = iterall.next();
				if (socket == thissocketsAll) {
					iterall.remove();
				}
			}
		}
	}

	// pc用户安全登出，含socket
	public void pcUserLoginOut(PCUserDto initPCUser, Socket socket) {
		// 用户非安全登出
		logger.error("#################用户=" + initPCUser.getName() + " 非安全登出");
		// 执行登出操作
		new LoginOutUserAction().loginOut(initPCUser.getName());
		// 将用户登录状态修改为登出
		initPCUser.setLoginFlag(0);
		// 在登录集合中删除当前登出用户的socket
		reduceMapUserLogin(initPCUser, socket);
	}

	// pc用户费安全登出，不含socket
	public void pcUserLoginOutNoSocket(PCUserDto initPCUser) {
		initPCUser.setLoginFlag(0);
		// 执行登出操作
		new LoginOutUserAction().loginOut(initPCUser.getName());
		// 遍历这个用户，删掉为空的socket
		Map<Long, PCUserDto> mappc = KlineConstants.mapUserLogin;
		PCUserDto pcdtp = mappc.get(initPCUser.getMt4Id());
		if (pcdtp != null) {
			List<Socket> listso = pcdtp.getListSockets();
			if (!CollectionUtils.isEmpty(listso)) {
				Iterator<Socket> iterrator = listso.iterator();
				while (iterrator.hasNext()) {
					if (iterrator.next() == null || iterrator.next().isClosed()) {
						iterrator.remove();
					}
				}
			}
		}
		// 遍历 allusersocket ，删掉为空的socket
		List<Socket> allUser = KlineConstants.SOCKET_ALLUSER;
		if (!CollectionUtils.isEmpty(allUser)) {
			Iterator<Socket> iteAlluser = allUser.iterator();
			while (iteAlluser.hasNext()) {
				if (iteAlluser.next() == null || iteAlluser.next().isClosed()) {
					iteAlluser.remove();
				}
			}
		}

	}
}
