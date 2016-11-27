package com.blackbeard.util.orderUtil;

import java.io.IOException;

import com.blackbeard.common.dto.OrderResultDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssic.util.StringUtils;

public class ResolveMt4OrderResultInfo {
	// 解析mt4返回的订单结果
	// {"closeprice":0.0,"closetime":0,"cmd":1,"comment":"DW1min","loginID":18030158,"openprice":1.129740,"opentime":1467627083,"profit":0.0,"symbol":"EURUSDDB","tick":447}
    // 解析mt4返回的字符串，closetime为0表示下单1表示下单结果，如果为1把该条数据插入历史订单表，并且推送pc
	
	
	//解析订单结果
	public static OrderResultDto getOrderResultDto(String orderResult){
		//orderResult="{\"closeprice\":0.0,\"closetime\":0,\"cmd\":0,\"comment\":\"UP1min\",\"loginID\":18030158,\"openprice\":1.129740,\"opentime\":1467628386,\"profit\":0.0,\"symbol\":\"EURUSDDB\",\"tick\":458}";
		//orderResult="{\"closeprice\":1.1110,\"closetime\":1467628443,\"cmd\":0,\"comment\":\"dddddddddddddddddddddddddddddddd�����������\",\"loginID\":18030158,\"openprice\":1.129740,\"opentime\":1467628383,\"profit\":-1000.0,\"symbol\":\"EURUSDDB\",\"tick\":457}";
		OrderResultDto orderResultDto = new OrderResultDto();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			orderResultDto= objectMapper.readValue(orderResult, OrderResultDto.class);
			//openprice /100000   closeprice /100000
			if(!StringUtils.isEmpty(orderResultDto.getCloseprice())&&!"0".equals(orderResultDto.getCloseprice())){
				double closeprice =Double.valueOf(orderResultDto.getCloseprice())/100000   ;
				orderResultDto.setCloseprice(String.valueOf(closeprice));
			}else{
				orderResultDto.setCloseprice("0");
			}
			if(!StringUtils.isEmpty(orderResultDto.getOpenprice())&&!"0".equals(orderResultDto.getOpenprice())){
				double openprice = Double.valueOf(orderResultDto.getOpenprice())/100000 ;
				orderResultDto.setOpenprice(String.valueOf(openprice));
			}else{
				orderResultDto.setOpenprice("0");
			}
		
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderResultDto;
		
	}
	
	public static void main(String[] args) {
		String orderResult="{\"closeprice\":1.1110,\"closetime\":1467628443,\"cmd\":0,\"comment\":\"dddddddddddddddddddddddddddddddd�����������\",\"loginID\":18030158,\"openprice\":1.129740,\"opentime\":1467628383,\"profit\":-1000.0,\"symbol\":\"EURUSDDB\",\"tick\":457}";
		getOrderResultDto(orderResult);
	}
	
	
	
	
	
	
}
