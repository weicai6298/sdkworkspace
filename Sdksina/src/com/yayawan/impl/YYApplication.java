package com.yayawan.impl;


import com.weibo.game.eversdk.core.EverSDK;

import android.app.Application;
import android.content.Context;

public class YYApplication extends Application {

	@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        EverSDK.getInstance().attachBaseContext(this);
    }

	@Override
	public void onCreate() {
		super.onCreate();
		 EverSDK.getInstance().onApplicationInit(this);
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	

    @Override
    public void onTerminate() {
        super.onTerminate();
        EverSDK.getInstance().onTerminate(this);
    }
}
