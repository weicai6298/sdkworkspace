package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.kkgame.utils.Handle;
import com.yayawan.proxy.YYWActivityStub;

public class ActivityStubImpl implements YYWActivityStub {

	@Override
	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub

		Handle.active_handler(paramActivity);
		YaYawanconstants.onCreate(paramActivity);
		YaYawanconstants.inintsdk(paramActivity);
		
	}

	@Override
	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onStop(paramActivity);
	}

	@Override
	public void onResume(Activity paramActivity) {
		YaYawanconstants.onResume(paramActivity);
	}

	@Override
	public void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onPause(paramActivity);
	}

	@Override
	public void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		YaYawanconstants.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		YaYawanconstants.onActivityResult(paramActivity,paramInt1,paramInt2,paramIntent);
	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		YaYawanconstants.onNewIntent(paramIntent);
	}

	@Override
	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(Activity mActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onStart(mActivity);
	}

}
