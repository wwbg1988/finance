package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.pojo.web.UserInfo;

public interface IUserInfoService {

	void insert(UserInfoDto dto);

	/**
	 * 通过用户名查找用户
	 * 
	 * @param userName
	 * @return boolean
	 * @date 2016年6月22日 上午11:41:13
	 */
	boolean findByUserName(String userName);

	/**
	 * findCountBy：查找用户数量
	 * 
	 * @return int
	 * @exception
	 * @author 刘博
	 * @date 2016年6月22日 上午11:41:13
	 */
	public int findCount();

	// 查询用户信息
	public List<UserInfoDto> findBy(UserInfoDto userInfoDto);

	/**
	 * findByUserNameAndPwd：查找用户数量
	 * 
	 * @param name
	 * @param md5Pwd
	 * @return UserInfo
	 * @exception
	 * @author 刘博
	 * @date 2016年6月22日 上午11:51:13
	 */
	UserInfo findByUserNameAndPwd(String name, String md5Pwd);

	// 把用户状态表中amount为0的数据更新到userinfo，删除
	public void addHistoryToUser();

	/**
	 * 审核用户信息
	 * 
	 * @param UserInfoDto
	 * @return
	 * @exception
	 * @author 刘博
	 * @date 2016年6月24日 上午11:51:13
	 */
	void auditUserInfo(UserInfoDto userInfoDto);
	
	
	 /**     
     * findListByPage：查找所有的用户信息
     * @param userInfoDto
     * @param limitPageDto 
     * @return
     * @exception	
     * @author 刘博
     * @date 2016年6月29日 下午4:09:22	 
     */
    
	List<UserInfoDto> findListByPage(UserInfoDto userInfoDto,
			LimitPageDto limitPageDto);

	
	//用户在线
	
	public void updateOnLine(UserInfoDto userInfoDto);
	
	//用户离线
	
	public void updateOutLine(UserInfoDto userInfoDto);
}
