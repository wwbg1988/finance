package com.blackbeard.util.orderUtil;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.util.Tools;

public class LoginUserDetailAction {

	private static final Logger logger = Logger
			.getLogger(LoginUserDetailAction.class);
	private PCUserDto initPCUser;
	private Socket socket;
	private Map<Long, PCUserDto> mapUserLogin;

	public LoginUserDetailAction(PCUserDto pcuserdto, Socket socket,
			Map<Long, PCUserDto> maplogin) {
		this.initPCUser = pcuserdto;
		this.socket = socket;
		this.mapUserLogin = maplogin;
	}

	public void pcUserLogin(String body, ResultMessageDto resultMessageDto) {
		// 将pc信息转为用户登录对象
		PCUserDto resolve_pc_user = ResolvePCUserInfo.resolvePCUser_login(body,
				resultMessageDto);
		// 用户登录,返回用户登录对象
		PCUserDto is_pc_user_login = LoginUserAction.login(resolve_pc_user,
				resultMessageDto);
		// 将用户登录对象转为发送pc字符串
		String send_to_pc_str = LoginMessageInfo.getHandleBody(
				is_pc_user_login, resultMessageDto);

		if (resultMessageDto.getResultNum() != 200) {
			resultMessageDto.getPcUserDto().setName(is_pc_user_login.getName());
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_CODE, send_to_pc_str);
			return;
		}
		// 具体的登录操作，写入数据库，保存入socket集合
		pcUserLoginDetail(is_pc_user_login);
		// 组装返回对象
		Tools.setResultMessageDto(resultMessageDto, 200,
				KlineConstants.RESULT_SUCCESS_CODE, send_to_pc_str);
		logger.error("----------登录成功,当前的用户=" + initPCUser.getName()
				+ "------------");
	}

	public void pcUserLoginDetail(PCUserDto is_pc_user_login) {
		// 登陆成功
		// 登录状态修改为已登录
		initPCUser.setLoginFlag(1);
		// 为当前的登录用户赋值
		initPCUser.setName(is_pc_user_login.getName());
		initPCUser.setMt4Id(is_pc_user_login.getMt4Id());
		initPCUser.setBlance(is_pc_user_login.getBlance());
		// 获取allusersocket,登陆成功加入
		pcUserLoginDetailAddAllSocket();
		// 加入mapsocket，每个用户的socket集合
		pcUserLoginDetailAddMapSocket(is_pc_user_login);

	}

	public void pcUserLoginDetailAddAllSocket() {
		List<Socket> allUser = KlineConstants.SOCKET_ALLUSER;
		allUser.add(socket);
	}

	public void pcUserLoginDetailAddMapSocket(PCUserDto is_pc_user_login) {
		PCUserDto pcUserDto = mapUserLogin.get(is_pc_user_login.getMt4Id());
		if (pcUserDto != null) {
			// 该用户之前有登录信息
			// 获取登录socket集合
			List<Socket> listsocket = pcUserDto.getListSockets();
			if (CollectionUtils.isEmpty(listsocket)) {
				// socket集合为空新建socket集合加入当前socket
				listsocket = new ArrayList<Socket>();
				listsocket.add(socket);
				initPCUser.setListSockets(listsocket);
			} else {
				// socket集合不为空，直接加入当前socket
				listsocket.add(socket);
			}
			pcUserDto.setListSockets(listsocket);
		} else {
			// 该用户之前没有登录信息
			List<Socket> listsocket = new ArrayList<Socket>();
			listsocket.add(socket);
			PCUserDto pcUserDto2 = new PCUserDto();
			pcUserDto2.setListSockets(listsocket);
			pcUserDto2.setMt4Id(is_pc_user_login.getMt4Id());
			pcUserDto2.setName(is_pc_user_login.getName());
			// 把用户信息加入到用户登录map
			mapUserLogin.put(pcUserDto2.getMt4Id(), pcUserDto2);
		}
	}

}
