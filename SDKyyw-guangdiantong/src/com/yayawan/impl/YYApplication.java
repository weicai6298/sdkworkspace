package com.yayawan.impl;

import android.util.Log;

import com.kkgame.utils.DeviceUtil;
import com.lidroid.jxutils.http.Jxutilsinit;
import com.qq.gdt.action.ActionType;
import com.qq.gdt.action.GDTAction;
import com.yayawan.proxy.GameApitest;
import com.yayawan.proxy.YYWApplication;


public class YYApplication extends YYWApplication {


	@Override
	public void onCreate() {
		super.onCreate();
		Jxutilsinit.init(getApplicationContext());
		
		GameApitest.getGameApitestInstants(getApplicationContext()).sendTest("YYApplicationoncreate");
		String UserActionID = DeviceUtil.getGameInfo(getApplicationContext(), "UserActionID");
		String AppSecretKey = DeviceUtil.getGameInfo(getApplicationContext(), "AppSecretKey");
		// 第一个参数是Context上下文，第二个参数是您在DMP上获得的行为数据源ID，第三个参数是您在DMP上获得AppSecretKey
		GDTAction.init(this, UserActionID, AppSecretKey);
		GDTAction.logAction(ActionType.START_APP);
		Log.i("tag","onCreate结束");
		
	}
	
}
