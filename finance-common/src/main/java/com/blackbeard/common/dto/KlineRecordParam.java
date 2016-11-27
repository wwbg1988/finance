package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class KlineRecordParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8774296025037525890L;
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 密码
	 */
	private String pwd;
	/**
	 * 货币名称
	 */
	private String cury;
	/**
	 * 开始时间
	 */
	private String bTime;
	/**
	 * 结束时间
	 */
	private String eTime;

	/**
	 * 是否查所有货币 :1是;0:否
	 */
	private int all;
	/**
	 * 查询k线类型：1：1分钟K线，5：5分钟K线，15：15分钟K线
	 */
	private int type;
	
	/**
	 * 货币名称集合 
	 */
	private  List<String> currencyNameList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCury() {
		return cury;
	}

	public void setCury(String cury) {
		this.cury = cury;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public List<String> getCurrencyNameList() {
		return currencyNameList;
	}

	public void setCurrencyNameList(List<String> currencyNameList) {
		this.currencyNameList = currencyNameList;
	}
	
	

}
