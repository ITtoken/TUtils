package com.tianjj.tutils.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	/**
	 * MD5 加密
	 * 
	 * @param password
	 *            要加密的密码
	 * @return 加密后的密文
	 */
	public static String encodingMD5(String password) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");// 获取MD5算法对象
			byte[] digest = instance.digest(password.getBytes());// 对字符串加密,返回字节数组

			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff;// 获取字节的低八位有效值
				String hexString = Integer.toHexString(i);// 将整数转为16进制
				// System.out.println(hexString);

				if (hexString.length() < 2) {
					hexString = "0" + hexString;// 如果是1位的话,补0
				}
				sb.append(hexString);
			}
			String MD5encoding = sb.toString();
			return MD5encoding;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// 没有该算法时,抛出异常, 不会走到这里
		}
		return null;

	}
}