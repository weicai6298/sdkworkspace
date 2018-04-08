package com.yayawan.proxy;

import com.kkgame.utils.DeviceUtil;

import android.app.Application;
import android.content.Context;


public class YYWApplication extends Application {

	public static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		GameApitest.initOnapplication(this);
		mContext = getApplicationContext();
		// System.out.println("YYApplication");
		YYcontants.ISDEBUG=DeviceUtil.isDebug(getApplicationContext());
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		YYWApplication.mContext = mContext;
	}
}
