package com.blackbeard.common.dao.web;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.mapper.web.UserRechangeFailureRecordMapper;
import com.blackbeard.common.pojo.web.UserRechangeFailureRecordExample.Criteria;
import com.blackbeard.common.pojo.web.UserRechangeFailureRecord;
import com.blackbeard.common.pojo.web.UserRechangeFailureRecordExample;
import com.ssic.util.constants.DataStatus;

/**
 * 用户充值失败记录Dao
 * 
 * @author 刘博
 *
 */
@Repository
public class UserRechangeFailureRecordDao {

	@Autowired
	private UserRechangeFailureRecordMapper recordMapper;

	/**
	 * 插入用户充值失败记录
	 * 
	 * @param userRechangeFailureRecord
	 */
	public void insert(UserRechangeFailureRecord userRechangeFailureRecord) {
		recordMapper.insert(userRechangeFailureRecord);
	}

	/**
	 * 更新用户充值失败记录
	 * 
	 * @param userRechangeFailureRecord
	 */
	public void update(UserRechangeFailureRecord userRechangeFailureRecord) {
		recordMapper.updateByPrimaryKeySelective(userRechangeFailureRecord);
	}

	/**
	 * 查询所有用户充值失败记录
	 * 
	 * @param limitPageDto
	 *            分页对象
	 * @param userRechangeFailureRecord
	 *            查询条件参数
	 * @return List<UserRechangeFailureRecord> 返回对象集合
	 */
	public List<UserRechangeFailureRecord> findListByPage(
			UserRechangeFailureRecord failureRecord, LimitPageDto limitPageDto) {
		UserRechangeFailureRecordExample example = new UserRechangeFailureRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");

		if (!StringUtils.isEmpty(failureRecord.getId())) {
			criteria.andIdEqualTo(failureRecord.getId());
		}
		if (!StringUtils.isEmpty(failureRecord.getUserId())) {
			criteria.andUserIdEqualTo(failureRecord.getUserId());
		}
		if (!StringUtils.isEmpty(failureRecord.getMt4Id())) {
			criteria.andMt4IdEqualTo(failureRecord.getMt4Id());
		}
		// 如果有分页对象;
		if (limitPageDto != null
				&& !StringUtils.isEmpty(limitPageDto.getStar())
				&& !StringUtils.isEmpty(limitPageDto.getEnd())) {
			example.setOrderByClause("create_time desc limit "
					+ limitPageDto.getStar() + "," + limitPageDto.getEnd());
		}
		return recordMapper.selectByExample(example);

	}

	/**
	 * 查找数量
	 * 
	 * @return int
	 */
	public int findCount() {
		UserRechangeFailureRecordExample example = new UserRechangeFailureRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		int count = recordMapper.countByExample(example);
		return count;
	}

	/**
	 * 查找数量
	 * 
	 * @param id
	 *            主键id
	 * @return UserRechangeFailureRecord
	 */
	public UserRechangeFailureRecord findById(String id) {
		return recordMapper.selectByPrimaryKey(id);
	}

}
