package com.tianjj.tutils.base;

/**
 * 整形数组工具类
 * @author thunder
 *
 */
public class IntArrUtils {
	/**
	 * 此参数返回数组的最大值
	 */
	public static final int MAX_VALUE = 1;
	/**
	 * 此参数返回数组的最小值
	 */
	public static final int MIN_VALUE = 0;

	/**
	 * 获取整形数组中的最大值or最小值
	 * 
	 * @param intArr
	 *            目标数组
	 * @param valueType
	 *            值类型：最大值：{@link #MAX_VALUE} 最小值：{@link #MIN_VALUE}
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
	 * 对目标数组排序
	 * 
	 * @param intArr
	 *            目标数组
	 * @param arrLen
	 *            数组长度
	 * @return 排序后的数组
	 */
	private static int[] getSort(int[] intArr, int arrLen) {
		for (int i = 0; i < arrLen - 1; i++) {
			for (int j = 0; j < arrLen - 1 - i; j++) {
				if (intArr[j] > intArr[j + 1]) {
					/** 交换学生位置 */
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