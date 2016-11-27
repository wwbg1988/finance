package com.blackbeard.util.orderUtil;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;

public class ResolvePCUserInfo extends BaseMessageInfo{
	// 解析登录用户内容 [224310101100409name,password]

	public static PCUserDto resolvePCUser_login(String body,ResultMessageDto resultMessageDto) {
		PCUserDto pcUserDto = new PCUserDto();
		if (!body.contains(",")) {
			resultMessageDto.setResultCode(KlineConstants.RESULT_ERROR_CODE);
			resultMessageDto.setResultMessage("解析用户错误");
			resultMessageDto.setResultNum(500);
			return pcUserDto;
		}
		String[] bodys = body.split(",");
		if (bodys == null || bodys.length != 2) {
			resultMessageDto.setResultCode(KlineConstants.RESULT_ERROR_CODE);
			resultMessageDto.setResultMessage("解析用户错误");
			resultMessageDto.setResultNum(500);
			return pcUserDto;
		}
		pcUserDto.setName(bodys[0]);
		pcUserDto.setPassword(bodys[1]);
		resultMessageDto.setResultNum(200);
		return pcUserDto;
	}

	//登出[100410101100070张三,615db57aa314529aaa0fbe95b3e95bd3]
	public static PCUserDto resolvePCUser_loginOut(String body,ResultMessageDto resultMessageDto) {
		PCUserDto pcUserDto = new PCUserDto();
		if (!body.contains(",")) {
			resultMessageDto.setResultCode(KlineConstants.RESULT_ERROR_CODE);
			resultMessageDto.setResultMessage("解析用户错误");
			resultMessageDto.setResultNum(500);
			return pcUserDto;
		}
		String[] bodys = body.split(",");
		if (bodys == null || bodys.length != 2) {
			resultMessageDto.setResultCode(KlineConstants.RESULT_ERROR_CODE);
			resultMessageDto.setResultMessage("解析用户错误");
			resultMessageDto.setResultNum(500);
			return pcUserDto;
		}
		pcUserDto.setName(bodys[0]);
		pcUserDto.setMd5Message(bodys[1]);
		resultMessageDto.setResultNum(200);
		return pcUserDto;
	}
	
	
}
