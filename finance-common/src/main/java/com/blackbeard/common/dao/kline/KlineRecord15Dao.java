package com.blackbeard.common.dao.kline;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.KlineRecordParam;
import com.blackbeard.common.mapper.kline.KlineRecord15Mapper;
import com.blackbeard.common.pojo.kline.KlineRecord15;
import com.blackbeard.common.pojo.kline.KlineRecord15Example;
import com.blackbeard.common.pojo.kline.KlineRecord15Example.Criteria;
import com.ssic.util.BeanUtils;
import com.ssic.util.StringUtils;
import com.ssic.util.constants.DataStatus;

@Repository
public class KlineRecord15Dao {
	@Autowired
	private KlineRecord15Mapper klineRecord15Mapper;

	public List<KlineRecordDto> findklineRecord(
			KlineRecordParam klineRecordParam) {
		Long beginDate = Long.valueOf(klineRecordParam.getbTime());
		Long endDate = Long.valueOf(klineRecordParam.geteTime());
		KlineRecord15Example example = new KlineRecord15Example();
		Criteria criteria = example.createCriteria();
		example.setOrderByClause("time_stamp desc,currency");
		criteria.andStatEqualTo(DataStatus.ENABLED);
		if (klineRecordParam.getAll() == 0
				&& !StringUtils.isEmpty(klineRecordParam.getCury())) {
			criteria.andCurrencyEqualTo(klineRecordParam.getCury());
		}
		criteria.andTimeStampGreaterThanOrEqualTo(beginDate);
		criteria.andTimeStampLessThanOrEqualTo(endDate);
		List<KlineRecord15> list = klineRecord15Mapper.selectByExample(example);
		List<KlineRecordDto> dtolist = BeanUtils.createBeanListByTarget(list,
				KlineRecordDto.class);
		return dtolist;
	}
}
