package com.yayawan.impl;


import com.kkgame.sdk.utils.Util;
import com.kkgame.utils.PermissionUtils;
import com.lidroid.jxutils.http.Jxutilsinit;
import com.yayawan.proxy.GameApitest;
import com.yayawan.proxy.YYWApplication;

import android.Manifest;
import android.app.Application;
import android.content.Context;

public class YYApplication extends YYWApplication {

	public static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();
		Jxutilsinit.init(getApplicationContext());
		
			
	
		
		//System.out.println("YYApplication");
		GameApitest.getGameApitestInstants(getApplicationContext()).sendTest("YYApplicationoncreate="+Util.getPackageName(getApplicationContext()));
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		YYWApplication.mContext = mContext;
	}
}
