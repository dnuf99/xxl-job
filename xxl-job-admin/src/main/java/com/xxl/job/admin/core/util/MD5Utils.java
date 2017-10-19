package com.xxl.job.admin.core.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
	
	/**利用MD5进行加密
	 　* @param str  待加密的字符串
	　* @return  加密后的字符串
	*/
	public static String EncoderByMd5(String str){
		String md5Str = DigestUtils.md5Hex(str);  
		return md5Str;
	}
	
	public static void main(String[] args) {
		System.out.println(EncoderByMd5("abc=123_admin"));
	}
}
