package com.blackbeard.socket.server.dto;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import com.blackbeard.common.dto.PCUserDto;

/**
 * 服务端参数对象dto
 * 
 * @author 刘博
 *
 */
public class PCServerDto implements Serializable {

	// 客户端socket对象
	@Getter
	@Setter
	private Socket Socket;
	// 当前的用户信息
	@Getter
	@Setter
	private PCUserDto pcUserDto;
	// 所有用户的登录信息集合
	@Getter
	@Setter
	private Map<Long, PCUserDto> mapUserLogin;
	
	@Getter
	@Setter
	private Integer blanceIndex;   //发送余额次数


}
