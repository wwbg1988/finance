package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class GroupHandleDto implements Serializable{
	//消息头部
	
		@Getter
		@Setter
		private int id;     //头id     4      1000-9999
		@Getter
		@Setter
		private String languageId;   //语言id   2    10
		@Getter
		@Setter
		private String agreementId;   //协议号      2    10
		@Getter
		@Setter
		private String functionId;    //功能号      2     k线：11     订单：12   用户登录 :13   心跳:00
		@Getter
		@Setter
		private int lengthId;     //长度        5
}
