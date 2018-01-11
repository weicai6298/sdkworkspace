package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.zqhy.sdk.callback.ExitCallBack;
import com.zqhy.sdk.callback.InitCallBack;
import com.zqhy.sdk.callback.LoginCallBack;
import com.zqhy.sdk.callback.PayCallBack;
import com.zqhy.sdk.model.PayParams;
import com.zqhy.sdk.platform.lehihi.LehihiSDKApi;
import com.zqhy.sdk.ui.FloatViewManager;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static String pid;

	private static String appkey;

	private static String Uid;

	private static String UserName;

	private static String UserToken;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		pid = ""+DeviceUtil.getGameInfo(mactivity, "pid");
		appkey = ""+DeviceUtil.getGameInfo(mactivity, "appkey");
		int strPid = Integer.parseInt(pid);
		Log.i("tag","pid="+pid);
		Log.i("tag","appkey="+appkey);
		LehihiSDKApi.getInstance().init(mactivity, strPid, appkey, new InitCallBack() {
			@Override
			public void onInitSuccess() {
				//                LoggerE("init Success");
				isinit = true;
				Log.i("tag","初始化成功");
				//                Toast.makeText(mActivity, "初始化成功", Toast.LENGTH_SHORT).show();
				//                LehihiSDKApi.getInstance().registerReLoginCallBack(reLoginCallBack);
			}

			@Override
			public void onInitFailure(String message) {
				Log.i("tag","初始化失败\n失败原因：" + message);
				//                Toast.makeText(mActivity, "初始化失败", Toast.LENGTH_SHORT).show();
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
		Log.i("tag","isinit="+isinit);
		if(isinit){
			Log.i("tag","登录");
			LehihiSDKApi.getInstance().login(mactivity, new LoginCallBack() {
				@Override
				public void onLoginSuccess(String uid, String username, String token) {
					Uid = uid;
					UserName = username;
					UserToken = token;
					loginSuce(mactivity, Uid, UserName, UserToken);
					//                FloatViewManager.getInstance(mactivity).showFloat();
				}

				@Override
				public void onLoginFailure(String message) {
					//                LoggerE("onLoginFailure message:" + message);
					loginFail();
				}

				@Override
				public void onLoginCancel() {
					//                LoggerE("onLoginCancel");
					loginFail();
				}
			});
		}else{
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

		PayParams payParams = new PayParams();
		payParams.extendsinfo = morderid;
		payParams.username = UserName;
		payParams.token = UserToken;
		payParams.serverid = "1";
		payParams.amount = YYWMain.mOrder.money/100;

		LehihiSDKApi.getInstance().pay(mactivity, payParams, new PayCallBack() {
			@Override
			public void onPaySuccess(String message) {
				//                LoggerE("onPaySuccess message:" + message);
				Log.i("tag","onPaySuccess message:" + message);
				paySuce();
				Toast("支付成功");
			}

			@Override
			public void onPayFailure(String message) {
				//                LoggerE("onPayFailure message:" + message);
				Log.i("tag","onPayFailure message:" + message);
				payFail();
				Toast("支付失败");
			}

			@Override
			public void onPayCancel() {
				//                LoggerE("onPayCancel");
				payFail();
				Toast("支付失败");
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

		LehihiSDKApi.getInstance().exit(paramActivity, new ExitCallBack() {
			@Override
			public void onExit() {
				Log.i("tag","onExit");
				callback.onExit();
			}

			@Override
			public void onContinueGame() {
				Log.i("tag","onContinueGame");
			}

			@Override
			public void onCancel() {
				Log.i("tag","onCancel");
			}
		}, null);
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
		FloatViewManager.getInstance(paramActivity).showFloat();
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		FloatViewManager.getInstance(paramActivity).destroyFloat();
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
		FloatViewManager.getInstance(paramActivity).hideFloat();
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

	/*
	 * Toast提示
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
