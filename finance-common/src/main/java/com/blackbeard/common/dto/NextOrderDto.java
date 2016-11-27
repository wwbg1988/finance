package com.blackbeard.common.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class NextOrderDto implements Serializable {

	@Getter
	@Setter
	private Integer id; // 主键
	@Getter
	@Setter
	private Long loginId; // 用户mt4账号
	@Getter
	@Setter
	private String currencyType; // 货币名称
	@Getter
	@Setter
	private Integer direction; // 买或卖
	@Getter
	@Setter
	private String price; // 价格
	@Getter
	@Setter
	private Integer tick; // 订单号
	@Getter
	@Setter
	private String time; // 时间戳
	@Getter
	@Setter
	private Integer total; // 数量
	@Getter
	@Setter
	private String comment; // 涨跌分钟
	@Getter
	@Setter
	private int stat; // 状态
	@Getter
	@Setter
	private Date createTime; // 创建时间
	@Getter
	@Setter
	private Date updateTime; // 更新时间
	@Getter
	@Setter
	private Integer userId; // 用户id
	@Getter
	@Setter
	private String userName; // 下单所属用户名
	@Getter
	@Setter
	private String createTimeFormatString;// 创建时间格式化字符串
	@Getter
	@Setter
	private String timeFormatString; // 下单时间格式化字符串
	@Getter
	@Setter
	private String openTime; // 开仓时间(时间戳)
	@Getter
	@Setter
	private String closeTime; // 下单结束时间(时间戳)
	@Getter
	@Setter
	private String closePrice; // 下单时的K线货币结束价格
	@Getter
	@Setter
	private float profit;// 盈亏

}
