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

import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.Json;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Mt4ConfigDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.service.IMt4ConfigService;
import com.ssic.util.constants.DataStatus;

@Controller
@RequestMapping("/mt4Config")
public class Mt4ConfigController extends BaseController {

	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private IMt4ConfigService mt4ConfigService;
	@Autowired
	private WebUserController webUserController;

	@RequestMapping("/findAllMt4Config")
	public ModelAndView findAllUserInfo(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		String serverIp = pd.getString("serverIp");

		int count = mt4ConfigService.findCount();
		Mt4ConfigDto mt4ConfigDto = new Mt4ConfigDto();
		mt4ConfigDto.setServerIp(serverIp);
		if (!StringUtils.isEmpty(pd.getString("serverPort"))) {
			int serverPort = Integer.valueOf(pd.getString("serverPort"));
			mt4ConfigDto.setServerPort(serverPort);
		}

		// 获取分页的开始结束
		LimitPageDto limitPageDto = pageFunction.getLimitPage(page, count);
		List<Mt4ConfigDto> mt4ConfigDtoList;
		// 判断当前登录用户是否是超级管理员
		boolean isAdminUser = webUserController.isAdmin();
		if (!isAdminUser) {// 不是超级管理员,什么都看不到
			mt4ConfigDtoList = new ArrayList<Mt4ConfigDto>();
		} else {
			mt4ConfigDtoList = mt4ConfigService.findListByPage(mt4ConfigDto,
					limitPageDto);
		}

		// 设置page信息
		pageFunction.setLimitPage(mt4ConfigDtoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/mt4Config/mt4Config_list");
		mv.addObject("mt4ConfigList", mt4ConfigDtoList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 跳转到新增MT4配置项页面
	 */
	@RequestMapping(value = "/addPage")
	public ModelAndView addPage(Mt4ConfigDto mt4ConfigDto) throws Exception {
		ModelAndView mv = this.getModelAndView();
		mt4ConfigDto.setId(UUID.randomUUID().toString());
		mv.setViewName("web/mt4Config/mt4Config_add");
		mv.addObject("mt4ConfigDto", mt4ConfigDto);
		return mv;
	}

	/**
	 * 新增MT4Config
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Json add(Mt4ConfigDto mt4ConfigDto) throws Exception {
		Json j = new Json();
		Mt4ConfigDto mt4Config_Dto = new Mt4ConfigDto();
		List<Mt4ConfigDto> mt4ConfigDtoList = mt4ConfigService.findListByPage(
				mt4Config_Dto, null);
		if (!CollectionUtils.isEmpty(mt4ConfigDtoList)) {
			j.setSuccess(false);
			j.setMsg("MT4服务器配置项已经存在，请勿重复添加");
			j.setObj(mt4ConfigDto);
			return j;
		} else {
			mt4ConfigService.insert(mt4ConfigDto);
		}

		j.setSuccess(true);
		j.setMsg("新增MT4配置项成功!");
		j.setObj(mt4ConfigDto);
		return j;
	}

	/**
	 * 跳转到编辑mt4配置项页面
	 */
	@RequestMapping(value = "/editPage")
	public ModelAndView editPage(Mt4ConfigDto configDto) throws Exception {
		ModelAndView mv = this.getModelAndView();
		Mt4ConfigDto mt4ConfigDto = mt4ConfigService
				.findById(configDto.getId());
		mv.addObject("mt4ConfigDto", mt4ConfigDto);
		mv.setViewName("web/mt4Config/mt4Config_edit");
		return mv;
	}

	/**
	 * 编辑保存MT4配置项
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Mt4ConfigDto configDto) {
		Json j = new Json();
		if (!StringUtils.isEmpty(configDto.getServerIp())
				&& !StringUtils.isEmpty(configDto.getServerPort())) {
			List<Mt4ConfigDto> mt4ConfigDtoList = mt4ConfigService
					.findListByPage(configDto, null);
			if (!CollectionUtils.isEmpty(mt4ConfigDtoList)
					&& !mt4ConfigDtoList.get(0).getId()
							.equals(configDto.getId())) {
				j.setSuccess(false);
				j.setMsg("MT4服务器:[" + configDto.getServerIp() + ":"
						+ configDto.getServerPort() + "]已经存在，请勿重复编辑");
				j.setObj(configDto);
				return j;
			} else {
				// 编辑MT4配置项
				mt4ConfigService.update(configDto);
			}
		}
		j.setSuccess(true);
		j.setMsg("编辑MT4配置项成功");
		j.setObj(configDto);
		return j;
	}

	/**
	 * 
	 * delete：删除颜色
	 * 
	 * @param id
	 * @return
	 * @exception
	 * @author 刘博
	 * @date 2015年10月22日 上午9:55:02
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id) {
		Json j = new Json();
		{
			Mt4ConfigDto mt4ConfigDto = mt4ConfigService.findById(id);
			if (null != mt4ConfigDto) {
				mt4ConfigDto.setStat(DataStatus.DISABLED);
				mt4ConfigService.delete(mt4ConfigDto);
				j.setMsg("删除MT4配置项成功！");
				j.setSuccess(true);
			} else {
				j.setMsg("删除MT4配置项,请联系管理员!");
				j.setSuccess(false);
			}
		}
		return j;
	}
}
