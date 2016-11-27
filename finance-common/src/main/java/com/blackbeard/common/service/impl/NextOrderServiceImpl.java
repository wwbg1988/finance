package com.blackbeard.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.blackbeard.common.dao.order.NextOrderDao;
import com.blackbeard.common.dto.DateUtil;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.pojo.order.NextOrder;
import com.blackbeard.common.service.INextOrderService;
import com.blackbeard.common.service.UserService;
import com.ssic.util.BeanUtils;

@Service
public class NextOrderServiceImpl implements INextOrderService {

	@Autowired
	private NextOrderDao nextOrderDao;
	@Resource(name = "userService")
	private UserService userService;

	@Override
	public List<NextOrderDto> findBy(NextOrderDto nextOrderDto) {
		// TODO Auto-generated method stub
		return nextOrderDao.findBy(nextOrderDto);
	}

	@Override
	public void addNextOrder(NextOrderDto nextOrderDto) {
		// TODO Auto-generated method stub
		nextOrderDao.addNextOrder(nextOrderDto);
	}

	@Override
	public List<NextOrder> findList(List<Integer> userIdList,
			NextOrderDto nextOrderDto) {
		NextOrder nextOrder = new NextOrder();
		BeanUtils.copyProperties(nextOrderDto, nextOrder);
		return nextOrderDao.findList(userIdList, nextOrder);
	}

	@Override
	public int findCount(NextOrderDto nextOrderDto) {
		NextOrder nextOrder = new NextOrder();
		BeanUtils.copyProperties(nextOrderDto, nextOrder);
		return nextOrderDao.findCount(nextOrder);
	}

	@Override
	public List<NextOrderDto> findListByPage(NextOrderDto nextOrderDto,
			LimitPageDto limitPageDto, List<Integer> userIdList) {
		NextOrder nextOrder = new NextOrder();
		BeanUtils.copyProperties(nextOrderDto, nextOrder);
		PageData pds = new PageData();
		List<NextOrder> list = nextOrderDao.findListByPage(nextOrder,
				limitPageDto, userIdList);
		// 返回结果集合
		List<NextOrderDto> listDto = BeanUtils.createBeanListByTarget(list,
				NextOrderDto.class);
		// 循环历史订单集合,重新组装数据：开盘时间，结束时间，创建时间以及用户名
		for (NextOrderDto orderDto : listDto) {
			String[] array = StringUtils.split(orderDto.getComment(), ".");
			if (array != null && array.length > 1) {
				orderDto.setComment(array[1]);
			}
			String timeFormatString = DateUtil.longToDate(Long.valueOf(orderDto
					.getOpenTime()));
			orderDto.setTimeFormatString(timeFormatString);
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

	@Override
	public void reduceNextOrder(NextOrderDto nextOrderDto) {
		// TODO Auto-generated method stub
		nextOrderDao.reduceNextOrder(nextOrderDto);
	}

}
