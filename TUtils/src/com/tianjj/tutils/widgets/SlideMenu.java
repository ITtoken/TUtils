package com.tianjj.tutils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SlideMenu extends ViewGroup{

	private View menuView, mainView;
	private int menuHiewWidth;
	private int downX;
	private boolean isMenuShow = false;

	/**
	 * 鎯宠鍔ㄦ�丯ew鐨勬椂鍊欙紝璋冪敤杩欎釜鏋勯�犲嚱鏁�
	 * 
	 * @param context
	 */
	public SlideMenu(Context context) {
		super(context);
	}

	/**
	 * 瑕佹兂鍦▁ml鏂囦欢涓娇鐢紝灏卞繀椤昏瀹炵幇杩欎釜鏋勯�犲嚱鏁�
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 褰揤iewGroup涓墍鏈夌殑瀛怴iew锛堜竴绾у瓙View锛夐兘鍔犲湪瀹屾瘯鐨勬椂鍊欒皟鐢紝鍒濆鍖栧瓙view鐨勫紩鐢�
	 * 
	 * 娉ㄦ剰锛氳繖閲屾棤娉曡幏鍙栧瓙view鐨勫楂�
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}

	/**
	 * 鍐冲畾瀛愭帶浠跺湪Slidemenu涓殑缁樺埗澶у皬
	 * 
	 * widthMeasureSpec锛氳嚜瀹氫箟鎺т欢锛圫lideMenu锛夌殑瀹� heightMeasureSpec锛氬畾涔夋帶浠讹紙SlideMenu锛夌殑楂�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		menuHiewWidth = menuView.getLayoutParams().width;
		menuView.measure(menuHiewWidth, heightMeasureSpec);
		mainView.measure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * 鍐冲畾瀛愭帶浠跺湪SlideMenu涓樉绀轰綅缃�
	 * 
	 * 鈫� 浠roupView锛堣繖閲屾槸SlideView锛夌殑宸︿笂瑙掍负鍘熺偣锛孻杞村悜涓嬩负姝ｆ柟鍚戠殑鍧愭爣杞翠负鎬ヨ瘖鍩哄噯
	 * 
	 * l锛歡roupView鐨勫乏杈圭殑鍧愭爣锛圶鍧愭爣锛夛細0 t锛欸roupView涓婅竟鐨勫乏杈癸紙Y鍧愭爣锛夛細0
	 * r锛歡roupView鍙宠竟鐨勫潗鏍囷紙X宸﹁竟锛夛細Group鐨勫搴� b锛欸roupView涓嬭竟鐨勫潗鏍囷紙Y鍧愭爣锛夛細GroupView鐨勯珮
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
	 * 娣诲姞瑙︽懜浜嬩欢渚﹀惉
	 */
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