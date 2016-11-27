package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class Mt4OrderDto implements Serializable {

	// mt4订单信息,将对象转为json,属性不能修改
	@Getter
	@Setter
	private long loginId; // mt4账号
	@Getter
	@Setter
	private String currencyType; // 货币名称
	@Getter
	@Setter
	private Double price; // 货币价格
	@Getter
	@Setter
	private long total; // 购买数量
	@Getter
	@Setter
	private int direction; // 买卖类型 0买 1卖
	@Getter
	@Setter
	private String comment; // 类型及时间

}
