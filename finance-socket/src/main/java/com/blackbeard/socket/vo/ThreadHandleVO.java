package com.blackbeard.socket.vo;

import java.io.Serializable;

import javax.jms.ConnectionFactory;

import org.apache.activemq.pool.PooledConnection;

/**
 * ThreadHandleVO:线程处理参数VO对象
 * 
 * @author 刘博
 * 
 */
public class ThreadHandleVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2771725982028901047L;
	// 客户端收到的的(MP4)消息
	private String message;

	private Long currentTime;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

}
