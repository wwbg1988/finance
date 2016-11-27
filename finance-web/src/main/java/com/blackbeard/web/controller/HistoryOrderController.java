package com.blackbeard.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.admin.service.system.role.RoleService;
import com.blackbeard.admin.util.Jurisdiction;
import com.blackbeard.admin.util.ObjectExcelView;
import com.blackbeard.common.constant.WebConstants;
import com.blackbeard.common.dto.AccountConfigDto;
import com.blackbeard.common.dto.AccountResultDto;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.OrderChartDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.pojo.web.Role;
import com.blackbeard.common.pojo.web.User;
import com.blackbeard.common.service.IAccountConfigService;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.UserService;

@Controller
@RequestMapping("/historyOrderController")
public class HistoryOrderController extends BaseController {

	String menuUrl = "historyOrderController/findAllHistoryOrder.do"; // 菜单地址(权限用)
	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private IHistoryOrderService historyOrderService;
	@Autowired
	private IAccountConfigService accountConfigService;
	@Resource(name = "roleService")
	private RoleService roleService;
	@Resource(name = "userService")
	private UserService userService;
	@Autowired
	private WebUserController webUserController;

	@RequestMapping("/findAllHistoryOrder")
	public ModelAndView findAllHistoryOrder(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		int count = 0;
		List<PageData> userList = new ArrayList<PageData>();
		HistoryOrderDto historyOrderDto = new HistoryOrderDto();
		// 货币
		String symbol = pd.getString("symbol");
		/*
		 * // 订单号 if (!StringUtils.isEmpty(pd.getString("tick"))) { Long tick =
		 * Long.valueOf(pd.getString("tick")); historyOrderDto.setTick(tick); }
		 */
		// 用户id
		if (!StringUtils.isEmpty(pd.getString("USER_ID"))) {
			Integer USER_ID = Integer.valueOf(pd.getString("USER_ID"));
			historyOrderDto.setUserId(USER_ID);
		}
		// 买卖类型：0买，1卖,2全部;
		if (!StringUtils.isEmpty(pd.getString("cmd"))
				&& !pd.getString("cmd").equals("2")) {
			int cmd = Integer.valueOf(pd.getString("cmd"));
			historyOrderDto.setCmd(cmd);
		}
		historyOrderDto.setSymbol(symbol);
		List<Integer> userIdList = new ArrayList<Integer>();
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		List<Role> roleList = new ArrayList<Role>();
		if (!user.getUSERNAME().equals("admin")) {// 如果登录用户不是超级管理员
			// 根据当前登录用户角色id查找它的子角色id集合
			findChildrenRoleByPid(roleList, user.getROLE_ID());
			for (Role r : roleList) {
				Page page2 = new Page();
				PageData pds = new PageData();
				pds.put("ROLE_ID", r.getROLE_ID());
				page2.setPd(pds);
				userList.addAll(userService.listPdPageUser(page2));
			}
			if (!CollectionUtils.isEmpty(userList)) {
				for (PageData pda : userList) {
					userIdList.add(Integer.valueOf(pda.getString("USER_ID")));
				}
			}
			PageData userPageData = new PageData();
			userPageData.put("NAME", user.getNAME());
			userPageData.put("USER_ID", user.getUSER_ID());
			userList.add(userPageData);
			// userIdList集合加入当前登录用户id
			userIdList.add(Integer.valueOf(user.getUSER_ID()));
			// 查找历史订单总数量(在userIdList中的用户订单数量)
			count = historyOrderService.findList(userIdList, historyOrderDto)
					.size();
		} else {// 如果是超管
			// 开仓起始时间
			String openTimeStart = pd.getString("openTimeStart");
			// 开仓结束时间
			String openTimeEnd = pd.getString("openTimeEnd");
			if (openTimeStart != null && !"".equals(openTimeStart)) {
				openTimeStart = openTimeStart + " 00:00:00";
				long timeStart = DateUtil.stringToTimeStamp(openTimeStart);
				historyOrderDto.setOpenTimeStart(Long.toString(timeStart));
			}
			if (openTimeEnd != null && !"".equals(openTimeEnd)) {
				openTimeEnd = openTimeEnd + " 00:00:00";
				long timeEnd = DateUtil.stringToTimeStamp(openTimeEnd);
				historyOrderDto.setOpenTimeEnd(Long.toString(timeEnd));
			}
			// 是否平仓：1是,0否;
			if (!StringUtils.isEmpty(pd.getString("eveningUp"))) {
				historyOrderDto.setEveningUp(pd.getString("eveningUp"));
			}
			if (!StringUtils.isEmpty(historyOrderDto.getOpenTimeStart())
					&& !StringUtils.isEmpty(historyOrderDto.getOpenTimeEnd())) {
				AccountConfigDto accountConfigDto = new AccountConfigDto();
				// 查询结算结果
				AccountResultDto accountResultDto = historyOrderService
						.findAccountResult(historyOrderDto);
				if (accountResultDto != null) {
					// 查询结算比例
					List<AccountConfigDto> accountList = accountConfigService
							.findListByPage(accountConfigDto, null);
					if (!CollectionUtils.isEmpty(accountList)) {// 如果结算不为空
						float accountPercent = accountList.get(0)
								.getAccountPercent();
						// 佣金：结算百分比*总交易数量
						float accountCommission = accountResultDto
								.getAccountCount() * accountPercent;
						accountResultDto
								.setAccountCommission(accountCommission);
					}
					// 放入结算结果
					mv.addObject("accountResultDto", accountResultDto);
				} else {
					// 放入结算结果
					mv.addObject("accountResultDto", null);
				}
			}
			count = historyOrderService.findCount(historyOrderDto);
			PageData pds = new PageData();
			userList = userService.listAllUser(pds);
		}
		LimitPageDto limitPageDto;
		if (count == 0) {
			limitPageDto = new LimitPageDto();
		} else {
			limitPageDto = pageFunction.getLimitPage(page, count);
		}
		List<HistoryOrderDto> historyOrderDtoList = historyOrderService
				.findListByPage(historyOrderDto, limitPageDto, userIdList);
		// 设置page信息
		pageFunction.setLimitPage(historyOrderDtoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/order/history/order_list");
		pd.put("userName", user.getUSERNAME());

		mv.addObject("pd", pd);
		mv.addObject("historyOrderList", historyOrderDtoList);
		mv.addObject("currencyList", this.findCurrencyList());
		mv.addObject("cmdMap", WebConstants.cmdMap);
		mv.addObject("eveningMap", WebConstants.eveningMap);
		mv.addObject("userList", userList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	public List<Role> getLocalLoginUserChildRoleList() throws Exception {
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		List<Role> roleList = new ArrayList<Role>();
		findChildrenRoleByPid(roleList, user.getRole().getROLE_ID());
		roleList.add(user.getRole());
		return roleList;
	}

	public List<Role> findChildrenRoleByPid(List<Role> roleListResult,
			String pId) throws Exception {
		if (!StringUtils.isEmpty(pId)) {// 如果父id不为空
			PageData children_pd = new PageData();
			children_pd.put("ROLE_ID", pId);
			// 根据父id查询所有所有的子角色
			List<Role> childrenRoleList = roleService
					.listAllRolesByPId(children_pd);
			if (!CollectionUtils.isEmpty(childrenRoleList)) {
				// 将所有的子角色加入进去
				roleListResult.addAll(childrenRoleList);
				for (Role childRole : childrenRoleList) {// 继续循环，只要有子角色id是父id在全部加入集合中
					findChildrenRoleByPid(roleListResult,
							childRole.getROLE_ID());
				}
			}
		}
		// 返回所有的角色对象
		return roleListResult;
	}

	// ===================================================================================================

	/**
	 * 跳转到历史订单图表页面
	 */
	@RequestMapping(value = "/findChartPage")
	public ModelAndView findChartPage() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("web/order/history/chart");
		return mv;
	}

	@RequestMapping("/findChart")
	public ModelAndView findChart(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		int count = 0;
		HistoryOrderDto historyOrderDto = new HistoryOrderDto();
		// 开仓起始时间
		String openTimeStart = pd.getString("openTimeStart");
		// 开仓结束时间
		String openTimeEnd = pd.getString("openTimeEnd");
		if (!StringUtils.isEmpty((openTimeStart))) {
			openTimeStart = openTimeStart + " 00:00:00";
			long timeStart = DateUtil.stringToTimeStamp(openTimeStart);
			historyOrderDto.setOpenTimeStart(Long.toString(timeStart));
		}
		if (!StringUtils.isEmpty((openTimeEnd))) {
			openTimeEnd = openTimeEnd + " 00:00:00";
			long timeEnd = DateUtil.stringToTimeStamp(openTimeEnd);
			historyOrderDto.setOpenTimeEnd(Long.toString(timeEnd));
		}
		String strXml = historyOrderService.findChartByPage(historyOrderDto);

		mv.addObject("pd", pd);
		mv.addObject("strXML", strXml);
		mv.setViewName("web/order/history/chart");
		return mv;
	}

	/*
	 * 导出历史订单信息到EXCEL
	 * 
	 * @return
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel(HistoryOrderDto historyOrderDto)
			throws Exception {
		ModelAndView mv = this.getModelAndView();
		Page page = new Page();
		PageData pd = this.getPageData();
		List<HistoryOrderDto> historyOrderList = new ArrayList<HistoryOrderDto>();
		List<Integer> userIdList = new ArrayList<Integer>();
		List<PageData> userList = new ArrayList<PageData>();
		// 买卖类型：0买，1卖,2全部;
		if (historyOrderDto.getCmd() == 2) {// 2是全部
			historyOrderDto.setCmd(null);
		}
		// 判断当前用户是否是超管
		boolean isAdmin = webUserController.isAdmin();
		if (!isAdmin) {// 不是超级管理员
			historyOrderDto.setOpenTimeStart(null);
			historyOrderDto.setOpenTimeEnd(null);
			List<Role> roleList = new ArrayList<Role>();
			// 查找登录用户的所有子角色
			findChildrenRoleByPid(roleList, webUserController.getLoginUser()
					.getROLE_ID());
			for (Role r : roleList) {
				pd.put("ROLE_ID", r.getROLE_ID());
				page.setPd(pd);
				userList.addAll(userService.listPdPageUser(page));
			}
			if (!CollectionUtils.isEmpty(userList)) {
				for (PageData pda : userList) {
					userIdList.add(Integer.valueOf(pda.getString("USER_ID")));
				}
			}
			userIdList.add(Integer.valueOf(webUserController.getLoginUser()
					.getUSER_ID()));
		} else {// 是超管，如果开仓查询时间不为空，则转化时间戳进行比较
			// 开仓起始时间
			String openTimeStart = historyOrderDto.getOpenTimeStart();
			// 开仓结束时间
			String openTimeEnd = historyOrderDto.getOpenTimeEnd();
			if (openTimeStart != null && !"".equals(openTimeStart)) {
				openTimeStart = openTimeStart + " 00:00:00";
				long timeStart = DateUtil.stringToTimeStamp(openTimeStart);
				historyOrderDto.setOpenTimeStart(Long.toString(timeStart));
			}
			if (openTimeEnd != null && !"".equals(openTimeEnd)) {
				openTimeEnd = openTimeEnd + " 00:00:00";
				long timeEnd = DateUtil.stringToTimeStamp(openTimeEnd);
				historyOrderDto.setOpenTimeEnd(Long.toString(timeEnd));
			}
		}
		// 查找所有符合条件的数据集合
		historyOrderList = historyOrderService.findListByPage(historyOrderDto,
				null, userIdList);
		try {
			if (Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				List<String> titles = new ArrayList<String>();
				titles.add("订单号"); // 1
				titles.add("所属用户"); // 2
				titles.add("货币名称"); // 3
				titles.add("开仓价格"); // 4
				titles.add("开仓时间"); // 5
				titles.add("结束价格"); // 6
				titles.add("结束时间"); // 7
				titles.add("类型"); // 8
				titles.add("数量"); // 9
				titles.add("盈亏"); // 10
				titles.add("备注"); // 11

				dataMap.put("titles", titles);
				// 遍历全局变量历史订单集合 historyOrderList
				List<PageData> varList = new ArrayList<PageData>();
				for (int i = 0; i < historyOrderList.size(); i++) {
					PageData vpd = new PageData();
					vpd.put("var1", historyOrderList.get(i).getTick()); // 1
					vpd.put("var2", historyOrderList.get(i).getUserName()); // 2
					vpd.put("var3", historyOrderList.get(i).getSymbol()); // 3
					vpd.put("var4", historyOrderList.get(i).getOpenPrice()); // 4
					vpd.put("var5", historyOrderList.get(i)
							.getOpenTimeFormatString()); // 5
					vpd.put("var6", historyOrderList.get(i).getClosePrice()); // 6
					vpd.put("var7", historyOrderList.get(i)
							.getCloseTimeFormatString()); // 7
					if (historyOrderList.get(i).getCmd() == 0) {
						vpd.put("var8", "买入");// 8
					} else {
						vpd.put("var8", "卖出");// 8
					}
					vpd.put("var9", historyOrderList.get(i).getTotal());// 9
					vpd.put("var10", historyOrderList.get(i).getProfit()); // 10
					vpd.put("var11", historyOrderList.get(i).getComment()); // 10
					varList.add(vpd);
				}
				dataMap.put("varList", varList);
				ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
				mv = new ModelAndView(erv, dataMap);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
}
