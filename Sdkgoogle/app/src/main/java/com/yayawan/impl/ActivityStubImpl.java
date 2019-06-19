package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Handle;
import com.kkgame.utils.Yayalog;
import com.kkgame.sdk.gfutil.LoginGF;
import com.yayawan.proxy.YYWActivityStub;

public class ActivityStubImpl implements YYWActivityStub {

	public static Activity mactivity;

	@Override
	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	//启动activity  的oncreate
	public void launcherOncreate(Activity paramActivity){
			
	}

	//启动activity  的onNewIntent
	public void launcherOnNewIntent(Intent intent){
			
	}
	
	@Override
	public void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("oncreate");
		KgameSdk.initSdk(paramActivity);
		LoginGF.inintsdk(paramActivity);//新增
		Handle.active_handler(paramActivity);
	}

	@Override
	public void onResume(Activity paramActivity) {
		mactivity = paramActivity;
		KgameSdk.init(mactivity);

		Yayalog.loger("onresume");
	}

	@Override
	public void onPause(Activity paramActivity) {

		
		KgameSdk.stop(paramActivity);
		Yayalog.loger("onpause");
	}

	@Override
	public void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("onrestart");
	}

	@Override
	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("onstop");
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("ondestroy");
		LoginGF.onDestroy();//新增
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		LoginGF.onActivityResult(paramActivity,paramInt1,paramInt2,paramIntent);
	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initSdk(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("KgameSdksdk：initsdk");
		KgameSdk.initSdk(paramActivity);
	}

	public void payumenSucceed(String money) {
		Yayalog.loger("KgameSdksdk：payumenSucceed");
	}

	@Override
	public void onStart(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("onStart");
	}

	@Override
	public void launchActivityOnCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchActivityonOnNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Yayalog.loger("onBackPressed");
	}

	@Override
	public void attachBaseContext(Context newBase) {
		Yayalog.loger("attachBaseContext");
	}

	@Override
	public void onConfigurationChanged() {
		Yayalog.loger("onConfigurationChanged");
	};

}
