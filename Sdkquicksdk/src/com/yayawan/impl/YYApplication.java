package com.yayawan.impl;


import com.quicksdk.QuickSdkApplication;

public class YYApplication extends QuickSdkApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
