package com.blackbeard.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blackbeard.common.dao.web.UserInfoDao;
import com.blackbeard.common.dao.web.UserLoginStateDao;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.pojo.web.UserInfo;
import com.blackbeard.common.service.IUserInfoService;
import com.ssic.util.BeanUtils;
import com.ssic.util.constants.DataStatus;

@Service
public class UserInfoServiceImpl implements IUserInfoService {
	@Autowired
	private UserInfoDao userInfoDao;
	@Autowired
	private UserLoginStateDao userLoginStateDao;

	@Override
	public void insert(UserInfoDto dto) {
		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(dto, userInfo);
		userInfo.setStat(DataStatus.ENABLED);
		userInfo.setCreateTime(new Date());
		userInfo.setLastUpdateTime(new Date());
		userInfoDao.insertKline(userInfo);
	}

	@Override
	public boolean findByUserName(String userName) {
		return userInfoDao.findByUserName(userName);

	}

	@Override
	public int findCount() {
		return userInfoDao.findCount();

	}

	@Override
	public List<UserInfoDto> findBy(UserInfoDto userInfoDto) {
		// TODO Auto-generated method stub
		return userInfoDao.findBy(userInfoDto);
	}

	@Override
	public UserInfo findByUserNameAndPwd(String name, String md5Pwd) {
		UserInfo user = userInfoDao.findByUserNameAndPwd(name, md5Pwd);
		return user;
	}

	@Override
	@Transactional
	public void addHistoryToUser() {
		// TODO Auto-generated method stub
		// 将登陆状态更新到用户表
		// 删除amount=0等数据
		userInfoDao.updateUserByHistory();
		userLoginStateDao.delUserStateByAmount();
	}

	@Override
	public void auditUserInfo(UserInfoDto userInfoDto) {
		userInfoDao.audit(userInfoDto);
	}

	@Override
	public List<UserInfoDto> findListByPage(UserInfoDto userInfoDto,
			LimitPageDto limitPageDto) {
		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(userInfoDto, userInfo);
		List<UserInfo> list = userInfoDao
				.findListByPage(userInfo, limitPageDto);
		List<UserInfoDto> listDto = BeanUtils.createBeanListByTarget(list,
				UserInfoDto.class);
		return listDto;
	}

	@Override
	public void updateOnLine(UserInfoDto userInfoDto) {
		// TODO Auto-generated method stub
		userInfoDto.setLastLogin(new Date());
		userInfoDao.updateOnLine(userInfoDto);
	}

	@Override
	public void updateOutLine(UserInfoDto userInfoDto) {
		// TODO Auto-generated method stub
		userInfoDao.updateOutLine(userInfoDto);
	}
}
