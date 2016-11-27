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

import com.blackbeard.admin.util.Jurisdiction;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.Json;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.service.ICurrencyConfigService;
import com.ssic.util.constants.DataStatus;

/**
 * 货币配置项控制器
 * 
 * @author 刘博
 *
 */
@Controller
@RequestMapping("/currencyConfigController")
public class CurrencyConfigController extends BaseController {
	@Autowired
	private ICurrencyConfigService currencyConfigService;
	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private WebUserController webUserController;

	@RequestMapping("/findCurrencyConfigList")
	public ModelAndView findAllUserInfo(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		int count = currencyConfigService.findCount();
		CurrencyConfigDto currencyConfigDto = new CurrencyConfigDto();
		// 获取分页的开始结束
		LimitPageDto limitPageDto = pageFunction.getLimitPage(page, count);
		List<CurrencyConfigDto> currencyConfigDtoList;
		// 判断当前登录用户是否是超级管理员
		boolean isAdminUser = webUserController.isAdmin();
		if (!isAdminUser) {// 不是超级管理员,什么都看不到
			currencyConfigDtoList = new ArrayList<CurrencyConfigDto>();
		} else {
			currencyConfigDtoList = currencyConfigService.findListByPage(
					currencyConfigDto, limitPageDto);
		}
		// 设置page信息
		pageFunction.setLimitPage(currencyConfigDtoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/currencyConfig/currencyConfig_list");
		mv.addObject("currencyConfigList", currencyConfigDtoList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 跳转到新增货币利率配置项页面
	 */
	@RequestMapping(value = "/addPage")
	public ModelAndView addPage(CurrencyConfigDto currencyConfigDto)
			throws Exception {
		ModelAndView mv = this.getModelAndView();
		currencyConfigDto.setId(UUID.randomUUID().toString());
		mv.setViewName("web/currencyConfig/currencyConfig_add");
		mv.addObject("currencyConfigDto", currencyConfigDto);
		return mv;
	}

	/**
	 * 新增货币利率配置项
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Json add(CurrencyConfigDto currencyConfigDto) throws Exception {
		Json j = new Json();
		List<CurrencyConfigDto> CurrencyConfigDtoList = currencyConfigService
				.findListByPage(currencyConfigDto, null);
		if (!CollectionUtils.isEmpty(CurrencyConfigDtoList)) {
			j.setSuccess(false);
			j.setMsg("该货币已经存在!");
			j.setObj(currencyConfigDto);
			return j;
		}
		currencyConfigService.insert(currencyConfigDto);

		j.setSuccess(true);
		j.setMsg("新增货币利率配置项成功!");
		j.setObj(currencyConfigDto);
		return j;
	}

	/**
	 * 启用禁用货币
	 */
	@RequestMapping(value = "/editConfigEnable")
	public ModelAndView editConfigEnable() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String id = pd.getString("id");
			CurrencyConfigDto currencyConfigDto = currencyConfigService
					.findById(id);
			if (currencyConfigDto.getIsEnable() == 1) {
				currencyConfigDto.setIsEnable(DataStatus.DISABLED);
			} else {
				currencyConfigDto.setIsEnable(DataStatus.ENABLED);
			}
			currencyConfigService.update(currencyConfigDto);
			mv.setViewName("save_result");
			mv.addObject("msg", "success");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 跳转到编辑货币利率配置项页面
	 */
	@RequestMapping(value = "/editPage")
	public ModelAndView editPage(CurrencyConfigDto configDto) throws Exception {
		ModelAndView mv = this.getModelAndView();
		CurrencyConfigDto currencyConfigDto = currencyConfigService
				.findById(configDto.getId());
		mv.addObject("currencyConfigDto", currencyConfigDto);
		mv.setViewName("web/currencyConfig/currencyConfig_edit");
		return mv;
	}

	/**
	 * 编辑保存货币利率配置项
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(CurrencyConfigDto configDto) {
		Json j = new Json();
		if (!StringUtils.isEmpty(configDto.getCurrency())) {
			List<CurrencyConfigDto> currencyConfigDtoList = currencyConfigService
					.findListByPage(configDto, null);
			if (!CollectionUtils.isEmpty(currencyConfigDtoList)
					&& !currencyConfigDtoList.get(0).getId()
							.equals(configDto.getId())) {
				j.setSuccess(false);
				j.setMsg("货币利率配置项:[" + configDto.getCurrency() + "]已经存在，请勿重复编辑");
				j.setObj(configDto);
				return j;
			} else {
				// 编辑货币利率配置项
				configDto.setStat(DataStatus.ENABLED);
				currencyConfigService.update(configDto);
			}
		}
		j.setSuccess(true);
		j.setMsg("编辑货币利率配置项成功");
		j.setObj(configDto);
		return j;
	}

	/**
	 * 
	 * delete：删除货币利率配置项
	 * 
	 * @param id
	 * @return
	 * @exception
	 * @author 刘博
	 * @date 2016年8月02日 上午9:55:02
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		{
			CurrencyConfigDto CurrencyConfigDto = currencyConfigService
					.findById(id);
			if (null != CurrencyConfigDto) {
				currencyConfigService.delete(CurrencyConfigDto);
				j.setMsg("删除货币利率配置项功！");
				j.setSuccess(true);
			} else {
				j.setMsg("删除货币利率配置项失败,请联系管理员!");
				j.setSuccess(false);
			}
		}
		return j;
	}
}
