package com.yayawan.impl;


import com.huluxia.sdk.SdkConfig;
import com.huluxia.sdk.SdkConfig.Oriention;
import com.huluxia.sdk.UtilsAndroid;
import com.huluxia.sdk.framework.AppConfig;
import com.huluxia.sdk.login.usage.DbManager;
import com.huluxia.sdk.login.usage.LogServiceDaemon;
import com.kkgame.utils.DeviceUtil;

import android.app.ActivityManager;
import android.app.Application;
import android.util.Log;

public class YYApplication extends Application {

	private LogServiceDaemon logServiceDaemon;
	
	private Oriention type;

	@Override
	public void onCreate() {
		super.onCreate();
		Boolean isLandscape = DeviceUtil.isLandscape(this)?true:false;
        if(isLandscape){
        	type = SdkConfig.Oriention.ORIENTATION_LANDSCAPE;
        }else{
        	type = SdkConfig.Oriention.ORIENTATION_PORTRAIT;
        }
        int appid = Integer.parseInt(DeviceUtil.getGameInfo(this, "xj_appid"));
        String activity = DeviceUtil.getGameInfo(this, "startactivity");
		SdkConfig.getInstance()
		.setApkId(appid) //葫芦侠为每个游戏分配了一个ID；测试ID为1000；具体游戏ID请向葫芦侠商务获取。
		.setActivityName(activity) //请填入游戏主Activity名称, 此处提供范例。该字段用于判定游戏Activity是否可见，从而统计在线时长。
		.setNotifyUrl("") //该url由游戏方填写，此处提供范例
		.setOriention(type);//设置横屏(ORIENTATION_LANDSCAPE)或竖屏(ORIENTATION_PORTRAIT)，根据游戏实际情况填写，默认是竖屏
		initHlx();
		
	}
	
	
	private void initHlx(){
        if (!isMainProcess()){
            return;
        }
        String apk_id = SdkConfig.getInstance().getApkId();
        String ativity_name = SdkConfig.getInstance().getActivityName();
        String HLX_SDK_DB = "Hlx_" + apk_id + ".db";

        AppConfig.getInstance().initApp(this, apk_id);
        String appName = UtilsAndroid.getApplicationName();
        AppConfig.getInstance().setAppName(appName);
        DbManager.init(HLX_SDK_DB);//初始化数据库
        logServiceDaemon = new LogServiceDaemon(this, ativity_name);
    }
	
	protected boolean isMainProcess() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        int myPid = android.os.Process.myPid();
        String mainProcessName = this.getPackageName();
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
