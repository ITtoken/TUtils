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
	 * Get max value or minimum value in array.
	 * 
	 * @param intArr
	 * @param valueType
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

	private static int[] getSort(int[] intArr, int arrLen) {
		for (int i = 0; i < arrLen - 1; i++) {
			for (int j = 0; j < arrLen - 1 - i; j++) {
				if (intArr[j] > intArr[j + 1]) {
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