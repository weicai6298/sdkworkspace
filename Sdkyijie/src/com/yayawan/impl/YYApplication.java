package com.yayawan.impl;


import com.snowfish.cn.ganga.helper.SFOnlineApplication;


public class YYApplication extends SFOnlineApplication{

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
