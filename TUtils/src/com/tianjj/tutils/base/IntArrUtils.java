package com.tianjj.tutils.base;

/**
 * 鏁村舰鏁扮粍宸ュ叿绫�
 * 
 * @author thunder
 *
 */
public class IntArrUtils extends Base {
	public static final int MAX_VALUE = 1;

	public static final int MIN_VALUE = 0;

	/**
	 * 鑾峰彇鏁村舰鏁扮粍涓殑鏈�澶у�紀r鏈�灏忓��
	 * 
	 * @param intArr
	 *            鐩爣鏁扮粍
	 * @param valueType
	 *            鍊肩被鍨嬶細鏈�澶у�硷細{@link #MAX_VALUE} 鏈�灏忓�硷細{@link #MIN_VALUE}
	 * 
	 * @see {@link #MAX_VALUE}
	 * @see {@link #MIN_VALUE}
	 * 
	 */
	public static int getValueType(int[] intArr, int valueType) {
		int arrLen = intArr.length;
		int[] afterSort = getSort(intArr, arrLen);
		switch (valueType) {
		case MAX_VALUE:
			return afterSort[arrLen - 1];
		case MIN_VALUE:
			return afterSort[0];
		}

		return 0;
	}

	/**
	 * 瀵圭洰鏍囨暟缁勬帓搴�
	 * 
	 * @param intArr
	 *            鐩爣鏁扮粍
	 * @param arrLen
	 *            鏁扮粍闀垮害
	 * @return 鎺掑簭鍚庣殑鏁扮粍
	 */
	private static int[] getSort(int[] intArr, int arrLen) {
		for (int i = 0; i < arrLen - 1; i++) {
			for (int j = 0; j < arrLen - 1 - i; j++) {
				if (intArr[j] > intArr[j + 1]) {
					/** 浜ゆ崲瀛︾敓浣嶇疆 */
					int temp1 = 0;
					temp1 = intArr[j];
					intArr[j] = intArr[j + 1];
					intArr[j + 1] = temp1;
				}
			}
		}
		return intArr;
	}
}