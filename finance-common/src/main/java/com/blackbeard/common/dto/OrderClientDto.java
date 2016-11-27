package com.blackbeard.common.dto;

import lombok.Getter;
import lombok.Setter;

public class OrderClientDto {
//订单客户端信息
	
	@Getter
	@Setter
	private String pc_to_mt4_message;   //pc下订单消息
	@Getter
	@Setter
	private String mt4_to_pc_message;   //mt4返回的下订单结果
	
}
