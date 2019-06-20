package com.yayawan.impl;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.data.sdk.domain.model.DataParamsModel;
import com.bsgamesdk.android.BSGameSdk;
import com.bsgamesdk.android.callbacklistener.AccountCallBackListener;
import com.bsgamesdk.android.callbacklistener.BSGameSdkError;
import com.bsgamesdk.android.callbacklistener.CallbackListener;
import com.bsgamesdk.android.callbacklistener.ExitCallbackListener;
import com.bsgamesdk.android.callbacklistener.InitCallbackListener;
import com.bsgamesdk.android.callbacklistener.OrderCallbackListener;
import com.bsgamesdk.android.dc.DataCollect;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	private static int isfirstlogin = 0;

	// 声明BSGameSdk实例
	private static BSGameSdk gameSdk;

	public static String uid;
	public static String username;

	public static String roid_name = "默认昵称";
	
	public static String server_id;
	public static String server_name;
	public static String merchant_id;
	public static String app_id;
	public static String app_key;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		merchant_id = DeviceUtil.getGameInfo(mActivity, "bilibili_merchantid");
		app_id = DeviceUtil.getGameInfo(mActivity, "bilibili_appid");
		server_id = DeviceUtil.getGameInfo(mActivity, "bilibili_serverid");
		app_key = DeviceUtil.getGameInfo(mActivity, "bilibili_appkey");
		server_name = DeviceUtil.getGameInfo(mActivity, "bilibili_servername");
		Log.i("tag", "app_key = " +app_key);
		Log.i("tag", "server_name = " +server_name);
		BSGameSdk.initialize(true, mactivity, merchant_id, app_id,
				server_id, app_key, new InitCallbackListener() {
			@Override
			public void onSuccess() {
				isinit = true ;
				Log.i("tag", "初始化成功");
			}

			@Override
			public void onFailed() {
				Log.i("tag", "初始化失败");
			}
		}, null);
		//		register();
		gameSdk = BSGameSdk.getInstance();
		gameSdk.setAccountListener(new AccountCallBackListener() {

			@Override
			public void onAccountInvalid() {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Log.i("tag", "账号已登出");
						loginOut();
					}
				});
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
			gameSdk.login(new CallbackListener() {

				@Override
				public void onSuccess(Bundle arg0) {
					// 此处为操作成功时执行，返回值通过Bundle传回

					//					LogUtils.d("onSuccess");
					uid = arg0.getString("uid");
					username = arg0.getString("username");
					Log.i("tag","登录-uid = " +uid);
					String access_token = arg0.getString("access_token");
					String expire_times = arg0.getString("expire_times");
					String refresh_token = arg0.getString("refresh_token");
					String nickname = arg0.getString("nickname");
					loginSuce(mactivity, uid, username, access_token);
					Toast("登录成功");

					//					preferences.edit().clear().commit();
					//					preferences.edit().putString("username", userName)
					//							.commit();
					//					preferences.edit().putString("uid", uid).commit();
					//
					//					makeToast("uid: " + uid + " nickname: " + nickname
					//							+ " access_token: " + access_token
					//							+ " expire_times: " + expire_times
					//							+ " refresh_token: " + refresh_token);
				}

				@Override
				public void onFailed(BSGameSdkError arg0) {
					// 此处为操作失败时执行，返回值为BSGameSdkError类型变量，其中包含ErrorCode和ErrorMessage
					//					LogUtils.d("onFailed\nErrorCode : "
					//							+ arg0.getErrorCode() + "\nErrorMessage : "
					//							+ arg0.getErrorMessage());
					//					makeToast("onFailed\nErrorCode : "
					//							+ arg0.getErrorCode() + "\nErrorMessage : "
					//							+ arg0.getErrorMessage());
					loginFail();
					Log.i("tag","onFailed\nErrorCode : "
							+ arg0.getErrorCode() + "\nErrorMessage : "
							+ arg0.getErrorMessage());
					Toast("登录失败");
				}

				@Override
				public void onError(BSGameSdkError arg0) {
					// 此处为操作异常时执行，返回值为BSGameSdkError类型变量，其中包含ErrorCode和ErrorMessage
					//					LogUtils.d("onError\nErrorCode : "
					//							+ arg0.getErrorCode() + "\nErrorMessage : "
					//							+ arg0.getErrorMessage());
					//					makeToast("onError\nErrorCode : " + arg0.getErrorCode()
					//							+ "\nErrorMessage : " + arg0.getErrorMessage());
					loginFail();
					Log.i("tag","onError\nErrorCode : "
							+ arg0.getErrorCode() + "\nErrorMessage : "
							+ arg0.getErrorMessage());
					Toast("登录异常");
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
	public static void pay(Activity mactivity, String morderid, String game_sign) {
		Yayalog.loger("YaYawanconstantssdk支付");
		String name = roid_name;
		Log.i("tag","name = " +name);
		int money = Integer.parseInt(YYWMain.mOrder.money+"");
		Log.i("tag","money = " +money);
		Log.i("tag","game_sign = " +game_sign);
		gameSdk.pay(Integer.parseInt(uid), username, name, server_id, money, 1, morderid, YYWMain.mOrder.goods, YYWMain.mOrder.goods, "", "", game_sign, new OrderCallbackListener() {

			@Override
			public void onSuccess(String out_trade_no, String bs_trade_no) {
				// 此处为操作成功时执行，返回值通过Bundle传回
				Log.i("tag","onSuccess");
				Log.i("tag","CPTradeNo: " + out_trade_no +
						"\nBSTradeNo: " + bs_trade_no );
				paySuce();
				Toast("支付成功");
			}

			@Override
			public void onFailed(String out_trade_no,BSGameSdkError arg0) {
				// 此处为操作失败时执行，返回值为BSGameSdkError类型变量，其中包含ErrorCode和ErrorMessage
				Log.i("tag","onFailed\n" + "payOutTradeNo: "
						+ out_trade_no + "\nErrorCode : "
						+ arg0.getErrorCode() + "\nErrorMessage : "
						+ arg0.getErrorMessage());
				payFail();
				Toast("支付失败");
			}

			@Override
			public void onError(String out_trade_no, BSGameSdkError arg0) {
				// 此处为操作异常时执行，返回值为BSGameSdkError类型变量，其中包含ErrorCode和ErrorMessage
				Log.i("tag","onError\n" + "payOutTradeNo: "
						+ out_trade_no + "\nErrorCode : "
						+ arg0.getErrorCode() + "\nErrorMessage : "
						+ arg0.getErrorMessage());
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
	public static void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				gameSdk.exit(new ExitCallbackListener() {
					
					@Override
					public void onExit() {
//						callback.onExit();
						mActivity.finish();
						System.exit(0);
					}
				});
			}
		});
	
	}
	
	public static DataParamsModel dataParamsModel;
	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		// 1为角色登陆成功 2为角色创建 3为角色升级。
		roid_name = roleName;
		Log.i("tag","zoneId = " +zoneId);
		Log.i("tag","zoneName = " +zoneName);
		Log.i("tag","roleId = " +roleId);
		Log.i("tag","roleName = " +roleName);
		if(roid_name.equals("")){
			roid_name = username;
		}
		Log.i("tag","上报-uid = " +uid);
		Log.i("tag","上报-merchant_id = " +merchant_id);
		Log.i("tag","上报-server_id = " +server_id);
		Log.i("tag","上报-app_id = " +app_id);
		if (Integer.parseInt(ext) == 1) {
			if(isfirstlogin == 0){
				//数据SDK接入
				Log.i("tag","登录-数据SDK接入");
				DataParamsModel dataParamsModel = new DataParamsModel();
				dataParamsModel.setMerchant_id(merchant_id);
				dataParamsModel.setServer_id(server_id);
				dataParamsModel.setApp_id(app_id);
				dataParamsModel.setUid(uid);
				DataCollect.getInstance().dCInit(paramActivity, dataParamsModel);
			}
			Log.i("tag","角色登陆成功roid_name = " +roid_name);
			gameSdk.notifyZone(server_id, server_name, roleId, roid_name);
		}else if(Integer.parseInt(ext) == 2){
			//数据SDK接入
			Log.i("tag","创角-数据SDK接入");
			dataParamsModel = new DataParamsModel();
			dataParamsModel.setMerchant_id(merchant_id);
			dataParamsModel.setServer_id(server_id);
			dataParamsModel.setApp_id(app_id);
			dataParamsModel.setUid(uid);
			DataCollect.getInstance().dCInit(paramActivity, dataParamsModel);
			isfirstlogin = 1;
			Log.i("tag","角色创建roid_name = " + roid_name);
			gameSdk.createRole(roid_name, roleId);
			Log.i("tag","角色创建成功");
		}
	}

	public static void onResume(Activity paramActivity) {
		DataCollect.getInstance().appOnline(paramActivity);
	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		DataCollect.getInstance().appDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

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
		DataCollect.getInstance().appOffline(paramActivity);
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
	public static void Toast(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
			}
		});
	}
	public static void register(){
		gameSdk.register(new CallbackListener() {

			@Override
			public void onSuccess(Bundle arg0) {
				// 此处为操作成功时执行，返回值通过Bundle传回
				Log.i("tag","onSuccess");
				//				// 注册成功后已退出登录，清除保存的信息
				//				preferences.edit().clear().commit();
				//				String result = arg0.getString("result");
				//				makeToast("return: " + result);
			}

			@Override
			public void onFailed(BSGameSdkError arg0) {
				// 此处为操作失败时执行，返回值为BSGameSdkError类型变量，其中包含ErrorCode和ErrorMessage
				Log.i("tag","onFailed\nErrorCode : "
						+ arg0.getErrorCode() + "\nErrorMessage : "
						+ arg0.getErrorMessage());
			}

			@Override
			public void onError(BSGameSdkError arg0) {
				// 此处为操作异常时执行，返回值为BSGameSdkError类型变量，其中包含ErrorCode和ErrorMessage
				Log.i("tag","onError\nErrorCode : "
						+ arg0.getErrorCode() + "\nErrorMessage : "
						+ arg0.getErrorMessage());
			}

		});
	}
}
