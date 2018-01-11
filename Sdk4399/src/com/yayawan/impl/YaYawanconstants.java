package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.widget.Toast;
import cn.m4399.operate.OperateCenter;
import cn.m4399.operate.OperateCenter.OnLoginFinishedListener;
import cn.m4399.operate.OperateCenter.OnQuitGameListener;
import cn.m4399.operate.OperateCenter.OnRechargeFinishedListener;
import cn.m4399.operate.OperateCenterConfig;
import cn.m4399.operate.OperateCenterConfig.PopLogoStyle;
import cn.m4399.operate.OperateCenterConfig.PopWinPosition;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	private static OperateCenter mOpeCenter;
	private static OperateCenterConfig mOpeConfig;
	private static String GAME_KEY;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		GAME_KEY = DeviceUtil.getGameInfo(mActivity, "GAME_KEY");
		init(mactivity);
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
		Log.i("tag","isinit ="+isinit);
		if(isinit){
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
			mOpeCenter.login(mactivity, new OnLoginFinishedListener() {

				@Override
				public void onLoginFinished(boolean success, int resultCode,
						cn.m4399.operate.User userInfo) {
					//登录结束后的游戏逻辑
					String uid = userInfo.getUid();
					String name = userInfo.getName();
					String token =userInfo.getState();
					Log.i("tag", "uid="+uid);
					Log.i("tag", "name="+name);
					Log.i("tag", "token="+token);
					loginSuce(mactivity, uid, name, token);
					Toast("登录成功");
				}
			});
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
		int money = Integer.parseInt(YYWMain.mOrder.money/100+"");
		//充值金额（元）
		//游戏方订单号
		//商品名称
		Log.i("tag","money = "+money);
		Log.i("tag","morderid = "+morderid);
		Log.i("tag","YYWMain.mOrder.goods = "+YYWMain.mOrder.goods);
		mOpeCenter.recharge(mactivity,money,morderid,YYWMain.mOrder.goods,new OnRechargeFinishedListener() {

			@Override
			public void onRechargeFinished(boolean success, int resultCode,String msg){
				Log.i("tag","支付resultCode = "+resultCode);
				Log.i("tag","支付msg = "+msg);
				Log.i("tag","success = "+success);
				if(success){
					// 请求游戏服，获取充值结果
					paySuce();
					Toast("支付成功");
				}else{
					//充值失败逻辑
					payFail();
					Toast("支付失败");
				}
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

		mOpeCenter.shouldQuitGame(paramActivity, new OnQuitGameListener() {

			@Override
			public void onQuitGame(boolean shouldQuit) {
				// 点击“退出游戏”时，shouldQuit为true，游戏处理自己的退出业务逻辑
				// 点击“前往游戏圈”时，shouldQuit为false，SDK会进入游戏圈或者下载
				//  游戏盒子界面，游戏可以不做处理。
				// 点击“留在游戏”时，shouldQuit为false，SDK和游戏都不做任何处理
				// 点击右上角的关闭图标，shouldQuit为false，SDK和游戏都不做任何处理
				if(shouldQuit){
					callback.onExit();
				}
			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		mOpeCenter.destroy();
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
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
			}
		});
	}

	private static void init(Activity mactivity){
		mOpeCenter = OperateCenter.getInstance();
		mOpeConfig = new OperateCenterConfig.Builder(mactivity)
		.setGameKey(GAME_KEY)     //设置GameKey
		.setDebugEnabled(false)     //设置DEBUG模式,用于接入过程中开关日志输出，发布前必须设置为false或删除该行。默认为false。
		.setOrientation(DeviceUtil.isLandscape(mActivity)?ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)  //设置横竖屏方向，默认为横屏，现支持横竖屏，和180度旋转
		.setSupportExcess(true)     //设置服务端是否支持处理超出部分金额，默认为false
		.setPopLogoStyle(PopLogoStyle.POPLOGOSTYLE_ONE) //设置悬浮窗样式，现有四种可选
		.setPopWinPosition(PopWinPosition.POS_LEFT) //设置悬浮窗默认显示位置，现有四种可选
		.build();
		mOpeCenter.setConfig(mOpeConfig);
		mOpeCenter.init(mactivity,new OperateCenter.OnInitGloabListener() {
			// 初始化结束执行后回调
			@Override
			public void onInitFinished(boolean isLogin, cn.m4399.operate.User userInfo) {
				assert(isLogin == mOpeCenter.isLogin());
				isinit = true;
				Log.i("tag","SDK版本号="+mOpeCenter.getVersion());
			}
			// 注销帐号的回调， 包括个人中心里的注销和logout()注销方式
			// fromUserCenter区分是否是从悬浮窗-个人中心("4399游戏助手页面")注销的，若是则为true，不是为false
			@Override
			public void onUserAccountLogout(boolean fromUserCenter, int resultCode) {
				if(fromUserCenter){
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loginOut();
						}
					});
				}
			}

			// 切换帐号的回调 
			//fromUserCenter区分是否是从"4399游戏助手页面"切换的，若是则为true，不是为false
			@Override
			public void onSwitchUserAccountFinished(boolean fromUserCenter,
					cn.m4399.operate.User arg1) {
				if(fromUserCenter){
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loginOut();
						}
					});
				}
			}



		});
	}

}
