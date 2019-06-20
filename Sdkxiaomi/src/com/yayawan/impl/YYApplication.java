package com.yayawan.impl;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		// TODO Auto-generated method stub
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
