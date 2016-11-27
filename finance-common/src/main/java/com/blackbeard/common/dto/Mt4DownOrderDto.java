package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Mt4DownOrderDto implements Serializable{
     //解析json,类的属性不能更改
	//下订单类
	//将mt4下订单数据解析成对象
	//{"comment":"UP5min","currencyType":"EURUSD","direction":1,"loginId":18030158,"price":1.129740,"tick":71,"time":1467347591,"total":100}
	
	@Getter
	@Setter
	private String comment;  //分钟数张或跌
	@Getter
	@Setter
	private String currencyType;  //货币名称
	@Getter
	@Setter
	private int direction;  //买或卖    1卖  0买
	@Getter
	@Setter
	private long loginId;   //用户的mt4id
	@Getter
	@Setter
	private String price;   //价格
	@Getter
	@Setter
	private Integer tick;  //订单号
	@Getter
	@Setter
	private String time;  //时间戳
	@Getter
	@Setter
	private Integer total;  //数量
	
	
	
	
	
	
	
	
	
}
