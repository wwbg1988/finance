package com.blackbeard.common.dao.order;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.mapper.order.NextOrderExMapper;
import com.blackbeard.common.mapper.order.NextOrderMapper;
import com.blackbeard.common.pojo.order.NextOrderExample.Criteria;
import com.blackbeard.common.pojo.order.NextOrder;
import com.blackbeard.common.pojo.order.NextOrderExample;
import com.ssic.util.constants.DataStatus;

@Repository
public class NextOrderDao {

	@Autowired
	private NextOrderExMapper exMapper;
	@Autowired
	private NextOrderMapper mapper;

	public List<NextOrderDto> findBy(NextOrderDto nextOrderDto) {
		return exMapper.findBy(nextOrderDto);
	}

	public void addNextOrder(NextOrderDto nextOrderDto) {
		exMapper.addNextOrder(nextOrderDto);
	}

	public List<NextOrder> findList(List<Integer> userIdList,
			NextOrder nextOrder) {
		NextOrderExample example = new NextOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!CollectionUtils.isEmpty(userIdList)) {
			criteria.andUserIdIn(userIdList);
		}
		if (!CollectionUtils.isEmpty(userIdList)) {
			criteria.andUserIdIn(userIdList);
		}
		if (!StringUtils.isEmpty(nextOrder.getCurrencyType())) {// 货币
			criteria.andCurrencyTypeEqualTo(nextOrder.getCurrencyType());
		}
		if (null != nextOrder.getDirection()) {// 买卖
			criteria.andDirectionEqualTo(nextOrder.getDirection());
		}
		if (null != nextOrder.getTick() && 0 != nextOrder.getTick()) {// 订单号
			criteria.andTickEqualTo(nextOrder.getTick());
		}
		if (null != nextOrder.getUserId()) {// 用户id
			criteria.andUserIdEqualTo(nextOrder.getUserId());
		}
		return mapper.selectByExample(example);
	}

	public int findCount(NextOrder nextOrder) {
		NextOrderExample example = new NextOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");

		if (!StringUtils.isEmpty(nextOrder.getCurrencyType())) {// 货币
			criteria.andCurrencyTypeEqualTo(nextOrder.getCurrencyType());
		}
		if (null != nextOrder.getDirection()) {// 买卖
			criteria.andDirectionEqualTo(nextOrder.getDirection());
		}
		if (null != nextOrder.getTick() && 0 != nextOrder.getTick()) {// 订单号
			criteria.andTickEqualTo(nextOrder.getTick());
		}
		if (null != nextOrder.getUserId()) {// 用户id
			criteria.andUserIdEqualTo(nextOrder.getUserId());
		}
		int count = mapper.countByExample(example);
		return count;
	}

	public List<NextOrder> findListByPage(NextOrder nextOrder,
			LimitPageDto limitPageDto, List<Integer> userIdList) {
		NextOrderExample example = new NextOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!CollectionUtils.isEmpty(userIdList)) {
			criteria.andUserIdIn(userIdList);
		}
		if (!StringUtils.isEmpty(nextOrder.getCurrencyType())) {// 货币
			criteria.andCurrencyTypeEqualTo(nextOrder.getCurrencyType());
		}
		if (null != nextOrder.getDirection()) {// 买卖
			criteria.andDirectionEqualTo(nextOrder.getDirection());
		}
		if (null != nextOrder.getTick() && 0 != nextOrder.getTick()) {// 订单号
			criteria.andTickEqualTo(nextOrder.getTick());
		}
		if (null != nextOrder.getUserId()) {// 用户id
			criteria.andUserIdEqualTo(nextOrder.getUserId());
		}
		// 如果有分页对象;
		if (limitPageDto != null
				&& !StringUtils.isEmpty(limitPageDto.getStar())
				&& !StringUtils.isEmpty(limitPageDto.getEnd())) {
			example.setOrderByClause("create_time desc limit "
					+ limitPageDto.getStar() + "," + limitPageDto.getEnd());
		}
		return mapper.selectByExample(example);
	}

	public void reduceNextOrder(NextOrderDto nextOrderDto){
		exMapper.reduceNextOrder(nextOrderDto);
	}
	
}
