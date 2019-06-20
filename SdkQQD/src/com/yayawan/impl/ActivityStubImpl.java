package com.yayawan.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.Yayalog;
import com.tencent.ysdk.api.YSDKApi;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yayawan.impl.qqhelper.QqYsdkHelp;
import com.yayawan.proxy.YYWActivityStub;


public class ActivityStubImpl implements YYWActivityStub {

	public void applicationInit(Activity paramActivity) {

	}

	private Activity mActivity;
	public static ProgressDialog mAutoLoginWaitingDlg;
	public static int isyoumeng;

	public void onCreate(Activity paramActivity) {
		Myconstants.mpayinfo=new Payinfo();
		Handle.active_handler(paramActivity);
		
		//广点通激活
		//GuangdiantongUtils.guangDiantongInit(paramActivity.getApplicationContext());
		
		mActivity = paramActivity;
		QqYsdkHelp.onCreate(paramActivity);
		QqYsdkHelp.inintsdk(paramActivity);
		YSDKApi.onCreate(paramActivity);
		YSDKApi.handleIntent(paramActivity.getIntent());
		
		String youmeng = DeviceUtil.getGameInfo(paramActivity, "isyoumeng");
		isyoumeng = Integer.parseInt(youmeng);
		if (isyoumeng == 1) {
			UMGameAgent.setDebugMode(true);
			UMGameAgent.init(mActivity);
		}
	}

	public void onStop(Activity paramActivity) {
		YSDKApi.onStop(paramActivity);
	}

	public void onResume(Activity paramActivity) {
		YSDKApi.onResume(paramActivity);
		if (isyoumeng == 1) {
			MobclickAgent.onResume(paramActivity);
		}
	}

	public void onPause(Activity paramActivity) {
		// Utilsjf.stopDialog();
		YSDKApi.onPause(paramActivity);
		if (isyoumeng == 1) {
			MobclickAgent.onPause(paramActivity);
		}
	}

	public void onRestart(Activity paramActivity) {
		YSDKApi.onRestart(paramActivity);
	}

	public void onDestroy(Activity paramActivity) {
		//YSDKApi.logout();
		YSDKApi.onDestroy(paramActivity);
	}

	public void applicationDestroy(Activity paramActivity) {

	}

	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		Yayalog.loger("onActivityResult++++++++++++");
		YSDKApi.onActivityResult(paramInt1, paramInt2, paramIntent);
	}

	public void onNewIntent(Intent paramIntent) {

		// Logger.d("onNewIntent");
		System.out.println("onNewIntent");
		//GAME 处理游戏被拉起的情况
		// launchActivity的onCreat()和onNewIntent()中必须调用
		// YSDKApi.handleCallback()。否则会造成微信登录无回调
		
		YSDKApi.handleIntent(paramIntent);
		
	}

	public void initSdk(Activity arg0) {

	}

	public void onStart(Activity arg0) {
		
	}

	@Override
	public void attachBaseContext(Context arg0) {
		// TODO Auto-generated method stub
		
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfigurationChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestPermissionsResult(int arg0, String[] arg1, int[] arg2) {
		// TODO Auto-generated method stub
		
	}

}
