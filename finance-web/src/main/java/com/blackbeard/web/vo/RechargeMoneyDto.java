package com.blackbeard.web.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class RechargeMoneyDto implements Serializable {

	@Getter
	@Setter
	private int id;// 用户mt4Id

	@Getter
	@Setter
	private double money;// 充值金额

	@Getter
	@Setter
	private String ip;

	@Getter
	@Setter
	private int lgmanage;

	@Getter
	@Setter
	private String lgpword;

	/**
	 * 创建时间
	 */
	@Getter
	@Setter
	private String comment;

}
