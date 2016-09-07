package com.tianjj.tutils.widgets;

import com.tianjj.tutils.helper.QuickIndexHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickIndexView extends View {

	private Paint mPaint;
	private int mTextWidth;
	private int mTextHeight;
	private float mTextPosX;
	private float mTextPosY;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private float perHeight;
	private int mCurrentLetterIndex = -1;
	private int mFocusIndex = -1;
	private OnLetterChosenListener mListener;

	private static final String[] LETTERS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };

	public QuickIndexView(Context context) {
		super(context, null);
	}

	public QuickIndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < LETTERS.length; i++) {
			initPaint(i);
			getLetterPos(i);

			if (i == mFocusIndex) {
				mPaint.setColor(Color.DKGRAY);
			}

			canvas.drawText(LETTERS[i], mTextPosX, mTextPosY, mPaint);
		}

	}

	/**
	 * 获取每一个字母所在的位置
	 * 
	 * @param i
	 *            第i个字母
	 */
	private void getLetterPos(int i) {
		mTextPosX = mMeasuredWidth / 2 - mTextWidth / 2;
		perHeight = mMeasuredHeight / LETTERS.length;
		float perLetterPos = (perHeight + mTextHeight) / 2;
		mTextPosY = perHeight * i + perLetterPos;
	}

	/**
	 * 初始化画笔
	 * 
	 * @param i
	 *            第i个字母的画壁
	 */
	private void initPaint(int i) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(15);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);

		getTextBounds(LETTERS[i]);
	}

	/**
	 * 获取文本所在的矩形大小
	 * 
	 * @param text
	 *            对应的文本
	 */
	private void getTextBounds(String text) {
		Rect rect = new Rect();
		mPaint.getTextBounds(text, 0, text.length(), rect);
		mTextWidth = rect.width();
		mTextHeight = rect.height();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			float dy = event.getY();
			int index = (int) (dy / perHeight);
			if (index != mCurrentLetterIndex && index < LETTERS.length) {
				mCurrentLetterIndex = index;
				mFocusIndex = mCurrentLetterIndex;
				mListener.chooseLetter(LETTERS[mCurrentLetterIndex]);
			}
			break;
		case MotionEvent.ACTION_UP:
			mFocusIndex = -1;
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mMeasuredWidth = getMeasuredWidth();
		mMeasuredHeight = getMeasuredHeight();
	}

	public interface OnLetterChosenListener {
		public void chooseLetter(String letter);
	}

	public void setOnLetterChosenListener(OnLetterChosenListener listener) {
		mListener = listener;
	}

	public void setFocusLetter(int position) {
		mFocusIndex = position;
		invalidate();
	}

	public void setFocusLetter(String letter) {
		mFocusIndex = QuickIndexHelper.getLetterPosition(letter, LETTERS);
		invalidate();
	}

}
