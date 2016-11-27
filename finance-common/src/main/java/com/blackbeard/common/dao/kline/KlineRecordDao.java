package com.blackbeard.common.dao.kline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.KlineRecordParam;
import com.blackbeard.common.mapper.kline.KlineRecord15Mapper;
import com.blackbeard.common.mapper.kline.KlineRecord5Mapper;
import com.blackbeard.common.mapper.kline.KlineRecordMapper;
import com.blackbeard.common.pojo.kline.KlineRecord;
import com.blackbeard.common.pojo.kline.KlineRecord15;
import com.blackbeard.common.pojo.kline.KlineRecord5;
import com.blackbeard.common.pojo.kline.KlineRecordExample;
import com.blackbeard.common.pojo.kline.KlineRecordExample.Criteria;
import com.ssic.util.BeanUtils;
import com.ssic.util.StringUtils;
import com.ssic.util.constants.DataStatus;

@Repository
public class KlineRecordDao {
	@Autowired
	private KlineRecordMapper klineRecordMapper;
	@Autowired
	private KlineRecord5Mapper klineRecord5Mapper;
	@Autowired
	private KlineRecord15Mapper klineRecord15Mapper;

	public void insertKline(KlineRecord klineRecord, int minute) {
		switch (minute) {
		case KlineConstants.KLINE_ONE:
			klineRecordMapper.insert(klineRecord);
			break;
		case KlineConstants.KLINE_FIVE:
			KlineRecord5 klineRecord5 = new KlineRecord5();
			BeanUtils.copyProperties(klineRecord, klineRecord5);
			klineRecord5Mapper.insert(klineRecord5);
			break;
		case KlineConstants.KLINE_FIFTEEN:
			KlineRecord15 klineRecord15 = new KlineRecord15();
			BeanUtils.copyProperties(klineRecord, klineRecord15);
			klineRecord15Mapper.insert(klineRecord15);
			break;
		}

	}

	public List<KlineRecordDto> findklineRecord(
			KlineRecordParam klineRecordParam) {
		Long beginDate = Long.valueOf(klineRecordParam.getbTime());
		Long endDate = Long.valueOf(klineRecordParam.geteTime());
		KlineRecordExample example = new KlineRecordExample();
		Criteria criteria = example.createCriteria();
		example.setOrderByClause("time_stamp desc,currency");
		criteria.andStatEqualTo(DataStatus.ENABLED);
		if (klineRecordParam.getAll() == 0
				&& !StringUtils.isEmpty(klineRecordParam.getCury())) {
			criteria.andCurrencyEqualTo(klineRecordParam.getCury());
		}
		if (!CollectionUtils.isEmpty(klineRecordParam.getCurrencyNameList())) {
			criteria.andCurrencyIn(klineRecordParam.getCurrencyNameList());
		}
		criteria.andTimeStampGreaterThanOrEqualTo(beginDate);
		criteria.andTimeStampLessThanOrEqualTo(endDate);
		List<KlineRecord> list = klineRecordMapper.selectByExample(example);
		List<KlineRecordDto> dtolist = BeanUtils.createBeanListByTarget(list,
				KlineRecordDto.class);
		return dtolist;
	}
}
