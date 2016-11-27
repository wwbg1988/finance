package com.blackbeard.common.dto;

import java.net.Socket;

import lombok.Getter;
import lombok.Setter;

public class PcServerElementDto {

	//pc服务端几个线程启动需要的参数
	@Getter
	@Setter
	private Integer loginFlag;  //用户登录状态

}
