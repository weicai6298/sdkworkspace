package com.yayawan.impl;


import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameCrashHandler;

import android.app.Application;
import android.util.Log;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
		
		GameServiceSDK.setCrashHandler(getApplicationContext(), new
				GameCrashHandler(){
				@Override
				public void onCrash(String stackInfo) {
				Log.e("GameApplication", "onCrash:" + stackInfo);
				Log.i("tag", "onCrash:" + stackInfo);
				}
				}); 
	}
}
