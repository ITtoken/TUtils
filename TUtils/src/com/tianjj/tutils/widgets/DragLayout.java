package com.tianjj.tutils.widgets;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class DragLayout extends FrameLayout {
	private ViewGroup mMenu_ViewGroup;
	private ViewGroup mMain_ViewGroup;
	private int mScreenWidth;
	private int mScreenHeight;
	private float mAnimProgress;
	private ViewDragHelper mViewDragHelper;
	private float mMainMaxLeft;
	private boolean mIsMenuOpen = false;
	private DragStatListener mListener;

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mMenu_ViewGroup = (ViewGroup) getChildAt(0);
		mMain_ViewGroup = (ViewGroup) getChildAt(1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mScreenWidth = getMeasuredWidth();
		mScreenHeight = getMeasuredHeight();
		mMainMaxLeft = mScreenWidth * 0.6f;
	}

	// TODO----------------------------private---------------------------------------
	private void init(Context context) {
		// 绗竴姝�:鍒涘缓ViewDragHelper
		mViewDragHelper = ViewDragHelper.create(this, mCallback);
	}

	private int fixLeft(int left) {
		if (left > (int) (mMainMaxLeft)) {
			left = (int) (mMainMaxLeft);
			mListener.open();
		} else if (left < 0) {
			left = 0;
			mListener.close();
		}
		return left;
	}

	/**
	 * excute specified animation for menu content
	 * 
	 * @param menuAlpha
	 *            use the alpha animation for menu
	 * @param menuTranslate
	 *            use the translation animation for menu
	 * @param menuScaleX
	 *            use the scale animation to scale X for menu
	 * @param menuScaleY
	 *            use the scale animation to scale Y for menu
	 * @param mainAnim
	 *            use the main content animation
	 */
	private void animExcute(boolean menuAlpha, boolean menuTranslate, boolean menuScaleX, boolean menuScaleY,
			boolean mainAnim) {
		FloatEvaluator f_Evaluator = new FloatEvaluator();
		ArgbEvaluator argb_Evaluator = new ArgbEvaluator();

		if (menuAlpha) {
			mMenu_ViewGroup.setAlpha(mAnimProgress);
		}

		if (menuTranslate) {
			mMenu_ViewGroup.setTranslationX(
					f_Evaluator.evaluate(mAnimProgress, -mMenu_ViewGroup.getMeasuredWidth() * 1.0 / 5, 0));
		}

		if (menuScaleX) {
			mMenu_ViewGroup.setScaleX(
					f_Evaluator.evaluate(mAnimProgress, 0.5f * mAnimProgress + 0.5f, 0.5f * mAnimProgress + 0.5f));
		}

		if (menuScaleY) {
			mMenu_ViewGroup.setScaleY(
					f_Evaluator.evaluate(mAnimProgress, 0.5f * mAnimProgress + 0.5f, 0.5f * mAnimProgress + 0.5f));
		}
		mMenu_ViewGroup.setPivotX(0);

		if (mainAnim) {
			mMain_ViewGroup.setScaleX(f_Evaluator.evaluate(mAnimProgress, 1, 0.8f));
			mMain_ViewGroup.setScaleY(f_Evaluator.evaluate(mAnimProgress, 1, 0.8f));
		}

		getBackground().setColorFilter((Integer) argb_Evaluator.evaluate(mAnimProgress, Color.BLACK, Color.TRANSPARENT),
				PorterDuff.Mode.DARKEN);
	}

	// TODO----------------------------public---------------------------------------
	public DragLayout(Context context) {
		super(context, null);
	}

	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private Callback mCallback = new Callback() {

		@Override
		public boolean tryCaptureView(View view, int pointerId) {
			return true;
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (child == mMain_ViewGroup) {
				left = fixLeft(left);
			}
			return left;
		}

		public int getViewHorizontalDragRange(View child) {
			return getMeasuredWidth();
		};

		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
		}

		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			int mainPos = 0;
			mAnimProgress = Math.abs(mMain_ViewGroup.getLeft() / mMainMaxLeft);

			if (changedView == mMenu_ViewGroup) {
				mainPos = mMain_ViewGroup.getLeft() + dx;

				if (mainPos > mMainMaxLeft) {
					mainPos = (int) mMainMaxLeft;
					mListener.open();
				} else if (mainPos < 0) {
					mainPos = 0;
					mListener.close();
				}
				mMenu_ViewGroup.layout(0, 0, mScreenWidth, mScreenHeight);
				mMain_ViewGroup.layout(mainPos, 0, mainPos + mScreenWidth, mScreenHeight);
			}

			animExcute(true, true, false, false, false);
			mListener.draging();
			invalidate();
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);

			if (xvel < 5 && mMain_ViewGroup.getLeft() >= mMainMaxLeft / 2.0) {
				open(true);
			} else if (xvel < 5 && mMain_ViewGroup.getLeft() < mMainMaxLeft / 2.0) {
				close(true);
			} else if (xvel >= 5) {
				open(true);
			} else {
				close(true);
			}
			//
			// if (xvel == 0 && releasedChild == mMain_ViewGroup) {
			// close(true);
			// }
		}
	};

	public void open(boolean isSmooth) {
		int finalLeft = (int) (mMainMaxLeft);
		if (isSmooth) {
			boolean slideViewTo = mViewDragHelper.smoothSlideViewTo(mMain_ViewGroup, finalLeft, 0);
			if (slideViewTo) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMain_ViewGroup.layout(finalLeft, 0, finalLeft + mScreenWidth, mScreenHeight);
		}

		mIsMenuOpen = true;
		mListener.open();
	}

	public void close(boolean isSmooth) {
		int finalLeft = 0;
		if (isSmooth) {
			boolean slideViewTo = mViewDragHelper.smoothSlideViewTo(mMain_ViewGroup, finalLeft, 0);
			if (slideViewTo) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMain_ViewGroup.layout(finalLeft, 0, finalLeft + mScreenWidth, mScreenHeight);
		}
		mIsMenuOpen = false;
		mListener.close();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mViewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mViewDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	/**
	 * get the menu-show status
	 * 
	 * @return true :menu is show or false is not show
	 */
	public boolean isMenuSHow() {
		return mIsMenuOpen;
	}

	/**
	 * get the main view group
	 * 
	 * @return the object for main view group
	 */
	public ViewGroup getMainViewGroup() {
		return mMain_ViewGroup;
	}

	/**
	 * get the menu view group
	 * 
	 * @return the object for menu view group
	 */
	public ViewGroup getMenuViewGroup() {
		return mMenu_ViewGroup;
	}

	// TODO----------------------------listener---------------------------------------

	public interface DragStatListener {
		public void close();

		public void open();

		public void draging();
	}

	public void setDragStatListener(DragStatListener listener) {
		this.mListener = listener;
	}

}
