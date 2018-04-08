package com.yayawan.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.dianyou.openapi.DYSDK;
import com.dianyou.openapi.interfaces.IOwnedCallBack;
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
	public void logout(Activity paramActivity, String paramString,
			Object paramObject) {
		DYSDK.login().logout(paramActivity, new IOwnedCallBack() {
			@Override
			public void onSuccess() {
				// TODO当前登录用户已退出,应将“游戏场景”切换到未登录的状态(业务场景操作流程和doLogin中的onSuccess方法回调类似).
				Log.d("dy", "登出点游账号成功");

				// 弹出登录框
//				doLogin();
//				YaYawanconstants.loginOut();
			}

			@Override
			public void onCancel(Throwable t, int errorNo, String strMsg,
					boolean alert) {
				// TODO注销失败触发回调
				Log.d("dy", "onCancel：" + errorNo + ":" + strMsg);
			}
		});
	}

	@Override
	public void setUserListener(Activity paramActivity,
			YYWUserManagerCallBack paramXMUserListener) {

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
