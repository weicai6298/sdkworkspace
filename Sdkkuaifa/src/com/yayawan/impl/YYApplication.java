package com.yayawan.impl;


import com.hjr.sdkkit.framework.mw.app.SDKBaseApplication;

//import android.app.Application;

public class YYApplication extends SDKBaseApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
