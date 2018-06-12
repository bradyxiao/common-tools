package com.heqingwei.iflashlight;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.WindowManager;

public class IFLLightScreenContext {
	
	protected static final String TAG = "IFLLightScreenContext";
	
	private Thread mActionThread;
	private boolean mTerminated;
	
	private Context mCtx;
	private Handler mHandler;
	
	public IFLLightScreenContext(Context ctx) {
		mCtx = ctx;
		mHandler = ((IFLLightScreenActivity)mCtx).getHandler();
	}
	
	public void startSOSFlash() {
		stopAction();
		
		mActionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "sos thread starts");
				while(!Thread.interrupted() && !mTerminated) {
					for(int t: IFLConfig.SOS_DELAY_TIMES) {
						mHandler.obtainMessage(IFLLightScreenActivity.MSG_SET_NEW_COLOR, IFLConfig.getLastColor(), 0).sendToTarget();
						try {
							Thread.sleep(t);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							break;
						}
						mHandler.obtainMessage(IFLLightScreenActivity.MSG_SET_NEW_COLOR, 0, 0).sendToTarget();
						try {
							Thread.sleep(t);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							break;
						}
					}
					if(mTerminated) {
						break;
					}
					mHandler.obtainMessage(IFLLightScreenActivity.MSG_SET_NEW_COLOR, 0, 0).sendToTarget();
					try {
						Thread.sleep(IFLConfig.MORSE_DELAY_DI * 7);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						break;
					}
				}
				Log.d(TAG, "sos thread stoped");
				mHandler.sendEmptyMessage(IFLLightScreenActivity.MSG_ACTION_STOPED);
			}});
		
		mTerminated = false;
		mActionThread.start();
	}
	
	public void flash() {
		stopAction();
		mActionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "flash thread starts");
				while(!Thread.interrupted() && !mTerminated) {
					mHandler.obtainMessage(IFLLightScreenActivity.MSG_SET_NEW_COLOR, 0, 0).sendToTarget();
					int delay = 1000 / (IFLConfig.getLastFTS() + 1);
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						break;
					}
					mHandler.obtainMessage(IFLLightScreenActivity.MSG_SET_NEW_COLOR, IFLConfig.getLastColor(), 0).sendToTarget();
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
						break;
					}
				}
				Log.d(TAG, "flash thread stoped");
				mHandler.sendEmptyMessage(IFLLightScreenActivity.MSG_ACTION_STOPED);
			}});
		
		mTerminated = false;
		mActionThread.start();
	}
	
	public void stopAction() {
		//IFLUiHelper.toastMsg(mCtx, mCtx.getResources().getString(R.string.stopping_sos_flash));
		if(!mTerminated && mActionThread != null ) {
			mActionThread.interrupt();
			mTerminated = true;
			mActionThread = null;
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getLastBrightness() {
		int defaultBrightness = (int)IFLLightScreenActivity.MAX_BRIGHTNESS / 2;
		try {
			defaultBrightness =  android.provider.Settings.System.getInt(mCtx.getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return IFLConfig.getLastBrightness(defaultBrightness);
	}
	
	public void setBrightness(int brightness) {
	    WindowManager.LayoutParams layoutParams = ((IFLLightScreenActivity)mCtx).getWindow().getAttributes();
	    layoutParams.screenBrightness = brightness / IFLLightScreenActivity.MAX_BRIGHTNESS;
	    ((IFLLightScreenActivity)mCtx).getWindow().setAttributes(layoutParams);
	}
	
}
