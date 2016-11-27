package com.blackbeard.common.service;

import java.util.List;

import com.blackbeard.common.dto.AccountResultDto;
import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.LimitPageDto;
import com.blackbeard.common.dto.NextOrderDto;
import com.blackbeard.common.dto.OrderChartDto;
import com.blackbeard.common.dto.OrderParam;
import com.blackbeard.common.pojo.order.HistoryOrder;

public interface IHistoryOrderService {

	public List<HistoryOrderDto> findBy(HistoryOrderDto historyOrderDto);

	public void addHistoryOrder(HistoryOrderDto historyOrderDto);

	/**
	 * 查找数量
	 * 
	 * @author 刘博
	 * @return int
	 */
	public int findCount(HistoryOrderDto historyOrderDto);

	/**
	 * findListByPage：查找所有的历史订单
	 * 
	 * @author 刘博
	 * @param historyOrderDto
	 * @param limitPageDto
	 * @param roleList
	 * @return List<HistoryOrderDto>
	 * @exception
	 * @author 刘博
	 * @date 2016年7月7日 下午3:09:22
	 */

	public List<HistoryOrderDto> findListByPage(
			HistoryOrderDto historyOrderDto, LimitPageDto limitPageDto,
			List<Integer> roleList);

	public List<HistoryOrder> findList(List<Integer> userIdList,
			HistoryOrderDto historyOrderDto);

	/**
	 * findOrderList：web接口:查找某个用户的的历史订单
	 * 
	 * @param orderParam
	 * @return List<HistoryOrderDto>
	 * @exception
	 * @author 刘博
	 * @date 2016年7月11日 下午3:09:22
	 */
	public List<HistoryOrderDto> findOrderList(OrderParam orderParam);

	/**
	 * findAccountResult：查询结算结果
	 * 
	 * @param historyOrderDto
	 * @return AccountResultDto
	 * @exception
	 * @author 刘博
	 * @date 2016年7月20日 下午13:09:22
	 */
	public AccountResultDto findAccountResult(HistoryOrderDto historyOrderDto);

	/**
	 * inserByNextOrderDto：通过临时订单dto对象插入历史订单数据
	 * 
	 * @param nextOrderDto
	 *            临时订单dto对象
	 * 
	 * @exception
	 * @author 刘博
	 * @date 2016年7月22日 下午13:09:22
	 */
	public void inserByNextOrderDto(NextOrderDto nextOrderDto);

	/**
	 * findChartByPage：生成历史订单图表
	 * 
	 * @param historyOrderDto
	 *            历史订单dto对象
	 * @exception
	 * @author 刘博
	 * @date 2016年7月29日 下午13:09:22
	 * @return String
	 */
	public String findChartByPage(HistoryOrderDto historyOrderDto);

}
