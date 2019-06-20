package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kkgame.utils.Handle;
import com.yayawan.proxy.YYWActivityStub;

public class ActivityStubImpl implements YYWActivityStub {

	public void applicationInit(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub

		Handle.active_handler(paramActivity);
		YaYawanconstants.inintsdk(paramActivity);
		YaYawanconstants.onCreate(paramActivity);
		
	}

	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onStop(paramActivity);
	}

	public void onResume(Activity paramActivity) {
		YaYawanconstants.onResume(paramActivity);
	}

	public void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onPause(paramActivity);
	}

	public void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onRestart(paramActivity);
	}

	public void onDestroy(Activity paramActivity) {
		YaYawanconstants.onDestroy(paramActivity);
	}

	public void applicationDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		YaYawanconstants.onActivityResult(paramActivity,paramInt1,paramInt2,paramIntent);
	}

	public void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		YaYawanconstants.onNewIntent(paramIntent);
	}

	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(Activity mActivity) {
		// TODO Auto-generated method stub
		YaYawanconstants.onStart(mActivity);
	}

	@Override
	public void launchActivityOnCreate(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchActivityonOnNewIntent(Intent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onRequestPermissionsResult(int arg0, String[] arg1, int[] arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		YaYawanconstants.onBackPressed();
		
	}

	@Override
	public void onConfigurationChanged() {
		YaYawanconstants.onConfigurationChanged();
	}

	@Override
	public void attachBaseContext(Context newBase) {
		YaYawanconstants.attachBaseContext(newBase);
	}
}
