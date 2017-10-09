package com.yayawan.impl;


import com.mumayi.paymentmain.ui.MMYApplication;

import android.app.Application;

public class YYApplication extends MMYApplication {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	}
}
