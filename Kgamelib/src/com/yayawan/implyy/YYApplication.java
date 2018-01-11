package com.yayawan.implyy;


import com.yayawan.proxy.YYWApplication;

import android.app.Application;
import android.content.Context;

public class YYApplication extends YYWApplication {

	public static Context mContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = getApplicationContext();
		// System.out.println("YYApplication");
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		YYWApplication.mContext = mContext;
	}
}
