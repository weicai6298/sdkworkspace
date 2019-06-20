package com.yayawan.impl;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;

import com.app.yjy.game.OneNineGame;
import com.onenine.game.lib.callback.OneNineGameCallback;
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
OneNineGame.changeAccount(paramActivity,new OneNineGameCallback() {
			
			@Override
			public void onOneNineGameSucess(String msg) {
				//获取uid和sessionid后向我方服务器做登录校验用户合法性
				try {
					JSONObject obj = new JSONObject(msg);
					int result = obj.optInt("result");
					if(1==result){
					String	uid=obj.optString("uid");
String sessionid = obj.optString("session_id");
YaYawanconstants.tuichu = 1;
YaYawanconstants.loginSuce(paramActivity, uid, uid, sessionid);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onOneNineGameFailure() {
				YaYawanconstants.loginFail();
			}
			@Override
			public void onOneNineGameCancel() {
				YaYawanconstants.loginFail();
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
