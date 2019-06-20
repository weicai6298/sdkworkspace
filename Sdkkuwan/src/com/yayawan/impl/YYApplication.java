package com.yayawan.impl;


public class YYApplication extends com.ddgame.impl.YYApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
