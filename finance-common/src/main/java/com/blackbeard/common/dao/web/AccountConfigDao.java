package com.blackbeard.common.dao.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.mapper.web.AccountConfigMapper;
import com.blackbeard.common.pojo.web.AccountConfig;
import com.blackbeard.common.pojo.web.AccountConfigExample;
import com.blackbeard.common.pojo.web.AccountConfigExample.Criteria;
import com.ssic.util.constants.DataStatus;

@Repository
public class AccountConfigDao {
	@Autowired
	private AccountConfigMapper accountConfigMapper;

	public void insert(AccountConfig accountConfig) {
		accountConfigMapper.insert(accountConfig);
	}

	public void update(AccountConfig accountConfig) {
		accountConfigMapper.updateByPrimaryKeySelective(accountConfig);
	}

	public List<AccountConfig> findListByPage(AccountConfig accountConfig,
			LimitPageDto limitPageDto) {
		AccountConfigExample example = new AccountConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (accountConfig.getAccountPercent() != null) {// 百分比查询
			criteria.andAccountPercentEqualTo(accountConfig.getAccountPercent());
		}
		// 如果有分页对象;
		if (limitPageDto != null
				&& !StringUtils.isEmpty(limitPageDto.getStar())
				&& !StringUtils.isEmpty(limitPageDto.getEnd())) {
			example.setOrderByClause("create_time desc limit "
					+ limitPageDto.getStar() + "," + limitPageDto.getEnd());
		}
		return accountConfigMapper.selectByExample(example);

	}

	public int findCount() {
		AccountConfigExample example = new AccountConfigExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		int count = accountConfigMapper.countByExample(example);
		return count;
	}

	public AccountConfig findById(String id) {
		return accountConfigMapper.selectByPrimaryKey(id);
	}
}
