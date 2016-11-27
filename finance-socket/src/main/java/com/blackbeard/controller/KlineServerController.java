package com.blackbeard.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.IKlineRecordService;
import com.blackbeard.common.service.IUserInfoService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.client.kline.ReceiveThread;
import com.blackbeard.socket.service.KlineHandleService;
import com.ssic.util.model.Response;

@Controller
@RequestMapping("/http/kline")
public class KlineServerController {
	private static final Logger logger = Logger
			.getLogger(KlineServerController.class);

	@Autowired
	private IKlineRecordService klineRecordService;
	@Autowired
	private KlineHandleService klineHandleService;
	@Autowired
	private ReceiveThread receiveThread;

	
	/**
	 * 获取当天k线数据
	 * 
	 * @return
	 */

	@RequestMapping(value = "/getlocalDayKline", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public Response<String> getlocalDayKline(
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String pwd) {
		logger.info("AppUserController user login name=" + name + "|pwd=" + pwd);
		Response<String> result = new Response<String>();
		result.setMessage("账号密码错误，请重新输入！");
		return result;

	}

	@RequestMapping("/test.do")
	@ResponseBody
	public String test() {
		System.out.println("klineRecordService=" + klineRecordService + "===="
				+ klineHandleService + "----");
		receiveThread.run();
		return "success";
	}
	
	@RequestMapping("/getUser.do")
	@ResponseBody
	public String getUser(){
		IUserInfoService userInfoService=	SocketUtils.getUserInfoService();		
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserName("kaka");
		List<UserInfoDto> list = userInfoService.findBy(userInfoDto);
		Integer blance = list.get(0).getBlance();
		double bdouble = blance;
		
		//Math.round(a)
		//Math.round(blance.doubleValue() / 100 * 100)/100.0;
		
		double kkd = Math.round(blance.doubleValue()/100*100)/100.0;
		System.out.println("kkd="+kkd);
		System.out.println("show double ="+454545454.00);
		System.out.println("blance="+blance);
		System.out.println("bdouble="+bdouble);
		return "success";
	}
	
	@RequestMapping("/getHistoryOrder.do")
	@ResponseBody
	public String getHistoryOrder(){
	 IHistoryOrderService historyOrderService =	SocketUtils.getHistoryOrderService();
	 HistoryOrderDto historyOrderDto = new HistoryOrderDto();
//	 historyOrderDto.setCloseprice(1.1110);
//	 historyOrderDto.setClosetime(1467628443);
//	 historyOrderDto.setCmd(0);
//	 historyOrderDto.setComment("ddddddddddddddddddd");
//	 historyOrderDto.setLoginID(18030158);
//	 historyOrderDto.setOpenprice(1.129740);
//	 historyOrderDto.setOpentime(1467628383);
//	 historyOrderDto.setProfit(-1000.0);
	 historyOrderDto.setSymbol("EURUSDDB");
	 historyOrderDto.setTick(457);
	 //historyOrderService.addHistoryOrder(historyOrderDto);
	  historyOrderService.findBy(historyOrderDto);
	 return "success";	
	}
	

}
