package com.tianjj.tutils.widgets;

import java.util.List;

import com.tianjj.tutils.helper.QuickIndexHelper;
import com.tianjj.tutils.widgets.QuickIndexView.OnLetterChosenListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class QuickIndexListview extends FrameLayout {

	private ListView mListView;
	private QuickIndexView mQuickIndex;
	private String mIndexChar;
	private int mListViewWidth;
	private int mListViewHeight;
	private int mQuickIndexWidth;
	private int mQuickIndexHeight;
	private QuickListAdapter mAdapter;
	private List<String> mOrderedList;

	public QuickIndexListview(Context context) {
		super(context, null);
	}

	public QuickIndexListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mListView = (ListView) getChildAt(0);
		mQuickIndex = (QuickIndexView) getChildAt(1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mListViewWidth = getMeasuredWidth();
		mListViewHeight = getMeasuredHeight();

		mQuickIndexWidth = mQuickIndex.getLayoutParams().width;
		mQuickIndexHeight = mListViewHeight;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mListView.layout(l, t, r, b);
		mQuickIndex.layout(mListViewWidth - mQuickIndexWidth, t, r, b);
	}

	/**
	 * {@link QuickListView} Listener
	 * 
	 * @author Tianjj
	 *
	 */
	public abstract class QuickListAdapter {
		/**
		 * Get the item count that to show.
		 * 
		 * @return
		 */
		public int getCount() {
			if (mOrderedList != null) {
				return mOrderedList.size();
			}
			return 0;
		}

		/**
		 * Get the item's View.
		 * 
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		public abstract View getView(int position, View convertView, ViewGroup parent, List<String> orderedList);
	}

	private class myAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAdapter.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mAdapter.getView(position, convertView, parent, mOrderedList);
		}

	}
	
	//External insterfaces------------------------------------------------------
	/**
	 * Set the item's content. Must called before <b>setAdapter(QuickListAdapter adapter)</b>
	 * 
	 * @param mItemTests
	 */
	public void setItemText(String... mItemTests) {
		if (mItemTests == null || mItemTests.length == 0) {
			throw new RuntimeException("Param's value must not be NULL!");
		}
		mOrderedList = QuickIndexHelper.getOrderedList(mItemTests);
	}
	
	/**
	 *Set the item's content of <b>QuickListView</b>
	 * @param adapter
	 */
	public void setAdapter(QuickListAdapter adapter) {
		mAdapter = adapter;
		mListView.setAdapter(new myAdapter());
		mQuickIndex.setOnLetterChosenListener(new OnLetterChosenListener() {
			@Override
			public void chooseLetter(String letter) {
				mIndexChar = letter;

				if (mOrderedList == null) {
					throw new RuntimeException("Method 'setItemText(mItemTests2)' must called and before setAdapter");
				}
				int position = QuickIndexHelper.getLetterPosition(letter, mOrderedList);
				mListView.setSelection(position);
			}
		});
	}

}
