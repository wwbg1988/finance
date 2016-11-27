package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Mt4ConfigDto;

public interface IMt4ConfigService {
	/**
	 * 插入mt4服务器配置项
	 * 
	 * @param dto
	 */
	void insert(Mt4ConfigDto dto);

	/**
	 * 更新mt4服务器配置项
	 * 
	 * @param dto
	 */
	void update(Mt4ConfigDto dto);

	/**
	 * findListByPage：查找所有的MT4服务器配置项信息
	 * 
	 * @param mt4ConfigDto
	 * @param limitPageDto
	 * @return List<Mt4ConfigDto>
	 * @exception
	 * @author 刘博
	 * @date 2016年6月29日 下午3:09:22
	 */

	List<Mt4ConfigDto> findListByPage(Mt4ConfigDto mt4ConfigDto,
			LimitPageDto limitPageDto);

	/**
	 * 查找数量
	 * 
	 * @return int
	 */
	int findCount();

	/**
	 * 根据id查找对象
	 * 
	 * @return Mt4ConfigDto
	 */
	Mt4ConfigDto findById(String id);

	/**
	 * 根据id删除对象
	 * 
	 * @return Mt4ConfigDto
	 */
	void delete(Mt4ConfigDto mt4ConfigDto);
}
