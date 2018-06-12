package com.heqingwei.iflashlight;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class IFLSysUtil {
	
	public static String getVersionName(Context ctx){
        try { 
        	PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        	return info.versionName;
        } catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
			return "";
		} 
	}
	
 	public static void installApk(Context ctx, final String apkFilePath) {
 		
 		File apkfile = new File(apkFilePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive"); 
        ctx.startActivity(i);
 	}
}
