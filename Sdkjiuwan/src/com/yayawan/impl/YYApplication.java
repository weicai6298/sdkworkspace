package com.yayawan.impl;


import com.game91.framework.NineApplication;
import com.game91.framework.NineApplicationHelper;

public class YYApplication extends NineApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
	
	
}
