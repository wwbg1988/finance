package com.blackbeard.util.orderUtil;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.PCUserDto;
import com.ssic.util.StringUtils;

public class GroupUserBlanceInfo extends BaseMessageInfo{

	//组装用户的blance信息
	//[224310101100409username,blance,orderBlance:currentTime]

		public  String groupHandleBody(PCUserDto pcUserDto){
			String handle_body_str = "";
			String body_str =  getBodyStr(pcUserDto);
			String handle_str = getHandleStr(body_str.length());
			handle_body_str ="[" + handle_str + body_str + "]";
			return handle_body_str;
		}
		//组装body
		public  String getBodyStr(PCUserDto pcUserDto){
			String body_str ="";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(pcUserDto.getName()).append(",").append(pcUserDto.getBlance()/100.0).append(",")
			.append(pcUserDto.getOrderBlance()/100.0).append(":").append(System.currentTimeMillis());
			body_str = stringBuffer.toString();
			return body_str;
		}
		
		//组装handle
		public  String getHandleStr(int bodyLength){
			StringBuffer sb = new StringBuffer();
			// 长度不够5位的前面补零
			String strlength = String.valueOf(bodyLength + 17);
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
					.append(KlineConstants.init_agreementId).append(KlineConstants.PC_MESSAGE_USER_BLANCE)
					.append(strlength);
			return sb.toString();
		}
		

}
