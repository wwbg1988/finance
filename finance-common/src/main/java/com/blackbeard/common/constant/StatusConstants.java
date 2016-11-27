package com.blackbeard.common.constant;

import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.UserLoginStateDto;

/**
 * 状态常量类
 * 
 * @author 刘博
 *
 */
public interface StatusConstants {
	// 返回成功
	static final int RETURN_SUCCESS = 200;
	// 返回失败
	static final int RETURN_FAILE = 500;
	// 0
	public static final int ZERO = 0;
	// 1
	public static final int ONE = 1;
	// 常量对象 HistoryOrderHandleThread线程使用
	public static NextOrderDto nextOrderDto = new NextOrderDto();
	// 用户登录状态对象 HistoryOrderHandleThread线程使用
	public static UserLoginStateDto userLoginStateDto = new UserLoginStateDto();
}
