package com.blackbeard.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.dao.web.Mt4ConfigDao;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Mt4ConfigDto;
import com.blackbeard.common.pojo.web.Mt4Config;
import com.blackbeard.common.service.IMt4ConfigService;
import com.ssic.util.BeanUtils;
import com.ssic.util.constants.DataStatus;

@Service
public class Mt4ConfigServiceImpl implements IMt4ConfigService {
	@Autowired
	private Mt4ConfigDao mt4ConfigDao;

	@Override
	public void insert(Mt4ConfigDto dto) {
		Mt4Config mt4Config = new Mt4Config();
		BeanUtils.copyProperties(dto, mt4Config);
		mt4Config.setStat(DataStatus.ENABLED);
		mt4Config.setCreateTime(new Date());
		mt4Config.setLastUpdateTime(new Date());
		mt4ConfigDao.insert(mt4Config);
	}

	@Override
	public void update(Mt4ConfigDto dto) {
		Mt4Config mt4Config = new Mt4Config();
		BeanUtils.copyProperties(dto, mt4Config);
		mt4Config.setLastUpdateTime(new Date());
		mt4ConfigDao.update(mt4Config);
	}

	@Override
	public List<Mt4ConfigDto> findListByPage(Mt4ConfigDto mt4ConfigDto,
			LimitPageDto limitPageDto) {
		Mt4Config mt4Config = new Mt4Config();
		BeanUtils.copyProperties(mt4ConfigDto, mt4Config);
		List<Mt4Config> list = mt4ConfigDao.findListByPage(mt4Config,
				limitPageDto);
		List<Mt4ConfigDto> listDto = BeanUtils.createBeanListByTarget(list,
				Mt4ConfigDto.class);
		return listDto;
	}

	@Override
	public int findCount() {
		return mt4ConfigDao.findCount();
	}

	@Override
	public Mt4ConfigDto findById(String id) {
		Mt4ConfigDto dto = new Mt4ConfigDto();
		Mt4Config mt4Config = mt4ConfigDao.findById(id);
		if (mt4Config != null) {
			dto = BeanUtils.createBeanByTarget(mt4Config, Mt4ConfigDto.class);
		}
		return dto;
	}

	@Override
	public void delete(Mt4ConfigDto mt4ConfigDto) {
		Mt4Config mt4Config = new Mt4Config();
		BeanUtils.copyProperties(mt4ConfigDto, mt4Config);
		mt4Config.setLastUpdateTime(new Date());
		mt4ConfigDao.update(mt4Config);
		
	}

}
