package com.blackbeard.common.pojo.web;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.blackbeard.common.dto.Page;

/**
 * 
 * 类名称：User.java 类描述：
 * 
 * @author FH 作者单位： 联系方式： 创建时间：2014年6月28日
 * @version 1.0
 */
public class User {
	private String USER_ID; // 用户id
	private String USERNAME; // 用户名
	private String PASSWORD; // 密码
	private String NAME; // 姓名
	private String RIGHTS; // 权限
	private String ROLE_ID; // 角色id
	private String LAST_LOGIN; // 最后登录时间
	private String IP; // 用户登录ip地址
	private String STATUS; // 状态
	private Role role; // 角色对象
	private Page page; // 分页对象
	private String SKIN;// 皮肤
	@Getter
	@Setter
	private String PHONE;//手机号码
	@Getter
	@Setter
	private String EMAIL;// 邮箱

	/**
	 * mt4Id
	 */
	@Getter
	@Setter
	private String mt4Id;

	/**
	 * MT4用户组id
	 */
	@Getter
	@Setter
	private String mt4UserGroup;

	/**
	 * 用户状态:0 注册,1 完成,2 在线,3 离线
	 */

	@Getter
	@Setter
	private Integer userState;

	/**
	 * 余额,乘以100保存bigint
	 */
	@Getter
	@Setter
	private Long blance;

	/**
	 * 证件编号
	 */
	@Getter
	@Setter
	private String certificateNo;

	/**
	 * 证件图片保存地址
	 */
	@Getter
	@Setter
	private String certificateUrl;



	/**
	 * 创建时间
	 */
	@Getter
	@Setter
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Getter
	@Setter
	private Date lastUpdateTime;

	@Getter
	@Setter
	private String md5Message; // server发给pc 的用户名MD5加密

	public String getSKIN() {
		return SKIN;
	}

	public void setSKIN(String sKIN) {
		SKIN = sKIN;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getRIGHTS() {
		return RIGHTS;
	}

	public void setRIGHTS(String rIGHTS) {
		RIGHTS = rIGHTS;
	}

	public String getROLE_ID() {
		return ROLE_ID;
	}

	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}

	public String getLAST_LOGIN() {
		return LAST_LOGIN;
	}

	public void setLAST_LOGIN(String lAST_LOGIN) {
		LAST_LOGIN = lAST_LOGIN;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Page getPage() {
		if (page == null)
			page = new Page();
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
