package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.vqs456.sdk.VqsManager;
import com.vqs456.sdk.http.LogOutListener;
import com.vqs456.sdk.http.LoginListener;
import com.vqs456.sdk.http.PayListener;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	public static VqsManager vqsManager;
	private static LoginListener loginListener;
	private static PayListener payListener;
	private static LogOutListener logOutListener;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		initCallback();
		vqsManager = VqsManager.getInstance();
		vqsManager.init(mactivity, loginListener, payListener);
		//设置注销回调
		vqsManager.setLogOutListerner(logOutListener);

		isinit = true ;

	}

	/**
	 * 初始化回调
	 */
	private static void initCallback() {
		loginListener = new LoginListener() {

			@Override
			public void LoginSuccess(String result, String userID, String username, String logintime, String sign) {
				Log.i("tag", "result="+result);
				Log.i("tag", "userID="+userID);
				Log.i("tag", "username="+username);
				Log.i("tag", "logintime="+logintime);
				Log.i("tag", "sign="+sign);
				loginSuce(mActivity, userID, username, sign);
				Toast("登录成功");
			}

			@Override
			public void LoginFailure(String errorID) {
				// TODO Auto-generated method stub
				loginFail();
				Toast("登录失败");
			}
		};
		payListener = new PayListener() {
			@Override
			public void PaySuccess(String result) {
				// TODO Auto-generated method stub
				paySuce();
				Toast("支付成功");
			}

			@Override
			public void PayFailure(String errorID) {
				// TODO Auto-generated method stub
				payFail();
				Toast("支付失败");
			}

			@Override
			public void PayCancel(String errorID) {
				// TODO Auto-generated method stub
				payFail();
				Toast("支付取消");
			}
		};
		logOutListener = new LogOutListener() {
			@Override
			public void LogOut(String result) {
				// 添加游戏登出代码

				loginOut();
				Toast("登出成功");


			}
		};
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
			vqsManager.login();
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

		vqsManager.Pay(YYWMain.mOrder.money+"", YYWMain.mOrder.goods, morderid, "");
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
				callback.onExit();
			}

			@Override
			public void onLogout() {

			}

			@Override
			public void onError(int arg0) {

			}

			@Override
			public void onCancel() {

			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		
		/**
		 * @playerid 游戏内角色ID
		 * @nickname 游戏内角色昵称
		 * @profession 游戏内职业
		 * @lvl 游戏内等级
		 * @gang 游戏内帮派
		 * @diamonds 戏内元宝或钻石数量
		 * @viplvl 游戏内VIP等级
		 * @gamename 游戏名称
		 * @servicename 游戏服务器名称
		 */
		if (Integer.parseInt(ext) == 1){
		vqsManager.setGameInfo(roleId, roleName, "", roleLevel, "", "", "", DeviceUtil.getGameInfo(paramActivity, "gamename"), zoneName);
		}
	}

	public static void onResume(Activity paramActivity) {
		vqsManager.onResume();
	}

	public static void onPause(Activity paramActivity) {
		vqsManager.onPause();
	}

	public static void onDestroy(Activity paramActivity) {
		vqsManager.onDestroy();
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity paramActivity) {

	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		vqsManager.onStop();
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

	/**
	 * 支付成功
	 * 
	 */
	public static void paySuce() {
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	/**
	 * 支付失败
	 * 
	 */
	public static void payFail() {
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	/*
	 * Toast提示
	 */
	public static void Toast(final String msg){
		//		mActivity.runOnUiThread(new Runnable() {
		//
		//			@Override
		//			public void run() {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
		//			}
		//		});
	}


}
