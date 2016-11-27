package com.blackbeard.common.mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackbeard.common.dto.NextOrderDto;

public interface NextOrderExMapper {

	public List<NextOrderDto> findBy(@Param("nextOrderDto")NextOrderDto nextOrderDto);
	
	public void addNextOrder(@Param("nextOrderDto")NextOrderDto nextOrderDto);
	
	public void reduceNextOrder(@Param("nextOrderDto")NextOrderDto nextOrderDto);
	
}
