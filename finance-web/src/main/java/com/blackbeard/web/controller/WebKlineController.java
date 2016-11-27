package com.blackbeard.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.KlineRecordParam;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.service.ICurrencyConfigService;
import com.blackbeard.common.service.IKlineRecordService;
import com.blackbeard.common.service.UserService;
import com.blackbeard.common.util.MD5Util;
import com.blackbeard.web.util.WebUtils;
import com.blackbeard.web.validator.WebKlineValidator;

@Controller
@RequestMapping("/web/kline")
public class WebKlineController {
	private static final Logger logger = Logger
			.getLogger(WebKlineController.class);

	@Autowired
	private IKlineRecordService klineRecordService;
	@Autowired
	ICurrencyConfigService currencyConfigService;

	@Resource(name = "userService")
	private UserService userService;

	@InitBinder
	public void initBinder(DataBinder binder) {
		binder.setValidator(new WebKlineValidator());
	}

	/**
	 * findKlineRecord 查询某个时间段的K线记录
	 * 
	 * @param klineRecordParam
	 *            K线查询对象
	 * @return Response<String>
	 * @throws Exception
	 * @exception
	 * @author 刘博
	 * @date 2016年6月22日 下午15:12:11
	 */
	@RequestMapping(value = "/findKlineRecord.do")
	@ResponseBody
	public String findKlineRecord(
			@Valid @ModelAttribute("klineRecordParam") KlineRecordParam klineRecordParam,
			BindingResult bindingResult) throws Exception {
		String resultString = null;
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			if (!CollectionUtils.isEmpty(errors)) {
				return resultString;
			}
		}
		PageData pd = new PageData();
		pd.put("USERNAME", klineRecordParam.getName());
		pd.put("PASSWORD", MD5Util.base64Encode(klineRecordParam.getPwd()));
		// 根据用户名密码查询用户数据
		PageData return_pd = userService.findByUserName(pd);
		if (return_pd == null) {
			return resultString;
		}

		// 查询出显示几种货币
		CurrencyConfigDto currencyConfigDto = new CurrencyConfigDto();
		currencyConfigDto.setIsEnable(1);
		List<CurrencyConfigDto> currencyConfigDtoList = currencyConfigService
				.findBy(currencyConfigDto);
		List<String> currencyNameList = new ArrayList<String>();
		for (CurrencyConfigDto configDto : currencyConfigDtoList) {
			currencyNameList.add(configDto.getCurrency());
		}
		klineRecordParam.setCurrencyNameList(currencyNameList);
		List<KlineRecordDto> list = klineRecordService
				.findklineRecord(klineRecordParam);
		if (CollectionUtils.isEmpty(list)) {
			return resultString;
		}
		if (klineRecordParam.getAll() == 1) {// 查找所有货币的结果集
			resultString = WebUtils.listToStringforAll(list);
		} else {// 查找某一种货币的结果集
			resultString = WebUtils.listToString(list);
		}
		logger.info("web返回的kline数据为:----" + resultString);
		return resultString;
	}
}
