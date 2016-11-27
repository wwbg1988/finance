package com.blackbeard.common.dao.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.mapper.web.UserLoginStateExMapper;

@Repository
public class UserLoginStateDao {

	@Autowired
	private UserLoginStateExMapper exmapper;
	
	public void addUserLogin(UserLoginStateDto userLoginStateDto){
		userLoginStateDto.setCreateTime(new Date());
		exmapper.addUserLogin(userLoginStateDto);
	}
	
	public List<UserLoginStateDto> findBy(UserLoginStateDto userLoginStateDto){
		return exmapper.findBy(userLoginStateDto);
	}
	
	
	public void editUserLoginAmount(UserLoginStateDto userLoginStateDto){
		userLoginStateDto.setUpdateTime(new Date());
		exmapper.editUserLoginAmount(userLoginStateDto);
	}
	
	public void reduceUserAmount(UserLoginStateDto userLoginStateDto){
		userLoginStateDto.setUpdateTime(new Date());
		exmapper.reduceUserAmount(userLoginStateDto);
	}
	
	public void delUserStateByAmount(){
		exmapper.delUserStateByAmount();
	}
	
	public void reduceUserBlance(UserLoginStateDto userLoginStateDto){
		exmapper.reduceUserBlance(userLoginStateDto);
	}
	
	public void updateBlance(UserLoginStateDto userLoginStateDto){
		exmapper.updateBlance(userLoginStateDto);
	}
	
}
