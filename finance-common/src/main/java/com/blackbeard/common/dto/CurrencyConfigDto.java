package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 货币配置项dto
 * 
 * @author liubo
 *
 */
public class CurrencyConfigDto implements Serializable {

	/**
	 * 主键id
	 */
	@Getter
	@Setter
	private String id;

	/**
	 * 货币名称
	 */
	@Getter
	@Setter
	private String currency;

	/**
	 * 货币是否启用:1是;0:否
	 */
	@Getter
	@Setter
	private Integer isEnable;

	/**
	 * 状态,是否有效:1是;0:否
	 */
	@Getter
	@Setter
	private int stat;

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
	
	@Getter
	@Setter
	private String currencyRates; //货币利率

}
