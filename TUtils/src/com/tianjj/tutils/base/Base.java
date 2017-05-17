package com.tianjj.tutils.base;

import android.util.Log;

public class Base {
	private static final boolean LOG_ON = false;

	// =====LOG=====
	protected final void LogD(String tag, String msg) {
		if (LOG_ON) {
			Log.d(tag, msg);
		}
	}

	protected final void LogE(String tag, String msg) {
		if (LOG_ON) {
			Log.e(tag, msg);
		}
	}

	protected final void LogI(String tag, String msg) {
		if (LOG_ON) {
			Log.i(tag, msg);
		}
	}

	protected final void LogW(String tag, String msg) {
		if (LOG_ON) {
			Log.w(tag, msg);
		}
	}

	protected final void LogV(String tag, String msg) {
		if (LOG_ON) {
			Log.v(tag, msg);
		}
	}

	// =====TAG=====
	protected static final <T> String Tag(Class<T> clazz) {
		return clazz.getSimpleName();
	}

}
