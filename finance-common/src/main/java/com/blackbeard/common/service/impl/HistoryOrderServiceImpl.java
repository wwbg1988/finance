package com.blackbeard.common.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackbeard.common.constant.WebConstants;
import com.blackbeard.common.dao.order.HistoryOrderDao;
import com.blackbeard.common.dto.AccountResultDto;
import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.OrderChartDto;
import com.blackbeard.common.dto.OrderParam;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.pojo.order.HistoryOrder;
import com.blackbeard.common.service.IHistoryOrderService;
import com.blackbeard.common.service.UserService;
import com.ssic.util.BeanUtils;
import com.ssic.util.StringUtils;

@Service
public class HistoryOrderServiceImpl implements IHistoryOrderService {

	@Autowired
	private HistoryOrderDao historyOrderDao;
	@Resource(name = "userService")
	private UserService userService;

	@Override
	public List<HistoryOrderDto> findBy(HistoryOrderDto historyOrderDto) {
		// TODO Auto-generated method stub
		return historyOrderDao.findBy(historyOrderDto);
	}

	@Override
	public void addHistoryOrder(HistoryOrderDto historyOrderDto) {
		// TODO Auto-generated method stub
		historyOrderDao.addHistoryOrder(historyOrderDto);
	}

	@Override
	public int findCount(HistoryOrderDto historyOrderDto) {
		HistoryOrder historyOrder = new HistoryOrder();
		BeanUtils.copyProperties(historyOrderDto, historyOrder);
		return historyOrderDao.findCount(historyOrder);
	}

	@Override
	public List<HistoryOrderDto> findListByPage(
			HistoryOrderDto historyOrderDto, LimitPageDto limitPageDto,
			List<Integer> userIdList) {
		HistoryOrder historyOrder = new HistoryOrder();
		BeanUtils.copyProperties(historyOrderDto, historyOrder);
		List<HistoryOrder> list = historyOrderDao.findListByPage(historyOrder,
				limitPageDto, userIdList);
		// 返回结果集合
		List<HistoryOrderDto> listDto = BeanUtils.createBeanListByTarget(list,
				HistoryOrderDto.class);
		// 定义总的交易数量,前台展示用
		long totalCount = 0;
		// 循环历史订单集合,重新组装数据：开盘时间，结束时间，创建时间以及用户名
		for (HistoryOrderDto orderDto : listDto) {
			String openTimeFormatString = DateUtil.longToDate(Long
					.valueOf(orderDto.getOpenTime()));
			String closeTimeFormatString = DateUtil.longToDate(Long
					.valueOf(orderDto.getCloseTime()));
			orderDto.setOpenTimeFormatString(openTimeFormatString);
			orderDto.setCloseTimeFormatString(closeTimeFormatString);
			PageData pd = new PageData();
			pd.put("USER_ID", orderDto.getUserId());
			try {
				pd = userService.findByUiId(pd);
				if (pd != null && pd.containsKey("NAME")) {
					orderDto.setUserName(pd.getString("NAME"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return listDto;
	}

	public List<HistoryOrder> findList(List<Integer> userIdList,
			HistoryOrderDto historyOrderDto) {
		HistoryOrder historyOrder = new HistoryOrder();
		BeanUtils.copyProperties(historyOrderDto, historyOrder);
		return historyOrderDao.findList(userIdList, historyOrder);
	}

	@Override
	public List<HistoryOrderDto> findOrderList(OrderParam orderParam) {
		List<HistoryOrder> list = historyOrderDao.findOrderList(orderParam);
		// 返回结果集合
		List<HistoryOrderDto> listDto = BeanUtils.createBeanListByTarget(list,
				HistoryOrderDto.class);
		return listDto;
	}

	@Override
	public AccountResultDto findAccountResult(HistoryOrderDto historyOrderDto) {
		AccountResultDto accountResultDto = new AccountResultDto();
		HistoryOrder historyOrder = new HistoryOrder();
		BeanUtils.copyProperties(historyOrderDto, historyOrder);
		// 查询所有的历史订单
		List<HistoryOrder> list = historyOrderDao.findList(null, historyOrder);
		int total = 0;
		int index = 0;
		for (HistoryOrder order : list) {// 遍历，取profit(盈亏不为0的订单)
			if (!StringUtils.isEmpty(historyOrderDto.getEveningUp())
					&& historyOrderDto.getEveningUp().equals("1")) {// 1是包含平仓,去所有历史订单量
				// 把每个对象的交易量放入total对象中;
				total += order.getTotal();
				// 每进来一次，订单个数+1
				index++;
			} else {// 0不包含平仓
				if (order.getProfit() != 0) {
					total += order.getTotal();
					index++;
				}
			}
		}
		accountResultDto.setAccountSize(index);
		accountResultDto.setAccountCount(total);
		return accountResultDto;
	}

	@Override
	public void inserByNextOrderDto(NextOrderDto orderDto) {
		HistoryOrderDto historyOrderDto = new HistoryOrderDto();
		// k线对象获取closePrice
		historyOrderDto.setOpenPrice(orderDto.getPrice());
		historyOrderDto.setClosePrice(orderDto.getClosePrice());
		historyOrderDto.setOpenTime(orderDto.getOpenTime());
		historyOrderDto.setCloseTime(orderDto.getCloseTime());
		historyOrderDto.setCreateTime(new Date());
		historyOrderDto.setLoginId(orderDto.getLoginId());
		historyOrderDto.setCmd(orderDto.getDirection());
		historyOrderDto.setProfit(orderDto.getProfit());
		historyOrderDto.setSymbol(orderDto.getCurrencyType());
		historyOrderDto.setTick(orderDto.getTick());
		historyOrderDto.setTotal(orderDto.getTotal());
		addHistoryOrder(historyOrderDto);
	}

	@Override
	public String findChartByPage(HistoryOrderDto historyOrderDto) {
		// 查询所有的历史订单
		List<OrderChartDto> list = historyOrderDao.findChart(historyOrderDto);
		String strXML = "";
		strXML += "<graph caption='货币成交量统计' xAxisName='货币名称' yAxisName='数量' decimalPrecision='0' formatNumberScale='0'>";
		if (!CollectionUtils.isEmpty(list)) {
			for (OrderChartDto chartDto : list) {
				int index = (int) (Math.random() * WebConstants.colorArr.length);
				strXML += "<set name='" + chartDto.getName() + "' value='"
						+ chartDto.getValue() + "' color='"
						+ WebConstants.colorArr[index] + "'/>";
			}
		}
		strXML += "</graph>";
		return strXML;
	}
}
