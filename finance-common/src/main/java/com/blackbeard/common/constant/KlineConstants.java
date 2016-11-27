package com.blackbeard.common.constant;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.PCUserDto;
import com.ssic.util.PropertiesUtils;

/**
 * K线常量类集合
 * 
 * @author Administrator
 *
 */
public class KlineConstants {
	// 1分钟
	public static final int KLINE_ONE = 1;
	// 5分钟
	public static final int KLINE_FIVE = 5;
	// 15分钟
	public static final int KLINE_FIFTEEN = 15;
	// K线功能号
	public static final String KLINE_FUNCTION_NUMBER = "11";
	// K线协议号
	public static final String KLINE_PROTOCOL_NUMBER = "10";
	// K线语言
	public static final String KLINE_LANGUAGES = "10";
	// 下单盈利率
	public static final float NEXT_ORDER_PROFIT_RATIO = Float
			.parseFloat(PropertiesUtils.getProperty("nextorder_profit_ratio"));

	// 0
	public static final int ZERO = 0;
	// 1
	public static final int ONE = 1;
	// 12
	public static final int TWELVE = 12;
	// 20
	public static final int TWENTY = 20;
	// 1000
	public static final int ONE_THOUSAND = 1000;
	// 9999
	public static final int NINE_NINE_NINE_NINE = 9999;

	// mp4服务器地址
	public static String MP4_KLINE_ADDRESS = PropertiesUtils
			.getProperty("mp4Server.kline.address");
	// mp4端口号
	public static int MP4_KLINE_PORT = Integer.valueOf(PropertiesUtils
			.getProperty("mp4Server.kline.port"));
	// k线订阅名称
	public static String KLINE_TOPIC_NAME = PropertiesUtils
			.getProperty("activemq.topic.kline.name");

	// java服务器端口号
	public static int JAVA_SERVER_PORT = Integer.valueOf(PropertiesUtils
			.getProperty("java.server.port"));

	// mt4下订单地址
	public static String MT4_NEXT_ORDER_ADDRESS = PropertiesUtils
			.getProperty("mp4Server.nextorder.address");
	// mt4下订单端口号
	public static int MT4__NEXT_ORDER_PORT = Integer.valueOf(PropertiesUtils
			.getProperty("mp4Server.nextorder.port"));
	// 历史订单server端口
	public static int SERVER_HISTORY_ORDER_PORT = Integer
			.valueOf(PropertiesUtils.getProperty("server.historyorder.port"));
	// 历史订单队列名称
	public static String HISTORY_ORDER_QUEUE_NAME = PropertiesUtils
			.getProperty("activemq.queue.order.name");

	public static final int WEB_KLINE_LENGTH = 75;

	// 读写的编码方式
	public static final String ENCODEING_TYPE = "UTF-8";

	// pc发送信息功能号 心跳包
	public static final String PC_MESSAGE_JUMP = "00";
	// pc发送信息功能号 登录
	public static final String PC_MESSAGE_LOGIN = "13";
	// pc发送信息功能号 登出
	public static final String PC_MESSAGE_LOGIN_OUT = "14";
	// pc发送信息功能号 k线
	public static final String PC_MESSAGE_KLINE = "11";
	// pc发送信息功能号 订单
	public static final String PC_MESSAGE_ORDER = "15";
	// pc发送信息功能号 订单结果
	public static final String PC_MESSAGE_ORDER_RESULT = "16";
	// pc发送信息功能号 用户余额
	public static final String PC_MESSAGE_USER_BLANCE = "17";
	// pc发送信息功能号 错误
	public static final String PC_MESSAGE_ERROR = "18";

	// 初始头部的语言
	public static final int init_languageId = 10;
	// 初始头部的协议号
	public static final int init_agreementId = 10;
	// 用户登录信息集合
	public static Map<Long, PCUserDto> mapUserLogin = new Hashtable<Long, PCUserDto>();
	// 创建一个线程安全的list.socket集合
	public static List<Socket> SOCKET_ALLUSER =Collections.synchronizedList(new ArrayList<Socket>());
	// 需要显示的货币list集合
	public static List<CurrencyConfigDto> LIST_CURRENCY = new ArrayList<CurrencyConfigDto>();

	// -------------------错误信息--------------------------

	public static final int RESULT_NUM_SUCCESS = 200; // 返回成功编号

	public static final int RESULT_NUM_FALIE = 500; // 返回失败编号

	// 用户登录信息不存在
	public static final String RESULT_ERROR_USE_NO_EXIST = "USE_NO_EXIST";
	// 订单不存在
	public static final String RESULT_ERROR_ORDER_NO_EXIST = "ORDER_NO_EXIST";
	// 余额小于下单金额
	public static final String RESULT_ERROR_BLANCE_SHORT = "BLANCE_SHORT";
	// 长度校验失败
	public static final String RESULT_ERROR_LENGTH_ERROR = "LENGTH_ERROR";
	// 信息解析错误
	public static final String RESULT_ERROR_RESOLVE_ERROR = "RESOLVE_ERROR";
	// 组装数据失败
	public static final String RESULT_ERROR_GROUP_ERROR = "GROUP_ERROR";
	// mt4订单号为负数
	public static final String RESULT_ERROR_ORDER_RESULT_ERROR = "ORDER_RESULT_ERROR";
	// 获取mt4下订单数据错误
	public static final String RESULT_ERROR_NEXT_ORDER_ERROR = "NEXT_ORDER_ERROR";

	// -------登录-------
	public static final String RESULT_ERROR_CODE = "LOGIN_ERROR"; // 返回错误
	public static final String RESULT_SUCCESS_CODE = "200"; // 返回成功
	public static final String RESULT_NAME_NOT_EXIST = "NAME_NOT_EXIST"; // 用户名不存在
	public static final String RESULT_PASSWORD_ERROR = "PASSWORD_ERROR"; // 密码错误
	public static final String RESULT_LOGINGED = "LOGINGED"; // 已经登录不能重复登录
	public static final String RESULT_NOT_LOGINGED = "NOT_LOGINGED"; // 该用户还没有登录
}
