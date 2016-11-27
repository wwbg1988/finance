package com.blackbeard.common.util;

import org.apache.log4j.Logger;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.ResultMessageDto;

public class ReturnErrorMessageAction {

	private static final Logger logger = Logger
			.getLogger(ReturnErrorMessageAction.class);
	//初始化错误信息
	public ResultMessageDto initResultMessage(){
		ResultMessageDto resultMessageDto = new ResultMessageDto();
		resultMessageDto.setResultNum(200);
		return resultMessageDto;
	}
	
	
	//打印错误信息
	public void logResultMessage(ResultMessageDto resultMessageDto){
		if (resultMessageDto!=null) {
			if (resultMessageDto.getResultNum()!=KlineConstants.RESULT_NUM_SUCCESS) {
				logger.error(resultMessageDto.getResultMessage());
			}
		}
	}
	
	
	
}
