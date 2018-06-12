package com.heqingwei.iflashlight;


import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Vibrator;

public class IFLDeviceManager extends Application {

	private Vibrator mVibrator;
	private SoundPool mSoundPool;
	private static IFLDeviceManager mInstance = null;
	
	private IFLDeviceManager() {
		initAll();
	}

	public static final IFLDeviceManager getInstance() {
		if(mInstance == null) {
			mInstance = new IFLDeviceManager(); 
		}
		return mInstance ;
	}  
	
	private void initAll() {
//		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				mSoundPool.play(sampleId, 1, 1, 0, 0, 1);
			}

		});
	}
	
	public void vibrate(long [] pattern) {
//		mVibrator.vibrate(pattern, -1);
	}

	public void clickVibrate() {
		vibrate(new long[] {0, 50});
	}

	public void playSound(Context ctx, final int resId) {
		if(resId < 0) {
			return;
		}
		mSoundPool.load(ctx, resId, 0);
	}
}