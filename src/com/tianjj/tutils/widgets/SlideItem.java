package com.tianjj.tutils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 
 * @author thunder
 * 
 *         <h3>example:</h3> 1_ get the instance of LeftSlide<br/>
 *         LeftSlide leftSlide = (LeftSlide) findViewById(R.id.leftSlide);</br>
 *         2_set listener to LeftSlide like follow example.
 *         <h6>leftSlide.addOnClickListener(new OnClickListener() {</br>
 * 
 * @Override public void onClick(WhitchBtnClicked whitchBtn) {</br>
 *           switch (whitchBtn) {</br>
 *           case ONE:</br>
 *           Toast.makeText(MainActivity.this, "button_main(1)", 0).show();</br>
 *           break;</br>
 *           case TWO:</br>
 *           Toast.makeText(MainActivity.this, "button_2", 0).show();</br>
 *           break;</br>
 *           case THREE:</br>
 *           Toast.makeText(MainActivity.this, "button_3", 0).show();</br>
 *           break;</br>
 *           }</br>
 *           }</br>
 *           });</h6></br>
 *
 */
public class SlideItem extends ViewGroup {

	public static final int ONE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	private int child1_width;
	private int child1_height;
	private int child2_width;
	private int child2_height;
	private int child3_width;
	private int child3_height;
	private View child1;
	private View child2;
	private View child3;
	private int down_x;
	private int zero_tag = 0;
	private OnClickListener listener;
	private boolean isShow = false;
	private int move_x;

	public SlideItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlideItem(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int d_x = 0;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			down_x = (int) ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int m_x = (int) ev.getX();
			int delta_x = (int) Math.abs(m_x - d_x);
			if (delta_x != 0) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		child1 = getChildAt(0);
		child2 = getChildAt(1);
		child3 = getChildAt(2);

		child1.measure(child1.getWidth(), child1.getHeight());
		child2.measure(child2.getWidth(), child2.getHeight());
		child3.measure(child3.getWidth(), child3.getHeight());

		child1_width = child1.getMeasuredWidth();
		child1_height = child1.getMeasuredHeight();
		child2_width = child2.getMeasuredWidth();
		child2_height = child2.getMeasuredHeight();
		child3_width = child3.getMeasuredWidth();
		child3_height = child3.getMeasuredHeight();

		child1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isShow) {
					close();
				} else {
					listener.onClick(ONE);
				}
			}
		});

		child2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(TWO);
				close();
			}
		});

		child3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(THREE);
				close();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(child1_width, child1_height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		child1.layout(l, t, child1_width, child1_height);
		child2.layout(child1_width, t, child1_width + child2_width, child2_height);
		child3.layout(child1_width + child2_width, t, child1_width + child2_width + child3_width, child3_height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			down_x = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			move_x = (int) event.getX();
			int delta_x = move_x - down_x;

			if (delta_x > 0) {// slide to right
				if (zero_tag <= 0) {
					zero_tag = 0;
					scrollTo(0, 0);
					return false;
				} else {
					zero_tag -= Math.abs(delta_x);
				}
			} else {// slide to left
				if (zero_tag >= (child2_width + child3_width)) {
					zero_tag = child2_width + child3_width;
					scrollTo(child2_width + child3_width, 0);
					return false;
				} else {
					zero_tag += Math.abs(delta_x);
				}
			}
			scrollBy(-delta_x, 0);

			down_x = move_x;
			break;
		case MotionEvent.ACTION_UP:
			if (zero_tag >= child2_width) {
				show();
			} else {
				close();
			}
			break;
		}
		return true;
	}

	/**
	 * the clicklistener for View or Layout
	 * 
	 * @author tianjj
	 *
	 */
	public interface OnClickListener {
		/**
		 * 
		 * @param whitchBtn
		 *            to represent whitch View is clicked currently
		 * 
		 * @see SlideItem
		 */
		public void onClick(int whitchBtn);
	}

	public void addOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}

	/**
	 * close the second view of item
	 */
	public void close() {
		ScrollAnimation scrollAnimation = new ScrollAnimation(this, 0);
		startAnimation(scrollAnimation);
		zero_tag = 0;// reinit zerotag
		isShow = false;
	}

	/**
	 * show the second view of item
	 */
	public void show() {
		ScrollAnimation scrollAnimation = new ScrollAnimation(this, child2_width + child3_width);
		startAnimation(scrollAnimation);
		zero_tag = child2_width + child3_width;// reinit zerotag
		isShow = true;
	}

	/**
	 * get the main(first) view of item
	 * 
	 * @return the main(first) view of item
	 */
	public View getManiLayout() {
		return child1;
	}

	/**
	 * whether the item's second view is show
	 * 
	 * @return true :the item's second view is show false :the item's second
	 *         view is not show
	 */
	public boolean isShow() {
		return isShow;
	}

	class ScrollAnimation extends Animation {

		private View view;
		private int startX;
		private float totalX;
		private int targetX;

		public ScrollAnimation(View view, int targetX) {
			super();
			this.view = view;
			startX = view.getScrollX();
			totalX = targetX - startX;

			setDuration((long) Math.abs(totalX * 2));
		}

		public ScrollAnimation() {
			super();
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			int scrollX = (int) (startX + totalX * interpolatedTime);
			view.scrollTo(scrollX, 0);
		}
	}

}
