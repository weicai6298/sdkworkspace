package com.yayawan.impl;


import com.cgamex.usdk.api.CGamexApplication;


public class YYApplication extends CGamexApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
