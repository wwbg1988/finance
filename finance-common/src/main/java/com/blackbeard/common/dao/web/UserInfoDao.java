package com.blackbeard.common.dao.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.UserInfoDto;
import com.blackbeard.common.mapper.web.UserInfoExMapper;
import com.blackbeard.common.mapper.web.UserInfoMapper;
import com.blackbeard.common.pojo.web.UserInfo;
import com.blackbeard.common.pojo.web.UserInfoExample;
import com.blackbeard.common.pojo.web.UserInfoExample.Criteria;
import com.ssic.util.constants.DataStatus;

@Repository
public class UserInfoDao {

	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserInfoExMapper userInfoExMapper;

	public void insertKline(UserInfo userInfo) {
		userInfoMapper.insert(userInfo);
	}

	/**
	 * findByUserName：通过用户名查找用户
	 * 
	 * @return boolean
	 * @exception
	 * @author 刘博
	 * @date 2016年6月22日 上午11:41:13
	 */
	public boolean findByUserName(String userName) {
		UserInfoExample example = new UserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!StringUtils.isEmpty(userName)) {
			criteria.andUserNameEqualTo(userName);
		}
		List<UserInfo> list = userInfoMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			return true;
		}
		return false;
	}

	/**
	 * findCountBy：查找用户数量
	 * 
	 * @return int
	 * @exception
	 * @author 刘博
	 * @date 2016年6月22日 上午11:41:13
	 */
	public int findCount() {
		UserInfoExample example = new UserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		int count = userInfoMapper.countByExample(example);
		return count;
	}

	public UserInfo findByUserNameAndPwd(String name, String md5Pwd) {
		UserInfoExample example = new UserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!StringUtils.isEmpty(name)) {
			criteria.andUserNameEqualTo(name);
		}
		if (!StringUtils.isEmpty(md5Pwd)) {
			criteria.andUserPasswordEqualTo(md5Pwd);
		}
		List<UserInfo> list = userInfoMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	public List<UserInfoDto> findBy(UserInfoDto userInfoDto) {
		return userInfoExMapper.findBy(userInfoDto);
	}

	public void updateUserByHistory() {
		userInfoExMapper.updateUserByHistory();
	}

	public void audit(UserInfoDto userInfoDto) {
		UserInfo user = userInfoMapper.selectByPrimaryKey(userInfoDto.getId());
		user.setUserState(userInfoDto.getUserState());
		user.setMt4Id(userInfoDto.getMt4Id());
		user.setLastUpdateTime(new Date());
		userInfoMapper.updateByPrimaryKeySelective(user);
	}

	public List<UserInfo> findListByPage(UserInfo userInfo,
			LimitPageDto limitPageDto) {
		UserInfoExample example = new UserInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!StringUtils.isEmpty(userInfo.getUserName())) {
			criteria.andUserNameEqualTo(userInfo.getUserName());
		}
		if (!StringUtils.isEmpty(userInfo.getUserTelphone())) {
			criteria.andUserTelphoneEqualTo(userInfo.getUserTelphone());
		}
		if (!StringUtils.isEmpty(userInfo.getCertificateNo())) {
			criteria.andCertificateNoEqualTo(userInfo.getCertificateNo());
		}
		// 如果有分页对象;
		if (limitPageDto != null
				&& !StringUtils.isEmpty(limitPageDto.getStar())
				&& !StringUtils.isEmpty(limitPageDto.getEnd())) {
			example.setOrderByClause("create_time desc limit "
					+ limitPageDto.getStar() + "," + limitPageDto.getEnd());
		}
		return userInfoMapper.selectByExample(example);

	}
	
	//用户在线
	public void updateOnLine(UserInfoDto userInfoDto){
		userInfoExMapper.updateOnLine(userInfoDto);
	}
	//用户离线
	public void updateOutLine(UserInfoDto userInfoDto){
		userInfoExMapper.updateOutLine(userInfoDto);
	}
}
