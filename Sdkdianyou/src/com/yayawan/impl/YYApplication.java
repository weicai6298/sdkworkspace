package com.yayawan.impl;


import com.dianyou.openapi.DYOnlineApplication;

import android.app.Application;

public class YYApplication extends DYOnlineApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
