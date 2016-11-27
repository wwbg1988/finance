package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.UserLoginStateDto;

public interface IUserLoginStateService {

	public void  addUserLogin(UserLoginStateDto userLoginStateDto);
	
	public List<UserLoginStateDto> findBy(UserLoginStateDto userLoginStateDto);
	
	public void editUserLoginAmount(UserLoginStateDto userLoginStateDto);
	
	//减用户登录的数量
	public void reduceUserAmount(UserLoginStateDto userLoginStateDto);
	
	//减余额
	public void reduceUserBlance(UserLoginStateDto userLoginStateDto);
	
	//更新余额
    public void updateBlance(UserLoginStateDto userLoginStateDto);
}
