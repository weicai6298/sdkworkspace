package com.yayawan.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdkmain.KgameSdk;
import com.qq.gdt.action.ActionType;
import com.qq.gdt.action.GDTAction;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWLoginer;

public class LoginImpl implements YYWLoginer {

    public void login(final Activity paramActivity, YYWUserCallBack userCallBack, String paramString) {

    	new Handler(Looper.getMainLooper()).post(new Runnable() {

            public void run() {


			        KgameSdk.login(paramActivity, new KgameSdkUserCallback() {

			            public void onSuccess(User user, int arg1) {
			            	
			            	if (ViewConstants.logintype==2) {
		                    	System.out.println("GDT-register");
		                    	

		                    	GDTAction.logAction(ActionType.REGISTER);
		                    	Log.i("tag","GDT-register");
							}
			            	
			                if (YYWMain.mUserCallBack != null) {

			                    YYWUser yywUser = new YYWUser();

			                    yywUser.uid = user.uid + "";
			                    yywUser.icon = user.icon;
			                    yywUser.body = user.body;
			                    yywUser.lasttime = user.lasttime;
			                    yywUser.money = user.money;
			                    yywUser.nick = user.nick;
			                    yywUser.password = user.password;
			                    yywUser.phoneActive = user.phoneActive;
			                    yywUser.success = user.success;
			                    yywUser.token = user.token;
			                    yywUser.userName = user.userName;
			                    YYWMain.mUser=yywUser;
			                    YYWMain.mUserCallBack.onLoginSuccess(yywUser, "success");
			                }
			            }

			            public void onLogout() {
			            	//YayaWan.stop(paramActivity);
			                if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onLogout("logout");
			                }
			                
			                
			            }

			            public void onError(int arg0) {
			            	
			                if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onLoginFailed("failed", "");
			                }
			            }

			            public void onCancel() {
			                // TODO Auto-generated method stub
			            	if (YYWMain.mUserCallBack != null) {
			                    YYWMain.mUserCallBack.onCancel();
			                }
			            }
			        });

            	}

    	});

    }

    public void relogin(Activity paramActivity, YYWUserCallBack userCallBack,
            String paramString) {

    }

}
