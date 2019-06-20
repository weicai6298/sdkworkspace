package com.yayawan.impl;


import com.yuecheng.sdk.OnlyApplication;


public class YYApplication extends OnlyApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
