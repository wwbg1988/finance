package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class UserInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1596007833288561878L;

	@Getter
	@Setter
	private Integer id;

	/**
	 * 用户名
	 */
	@Getter
	@Setter
	private String userName;

	/**
	 * 用户密码
	 */
	@Getter
	@Setter
	private String userPassword;

	/**
	 * 用户邮箱
	 */
	@Getter
	@Setter
	private String userMail;

	/**
	 * mt4Id
	 */
	@Getter
	@Setter
	private String mt4Id;

	/**
	 * MT4用户组id
	 */
	@Getter
	@Setter
	private String mt4UserGroup;

	/**
	 * 用户状态:0 注册,1 完成,2 在线,3 离线
	 */

	@Getter
	@Setter
	private Integer userState;

	/**
	 * 余额,乘以100保存bigint
	 */
	@Getter
	@Setter
	private Integer blance;

	/**
	 * 证件编号
	 */
	@Getter
	@Setter
	private String certificateNo;

	/**
	 * 证件图片保存地址
	 */
	@Getter
	@Setter
	private String certificateUrl;

	/**
	 * 电话号
	 */
	@Getter
	@Setter
	private String userTelphone;

	/**
	 * 创建时间
	 */
	@Getter
	@Setter
	private Date createTime;

	/**
	 * 更新时间
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

	@Getter
	@Setter
	private String md5Message; // server发给pc 的用户名MD5加密

	@Getter
	@Setter
	private Integer star;
	@Getter
	@Setter
	private Integer end;
	@Getter
	@Setter
	private Date lastLogin;   //最后登录时间
}
