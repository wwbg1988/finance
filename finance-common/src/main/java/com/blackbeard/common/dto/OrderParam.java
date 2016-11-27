package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * web 历史订单接口参数对象
 * 
 * @author 刘博
 *
 */
public class OrderParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3505231080469189805L;

	/**
	 * 用户名称
	 */
	private String name;

	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 开始时间
	 */
	private String bTime;
	/**
	 * 结束时间
	 */
	private String eTime;

	/**
	 * 订单编号
	 */
	private Long orderNo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getbTime() {
		return bTime;
	}

	public void setbTime(String bTime) {
		this.bTime = bTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

}
