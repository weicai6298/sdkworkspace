package com.yayawan.impl;


import android.app.Application;
import android.util.Log;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("tag","1");
		YaYawanconstants.applicationInit(getApplicationContext());
		Log.i("tag","2");
	}
}
