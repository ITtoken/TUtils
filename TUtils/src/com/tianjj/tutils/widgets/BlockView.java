package com.tianjj.tutils.widgets;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

@SuppressLint("ClickableViewAccessibility")
public class BlockView extends SurfaceView implements Callback, Runnable, OnTouchListener {

	// Surface init
	private Thread mThread;
	public boolean isRunning = false;
	private SurfaceHolder holder;

	// Blocks
	private float[] cordinateXs;
	private float[] cordinateYs;
	private float mSurfaceWidth;
	private float mSurfaceHeight;
	private int move_X;
	private int move_Y;

	private List<Point> list = new ArrayList<Point>();
	private SparseArray<Rect> blocks = new SparseArray<Rect>();

	private boolean gridShow = false;
	private OnTapDownListener mListener;

	private static final int DOWN = 0;
	private static final int MOVE = 1;
	private static final int UP = 2;
	private int stat = -1;

	private Rect mRect;

	private int xBlocks = 10;
	private int yBlocks = 10;

	// Bitmaps
	ArrayList<Bitmap> mBitmaps;

	public BlockView(Context context) {
		this(context, null);
	}

	public BlockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		holder = getHolder();
		holder.addCallback(this);
		setOnTouchListener(this);
		mThread = new Thread(this);
	}

	@Override
	public void run() {
		doDraw(holder, getContext());
	}

	private void doDraw(SurfaceHolder holder, Context context) {
		Canvas canvas = null;
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);

		while (isRunning) {
			canvas = holder.lockCanvas();

			if (canvas != null) {
				// clear cavas
				canvas.drawColor(Color.WHITE);

				loopRender(canvas, paint);

				holder.unlockCanvasAndPost(canvas);
			}

			try {
				// Reduce frame
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void loopRender(Canvas canvas, Paint paint) {
		drawGrid(canvas, paint);
		drawBitmap(canvas, paint);
		drawTouchsShow(canvas, paint);
	}

	private void drawGrid(Canvas canvas, Paint paint) {
		if (gridShow) {
			// 绘制网格辅助线（临时，最后要去掉）---start---
			for (int i = 0; i < cordinateXs.length; i++) {
				canvas.drawLine(cordinateXs[i], 0, cordinateXs[i], mSurfaceHeight, paint);
			}

			for (int i = 0; i < cordinateYs.length; i++) {
				canvas.drawLine(0, cordinateYs[i], mSurfaceWidth, cordinateYs[i], paint);
			}
			// 绘制网格辅助线（临时，最后要去掉）---end---
		}
	}

	private void drawBitmap(Canvas canvas, Paint paint) {
		if (mBitmaps != null && mBitmaps.size() > 0) {
			int index = Math.min(mBitmaps.size(), blocks.size());
			for (int i = 0; i < index; i++) {
				Bitmap bitmap = mBitmaps.get(i);
				Rect rect = blocks.get(i);
				canvas.drawBitmap(bitmap, rect, rect, paint);
			}
		}
	}

	private void drawTouchsShow(Canvas canvas, Paint paint) {
		switch (stat) {
		case DOWN:
		case MOVE:
			System.out.println("mRect: " + mRect);
			if (null != mRect) {
				System.out.println("mRect into");
				paint.setAlpha(100);
				canvas.drawRect(mRect, paint);
			}
			break;
		case UP:
			if (null != mRect) {
				paint.setAlpha(0);
				canvas.drawRect(new Rect(mRect.left, mRect.top, mRect.left, mRect.top), paint);
			}
			break;

		default:
			break;
		}

		paint.setStrokeWidth(3);
		paint.setColor(Color.RED);
		for (int i = 0; i < list.size(); i++) {
			canvas.drawPoint(list.get(i).x, list.get(i).y, paint);
		}
	}

	private void initBlockArea() {
		float cordinateX = 0.0f;
		float cordinateY = 0.0f;
		// 每个像素块的宽和高
		float blockW = mSurfaceWidth / xBlocks;
		float blockH = mSurfaceHeight / yBlocks;

		// 存储X轴和Y轴的坐标信息
		cordinateXs = new float[xBlocks + 1];
		cordinateYs = new float[yBlocks + 1];
		for (int i = 0; i < cordinateXs.length; i++) {
			cordinateXs[i] = cordinateX;
			cordinateX += blockW;
			if (cordinateX > mSurfaceWidth) {
				cordinateX = mSurfaceWidth;
				cordinateXs[i] = cordinateX;
			}
		}

		for (int i = 0; i < cordinateYs.length; i++) {
			cordinateYs[i] = cordinateY;
			cordinateY += blockH;
			if (cordinateY > mSurfaceHeight) {
				cordinateY = mSurfaceHeight;
				cordinateYs[i] = cordinateY;
			}
		}

		// 将块信息存储到集合中，并编号
		int blockNum = 0;
		for (int i = 0; i < cordinateYs.length - 1; i++) {
			for (int j = 0; j < cordinateXs.length - 1; j++) {
				blocks.append(blockNum, new Rect((int) cordinateXs[j], (int) cordinateYs[i], (int) cordinateXs[j + 1],
						(int) cordinateYs[i + 1]));
				blockNum++;
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!isRunning) {
			isRunning = true;
			mThread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		this.mSurfaceWidth = width;
		this.mSurfaceHeight = height;

		initBlockArea();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (isRunning) {
				isRunning = false;
				mThread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("surface ending===");
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			move_X = (int) event.getX();
			move_Y = (int) event.getY();
			for (int i = 0; i < blocks.size(); i++) {
				Rect rect = blocks.get(i);
				if (move_X < rect.right && move_X > rect.left && move_Y < rect.bottom && move_Y > rect.top) {
					stat = DOWN;
					mRect = rect;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			int uX = (int) event.getX();
			int uY = (int) event.getY();
			for (int i = 0; i < blocks.size(); i++) {
				Rect rect = blocks.get(i);
				if (uX < rect.right && uX > rect.left && uY < rect.bottom && uY > rect.top) {
					// System.out.println(blocks.keyAt(i));
					if (mListener != null) {
						mListener.onTap(blocks.keyAt(i), rect);
					}
					stat = UP;
					mRect = rect;
				}
			}
			break;

		default:
			break;
		}
		return true;
	}

	public interface OnTapDownListener {
		public void onTap(int whitchBlock, Rect rect);
	}

	public void setOnTapDownListener(OnTapDownListener listener) {
		mListener = listener;
	}

	public void gridToShow(boolean toShow) {
		gridShow = toShow;
	}

	public SparseArray<Rect> getAllBlocksRect() {
		return blocks;
	}

	public void loadImage(ArrayList<Bitmap> bmps) {
		mBitmaps = bmps;
	}

}
