package com.blackbeard.common.mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.OrderChartDto;
import com.blackbeard.common.dto.Page;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.pojo.order.HistoryOrder;

public interface HistoryOrderExMapper {

	
	public List<HistoryOrderDto> findBy(@Param("historyOrderDto")  HistoryOrderDto historyOrderDto);
	
	public void addHistoryOrder(@Param("historyOrderDto")  HistoryOrderDto historyOrderDto);

	public int findCount();

	public List<OrderChartDto> findChart(@Param("historyOrderDto")  HistoryOrderDto historyOrderDto);

}
