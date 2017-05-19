package com.tianjj.tutils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SlideMenu extends ViewGroup {

	private View menuView, mainView;
	private int menuHiewWidth;
	private int downX;
	private boolean isMenuShow = false;

	public SlideMenu(Context context) {
		super(context);
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		menuHiewWidth = menuView.getLayoutParams().width;
		menuView.measure(menuHiewWidth, heightMeasureSpec);
		mainView.measure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		menuView.layout(-menuHiewWidth, 0, 0, b);
		mainView.layout(l, t, r, b);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int scrollX = moveX - downX;
			if (Math.abs(scrollX) >= 8) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int scrollX = moveX - downX;// 绉诲姩鐨勮窛绂�

			if (scrollX > 0) {// 鍙虫粦
				if (getScrollX() <= -menuHiewWidth) {
					setScrollX(-menuHiewWidth);
					isMenuShow = false;
					return false;
				}
			} else if (scrollX < 0) {// 宸︽粦
				if (getScrollX() >= 0) {
					setScrollX(0);
					isMenuShow = true;
					return false;
				}
			}
			scrollBy(-scrollX, 0);
			downX = moveX;
			break;
		case MotionEvent.ACTION_UP:
			if (getScrollX() <= -menuHiewWidth / 2) {
				showMenu();
			} else if (getScrollX() > -menuHiewWidth / 2) {
				closeMenu();
			}
			break;
		}
		return true;
	}

	/**
	 * open the menu layout
	 */
	public void showMenu() {
		ScrollAnimation sa = new ScrollAnimation(this, -menuHiewWidth);
		startAnimation(sa);
		isMenuShow = true;
	}

	/**
	 * close the menu layout
	 */
	public void closeMenu() {
		ScrollAnimation sa = new ScrollAnimation(this, 0);
		startAnimation(sa);
		isMenuShow = false;
	}

	/**
	 * get the main layout
	 * 
	 * @return
	 */
	public View getMenuView() {
		return menuView;
	}

	/**
	 * get the menu layout
	 * 
	 * @return
	 */
	public View getMainView() {
		return mainView;
	}

	class ScrollAnimation extends Animation {

		private View view;
		private int targetX;
		private int startX;
		private float totalScroll;

		public ScrollAnimation(View view, int targetX) {
			super();
			this.view = view;
			this.targetX = targetX;

			startX = view.getScrollX();
			totalScroll = this.targetX - startX;

			setDuration(100);
			setInterpolator(new AccelerateDecelerateInterpolator());
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			int currentX = (int) (startX + totalScroll * interpolatedTime);
			view.scrollTo(currentX, 0);
		}
	}
}