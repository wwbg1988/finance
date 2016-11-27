package com.blackbeard.service.test;

import com.blackbeard.common.util.MD5Util;
import com.ssic.util.StringUtils;
import com.ssic.util.base64.Base64Coder;

public class TestMd5Str {
//测试md5加密解密
	
	public static void main(String[] args) throws Exception {
		System.out.println("----------MD5加密----------");
//		String name = "zhangsan";
//		System.out.println(name);
		String md5_name = MD5Util.base64Encode("doujiao");
		System.out.println(md5_name);
//		String solve_md5_name = MD5Util.base64Decode(md5_name);
//		System.out.println(solve_md5_name);
		System.out.println("-------base64加密------");
		String name2 = "EURUSDDB,1.32680,100,0,B,1";
		String name2Encode =  Base64Coder.encode(name2);
		System.out.println(name2Encode);
		String name2Decode = Base64Coder.decode("bnVsbCw1MDAsUkVTT0xWRV9FUlJPUjoxNDcwNzI5NDY2NTg2");
		System.out.println(name2Decode);
		
	}

}
