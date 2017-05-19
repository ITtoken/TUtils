package com.tianjj.tutils.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressbar extends View {

	private int measuredWidth;
	private int measuredHeight;
	private float mProgress;
	private float mTextSize = 20;
	private RectF arcRect = null;
	private Rect textBounds;
	private Paint paintFont;
	private Paint paintCircle;

	private int mStrockWidth = 5;

	private int mTextColor;
	private int mCircleColor;

	public CircleProgressbar(Context context) {
		super(context);
		init();
	}

	public CircleProgressbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		paintFont = new Paint();
		paintFont.setAntiAlias(true);
		paintFont.setColor(mTextColor == 0 ? Color.DKGRAY : mTextColor);
		paintFont.setTextSize(mTextSize);

		paintCircle = new Paint();
		paintCircle.setAntiAlias(true);
		paintCircle.setColor(mCircleColor == 0 ? Color.DKGRAY : mTextColor);
		paintCircle.setStyle(Paint.Style.STROKE);
		paintCircle.setStrokeWidth(mStrockWidth);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		measuredWidth = getMeasuredWidth();
		measuredHeight = getMeasuredHeight();

		initDrawRange();
	}

	private void initDrawRange() {
		int arcRange = Math.min(measuredWidth, measuredHeight);
		arcRect = new RectF(mStrockWidth, (measuredHeight - (float) arcRange) / 2 + mStrockWidth,
				(float) arcRange - mStrockWidth, (measuredHeight + (float) arcRange) / 2 - mStrockWidth);
		textBounds = new Rect();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawArc(canvas);
		drawText(canvas);
	}

	private void drawText(Canvas canvas) {
		String progressText = mProgress + "%";
		paintFont.getTextBounds(progressText, 0, progressText.length(), textBounds);
		canvas.drawText(progressText, (measuredWidth - textBounds.width()) / 2.0f,
				(measuredHeight + textBounds.height()) / 2.0f, paintFont);

	}

	private void drawArc(Canvas canvas) {
		canvas.drawArc(arcRect, -90, 360 / 100.0f * mProgress, false, paintCircle);
	}

	private void refreshUI() {
		init();
		invalidate();
	}

	/**
	 * Set the progress.
	 * @param progress
	 */
	public void setProgress(float progress) {
		if (progress < 0) {
			progress = 0;
		} else if (progress > 100) {
			progress = 100;
		}
		mProgress = progress;
		invalidate();
	}

	/**
	 * Set the text size in middle of Circle.
	 * @param textSize
	 */
	public void setTextSize(float textSize) {
		if (textSize <= 0) {
			textSize = 20;// default size
		}
		mTextSize = textSize;
		refreshUI();
	}

	/**
	 * Set the circle's width
	 * @param circleWidth
	 */
	public void setCircleWidth(int circleWidth) {
		if (circleWidth < 0) {
			circleWidth = 0;
		}
		mStrockWidth = circleWidth;
		refreshUI();
	}

	/**
	 * Set the color of text and circle.
	 * @param textColor
	 * @param circleColor
	 */
	public void setColor(int textColor, int circleColor) {
		mTextColor = textColor == 0 ? Color.DKGRAY : textColor;
		mCircleColor = circleColor == 0 ? Color.DKGRAY : circleColor;
		refreshUI();
	}

}
