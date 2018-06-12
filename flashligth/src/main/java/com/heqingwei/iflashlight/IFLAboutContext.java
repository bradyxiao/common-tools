package com.heqingwei.iflashlight;

import com.common.update.UpdateManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

public class IFLAboutContext {
	
	private Context mCtx;
	private UpdateManager mUpdateManager;
	
	public IFLAboutContext(Context ctx) {
		mCtx = ctx;
		mUpdateManager = new UpdateManager(ctx);
	}
	
	public void gotoRank() {
		Uri uri = Uri.parse("market://details?id=" + mCtx.getPackageName()); 
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mCtx.startActivity(intent); 
	}
	
	public void checkUpdate(Handler handler) {
		mUpdateManager.checkUpdate(handler);
	}
	
	public void doUpdate() {
		mUpdateManager.downloadApk();
	}
}
