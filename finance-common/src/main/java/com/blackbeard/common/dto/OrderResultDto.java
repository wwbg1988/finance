package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class OrderResultDto implements Serializable{
//需要解析json，属性不能修改
	
	@Getter
	@Setter
	private String closeprice;   //结束价格
	@Getter
	@Setter
	private String closetime;    //结束时间
	@Getter
	@Setter
	private int cmd;   //买卖   0买 1卖
	@Getter
	@Setter
	private String comment;   //备注
	@Getter
	@Setter
	private long loginID;   //mt4id
	@Getter
	@Setter
	private String openprice;   //开盘价格
	@Getter
	@Setter
	private long opentime;    //开盘时间
	@Getter
	@Setter
	private long profit;   //价格
	@Getter
	@Setter
	private String symbol;    //货币名称
	@Getter
	@Setter
	private Integer tick;   //订单号
    @Getter
    @Setter
	private Integer  total ; //订单量
	
}
