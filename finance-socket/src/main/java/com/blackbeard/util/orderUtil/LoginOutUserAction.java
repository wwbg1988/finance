package com.blackbeard.util.orderUtil;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.service.IUserInfoService;
import com.blackbeard.common.service.IUserLoginStateService;
import com.blackbeard.common.util.MD5Util;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.util.Tools;
import com.ssic.util.StringUtils;

import freemarker.log.Logger;

public class LoginOutUserAction {
	// 用户登出操作

	@Transactional
	public PCUserDto loginOut(PCUserDto pcUserDto,
			ResultMessageDto resultMessageDto) {
		// 安全登出
		if (pcUserDto == null) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_CODE, "登录用户不能为空");
			return pcUserDto;
		}
		if (StringUtils.isEmpty(pcUserDto.getName())) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_NAME_NOT_EXIST, "用户名称不能为空");
			return pcUserDto;
		}
		if (StringUtils.isEmpty(pcUserDto.getMd5Message())) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_PASSWORD_ERROR, "用户服务号不能为空");
			return pcUserDto;
		}
		System.out.println("#############用户=" + pcUserDto.getName() + " 安全登出");
		//处理用户的登出操作
		handleLoginOut(pcUserDto, resultMessageDto);
		return pcUserDto;
	}

	// 非安全登出
	public void loginOut(String userName) {
		if (!StringUtils.isEmpty(userName)) {
			IUserLoginStateService userLoginStateService = SocketUtils
					.getUserLoginStateService();
			// sysuser用户离线
			PCUserDto pcUserDto = new PCUserDto();
			pcUserDto.setName(userName);
			sysUserLoginOut(pcUserDto);
			// t_ctr_user_login_state表amount-1
			UserLoginStateDto userLoginStateDto = new UserLoginStateDto();
			userLoginStateDto.setUserName(userName);
			userLoginStateService.reduceUserAmount(userLoginStateDto);
		}
	}

	public void sysUserLoginOut(PCUserDto pcUserDto) {
		// 查询出当前用户登录的客户端数量
		// amount为正数-1，
		// amount-1=0 , 把用户的登录状态更新为离线
		IUserInfoService userInfoService = SocketUtils.getUserInfoService();
		// 获取当前用户的登录客户端数量
		int amount = getUserLogin(pcUserDto);
		System.out.println("###########用户=" + pcUserDto.getName() + ",客户端的数量:"
				+ amount);
		if (amount > 0) {
			UserInfoDto userInfoDto = new UserInfoDto();
			userInfoDto.setUserName(pcUserDto.getName());
			if (amount == 1) {
				// 最后一个客户端下线
				userInfoService.updateOutLine(userInfoDto);
			} else {
				// 还有其他客户端在登录
				userInfoService.updateOnLine(userInfoDto);
			}
		}

	}

	// 获取当前用户的登录客户端数量
	public int getUserLogin(PCUserDto pcUserDto) {
		IUserLoginStateService userLoginStateService = SocketUtils
				.getUserLoginStateService();
		UserLoginStateDto userLoginStateDto2 = new UserLoginStateDto();
		userLoginStateDto2.setUserName(pcUserDto.getName());
		List<UserLoginStateDto> list_userlogin = userLoginStateService
				.findBy(userLoginStateDto2);
		int amount = list_userlogin.get(0).getAmount();
		return amount;
	}
	
	//处理用户的登出操作
	public void handleLoginOut(PCUserDto pcUserDto,ResultMessageDto resultMessageDto){
		IUserLoginStateService userLoginStateService = SocketUtils
				.getUserLoginStateService();
		// MD5码与用户名一致
		if (MD5Util.isTrueUserName(pcUserDto.getName(),
				pcUserDto.getMd5Message())) {
			// 执行登出操作,更新sysuser的用户登录状态
			sysUserLoginOut(pcUserDto);
			// t_ctr_user_login_state表amount-1
			// 用户登录记录amount-1
			UserLoginStateDto userLoginStateDto = new UserLoginStateDto();
			userLoginStateDto.setUserName(pcUserDto.getName());
			userLoginStateService.reduceUserAmount(userLoginStateDto);
			resultMessageDto.setResultCode(KlineConstants.RESULT_SUCCESS_CODE);
			resultMessageDto.setResultMessage("用户登出成功");
		}
	}

}
