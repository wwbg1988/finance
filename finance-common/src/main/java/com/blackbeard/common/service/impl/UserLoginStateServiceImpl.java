package com.blackbeard.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.dao.web.UserLoginStateDao;
import com.blackbeard.common.dto.UserLoginStateDto;
import com.blackbeard.common.service.IUserLoginStateService;

@Service
public class UserLoginStateServiceImpl implements IUserLoginStateService{

	@Autowired
	private UserLoginStateDao userLoginStateDao;
	
	
	@Override
	public void addUserLogin(UserLoginStateDto userLoginStateDto) {
		// TODO Auto-generated method stub
		userLoginStateDao.addUserLogin(userLoginStateDto);
	}

	@Override
	public List<UserLoginStateDto> findBy(UserLoginStateDto userLoginStateDto) {
		// TODO Auto-generated method stub
		return userLoginStateDao.findBy(userLoginStateDto);
	}

	@Override
	public void editUserLoginAmount(UserLoginStateDto userLoginStateDto) {
		// TODO Auto-generated method stub
		userLoginStateDao.editUserLoginAmount(userLoginStateDto);
	}

	@Override
	public void reduceUserAmount(UserLoginStateDto userLoginStateDto) {
		// TODO Auto-generated method stub
		//登录用户的amount-1
		userLoginStateDao.reduceUserAmount(userLoginStateDto);
	}

	@Override
	public void reduceUserBlance(UserLoginStateDto userLoginStateDto) {
		// TODO Auto-generated method stub
		userLoginStateDao.reduceUserBlance(userLoginStateDto);
	}

	@Override
	public void updateBlance(UserLoginStateDto userLoginStateDto) {
		// TODO Auto-generated method stub
		userLoginStateDao.updateBlance(userLoginStateDto);
	}

	
}
