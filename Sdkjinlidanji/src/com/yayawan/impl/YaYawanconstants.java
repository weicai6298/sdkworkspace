package com.yayawan.impl;

import java.util.HashMap;
import java.util.Random;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.gionee.game.offlinesdk.floatwindow.AppInfo;
import com.gionee.game.offlinesdk.floatwindow.GamePlatform;
import com.gionee.game.offlinesdk.floatwindow.QuitGameCallback;
import com.gionee.game.offlinesdk.floatwindow.pay.GamePlayByTradeData;
import com.gionee.game.offlinesdk.floatwindow.pay.OrderInfo;
import com.gionee.game.offlinesdk.floatwindow.pay.PayGameCallback;
import com.gionee.gsp.GnEFloatingBoxPositionModel;
import com.gionee.pay.gsp.PayCallback;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	// private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;
	private static Application application;

	// private static boolean isinit = false;
	private static String uid;
//	private static int isyoumeng;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		/**
		 * 6.0以上系统需要手动申请悬浮窗权限
		 */
		if (Build.VERSION.SDK_INT >= 23){
			GamePlatform.requestFloatWindowsPermission(mactivity);
		}
		GamePlatform.setFloatingBoxOriginPosition(GnEFloatingBoxPositionModel.LEFT_TOP);
		GamePlatform.startFloatWindowsService(mactivity);
//		String youmeng = DeviceUtil.getGameInfo(mActivity, "isyoumeng");
//		isyoumeng = Integer.parseInt(youmeng);
//		if(isyoumeng == 1) {
//			UMGameAgent.setDebugMode(true);
//			UMGameAgent.init(mActivity);
//		}
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		application = (Application) applicationContext;
		AppInfo appInfo = new AppInfo();
		String apiKey = "" + DeviceUtil.getGameInfo(application, "jinliapiKey");
		String privateKey = ""
				+ DeviceUtil.getGameInfo(application, "jinliprivateKey");
		appInfo.setApiKey(apiKey); // apiKey由开发者后台申请得到
		appInfo.setPrivateKey(privateKey); // privateKey由开发者后台申请得到
		GamePlatform.init(application, appInfo);
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
	    String id = DeviceUtil.getGameInfo(mactivity, "youxi");
		if(!id.equals("0")){
			Log.i("tag", "id=" + id);
			loginSuce(mactivity, id+"", id+"", id+"");
		}else {
		String tempuid = Sputils.getSPstring("uid", "tem", mactivity);
		Log.i("tag", "tempuid=" + tempuid);
		if (tempuid.equals("tem")) {
			String uidtemp = System.currentTimeMillis() + "kk";
			uid = uidtemp.substring(4, uidtemp.length())
					+ new Random().nextInt(10);
			Sputils.putSPstring("uid", uid, mactivity);
			loginSuce(mactivity, uid, uid, uid);
		} else {
			uid = tempuid;
			loginSuce(mactivity, tempuid, tempuid, tempuid);
		}
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Log.i("tag", "发起支付");
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCpOrderNum(morderid);
		// 商品标题
		orderInfo.setSubject(YYWMain.mOrder.goods);
		// 商品总价格
		orderInfo.setTotalFee(YYWMain.mOrder.money / 100 + "");
		// 商品实际价格
		orderInfo.setDealPrice(YYWMain.mOrder.money / 100 + "");
		orderInfo.setProductName(YYWMain.mOrder.goods);
		orderInfo.setPayMethod(GamePlayByTradeData.PAY_METHOD_UNSPECIFIED);
		Log.i("tag", "orderInfo=" + orderInfo);
		GamePlayByTradeData.getInstance().pay(mactivity, orderInfo, new PayGameCallback() {

			@Override
			public void onSuccess() {
				Log.i("tag", "支付成功");
				paySuce();
				Log.i("tag", "支付成功1");
			}

			@Override
			public void onFail(String arg0, String arg1) {
				Log.i("tag", "支付失败1");
				payFail();
				Log.i("tag", "支付失败1");
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
				
		GamePlatform.quitGame(paramActivity,
				new QuitGameCallback() {

			@Override
			public void onQuit() {
//				if (isyoumeng == 1) {
//					Log.i("tag", "友盟退出");
//					MobclickAgent.onProfileSignOff();
					//							MobclickAgent.onKillProcess(mActivity);
//				}
				callback.onExit();
			}

			@Override
			public void onCancel() {

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
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		Yayalog.loger("YaYawanconstants设置角色信息");
		if (Integer.parseInt(ext) == 1) {
//			if (isyoumeng == 1) {
//				Log.i("tag", "友盟进入游戏");
//				MobclickAgent.onProfileSignIn(uid);
//			}
		}
	}

	public static void onResume(Activity paramActivity) {
//		if(isyoumeng == 1){
//			MobclickAgent.onResume(paramActivity);
//		}
	}

	public static void onPause(Activity paramActivity) {
//		if(isyoumeng == 1){
//			MobclickAgent.onPause(paramActivity);
//		}
	}

	public static void onDestroy(Activity paramActivity) {

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		/**
		 * 该处是为了提示权限赋予成功
		 */
		GamePlatform.onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity) {

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

	/*
	 * 支付成功
	 */
	public static void paySuce() {
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
			}
		});
	}

	public static void payFail() {
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// 支付成功
				if (YYWMain.mPayCallBack != null) {
					YYWMain.mPayCallBack.onPayFailed(null, null);
				}
			}
		});
	}

}
