package com.yayawan.implyy;

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

	}

	@Override
	public void onCreate(Activity paramActivity) {
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
		KgameSdk.stop(paramActivity);
		Yayalog.loger("onpause");
	}

	@Override
	public void onRestart(Activity paramActivity) {
		Yayalog.loger("onrestart");
	}

	@Override
	public void onStop(Activity paramActivity) {

		Yayalog.loger("onstop");
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		Yayalog.loger("ondestroy");
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {

	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

	}

	@Override
	public void onNewIntent(Intent paramIntent) {

	}

	@Override
	public void initSdk(Activity paramActivity) {
		Yayalog.loger("KgameSdksdk：initsdk");
	}

	public void payumenSucceed(String money) {
		Yayalog.loger("KgameSdksdk：payumenSucceed");
	}

	@Override
	public void onStart(Activity paramActivity) {

	};

}
