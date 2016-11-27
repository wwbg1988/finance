package com.blackbeard.util.orderUtil;

import java.util.HashMap;
import java.util.Map;

import com.blackbeard.common.dto.GroupHandleDto;
import com.ssic.util.StringUtils;
import com.ssic.util.base64.Base64Coder;

public class BaseMessageInfo {
	// 统一的组装信息基类，包含统一的组装头部，将body与头部组成完整的字符
	private static int init_squenceId = 1000; // 初始的头部id

	private static GroupHandleDto groupHandleDto = new GroupHandleDto();

	// 获取自增长的ID 1000-9999
	public static int getQuenceId() {
		init_squenceId++;
		if (init_squenceId > 9999) {
			init_squenceId = 1000;
		}
		return init_squenceId;
	}

	// 解析成头部和内容
	public static Map<String, String> resolveHandleBody(String message) {
		if (StringUtils.isEmpty(message))
			return null;
		if (!message.contains("["))
			return null;
		if (!message.contains("]"))
			return null;
		Map<String, String> map = new HashMap<String, String>();
		message = message.substring(1, message.length() - 1);
		// System.out.println("message2=" + message);
		String handle = message.substring(0, 15);
		// System.out.println("handle=" + handle);
		String body = message.substring(15, message.length());
		// System.out.println("body=" + body);
		map.put("handle", handle);
		map.put("body", body);
		return map;
	}

	// 解析头部内容并且验证长度是否正确
	public static GroupHandleDto resolveHandle(String handle) {
		if (StringUtils.isNumeric(handle)) {
			if (StringUtils.isEmpty(handle)) {
				return null;
			}
			if (handle.length() != 15) {
				return null;
			}
			String id = handle.substring(0, 4);
			String languageId = handle.substring(4, 6);
			String agreementId = handle.substring(6, 8);
			String functionId = handle.substring(8, 10);
			String lengthId = handle.substring(10, 15);
			groupHandleDto.setId(Integer.parseInt(id));
			groupHandleDto.setLanguageId(languageId);
			groupHandleDto.setAgreementId(agreementId);
			groupHandleDto.setFunctionId(functionId);
			groupHandleDto.setLengthId(Integer.parseInt(lengthId));
			return groupHandleDto;
		} else {
			return null;
		}
	}

	// 解析头部body，返回头部对象，内容
	public static Map<String, Object> resolveAll(String message) {
		Map<String, Object> mapAll = new HashMap<String, Object>();
		Map<String, String> map = resolveHandleBody(message);
		if (map == null) {
			return mapAll;
		}
		String handle = map.get("handle");
		String body = map.get("body");
		// 解析头部
		GroupHandleDto groupHandleDto = resolveHandle(handle);
		if (groupHandleDto == null) {
			return setMapInfo(mapAll, "message 头部解析错误", 500);
		}
		// 头部解析成功，并且长度相同
		mapAll.put("groupHandleDto", groupHandleDto);
		mapAll.put("body", body);
		mapAll.put("result_msg", "message 货币信息解析成功");
		mapAll.put("result_code", 200);
		return mapAll;
	}

	// 填充map信息
	public static Map<String, Object> setMapInfo(Map<String, Object> map,
			String msg, int code) {
		map.put("result_msg", msg);
		map.put("result_code", code);
		return map;
	}

	// base64加密
	// [107010101800051kaka,500,LOGIN_ERROR:1470711276786]
	public static String getBase64Str(String msg) {
		if (StringUtils.isEmpty(msg) || msg.length() < 17) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String msg_handle_star = msg.substring(0, 1);
		String msg_handle = msg.substring(1, 16);
		String msg_body = msg.substring(16, msg.length() - 1);
		String msg_body_end = msg.substring(msg.length() - 1, msg.length());
		String functionId = msg_handle.substring(8, 10);
		try {
			String msg_body_64 = Base64Coder.encode(msg_body);
			if("18".equals(functionId)){
				//统一错误码不加密
				sb.append(msg_handle_star).append(msg_handle).append(msg_body).append(msg_body_end);
			}else{
				sb.append(msg_handle_star).append(getBase64Handle(msg_handle, msg_body_64)).append(msg_body_64).append(msg_body_end);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	//重新计算加密后的handle长度
	public static String getBase64Handle(String handle, String body){
		String handle_other = handle.substring(0, 10);
		int length_new = 17+body.length();
		String length_new_str = String.valueOf(length_new);
		if (length_new_str.length() < 5) {
			int cha = 5 - length_new_str.length();
			if (cha == 1) {
				length_new_str = "0" + length_new_str;
			} else if (cha == 2) {
				length_new_str = "00" + length_new_str;
			} else if (cha == 3) {
				length_new_str = "000" + length_new_str;
			}
		}
		String result = handle_other+length_new_str;
		return result;
	}
	
	
	// base64解密
	// [224310101300035ZG91amlhbyxEb3VqaWFvMTIz]
	public static String resolveBase64Str(String base64_msg) {
		if (StringUtils.isEmpty(base64_msg) || base64_msg.length() < 17) {
			return "";
		}
		// 开始校验长度是否合法
		if (!checkPCStrLength(base64_msg)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String base64_msg_handle = base64_msg.substring(0, 16);
		String base64_msg_body = base64_msg.substring(16,
				base64_msg.length() - 1);
		String base64_msg_end = base64_msg.substring(base64_msg.length() - 1,
				base64_msg.length());
		try {
			String msg_body = Base64Coder.decode(base64_msg_body);
			sb.append(base64_msg_handle).append(msg_body)
					.append(base64_msg_end);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	// 校验pcmsg长度是否合法
	public static boolean checkPCStrLength(String base64_msg) {
		Map<String, String> base64handlebody = resolveHandleBody(base64_msg);
		if (base64handlebody == null) {
			return false;
		}
		String handle = base64handlebody.get("handle");
		// 解析头部
		GroupHandleDto groupHandleDto = resolveHandle(handle);
		if (groupHandleDto == null) {
			return false;
		}
		int lengthId = groupHandleDto.getLengthId();
		if (lengthId != base64_msg.length()) {
			return false;
		} else {
			return true;
		}
	}

}
