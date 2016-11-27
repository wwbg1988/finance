package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class HistoryOrderDto implements Serializable {

	@Getter
	@Setter
	private int id;
	@Getter
	@Setter
	private String closePrice; // 结束价格
	@Getter
	@Setter
	private String closeTime; // 结束时间
	@Getter
	@Setter
	private Integer cmd; // 买卖 0买 1卖
	@Getter
	@Setter
	private String comment; // 备注
	@Getter
	@Setter
	private long loginId; // mt4id
	@Getter
	@Setter
	private String openPrice; // 开盘价格
	@Getter
	@Setter
	private String openTime; // 开盘时间
	@Getter
	@Setter
	private float profit; // 价格
	@Getter
	@Setter
	private String symbol; // 货币名称
	@Getter
	@Setter
	private Integer total;// 数量
	@Getter
	@Setter
	private long tick; // 订单号
	@Getter
	@Setter
	private String eveningUp; // 是否平仓(1是,0否;web后台查询使用)

	@Getter
	@Setter
	private Integer userId;// 用户id

	@Getter
	@Setter
	private String userName; // 订单所属用户名
	@Getter
	@Setter
	private Date createTime;// 创建时间
	@Getter
	@Setter
	private String openTimeFormatString; // 开始时间格式化字符串

	@Getter
	@Setter
	private String openTimeStart; // 开盘价开始查询时间
	@Getter
	@Setter
	private String openTimeEnd; // 开盘价结束查询时间
	@Getter
	@Setter
	private String closeTimeFormatString;// 结束时间格式化字符串
	@Getter
	@Setter
	private String createTimeFormatString;// 创建时间格式化字符串

}
