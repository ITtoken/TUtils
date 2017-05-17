package com.tianjj.tutils.base;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil extends Base{

	private static PinyinUtil pu = new PinyinUtil();
	private boolean isStartWithHY;

	private PinyinUtil() {
	}

	public static PinyinUtil getInstance() {
		return pu;
	}

	/**
	 * convert the han-yu to pin-yin
	 * 
	 * @param hanyuString
	 * @return the pin-yin {@link String} of spacific han-yu
	 */
	public String getPinyin(String hanyuString, HanyuPinyinCaseType caseType) {
		StringBuffer sb = new StringBuffer();

		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			format.setCaseType(caseType);
			String[] stringArray = null;
			for (int i = 0; i < hanyuString.length(); i++) {
				stringArray = PinyinHelper.toHanyuPinyinStringArray(hanyuString.charAt(i), format);
				if (i == 0) {
					if (stringArray == null) {
						isStartWithHY = false;
					} else {
						isStartWithHY = true;
					}
				}

				if (stringArray == null) {
					sb.append(hanyuString.charAt(i));
				} else {
					sb.append(stringArray[0]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * get the first char letter in pinyin string of specific han-yu , if the
	 * first char is ' ',return '#'
	 * 
	 * @param hanyuString
	 * @return the first char
	 */
	public char getFirstChar(String hanyuString) {
		char firstChar = getPinyin(hanyuString, HanyuPinyinCaseType.UPPERCASE).charAt(0);
		if (firstChar == ' ') {
			firstChar = '#';
		}
		return firstChar;

	}

	/**
	 * if the han-yu start with han-yu ,else letter.
	 * 
	 * @param hanyuString
	 * @return <code>true</code> is start with han-yu,else letter if
	 *         <code>false</code>
	 */
	public boolean isStartWithHY(String hanyuString) {
		getPinyin(hanyuString, HanyuPinyinCaseType.UPPERCASE).charAt(0);
		return isStartWithHY;
	}

}
