package com.blackbeard.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.dao.web.AccountConfigDao;
import com.blackbeard.common.dto.AccountConfigDto;
import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Mt4ConfigDto;
import com.blackbeard.common.pojo.web.AccountConfig;
import com.blackbeard.common.pojo.web.Mt4Config;
import com.blackbeard.common.service.IAccountConfigService;
import com.ssic.util.BeanUtils;
import com.ssic.util.constants.DataStatus;

@Service
public class AccountConfigServiceImpl implements IAccountConfigService {

	@Autowired
	private AccountConfigDao accountConfigDao;

	@Override
	public void insert(AccountConfigDto dto) {
		AccountConfig accountConfig = new AccountConfig();
		BeanUtils.copyProperties(dto, accountConfig);
		accountConfig.setStat(DataStatus.ENABLED);
		accountConfig.setCreateTime(new Date());
		accountConfig.setLastUpdateTime(new Date());
		accountConfigDao.insert(accountConfig);
	}

	@Override
	public void update(AccountConfigDto dto) {
		AccountConfig accountConfig = new AccountConfig();
		BeanUtils.copyProperties(dto, accountConfig);
		accountConfig.setLastUpdateTime(new Date());
		accountConfigDao.update(accountConfig);
	}

	@Override
	public List<AccountConfigDto> findListByPage(
			AccountConfigDto accountConfigDto, LimitPageDto limitPageDto) {
		AccountConfig accountConfig = new AccountConfig();
		BeanUtils.copyProperties(accountConfigDto, accountConfig);
		List<AccountConfig> list = accountConfigDao.findListByPage(
				accountConfig, limitPageDto);
		List<AccountConfigDto> listDto = BeanUtils.createBeanListByTarget(list,
				AccountConfigDto.class);
		for (AccountConfigDto configDto : listDto) {
			String createTimeFormatString = DateUtil.formatDate(configDto
					.getCreateTime());
			configDto.setCreateTimeFormatString(createTimeFormatString);
		}
		return listDto;
	}

	@Override
	public int findCount() {
		return accountConfigDao.findCount();
	}

	@Override
	public AccountConfigDto findById(String id) {
		AccountConfigDto dto = new AccountConfigDto();
		AccountConfig accountConfig = accountConfigDao.findById(id);
		if (accountConfig != null) {
			dto = BeanUtils.createBeanByTarget(accountConfig,
					AccountConfigDto.class);
		}
		return dto;
	}

	@Override
	public void delete(AccountConfigDto accountConfigDto) {
		AccountConfig accountConfig = new AccountConfig();
		BeanUtils.copyProperties(accountConfigDto, accountConfig);
		accountConfig.setLastUpdateTime(new Date());
		accountConfigDao.update(accountConfig);
	}

}
