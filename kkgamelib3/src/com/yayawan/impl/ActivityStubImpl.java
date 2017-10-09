package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Handle;
import com.kkgame.utils.Yayalog;
import com.yayawan.proxy.YYWActivityStub;

public class ActivityStubImpl implements YYWActivityStub {

	public static Activity mactivity;

	@Override
	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("oncreate");
		Handle.active_handler(paramActivity);
		KgameSdk.initSdk(paramActivity);

	}

	@Override
	public void onResume(Activity paramActivity) {
		mactivity = paramActivity;
		KgameSdk.init(mactivity);

		Yayalog.loger("onresume");
	}

	@Override
	public void onPause(Activity paramActivity) {

		

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
		KgameSdk.stop(paramActivity);
		Yayalog.loger("onstop");
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		Yayalog.loger("ondestroy");
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub

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

	};

}
