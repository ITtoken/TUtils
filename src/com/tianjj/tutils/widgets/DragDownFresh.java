package com.tianjj.tutils.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DragDownFresh extends ViewGroup {

	private View topView;
	private int topView_height;
	private int down_y;
	private boolean isShow;
	private OnRefreshListener listener;
	private ListView listView;
	private View footer;
	private Context context;

	public DragDownFresh(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public DragDownFresh(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		topView = getChildAt(0);
		listView = (ListView) getChildAt(1);
		footer = getChildAt(2);

		removeView(footer);
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.addView(footer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		listView.addFooterView(linearLayout);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			down_y = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int move_y = (int) ev.getY();
			int delta_y = move_y - down_y;
			if (delta_y > 0 && listView.getFirstVisiblePosition() == 0) {
				return true;
			} else if (delta_y <= 0 && listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
				listener.footerShow();
			} else {
				listener.footerCancel();
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		topView.measure(widthMeasureSpec, topView.getHeight() + 5);
		listView.measure(widthMeasureSpec, heightMeasureSpec);

		topView_height = topView.getMeasuredHeight();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		topView.layout(l, -topView_height - 5, r, 0);
		listView.layout(l, t, r, b);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			down_y = (int) event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			int move_y = (int) event.getY();
			int delta_y = move_y - down_y;

			if (delta_y > 0) {
				if (getScrollY() <= -topView_height) {
					setScrollY(-topView_height);
					return false;
				}

				if (getScrollY() == -topView_height / 2) {
					listener.onStartRefresh();
				}
			} else if (delta_y < 0) {
				if (getScrollY() >= 0) {
					setScrollY(0);
					return false;
				}

				if (getScrollY() == -topView_height / 2) {
					listener.cancelRefresh();
				}
			}

			scrollBy(0, -delta_y / 2);
			down_y = move_y;
			break;

		case MotionEvent.ACTION_UP:
			if (getScrollY() <= -topView_height / 2) {
				show();
			} else {
				close();
			}
			break;
		}
		return true;
	}

	/**
	 * show top view to start refresh
	 */
	public void show() {
		ScrollAnimation scrollAnimation = new ScrollAnimation(this, -topView_height);
		startAnimation(scrollAnimation);
		isShow = true;

		listener.onRefreshing();
	}

	/**
	 * get the status about whether top view is show
	 * 
	 * @return
	 */
	public boolean isShow() {
		return isShow;
	}

	/**
	 * show top view to start refresh
	 */
	public void close() {
		ScrollAnimation scrollAnimation = new ScrollAnimation(this, 0);
		startAnimation(scrollAnimation);
		isShow = false;

		listener.cancelRefresh();
	}

	/**
	 * get the object of the top view
	 * 
	 * @return
	 */
	public View getTopView() {
		return topView;
	}

	/**
	 * get the object of the expand listview
	 * 
	 * @return
	 */
	public ListView getListView() {
		// return expandListView;
		return listView;
	}

	/**
	 * get the object of the footer view
	 * 
	 * @return
	 */
	public View getFooterView() {
		// return expandListView;
		return footer;
	}

	/**
	 * a listener when refresh status have change
	 * 
	 * @author thunder
	 *
	 */
	public interface OnRefreshListener {
		/**
		 * called when start refresh
		 */
		public void onStartRefresh();

		/**
		 * called when refreshing
		 */
		public void onRefreshing();

		/**
		 * called when cancel refresh
		 */
		public void cancelRefresh();

		/**
		 * called when footer view is show
		 */
		public void footerShow();

		/**
		 * called when cancel footer view
		 */
		public void footerCancel();
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	class ScrollAnimation extends Animation {
		View view;
		int startY;
		int targetY;
		int totalY;

		public ScrollAnimation(View view, int targetY) {
			super();
			this.view = view;
			this.targetY = targetY;

			startY = view.getScrollY();
			totalY = targetY - startY;

			setDuration(150);
			setInterpolator(new OvershootInterpolator());
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			int scrollY = (int) (startY + totalY * interpolatedTime);
			scrollTo(0, scrollY);
		}
	}

}
