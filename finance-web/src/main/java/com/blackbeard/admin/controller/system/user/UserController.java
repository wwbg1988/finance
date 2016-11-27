package com.blackbeard.admin.controller.system.user;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.admin.service.system.menu.MenuService;
import com.blackbeard.admin.service.system.role.RoleService;
import com.blackbeard.admin.util.AppUtil;
import com.blackbeard.admin.util.FileDownload;
import com.blackbeard.admin.util.FileUpload;
import com.blackbeard.admin.util.GetPinyin;
import com.blackbeard.admin.util.Jurisdiction;
import com.blackbeard.admin.util.ObjectExcelRead;
import com.blackbeard.admin.util.ObjectExcelView;
import com.blackbeard.admin.util.PathUtil;
import com.blackbeard.common.constant.WebConstants;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.Tools;
import com.blackbeard.common.pojo.web.Role;
import com.blackbeard.common.pojo.web.User;
import com.blackbeard.common.service.UserService;
import com.blackbeard.common.util.MD5Util;
import com.blackbeard.web.controller.BaseController;
import com.ssic.util.constants.DataStatus;

/**
 * 类名称：UserController 创建人：FH 创建时间：2014年6月28日
 * 
 * @version
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {

	String menuUrl = "user/listUsers.do"; // 菜单地址(权限用)
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "roleService")
	private RoleService roleService;
	@Resource(name = "menuService")
	private MenuService menuService;

	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/saveU")
	public ModelAndView saveU(PrintWriter out) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		pd.put("USER_ID", this.get32UUID()); // ID
		pd.put("RIGHTS", ""); // 权限
		pd.put("LAST_LOGIN", ""); // 最后登录时间
		pd.put("IP", ""); // IP
		pd.put("STATUS", "0"); // 状态
		pd.put("SKIN", "default"); // 默认皮肤

		pd.put("PASSWORD",
				new SimpleHash("SHA-1", pd.getString("USERNAME"), pd
						.getString("PASSWORD")).toString());

		if (null == userService.findByUId(pd)) {
			if (Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
				userService.saveU(pd);
			} // 判断新增权限
			mv.addObject("msg", "success");
		} else {
			mv.addObject("msg", "failed");
		}
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 判断用户名是否存在
	 */
	@RequestMapping(value = "/hasU")
	@ResponseBody
	public Object hasU() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (userService.findByUId(pd) != null) {
				errInfo = "error";
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 判断邮箱是否存在
	 */
	@RequestMapping(value = "/hasE")
	@ResponseBody
	public Object hasE() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();

			if (userService.findByUE(pd) != null) {
				errInfo = "error";
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 判断编码是否存在
	 */
	@RequestMapping(value = "/hasN")
	@ResponseBody
	public Object hasN() {
		Map<String, String> map = new HashMap<String, String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (userService.findByUN(pd) != null) {
				errInfo = "error";
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo); // 返回结果
		return AppUtil.returnObject(new PageData(), map);
	}

	/**
	 * 修改用户
	 */
	@RequestMapping(value = "/editU")
	public ModelAndView editU() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		if (pd.getString("PASSWORD") != null
				&& !"".equals(pd.getString("PASSWORD"))) {
			pd.put("PASSWORD", MD5Util.base64Encode(pd.getString("PASSWORD")));
		}
		pd.put("lastUpdateTime", new Date());
		if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
			userService.editU(pd);
		}
		mv.addObject("msg", "success");
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 去修改用户页面
	 */
	@RequestMapping(value = "/goEditU")
	public ModelAndView goEditU() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();

		// 顶部修改个人资料
		String fx = pd.getString("fx");

		// System.out.println(fx);

		if ("head".equals(fx)) {
			mv.addObject("fx", "head");
		} else {
			mv.addObject("fx", "user");
		}

		pd = userService.findByUiId(pd); // 根据ID读取
		// 获取当前登录用户所属角色以及子角色集合
		List<Role> roleList = getRoleListForUser(pd);
		mv.setViewName("system/user/user_edit");
		mv.addObject("msg", "editU");
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);

		return mv;
	}

	public List<Role> getRoleListForUser(PageData pd) throws Exception {
		List<Role> roleList_result = new ArrayList<Role>();
		List<Role> roleList = new ArrayList<Role>();
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		if (user != null) {
			if (user.getUSERNAME().equals("admin")) {// 如果是系统管理员(最大权限).可以查看所有角色
				roleList = roleService.listAllERRoles(); // 列出所有二级角色
			} else {// 如果不是系统管理员，则根据登录用户的角色id查询当前角色以及他的下级分支角色
				String role_Id = user.getROLE_ID();
				// 查询所有的子角色，最后全部放入roleList_result集合中
				findChildrenRoleByPid(roleList_result, role_Id);
				pd.put("ROLE_ID", role_Id);
				PageData pdData = roleService.findObjectById(pd);
				Role r = new Role();
				r.setADD_QX(pdData.getString("ADD_QX"));
				r.setCHA_QX(pdData.getString("CHA_QX"));
				r.setDEL_QX(pdData.getString("DEL_QX"));
				r.setPARENT_ID(pdData.getString("PARENT_ID"));
				r.setPARENT_NAME(pdData.getString("PARENT_NAME"));
				r.setQX_ID(pdData.getString("QX_ID"));
				r.setRIGHTS(pdData.getString("RIGHTS"));
				r.setROLE_ID(pdData.getString("ROLE_ID"));
				r.setROLE_NAME(pdData.getString("ROLE_NAME"));
				roleList.add(r);
				roleList.addAll(roleList_result);
			}
		}
		return roleList;
	}

	/**
	 * 去新增用户页面
	 */
	@RequestMapping(value = "/goAddU")
	public ModelAndView goAddU() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<Role> roleList;

		roleList = roleService.listAllERRoles(); // 列出所有二级角色

		mv.setViewName("system/user/user_edit");
		mv.addObject("msg", "saveU");
		mv.addObject("pd", pd);
		mv.addObject("roleList", roleList);

		return mv;
	}

	/**
	 * 显示用户列表(用户组)
	 */
	@RequestMapping(value = "/listUsers")
	public ModelAndView listUsers(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<PageData> userList = new ArrayList<PageData>();
		List<Role> roleList = new ArrayList<Role>();
		PageData pd = new PageData();
		pd = this.getPageData();

		String USERNAME = pd.getString("USERNAME");
		if (null != USERNAME && !"".equals(USERNAME)) {
			USERNAME = USERNAME.trim();
			pd.put("USERNAME", USERNAME);
		}

		// 用户状态：0 待审核,1 注册完成,2 在线,3 离线,4:全部
		String state = pd.getString("user_state");
		if (!state.equals("4")) {
			state = state.trim();
			pd.put("user_state", state);
		}

		String lastLoginStart = pd.getString("lastLoginStart");
		String lastLoginEnd = pd.getString("lastLoginEnd");

		if (lastLoginStart != null && !"".equals(lastLoginStart)) {
			lastLoginStart = lastLoginStart + " 00:00:00";
			pd.put("lastLoginStart", lastLoginStart);
		}
		if (lastLoginEnd != null && !"".equals(lastLoginEnd)) {
			lastLoginEnd = lastLoginEnd + " 00:00:00";
			pd.put("lastLoginEnd", lastLoginEnd);
		}
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();

		User user = (User) session.getAttribute(Const.SESSION_USER);
		if (user != null) {

			User userr = (User) session.getAttribute(Const.SESSION_USERROL);
			user.setRole(userr.getRole());
			if (null != userr && !user.getUSERNAME().equals("admin")) {// 不是系统管理员
				pd.put("ROLE_ID", userr.getRole().getROLE_ID());
				roleList = findChildrenRoleByPid(roleList, userr.getRole()
						.getROLE_ID());
				if (CollectionUtils.isEmpty(roleList)) {// 最底层的角色,只能查看当前用户
					page.setPd(pd);
					userList = userService.listPdPageUser(page); // 列出用户列表
					List<PageData> userList2 = new ArrayList<PageData>();
					for (PageData pdata : userList) {
						String userName = pdata.getString("USERNAME");
						if (userName.equals(userr.getUSERNAME()))
							userList2.add(pdata);
					}
					userList.clear();
					userList.addAll(userList2);
				} else {// 当前用户所属角色还有下级角色,则查看下级角色下的所有用户
					// 遍历所有角色下的用户集合
					for (Role r : roleList) {
						PageData pds = new PageData();
						pds.put("ROLE_ID", r.getROLE_ID());
						page.setPd(pds);
						userList.addAll(userService.listPdPageUser(page));
					}
					// 放入当前登录用户信息
					PageData userPageData = new PageData();
					userPageData.put("USERNAME", user.getUSERNAME());
					Page userPage = new Page();
					userPage.setPd(userPageData);
					userList.addAll(userService.listPdPageUser(userPage));
				}
			} else {// 系统管理员,最高权限
				page.setPd(pd);
				userList = userService.listPdPageUser(page); // 列出用户列表
				roleList = roleService.listAllERRoles(); // 列出所有二级角色
			}
		}
		// 判断是不是系统管理员，是传1，不是传0
		if (user.getUSERNAME().equals("admin")) {
			mv.addObject("isAdmin", DataStatus.ENABLED);
		} else {
			mv.addObject("isAdmin", DataStatus.DISABLED);
		}
		List<Map.Entry<String, String>> mappingList = null;
		// 通过ArrayList构造函数把map.entrySet()转换成list
		mappingList = new ArrayList<Map.Entry<String, String>>(
				WebConstants.stateMap.entrySet());
		// 通过比较器实现比较排序
		Collections.sort(mappingList,
				new Comparator<Map.Entry<String, String>>() {
					public int compare(Map.Entry<String, String> mapping1,
							Map.Entry<String, String> mapping2) {
						return mapping2.getKey().compareTo(mapping1.getKey());
					}
				});

		mv.addObject("stateMap",mappingList);
		mv.setViewName("system/user/user_list");
		mv.addObject("userList", userList);
		roleList.add(user.getRole());
		mv.addObject("roleList", roleList);
		mv.addObject("pd", pd);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
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

	/**
	 * 显示用户列表(tab方式)
	 */
	@RequestMapping(value = "/listtabUsers")
	public ModelAndView listtabUsers(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		List<PageData> userList = userService.listAllUser(pd); // 列出用户列表
		mv.setViewName("system/user/user_tb_list");
		mv.addObject("userList", userList);
		mv.addObject("pd", pd);
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	/**
	 * 删除用户
	 */
	@RequestMapping(value = "/deleteU")
	public void deleteU(PrintWriter out) {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
				userService.deleteU(pd);
			}
			out.write("success");
			out.close();
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}

	}

	/**
	 * 批量删除
	 */
	@RequestMapping(value = "/deleteAllU")
	@ResponseBody
	public Object deleteAllU() {
		PageData pd = new PageData();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pd = this.getPageData();
			List<PageData> pdList = new ArrayList<PageData>();
			String USER_IDS = pd.getString("USER_IDS");

			if (null != USER_IDS && !"".equals(USER_IDS)) {
				String ArrayUSER_IDS[] = USER_IDS.split(",");
				if (Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
					userService.deleteAllU(ArrayUSER_IDS);
				}
				pd.put("msg", "ok");
			} else {
				pd.put("msg", "no");
			}

			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			logAfter(logger);
		}
		return AppUtil.returnObject(pd, map);
	}

	// ===================================================================================================

	/*
	 * 导出用户信息到EXCEL
	 * 
	 * @return
	 */
	@RequestMapping(value = "/excel")
	public ModelAndView exportExcel() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			if (Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
				// 检索条件===
				String USERNAME = pd.getString("USERNAME");
				if (null != USERNAME && !"".equals(USERNAME)) {
					USERNAME = USERNAME.trim();
					pd.put("USERNAME", USERNAME);
				}
				String lastLoginStart = pd.getString("lastLoginStart");
				String lastLoginEnd = pd.getString("lastLoginEnd");
				if (lastLoginStart != null && !"".equals(lastLoginStart)) {
					lastLoginStart = lastLoginStart + " 00:00:00";
					pd.put("lastLoginStart", lastLoginStart);
				}
				if (lastLoginEnd != null && !"".equals(lastLoginEnd)) {
					lastLoginEnd = lastLoginEnd + " 00:00:00";
					pd.put("lastLoginEnd", lastLoginEnd);
				}
				// 检索条件===

				Map<String, Object> dataMap = new HashMap<String, Object>();
				List<String> titles = new ArrayList<String>();

				titles.add("用户名"); // 1
				titles.add("余额"); // 2
				titles.add("姓名"); // 3
				titles.add("职位"); // 4
				titles.add("手机"); // 5
				titles.add("证件编号"); // 6
				titles.add("邮箱"); // 7
				titles.add("最近登录"); // 8
				titles.add("上次登录IP"); // 9
				titles.add("备注"); // 10

				dataMap.put("titles", titles);

				List<PageData> userList = userService.listAllUser(pd);
				List<PageData> varList = new ArrayList<PageData>();
				for (int i = 0; i < userList.size(); i++) {
					PageData vpd = new PageData();
					vpd.put("var1", userList.get(i).getString("USERNAME")); // 1
					vpd.put("var2", userList.get(i).getString("blance")); // 2
					vpd.put("var3", userList.get(i).getString("NAME")); // 3
					vpd.put("var4", userList.get(i).getString("ROLE_NAME")); // 4
					vpd.put("var5", userList.get(i).getString("PHONE")); // 5
					vpd.put("var6", userList.get(i).getString("certificate_no")); // 6
					vpd.put("var7", userList.get(i).getString("EMAIL")); // 7
					vpd.put("var8", userList.get(i).getString("LAST_LOGIN")); // 8
					vpd.put("var9", userList.get(i).getString("IP"));//9
					vpd.put("var10", userList.get(i).getString("BZ")); // 10
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

	/**
	 * 打开上传EXCEL页面
	 */
	@RequestMapping(value = "/goUploadExcel")
	public ModelAndView goUploadExcel() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/user/uploadexcel");
		return mv;
	}

	/**
	 * 下载模版
	 */
	@RequestMapping(value = "/downExcel")
	public void downExcel(HttpServletResponse response) throws Exception {

		FileDownload.fileDownload(response, PathUtil.getClasspath()
				+ Const.FILEPATHFILE + "Users.xls", "Users.xls");

	}

	/**
	 * 从EXCEL导入到数据库
	 */
	@RequestMapping(value = "/readExcel")
	public ModelAndView readExcel(
			@RequestParam(value = "excel", required = false) MultipartFile file)
			throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
			return null;
		}
		if (null != file && !file.isEmpty()) {
			String filePath = PathUtil.getClassResources() + Const.FILEPATHFILE; // 文件上传路径
			String fileName = FileUpload.fileUp(file, filePath, "userexcel"); // 执行上传

			List<PageData> listPd = (List) ObjectExcelRead.readExcel(filePath,
					fileName, 1, 0, 0); // 执行读EXCEL操作,读出的数据导入List
										// 2:从第3行开始；0:从第A列开始；0:第0个sheet

			/* 存入数据库操作====================================== */
			pd.put("RIGHTS", ""); // 权限
			pd.put("LAST_LOGIN", ""); // 最后登录时间
			pd.put("IP", ""); // IP
			pd.put("STATUS", "0"); // 状态
			pd.put("SKIN", "default"); // 默认皮肤

			List<Role> roleList = roleService.listAllERRoles(); // 列出所有二级角色

			pd.put("ROLE_ID", roleList.get(0).getROLE_ID()); // 设置角色ID为随便第一个
			/**
			 * var0 :编号 var1 :姓名 var2 :手机 var3 :邮箱 var4 :备注
			 */
			for (int i = 0; i < listPd.size(); i++) {
				pd.put("USER_ID", this.get32UUID()); // ID
				pd.put("NAME", listPd.get(i).getString("var2")); // 姓名

				String USERNAME = GetPinyin.getPingYin(listPd.get(i).getString(
						"var2")); // 根据姓名汉字生成全拼
				pd.put("USERNAME", USERNAME);
				if (userService.findByUId(pd) != null) { // 判断用户名是否重复
					USERNAME = GetPinyin.getPingYin(listPd.get(i).getString(
							"var1"))
							+ Tools.getRandomNum();
					pd.put("USERNAME", USERNAME);
				}
				pd.put("BZ", listPd.get(i).getString("var8")); // 备注
				if (Tools.checkEmail(listPd.get(i).getString("var5"))) { // 邮箱格式不对就跳过
					pd.put("EMAIL", listPd.get(i).getString("var5"));
					if (userService.findByUE(pd) != null) { // 邮箱已存在就跳过
						continue;
					}
				} else {
					continue;
				}

				pd.put("NUMBER", listPd.get(i).getString("var1")); // 编号已存在就跳过
				pd.put("PHONE", listPd.get(i).getString("var4")); // 手机号

				pd.put("PASSWORD",
						new SimpleHash("SHA-1", USERNAME, "123").toString()); // 默认密码123
				if (userService.findByUN(pd) != null) {
					continue;
				}
				userService.saveU(pd);
			}
			/* 存入数据库操作====================================== */

			mv.addObject("msg", "success");
		}

		mv.setViewName("save_result");
		return mv;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,
				true));
	}

	/* ===============================权限================================== */
	public Map<String, String> getHC() {
		Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
	}
	/* ===============================权限================================== */
}
