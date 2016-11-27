package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class PCOrderDto implements Serializable {

	// pc端的订单数据

	@Getter
	@Setter
	private String currencyName; // 货币名称
	@Getter
	@Setter
	private String price; // 价格
	@Getter
	@Setter
	private String total; // 数量
	@Getter
	@Setter
	private String upOrDown; // 张或跌 DOWN跌 UP涨
	@Getter
	@Setter
	private String buyOrSell;  //买或卖   0卖1买
	@Getter
	@Setter
	private String minNo; // 多少分钟

}
