package com.blackbeard.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.dao.web.CurrencyConfigDao;
import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.pojo.web.CurrencyConfig;
import com.blackbeard.common.service.ICurrencyConfigService;
import com.ssic.util.BeanUtils;
import com.ssic.util.constants.DataStatus;

@Service
public class CurrencyConfigServiceImpl implements ICurrencyConfigService {

	@Autowired
	private CurrencyConfigDao currencyConfigDao;

	@Override
	public void insert(CurrencyConfigDto dto) {
		CurrencyConfig currencyConfig = new CurrencyConfig();
		BeanUtils.copyProperties(dto, currencyConfig);
		currencyConfig.setStat(DataStatus.ENABLED);
		currencyConfig.setCreateTime(new Date());
		currencyConfig.setLastUpdateTime(new Date());

		currencyConfigDao.insert(currencyConfig);
	}

	@Override
	public void update(CurrencyConfigDto dto) {
		CurrencyConfig currencyConfig = new CurrencyConfig();
		BeanUtils.copyProperties(dto, currencyConfig);
		currencyConfig.setLastUpdateTime(new Date());
		currencyConfigDao.update(currencyConfig);
	}

	@Override
	public List<CurrencyConfigDto> findListByPage(
			CurrencyConfigDto currencyConfigDto, LimitPageDto limitPageDto) {
		CurrencyConfig currencyConfig = new CurrencyConfig();
		BeanUtils.copyProperties(currencyConfigDto, currencyConfig);
		List<CurrencyConfig> list = currencyConfigDao.findListByPage(
				currencyConfig, limitPageDto);
		List<CurrencyConfigDto> listDto = BeanUtils.createBeanListByTarget(
				list, CurrencyConfigDto.class);
		for (CurrencyConfigDto configDto : listDto) {
			String createTimeFormatString = DateUtil.formatDate(configDto
					.getCreateTime());
			configDto.setCreateTimeFormatString(createTimeFormatString);
		}
		return listDto;
	}

	@Override
	public int findCount() {
		return currencyConfigDao.findCount();
	}

	@Override
	public CurrencyConfigDto findById(String id) {
		CurrencyConfigDto dto = new CurrencyConfigDto();
		CurrencyConfig currencyConfig = currencyConfigDao.findById(id);
		if (currencyConfig != null) {
			dto = BeanUtils.createBeanByTarget(currencyConfig,
					CurrencyConfigDto.class);
		}
		return dto;
	}

	@Override
	public void delete(CurrencyConfigDto currencyConfigDto) {
		CurrencyConfig currencyConfig = new CurrencyConfig();
		BeanUtils.copyProperties(currencyConfigDto, currencyConfig);
		currencyConfig.setLastUpdateTime(new Date());
		currencyConfig.setStat(DataStatus.DISABLED);
		currencyConfigDao.update(currencyConfig);
	}

	@Override
	public List<CurrencyConfigDto> findBy(CurrencyConfigDto currencyConfigDto) {
		// TODO Auto-generated method stub
		return currencyConfigDao.findBy(currencyConfigDto);
	}

}
