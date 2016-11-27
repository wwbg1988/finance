package com.blackbeard.common.mapper.web;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackbeard.common.dto.UserLoginStateDto;

public interface UserLoginStateExMapper {

	public void  addUserLogin(@Param("userLoginStateDto") UserLoginStateDto userLoginStateDto);
	
	public List<UserLoginStateDto> findBy(@Param("userLoginStateDto") UserLoginStateDto userLoginStateDto);
	
	public void editUserLoginAmount(@Param("userLoginStateDto") UserLoginStateDto userLoginStateDto);
	
	public void reduceUserAmount(@Param("userLoginStateDto") UserLoginStateDto userLoginStateDto);
	
	public void delUserStateByAmount();
	
	public void reduceUserBlance(@Param("userLoginStateDto") UserLoginStateDto userLoginStateDto);
	
	public void updateBlance(@Param("userLoginStateDto") UserLoginStateDto userLoginStateDto);
}
