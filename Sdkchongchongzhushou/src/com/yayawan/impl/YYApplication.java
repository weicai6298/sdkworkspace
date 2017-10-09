package com.yayawan.impl;


import com.kkgame.utils.DeviceUtil;
import com.lion.ccpay.sdk.CCApplicationUtils;
import com.lion.ccpay.sdk.CCPaySdk;
import com.yayawan.proxy.YYWApplication;

import android.app.Application;
import android.util.Log;

public class YYApplication extends Application {

	

	public void onCreate() {
		super.onCreate();
		Log.i("tag","application初始化");
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
