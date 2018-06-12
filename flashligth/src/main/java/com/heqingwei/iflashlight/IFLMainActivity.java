package com.heqingwei.iflashlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ToggleButton;

import com.tencent.qcloud.flashligth.R;

public class IFLMainActivity extends Activity implements OnMenuItemClickListener {
	
	public static final int MSG_SET_NEW_COLOR = 10;

	private static final String TAG = "IFLMainActivity";
	
	private IFLAppContext mAppCtx;
	private ToggleButton mSwitchButton;
	private SurfaceView mPreview;
	private SurfaceHolder mHolder;
	private LinearLayout mMainLayout;
	
	private Handler mHandler = new Handler() ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAppCtx = new IFLAppContext(this);
		
		initView();
		
	//	autoTurnLight();
	}
	
	@SuppressLint("NewApi")
	private void initView() {
		mMainLayout = (LinearLayout) findViewById(R.id.main_layout);
		mMainLayout.setBackground(IFLPhotoUtil.bitmap2Gray(IFLMainActivity.this,  R.drawable.light_on_bg));
		
		mPreview = (SurfaceView) findViewById(R.id.preview);
		mHolder = mPreview.getHolder();
		mSwitchButton = (ToggleButton) findViewById(R.id.switch_button);
		mSwitchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().playSound(IFLMainActivity.this, R.raw.key);
				turnOnOffFLashLight();
			}
			
		});
		
		findViewById(R.id.light_screen_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IFLDeviceManager.getInstance().clickVibrate();
				startLightScreenActivity();
			}
		});
		
		findViewById(R.id.more_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showBottomPopMenu(v);
			}
			
		});
	}

	@SuppressLint("NewApi")
	private void showBottomPopMenu(View v) {
		 PopupMenu popup = new PopupMenu(this, v);
		 popup.setOnMenuItemClickListener(this);
		 popup.inflate(R.menu.main);
		 popup.show();
	}
	
	private void autoTurnLight() {
		if(!IFLConfig.isAutoLightOn()) {
			return;
		}


		// 用户设置了启动时打开手电
		Log.d(TAG, "auto light on");
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mSwitchButton.setChecked(true);
				turnOnOffFLashLight();
			}}, 50);
	}
	
	@SuppressLint("NewApi")
	private void turnOnOffFLashLight() {
		if(mSwitchButton.isChecked()) {
			mMainLayout.setBackgroundResource( R.drawable.light_on_bg );
			findViewById(R.id.toolbar_layout).setBackgroundColor(0x00ffffff);
		} else {
			mMainLayout.setBackground(IFLPhotoUtil.bitmap2Gray(IFLMainActivity.this,  R.drawable.light_on_bg));
			findViewById(R.id.toolbar_layout).setBackgroundColor(IFLConfig.MAIN_TOOLBAR_BG_COLOR);
		}
		mAppCtx.turnFlashLight(mSwitchButton.isChecked(), mHolder);
	}
	
	private void startLightScreenActivity() {
		this.startActivity(new Intent(this, IFLLightScreenActivity.class));
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mSwitchButton.setChecked(false);
		mAppCtx.turnOffFlashLight();
		mAppCtx.releaseCamera();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_about:
			startActivity(new Intent(this, IFLAboutActivity.class));
			break;
		case R.id.menu_settings:
			startActivity(new Intent(this, IFLSettingsActivity.class));
			break;
		}
		return false;
	}
}
