package com.blackbeard.common.dto;

import java.io.Serializable;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PCUserDto implements Serializable{
//PC用户登录对象

	@Getter
	@Setter
	private String name;    //登录账号
	
	@Getter
	@Setter
	private String password;    //登录密码
	
	@Getter
	@Setter
	private Date CreateTime;  //创建时间
	
	@Getter
	@Setter
	private Date updateTime;  //更新时间
	
	@Getter
	@Setter
	private String id;    //id
	
	@Getter
	@Setter
	private String md5Message;   //server发给pc 的用户名MD5加密
	
	@Getter
	@Setter
	private Long mt4Id;    //mt4id
	
	@Getter
	@Setter
	private long  blance;   //用户余额
	
	@Getter
	@Setter
	private long orderBlance;  //订单余额
	
	@Getter
	@Setter
	private Integer loginFlag;  //用户登录状态
	@Getter
	@Setter
	private List<Socket> listSockets;  //用户登录之后产生的socket对象
	
}
