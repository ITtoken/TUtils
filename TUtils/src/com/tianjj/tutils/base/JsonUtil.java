package com.tianjj.tutils.base;

public class JsonUtil extends Base{
	/**
	 * 
	 * @param json
	 * @param strs
	 * @return return a result json string
	 */
	public static String expandJson(String json, String... strs) {
		String j = json.replaceAll("'", "\"");

		StringBuffer sb = new StringBuffer(j.substring(0, j.indexOf("}")));
		for (int i = 0; i < strs.length; i++) {
			if (i % 2 == 0) {
				sb.append(",\"" + strs[i] + "\"");
			} else {
				sb.append(":\"" + strs[i] + "\"");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * a pair arrays of type for String will be converted to a json string
	 * 
	 * @param keys
	 *            a collection of json's keys
	 * @param values
	 *            a collection of json's values
	 * 
	 * @throws RuntimeException
	 *             "json 杞崲鏉′欢涓嶆垚绔�"
	 * @return a json string whitch has formated
	 */
	public static String arrays2Json(String[] keys, String[] values) throws RuntimeException {

		if (keys == null || keys.length == 0 || values == null || values.length == 0 || keys.length != values.length) {
			RuntimeException exception = new RuntimeException("json 杞崲鏉′欢涓嶆垚绔�,璇锋鏌ユ暟缁勯暱搴︽槸鍚︿竴鏍�");
			exception.printStackTrace();
			throw exception;
		}
		StringBuffer sb = new StringBuffer("{");

		for (int i = 0; i < keys.length; i++) {
			String k_v_pair = "\"" + keys[i].trim() + "\":\"" + values[i].trim() + "\",";
			sb.append(k_v_pair);
		}
		sb.replace(sb.length() - 1, sb.length(), "}");
		return sb.toString();
	}
}
