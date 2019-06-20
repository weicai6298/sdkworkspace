package com.yayawan.impl;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;
import com.tencent.ysdk.api.YSDKApi;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWUserManager;


public class UserManagerImpl implements YYWUserManager {

	@Override
	public void manager(Activity paramActivity) {

		Toast.makeText(paramActivity, "个人中心", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub
		YSDKApi.logout();
		Sputils.putSPstring("logout", "yes", paramActivity);
	}

	@Override
	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exit(final Activity paramActivity, final YYWExitCallback callback) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		Yayalog.loger("进来了丫丫玩退出");
		if (DeviceUtil.getGameInfo(paramActivity, "addexit").equals("yes")) {
			paramActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
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

	@Override
	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub

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

					@Override
					public void onClick(final DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Sputils.putSPstring("logout", "yes", activitiy);
						YSDKApi.logout();
						Myconstants.openId=null;
						Timer timer = new Timer();
						
						timer.schedule(new TimerTask() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								activitiy.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										dialog.dismiss();
//										activitiy.finish();
										//callback.onExit();
										if (YYWMain.mUserCallBack != null) {
											YYWMain.mUserCallBack.onLogout(null);
										}
										
									}
								});
								
							}
						}, 1000);
						
					}
				})
				.setNeutralButton("普通退出", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						activitiy.finish();
						//callback.onExit();
						
					}
				}). create();

		dialog.show();
	}

	@Override
	public void setData(Activity arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5, String arg6, String arg7) {
		// TODO Auto-generated method stub
		
	}

}
