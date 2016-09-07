package com.tianjj.tutils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;

public class SlideMenu extends ViewGroup {

	private View menuView, mainView;
	private int menuHiewWidth;
	private int downX;
	private boolean isMenuShow = false;

	/**
	 * 想要动态New的时候，调用这个构造函数
	 * 
	 * @param context
	 */
	public SlideMenu(Context context) {
		super(context);
	}

	/**
	 * 要想在xml文件中使用，就必须要实现这个构造函数
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 当ViewGroup中所有的子View（一级子View）都加在完毕的时候调用，初始化子view的引用
	 * 
	 * 注意：这里无法获取子view的宽高
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}

	/**
	 * 决定子控件在Slidemenu中的绘制大小
	 * 
	 * widthMeasureSpec：自定义控件（SlideMenu）的宽 heightMeasureSpec：定义控件（SlideMenu）的高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		menuHiewWidth = menuView.getLayoutParams().width;
		menuView.measure(menuHiewWidth, heightMeasureSpec);
		mainView.measure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * 决定子控件在SlideMenu中显示位置
	 * 
	 * ↓ 以GroupView（这里是SlideView）的左上角为原点，Y轴向下为正方向的坐标轴为急诊基准
	 * 
	 * l：groupView的左边的坐标（X坐标）：0 t：GroupView上边的左边（Y坐标）：0
	 * r：groupView右边的坐标（X左边）：Group的宽度 b：GroupView下边的坐标（Y坐标）：GroupView的高
	 * 
	 */
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

	/**
	 * 添加触摸事件侦听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getX();
			int scrollX = moveX - downX;// 移动的距离

			if (scrollX > 0) {// 右滑
				if (getScrollX() <= -menuHiewWidth) {
					setScrollX(-menuHiewWidth);
					isMenuShow = false;
					return false;
				}
			} else if (scrollX < 0) {// 左滑
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