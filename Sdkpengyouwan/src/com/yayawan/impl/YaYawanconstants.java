package com.yayawan.impl;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.pengyouwan.sdk.open.FloatViewTool;
import com.pengyouwan.sdk.open.PayConstant;
import com.pengyouwan.sdk.open.RoleConstant;
import com.pyw.open.support.PYWPlatform;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	public static final String ACTION_LOGIN_SDK_SUCCESS = "ACTION_LOGIN_SDK_SUCCESS";

	public static final String ACTION_TO_START_LOGIN = "ACTION_TO_START_LOGIN";

	public static final String ACTION_TO_EXIT_GAME = "ACTION_TO_EXIT";

	public static SDKResultReceiver mReceiver;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		/**
		 *  初始化sdk，必须在主act中onCreate执行
		 */

		PYWPlatform.initSDK(mActivity.getApplication(), mActivity, new SDKEventListener(mActivity, mActivity));
		// 注册SDK回调的广播
		initSDKCallbackReceiver();
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
			PYWPlatform.openLogin(mactivity);
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
		log("支付开始");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 产品名称，用于显示在朋友玩充值界面，请于游戏界面中的商品保持一致
		paramsMap.put(PayConstant.PAY_PRODUCE_NAME, YYWMain.mOrder.goods);
		// 实际充值金额，单位元 可任意金额 不需要与产品id对应
		try {
			paramsMap.put(PayConstant.PAY_MONEY,YYWMain.mOrder.money/100+"");
		}catch (Exception e) {
			Toast("订单金额有误");
			e.printStackTrace();
			return;
		}
		// 订单id，此项必须要填写，并且参数名必须为"order_id"，否则会出错
		paramsMap.put(PayConstant.PAY_ORDER_ID, morderid);
		//		 .put(PayConstant.PAY_ORDER_ID, "" + System.currentTimeMillis());
		// 厂商需要朋友玩回调时回传的参数，届时会原样返回，此项非必填
		paramsMap.put(PayConstant.PAY_EXTRA, "");
		PYWPlatform.openChargeCenter(mactivity, paramsMap, true);
		log("支付完成");

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
		PYWPlatform.exit(mActivity);


	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(RoleConstant.ROLEID, roleId);
		paramsMap.put(RoleConstant.ROLELEVEL, roleLevel);
		paramsMap.put(RoleConstant.ROLENAME, roleName);
		paramsMap.put(RoleConstant.SERVERAREA, zoneId);
		paramsMap.put(RoleConstant.SERVERAREANAME, zoneName);
		PYWPlatform.getRoleMessage(paramActivity, paramsMap);
	}
	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		if (mReceiver != null) {
			mActivity.unregisterReceiver(mReceiver);
		}
		FloatViewTool.instance(mActivity).destroyFloatView();
	}

	public static void onActivityResult(Activity paramActivity, 
			int paramInt1,int paramInt2, Intent paramIntent) {

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
	 * 在进入游戏后，需调用此接口上传游戏内角色信息
	 */


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

	/*
	 * 日志
	 */
	public static void log(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.i("tag",msg);
			}
		});
	}

	/**
	 * 注册SDK回调广播接收器
	 * 注意！！1、该广播不是SDK内部发出给游戏接收的！而是游戏收到SDK登录回调事件ENTER_GAME_SUCCESS发出通知界面的！
	 * 2、登录成功后通过广播通知界面，这是demo采用的一种实现方式，不是SDK要求的固定实现方案，具体实现方案由游戏自行决定选择！
	 * 3、发送退出通知后，退出游戏操作也是由游戏自行决定
	 */
	public static void initSDKCallbackReceiver() {
		mReceiver = new SDKResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_LOGIN_SDK_SUCCESS);
		filter.addAction(ACTION_TO_START_LOGIN);
		filter.addAction(ACTION_TO_EXIT_GAME);
		mActivity.registerReceiver(mReceiver, filter);

	}



	/**
	 * 描述:SDK回调广播接收者
	 */
	static class SDKResultReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			log("action="+action);
			if (ACTION_LOGIN_SDK_SUCCESS.equals(action)) {
				//showLoginView();
				//显示悬浮窗
				if (null != PYWPlatform.getCurrentUser()) { // 表示当前登录状态
					FloatViewTool.instance(mActivity).showFloatView();
				}
			} else if (ACTION_TO_START_LOGIN.equals(action)) {
				// showStartLoginView();
				// 此处可知是经过切换账号的逻辑到此处切换场景，故可以主动调起一次sdk登陆
				// PYWPlatform.openLogin(MainActivity.this);
				FloatViewTool.instance(mActivity).hideFloatView();// 隐藏浮点
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loginOut();
					}
				});
			} else if (ACTION_TO_EXIT_GAME.equals(action)) {
				//				exit();
				//              退出
				//				Toast("退出");
				//				new Handler().postDelayed(new Runnable() {
				//					
				//					@Override
				//					public void run() {
				//						log("退出");
				//				Process.killProcess(Process.myPid());
				mActivity.finish();
				System.exit(0);
				//					}
				//				}, 50);
			}
		}
	}


}
