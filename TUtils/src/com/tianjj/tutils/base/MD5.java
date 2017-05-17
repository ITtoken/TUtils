package com.tianjj.tutils.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 extends Base {
	/**
	 * @param password
	 *            The string needing to encrypted.
	 * @return The string be encrypted.
	 */
	public static String encodingMD5(String password) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(password.getBytes());

			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				// System.out.println(hexString);

				if (hexString.length() < 2) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			String MD5encoding = sb.toString();
			return MD5encoding;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;

	}
}