package com.yayawan.impl;


import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.kkgame.utils.DeviceUtil;
import com.kwai.opensdk.allin.client.AllInApplication;
import com.kwai.opensdk.allin.client.AllInSDKClient;
import com.kwai.opensdk.allin.internal.log.Flog;

public class YYApplication extends AllInApplication {



	@Override
	public void onCreate() {
				super.onCreate();

//		YaYawanconstants.applicationInit(getApplicationContext());
	}

	@Override
	public void attachBaseContext(Context context) {
		super.attachBaseContext(context);
		
			String kf_appid = DeviceUtil.getGameInfo(this, "kf_appid");
			String kf_appkey = DeviceUtil.getGameInfo(this, "kf_appkey");
			Map<String, String> map = new HashMap<String, String>();
			map.put("kwai_app_id", kf_appid);
			map.put("kwai_app_key", kf_appkey);
			Log.i("tag","kf_appid = " + kf_appid);
			Log.i("tag","kf_appkey = " + kf_appkey);
			AllInSDKClient.setChannelParams(map);
		
	}

	
	@Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

}
