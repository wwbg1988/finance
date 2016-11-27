package com.blackbeard.common.dao.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.mapper.web.CurrencyConfigExMapper;
import com.blackbeard.common.mapper.web.CurrencyConfigMapper;
import com.blackbeard.common.pojo.web.CurrencyConfig;
import com.blackbeard.common.pojo.web.CurrencyConfigExample;
import com.blackbeard.common.pojo.web.CurrencyConfigExample.Criteria;
import com.ssic.util.constants.DataStatus;

/**
 * 货币配置项Dao层
 * 
 * @author 刘博
 *
 */
@Repository
public class CurrencyConfigDao {
	@Autowired
	private CurrencyConfigMapper currencyConfigMapper;
	@Autowired
	private CurrencyConfigExMapper exmapper;


	public void insert(CurrencyConfig currencyConfig) {
		currencyConfigMapper.insert(currencyConfig);
	}

	public void update(CurrencyConfig currencyConfig) {
		currencyConfigMapper.updateByPrimaryKeySelective(currencyConfig);
	}

	public List<CurrencyConfig> findListByPage(CurrencyConfig currencyConfig,
			LimitPageDto limitPageDto) {
		CurrencyConfigExample example = new CurrencyConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!StringUtils.isEmpty(currencyConfig.getCurrency())) {// 货币名称
			criteria.andCurrencyEqualTo(currencyConfig.getCurrency());
		}
		// 如果有分页对象;
		if (limitPageDto != null
				&& !StringUtils.isEmpty(limitPageDto.getStar())
				&& !StringUtils.isEmpty(limitPageDto.getEnd())) {
			example.setOrderByClause("create_time desc limit "
					+ limitPageDto.getStar() + "," + limitPageDto.getEnd());
		}
		return currencyConfigMapper.selectByExample(example);

	}

	public int findCount() {
		CurrencyConfigExample example = new CurrencyConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		int count = currencyConfigMapper.countByExample(example);
		return count;
	}

	public CurrencyConfig findById(String id) {
		return currencyConfigMapper.selectByPrimaryKey(id);
	}
	
	public List<CurrencyConfigDto> findBy(CurrencyConfigDto currencyConfigDto) {
		return exmapper.findBy(currencyConfigDto);
	}
}
