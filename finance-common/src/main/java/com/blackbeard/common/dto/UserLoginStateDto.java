package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class UserLoginStateDto implements Serializable{

	@Getter
	@Setter
	private String id;   //id
	
	@Getter
	@Setter
	private String userName;   //用户名
	
	@Getter
	@Setter
	private Long mt4Id;    //mt4id
	
	@Getter
	@Setter
	private long blance;   //余额
	
	@Getter
	@Setter
	private int amount;    //客户端数量
	
	@Getter
	@Setter
    private Date createTime;   //创建时间
	
	@Getter
	@Setter
	private Date updateTime;   //更新时间
	
	
	@Getter
	@Setter
	private int userId;   //用户ID
	
}
