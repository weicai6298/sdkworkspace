package com.yayawan.impl;


import android.app.Application;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		com.baidu.gamesdk.BDGameSDK.initApplication(this);
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
