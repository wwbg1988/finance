package com.blackbeard.common.constant;

import java.util.HashMap;
import java.util.Map;

import com.ssic.util.PropertiesUtils;

public class WebConstants {
	// WEB 注册MT4功能号
	public static final String WEB_LOGIN_FUNCTION_NUMBER = "10";

	// mp4注册账号服务器地址
	public static String WEB_MT4_LOGIN_SERVER_IP = PropertiesUtils
			.getProperty("web.register.address");
	// mp4注册账号服务器端口号
	public static int WEB_MT4_LOGIN_SERVER_PORT = Integer
			.valueOf(PropertiesUtils.getProperty("web.register.port"));
	// mp4账号充值服务端口号
	public static int WEB_MT4_RECHARGE_PORT = Integer.valueOf(PropertiesUtils
			.getProperty("web.recharge.port"));
	// 0买,1卖
	public final static Map cmdMap = new HashMap() {
		{
			put("全部", 2);
			put("买入", 0);
			put("卖出", 1);
		}
	};
	// 是否平仓集合
	public final static Map eveningMap = new HashMap() {
		{
			put("否", 0);
			put("是", 1);
		}
	};

	// 用户状态Map集合:状态,0 待审核,1 注册完成,2 在线,3 离线
	public final static Map stateMap = new HashMap() {
		{
			put("0", "待审核");
			put("1", "注册完成");
			put("2", "在线");
			put("3", "离线");
			put("4", "全部");
		}
	};

	public static String[] colorArr = { "AFD8F8", "F6BD0F", "8BBA00", "FF8E46",
			"008E8E", "D64646", "8E468E", "588526", "B3AA00", "008ED6",
			"9D080D", "A186BE", "#EE3B3B", "#EE00EE", "#EE0000", "#D02090",
			"#D4D4D4", "#FF8C69", "#CD1076", "#C0FF3E", "#BFBFBF", "#B4EEB4",
			"#B22222", "#9ACD32", "#9BCD9B", "#9FB6CD", "#A0522D" };

}
