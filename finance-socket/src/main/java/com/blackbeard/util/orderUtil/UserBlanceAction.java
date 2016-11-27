package com.blackbeard.util.orderUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.service.IUserInfoService;
import com.blackbeard.common.service.IUserLoginStateService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.util.Tools;
import com.ssic.util.StringUtils;

public class UserBlanceAction {

	// 查询数据库，查询用户最新的余额，
	// 先查询用户登录表，没有则查询用户信息表
	private static final Logger logger = Logger
			.getLogger(UserBlanceAction.class);

	public Long getUserBlance(PCUserDto pcUserDto,
			ResultMessageDto resultMessageDto) {
		long blance = 0;
		if (pcUserDto == null) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_USE_NO_EXIST, "查询用户信息为空");
			return blance;
		}
		// 获取这个用户的登录信息
		List<UserLoginStateDto> listuserlogin = getUserLoginInfo(pcUserDto);
		if (!CollectionUtils.isEmpty(listuserlogin)) {
			blance = listuserlogin.get(0).getBlance();
			return blance;
		}
		logger.error("----------用户登录信息表为空--------------thread="
				+ Thread.currentThread().getName());
		// 登录信息为空，开始获取用户信息
		List<UserInfoDto> listuser = getUserInfo(pcUserDto);
		if (CollectionUtils.isEmpty(listuser)) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_USE_NO_EXIST, "用户表信息为空");
			return blance;
		}
		blance = listuser.get(0).getBlance();
		return blance;
	}

	// 获取当前用户的登录情况
	public List<UserLoginStateDto> getUserLoginInfo(PCUserDto pcUserDto) {
		UserLoginStateDto userLoginStateDto = new UserLoginStateDto();
		IUserLoginStateService userLoginStateService = SocketUtils
				.getUserLoginStateService();
		if (!StringUtils.isEmpty(pcUserDto.getName())) {
			userLoginStateDto.setUserName(pcUserDto.getName());
		} else if (pcUserDto.getMt4Id() != null) {
			userLoginStateDto.setMt4Id(pcUserDto.getMt4Id());
		} else {
			logger.error("########错误：查询用户登录信息错误");
			return new ArrayList<UserLoginStateDto>();
		}
		List<UserLoginStateDto> listuserlogin = userLoginStateService
				.findBy(userLoginStateDto);
		return listuserlogin;
	}

	// 获取当前的用户信息
	public List<UserInfoDto> getUserInfo(PCUserDto pcUserDto) {
		IUserInfoService userInfoService = SocketUtils.getUserInfoService();
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserName(pcUserDto.getName());
		List<UserInfoDto> listuser = userInfoService.findBy(userInfoDto);
		return listuser;
	}

}
