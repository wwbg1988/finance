package com.blackbeard.common.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class KlineRecordDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1583783687330303030L;

	// 货币
	@Getter
	@Setter
	private String currency;
	// 开盘价

	@Getter
	@Setter
	private String openPrice;
	// 收盘价

	@Getter
	@Setter
	private String closePrice;
	// 最高价

	@Getter
	@Setter
	private String highPrice;

	// 最低价
	@Getter
	@Setter
	private String lowPrice;

	// 浮点数
	@Getter
	@Setter
	private String decimals;

	@Getter
	@Setter
	private Long timeStamp;

	@Getter
	@Setter
	private int headerID;

}
