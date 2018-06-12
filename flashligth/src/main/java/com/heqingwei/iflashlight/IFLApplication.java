package com.heqingwei.iflashlight;

import android.app.Application;
import android.widget.Toast;

public class IFLApplication extends Application {
	
    private static IFLApplication instance;

    public static IFLApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    
    public void debug(final String msg) {
    	if(msg != null) {
    		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    	}
    }
}