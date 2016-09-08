package com.tianjj.tutils.base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

public class AndroidUtils {
	private static WindowManager wm;
	private static int screenWidth;
	private static int screenHeight;
	private static long times[];

	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	private static void init(Context context) {
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	/**
	 * 双击
	 * 
	 * @param gapTime
	 *            点击间隔时间
	 * @return 点击成功与否判断:true表示双击成功,false表示双击失败
	 */
	public static boolean doubleClick(long gapTime) {
		times = new long[2];
		System.arraycopy(times, 1, times, 0, times.length - 1);
		long time1 = System.currentTimeMillis();
		times[times.length - 1] = time1;
		if (times[times.length - 1] - times[0] <= gapTime) {
			return true;
		}
		return false;
	}

	/**
	 * 多次点击事件
	 * 
	 * @param gapTime
	 * @return
	 */
	public static boolean MuitiClick(long gapTime, int clickTimes) {
		times = new long[clickTimes];
		System.arraycopy(times, 1, times, 0, times.length - 1);
		long time1 = System.currentTimeMillis();
		times[times.length - 1] = time1;
		if (times[times.length - 1] - times[0] <= gapTime) {
			return true;
		}
		return false;
	}

	/**
	 * 获取屏幕尺寸
	 *
	 * @param context
	 * @return 屏幕尺寸信息
	 */
	public static int getScrennWidth(Context context) {
		init(context);
		return screenWidth;
	}

	/**
	 * 获取屏幕高度
	 *
	 * @param context
	 * @return 屏幕尺寸信息
	 */
	public static int getScrennHeight(Context context) {
		init(context);
		return screenHeight;
	}
	
	/**
	 *  单例Toast
	 * @param context
	 * @param s 要显示的内容
	 */
	public static void showToast(Context context, String s) {
		if (toast == null) {
			toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (s.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = s;
				toast.setText(s);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	/**
	 * 单例Toast
	 * @param context
	 * @param resId 字符串资源ID
	 */
	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}

}
