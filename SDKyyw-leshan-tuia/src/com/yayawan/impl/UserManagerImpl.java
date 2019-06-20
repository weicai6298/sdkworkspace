package com.yayawan.impl;

import android.app.Activity;

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
		// TODO Auto-generated method stub
		KgameSdk.accountManager(paramActivity);
	}

	public void login(Activity paramActivity, String paramString,
			Object paramObject) {
		// TODO Auto-generated method stub

	}

	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		KgameSdk.stop(paramActivity);
		
	}

	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {
		// TODO Auto-generated method stub

	}

	public void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "退出游戏", Toast.LENGTH_SHORT).show();
		paramActivity.runOnUiThread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

					

					public void onCancel() {
						// TODO Auto-generated method stub
						
					}

					public void onError(int arg0) {
						// TODO Auto-generated method stub
						
					}

					public void onLogout() {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(User arg0, int arg1) {
						// TODO Auto-generated method stub
						callback.onExit();
					}
				});
			}
		});

	}

	public void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub
		KgameSdk.setRoleData(arg0, YYWMain.mRole.getRoleId(),
				YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
				YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
	}
	//3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,String roleCTime,String ext){
		Yayalog.loger("调用了impl中的usermanagerimpl中的setdata方法");

		
		if (Integer.parseInt(ext)==1) {
			KgameSdk.setRoleData(paramActivity, YYWMain.mRole.getRoleId(),
					YYWMain.mRole.getRoleName(), YYWMain.mRole.getRoleLevel(),
					YYWMain.mRole.getZoneId(), YYWMain.mRole.getZoneName());
		}
		
	}
}
