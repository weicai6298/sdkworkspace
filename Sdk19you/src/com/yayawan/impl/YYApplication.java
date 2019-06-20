package com.yayawan.impl;


import com.app.yjy.game.OneNineGameApplication;


public class YYApplication extends OneNineGameApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
