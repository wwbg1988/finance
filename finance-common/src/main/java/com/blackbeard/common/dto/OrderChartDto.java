package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单图表dto类
 * 
 * @author Administrator
 *
 */
public class OrderChartDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6180772068851253362L;

	/**
	 * 日期
	 */
	@Getter
	@Setter
	private String name;
	/**
	 * 数量
	 */
	@Getter
	@Setter
	private String value;
	/**
	 * 颜色
	 */
	@Getter
	@Setter
	private String color;

}
