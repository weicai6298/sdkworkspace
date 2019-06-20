package com.yayawan.impl;


import com.yaoyue.release.YYSDKApplication;

public class YYApplication extends YYSDKApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
