package com.blackbeard.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.common.dto.AccountConfigDto;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.Json;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.service.IAccountConfigService;
import com.ssic.util.constants.DataStatus;

@Controller
@RequestMapping("/accountingConfig")
public class AccountConfigController extends BaseController {

	@Autowired
	private IAccountConfigService accountConfigService;
	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private WebUserController webUserController;

	@RequestMapping("/findAccounting")
	public ModelAndView findAllUserInfo(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		int count = accountConfigService.findCount();
		AccountConfigDto AccountConfigDto = new AccountConfigDto();
		// 获取分页的开始结束
		LimitPageDto limitPageDto = pageFunction.getLimitPage(page, count);
		List<AccountConfigDto> AccountConfigDtoList;
		// 判断当前登录用户是否是超级管理员
		boolean isAdminUser = webUserController.isAdmin();
		if (!isAdminUser) {// 不是超级管理员,什么都看不到
			AccountConfigDtoList = new ArrayList<AccountConfigDto>();
		} else {
			AccountConfigDtoList = accountConfigService.findListByPage(
					AccountConfigDto, limitPageDto);
		}
		// 设置page信息
		pageFunction.setLimitPage(AccountConfigDtoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/accountConfig/accountConfig_list");
		mv.addObject("accountConfigList", AccountConfigDtoList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 跳转到新增结算配置项页面
	 */
	@RequestMapping(value = "/addPage")
	public ModelAndView addPage(AccountConfigDto accountConfigDto)
			throws Exception {
		ModelAndView mv = this.getModelAndView();
		accountConfigDto.setId(UUID.randomUUID().toString());
		mv.setViewName("web/accountConfig/accountConfig_add");
		mv.addObject("accountConfigDto", accountConfigDto);
		return mv;
	}

	/**
	 * 新增结算配置项
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Json add(AccountConfigDto accountConfigDto) throws Exception {
		Json j = new Json();
		AccountConfigDto accountConfig_Dto = new AccountConfigDto();
		List<AccountConfigDto> accountConfigDtoList = accountConfigService
				.findListByPage(accountConfig_Dto, null);
		if (!CollectionUtils.isEmpty(accountConfigDtoList)) {
			j.setSuccess(false);
			j.setMsg("结算配置项已经存在，请勿重复添加");
			j.setObj(accountConfig_Dto);
			return j;
		} else {
			accountConfigService.insert(accountConfigDto);
		}
		j.setSuccess(true);
		j.setMsg("新增结算配置项成功!");
		j.setObj(accountConfigDto);
		return j;
	}

	/**
	 * 跳转到编辑mt4配置项页面
	 */
	@RequestMapping(value = "/editPage")
	public ModelAndView editPage(AccountConfigDto configDto) throws Exception {
		ModelAndView mv = this.getModelAndView();
		AccountConfigDto AccountConfigDto = accountConfigService
				.findById(configDto.getId());
		mv.addObject("accountConfigDto", AccountConfigDto);
		mv.setViewName("web/accountConfig/accountConfig_edit");
		return mv;
	}

	/**
	 * 编辑保存结算配置项
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(AccountConfigDto configDto) {
		Json j = new Json();
		if (!StringUtils.isEmpty(configDto.getAccountPercent())) {
			List<AccountConfigDto> AccountConfigDtoList = accountConfigService
					.findListByPage(configDto, null);
			if (!CollectionUtils.isEmpty(AccountConfigDtoList)
					&& !AccountConfigDtoList.get(0).getId()
							.equals(configDto.getId())) {
				j.setSuccess(false);
				j.setMsg("结算配置项:[" + configDto.getAccountPercent()
						+ "]已经存在，请勿重复编辑");
				j.setObj(configDto);
				return j;
			} else {
				// 编辑结算配置项
				accountConfigService.update(configDto);
			}
		}
		j.setSuccess(true);
		j.setMsg("编辑结算配置项成功");
		j.setObj(configDto);
		return j;
	}

	/**
	 * 
	 * delete：删除结算配置项
	 * 
	 * @param id
	 * @return
	 * @exception
	 * @author 刘博
	 * @date 2016年7月20日 上午9:55:02
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		{
			AccountConfigDto AccountConfigDto = accountConfigService
					.findById(id);
			if (null != AccountConfigDto) {
				AccountConfigDto.setStat(DataStatus.DISABLED);
				accountConfigService.delete(AccountConfigDto);
				j.setMsg("删除结算配置项功！");
				j.setSuccess(true);
			} else {
				j.setMsg("删除结算配置项失败,请联系管理员!");
				j.setSuccess(false);
			}
		}
		return j;
	}
}
