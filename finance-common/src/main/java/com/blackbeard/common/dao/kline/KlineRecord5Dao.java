package com.blackbeard.common.dao.kline;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.KlineRecordParam;
import com.blackbeard.common.mapper.kline.KlineRecord5Mapper;
import com.blackbeard.common.pojo.kline.KlineRecord5;
import com.blackbeard.common.pojo.kline.KlineRecord5Example;
import com.blackbeard.common.pojo.kline.KlineRecord5Example.Criteria;
import com.ssic.util.BeanUtils;
import com.ssic.util.StringUtils;
import com.ssic.util.constants.DataStatus;

@Repository
public class KlineRecord5Dao {
	@Autowired
	private KlineRecord5Mapper klineRecord5Mapper;

	public List<KlineRecordDto> findklineRecord(
			KlineRecordParam klineRecordParam) {
		Long beginDate = Long.valueOf(klineRecordParam.getbTime());
		Long endDate = Long.valueOf(klineRecordParam.geteTime());
		KlineRecord5Example example = new KlineRecord5Example();
		Criteria criteria = example.createCriteria();
		example.setOrderByClause("time_stamp desc,currency");
		criteria.andStatEqualTo(DataStatus.ENABLED);
		if (klineRecordParam.getAll() == 0
				&& !StringUtils.isEmpty(klineRecordParam.getCury())) {
			criteria.andCurrencyEqualTo(klineRecordParam.getCury());
		}
		criteria.andTimeStampGreaterThanOrEqualTo(beginDate);
		criteria.andTimeStampLessThanOrEqualTo(endDate);
		List<KlineRecord5> list = klineRecord5Mapper.selectByExample(example);
		List<KlineRecordDto> dtolist = BeanUtils.createBeanListByTarget(list,
				KlineRecordDto.class);
		return dtolist;
	}
}