package com.blackbeard.common.dao.order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.OrderChartDto;
import com.blackbeard.common.dto.OrderParam;
import com.blackbeard.common.mapper.order.HistoryOrderExMapper;
import com.blackbeard.common.mapper.order.HistoryOrderMapper;
import com.blackbeard.common.pojo.order.HistoryOrder;
import com.blackbeard.common.pojo.order.HistoryOrderExample;
import com.blackbeard.common.pojo.order.HistoryOrderExample.Criteria;
import com.ssic.util.DateUtils;
import com.ssic.util.constants.DataStatus;

@Repository
public class HistoryOrderDao {

	@Autowired
	private HistoryOrderExMapper exmapper;

	@Autowired
	private HistoryOrderMapper mapper;

	public List<HistoryOrderDto> findBy(HistoryOrderDto historyOrderDto) {
		return exmapper.findBy(historyOrderDto);
	}

	public void addHistoryOrder(HistoryOrderDto historyOrderDto) {
		exmapper.addHistoryOrder(historyOrderDto);
	}

	public int findCount(HistoryOrder historyOrder) {
		HistoryOrderExample example = new HistoryOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		if (!StringUtils.isEmpty(historyOrder.getSymbol())) {// 货币
			criteria.andSymbolEqualTo(historyOrder.getSymbol());
		}
		if (null != historyOrder.getCmd()) {// 买卖
			criteria.andCmdEqualTo(historyOrder.getCmd());
		}
		if (null != historyOrder.getTick() && 0 != historyOrder.getTick()) {// 订单号
			criteria.andTickEqualTo(historyOrder.getTick());
		}
		if (null != historyOrder.getUserId()) {// 用户id
			criteria.andUserIdEqualTo(historyOrder.getUserId());
		}
		if (!StringUtils.isEmpty(historyOrder.getOpenTimeStart())) {
			criteria.andOpenTimeGreaterThanOrEqualTo(historyOrder
					.getOpenTimeStart());
		}
		if (!StringUtils.isEmpty(historyOrder.getOpenTimeEnd())) {
			criteria.andOpenTimeLessThanOrEqualTo(historyOrder.getOpenTimeEnd());
		}
		int count = mapper.countByExample(example);
		return count;
	}

	public List<HistoryOrder> findListByPage(HistoryOrder historyOrder,
			LimitPageDto limitPageDto, List<Integer> userIdList) {
		HistoryOrderExample example = new HistoryOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!CollectionUtils.isEmpty(userIdList)) {
			criteria.andUserIdIn(userIdList);
		}
		if (!StringUtils.isEmpty(historyOrder.getSymbol())) {// 货币
			criteria.andSymbolEqualTo(historyOrder.getSymbol());
		}
		if (null != historyOrder.getCmd()) {// 买卖
			criteria.andCmdEqualTo(historyOrder.getCmd());
		}
		if (null != historyOrder.getTick() && 0 != historyOrder.getTick()) {// 订单号
			criteria.andTickEqualTo(historyOrder.getTick());
		}
		if (null != historyOrder.getUserId()) {// 用户id
			criteria.andUserIdEqualTo(historyOrder.getUserId());
		}
		if (!StringUtils.isEmpty(historyOrder.getOpenTimeStart())) {
			criteria.andOpenTimeGreaterThanOrEqualTo(historyOrder
					.getOpenTimeStart());
		}
		if (!StringUtils.isEmpty(historyOrder.getOpenTimeEnd())) {
			criteria.andOpenTimeLessThanOrEqualTo(historyOrder.getOpenTimeEnd());
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

	public List<HistoryOrder> findList(List<Integer> userIdList,
			HistoryOrder historyOrder) {
		HistoryOrderExample example = new HistoryOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		example.setOrderByClause("create_time desc");
		if (!CollectionUtils.isEmpty(userIdList)) {
			criteria.andUserIdIn(userIdList);
		}
		if (!CollectionUtils.isEmpty(userIdList)) {
			criteria.andUserIdIn(userIdList);
		}
		if (!StringUtils.isEmpty(historyOrder.getSymbol())) {// 货币
			criteria.andSymbolEqualTo(historyOrder.getSymbol());
		}
		if (null != historyOrder.getCmd()) {// 买卖
			criteria.andCmdEqualTo(historyOrder.getCmd());
		}
		if (null != historyOrder.getTick() && 0 != historyOrder.getTick()) {// 订单号
			criteria.andTickEqualTo(historyOrder.getTick());
		}
		if (null != historyOrder.getUserId()) {// 用户id
			criteria.andUserIdEqualTo(historyOrder.getUserId());
		}
		if (!StringUtils.isEmpty(historyOrder.getOpenTimeStart())) {
			criteria.andOpenTimeGreaterThanOrEqualTo(historyOrder
					.getOpenTimeStart());
		}
		if (!StringUtils.isEmpty(historyOrder.getOpenTimeEnd())) {
			criteria.andOpenTimeLessThanOrEqualTo(historyOrder.getOpenTimeEnd());
		}
		return mapper.selectByExample(example);
	}

	public List<HistoryOrder> findOrderList(OrderParam orderParam) {
		HistoryOrderExample example = new HistoryOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatEqualTo(DataStatus.ENABLED);
		if (!StringUtils.isEmpty(orderParam.getbTime())) {
			Date beginDate = new Date(Long.valueOf(orderParam.getbTime()));
			criteria.andCreateTimeGreaterThanOrEqualTo(beginDate);
		}
		if (!StringUtils.isEmpty(orderParam.geteTime())) {
			Date endDate = new Date(Long.valueOf(orderParam.geteTime()));
			criteria.andCreateTimeLessThanOrEqualTo(endDate);
		}
		example.setOrderByClause("create_time desc");
		if (!StringUtils.isEmpty(orderParam.getUserId())) {
			criteria.andUserIdEqualTo(Integer.valueOf(orderParam.getUserId()));
		}
		if (null != orderParam.getOrderNo()) {
			criteria.andTickEqualTo(orderParam.getOrderNo());
		}

		return mapper.selectByExample(example);
	}

	public List<OrderChartDto> findChart(HistoryOrderDto historyOrderDto) {
		return exmapper.findChart(historyOrderDto);
	}
}
