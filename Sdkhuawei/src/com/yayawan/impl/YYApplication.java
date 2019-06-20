package com.yayawan.impl;



import com.huawei.android.hms.agent.HMSAgent;

import android.app.Application;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
