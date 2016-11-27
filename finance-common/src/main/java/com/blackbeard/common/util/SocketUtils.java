package com.blackbeard.common.util;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.blackbeard.common.service.ICurrencyConfigService;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.IKlineRecordService;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.service.IUserInfoService;
import com.blackbeard.common.service.IUserLoginStateService;

public class SocketUtils {
	public static AbstractApplicationContext ctx;

	public static AbstractApplicationContext initApplicationContext() {
		if (ctx == null) {
			ctx = new ClassPathXmlApplicationContext(
					new String[] { "classpath*:/spring-config/applicationContext.xml" });
		}
		return ctx;
	}

	public static IKlineRecordService getKlineService() {
		IKlineRecordService klineRecordService = (IKlineRecordService) ctx
				.getBean("klineRecordService");
		return klineRecordService;
	}

	public static IUserInfoService getUserInfoService() {
		IUserInfoService userInfoService = (IUserInfoService) ctx
				.getBean("UserInfoService");
		return userInfoService;
	}
	
	public static IUserLoginStateService getUserLoginStateService(){
		IUserLoginStateService userLoginStateService = (IUserLoginStateService) ctx.getBean("UserLoginStateService");
		return userLoginStateService;
	}
	
	public static INextOrderService getNextOrderService(){
		INextOrderService nextOrderService = (INextOrderService) ctx.getBean("NextOrderService");
		return nextOrderService;
	}
	
	public static IHistoryOrderService getHistoryOrderService(){
		IHistoryOrderService historyOrderService = (IHistoryOrderService) ctx.getBean("HistoryOrderService");
		return historyOrderService;
	}
	
	public static ICurrencyConfigService  getCurrencyConfigService(){
		ICurrencyConfigService currencyConfigService = (ICurrencyConfigService) ctx.getBean("CurrencyConfigService");
		return currencyConfigService;
	}
	

	public static void main(String[] args) {
		initApplicationContext();
		IKlineRecordService a = getKlineService();
	}
}
