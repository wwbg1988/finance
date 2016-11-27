package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.AccountConfigDto;
/**
 * 结算配置项Service层
 * @author 刘博
 *
 */
public interface IAccountConfigService {
	/**
	 * 插入结算配置项
	 * 
	 * @param dto
	 */
	void insert(AccountConfigDto dto);

	/**
	 * 更新结算配置项
	 * 
	 * @param dto
	 */
	void update(AccountConfigDto dto);

	/**
	 * findListByPage：查找所有的结算配置项信息
	 * 
	 * @param accountConfigDto
	 * @param limitPageDto
	 * @return List<AccountConfigDto>
	 * @exception
	 * @author 刘博
	 * @date 2016年6月29日 下午3:09:22
	 */

	List<AccountConfigDto> findListByPage(AccountConfigDto accountConfigDto,
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
	 * @return AccountConfigDto
	 */
	AccountConfigDto findById(String id);

	/**
	 * 根据id删除对象
	 * 
	 * @return AccountConfigDto
	 */
	void delete(AccountConfigDto accountConfigDto);
}
