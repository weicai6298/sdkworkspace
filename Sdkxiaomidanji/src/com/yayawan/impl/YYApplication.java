package com.yayawan.impl;


import com.xiaomi.hy.dj.HyDJ;

import android.app.Application;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	
	@Override
    public void onTerminate() {
        super.onTerminate();
        HyDJ.getInstance().onTerminate(this);
    }
}
