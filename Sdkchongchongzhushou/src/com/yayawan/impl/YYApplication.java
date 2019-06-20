package com.yayawan.impl;


import com.lion.ccpay.sdk.CCPaySdk;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public class YYApplication extends Application {

	

	public void onCreate() {
		super.onCreate();
		Log.i("tag","application初始化");
		CCPaySdk.getInstance().onCreate(this);
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	@Override
	protected void attachBaseContext(Context base) {
		// TODO Auto-generated method stub
		super.attachBaseContext(base);
		CCPaySdk.getInstance().attachBaseContext(this, base);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		CCPaySdk.getInstance().onConfigurationChanged(this, newConfig);
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		CCPaySdk.getInstance().onTerminate();
	}
}
