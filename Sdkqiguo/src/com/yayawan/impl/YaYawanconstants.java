package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kding.api.QiGuoApi;
import com.kding.api.QiGuoCallBack;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		String qiguoappid= ""+DeviceUtil.getGameInfo(mactivity, "qiguoappid");
        QiGuoApi.INSTANCE.initSdk(mActivity, qiguoappid, new QiGuoCallBack() {

            @Override
            public void onSuccess() {
                Log.e("init ", " init  suc");
                //初始化成功后方可调用登陆
                isinit = true;
                //login(mActivity);
            }

            @Override
            public void onFailure(String errorMsg) {
                Log.e("init ", errorMsg);

            }

			@Override
			public void onCancel() {
		           Log.e("init ", "取消");
			}
        });
	}
	
	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		
		if (isinit) {
	        QiGuoApi.INSTANCE.showLogin(new QiGuoCallBack() {

	            @Override
	            public void onSuccess() {
	                String userId = QiGuoApi.INSTANCE.getUserId();
	                String userToken = QiGuoApi.INSTANCE.getToken();
	                Log.e("login ", " login  suc" + "   userId = " + userId);
	                //loginLabel.setText("登陆成功，当前账号uid为 "+userId);
	                YaYawanconstants.loginSuce(mactivity, userId+"", userId+"",userToken);
	            }

	            @Override
	            public void onFailure(String errorMsg) {
	                Log.e("login ", errorMsg);
	                //loginLabel.setText("登陆失败 "+errorMsg);
	                YaYawanconstants.loginFail();
	            }

				@Override
				public void onCancel() {
					Log.e("login ", "登陆取消");
					//loginLabel.setText("登陆取消");
					YaYawanconstants.loginFail();
				}
	        });
		}else {
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		
        QiGuoApi.INSTANCE.pay(YYWMain.mOrder.goods, YYWMain.mOrder.goods, 
        		(YYWMain.mOrder.money/100)+"",morderid, new QiGuoCallBack() {
            @Override
            public void onSuccess() {
                Log.e("pay ", " pay  suc");
                //payLabel.setText("支付成功");
                paySuce();
            }

            @Override
            public void onFailure(String errorMsg) {
                Log.e("pay ", " pay  fail  "+errorMsg);
                //payLabel.setText("支付失败");
                YaYawanconstants.payFail();
            }

			@Override
			public void onCancel() {
		         Log.e("pay ", " 支付取消");
		         //payLabel.setText("支付取消");
		         YaYawanconstants.payFail();
			}
        });
	}

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
			
			@Override
			public void onSuccess(User arg0, int arg1) {
				// TODO Auto-generated method stub
				callback.onExit();
			}
			
			@Override
			public void onLogout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
		//

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		QiGuoApi.INSTANCE.onResume();
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		QiGuoApi.INSTANCE.onPause();
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

	}
	
	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub
		
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	

	/**
	 * 登录成功调用
	 * 
	 * @param mactivity
	 * @param uid
	 *            唯一id
	 * @param username
	 *            用户名..如果用户名为空.则拿uid作为用户名
	 * @param session
	 *            token验证码
	 */
	public static void loginSuce(Activity mactivity, String uid,
			String username, String session) {

		YYWMain.mUser = new YYWUser();

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		;
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ uid + "";
		}

		// YYWMain.mUser.nick = data.getNickName();
		YYWMain.mUser.token = JSONUtil.formatToken(mactivity, session,
				YYWMain.mUser.userName);

		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
			Handle.login_handler(mactivity, YYWMain.mUser.uid,
					YYWMain.mUser.userName);
		}
	}

	
	/**
	 * 登出
	 */
	public static void loginOut() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLogout(null);

		}
	}
	/**
	 * 登录失败
	 */
	public static void loginFail() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginFailed(null, null);

		}
	}

	/*
	 * 支付成功
	 */
	public static void paySuce() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	
	

	

}
