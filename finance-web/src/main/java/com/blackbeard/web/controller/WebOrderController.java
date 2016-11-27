package com.blackbeard.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.OrderParam;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.pojo.order.NextOrder;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.service.UserService;
import com.blackbeard.web.util.WebUtils;
import com.blackbeard.web.validator.WebOrderValidator;

@Controller
@RequestMapping("/web/order")
public class WebOrderController {

	private static final Logger logger = Logger
			.getLogger(WebOrderController.class);

	@Autowired
	private IHistoryOrderService historyOrderService;
	@Autowired
	private INextOrderService nextOrderService;

	@Resource(name = "userService")
	private UserService userService;

	@InitBinder
	public void initBinder(DataBinder binder) {
		binder.setValidator(new WebOrderValidator());
	}

	/**
	 * findHistoryOrder 查询历史订单记录
	 * 
	 * @param orderParam
	 *            订单查询对象
	 * @return String
	 * @throws Exception
	 * @exception
	 * @author 刘博
	 * @date 2016年7月11日 下午15:12:11
	 */
	@RequestMapping(value = "/findHistoryOrder.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String findHistoryOrder(
			@Valid @ModelAttribute("orderParam") OrderParam orderParam,
			BindingResult bindingResult) throws Exception {
		String resultString = null;
		if (bindingResult.hasErrors()) {
			List<ObjectError> errors = bindingResult.getAllErrors();
			if (!CollectionUtils.isEmpty(errors)) {
				return resultString;
			}
		}
		PageData pd = new PageData();
		pd.put("USERNAME", orderParam.getName());
		PageData return_pd = userService.findByUserName(pd);
		if (return_pd == null) {
			return resultString;
		}
		orderParam.setUserId(return_pd.getString("USER_ID"));
		List<HistoryOrderDto> list = historyOrderService
				.findOrderList(orderParam);
		if (CollectionUtils.isEmpty(list)) {
			return resultString;
		}
		// 把历史订单集合组装专户中为特定数据结构，返回;
		resultString = WebUtils.listToStringforOrder(list);
		logger.info("web返回的历史订单 数据为:----" + resultString);
		return resultString;
	}

	/**
	 * findHistoryOrder 查询历史订单记录
	 * 
	 * @param orderParam
	 *            订单查询对象
	 * @return String
	 * @throws Exception
	 * @exception
	 * @author 刘博
	 * @date 2016年7月11日 下午15:12:11
	 */
	@RequestMapping(value = "/findHistoryOrderByOrderNo.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String findHistoryOrderByOrderNo(@Param("name") String name,
			@Param("orderNo") Long orderNo) throws Exception {
		String resultString = null;
		if (StringUtils.isEmpty(name)) {// 用户名为空，返回空
			return resultString;
		}
		PageData pd = new PageData();
		pd.put("USERNAME", name);
		PageData return_pd = userService.findByUserName(pd);
		if (return_pd == null) {// 用户名查询不到,返回空
			return resultString;
		}
		if (orderNo == null) {// 订单编号为空，返回空
			return resultString;
		}
		OrderParam orderParam = new OrderParam();
		orderParam.setOrderNo(orderNo);
		orderParam.setUserId(return_pd.getString("USER_ID"));
		List<HistoryOrderDto> list = historyOrderService
				.findOrderList(orderParam);
		if (CollectionUtils.isEmpty(list)) {
			return resultString;
		}
		// 把历史订单集合组装专户中为特定数据结构，返回;
		resultString = WebUtils.listToStringforOrder(list);
		logger.info("web返回的历史订单 数据为:----" + resultString);
		return resultString;
	}

	/**
	 * findOnlineOrder 查询在线订单记录
	 * 
	 * @param orderParam
	 *            订单查询对象
	 * @return String
	 * @throws Exception
	 * @exception
	 * @author 刘博
	 * @date 2016年7月14日 下午15:12:11
	 */
	@RequestMapping(value = "/findOnlineOrder.do", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String findOnlineOrder(@Param("name") String name) throws Exception {
		String resultString = null;
		if (StringUtils.isEmpty(name)) {// 用户名为空，返回空
			return resultString;
		}
		PageData pd = new PageData();
		pd.put("USERNAME", name);
		PageData return_pd = userService.findByUserName(pd);
		if (return_pd == null) {
			return resultString;
		}
		NextOrderDto nextOrderDto = new NextOrderDto();
		nextOrderDto.setUserId(Integer.valueOf(return_pd.getString("USER_ID")));
		List<Integer> userIdList = new ArrayList<Integer>();
		List<NextOrder> list = nextOrderService.findList(userIdList,
				nextOrderDto);
		if (CollectionUtils.isEmpty(list)) {
			return resultString;
		}
		// 把在线订单集合组装专户中为特定数据结构，返回;
		resultString = WebUtils.listToStringforOnlineOrder(list);
		logger.info("web返回的历史订单 数据为:----" + resultString);
		return resultString;
	}
}
