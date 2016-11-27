package com.blackbeard.tasks;

import org.springframework.stereotype.Component;

import com.blackbeard.common.service.IUserInfoService;
import com.blackbeard.common.util.SocketUtils;


@Component("UserInfoTask")
public class UserInfoTask {


	public void updateUserInfoByHistory() {
		System.out.println("---run tasks---" + System.currentTimeMillis());
		IUserInfoService userInfoService = SocketUtils.getUserInfoService();
		userInfoService.addHistoryToUser();
	}

}
