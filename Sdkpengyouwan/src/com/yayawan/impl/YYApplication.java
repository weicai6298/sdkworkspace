package com.yayawan.impl;


import com.pyw.open.PYWPoxyApplication;
import com.pyw.open.support.PYWPlatform;

import android.app.Application;

public class YYApplication extends PYWPoxyApplication{

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
		
	}
}
