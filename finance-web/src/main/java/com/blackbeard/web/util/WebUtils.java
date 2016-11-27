package com.blackbeard.web.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import com.blackbeard.common.dto.HistoryOrderDto;
import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.dto.Mt4ConfigDto;
import com.blackbeard.common.dto.Mt4RegisterDto;
import com.blackbeard.common.dto.PageData;
import com.blackbeard.common.pojo.order.NextOrder;
import com.blackbeard.common.util.MD5Util;
import com.blackbeard.web.vo.RechargeMoneyDto;

public class WebUtils {

	private static Long timeStamp = 0L;

	public static String listToString(List<KlineRecordDto> list) {
		StringBuilder returnString = new StringBuilder();
		for (KlineRecordDto klineDto : list) {
			returnString.append(objectToString(klineDto));
		}
		return returnString.toString();
	}

	private static String objectToString(KlineRecordDto klineDto) {
		String simpleKlineRecord = klineDto.getCurrency() + ","
				+ klineDto.getOpenPrice() + "," + klineDto.getClosePrice()
				+ "," + klineDto.getHighPrice() + "," + klineDto.getLowPrice()
				+ "," + klineDto.getDecimals() + "," + klineDto.getTimeStamp();
		return simpleKlineRecord + "\r\n";
		// 如果k线传输长度小于75,则补充/0
		// return codeAddLength(simpleKlineRecord,
		// KlineConstants.WEB_KLINE_LENGTH);
	}

	public static String codeAddLength(String code, int len) {
		while (code.length() < len) {
			code += "0";
		}
		System.out.println(code);
		System.out.println("字符串长度:-----" + code.length());
		return code;
	}

	public static String listToStringforAll(List<KlineRecordDto> list) {
		StringBuilder returnString = new StringBuilder();
		List<KlineRecordDto> newList = new ArrayList<KlineRecordDto>();
		for (KlineRecordDto klineDto : list) {
			if (timeStamp == 0) {
				timeStamp = klineDto.getTimeStamp();
			}
			if (klineDto.getTimeStamp().equals(timeStamp)) {
				newList.add(klineDto);
			} else {
				// 13组货币组装一个对象;
				String objectString = listObjectToString(newList, timeStamp);
				if (!StringUtils.isEmpty(objectString)) {
					returnString.append(objectString);
				}
				newList.clear();
				newList.add(klineDto);
				timeStamp = 0L;
			}
		}
		return returnString.toString();
	}

	private static String listObjectToString(List<KlineRecordDto> newList,
			Long timeStamp) {
		int i = 0;
		String simpleKlineRecord = "";
		if (!CollectionUtils.isEmpty(newList)) {
			for (KlineRecordDto klineDto : newList) {
				if (i > 0) {
					simpleKlineRecord += ".";
				}
				simpleKlineRecord += klineDto.getCurrency() + ","
						+ klineDto.getOpenPrice() + ","
						+ klineDto.getClosePrice() + ","
						+ klineDto.getHighPrice() + ","
						+ klineDto.getLowPrice() + "," + klineDto.getDecimals();
				i++;
			}
			simpleKlineRecord += ":" + timeStamp;
			return simpleKlineRecord + "\r\n";
		}
		return null;
	}

	public static String loadMt4ConigParam(PageData pd,
			Mt4ConfigDto mt4ConfigDto) {
		Mt4RegisterDto dto = new Mt4RegisterDto();
		dto.setUsername(pd.getString("USERNAME"));
		dto.setUserpassword(MD5Util.base64Decode(pd.getString("PASSWORD")));
		dto.setUseremail(pd.getString("EMAIL"));
		dto.setUserlever(mt4ConfigDto.getUserlever());
		dto.setIp(mt4ConfigDto.getServerIp() + ":"
				+ mt4ConfigDto.getServerPort());
		dto.setLgmanage(Integer.valueOf(mt4ConfigDto.getLgManage()));
		dto.setLgpword(mt4ConfigDto.getLgPword());
		dto.setMid(mt4ConfigDto.getMid());
		dto.setUsergroup(pd.getString("USERNAME"));
		JSONObject jsonObject = JSONObject.fromObject(dto);
		String mt4ConfigParam = jsonObject.toString();
		return mt4ConfigParam;
	}

	public static String listToStringforOrder(List<HistoryOrderDto> list) {
		StringBuilder returnString = new StringBuilder();
		for (HistoryOrderDto historyOrderDto : list) {
			returnString.append(orderToString(historyOrderDto));
		}
		return returnString.toString();
	}

	private static String orderToString(HistoryOrderDto historyOrderDto) {
		// 返回订单数据格式：货币名称,开始价格,结束价格,开始时间,结束时间,类型,盈亏,订单量,订单编号+换行
		String simpleOrderRecord = historyOrderDto.getSymbol() + ","
				+ historyOrderDto.getOpenPrice() + ","
				+ historyOrderDto.getClosePrice() + ","
				+ historyOrderDto.getOpenTime() + ","
				+ historyOrderDto.getCloseTime() + ","
				+ historyOrderDto.getCmd() + "," + historyOrderDto.getProfit()
				+ "," + historyOrderDto.getTotal() + ","
				+ historyOrderDto.getTick();
		return simpleOrderRecord + "\r\n";
	}

	public static String loadRechargeMoneyParam(
			RechargeMoneyDto rechargeMoneyDto, Mt4ConfigDto mt4ConfigDto) {
		rechargeMoneyDto.setIp(mt4ConfigDto.getServerIp() + ":"
				+ mt4ConfigDto.getServerPort());
		rechargeMoneyDto
				.setLgmanage(Integer.valueOf(mt4ConfigDto.getLgManage()));
		rechargeMoneyDto.setLgpword(mt4ConfigDto.getLgPword());
		JSONObject jsonObject = JSONObject.fromObject(rechargeMoneyDto);
		String rechargeMoneyParam = jsonObject.toString();
		System.out
				.println("rechargeMoneyParam===========" + rechargeMoneyParam);
		return rechargeMoneyParam;
	}

	public static String listToStringforOnlineOrder(List<NextOrder> list) {
		StringBuilder returnString = new StringBuilder();
		for (NextOrder nextOrder : list) {
			returnString.append(onlineOrderToString(nextOrder));
		}
		return returnString.toString();
	}

	private static String onlineOrderToString(NextOrder nextOrder) {
		// 返回在线订单数据格式：货币名称,买或卖,开仓价格,订单号,开仓时间,数量,涨跌分钟+换行
		String onlineOrderRecord = nextOrder.getCurrencyType() + ","
				+ nextOrder.getDirection() + "," + nextOrder.getPrice() + ","
				+ nextOrder.getTick() + "," + nextOrder.getOpenTime() + ","
				+ nextOrder.getTotal() + "," + nextOrder.getComment();
		return onlineOrderRecord + "\r\n";
	}
}
