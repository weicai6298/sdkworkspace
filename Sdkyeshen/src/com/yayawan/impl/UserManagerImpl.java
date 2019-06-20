package com.yayawan.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.bignox.sdk.NoxSDKPlatform;
import com.bignox.sdk.export.entity.KSUserEntity;
import com.bignox.sdk.export.listener.NoxEvent;
import com.bignox.sdk.export.listener.OnLogoutListener;
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
		NoxSDKPlatform.getInstance().noxLogout(new OnLogoutListener() {

			@Override
			public void finish(final NoxEvent<KSUserEntity> event) {
				paramActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						YaYawanconstants.loginOut();
						Log.i("tag","注销,状态码 =" +event.getStatus());
					}
				});
			}

		});
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
