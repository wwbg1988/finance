package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class UserRechangeFailureRecordDto implements Serializable {

	@Getter
	@Setter
	private String id;

	/**
	 * 用户id
	 */
	@Getter
	@Setter
	private Integer userId;
	
	/**
	 * 用户姓名
	 */
	@Getter
	@Setter
	private String userName;

	/**
	 * mt4_id
	 */
	@Getter
	@Setter
	private String mt4Id;

	/**
	 * 充值金额
	 */
	@Getter
	@Setter
	private Long rechargeMoney;

	/**
	 * 创建时间
	 */
	@Getter
	@Setter
	private Date createTime;

	@Getter
	@Setter
	private String createTimeFormatString;

	/**
	 * 修改时间
	 */
	@Getter
	@Setter
	private Date lastUpdateTime;

	/**
	 * 是否有效
	 */
	@Getter
	@Setter
	private Integer stat;

}
