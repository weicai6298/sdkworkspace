package com.yayawan.impl;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;
import com.tencent.ysdk.api.YSDKApi;
import com.umeng.analytics.MobclickAgent;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.proxy.YYWUserManager;


public class UserManagerImpl implements YYWUserManager {

	public void manager(Activity paramActivity) {

		Toast.makeText(paramActivity, "个人中心", Toast.LENGTH_SHORT).show();
	}

	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
	}

	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		YSDKApi.logout();
		Sputils.putSPstring("logout", "yes", paramActivity);
	}

	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {

	}

	public void exit(final Activity paramActivity, final YYWExitCallback callback) {
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		Yayalog.loger("进来了丫丫玩退出");
		if (DeviceUtil.getGameInfo(paramActivity, "addexit").equals("yes")) {
			paramActivity.runOnUiThread(new Runnable() {
				
				public void run() {
					
					Exitgame(paramActivity, callback);
				}});
			
		}else {
			
			//Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
			Yayalog.loger("进来了不添加退出框的退出");
			callback.onExit();
			paramActivity.finish();
		}
		
		//WGPlatform.WGLogout();
	}

	
	public void setRoleData(Activity arg0) {
		

	}
	
	/**
	 * 退出登录
	 * 
	 * @param activitiy
	 * @param callback 
	 * @param onexit
	 */
	public static void Exitgame(final Activity activitiy, final YYWExitCallback callback) {

		Dialog dialog = new AlertDialog.Builder(activitiy).setTitle("退出游戏提示")

		.setMessage("是否退出游戏？点击空白处取消退出")
				.setPositiveButton("注销退出", new DialogInterface.OnClickListener() {

					
					public void onClick(final DialogInterface dialog, int which) {
						Sputils.putSPstring("logout", "yes", activitiy);
						YSDKApi.logout();
						Myconstants.openId=null;
						Timer timer = new Timer();
						
						timer.schedule(new TimerTask() {
							
							@Override
							public void run() {
								activitiy.runOnUiThread(new Runnable() {
									
									
									public void run() {
										dialog.dismiss();
										if (ActivityStubImpl.isyoumeng == 1) {
											Log.i("tag", "友盟退出1");
											MobclickAgent.onProfileSignOff();
//											MobclickAgent.onKillProcess(mActivity);
										}
										activitiy.finish();
										//callback.onExit();
										
									}
								});
								
							}
						}, 1000);
						
					}
				})
				.setNeutralButton("普通退出", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (ActivityStubImpl.isyoumeng == 1) {
							Log.i("tag", "友盟退出2");
							MobclickAgent.onProfileSignOff();
//							MobclickAgent.onKillProcess(mActivity);
						}
						activitiy.finish();
						//callback.onExit();
						
					}
				}). create();

		dialog.show();
	}

	public void setData(Activity arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, String arg7) {
		if (Integer.parseInt(arg7) == 1) {
			if (ActivityStubImpl.isyoumeng == 1) {
				Log.i("tag", "友盟进入游戏");
				MobclickAgent.onProfileSignIn(LoginImpl.uid);
			}
		}
	}

}
