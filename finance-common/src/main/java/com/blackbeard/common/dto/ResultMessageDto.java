package com.blackbeard.common.dto;

import lombok.Setter;

import lombok.Getter;

public class ResultMessageDto {
   //返回信息对象
	
	@Getter
	@Setter
	private int resultNum;   //返回结果编号
	@Getter
	@Setter
	private String resultCode;   //返回结果编码
	@Getter
	@Setter
	private String resultMessage;   //返回结果消息
	@Getter
	@Setter
	private Object body;  //返回的对象
	@Getter
	@Setter
	private PCUserDto pcUserDto;   //用户登录信息
	@Getter
	@Setter
	private String functionId;   //功能号

}
