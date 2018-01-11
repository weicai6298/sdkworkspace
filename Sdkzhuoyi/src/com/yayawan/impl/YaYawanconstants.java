package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.zhuoyou.pay.sdk.ZYGameManager;
import com.zhuoyou.pay.sdk.account.UserInfo;
import com.zhuoyou.pay.sdk.entity.PayParams;
import com.zhuoyou.pay.sdk.listener.IZYLoginCheckListener;
import com.zhuoyou.pay.sdk.listener.ZYInitListener;
import com.zhuoyou.pay.sdk.listener.ZYLoginListener;
import com.zhuoyou.pay.sdk.listener.ZYRechargeListener;


public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static UserInfo mUserInfo = null;

	private static String uid;

	private static String username;

	private static String token;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		ZYGameManager.initSDK(mactivity);
		isinit = true;
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			denglu();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					checklogin();
				}
			}, 2000);
		}else{
			inintsdk(mactivity);
		}

	}
	private static void init() {
		ZYGameManager.init(mActivity,new ZYInitListener() {

			@Override
			public void iniSuccess(UserInfo userInfo) {
				mUserInfo = userInfo;
				uid = mUserInfo.getOpenId()+"";
				username = mUserInfo.getUsername();
				token = mUserInfo.getAccessToken();
				Log.i("tag", "uid =" + uid);
				Log.i("tag", "username =" + username);
				Log.i("tag", "token =" + token);

			}

			@Override
			public void iniFail(String msg) {
				Toast.makeText(mActivity, "初始化失败" + msg, Toast.LENGTH_SHORT).show();
				loginFail();
			}

		});
	}
	private static void denglu(){
		ZYGameManager.login(mActivity, new ZYLoginListener() {

			@Override
			public void logout() {
				YaYawanconstants.loginOut();
			}

			@Override
			public void login() {
				init();
			}
		}, DeviceUtil.isLandscape(mActivity)?ZYGameManager.LOIGN_THEME_LANDSCAPE : ZYGameManager.LOIGN_THEME_PORTRAIT);
	}

	private static void checklogin(){
		if (null != mUserInfo) {
			ZYGameManager.loginCheck(mActivity, mUserInfo.getOpenId(), mUserInfo.getAccessToken(),
					new IZYLoginCheckListener() {

				@Override
				public void checkResult(String code, String message) {
					if (!TextUtils.isEmpty(code) && code.equals("0")) {
						// 登录验证通过，进入游戏
						loginSuce(mActivity, uid, username, token);
						Toast("登录成功");
					}
				}
			});
		} else {
			loginFail();
			Toast("请先初始化登录  ！！！");
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		PayParams params=new PayParams();
		params.setAmount(YYWMain.mOrder.money/100);
		params.setPropsName(YYWMain.mOrder.goods);
		params.setOrderId(morderid);
		//	        params.setExtraParam("");
		ZYGameManager.pay(params, mactivity,new ZYRechargeListener() {

			@Override
			public void success(PayParams params, String zyOrderId) {
				paySuce();
				Toast("支付成功");
			}

			@Override
			public void fail(PayParams params, String erroMsg) {
				payFail();
				Toast("支付失败");
				Log.i("tag","支付失败erroMsg="+erroMsg);
			}
		});
	}

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

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

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
	}
	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		ZYGameManager.onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity2) {

	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {

	}

	public static void onConfigurationChanged(Configuration newConfig) {
		//		super.onConfigurationChanged(newConfig);
		ZYGameManager.onConfigurationChanged(mActivity);
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

	/**
	 * 提示
	 * 
	 * @param msg
	 *        提示的内容
	 */
	public static void Toast(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
			}
		});
	}


}
