package com.blackbeard.util.orderUtil;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.Mt4DownOrderDto;
import com.ssic.util.StringUtils;

public class GroupToPcDownOrderInfo extends BaseMessageInfo{

	//组合成发给pc的下订单数据
	//[100210101300058EURUSD,1.12974,100,1,UP5min,76,1467358309]
	public String groupHandleBody(Mt4DownOrderDto mt4DownOrderDto){
		String handle_body_str = "";
		String body_str =  getBodyStr(mt4DownOrderDto);
		String handle_str = getHandleStr(body_str.length());
		handle_body_str ="[" + handle_str + body_str + "]";
		return handle_body_str;
	}
	// handle+货币名称+价格+数量+买或卖+涨跌分钟+订单号+时间戳
	//组装body
	public String getBodyStr(Mt4DownOrderDto mt4DownOrderDto){
		String body_str ="";
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(mt4DownOrderDto.getCurrencyType()).append(",").append(mt4DownOrderDto.getPrice()).append(",")
		.append(mt4DownOrderDto.getTotal()).append(",").append(mt4DownOrderDto.getDirection()).append(",")
		.append(mt4DownOrderDto.getComment()).append(",").append(mt4DownOrderDto.getTick()).append(",")
		.append(mt4DownOrderDto.getTime());
		body_str = stringBuffer.toString();
		return body_str;
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
				.append(KlineConstants.init_agreementId).append(KlineConstants.PC_MESSAGE_ORDER)
				.append(strlength);
		return sb.toString();
	}
	
//	public static void main(String[] args) {
//		
//		Mt4DownOrderDto mt4DownOrderDto = new Mt4DownOrderDto();
//		mt4DownOrderDto.setComment(comment);
//		
//	}
	
}
