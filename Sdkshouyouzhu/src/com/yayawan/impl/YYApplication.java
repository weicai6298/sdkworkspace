package com.yayawan.impl;


//import com.kkgame.utils.DeviceUtil;
//import com.uu898.gamesdk.UGSdk;

import android.app.Application;

public class YYApplication extends Application {

	

	@Override
	public void onCreate() {
		super.onCreate();
		
		YaYawanconstants.applicationInit(getApplicationContext());
	
//		String uu_appid = ""+DeviceUtil.getGameInfo(this, "uu_appid"); 
//		String uu_key = ""+DeviceUtil.getGameInfo(this, "uu_key"); 
//		UGSdk.getInstance().init(this,uu_appid,uu_key,false);
//		YaYawanconstants.isinit = true;
	}
}
