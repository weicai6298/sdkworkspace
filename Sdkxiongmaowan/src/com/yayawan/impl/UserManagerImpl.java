package com.yayawan.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.xmwsdk.control.XmwMatrix;
import com.xmwsdk.inface.XmwIDispatcherCallback;
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
		XmwMatrix.getInstance().logoutXMW(paramActivity, logoutcallback);
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

	// 登出回调
			public XmwIDispatcherCallback logoutcallback = new XmwIDispatcherCallback() {
				@Override
				public void onFinished(int code, String data) {
					Log.i("tag","登出回调"+"code:"+code+"data:"+data);
					if (code == 0) {
						// 登出 成功
						System.out.println("data:"+data);
//						doLogin(island);
						//System.exit(0);
								YaYawanconstants.loginOut();
								Log.i("tag","切换账号");
					}else {
						//登出 异常
						System.out.println("data:"+data);
					}
				}
			};
}
