package com.yayawan.impl;



public class YYApplication extends com.tygrm.sdk.TYRApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
