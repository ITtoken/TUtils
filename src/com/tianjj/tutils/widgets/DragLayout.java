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

	/**
	 * 该方法在onMeasure方法之后调用
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mScreenWidth = getMeasuredWidth();
		mScreenHeight = getMeasuredHeight();
		mMainMaxLeft = mScreenWidth * 0.6f;
	}

	// TODO----------------------------private---------------------------------------
	private void init(Context context) {
		// 第一步:创建ViewDragHelper
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

		// 第三部:实现相应的回调方法

		/**
		 * 根据返回结果，决定当前触摸的 view是否可以拖拽 view:当前被拖拽的view pointerId:区分多点触摸的id
		 */
		@Override
		public boolean tryCaptureView(View view, int pointerId) {
			// return view == main;// 只有住布局可以拖拽移动
			return true;// 全部子布局都可以拖拽移动
		}

		/**
		 * 根据建议值修正将要移动的位置
		 * 
		 * ::返回的值决定当前view可以被拖到那个位置为止(之后将不能再继续拖拽)
		 * 
		 * left:滑动view距离左边的距离 dx:滑动的偏移量(滑动越快,便宜量的绝对值越大),过dx可以获得滑动的方向，（向右>0,向左<0）
		 */
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (child == mMain_ViewGroup) {
				left = fixLeft(left);
			}
			return left;
		}

		/**
		 * 获取可触摸的范围,根据返回值决定可以触发拖拽事件的范围,没有被指定(返回)的范围,拖拽将不被触发.
		 * 
		 * 注意:
		 * 是根据getViewHorizontalDragRange和getViewVerticalDragRange两个方法的返回值交集的范围(
		 * 是可触范围)
		 */
		public int getViewHorizontalDragRange(View child) {
			return getMeasuredWidth();
		};

		/**
		 * 当capturedChild(将要拖拽的布局)被捕获时调用
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
		}

		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
		}

		/**
		 * 当拖拽位置变化时调用(一般对一些view的状态如透明度,大小等进行"渐变"设置)
		 * 
		 * left:被拖拽控件距离左边的距离(相当于view.getLeft())
		 * 
		 * top:被拖拽控件距离上边的距离(相当于view.getLeft())
		 */
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
			invalidate();// 注意:为了兼容低版本,一定要调用这个(在低版本上没有主动调用invalidate重绘,而高版本上面有)
		}

		/**
		 * 在释放releasedChild(拖拽的view)时调用,一般执行一些动画 xvel:水平方向的速度(左负值,右正值)
		 * 
		 * yvel:垂直方向的速度(下正值,上负值)
		 * 
		 */
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
			// 注意:知识触发一个平滑动画,而不是执行,返回true代表还没有移动到指定位置,还需要继续滑动
			boolean slideViewTo = mViewDragHelper.smoothSlideViewTo(mMain_ViewGroup, finalLeft, 0);
			if (slideViewTo) {
				// 执行动画重绘操作，参数为当前要滑动的view的父布局
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
			// 注意:知识触发一个平滑动画,而不是执行,返回true代表还没有移动到指定位置,还需要继续滑动
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
		// continueSettling方法要是在computeScroll中调用，就一定要传一个true
		if (mViewDragHelper.continueSettling(true)) {
			// 返回true,表示动画还需要继续执行
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	// 第二部:传递触摸事件,将触摸事件交给ViewDragHelper处理
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
