package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.LimitPageDto;

/**
 * 货币配置项Service层
 * 
 * @author 刘博
 *
 */
public interface ICurrencyConfigService {
	/**
	 * 插入结算配置项
	 * 
	 * @param dto
	 */
	void insert(CurrencyConfigDto dto);

	/**
	 * 更新结算配置项
	 * 
	 * @param dto
	 */
	void update(CurrencyConfigDto dto);

	/**
	 * findListByPage：查找所有的结算配置项信息
	 * 
	 * @param currencyConfigDto
	 * @param limitPageDto
	 * @return List<CurrencyConfigDto>
	 * @exception
	 * @author 刘博
	 * @date 2016年8月02日 下午3:09:22
	 */

	List<CurrencyConfigDto> findListByPage(CurrencyConfigDto currencyConfigDto,
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
	 * @return CurrencyConfigDto
	 */
	CurrencyConfigDto findById(String id);

	/**
	 * 根据id删除对象
	 * 
	 * @return currencyConfigDto
	 */
	void delete(CurrencyConfigDto currencyConfigDto);
	
	public List<CurrencyConfigDto> findBy(CurrencyConfigDto currencyConfigDto);
}
