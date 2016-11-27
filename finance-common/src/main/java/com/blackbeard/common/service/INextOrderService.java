package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.pojo.order.NextOrder;

public interface INextOrderService {

	public List<NextOrderDto>  findBy(NextOrderDto nextOrderDto);
	
	public void addNextOrder(NextOrderDto nextOrderDto);

	public List<NextOrder>  findList(List<Integer> userIdList, NextOrderDto nextOrderDto);

	public int findCount(NextOrderDto nextOrderDto);

	public List<NextOrderDto> findListByPage(NextOrderDto nextOrderDto,
			LimitPageDto limitPageDto, List<Integer> userIdList);
	
	public void reduceNextOrder(NextOrderDto nextOrderDto);
}
