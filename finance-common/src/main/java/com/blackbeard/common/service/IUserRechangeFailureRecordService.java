package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.Mt4ConfigDto;
import com.blackbeard.common.dto.UserRechangeFailureRecordDto;

/**
 * 用户充值失败记录Service层
 * 
 * @author 刘博
 *
 */
public interface IUserRechangeFailureRecordService {
	/**
	 * 插入用户充值失败记录
	 * 
	 * @param dto
	 */
	void insert(UserRechangeFailureRecordDto dto);

	/**
	 * 更新用户充值失败记录
	 * 
	 * @param dto
	 */
	void update(UserRechangeFailureRecordDto dto);

	/**
	 * findListByPage：查找所有的用户充值失败记录信息
	 * 
	 * @param userRechangeFailureRecordDto
	 * @param limitPageDto
	 * @return List<UserRechangeFailureRecordDto>
	 * @exception
	 * @author 刘博
	 * @date 2016年7月14日 下午3:09:22
	 */

	List<UserRechangeFailureRecordDto> findListByPage(
			UserRechangeFailureRecordDto userRechangeFailureRecordDto,
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
	 * @return UserRechangeFailureRecordDto
	 */
	UserRechangeFailureRecordDto findById(String id);

	/**
	 * 根据id删除对象
	 * 
	 * @return userRechangeFailureRecordDto
	 */
	void delete(UserRechangeFailureRecordDto userRechangeFailureRecordDto);
}
