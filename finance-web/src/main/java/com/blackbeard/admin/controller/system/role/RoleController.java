package com.blackbeard.admin.controller.system.role;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.blackbeard.admin.entity.system.Menu;
import com.blackbeard.admin.service.system.menu.MenuService;
import com.blackbeard.admin.service.system.role.RoleService;
import com.blackbeard.admin.util.AppUtil;
import com.blackbeard.admin.util.Jurisdiction;
import com.blackbeard.admin.util.RightsHelper;
import com.blackbeard.common.dto.Const;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.Tools;
import com.blackbeard.common.pojo.web.Role;
import com.blackbeard.common.pojo.web.User;
import com.blackbeard.common.service.UserService;
import com.blackbeard.web.controller.BaseController;

/**
 * 类名称：RoleController 创建人：FH 创建时间：2014年6月30日
 * 
 * @version
 */
@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

	String menuUrl = "role.do"; // 菜单地址(权限用)
	@Resource(name = "menuService")
	private MenuService menuService;
	@Resource(name = "roleService")
	private RoleService roleService;
	@Resource(name = "userService")
	private UserService userService;

	/**
	 * 权限(增删改查)
	 */
	@RequestMapping(value = "/qx")
	public ModelAndView qx() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String msg = pd.getString("msg");
			if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
				roleService.updateQx(msg, pd);
			}
			mv.setViewName("save_result");
			mv.addObject("msg", "success");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * K权限
	 */
	@RequestMapping(value = "/kfqx")
	public ModelAndView kfqx() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String msg = pd.getString("msg");
			if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
				roleService.updateKFQx(msg, pd);
			}
			mv.setViewName("save_result");
			mv.addObject("msg", "success");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * c权限
	 */
	@RequestMapping(value = "/gysqxc")
	public ModelAndView gysqxc() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String msg = pd.getString("msg");
			if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
				roleService.gysqxc(msg, pd);
			}
			mv.setViewName("save_result");
			mv.addObject("msg", "success");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 列表
	 */
	@RequestMapping
	public ModelAndView list(Page page, HttpServletRequest request)
			throws Exception {
		ModelAndView mv = this.getModelAndView();
		List<Role> roleList_z = new ArrayList<Role>();
		List<Role> roleList_result = new ArrayList<Role>();
		PageData pd = new PageData();
		pd = this.getPageData();

		String roleId = pd.getString("ROLE_ID");
		if (roleId == null || "".equals(roleId)) {
			pd.put("ROLE_ID", "1");
		}
		List<Role> roleList = roleService.listAllRoles(); // 列出所有部门
		// shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		if (user != null) {
			if (user.getUSERNAME().equals("admin")) {// 如果是系统管理员(最大权限).可以查看所有角色

				roleList_z = roleService.listAllRolesByPId(pd); // 列出此部门的所有下级

				for (Role role : roleList_z) {
					// 查询所有的子角色，最后全部放入roleList_z集合中
					findChildrenRoleByPid(roleList_result, role.getROLE_ID());
				}
				roleList_z.addAll(roleList_result);
			} else {// 如果不是系统管理员，则根据登录用户的角色id查询当前角色以及他的下级分支角色
				String role_Id = user.getROLE_ID();
				// 查询所有的子角色，最后全部放入roleList_result集合中
				findChildrenRoleByPid(roleList_result, role_Id);
				// 把roleList_result的结果放入最终角色结果集roleList_z中;
				roleList_z.addAll(roleList_result);
			}
		}
		for (Role role : roleList_z) {// 遍历角色集合 ，重新查找父角色名称 ，放入返回角色集合中
			PageData role_pd = new PageData();
			role_pd.put("ROLE_ID", role.getPARENT_ID());
			PageData pd_result = roleService.findObjectById(role_pd);
			role.setPARENT_NAME(pd_result.getString("ROLE_NAME"));
			String request2 = request.getScheme() // 当前链接使用的协议
					+ "://"
					+ request.getServerName()// 服务器地址
					+ ":"
					+ request.getServerPort() // 端口号
					+ request.getContextPath()
					+ "/web/user/toRegister.do?ROLE_ID=" + role.getROLE_ID();
			role.setREGISTER_URL(request2);
			System.out.println("====请求地址：====" + request2);
		}
		List<PageData> kefuqxlist = roleService.listAllkefu(pd); // 管理权限列表
		List<PageData> gysqxlist = roleService.listAllGysQX(pd); // 用户权限列表
		pd = roleService.findObjectById(pd); // 取得点击部门
		pd.put("userName", user.getUSERNAME());
		mv.addObject("pd", pd);
		mv.addObject("kefuqxlist", kefuqxlist);
		mv.addObject("gysqxlist", gysqxlist);
		mv.addObject("roleList", roleList);
		mv.addObject("roleList_z", roleList_z);
		mv.setViewName("system/role/role_list");
		mv.addObject(Const.SESSION_QX, this.getHC()); // 按钮权限
		return mv;
	}

	public List<Role> findChildrenRoleByPid(List<Role> roleList_result,
			String pId) throws Exception {
		if (!StringUtils.isEmpty(pId)) {// 如果父id不为空
			PageData children_pd = new PageData();
			children_pd.put("ROLE_ID", pId);
			// 根据父id查询所有所有的子角色
			List<Role> childrenRoleList = roleService
					.listAllRolesByPId(children_pd);
			if (!CollectionUtils.isEmpty(childrenRoleList)) {
				// 将所有的子角色加入进去
				roleList_result.addAll(childrenRoleList);
				for (Role childRole : childrenRoleList) {// 继续循环，只要有子角色id是父id在全部加入集合中
					findChildrenRoleByPid(roleList_result,
							childRole.getROLE_ID());
				}
			}
		}
		// 返回所有的角色对象
		return roleList_result;
	}

	/**
	 * 新增页面
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	public ModelAndView toAdd(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
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
		pd = this.getPageData();
		mv.setViewName("system/role/role_add");
		mv.addObject("roleList", roleList);
		mv.addObject("pd", pd);
		return mv;
	}

	/**
	 * 保存新增信息
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView add() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String parent_id = pd.getString("PARENT_ID"); // 父类角色id
			if (parent_id == null || "".equals(parent_id)) {
				parent_id = "1";
				pd.put("PARENT_ID", parent_id);
			}
			pd.put("ROLE_ID", parent_id);
			if ("0".equals(parent_id)) {
				pd.put("RIGHTS", "");
			} else {
				String rights = roleService.findObjectById(pd).getString(
						"RIGHTS");
				pd.put("RIGHTS", (null == rights) ? "" : rights);
			}

			pd.put("QX_ID", "");

			String UUID = this.get32UUID();

			pd.put("GL_ID", UUID);
			pd.put("FX_QX", 0); // 发信权限
			pd.put("FW_QX", 0); // 服务权限
			pd.put("QX1", 0); // 操作权限
			pd.put("QX2", 0); // 产品权限
			pd.put("QX3", 0); // 预留权限
			pd.put("QX4", 0); // 预留权限
			if (Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
				roleService.saveKeFu(pd);
			}// 保存到K权限表

			pd.put("U_ID", UUID);
			pd.put("C1", 0); // 每日发信数量
			pd.put("C2", 0);
			pd.put("C3", 0);
			pd.put("C4", 0);
			pd.put("Q1", 0); // 权限1
			pd.put("Q2", 0); // 权限2
			pd.put("Q3", 0);
			pd.put("Q4", 0);
			if (Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
				roleService.saveGYSQX(pd);
			}// 保存到G权限表
			pd.put("QX_ID", UUID);

			pd.put("ROLE_ID", UUID);
			pd.put("ADD_QX", "0");
			pd.put("DEL_QX", "0");
			pd.put("EDIT_QX", "0");
			pd.put("CHA_QX", "0");
			if (Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
				roleService.add(pd);
			}
			mv.addObject("msg", "success");
		} catch (Exception e) {
			logger.error(e.toString(), e);
			mv.addObject("msg", "failed");
		}
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 请求编辑
	 */
	@RequestMapping(value = "/toEdit")
	public ModelAndView toEdit(String ROLE_ID) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			pd.put("ROLE_ID", ROLE_ID);
			pd = roleService.findObjectById(pd);
			mv.setViewName("system/role/role_edit");
			mv.addObject("pd", pd);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return mv;
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
				pd = roleService.edit(pd);
			}
			mv.addObject("msg", "success");
		} catch (Exception e) {
			logger.error(e.toString(), e);
			mv.addObject("msg", "failed");
		}
		mv.setViewName("save_result");
		return mv;
	}

	/**
	 * 请求角色菜单授权页面
	 */
	@RequestMapping(value = "/auth")
	public String auth(@RequestParam String ROLE_ID, Model model)
			throws Exception {

		try {
			List<Menu> menuList = menuService.listAllMenu();
			Role role = roleService.getRoleById(ROLE_ID);
			String roleRights = role.getRIGHTS();
			if (Tools.notEmpty(roleRights)) {
				for (Menu menu : menuList) {
					menu.setHasMenu(RightsHelper.testRights(roleRights,
							menu.getMENU_ID()));
					if (menu.isHasMenu()) {
						List<Menu> subMenuList = menu.getSubMenu();
						for (Menu sub : subMenuList) {
							sub.setHasMenu(RightsHelper.testRights(roleRights,
									sub.getMENU_ID()));
						}
					}
				}
			}
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			json = json.replaceAll("MENU_ID", "id")
					.replaceAll("MENU_NAME", "name")
					.replaceAll("subMenu", "nodes")
					.replaceAll("hasMenu", "checked");
			model.addAttribute("zTreeNodes", json);
			model.addAttribute("roleId", ROLE_ID);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}

		return "authorization";
	}

	/**
	 * 请求角色按钮授权页面
	 */
	@RequestMapping(value = "/button")
	public ModelAndView button(@RequestParam String ROLE_ID,
			@RequestParam String msg, Model model) throws Exception {
		ModelAndView mv = this.getModelAndView();
		try {
			List<Menu> menuList = menuService.listAllMenu();
			Role role = roleService.getRoleById(ROLE_ID);

			String roleRights = "";
			if ("add_qx".equals(msg)) {
				roleRights = role.getADD_QX();
			} else if ("del_qx".equals(msg)) {
				roleRights = role.getDEL_QX();
			} else if ("edit_qx".equals(msg)) {
				roleRights = role.getEDIT_QX();
			} else if ("cha_qx".equals(msg)) {
				roleRights = role.getCHA_QX();
			}

			if (Tools.notEmpty(roleRights)) {
				for (Menu menu : menuList) {
					menu.setHasMenu(RightsHelper.testRights(roleRights,
							menu.getMENU_ID()));
					if (menu.isHasMenu()) {
						List<Menu> subMenuList = menu.getSubMenu();
						for (Menu sub : subMenuList) {
							sub.setHasMenu(RightsHelper.testRights(roleRights,
									sub.getMENU_ID()));
						}
					}
				}
			}
			JSONArray arr = JSONArray.fromObject(menuList);
			String json = arr.toString();
			// System.out.println(json);
			json = json.replaceAll("MENU_ID", "id")
					.replaceAll("MENU_NAME", "name")
					.replaceAll("subMenu", "nodes")
					.replaceAll("hasMenu", "checked");
			mv.addObject("zTreeNodes", json);
			mv.addObject("roleId", ROLE_ID);
			mv.addObject("msg", msg);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		mv.setViewName("system/role/role_button");
		return mv;
	}

	/**
	 * 保存角色菜单权限
	 */
	@RequestMapping(value = "/auth/save")
	public void saveAuth(@RequestParam String ROLE_ID,
			@RequestParam String menuIds, PrintWriter out) throws Exception {
		PageData pd = new PageData();
		try {
			if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
				if (null != menuIds && !"".equals(menuIds.trim())) {
					BigInteger rights = RightsHelper.sumRights(Tools
							.str2StrArray(menuIds));
					Role role = roleService.getRoleById(ROLE_ID);
					role.setRIGHTS(rights.toString());
					roleService.updateRoleRights(role);
					pd.put("rights", rights.toString());
				} else {
					Role role = new Role();
					role.setRIGHTS("");
					role.setROLE_ID(ROLE_ID);
					roleService.updateRoleRights(role);
					pd.put("rights", "");
				}

				pd.put("roleId", ROLE_ID);
				roleService.setAllRights(pd);
			}
			out.write("success");
			out.close();
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	/**
	 * 保存角色按钮权限
	 */
	@RequestMapping(value = "/roleButton/save")
	public void orleButton(@RequestParam String ROLE_ID,
			@RequestParam String menuIds, @RequestParam String msg,
			PrintWriter out) throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			if (Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
				if (null != menuIds && !"".equals(menuIds.trim())) {
					BigInteger rights = RightsHelper.sumRights(Tools
							.str2StrArray(menuIds));
					pd.put("value", rights.toString());
				} else {
					pd.put("value", "");
				}
				pd.put("ROLE_ID", ROLE_ID);
				roleService.updateQx(msg, pd);
			}
			out.write("success");
			out.close();
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object deleteRole(@RequestParam String ROLE_ID) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		PageData pd = new PageData();
		String errInfo = "";
		try {
			if (Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
				pd.put("ROLE_ID", ROLE_ID);
				List<Role> roleList_z = roleService.listAllRolesByPId(pd); // 列出此部门的所有下级
				if (roleList_z.size() > 0) {
					errInfo = "false";
				} else {

					List<PageData> userlist = roleService.listAllUByRid(pd);
					List<PageData> appuserlist = roleService
							.listAllAppUByRid(pd);
					if (userlist.size() > 0 || appuserlist.size() > 0) {
						errInfo = "false2";
					} else {
						roleService.deleteRoleById(ROLE_ID);
						roleService.deleteKeFuById(ROLE_ID);
						roleService.deleteGById(ROLE_ID);
						errInfo = "success";
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
	}

	/* ===============================权限================================== */
	public Map<String, String> getHC() {
		Subject currentUser = SecurityUtils.getSubject(); // shiro管理的session
		Session session = currentUser.getSession();
		return (Map<String, String>) session.getAttribute(Const.SESSION_QX);
	}
	/* ===============================权限================================== */

}
