package com.yayawan.impl;


import android.app.Application;
import android.content.Context;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	
}
