package com.blackbeard.util.orderUtil;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.common.util.MD5Util;
import com.ssic.util.StringUtils;

public class LoginMessageInfo extends BaseMessageInfo{
	//组装登录信息，主要组装登录信息的body部分
	
		//[224310101100409login_result_code,servermsg:currentTime]
		// 组装内容信息
		//server to pc:  [224310101100409张三,200,server_num:currentTime]
		public static StringBuffer getBody(PCUserDto pcUserDto,ResultMessageDto resultMessageDtos) {
			StringBuffer sb = new StringBuffer();
			String userName = pcUserDto.getName();
		    sb.append(userName).append(",").append(resultMessageDtos.getResultCode()).append(",").append(MD5Util.md5(userName)).append(",").append(KlineConstants.NEXT_ORDER_PROFIT_RATIO).append(":");
			return sb.append(System.currentTimeMillis());
		}
		
		
		// 组装头部信息
		public static StringBuffer getHandle(int bodyHenght) {
			StringBuffer sb = new StringBuffer();
			// 长度不够5位的前面补零
			String strlength = String.valueOf(bodyHenght + 17);
			if (!StringUtils.isEmpty(strlength)) {
				int strlength_size = strlength.length();
				if (strlength_size < 5) {
					int cha = 5 - strlength_size;
					if (cha == 1) {
						strlength = "0" + strlength;
					} else if (cha == 2) {
						strlength = "00" + strlength;
					} else if (cha == 3) {
						strlength = "000" + strlength;
					}
				}
			}
			sb.append(getQuenceId()).append(KlineConstants.init_languageId)
					.append(KlineConstants.init_agreementId).append(KlineConstants.PC_MESSAGE_LOGIN)
					.append(strlength);
			return sb;
		}
		
		
		
		// 拼接头部和内容
		public static String getHandleBody(PCUserDto pcUserDto,ResultMessageDto resultMessageDtos) {
			StringBuffer sb = new StringBuffer();
			String handlestr = getHandle(getBody(pcUserDto,resultMessageDtos).toString().length())
					.toString();
			sb.append("[").append(handlestr).append(getBody(pcUserDto,resultMessageDtos).toString())
					.append("]");
			return sb.toString();
		}
}
