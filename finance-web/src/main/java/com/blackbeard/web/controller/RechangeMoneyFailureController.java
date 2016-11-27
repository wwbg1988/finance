package com.blackbeard.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.Json;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.dto.UserRechangeFailureRecordDto;
import com.blackbeard.common.service.IUserRechangeFailureRecordService;
import com.ssic.util.constants.DataStatus;

@Controller
@RequestMapping("/rechangeFailureController")
public class RechangeMoneyFailureController extends BaseController {

	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private IUserRechangeFailureRecordService rechangeFailureRecordService;

	@Autowired
	private WebUserController webUserController;

	@RequestMapping("/findAllRechangeMoneyFailure")
	public ModelAndView findAllUserInfo(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		String serverIp = pd.getString("serverIp");

		int count = rechangeFailureRecordService.findCount();
		UserRechangeFailureRecordDto rechangeFailureRecordDto = new UserRechangeFailureRecordDto();
		LimitPageDto limitPageDto;
		if (count == 0) {
			limitPageDto = new LimitPageDto();
		} else {
			limitPageDto = pageFunction.getLimitPage(page, count);
		}
		List<UserRechangeFailureRecordDto> rechangeFailureRecordDtoList;
		// 判断当前登录用户是否是超级管理员
		boolean isAdminUser = webUserController.isAdmin();
		if (!isAdminUser) {// 不是超级管理员,什么都看不到
			rechangeFailureRecordDtoList = new ArrayList<UserRechangeFailureRecordDto>();
		} else {
			rechangeFailureRecordDtoList = rechangeFailureRecordService
					.findListByPage(rechangeFailureRecordDto, limitPageDto);
		}
		// 设置page信息
		pageFunction.setLimitPage(rechangeFailureRecordDtoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/rechange/rechangeFailure_list");
		mv.addObject("rechangeFailureList", rechangeFailureRecordDtoList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 
	 * delete：删除充值失败记录
	 * 
	 * @param id
	 * @return
	 * @exception
	 * @author 刘博
	 * @date 2016年7月14日 上午9:55:02
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		{
			UserRechangeFailureRecordDto failureRecordDto = rechangeFailureRecordService
					.findById(id);
			if (null != failureRecordDto) {
				failureRecordDto.setStat(DataStatus.DISABLED);
				rechangeFailureRecordService.delete(failureRecordDto);
				j.setMsg("删除充值失败记录成功！");
				j.setSuccess(true);
			} else {
				j.setMsg("删除充值失败记录异常,请联系管理员!");
				j.setSuccess(false);
			}
		}
		return j;
	}
}
