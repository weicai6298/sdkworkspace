package com.yayawan.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.vivo.unionsdk.open.VivoAccountCallback;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoPayCallback;
import com.vivo.unionsdk.open.VivoPayInfo;
import com.vivo.unionsdk.open.VivoRoleInfo;
import com.vivo.unionsdk.open.VivoUnionSDK;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.RequestQueue;
import android.util.Log;
import android.widget.Toast;



public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static String APPID;

	private static String APPKEY;

	private static String uid;
	
	private static String openId;
    private static VivoPayInfo mVivoPayInfo;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		VivoUnionSDK.registerAccountCallback(mactivity, registeraccountcallback);
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		//SDK初始化, 请传入自己游戏的appid替换demo中的appid。
		APPID = ""+DeviceUtil.getGameInfo(applicationContext, "APPID");
		APPKEY = ""+DeviceUtil.getGameInfo(applicationContext, "APPKEY");
		Log.i("tag", "APPID="+APPID);
		VivoUnionSDK.initSdk(applicationContext, APPID, false);
		isinit = true;
		Log.i("tag", "初始化结束");
	}

	static VivoAccountCallback registeraccountcallback = new VivoAccountCallback() {

		@Override
		public void onVivoAccountLogout(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onVivoAccountLoginCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onVivoAccountLogin(String userName, String openId, String authToken) {
			// TODO Auto-generated method stub
			uid = openId;
			String username = userName;
			String token = authToken;
			Log.i("tag","uid="+uid);
			Log.i("tag","username="+username);
			Log.i("tag","token="+token);
			Log.i("tag","登录成功");
			loginSuce(mActivity, uid, username, token);
			Log.i("tag","登录成功1");
			//登录成功后上报角色信息
		}
	};

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			VivoUnionSDK.login(mactivity);
		}else{
			inintsdk(mactivity);
		}

	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid, String assessKey, String order_id) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Log.i("tag","morderid="+morderid);
		VivoPayInfo payinfo = new VivoPayInfo(YYWMain.mOrder.goods,YYWMain.mOrder.goods, YYWMain.mOrder.money+"", assessKey, APPID, order_id, uid);
	VivoUnionSDK.pay(mactivity, payinfo, mVivoPayCallback);
	}

	 private static VivoPayCallback mVivoPayCallback = new VivoPayCallback() {
	        //客户端返回的支付结果不可靠，请以服务器端最终的支付结果为准；
	        public void onVivoPayResult(String transNo, boolean isSucc, String errorCode) {
	        	Log.i("tag","transNo="+transNo);
	        	Log.i("tag","isSucc="+isSucc);
	        	Log.i("tag","errorCode="+errorCode);
	            if (isSucc) {
	            	Log.i("tag", "支付成功");
					paySuce();
					Log.i("tag", "支付成功1");
//	                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
	            } else {
	            	Log.i("tag", "支付失败1");
					payFail();
					Log.i("tag", "支付失败1");
//	                Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
	            }
	        };
	    };
	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		VivoUnionSDK.exit(paramActivity, new VivoExitCallback() {

			@Override
			public void onExitConfirm() {
				// TODO Auto-generated method stub
				callback.onExit();
			}

			@Override
			public void onExitCancel() {
				// TODO Auto-generated method stub

			}
		});
		//

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		if (Integer.parseInt(ext) == 1){
			//登录成功后上报角色信息
			//VivoUnionSDK.reportRoleInfo(new VivoRoleInfo("角色ID", "角色等级", "角色名称", "区服ID", "区服名称"));
			VivoUnionSDK.reportRoleInfo(new VivoRoleInfo(roleId, roleLevel, roleName, zoneId, zoneName));
		}
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

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






}
