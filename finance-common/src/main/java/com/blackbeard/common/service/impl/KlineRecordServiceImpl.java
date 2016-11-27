package com.blackbeard.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dao.kline.KlineRecord15Dao;
import com.blackbeard.common.dao.kline.KlineRecord5Dao;
import com.blackbeard.common.dao.kline.KlineRecordDao;
import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.KlineRecordParam;
import com.blackbeard.common.pojo.kline.KlineRecord;
import com.blackbeard.common.service.IKlineRecordService;
import com.ssic.util.constants.DataStatus;

@Service
public class KlineRecordServiceImpl implements IKlineRecordService {
	@Autowired
	private KlineRecordDao klineRecordDao;
	@Autowired
	private KlineRecord5Dao klineRecord5Dao;
	@Autowired
	private KlineRecord15Dao klineRecord15Dao;

	@Override
	public void insert(KlineRecordDto dto, int minute) {
		KlineRecord klineRecord = new KlineRecord();
		BeanUtils.copyProperties(dto, klineRecord);
		klineRecord.setId(UUID.randomUUID().toString());
		klineRecord.setCreateTime(new Date());
		klineRecord.setLastUpdateTime(new Date());
		klineRecord.setStat(DataStatus.ENABLED);
		klineRecordDao.insertKline(klineRecord, minute);
	}

	@Override
	public List<KlineRecordDto> findklineRecord(
			KlineRecordParam klineRecordParam) {
		List<KlineRecordDto> list = new ArrayList<KlineRecordDto>();
		switch (klineRecordParam.getType()) {
		case KlineConstants.KLINE_ONE:
			list = klineRecordDao.findklineRecord(klineRecordParam);
			break;
		case KlineConstants.KLINE_FIVE:
			list = klineRecord5Dao.findklineRecord(klineRecordParam);
			break;
		case KlineConstants.KLINE_FIFTEEN:
			list = klineRecord15Dao.findklineRecord(klineRecordParam);
			break;
		}
		return list;
	}
}
