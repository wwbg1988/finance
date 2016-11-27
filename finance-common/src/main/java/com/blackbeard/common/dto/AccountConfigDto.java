package com.blackbeard.common.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 结算配置项dto
 * 
 * @author 刘博
 *
 */
public class AccountConfigDto {
	/**
	 * 主键id
	 */
	@Getter
	@Setter
	private String id;

	/**
	 * 结算百分比
	 */
	@Getter
	@Setter
	private Float accountPercent;

	/**
	 * 结算等级
	 */
	@Getter
	@Setter
	private Integer level;

	/**
	 * 创建时间
	 */
	@Getter
	@Setter
	private Date createTime;

	/**
	 * 创建时间格式化字符串 
	 */
	@Getter
	@Setter
	private String createTimeFormatString;

	/**
	 * 更新时间
	 */
	@Getter
	@Setter
	private Date lastUpdateTime;

	/**
	 * 是否有效:1是,0:否
	 */
	@Getter
	@Setter
	private Integer stat;
}
