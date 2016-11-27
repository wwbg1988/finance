package com.blackbeard.common.util;

import java.util.ArrayList;
import java.util.List;

import com.ssic.util.StringUtils;

public class ResolveStickPackageAction {
	// 解决粘包问题

		// 必须是以[开始]结束,全部存入静态变量,分割字符串,把之前取到的数据删掉

		public  StringBuffer MT4_MESSAGE = new StringBuffer();

		public  String getMt4Message(String message) {
			if (StringUtils.isEmpty(message))
				return "";
			String return_message = "";
			// 判断传过来的字符串是不是p[]结尾
			boolean hasMacket = hasStarEndMarket(message);
			// 是[]结尾是一条完整数据,直接返回
			if (hasMacket) {
				return_message = message;
			} else {
				System.out.println("发生粘包现象");
				// 不是[]结尾不是条完整数据,保存在静态变量中,截取返回,删除取出来的
				return_message = getDelMessage(message);
			}
			return return_message;
		}

		public  boolean hasStarEndMarket(String message) {
			String star_msg = message.substring(0, 1);
			String end_msg = message.substring(message.length() - 1,
					message.length());
			if ("[".equals(star_msg) && "]".equals(end_msg))
				return true;
			else
				return false;
		}

		public  String getDelMessage(String message) {
			StringBuffer return_msg = new StringBuffer();
			System.out.println("MT4_MESSAGE1=("+Thread.currentThread().getName()+")"+MT4_MESSAGE);
			MT4_MESSAGE.append(message);
			System.out.println("MT4_MESSAGE2=("+Thread.currentThread().getName()+")"+MT4_MESSAGE);
			StringBuffer sb = new StringBuffer();
			int star_macket = MT4_MESSAGE.indexOf("[");
			int end_macket = MT4_MESSAGE.lastIndexOf("]") + 1;
			sb.append(MT4_MESSAGE.substring(star_macket, end_macket));
			return_msg.append(MT4_MESSAGE.substring(0, star_macket)).append(
					MT4_MESSAGE.substring(end_macket, MT4_MESSAGE.length()));
			MT4_MESSAGE = return_msg;
			System.out.println("MT4_MESSAGE3=("+Thread.currentThread().getName()+")"+MT4_MESSAGE);
			return sb.toString();
		}
		
		//[][][]  ====   list([])   将收到的一条信息解析成
 		public  List<String> splitPCMessage(String pcMessage){
			String[] bb =  pcMessage.split("]");
			List<String> result_strs = new ArrayList<String>(); 
			if(bb!=null && bb.length>0){
				for(String bi:bb){
					bi=bi+"]";
					result_strs.add(bi);
				}
			}
			return result_strs;
		}

//		public static void main(String[] args) {
//			// String message = "[zhangsan][lisi][wangwu]";
//			// String result_msg ="";
//			// result_msg = getMt4Message(message);
//			// System.out.println("result_msg="+result_msg);
//			String message1 = "[zhangsan][lisi][wangwu]";
//			String message2 = "[zha";
//			String message3 = "ngsan][lisi][wang";
//			String message4 = "wu]";
//			String[] messages = new String[] { message1, message2, message3,
//					message4 };
//			System.out.println(MT4_MESSAGE);
//			for (String msg : messages) {
//				String return_msg = getMt4Message(msg);
//				System.out.println("return_msg=" + return_msg);
//				System.out.println(MT4_MESSAGE);
//			}
//		}
}
