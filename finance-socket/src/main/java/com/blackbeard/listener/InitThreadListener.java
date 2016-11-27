package com.blackbeard.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.blackbeard.common.service.IKlineRecordService;

public class InitThreadListener implements ServletContextListener {

	protected static final Log logger = LogFactory
			.getLog(InitThreadListener.class);

	@Autowired
	private IKlineRecordService klineRecordService;
	
	
	public void setKlineRecordService(IKlineRecordService klineRecordService) {
		this.klineRecordService = klineRecordService;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("---klineRecordService---"+klineRecordService);
	/*	new initThread2().start();
		new initThread1().start();*/

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("启动的时候执行了InitThreadListener方法：----------");
		// web 启动时执行

	}

}
