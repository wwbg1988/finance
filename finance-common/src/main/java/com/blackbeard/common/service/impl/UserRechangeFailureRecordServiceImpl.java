package com.blackbeard.common.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.dao.web.UserRechangeFailureRecordDao;
import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.dto.UserRechangeFailureRecordDto;
import com.blackbeard.common.pojo.web.UserRechangeFailureRecord;
import com.blackbeard.common.service.IUserRechangeFailureRecordService;
import com.blackbeard.common.service.UserService;
import com.ssic.util.BeanUtils;
import com.ssic.util.constants.DataStatus;

/**
 * 用户充值失败记录Service层实现类
 * 
 * @author 刘博
 *
 */
@Service
public class UserRechangeFailureRecordServiceImpl implements
		IUserRechangeFailureRecordService {

	@Autowired
	private UserRechangeFailureRecordDao dao;
	@Resource(name = "userService")
	private UserService userService;

	@Override
	public void insert(UserRechangeFailureRecordDto dto) {
		UserRechangeFailureRecord failureRecord = new UserRechangeFailureRecord();
		BeanUtils.copyProperties(dto, failureRecord);
		failureRecord.setStat(DataStatus.ENABLED);
		failureRecord.setCreateTime(new Date());
		failureRecord.setLastUpdateTime(new Date());
		dao.insert(failureRecord);
	}

	@Override
	public void update(UserRechangeFailureRecordDto dto) {
		UserRechangeFailureRecord failureRecord = new UserRechangeFailureRecord();
		BeanUtils.copyProperties(dto, failureRecord);
		failureRecord.setLastUpdateTime(new Date());
		dao.update(failureRecord);
	}

	@Override
	public List<UserRechangeFailureRecordDto> findListByPage(
			UserRechangeFailureRecordDto recordDto, LimitPageDto limitPageDto) {
		UserRechangeFailureRecord failureRecord = new UserRechangeFailureRecord();
		BeanUtils.copyProperties(recordDto, failureRecord);
		List<UserRechangeFailureRecord> list = dao.findListByPage(
				failureRecord, limitPageDto);
		List<UserRechangeFailureRecordDto> listDto = BeanUtils
				.createBeanListByTarget(list,
						UserRechangeFailureRecordDto.class);
		for (UserRechangeFailureRecordDto dto : listDto) {
			String createTimeFormatString = DateUtil.formatDate((dto.getCreateTime()));
			dto.setCreateTimeFormatString(createTimeFormatString);
			PageData pd = new PageData();
			pd.put("USER_ID", dto.getUserId());
			try {
				pd = userService.findByUiId(pd);
				if (pd != null && pd.containsKey("NAME")) {
					dto.setUserName(pd.getString("NAME"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return listDto;
	}

	@Override
	public int findCount() {
		return dao.findCount();
	}

	@Override
	public UserRechangeFailureRecordDto findById(String id) {
		UserRechangeFailureRecordDto dto = new UserRechangeFailureRecordDto();
		UserRechangeFailureRecord failureRecord = dao.findById(id);
		if (failureRecord != null) {
			dto = BeanUtils.createBeanByTarget(failureRecord,
					UserRechangeFailureRecordDto.class);
		}
		return dto;
	}

	@Override
	public void delete(UserRechangeFailureRecordDto failureRecordDto) {
		UserRechangeFailureRecord failureRecord = new UserRechangeFailureRecord();
		BeanUtils.copyProperties(failureRecordDto, failureRecord);
		failureRecord.setLastUpdateTime(new Date());
		dao.update(failureRecord);
	}

}
