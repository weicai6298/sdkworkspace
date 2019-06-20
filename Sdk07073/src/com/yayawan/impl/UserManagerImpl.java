package com.yayawan.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.leshan.sdk.LeshanApi;
import com.leshan.sdk.QuitCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.proxy.YYWUserManager;

public class UserManagerImpl implements YYWUserManager {

	@Override
	public void manager(Activity paramActivity) {

		Toast.makeText(paramActivity, "个人中心", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void login(Activity paramActivity, String paramString,
			Object paramObject) {

	}

	@Override
	public void logout(final Activity paramActivity, String paramString,
			Object paramObject) {
//				LeshanApi.getInstance(paramActivity,paramActivity,YaYawanconstants.appid).quit(YaYawanconstants.appid, new QuitCallBack() {
//					
//					@Override
//					public void Quit() {
//						paramActivity.runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								YaYawanconstants.loginOut();
//								Log.i("tag","登出成功");
//							}
//						});
//					}
//				});

	}

	@Override
	public void setUserListener(Activity paramActivity, YYWUserManagerCallBack paramXMUserListener) {

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		System.out.println("来这里了");

		YaYawanconstants.exit(paramActivity, callback);

	}

	@Override
	public void setRoleData(Activity arg0) {
		
	}
	
	@Override
	public void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext) {
		YaYawanconstants.setData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime, ext);
	}

}
