package com.yayawan.impl;


import com.hanfeng.nsdk.NSdkApplication;


public class YYApplication extends NSdkApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
