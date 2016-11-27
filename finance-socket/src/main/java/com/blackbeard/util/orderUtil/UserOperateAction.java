package com.blackbeard.util.orderUtil;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.GroupHandleDto;
import com.blackbeard.common.dto.PCUserDto;
import com.blackbeard.common.dto.ResultMessageDto;
import com.blackbeard.common.util.ResolveStickPackageAction;
import com.blackbeard.socket.server.dto.PCServerDto;
import com.blackbeard.util.Tools;
import com.ssic.util.StringUtils;

public class UserOperateAction {

	// 执行用户的各种操作
	private static final Logger logger = Logger
			.getLogger(UserOperateAction.class);

	private Socket socket;
	private PCUserDto initPCUser;
	private Map<Long, PCUserDto> mapUserLogin;

	public UserOperateAction(PCServerDto pcServerDto) {
		this.socket = pcServerDto.getSocket();
		this.initPCUser = pcServerDto.getPcUserDto();
		this.mapUserLogin = pcServerDto.getMapUserLogin();
	}

	private ResolveStickPackageAction resolveStickPackageAction = new ResolveStickPackageAction();

	public List<ResultMessageDto> UserOperToStr(String temp) {
		List<ResultMessageDto> listresult = new ArrayList<ResultMessageDto>();
		// 粘包处理
		temp = resolveStickPackageAction.getMt4Message(temp);
		List<String> list_pc_msg = resolveStickPackageAction
				.splitPCMessage(temp);
        //校验粘包处理后的字符串链表是否为空
		if (CollectionUtils.isEmpty(list_pc_msg)) {
			ResultMessageDto resultMessageDto =  pcMsgIsEmpty();
			listresult.add(resultMessageDto);
			return listresult;
		}
		// 循环处理每一条信息
		for (String pcmsg : list_pc_msg) {
			//base64解密
			pcmsg = BaseMessageInfo.resolveBase64Str(pcmsg);
			if(StringUtils.isEmpty(pcmsg)){
				ResultMessageDto resultMessageDto64 = new ResultMessageDto();
				resultMessageDto64.setPcUserDto(initPCUser);
				Tools.setResultMessageDto(resultMessageDto64, 500, KlineConstants.RESULT_ERROR_RESOLVE_ERROR, "解析失败");
				listresult.add(resultMessageDto64);
				continue;
			}
			logger.error("get_pc_message="+pcmsg);
			ResultMessageDto resultMessageDto = handlePcMsg(pcmsg);
			listresult.add(resultMessageDto);
		}
		return listresult;
	}


    //处理pc发送的每一条消息
	public ResultMessageDto handlePcMsg(String message) {
		// 一条pc消息对应生成一个对应的结果对象
		ResultMessageDto resultMessageDto = new ResultMessageDto();
		resultMessageDto.setPcUserDto(initPCUser);
		// 将pc发送的消息分别解析成handle,body
		Map<String, Object> map_all = BaseMessageInfo.resolveAll(message);
        //校验解析出来的handle，body是否正确
		if(!checkResolveMsg(map_all, resultMessageDto)){
			return resultMessageDto;
		}
        //获取body 
		String body = (String) map_all.get("body");
		// 获取当前登录信息的功能号
		String function_id = getFunctionId(map_all);
		resultMessageDto.setFunctionId(function_id);
		// 如果是00心跳则直接返回
		if(functionidIsJump(function_id,resultMessageDto)){
			return resultMessageDto;
		}
		
		if (initPCUser.getLoginFlag() == 0) {
			//用户没有登录的操作
			pcUserNoLoginedOper(function_id,body,resultMessageDto);
		} else {
			//用户登录的操作
			pcUserLoginedOper(function_id,body,resultMessageDto);
		}
		return resultMessageDto;
	}
	
	//pcmsg为空，为resultMessageDto赋值
	public ResultMessageDto pcMsgIsEmpty() {
		ResultMessageDto resultMessageDto = new ResultMessageDto();
		resultMessageDto.setPcUserDto(initPCUser);
		Tools.setResultMessageDto(resultMessageDto, 500,
				KlineConstants.RESULT_ERROR_RESOLVE_ERROR, "粘包解析错误");
		logger.error("-----------粘包解析错误------------");
		return resultMessageDto;
	}

	//获取该条pcmsg的功能号
	public String getFunctionId(Map<String, Object> map) {
		GroupHandleDto groupHandleDto = (GroupHandleDto) map
				.get("groupHandleDto");
		return groupHandleDto.getFunctionId();
	}
	
	//对pcmsg进行校验
	public boolean checkResolveMsg(Map<String, Object> map_all,ResultMessageDto resultMessageDto){
		if (map_all == null || map_all.get("result_code") == null
				|| map_all.get("body") == null) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_RESOLVE_ERROR, "解析错误");
			logger.error("-----------解析错误------------");
			return false;
		}
		int result_code = (int) map_all.get("result_code");
		if (result_code != 200) {
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_ERROR_LENGTH_ERROR, "解析头部验证长度失败");
			logger.error("-------resolve pc handle:解析头部验证长度失败-------");
			return false;
		}
		return true;
	}

	//验证功能号是否是心跳
	public boolean functionidIsJump(String functionId,ResultMessageDto resultMessageDto){
		if (KlineConstants.PC_MESSAGE_JUMP.equals(functionId)) {
			Tools.setResultMessageDto(resultMessageDto, 200,
					KlineConstants.PC_MESSAGE_JUMP, null);
			return true;
		}else{
			return false;
		}
	}
	
	//用户已经登录，处理pc消息
	public void pcUserNoLoginedOper(String function_id,String body,ResultMessageDto resultMessageDto){
		// 用户必须登录成功才能进行其他的操作,并且登录成功之后保存用户信息
		if (KlineConstants.PC_MESSAGE_LOGIN.equals(function_id)) {
			// 用户登录
			new LoginUserDetailAction(initPCUser, socket, mapUserLogin)
					.pcUserLogin(body, resultMessageDto);
		} else {
			// 用户没有登录，不能进行其他的操作
			logger.error("-------------用户还没有登录--------------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_NOT_LOGINGED, "当前用户未登录");
		}
	}
	
	//用户没有登录，处理pc消息
	public void pcUserLoginedOper(String function_id,String body,ResultMessageDto resultMessageDto){
		if (KlineConstants.PC_MESSAGE_LOGIN_OUT.equals(function_id)) {
			// 用户登出
			new LoginOutUserDetailAction(initPCUser, mapUserLogin).pcUserLoginOut(body, socket, resultMessageDto);
		} else if (KlineConstants.PC_MESSAGE_ORDER.equals(function_id)) {
			// 用户下订单
			 SendOrderAction.pcUserOrder(body, socket,initPCUser, resultMessageDto);
		} else if (KlineConstants.PC_MESSAGE_LOGIN.equals(function_id)) {
			// 用户登录
			logger.error("----------已经有用户登录------------");
			Tools.setResultMessageDto(resultMessageDto, 500,
					KlineConstants.RESULT_LOGINGED, "已经有用户登录");
		} else {
			// logger.error("----------登录之后进行其他操作------------");
			resultMessageDto.setResultNum(200);
		}
	}
	
	

}
