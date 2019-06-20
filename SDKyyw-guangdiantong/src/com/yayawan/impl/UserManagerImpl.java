package com.yayawan.impl;

import android.app.Activity;
import android.util.Log;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Yayalog;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserManagerCallBack;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWUserManager;


public class UserManagerImpl implements YYWUserManager {

	public void manager(Activity paramActivity) {
		KgameSdk.accountManager(paramActivity);
	}

	public void login(Activity paramActivity, String paramString,
			Object paramObject) {

	}

	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		KgameSdk.stop(paramActivity);
		
	}

	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {

	}

	public void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		paramActivity.runOnUiThread(new Runnable() {

			public void run() {
				KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

					

					public void onCancel() {
						
					}

					public void onError(int arg0) {
						
					}

					public void onLogout() {
						
					}

					public void onSuccess(User arg0, int arg1) {
						callback.onExit();
					}
				});
			}
		});

	}

	public void setRoleData(Activity arg0) {
		KgameSdk.setRoleData(arg0, YYWMain.mRole.getRoleId(),
				YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
				YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
	}
	//3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,String roleCTime,String ext){
		Yayalog.loger("调用了impl中的usermanagerimpl中的setdata方法");
		
		if (Integer.parseInt(ext)==1) {
			
			Log.i("tag","Integer.parseInt(ext)==1");
			Log.i("tag","roleId = " +roleId);
	        Log.i("tag","roleName = " +roleName);
	        Log.i("tag","roleLevel = " +roleLevel);
	        Log.i("tag","zoneId = " +zoneId);
	        Log.i("tag","zoneName = " +zoneName);
	        Log.i("tag","roleCTime = " +roleCTime);
	        Log.i("tag","ext = " + ext);
	        KgameSdk.setRoleData(paramActivity, YYWMain.mRole.getRoleId(),
	        		YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
	        		YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
		}else if (Integer.parseInt(ext)==2) {
			Log.i("tag","Integer.parseInt(ext)==2");
			Log.i("tag","roleId = " +roleId);
	        Log.i("tag","roleName = " +roleName);
	        Log.i("tag","roleLevel = " +roleLevel);
	        Log.i("tag","zoneId = " +zoneId);
	        Log.i("tag","zoneName = " +zoneName);
	        Log.i("tag","roleCTime = " +roleCTime);
	        Log.i("tag","ext = " + ext);
		}else if (Integer.parseInt(ext)==3) {
			Log.i("tag","Integer.parseInt(ext)==3");
			Log.i("tag","roleId = " +roleId);
	        Log.i("tag","roleName = " +roleName);
	        Log.i("tag","roleLevel = " +roleLevel);
	        Log.i("tag","zoneId = " +zoneId);
	        Log.i("tag","zoneName = " +zoneName);
	        Log.i("tag","roleCTime = " +roleCTime);
	        Log.i("tag","ext = " + ext);
		}
		
	}
}
