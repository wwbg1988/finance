package com.blackbeard.web.controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.admin.service.system.role.RoleService;
import com.blackbeard.admin.util.AppUtil;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.ImageInfoDto;
import com.blackbeard.common.dto.Json;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Mt4ConfigDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.PageFunction;
import com.blackbeard.common.dto.Tools;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.dto.UserRechangeFailureRecordDto;
import com.blackbeard.common.pojo.web.Role;
import com.blackbeard.common.pojo.web.User;
import com.blackbeard.common.pojo.web.UserInfo;
import com.blackbeard.common.service.ICreateImageService;
import com.blackbeard.common.service.IMt4ConfigService;
import com.blackbeard.common.service.IUserInfoService;
import com.blackbeard.common.service.IUserLoginStateService;
import com.blackbeard.common.service.IUserRechangeFailureRecordService;
import com.blackbeard.common.service.UserService;
import com.blackbeard.common.util.MD5Util;
import com.blackbeard.web.socket.RegisterMT4UserClient;
import com.blackbeard.web.util.WebUtils;
import com.blackbeard.web.vo.RechargeMoneyDto;
import com.ssic.util.DateUtils;
import com.ssic.util.constants.DataStatus;

@Controller
@RequestMapping("/web/user")
public class WebUserController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(WebUserController.class);
	String menuUrl = "user/listUsers.do"; // 菜单地址(权限用)
	@Autowired
	private ICreateImageService createImageService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private PageFunction pageFunction;
	@Autowired
	private IMt4ConfigService mt4ConfigService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "roleService")
	private RoleService roleService;
	@Autowired
	private IUserLoginStateService userLoginStateService;
	@Autowired
	private IUserRechangeFailureRecordService rechangeFailureRecordService;

	/**
	 * 跳转到注册页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toRegister.do")
	public ModelAndView toRegister(String ROLE_ID) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("ROLE_ID", ROLE_ID);
		pd.put("SYSNAME", Tools.readTxtFile(Const.SYSNAME)); // 读取系统名称
		mv.setViewName("web/user_register");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 跳转到充值金额页面
	 * 
	 * @param isFailure
	 *            是否是失败充值类型 :1 :是
	 * @param id
	 *            删除失败记录的主键id
	 * @param rechargeMoney
	 *            充值金额
	 * @return
	 */
	@RequestMapping(value = "/goRechargeMoney.do")
	public ModelAndView goRechargeMoney(String USER_ID, String isFailure,
			String id, String money) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USER_ID", USER_ID);
		if (!StringUtils.isEmpty(isFailure) && isFailure.equals("1")) {
			pd.put("isFailure", 1);
		} else {
			pd.put("isFailure", 0);
		}
		pd.put("id", id);
		pd.put("money", money);
		mv.setViewName("web/user_rechargeMoney");
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 充值金额
	 * 
	 * @param userId
	 *            用户id
	 * @param money
	 *            充值金额
	 * 
	 * @return
	 */
	@RequestMapping("/rechargeMoney")
	public ModelAndView rechargeMoney() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		// 用户id
		String userId = pd.getString("USER_ID");
		// 是否是充值失败类型：1是,0否;
		String isFailure = pd.getString("isFailure");
		// 删除失败记录表的主键id
		String id = pd.getString("id");
		pd.put("USER_ID", userId);
		PageData pds = userService.findByUiId(pd);
		String userState = pds.getString("user_state");
		// 充值之前余额
		long blance;
		if (!pds.containsKey("blance")) {
			blance = 0;
		} else {
			blance = Long.parseLong(pds.getString("blance"));
		}
		if (userState.equals(DataStatus.DISABLED)) {
			mv.addObject("msg", "用户审核未通过,无法充值");
			mv.setViewName("save_result");
			return mv;
		}

		int mt4Id = Integer.valueOf(pds.getString("mt4_id"));
		if (StringUtils.isEmpty(pds.getString("mt4_id"))) {
			mv.addObject("msg", "用户mt4Id为空,无法充值");
			mv.setViewName("save_result");
			return mv;
		}
		// 时间
		String comment = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		// 充值金额
		Double money = Double.parseDouble(pd.getString("money"));
		// 调用mt4接口注册MT4账号
		List<Mt4ConfigDto> listConfig = mt4ConfigService.findListByPage(
				new Mt4ConfigDto(), null);
		if (CollectionUtils.isEmpty(listConfig)) {
			mv.addObject("msg", "mt4服务器信息不存在,无法充值");
			return mv;
		}
		// 充值金额dto
		RechargeMoneyDto rechargeMoneyDto = new RechargeMoneyDto();
		rechargeMoneyDto.setMoney(money);
		rechargeMoneyDto.setComment(comment);
		rechargeMoneyDto.setId(mt4Id);
		// 将用户信息以及mt4配置项信息组合成充值接口的字符串；
		String rechargeMoneyParam = WebUtils.loadRechargeMoneyParam(
				rechargeMoneyDto, listConfig.get(0));
		// 调用充值金额sokcet接口
		String returnMsg = RegisterMT4UserClient
				.rechargeMoneyForUser(rechargeMoneyParam);
		System.out.println("注册用户返回returnMsg：--------" + returnMsg);
		if (!returnMsg.equals("0")) {// 充值接口返回消息失败
			if (isFailure.equals("0")) {// 如果本次充值接口返回失败,且不是充值失败类型,则插入到失败记录表中
				UserRechangeFailureRecordDto dto = new UserRechangeFailureRecordDto();
				dto.setId(UUID.randomUUID().toString());
				dto.setUserId(Integer.valueOf(userId));
				dto.setMt4Id(String.valueOf(mt4Id));
				dto.setRechargeMoney(money.longValue());
				rechangeFailureRecordService.insert(dto);
			}
			mv.addObject("msg", "MT4充值接口调用异常,无法充值");
			mv.setViewName("save_result");
			return mv;
		}
		if (isFailure.equals("1")) {// 如果本次充值成功,且类型是充值失败,则删除充值失败记录表的本条记录;
			UserRechangeFailureRecordDto failureDto = rechangeFailureRecordService
					.findById(id);
			failureDto.setStat(DataStatus.DISABLED);
			rechangeFailureRecordService.delete(failureDto);
		}
		// 更新用户余额:充值之前余额+本次充值金额*100
		blance += money.longValue() * 100;
		PageData newPageData = new PageData();
		newPageData.put("blance", blance);
		newPageData.put("USER_ID", userId);
		newPageData.put("lastUpdateTime", new Date());
		// 更新sys_user表的余额
		userService.updateUserBlance(newPageData);
		// 更新用户状态表的余额
		updateUserLoginState(userId, money.longValue()*100);
		mv.addObject("msg", "rechangeSuccess");
		mv.setViewName("save_result");
		return mv;
	}

	// 更新该用户在登录状态表的余额
	public void updateUserLoginState(String userId, long rechargeMoney) {
		UserLoginStateDto userLoginStateDto = new UserLoginStateDto();
		userLoginStateDto.setUserId(Integer.valueOf(userId));
		List<UserLoginStateDto> userLoginStateDtoList = userLoginStateService
				.findBy(userLoginStateDto);
		if (!CollectionUtils.isEmpty(userLoginStateDtoList)) {// 如果该用户在线,则更新该用户在登录状态表的余额
			UserLoginStateDto loginstateDto = userLoginStateDtoList.get(0);
			long nowBlance = loginstateDto.getBlance() + rechargeMoney;
			loginstateDto.setBlance(nowBlance);
			userLoginStateService.updateBlance(loginstateDto);
		}
	}

	/**
	 * 用户登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login.do")
	public ModelAndView login(UserInfoDto userInfoDto, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mv = new ModelAndView();
		// 如果存在这个用户 则直接返回;
		String md5Pwd = MD5Util.md5(userInfoDto.getUserPassword());
		UserInfo userInfo = userInfoService.findByUserNameAndPwd(
				userInfoDto.getUserName(), md5Pwd);
		if (userInfo == null) {
			mv.addObject("msg", "false");
			mv.setViewName("save_result");
			return mv;
		}
		session.setAttribute("userInfo", userInfo);
		// request.getSession(true).setAttribute("userInfo", userInfo);
		mv.addObject("userName", userInfo.getUserName());
		mv.setViewName("web/index");
		return mv;
	}

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {
		Json j = new Json();
		if (session != null) {
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}

	/**
	 * 添加新用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	public ModelAndView add(MultipartFile imgUrl, User user,
			ImageInfoDto image, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("EMAIL", user.getEMAIL());
		pd.put("USERNAME", user.getUSERNAME());
		pd.put("NAME", user.getNAME());// 昵称
		pd.put("PHONE", user.getPHONE());
		int count = userService.selectMaxUserId();
		pd.put("USER_ID", count + 1); // ID
		pd.put("RIGHTS", ""); // 权限
		pd.put("LAST_LOGIN", ""); // 最后登录时间
		pd.put("IP", InetAddress.getLocalHost().getHostAddress());// IP
		pd.put("STATUS", "0"); // 状态
		pd.put("SKIN", "default"); // 默认皮肤
		pd.put("certificateNo", user.getCertificateNo());
		pd.put("createTime", new Date());
		pd.put("lastUpdateTime", new Date());
		pd.put("ROLE_ID", user.getROLE_ID());
		// user_group存入user_name
		pd.put("mt4_user_group", user.getUSERNAME());
		String pwd = user.getPASSWORD();
		pd.put("PASSWORD", MD5Util.base64Encode(pwd));
		if (null == userService.findByUId(pd)) {
			// 注册状态:0
			pd.put("userState", DataStatus.DISABLED);
			Map<String, Object> map = createImageService.createImage(image,
					imgUrl, request, response);
			String imageurl = (String) map.get("image_url");
			pd.put("certificateUrl", imageurl);
			// 保存用户
			userService.saveU(pd);

			mv.addObject("msg", "success");
		} else {
			mv.addObject("msg", "failed");
		}

		mv.setViewName("redirect:/index.jsp");
		return mv;
	}

	@RequestMapping("/findAllUserInfo")
	public ModelAndView findAllUserInfo(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		page.setPd(pd);
		String userName = pd.getString("userName");
		String certificateNo = pd.getString("certificateNo");
		String userTelphone = pd.getString("userTelphone");
		int count = userInfoService.findCount();
		UserInfoDto userInfoDto = new UserInfoDto();
		userInfoDto.setUserName(userName);
		userInfoDto.setCertificateNo(certificateNo);
		userInfoDto.setUserTelphone(userTelphone);
		// 获取分页的开始结束
		LimitPageDto limitPageDto = pageFunction.getLimitPage(page, count);
		List<UserInfoDto> userInfoList = userInfoService.findListByPage(
				userInfoDto, limitPageDto);
		// 设置page信息
		pageFunction.setLimitPage(userInfoList.size(), count,
				limitPageDto.getTotalPage(), page);
		// 组装mv对象并返回到前台页面
		mv.setViewName("web/user_list");
		mv.addObject("userInfoList", userInfoList);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	@RequestMapping("/audit")
	@ResponseBody
	public Json audit(int id) throws Exception {
		Json json = new Json();
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USER_ID", id);
		PageData pds = userService.findByUiId(pd);
		String userState = pds.getString("user_state");
		if (pds == null) {
			json.setMsg("审核失败,该用户不存在");
			json.setSuccess(false);
			return json;
		}
		if (!userState.equals("0")) {
			json.setMsg("审核失败,该用户已经注册");
			json.setSuccess(false);
			return json;
		}

		// 调用mt4接口注册MT4账号
		List<Mt4ConfigDto> listConfig = mt4ConfigService.findListByPage(
				new Mt4ConfigDto(), null);
		if (CollectionUtils.isEmpty(listConfig)) {
			json.setMsg("mt4服务器信息不存在,无法通过审核");
			json.setSuccess(false);
			return json;
		}
		String userParam = WebUtils.loadMt4ConigParam(pds, listConfig.get(0));

		// 调用注册sokcet接口
		String returnMsg = RegisterMT4UserClient.registerMt4User(userParam);
		System.out.println("注册用户返回returnMsg：--------" + returnMsg);
		if (returnMsg.equals("-1") || StringUtils.isEmpty(returnMsg)) {
			json.setMsg("MT4接口调用异常,无法通过审核");
			json.setSuccess(false);
			return json;
		}
		pds.put("mt4Id", returnMsg);
		pds.put("userState", DataStatus.ENABLED);
		pds.put("lastUpdateTime", new Date());
		userService.updateMt4IdByUId(pds);
		json.setMsg("审核成功");
		json.setSuccess(true);
		return json;
	}

	/**
	 * 判断用户是否存在
	 */
	@RequestMapping(value = "/hasUser")
	@ResponseBody
	public Object hasUser(@RequestParam("userName") String userName) {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();

			pd.put("USERNAME", userName);
			PageData pageData = userService.findByUserName(pd);
			if (pageData != null) {
				String username = pageData.getString("USERNAME");
				boolean existUserName = userName.equalsIgnoreCase(username);
				if (existUserName) {
					errInfo = "error";
				}

			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	public List<Role> findChildrenRoleListByLoginUser() {
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		List<Role> roleList = new ArrayList<Role>();
		if (!user.getUSERNAME().equals("admin")) {// 如果登录用户不是超级管理员
			try {
				findChildrenRoleByPid(roleList, user.getROLE_ID());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return roleList;
	}

	public List<PageData> findUserListByLoginUser(List<Role> roleList)
			throws Exception {
		List<Integer> userIdList = new ArrayList<Integer>();
		List<PageData> userList = new ArrayList<PageData>();
		for (Role r : roleList) {
			Page page2 = new Page();
			PageData pds = new PageData();
			pds.put("ROLE_ID", r.getROLE_ID());
			page2.setPd(pds);
			userList.addAll(userService.listPdPageUser(page2));
		}
		return userList;
	}

	public List<Integer> findUserIdListByLoginUser(List<Role> roleList)
			throws Exception {
		List<Integer> userIdList = new ArrayList<Integer>();
		List<PageData> userList = new ArrayList<PageData>();
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
		// 获取当前登录用户
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		// userIdList加入当前登录用户id
		userIdList.add(Integer.valueOf(user.getUSER_ID()));
		return userIdList;
	}

	public boolean isAdmin() {
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		if (user.getUSERNAME().equals("admin")) {
			return true;
		}
		return false;
	}

	public User getLoginUser() {
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		return user;
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

	public static void main(String[] args) {
		long times = 1466148600203L;
		Date date = new Date(times);
		System.out.println(date);
	}
}
