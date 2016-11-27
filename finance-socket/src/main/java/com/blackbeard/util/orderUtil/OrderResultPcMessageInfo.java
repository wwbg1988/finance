package com.blackbeard.util.orderUtil;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.OrderResultDto;
import com.ssic.util.StringUtils;

public class OrderResultPcMessageInfo extends BaseMessageInfo{

	
		public String groupHandleBody(OrderResultDto orderResultDto,long orderTotal){
			String handle_body_str = "";
			String body_str =  getBodyStr(orderResultDto,orderTotal);
			String handle_str = getHandleStr(body_str.length());
			handle_body_str ="[" + handle_str + body_str + "]";
			return handle_body_str;
		}
		// handle+symbol+closeprice+closetime+cmd+loginID+openprice+opentime+profit+tick
		//组装body
		public String getBodyStr(OrderResultDto orderResultDto,long orderTotal){
			String body_str ="";
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(orderResultDto.getSymbol()).append(",").append(orderResultDto.getCloseprice()).append(",").append(orderResultDto.getClosetime()).append(",")
			.append(orderResultDto.getCmd()).append(",").append(orderResultDto.getLoginID()).append(",")
			.append(orderResultDto.getOpenprice()).append(",").append(orderResultDto.getOpentime()).append(",")
			.append(orderResultDto.getProfit()).append(",").append(orderTotal).append(",").append(orderResultDto.getTick());
			return stringBuffer.toString();
		}
		
		//组装handle
		public String getHandleStr(int bodyLength){
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
					.append(KlineConstants.init_agreementId).append(KlineConstants.PC_MESSAGE_ORDER_RESULT)
					.append(strlength);
			return sb.toString();
		}

	
}
