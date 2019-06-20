package com.yayawan.impl;


import com.muzhiwan.sdk.app.MzwApplication;


public class YYApplication extends MzwApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
