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
import com.ssic.util.StringUtils;

public class LoginUserAction {
	// 根据传过来的用户名密码验证用户是否合法

		@Transactional
		public static PCUserDto login(PCUserDto pcUserDto,ResultMessageDto resultMessageDto) {

			if (pcUserDto == null) {
				resultMessageDto.setResultCode(KlineConstants.RESULT_ERROR_CODE);
				resultMessageDto.setResultMessage("登录用户不能为空");
				resultMessageDto.setResultNum(500);
				return pcUserDto;
			}
			if (StringUtils.isEmpty(pcUserDto.getName())) {
				resultMessageDto.setResultCode(KlineConstants.RESULT_NAME_NOT_EXIST);
				resultMessageDto.setResultMessage("用户名称不能为空");
				resultMessageDto.setResultNum(500);
				return pcUserDto;
			}
			if (StringUtils.isEmpty(pcUserDto.getPassword())) {
				resultMessageDto.setResultCode(KlineConstants.RESULT_PASSWORD_ERROR);
				resultMessageDto.setResultMessage("用户密码不能为空");
				resultMessageDto.setResultNum(500);
				return pcUserDto;
			}

			//如果该用户能在登录信息表中查到，则余额显示登录状态表的，如果没有余额显示用户表里的。
			IUserInfoService userInfoService = SocketUtils.getUserInfoService();
			UserInfoDto userInfoDto = new UserInfoDto();
			userInfoDto.setUserName(pcUserDto.getName());
		//	userInfoDto.setUserPassword( MD5Util.base64Encode(pcUserDto.getPassword()));
			List<UserInfoDto> listuserinfo = userInfoService.findBy(userInfoDto);
			if (CollectionUtils.isEmpty(listuserinfo)) {
				//根据用户名查询结果
				resultMessageDto.setResultCode(KlineConstants.RESULT_PASSWORD_ERROR);
				resultMessageDto.setResultMessage("用户名密码错误");
				resultMessageDto.setResultNum(500);
			}else {
				//看密码是否一致
				UserInfoDto userinfo1 =  listuserinfo.get(0);
				if(!userinfo1.getUserPassword().equals(MD5Util.base64Encode(pcUserDto.getPassword()))){
					resultMessageDto.setResultCode(KlineConstants.RESULT_PASSWORD_ERROR);
					resultMessageDto.setResultMessage("用户名密码错误");
					resultMessageDto.setResultNum(500);
					return pcUserDto;
				}
				resultMessageDto.setResultCode(KlineConstants.RESULT_SUCCESS_CODE);
				resultMessageDto.setResultMessage("用户登录成功");
				resultMessageDto.setResultNum(200);
				// 查询历史记录表，这个用户名不存在插入，存在amount+1
				IUserLoginStateService userLoginStateService = SocketUtils
						.getUserLoginStateService();
				UserLoginStateDto userLoginStateDto = new UserLoginStateDto();
				userLoginStateDto.setUserName(pcUserDto.getName());
				pcUserDto.setMt4Id(Long.valueOf(listuserinfo.get(0).getMt4Id()));
				userLoginStateDto.setMt4Id(pcUserDto.getMt4Id());
				List<UserLoginStateDto> listuserlogin = userLoginStateService
						.findBy(userLoginStateDto);
				
				if (CollectionUtils.isEmpty(listuserlogin)) {
					// 登录记录中不存在该用户则插入
					UserInfoDto userinfo_login = listuserinfo.get(0);
					userLoginStateDto.setAmount(1);
					userLoginStateDto.setBlance(userinfo_login.getBlance());
					userLoginStateDto.setMt4Id(Long.valueOf(userinfo_login.getMt4Id()));
					userLoginStateDto.setUserId(userinfo_login.getId());
					userLoginStateService.addUserLogin(userLoginStateDto);
					//登录记录中没有，余额保存用户表的。
					pcUserDto.setBlance( listuserinfo.get(0).getBlance());
				} else {
					// 登录记录中存在该用户则amount+1
					userLoginStateService.editUserLoginAmount(userLoginStateDto);
					//登录记录中有，余额保存登录记录中的。
					pcUserDto.setBlance( listuserlogin.get(0).getBlance());
				}
				//用户登录之后更新sysuser的user_state，用户在线状态
				userInfoService.updateOnLine(userInfoDto);
			}
			return pcUserDto;
		}

}
