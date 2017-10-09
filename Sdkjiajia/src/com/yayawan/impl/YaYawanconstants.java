package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yyjia.sdk.plugin.PluginCenter;
import com.yyjia.sdk.plugin.data.PInformation;
import com.yyjia.sdk.plugin.listen.GameExitListener;
import com.yyjia.sdk.plugin.listen.PInitListener;
import com.yyjia.sdk.plugin.listen.PLoginListener;
import com.yyjia.sdk.plugin.listen.PPayListener;
import com.yyjia.sdk.util.Utils;


public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;  

	private static PluginCenter pCenter = null;

	private static String zone_Id = "1";

	private static String role_Id = "1";


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		if (pCenter == null) {
			// 传入游戏的主Activity
			pCenter = PluginCenter.getInstance(mActivity);
			pCenter.init();
		}
		pCenter.setDebug(true);
		pCenter.onCreate(mActivity);
		// 登录登出监听器
		pCenter.setLoginListener(new PLoginListener() {
			// 登录监听方法
			@Override
			public void loginSuccessed(String code) {
				if (code.equals(PInformation.LOGIN_SUSECCED)) {
					String uid = pCenter.getUid();
					String token = pCenter.getSid();
					String name = pCenter.getUsername();
					log("uid = " + uid);
					log("name = " + name);
					log("token = " + token);
					loginSuce(mActivity, uid, name, token);
					Toast("登录成功");
					// 此处服务端必须请求登录验证 对账号进行验证
				} else {
					loginFail();
					Toast("登录失败");
				}
			}

			// 登出监听方法
			public void logoutSuccessed(String code) {
				if (code.equals(PInformation.LOGOUT_SUSECCED)) {
					mActivity.runOnUiThread(new Runnable() {
						public void run() {
							loginOut();
						}
					});
					log("2");
					Toast("退出成功");
				} else {
					Toast("退出失败");
				}
			}

			// 取消登录
			@Override
			public void loginCancel(String code) {
				if (code.equals(PInformation.LOGCANCEL_SUSECCED)) {
					Toast("取消登录成功");
				} else {
					Toast("取消登录失败");
				}
			}
		});

		pCenter= PluginCenter.getInstance(mActivity);
		pCenter.onCreate(mActivity);// 传入的必须是一个Activity,必须在init()调用
		pCenter.init();
		//初始化事件监听
		pCenter.setpInitListener(new PInitListener() {

			@Override
			public void initSuccessed(String code) {
				if (code.equals(PInformation.INIT_SUSECCED)) {
					isinit = true;
					log("初始化成功");
				} else {
					log("初始化失败");
				}
			}
		});
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
			pCenter.checkLogin(); //建议在初始化完成后调用
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
		String changeRate="10";//元宝跟人民币兑换比例
		String productId="s_"+YYWMain.mOrder.goods;//商品编号
		Float money=Float.parseFloat(YYWMain.mOrder.money/100+"");//金额 元为单位
		log(money+"");
		String productname=YYWMain.mOrder.goods;
		String serverId =zone_Id;
		String charId=role_Id;
		String cporderId= morderid;
		String callbackInfo="";
		pCenter.pay(mactivity, changeRate, productId, money, productname, serverId, charId, cporderId, callbackInfo, new PPayListener() {

			@Override
			public void paySuccessed(String code, String msg) {
				if (code.equals(PInformation.PAY_SUSECCED)) {
					paySuce();
					Toast("支付成功");
				}else {
					payFail();
					Toast("支付失败");
					log("支付失败=> " + Utils.checkEmpty(msg));
				}
			}

			@Override
			public void payGoBack() {
				payFail();
				Toast("支付取消");
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
		if (pCenter != null) {
			pCenter.GameExit(new GameExitListener() {

				@Override
				public void exitSuccessed(String code) {
					if (code.equals(PInformation.GAMEEXIT_SUCCESS)) {
						Toast("退出游戏");
						mActivity.finish();
						System.exit(0);
					} else {
						Toast("继续游戏");
					}
				}
			});
		} else {
			Toast("SDK未初始化");
		}

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//填入 1为角色登陆成功  2为角色创建  3为角色升级
		role_Id = roleId;
		zone_Id = zoneId;
		if (Integer.parseInt(ext) == 1){
			pCenter.enterGame(zoneId, zoneName, roleId, roleName, roleLevel);
		}else if (Integer.parseInt(ext) == 2){
			pCenter.createRoleInfo(zoneId, zoneName, roleId, roleName, roleCTime);
		}else if (Integer.parseInt(ext) == 3){
			pCenter.roleUpgrade(zoneId, zoneName, roleId, roleName, roleLevel);
		}else {
			pCenter.submitRoleInfo(zoneId, zoneName, roleId, roleName, roleLevel, roleCTime);
		}

	}
	public static void onResume(Activity paramActivity) {
		if (pCenter != null) {
			pCenter.onResume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		if (pCenter != null) {
			pCenter.onPause(paramActivity);
		}
	}

	public static void onDestroy(Activity paramActivity) {
		if (pCenter != null) {
			pCenter.onDestroy(paramActivity);
		}
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {
		if (pCenter != null) {
			pCenter.onActivityResult(paramInt1, paramInt2, paramIntent);
		}
	}

	public static void onNewIntent(Intent paramIntent) {
		if (pCenter != null) {
			pCenter.onNewIntent(paramIntent);
		}
	}

	public static void onStart(Activity paramActivity) {
		if (pCenter != null) {
			pCenter.onStart(paramActivity);
		}
	}

	public static void onRestart(Activity paramActivity) {
		if (pCenter != null) {
			pCenter.onRestart(paramActivity);
		}
	}

	public static void onCreate(Activity paramActivity) {
		if (pCenter == null) {
			pCenter.onCreate(paramActivity);
		}
	}

	public static void onStop(Activity paramActivity) {
		if (pCenter != null) {
			pCenter.onStop(paramActivity);
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		if (pCenter != null) {
			pCenter.onSaveInstanceState(outState);
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		if (pCenter != null) {
			pCenter.onConfigurationChanged(newConfig);
		}
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
	/*
	 * 日志打印
	 */
	public static void log(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.i("tag",msg);
			}
		});
	}

}
