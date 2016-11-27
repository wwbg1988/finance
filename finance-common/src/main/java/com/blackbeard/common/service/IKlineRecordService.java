package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.KlineRecordParam;



public interface IKlineRecordService {
	/**
	 * insert：插入k线
	 * 
	 * @param productColorDto
	 * @param minute
	 * @exception
	 * @author 刘博
	 * @date 2016年6月8日 下午1:27:36
	 */
	void insert(KlineRecordDto klineRecordDto,int minute);

	List<KlineRecordDto> findklineRecord(KlineRecordParam klineRecordParam);
}
