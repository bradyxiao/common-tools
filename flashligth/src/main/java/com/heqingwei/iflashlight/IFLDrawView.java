package com.heqingwei.iflashlight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class IFLDrawView extends View {
	
	protected static final String TAG = "IFLDrawView";
	
	private static final int MSG_SET_COLOR = 10;

	private Thread mFlashThread;
	private boolean mTerminated = false;
	
	private int mColor;
	private int mStrokeWidth;
	
	private Path mTPath;
	private Paint mTPaint;
	
	private static class MyHandler extends Handler{};
	private Handler mHandler = new MyHandler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_SET_COLOR:
				setNewColor_NoKeep(msg.arg1);
				break;
			}
		}
		
	};
	
	private Runnable mFlashRunnable = new Runnable() {

		@Override
		public void run() {
			mTerminated = false;
			while(!Thread.interrupted() && !mTerminated) {
				mHandler.obtainMessage(MSG_SET_COLOR, 0, 0).sendToTarget();
				try {
					Thread.sleep(IFLConfig.DRAW_FLASH_INTERVAL);
				} catch (InterruptedException e) {
					break;
				}
				mHandler.obtainMessage(MSG_SET_COLOR, mColor, 0).sendToTarget();
				try {
					Thread.sleep(IFLConfig.DRAW_FLASH_INTERVAL);
				} catch (InterruptedException e) {
					break;
				}
			}
			Log.d(TAG, "draw flash thread stoped!");
		}}; 
	
	public IFLDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTPath = new Path();
		mTPaint = new Paint();
		initDrawView(IFLConfig.DRAW_COLOR, IFLConfig.STROKE_WIDTH);
	}
	
	public void initDrawView(int color, int strokeWidth) {
		mColor = color;
		Log.d(TAG, "draw color init as " + mColor);
		mStrokeWidth = strokeWidth;
		mTPaint.setColor(color);
		mTPaint.setAntiAlias(true);
		mTPaint.setStrokeWidth(mStrokeWidth);
		mTPaint.setStyle(Paint.Style.STROKE);
		mTPaint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawPath(mTPath, mTPaint);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTPath.moveTo(event.getX(),event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			mTPath.lineTo(event.getX(),event.getY());
			invalidate();
			break;
		}
		return true;
	}
	
	public void clean() {
		mTPath.reset();
		invalidate();
	}
	
	public void setNewColor(int color) {
		mColor = color;
		mTPaint.setColor(color);
		invalidate();
	}
	
	public void setNewColor_NoKeep(int color) {
		mTPaint.setColor(color);
		invalidate();
	}
	
	public void flash() {
		mFlashThread = new Thread(mFlashRunnable);
		mFlashThread.start();
	}
	
	public void stopFlash() {
		if(mFlashThread == null) {
			return;
		}
		mFlashThread.interrupt();
		mTerminated = false;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mFlashThread = null;
		Log.d(TAG, "last draw color is " + mColor);
		setNewColor(mColor);
	}
	
}
