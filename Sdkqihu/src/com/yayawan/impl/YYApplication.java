package com.yayawan.impl;


import com.qihoo.gamecenter.sdk.matrix.Matrix;

import android.app.Application;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
		
		Matrix.initInApplication(this);
	}
}
