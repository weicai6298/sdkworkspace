package com.yayawan.impl;


import com.downjoy.DownjoyApplication;


public class YYApplication extends DownjoyApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
