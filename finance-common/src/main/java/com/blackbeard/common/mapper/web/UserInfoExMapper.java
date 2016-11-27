package com.blackbeard.common.mapper.web;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackbeard.common.dto.UserInfoDto;

public interface UserInfoExMapper {

	public List<UserInfoDto> findBy(@Param("userInfoDto") UserInfoDto userInfoDto);
	
	public void updateUserByHistory();
	
	public void updateOnLine(@Param("userInfoDto") UserInfoDto userInfoDto);
	
	public void updateOutLine(@Param("userInfoDto") UserInfoDto userInfoDto);
	
}
