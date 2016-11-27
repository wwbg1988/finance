package com.blackbeard.util.orderUtil;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.ssic.util.StringUtils;

public class GroupErrorMessageInfo extends BaseMessageInfo{

	//组装发给pc端的错误信息
	
	//[100110101300072kaka,200,5541c7b5a06c39b267a5efae6628e003:1467943581341]
	
	public  String groupHandleBody(ResultMessageDto resultMessageDto){
		String errorCode = resultMessageDto.getResultCode();
		PCUserDto pcUserDto = resultMessageDto.getPcUserDto();
		String handle_body_str = "";
		String body_str = getBodyStr(errorCode,pcUserDto);
		String handle_str = getHandleStr(body_str.length());
		handle_body_str ="[" + handle_str + body_str + "]";
		return handle_body_str;
	}

	public String getBodyStr(String errorCode,PCUserDto pcUserDto){
		String body_str ="";
		StringBuffer sb = new StringBuffer();
		sb.append(pcUserDto.getName()).append(",").append(500).append(",").append(errorCode).append(":")
		.append(System.currentTimeMillis());
		body_str=sb.toString();
		return body_str;
	}
	
	
	//组装handle
	public  String getHandleStr(int bodyLength){
		StringBuffer sb = new StringBuffer();
		// 长度不够5位的前面补零
		String strlength = String.valueOf(bodyLength + 17);
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
		
		sb.append(getQuenceId()).append(KlineConstants.init_languageId)
				.append(KlineConstants.init_agreementId).append(KlineConstants.PC_MESSAGE_ERROR)
				.append(strlength);
		return sb.toString();
	}

	
}
