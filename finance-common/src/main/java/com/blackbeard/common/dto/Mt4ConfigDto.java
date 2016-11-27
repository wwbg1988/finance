package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Mt4ConfigDto：MT4服务器配置项表Dto
 * 
 * @author 刘博
 *
 */
public class Mt4ConfigDto implements Serializable {
	/**
	 * 主键id
	 */
	@Getter
	@Setter
	private String id;

	/**
	 * MT4服务器IP
	 */
	@Getter
	@Setter
	private String serverIp;

	/**
	 * MT4服务器端口号
	 */
	@Getter
	@Setter
	private Integer serverPort;

	/**
	 * 用户级别(mt4用户)
	 */
	@Getter
	@Setter
	private Integer userlever;

	/**
	 * mt4登录账号
	 */
	@Getter
	@Setter
	private String lgManage;

	/**
	 * mt4登录密码
	 */
	@Getter
	@Setter
	private String lgPword;

	/**
	 * mid
	 */
	@Getter
	@Setter
	private String mid;

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
	 * 是否有效 1:是;0:否
	 */
	@Getter
	@Setter
	private Integer stat;

}
