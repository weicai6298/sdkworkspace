package com.yayawan.impl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gionee.gamesdk.floatwindow.GameOrder;
import com.gionee.gamesdk.floatwindow.GamePayCallBack;
import com.gionee.gamesdk.floatwindow.GamePayManager;
import com.gionee.gamesdk.floatwindow.GamePlatform;
import com.gionee.gamesdk.floatwindow.QuitGameCallback;
import com.gionee.gamesdk.floatwindow.GamePlatform.LoginListener;
import com.gionee.gameservice.account.AccountInfo;
import com.gionee.gsp.GnEFloatingBoxPositionModel;
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

	// private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static Application application;
	
	public static String userId;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		GamePlatform.requestFloatWindowsPermission(mActivity);
		// 设置悬浮窗的默认位置(如果不设置，则默认左上角)
		GamePlatform.setFloatingBoxOriginPosition(GnEFloatingBoxPositionModel.LEFT_TOP);
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		Yayalog.loger("applicationInit-YaYawanconstants初始化sdk");
		application = (Application) applicationContext;
		String apiKey = "" + DeviceUtil.getGameInfo(application, "apikey");
		GamePlatform.init(application, apiKey);
		isinit = true;

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if (isinit) {
			loginAccount(mactivity);
		} else {
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid ,String orderstring) {
		Yayalog.loger("YaYawanconstantssdk支付");
//		String privateKey = "" + DeviceUtil.getGameInfo(application, "privateKey");
		GamePayManager.getInstance().pay(mactivity, orderstring , new GamePayCallBack() {
			
			@Override
			public void onPaySuccess() {
				/**
//				 * 支付成功
//				 */
				paySuce();
				Toast("支付成功");
			}
			
			@Override
			public void onPayFail(Exception e) {
				/**
//				 * 创建订单失败或者支付失败
//				 */
				payFail();
				Toast("支付失败");
				Log.i("tag", "支付失败="+e);
			}
			
			@Override
			public void onCreateOrderSuccess(String arg0) {
				Log.i("tag","创建订单="+arg0);
				Yayalog.loger("jinli订单创建："+arg0);
				
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

			@Override
			public void run() {
				GamePlatform.quitGame(paramActivity, new QuitGameCallback() {

					@Override
					public void onQuit() {
//						callback.onExit();
						mActivity.finish();
						System.exit(0);

					}

					@Override
					public void onCancel() {
						Toast("继续游戏");

					}
				});

			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		Yayalog.loger("YaYawanconstants设置角色信息");
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		GamePlatform.onActivityResult(paramActivity, paramInt1, paramInt2,
				paramIntent);
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
	public static void Toast(final String msg) {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 如果登录过的，会自动登录
	 */
	private static void loginAccount(final Activity activity) {
		GamePlatform.loginAccount(activity, true, new LoginListener() {

			@Override
			public void onSuccess(
					com.gionee.gamesdk.floatwindow.AccountInfo accountInfo) {
				// 登录成功，处理自己的业务。
				// 获取pid
				String playerId = accountInfo.mPlayerId;
				// 获取amigoToken
				String amigoToken = accountInfo.mToken;
				// 获取userId
				userId = accountInfo.mUserId;
				Log.i("tag", "playerId = " + playerId);
				Log.i("tag", "amigoToken = " + amigoToken);
				Log.i("tag", "userId = " + userId);
				loginSuce(activity, playerId, playerId, amigoToken);
				Toast("登录成功");
			}

			@Override
			public void onError(Object e) {
				loginFail();
				Toast("登录失败");
				Log.i("tag", "登录失败：" + e);
			}

			@Override
			public void onCancel() {
				loginFail();
				Toast("取消登录");

			}
		});
	}

}
