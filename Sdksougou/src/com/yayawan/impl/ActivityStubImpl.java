package com.yayawan.impl;

import android.app.Activity;
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
		
	}

	public void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub

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
		YaYawanconstants.onActivityResult(paramActivity);
	}

	public void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

}
