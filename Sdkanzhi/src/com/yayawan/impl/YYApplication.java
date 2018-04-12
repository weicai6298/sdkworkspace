package com.yayawan.impl;


import com.anzhi.sdk.middle.manage.AnzhiGameApplication;

public class YYApplication extends AnzhiGameApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
