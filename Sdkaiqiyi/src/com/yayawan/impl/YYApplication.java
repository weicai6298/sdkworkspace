package com.yayawan.impl;


import com.iqiyi.sdk.platform.GamePlatform;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	
	@Override
    protected void attachBaseContext(Context base) {
        Log.i("tag", "attachBaseContext: ");
        super.attachBaseContext(base);
        GamePlatform.getInstance().initApplication(this);
    }
}
