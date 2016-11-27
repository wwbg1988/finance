package com.blackbeard.util.orderUtil;

import com.blackbeard.common.dto.Mt4OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mt4OrderMessageInfo {

	// 组装mt4订单信息
	// 00100128{"loginId":"170","currencyType":"EURUSDDB","price":"1.12974","total":"10000","direction":"1","comment":"UP1min"}

	private static final int MT4_HANDLE_LENGTH = 8; // mt4handle长度
	private static final int MT4_ORDER_STRLENGTH = 5; // mt4字符串的长度
	private static final String MT4_ORDER_FUNCTION_ID = "001"; // MT4order功能号

	// 组装body
	public static String sendMt4OrderBody(Mt4OrderDto mt4OrderDto) {
		String body = "";
		if (mt4OrderDto != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				body = objectMapper.writeValueAsString(mt4OrderDto);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return body;
	}

	// 组装handle
	public static String sendMt4OrderHandle(int bodyLength) {
		int length = bodyLength + MT4_HANDLE_LENGTH;
		String length_str = String.valueOf(length);
		int i = MT4_ORDER_STRLENGTH - length_str.length();
		if (i == 1) {
			length_str = "0" + length_str;
		} else if (i == 2) {
			length_str = "00" + length_str;
		} else if (i == 3) {
			length_str = "000" + length_str;
		} else if (i == 4) {
			length_str = "0000" + length_str;
		}
		return MT4_ORDER_FUNCTION_ID + length_str;
	}

	// 组装handlebody
	public static String sendBt4OrderHandleBody(Mt4OrderDto mt4OrderDto) {
		StringBuffer sb = new StringBuffer();
		String body = sendMt4OrderBody(mt4OrderDto);
		sb.append(body);
		return sb.toString();

	}

	public static void main(String[] args) {
		Mt4OrderDto mt4OrderDto = new Mt4OrderDto();
		mt4OrderDto.setLoginId(170);
		mt4OrderDto.setCurrencyType("EURUSDDB");
		mt4OrderDto.setPrice(1.12974);
		mt4OrderDto.setTotal(10000);
		mt4OrderDto.setDirection(1);
		mt4OrderDto.setComment("UP1min");
		String handlebody = sendBt4OrderHandleBody(mt4OrderDto);


	}

}
