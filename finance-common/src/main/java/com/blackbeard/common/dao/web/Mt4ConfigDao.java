package com.blackbeard.common.dao.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.mapper.web.Mt4ConfigMapper;
import com.blackbeard.common.pojo.web.Mt4Config;
import com.blackbeard.common.pojo.web.Mt4ConfigExample;
import com.blackbeard.common.pojo.web.UserInfoExample;
import com.blackbeard.common.pojo.web.Mt4ConfigExample.Criteria;
import com.ssic.util.constants.DataStatus;

@Repository
public class Mt4ConfigDao {
	@Autowired
	private Mt4ConfigMapper mt4ConfigMapper;

	public void insert(Mt4Config mt4Config) {
		mt4ConfigMapper.insert(mt4Config);
	}

	public void update(Mt4Config mt4Config) {
		mt4ConfigMapper.updateByPrimaryKeySelective(mt4Config);
	}

	public List<Mt4Config> findListByPage(Mt4Config mt4Config,
			LimitPageDto limitPageDto) {
		Mt4ConfigExample example = new Mt4ConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");

		if (!StringUtils.isEmpty(mt4Config.getServerIp())) {
			criteria.andServerIpEqualTo(mt4Config.getServerIp());
		}
		if (!StringUtils.isEmpty(mt4Config.getServerPort())) {
			criteria.andServerPortEqualTo(mt4Config.getServerPort());
		}
		// 如果有分页对象;
		if (limitPageDto != null
				&& !StringUtils.isEmpty(limitPageDto.getStar())
				&& !StringUtils.isEmpty(limitPageDto.getEnd())) {
			example.setOrderByClause("create_time desc limit "
					+ limitPageDto.getStar() + "," + limitPageDto.getEnd());
		}
		return mt4ConfigMapper.selectByExample(example);

	}

	public int findCount() {
		Mt4ConfigExample example = new Mt4ConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		int count = mt4ConfigMapper.countByExample(example);
		return count;
	}

	public Mt4Config findById(String id) {
		return mt4ConfigMapper.selectByPrimaryKey(id);
	}

}
