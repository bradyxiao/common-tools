package com.heqingwei.iflashlight;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import com.tencent.qcloud.flashligth.R;

public class IFLLightScreenActivity extends Activity {

	private static final String TAG = "IFLLightScreenActivity";
	
	public static final int MSG_SET_NEW_COLOR = 10;
	public static final int MSG_SET_BRIGHTESS = 11;
	public static final int MSG_ACTION_STOPED = 12;
	public static final int MSG_SET_AND_SABE_NEW_COLOR = 13;
	
	public static final float MAX_BRIGHTNESS = 255.0f;
	
	private FrameLayout mScreenColorLayout;
	private LinearLayout mSettingsLayout;
	private FrameLayout mDrawLayout;
	
	private IFLDrawView mDrawView;
	private boolean mIsDrawing = false; // 当前是否在绘图场景下
	
	private SeekBar mBrightnessSeekBar;
	private SeekBar mFlashFreqSeekBar;
	
	private ToggleButton mSosButton;
	private ToggleButton mFlashButton;
	private ToggleButton mDrawFlashButton;
	
	private IFLLightScreenContext mAppCtx;
	
	private int mBrightness;
	
	private int mSLVisibility = View.VISIBLE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_lightscreen);
		
		mAppCtx = new IFLLightScreenContext(this);
		
		mBrightness =mAppCtx.getLastBrightness();
		mAppCtx.setBrightness(mBrightness);
		
		initView();
	}
	
	private void initView() {
		mBrightnessSeekBar = (SeekBar) findViewById(R.id.brightness_seekbar);
		mBrightnessSeekBar.setProgress(mBrightness);
		mBrightnessSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mAppCtx.setBrightness(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				IFLConfig.saveLastBrightness(seekBar.getProgress());
			}
			
		});
		
		mFlashFreqSeekBar = (SeekBar) findViewById(R.id.flash_freq_seekbar);
		mFlashFreqSeekBar.setProgress(IFLConfig.getLastFTS());
		mFlashFreqSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				IFLConfig.saveLastFTS(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
		});
		
		mSettingsLayout = (LinearLayout) findViewById(R.id.settings_layout);
		mSettingsLayout.setVisibility(mSLVisibility);
		mScreenColorLayout = (FrameLayout) findViewById(R.id.lightscreen_layout);
		mScreenColorLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSLVisibility = mSLVisibility == View.VISIBLE ? View.INVISIBLE: View.VISIBLE;
				mSettingsLayout.setVisibility(mSLVisibility);
			}
			
		});
		setScreenColor(IFLConfig.getLastColor());
		
		OnClickListener setclrlistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				IFLUiHelper.pickColorDialog(IFLLightScreenActivity.this);
			}};
		
		findViewById(R.id.set_color_button).setOnClickListener(setclrlistener);
		findViewById(R.id.draw_color_button).setOnClickListener(setclrlistener);
		
		mSosButton = (ToggleButton) findViewById(R.id.sos_button);
		mSosButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IFLDeviceManager.getInstance().clickVibrate();
				sosFlash();
			}
			
		});
		
		mFlashButton = (ToggleButton) findViewById(R.id.flash_button);
		mDrawFlashButton = (ToggleButton) findViewById(R.id.draw_flash_button);
		OnClickListener flashlistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				flash();
			}};
		mFlashButton.setOnClickListener(flashlistener);
		mDrawFlashButton.setOnClickListener(flashlistener);
		
		
		mDrawLayout  = (FrameLayout) findViewById(R.id.draw_layout);
		mDrawLayout.findViewById(R.id.quit_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				mDrawLayout.setVisibility(View.GONE);
				mSettingsLayout.setVisibility(View.VISIBLE);
				mIsDrawing = false;
			}
		});
		
		mDrawView = (IFLDrawView)mDrawLayout.findViewById(R.id.draw_view);
		mDrawLayout.findViewById(R.id.redo_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				mDrawView.clean();
				mDrawView.stopFlash();
				mDrawFlashButton.setChecked(false);
			}
		});
		
		findViewById(R.id.draw_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				mDrawLayout.setVisibility(View.VISIBLE);
				mSettingsLayout.setVisibility(View.GONE);
				mIsDrawing = true;
			}});
	}
	
	private void sosFlash() {
		if(mSosButton.isChecked()) {
			 mAppCtx.startSOSFlash();
			 mFlashButton.setChecked(false);
			 mFlashFreqSeekBar.setVisibility(View.GONE);
			return;
		}
		mAppCtx.stopAction();
		Log.d(TAG, "stop sos");
	}
	
	private void flash() {
		if(mIsDrawing) {
			flashDrawBorad();
		} else {
			flashScreen();
		}
	}
	
	private void flashDrawBorad() {
		if(mDrawFlashButton.isChecked()) {
			mDrawView.flash();
		} else {
			mDrawView.stopFlash();
		}
	}
	
	private void flashScreen() {
		if(mFlashButton.isChecked()) {
			mFlashFreqSeekBar.setVisibility(View.VISIBLE);
			mAppCtx.flash();
			mSosButton.setChecked(false);
			return;
		}
		mAppCtx.stopAction();
		mFlashFreqSeekBar.setVisibility(View.GONE);
	}
	
	private static class IFLHandler extends Handler {};
	private Handler mHandler = new IFLHandler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_SET_NEW_COLOR:
				setScreenColor(msg.arg1);
				break;
			
			case MSG_SET_AND_SABE_NEW_COLOR:
				if(mIsDrawing) {
					mDrawView.setNewColor(msg.arg1);
				} else {
					IFLConfig.saveLastColor(msg.arg1);
					setScreenColor(msg.arg1);
				}
				break;
				
			case MSG_SET_BRIGHTESS:
				mAppCtx.setBrightness(msg.arg1);
				break;
				
			case MSG_ACTION_STOPED:
				setScreenColor(IFLConfig.getLastColor());
				break;
			}
		}};
	
	public Handler getHandler() {
		return mHandler;
	}
	
	private void setScreenColor(int color) {
		mScreenColorLayout.setBackgroundColor(color);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();	
		mAppCtx.stopAction();
	}
	
}
