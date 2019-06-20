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
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yiqiwanba.wansdk.api.GetUserCheckCodeListener;
import com.yiqiwanba.wansdk.api.OnLoginListener;
import com.yiqiwanba.wansdk.api.OnPayListener;
import com.yiqiwanba.wansdk.api.PayResult;
import com.yiqiwanba.wansdk.api.Player;
import com.yiqiwanba.wansdk.api.WanApi;
import com.yiqiwanba.wansdk.api.WanLogoutListener;

public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;

	public static String zone_id = "123";

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		String appid = DeviceUtil.getGameInfo(mactivity, "wb_appid");
		WanApi.init(mactivity, appid, new WanLogoutListener() {
			
			@Override
			public boolean logoutGame() {
				loginOut();
				return true;
			}
		});
		isinit = true ;
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
			WanApi.login(new OnLoginListener() {


				@Override
				public void onLoginSuccess(Player player) {
					final String uid = player.getUid();
					WanApi.getUserCheckCode(new GetUserCheckCodeListener() {

						@Override
						public void code(String code) {
							loginSuce(mactivity, uid, uid, code);
						}
					});
				}

				@Override
				public void onLoginFailure(Throwable e) {
					loginFail();
					Log.i("tag","登录失败 = " +e);
				}
			});
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		WanApi.pay(morderid, YYWMain.mOrder.goods, YYWMain.mOrder.money, 1, zone_id, null, new OnPayListener() {

			@Override
			public void onPaySuccess(PayResult payResult) {
				Log.i("tag","payResult =" + payResult);
				if(payResult.isSuccess()){
					paySuce();
				}
			}

			@Override
			public void onPayFailure(PayResult payResult, Throwable e) {
				payFail();
				Log.i("tag","支付失败  =" + e);
			}

			@Override
			public void onPayCancel() {
				payFail();
			}
		});
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
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
		});
	}

	/** 
	 * 设置角色信息
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);

		//1为角色登陆成功  2为角色创建  3为角色升级
		zone_id = zoneId;
		if(Integer.parseInt(ext) == 1){

		}else if(Integer.parseInt(ext) == 2){

		}else if(Integer.parseInt(ext) == 3){

		}
	}

	public static void onResume(Activity paramActivity) {
		WanApi.onResume();
	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		WanApi.onDestroy();
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {

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
		WanApi.onStop();
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
	public static void loginSuce(Activity mactivity, String uid,String username, String session) {

		YYWMain.mUser = new YYWUser();

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ uid + "";
		}

		//		 YYWMain.mUser.nick = data.getNickName();
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

}
