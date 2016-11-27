package com.blackbeard.socket.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.blackbeard.common.constant.KlineConstants;
import com.blackbeard.common.dto.CurrencyConfigDto;
import com.blackbeard.common.dto.KlineRecordDto;
import com.blackbeard.common.service.IKlineRecordService;
import com.blackbeard.common.util.SocketUtils;
import com.blackbeard.socket.vo.ThreadHandleVO;
import com.google.common.base.Objects;

/**
 * k线工具包
 * 
 * @author 刘博
 *
 */
@Component
public class KlineHandleService {

	// 全局变量分钟
	private static int now_minute = 60;
	// K线长度
	private static int kline_length = 728;
	// 返回消息头id:1000-9999
	private static int headerId = KlineConstants.ONE_THOUSAND;
	public static List<String> stringList = new ArrayList<String>();

	public static StringBuffer sb = new StringBuffer();

	// 全局list变量,存放k线对象:
	public static List<KlineRecordDto> list = new ArrayList<KlineRecordDto>();
	// 1分钟
	public static List<KlineRecordDto> list1 = new ArrayList<KlineRecordDto>();
	// 5分钟
	public static List<KlineRecordDto> list5 = new ArrayList<KlineRecordDto>();
	// 15分钟
	public static List<KlineRecordDto> list15 = new ArrayList<KlineRecordDto>();
	private static final Logger logger = Logger
			.getLogger(KlineHandleService.class);

	/**
	 * 
	 * 处理组装mp4传来的数据
	 * 
	 * @param ThreadHandleVO
	 *            threadHandleVO
	 * @return StringBuilder
	 * @author 刘博
	 *
	 */
	public static StringBuilder handleMessage(ThreadHandleVO threadHandleVO) {
		// 12 20 20
		int endIndex1 = KlineConstants.TWELVE;
		int endIndex2 = KlineConstants.TWENTY;
		int endIndex3 = KlineConstants.TWENTY;
		int index = KlineConstants.ONE;
		for (int i = KlineConstants.ZERO; i <= threadHandleVO.getMessage()
				.length(); i++) {
			if (i != KlineConstants.ZERO
					&& i % (endIndex1 + endIndex2 + endIndex3) == KlineConstants.ZERO) {
				int nowCount = index / KlineConstants.ONE;
				int firstIndex = KlineConstants.ZERO;
				int firstEnd = KlineConstants.ZERO;
				int secondEnd = KlineConstants.ZERO;
				int thirdEnd = KlineConstants.ZERO;
				if (nowCount == 1) {
					firstIndex = KlineConstants.ZERO;
					firstEnd = endIndex1;
					secondEnd = endIndex1 + endIndex2;
					thirdEnd = endIndex1 + endIndex2 + endIndex3;
				} else {
					int firstCount = (nowCount - KlineConstants.ONE) * 52;
					firstIndex = firstCount;
					firstEnd = firstCount + endIndex1;
					secondEnd = firstCount + endIndex1 + endIndex2;
					thirdEnd = firstCount + endIndex1 + endIndex2 + endIndex3;
				}
				if (StringUtils.isEmpty(threadHandleVO.getMessage())) {
					continue;
				}
				String currency = threadHandleVO.getMessage().substring(
						firstIndex, firstEnd);

				String decimal = threadHandleVO.getMessage().substring(
						firstEnd, secondEnd);
				String price = threadHandleVO.getMessage()
						.substring(secondEnd, thirdEnd).trim();
				
                 //用于上面循环判断使用
				++index;

				// 判断这种货币是否需要显示
				if (!hasThisCurrency(currency)) {
					continue;
				}

				if (validNotExists(currency.trim())) {
					KlineRecordDto vo = new KlineRecordDto();
					vo.setCurrency(currency.trim());
					vo.setDecimals(decimal.trim());
					vo.setClosePrice(price.trim());
					vo.setHighPrice(price.trim());
					vo.setLowPrice(price.trim());
					vo.setOpenPrice(price.trim());
					vo.setCurrency(currency.trim());
					vo.setTimeStamp(threadHandleVO.getCurrentTime());
					list.add(vo);
				} else {// 如果集合不为空且货币价格都存在,每次更新closePrice
					for (KlineRecordDto dto : list) {
						if (currency.trim().equals(dto.getCurrency())) {
							dto.setTimeStamp(threadHandleVO.getCurrentTime());
							dto.setClosePrice(price.trim());
							if (price.compareTo(dto.getHighPrice()) > KlineConstants.ZERO) {
								dto.setHighPrice(price.trim());
							} else if (price.compareTo(dto.getLowPrice()) < KlineConstants.ZERO) {
								dto.setLowPrice(price.trim());
							}
						}
					}
				}
				if (validNotExists5(currency.trim())) {
					KlineRecordDto vo = new KlineRecordDto();
					vo.setCurrency(currency.trim());
					vo.setDecimals(decimal.trim());
					vo.setClosePrice(price.trim());
					vo.setHighPrice(price.trim());
					vo.setLowPrice(price.trim());
					vo.setOpenPrice(price.trim());
					vo.setCurrency(currency.trim());
					vo.setTimeStamp(threadHandleVO.getCurrentTime());
					list5.add(vo);
				} else {// 如果集合不为空且货币价格都存在,每次更新closePrice
					for (KlineRecordDto dto : list5) {
						if (currency.trim().equals(dto.getCurrency())) {
							dto.setTimeStamp(threadHandleVO.getCurrentTime());
							dto.setClosePrice(price.trim());
							if (price.compareTo(dto.getHighPrice()) > KlineConstants.ZERO) {
								dto.setHighPrice(price.trim());
							} else if (price.compareTo(dto.getLowPrice()) < KlineConstants.ZERO) {
								dto.setLowPrice(price.trim());
							}
						}
					}
				}
				if (validNotExists15(currency.trim())) {
					KlineRecordDto vo = new KlineRecordDto();
					vo.setCurrency(currency.trim());
					vo.setDecimals(decimal.trim());
					vo.setClosePrice(price.trim());
					vo.setHighPrice(price.trim());
					vo.setLowPrice(price.trim());
					vo.setOpenPrice(price.trim());
					vo.setCurrency(currency.trim());
					vo.setTimeStamp(threadHandleVO.getCurrentTime());
					list15.add(vo);
				} else {// 如果集合不为空且货币价格都存在,每次更新closePrice
					for (KlineRecordDto dto : list15) {
						if (currency.trim().equals(dto.getCurrency())) {
							dto.setTimeStamp(threadHandleVO.getCurrentTime());
							dto.setClosePrice(price.trim());
							if (price.compareTo(dto.getHighPrice()) > KlineConstants.ZERO) {
								dto.setHighPrice(price.trim());
							} else if (price.compareTo(dto.getLowPrice()) < KlineConstants.ZERO) {
								dto.setLowPrice(price.trim());
							}
						}
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(list)) {
			StringBuilder returnString = listConvertToString(list);
			return returnString;
		}
		return null;
	}

	/**
	 * 获取自增长的ID 1000-9999
	 * 
	 * @return int
	 * @author 刘博
	 *
	 */
	public static int getHeaderId() {
		headerId++;
		if (headerId > KlineConstants.NINE_NINE_NINE_NINE) {
			headerId = KlineConstants.ONE_THOUSAND;
		}
		return headerId;
	}

	/**
	 * 获取当前时间:分钟
	 * 
	 * @return int
	 * @author 刘博
	 *
	 */
	public static int nowMinutes() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		int now_minute = cal.get(Calendar.MINUTE);
		return now_minute;
	}

	/**
	 * 获取当前时间之前或之后几分钟 minute
	 */

	public static int getTimeByMinute(int minute) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		cal.add(Calendar.MINUTE, minute);
		int return_minute = cal.get(Calendar.MINUTE);
		return return_minute;

	}

	/**
	 * 把k线的list集合对象转化为StringBuilder
	 * 
	 * @param List
	 *            <KlineRecordDto> list
	 * @return StringBuilder
	 * @author 刘博
	 *
	 */
	public static StringBuilder listConvertToString(List<KlineRecordDto> list) {
		StringBuilder returnString = new StringBuilder();
		returnString.append("[");
		String functionNumber = KlineConstants.KLINE_FUNCTION_NUMBER;
		String protocolNumber = KlineConstants.KLINE_PROTOCOL_NUMBER;
		String languages = KlineConstants.KLINE_LANGUAGES;
		String time = "";
		String currencyString = "";
		String headers = "";
		int i = KlineConstants.ZERO;
		for (KlineRecordDto dto : list) {
			String localString = "";
			if (i > KlineConstants.ZERO) {
				currencyString += ".";
			}
			headers = languages + protocolNumber + functionNumber;
			time = String.valueOf(dto.getTimeStamp());
			localString = dto.getCurrency().trim() + ","
					+ dto.getOpenPrice().trim() + ","
					+ dto.getClosePrice().trim() + ","
					+ dto.getHighPrice().trim() + ","
					+ dto.getLowPrice().trim() + "," + dto.getDecimals().trim();
			currencyString += localString;
			i++;
		}
		returnString.append(getHeaderId()).append(headers);
		currencyString += ":" + time + "]";
		int lengths = currencyString.length() + returnString.length() + 5;
		String returnLength = "";

		if (lengths > 999 && lengths < 10000) {
			returnLength = "0";
		} else if (lengths > 99 && lengths < 1000) {
			returnLength = "00";
		} else if (lengths < 100) {
			returnLength = "000";
		}
		returnString.append(returnLength + String.valueOf(lengths));
		returnString.append(currencyString);
		return returnString;
	}

	/**
	 * 验证 某一种货币是否存在于list集合中
	 * 
	 * @param* @return boolean
	 * @author 刘博
	 *
	 */
	public static boolean validNotExists(String currency) {
		for (KlineRecordDto dto : list) {
			if (currency.trim().equals(dto.getCurrency())) {
				return false;
			}
		}
		return true;
	}

	public static boolean validNotExists5(String currency) {
		for (KlineRecordDto dto : list5) {
			if (currency.trim().equals(dto.getCurrency())) {
				return false;
			}
		}
		return true;
	}

	public static boolean validNotExists15(String currency) {
		for (KlineRecordDto dto : list15) {
			if (currency.trim().equals(dto.getCurrency())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 发送MP4数据到MQ队列中，每分钟插入数据库
	 * 
	 * @param ThreadHandleVO
	 * @author 刘博
	 *
	 */
	public static void pushDataToMQ(ThreadHandleVO threadHandleVO) {
		StringBuilder returnString = handleMessage(threadHandleVO);
		if (!StringUtils.isEmpty(returnString.toString())) {
			if (now_minute == 60) {
				now_minute = nowMinutes();
			}
			if (nowMinutes() != now_minute) {// 1分钟存一次
				if (!CollectionUtils.isEmpty(list)) {
					insertDB(list, KlineConstants.KLINE_ONE);
				}
				list.clear();
				if (nowMinutes() % KlineConstants.KLINE_FIVE == KlineConstants.ZERO) {// 5分钟存一次
					if (!CollectionUtils.isEmpty(list5)) {
						insertDB(list5, KlineConstants.KLINE_FIVE);
					}
					list5.clear();
				}
				if (nowMinutes() % KlineConstants.KLINE_FIFTEEN == KlineConstants.ZERO) {// 15分钟存一次
					if (!CollectionUtils.isEmpty(list15)) {
						insertDB(list15, KlineConstants.KLINE_FIFTEEN);
					}
					list15.clear();
				}
				now_minute = nowMinutes();
			}

			// 发送数据到队列中
			if (returnString.length() != KlineConstants.ZERO
					&& returnString != null) {
				// 发送消息到队列
				// System.out.println("mt4-kline:"+returnString.toString());
				ActivemqService.sendMessage(returnString.toString());
			}
		}
	}

	/**
	 * 将上一分钟的k线对象插入到数据库
	 * 
	 * @param List
	 *            <KlineRecordDto> list
	 * @author 刘博
	 *
	 */
	public static void insertDB(List<KlineRecordDto> list, int minute) {
		IKlineRecordService klineRecordService = SocketUtils.getKlineService();
		for (KlineRecordDto dto : list) {
			try {
				if (Objects.equal(dto, null)) {
					continue;
				}
				klineRecordService.insert(dto, minute);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理黏包
	 * 
	 * @param klineData
	 *            有问题的k线数据
	 */
	public static List<String> handleStickyPackage(String klineData) {
		stringList.clear();
		if (klineData.length() != kline_length) {
			// 如果长度不等于728，放入returnMessage中;
			sb.append(klineData);
		} else {
			stringList.add(klineData);
		}
		while (true) {
			if (sb.length() >= kline_length) {
				String returnMessage = sb.toString().substring(0, kline_length);
				stringList.add(returnMessage);
				sb.delete(0, kline_length);
			} else {
				break;
			}
		}
		return stringList;
	}

	// 判断这种货币是否需要展示
	public static boolean hasThisCurrency(String currencyName) {
		List<CurrencyConfigDto> list_currency = KlineConstants.LIST_CURRENCY;
		if (CollectionUtils.isEmpty(list_currency)) {
			return false;
		}

		for (CurrencyConfigDto currencyConfigDto : list_currency) {
			String currency_enable = currencyConfigDto.getCurrency();
			if (currency_enable.equals(currencyName.trim())) {
				return true;
			}
		}
		return false;
	}

}
