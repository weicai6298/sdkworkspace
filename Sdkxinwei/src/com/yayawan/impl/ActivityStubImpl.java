package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.kkgame.utils.Handle;
import com.yayawan.proxy.YYWActivityStub;

public class ActivityStubImpl implements YYWActivityStub {

	@Override
	public void applicationInit(Activity paramActivity) {

	}

	@Override
	public void onCreate(Activity paramActivity) {

		Handle.active_handler(paramActivity);
		YaYawanconstants.inintsdk(paramActivity);
		YaYawanconstants.onCreate(paramActivity);
		
	}

	@Override
	public void onStop(Activity paramActivity) {
		YaYawanconstants.onStop(paramActivity);
	}

	@Override
	public void onResume(Activity paramActivity) {
		YaYawanconstants.onResume(paramActivity);
	}

	@Override
	public void onPause(Activity paramActivity) {
		YaYawanconstants.onPause(paramActivity);
	}

	@Override
	public void onRestart(Activity paramActivity) {
		YaYawanconstants.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		YaYawanconstants.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {

	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		YaYawanconstants.onActivityResult(paramActivity,paramInt1,paramInt2,paramIntent);
	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		YaYawanconstants.onNewIntent(paramIntent);
	}

	@Override
	public void initSdk(Activity arg0) {
		
	}

	@Override
	public void onStart(Activity mActivity) {
		YaYawanconstants.onStart(mActivity);
	}


}
