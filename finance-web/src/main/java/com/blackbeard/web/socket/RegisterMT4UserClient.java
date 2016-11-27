package com.blackbeard.web.socket;

import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blackbeard.common.constant.WebConstants;

/**
 * 用户注册MT4账号:短连接
 * 
 * @author 刘博
 *
 */
public class RegisterMT4UserClient {

	protected static final Log logger = LogFactory
			.getLog(RegisterMT4UserClient.class);

	public static String registerMt4User(String userParamCall) {
		Socket socket;
		try {
			socket = new Socket(WebConstants.WEB_MT4_LOGIN_SERVER_IP,
					WebConstants.WEB_MT4_LOGIN_SERVER_PORT);
			ExecutorService pool = Executors.newFixedThreadPool(100);
			Callable c1 = new RegisterMT4UserCall(userParamCall, socket);
			Future f1 = pool.submit(c1);
			userParamCall = (String) f1.get();
			pool.shutdown();
		} catch (Exception e) {
			logger.error("-------------获取注册接口失败-------------");
			e.printStackTrace();
		}
		return userParamCall;

	}

	public static String rechargeMoneyForUser(String rechargeMoneyParam) {
		Socket socket;
		try {
			socket = new Socket(WebConstants.WEB_MT4_LOGIN_SERVER_IP,
					WebConstants.WEB_MT4_RECHARGE_PORT);
			ExecutorService pool = Executors.newFixedThreadPool(100);
			Callable c1 = new RegisterMT4UserCall(rechargeMoneyParam, socket);
			Future f1 = pool.submit(c1);
			rechargeMoneyParam = (String) f1.get();
			pool.shutdown();
		} catch (Exception e) {
			logger.error("-------------获取注册接口失败-------------");
			e.printStackTrace();
		}
		return rechargeMoneyParam;

	}
}
