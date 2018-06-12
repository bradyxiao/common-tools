package com.heqingwei.iflashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.tencent.qcloud.flashligth.R;

public class IFLSettingsActivity extends Activity {
	
	private LinearLayout mAutoOffTimeLayout;
	private SeekBar mAutoOffSeekBar;
	private TextView mAutoOffTimeInfo;
	private CheckBox mAutoLightOnCkBox;
	private CheckBox mAutoOffCkBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		initView();
		
	}
	
	private void initView() {
		mAutoLightOnCkBox = (CheckBox) findViewById(R.id.auto_light_on_checkbox);
		mAutoLightOnCkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				IFLConfig.setAutoLightOn(isChecked);
			}});
		mAutoLightOnCkBox.setChecked(IFLConfig.isAutoLightOn());
		
		mAutoOffTimeLayout = (LinearLayout) findViewById(R.id.auto_off_time_layout);
		mAutoOffCkBox = (CheckBox) findViewById(R.id.auto_turn_off_checkbox);
		mAutoOffCkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mAutoOffTimeLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				if(!isChecked) {
					IFLConfig.setAutoOffTime(0);
				}
			}});
		mAutoOffCkBox.setChecked(IFLConfig.getAutoOffTime() > 0);
		
		int autoOffTime = IFLConfig.getAutoOffTime() + IFLConfig.MIN_AUTO_OFF_MIN;
		
		mAutoOffTimeInfo = (TextView) findViewById(R.id.auto_off_time_info);
		mAutoOffTimeInfo.setText(autoOffTime + "分钟");
		
		mAutoOffSeekBar = (SeekBar) findViewById(R.id.auto_off_seekbar);
		mAutoOffSeekBar.setProgress(autoOffTime);
		mAutoOffSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mAutoOffTimeInfo.setText(progress + IFLConfig.MIN_AUTO_OFF_MIN + "分钟");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
					IFLConfig.setAutoOffTime(seekBar.getProgress() + IFLConfig.MIN_AUTO_OFF_MIN);
			}});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
}
