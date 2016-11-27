package com.blackbeard.common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 结算结果Dto
 * 
 * @author 刘博
 *
 */
public class AccountResultDto {
	/**
	 * 结算总量(交易量)
	 */
	@Getter
	@Setter
	private int accountCount;
	/**
	 * 结算数据总条数
	 */
	@Getter
	@Setter
	private int accountSize;
	/**
	 * 结算比例所得佣金
	 */
	@Getter
	@Setter
	private float accountCommission;

}
