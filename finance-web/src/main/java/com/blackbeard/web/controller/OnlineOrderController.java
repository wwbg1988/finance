package com.blackbeard.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.admin.service.system.role.RoleService;
import com.blackbeard.common.constant.WebConstants;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.pojo.web.Role;
import com.blackbeard.common.pojo.web.User;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.service.UserService;

@Controller
@RequestMapping("/onlineOrder")
public class OnlineOrderController extends BaseController {

	@Autowired
	private WebUserController webUserController;
	@Resource(name = "roleService")
	private RoleService roleService;
	@Resource(name = "userService")
	private UserService userService;
	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private INextOrderService nextOrderService;

	@RequestMapping("/findAllOrder")
	public ModelAndView findAllOrder(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		int count = 0;
		List<PageData> userList = new ArrayList<PageData>();
		NextOrderDto nextOrderDto = new NextOrderDto();
		// 货币
		String currencyType = pd.getString("currencyType");
		// 订单号
		if (!StringUtils.isEmpty(pd.getString("tick"))) {
			Integer tick = Integer.valueOf(pd.getString("tick"));
			nextOrderDto.setTick(tick);
		}
		// 用户id
		if (!StringUtils.isEmpty(pd.getString("USER_ID"))) {
			Integer USER_ID = Integer.valueOf(pd.getString("USER_ID"));
			nextOrderDto.setUserId(USER_ID);
		}
		// 买卖类型：0买，1卖,2全部
		if (!StringUtils.isEmpty(pd.getString("direction"))
				&& !pd.getString("direction").equals("2")) {
			int direction = Integer.valueOf(pd.getString("direction"));
			nextOrderDto.setDirection(direction);
		}
		nextOrderDto.setCurrencyType(currencyType);
		List<Integer> userIdList = new ArrayList<Integer>();
		List<Role> roleList = new ArrayList<Role>();
		if (!webUserController.isAdmin()) {// 如果登录用户不是超级管理员
			// 查找当前登录用户角色以及子角色的集合
			roleList = webUserController.findChildrenRoleListByLoginUser();
			// 获取子角色集合的所有用户id
			userIdList = webUserController.findUserIdListByLoginUser(roleList);
			count = nextOrderService.findList(userIdList, nextOrderDto).size();
			// 获取子角色集合的所有用户
			userList = webUserController.findUserListByLoginUser(roleList);
			// 获取当前登录用户
			User user = webUserController.getLoginUser();
			PageData userPageData = new PageData();
			userPageData.put("NAME", user.getNAME());
			userPageData.put("USER_ID", user.getUSER_ID());
			userList.add(userPageData);
		} else {// 如果是超管
			count = nextOrderService.findCount(nextOrderDto);
			PageData pds = new PageData();
			userList = userService.listAllUser(pds);
		}
		LimitPageDto limitPageDto;
		if (count == 0) {
			limitPageDto = new LimitPageDto();
		} else {
			limitPageDto = pageFunction.getLimitPage(page, count);
		}
		List<NextOrderDto> onlineOrderDtoList = nextOrderService
				.findListByPage(nextOrderDto, limitPageDto, userIdList);
		// 设置page信息
		pageFunction.setLimitPage(onlineOrderDtoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/order/online/order_list");
		mv.addObject("pd", pd);
		mv.addObject("cmdMap", WebConstants.cmdMap);
		mv.addObject("currencyList", this.findCurrencyList());
		mv.addObject("onlineOrderDtoList", onlineOrderDtoList);
		mv.addObject("userList", userList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}
}
