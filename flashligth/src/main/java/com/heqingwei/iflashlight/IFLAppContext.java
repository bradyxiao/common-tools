package com.heqingwei.iflashlight;

import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;

public class IFLAppContext {
	
	private static final String TAG = "IFLAppContext";
	
	private Context mMainCtx;
	private Camera mCamera;
	
	public IFLAppContext(Context ctx) {
		mMainCtx = ctx;
	}
	
	public Boolean deviceHasFlashlight() {
		PackageManager packageManager = mMainCtx.getPackageManager();
		return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}
	
	public boolean turnOnFlashLight(SurfaceHolder holder) {
		if (!deviceHasFlashlight()) {
			return false;
		}
		
		turnOffFlashLight();
		
		if (mCamera == null) {
			try {
				mCamera = Camera.open();
			} catch(RuntimeException e) {
				return false;
			}
			if(mCamera == null) {
				Log.e(TAG, "open camera failed!");
				return false;
			}
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			mCamera.startPreview();
		}

		Parameters parameters = mCamera.getParameters();
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(parameters);
		
		return true;
	}
	
	public void turnOffFlashLight() {
		if (mCamera != null) {
			Parameters parameters = mCamera.getParameters();
			if (parameters.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			}
		}
	}
	
	public void turnFlashLight(boolean on, SurfaceHolder holder) {
		if(on) {
			turnOnFlashLight(holder);
		} else {
			turnOffFlashLight();
		}
	}
	
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
}
