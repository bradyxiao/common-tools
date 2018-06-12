package com.heqingwei.iflashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

public class IFLConfig {
	
	public static final String APP_PREFIX = "IFlashLight";
	
	public static final String LATEST_VERSION_DOWNLOAD_URL = "http://61.151.217.100/app_update/iflashlight/iflashlight.apk";
	
	public static final String LAST_COLOR_KEY = "lastcolor";
	public static final String LAST_BRIGHTNESS = "lastbrightness";
	public static final String LAST_FLASH_TIMES_PER_S = "lastflashtimespers";
	public static final String AUTO_OFF_TIME = "autoofftime";
	public static final String AUTO_LIGHT_ON = "autolighton";
	
	public static final int DEFAULT_SCREEN_COLOR = Color.parseColor("#ff00ff00");
	
	public static final int MIN_AUTO_OFF_MIN = 1;
	
	public static final int MAIN_TOOLBAR_BG_COLOR = 0xff1E90FF;
	
	public static final int DRAW_COLOR = Color.parseColor("#ff00ff00");
	public static final int STROKE_WIDTH = 50;
	public static final int DRAW_FLASH_INTERVAL = 500;
	
	public static final int[] COMMD_COLOR_VALUES = {0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFFFF};
	
	public static final int MORSE_DELAY_DI = 100;
	public static final int MORSE_DELAY_DA = MORSE_DELAY_DI * 3;
	public static final int[] SOS_DELAY_TIMES = {MORSE_DELAY_DI, MORSE_DELAY_DI, MORSE_DELAY_DI, 
																		MORSE_DELAY_DA, MORSE_DELAY_DA, MORSE_DELAY_DA, 
																		MORSE_DELAY_DI, MORSE_DELAY_DI, MORSE_DELAY_DI  };
	
	private static SharedPreferences mShPf = IFLApplication.getInstance().getSharedPreferences("data", Context.MODE_PRIVATE);
	private static Editor mShPfEditor = mShPf.edit();
	
	
	private static int getInt(String key, int defaultVal) {
		return mShPf.getInt(key, defaultVal);
	}
	private static void setInt(String key, int value) {
		mShPfEditor.putInt(key, value);
		mShPfEditor.commit();
	}
	
	public static void saveLastColor(int color) {
		setInt(LAST_COLOR_KEY, color);
	}
	public static int getLastColor() {
		return getInt(LAST_COLOR_KEY, DEFAULT_SCREEN_COLOR);
	}
	
	public static void saveLastBrightness(int brightness) {
		setInt(LAST_BRIGHTNESS, brightness);
	}
	public static int getLastBrightness(int defaultBrightness) {
		return getInt(LAST_BRIGHTNESS, defaultBrightness);
	}
	
	public static void saveLastFTS(int times) {
		setInt(LAST_FLASH_TIMES_PER_S, times);
	}
	public synchronized  static int getLastFTS() {
		return getInt(LAST_FLASH_TIMES_PER_S, 10);
	}
	
	public static void setAutoOffTime(int min) {
		setInt(AUTO_OFF_TIME, min); // min为0时表示不允许自动关闭
	}
	public static int getAutoOffTime() {
		return getInt(AUTO_OFF_TIME, 0); // 默认不允许自动关闭
	}
	
	public static void setAutoLightOn(boolean on) {
		setInt(AUTO_LIGHT_ON, on ? 1 : 0); 
	}
	public static boolean isAutoLightOn() {
		return getInt(AUTO_LIGHT_ON , 0) == 1;
	}
}
