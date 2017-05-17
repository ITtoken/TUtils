package com.tianjj.tutils.base;

/**
 * Util Singleton
 */
public abstract class Singleton<T> {
	private volatile T mInstance;

	protected abstract T create();

	public final T get() {
		if (mInstance == null) {
			synchronized (this) {
				if (mInstance == null) {
					mInstance = create();
				}
			}
		}
		return mInstance;
	}
}