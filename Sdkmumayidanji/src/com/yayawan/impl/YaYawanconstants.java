package com.yayawan.impl;

import org.json.JSONException;
import android.provider.Settings;
import org.json.JSONObject;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.mumayi.paymentmain.business.ILoginCallback;
import com.mumayi.paymentmain.business.ILogoutCallback;
import com.mumayi.paymentmain.business.ITradeCallback;
import com.mumayi.paymentmain.ui.PaymentCenterInstance;
import com.mumayi.paymentmain.ui.PaymentUsercenterContro;
import com.mumayi.paymentmain.ui.pay.MMYInstance;
import com.mumayi.paymentmain.ui.usercenter.PaymentFloatInteface;
import com.mumayi.paymentmain.util.PaymentConstants;
import com.mumayi.paymentmain.util.PaymentLog;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;
import com.yayawan.main.YYWMain;


public abstract class YaYawanconstants{

	private static Activity mActivity;

	private static boolean isinit=false;

	private static String gamename;

	private static String appkey;
	
	private static String mzoneId; //区服id
	
	private static String mroleName; //人物名称
	
	private static int mroleLevel; //人物等级

	private static  PaymentFloatInteface floatInteface;

	private static PaymentCenterInstance instance = null;

	private static PaymentUsercenterContro mUserCenter = null;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		instance = PaymentCenterInstance.getInstance(mActivity);
		// 获取用户中心的实例
		mUserCenter = instance.getUsercenterApi(mactivity);
		Yayalog.loger("YaYawanconstants初始化sdk");
		gamename = ""+DeviceUtil.getGameInfo(mActivity, "gamename");
		appkey = ""+DeviceUtil.getGameInfo(mActivity, "appkey");
		Log.i("tag","gamename="+gamename);
		Log.i("tag","appkey="+appkey);
		instance = PaymentCenterInstance.getInstance(mActivity);
		instance.initial(appkey,gamename);

		isinit = true;
		Log.i("tag","mUserCenter=="+mUserCenter);
		Log.i("tag","appkey="+appkey);
		// 设置是否开启bug模式， true打开可以显示Log日志， false不显示
		instance.setTestMode(true);

		// 设置登陆回调
		Log.i("tag","登陆回调");
		//        instance.setLoginCallBack(mActivity.this);
		instance.setLoginCallBack(logincallback);
		
		//注销回调
        instance.setLogoutCallback(logoutcallback);

		// 交易支付回调
		instance.setTradeCallback(tradecallback);
		Log.i("tag","登陆回调1");

		// 确保每次在刚进入游戏都会检测本地数据
		// 在调用登陆之前调用
				instance.findUserInfo();
		
        
     // 设置切换完账号后是否自动跳转登陆
        instance.setChangeAccountAutoToLogin(true);
        
     // 这个是检测是否具有开启悬浮窗的权限的方法 请开发者调用在登陆之前！！！
        instance.checkFloatPermission();
        mUserCenter.showFloat();
       
//        mUserCenter.showFloat();
		Log.i("tag","检测本地数据");

		instance.setUserArea("A区");
		instance.setUserName("漩涡鸣人");
		instance.setUserLevel(99);
		

	}



	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		//		PluginManager.getInstance().performStartActivity(arg0)
	}

	static ITradeCallback tradecallback = new ITradeCallback() {

		@Override
		public void onTradeSuccess(String tradeType, int tradeCode, Intent
				intent) {
			Log.i("tag","Success-tradeType="+tradeType);
			Log.i("tag","Success-tradeCode="+tradeCode);
			Log.i("tag","Success-intent="+intent);
			// 可在此处获取到提交的商品信息
			Bundle bundle = intent.getExtras();
			String orderId = bundle.getString("orderId");
			String productName = bundle.getString("productName");
			String productPrice = bundle.getString("productPrice");
			String productDesc = bundle.getString("productDesc");
			Log.e("mumayi1"," 这是 orderid-->"+orderId);
			Log.e("mumayi1"," 这是 productName-->"+productName);
			Log.e("mumayi1"," 这是 productPrice-->"+productPrice);
			Log.e("mumayi1"," 这是 productDesc-->"+productDesc);
			Log.e("mumayi1"," 这是 tradeType-->"+tradeType);
			if (tradeCode == MMYInstance.PAY_RESULT_SUCCESS) {
				// 在每次支付回调结束时候，调用此接口判断用户是否完善了资料
				Log.i("tag", "支付成功");
				paySuce();
				Log.i("tag", "支付成功1");
				mUserCenter.checkUserState(mActivity);
				Toast.makeText(mActivity, productName + "支付成功  支付金额:" +productPrice, Toast.LENGTH_SHORT).show();
			} else if (tradeCode == MMYInstance.PAY_RESULT_FAILED) {
				Log.i("tag", "支付失败1");
				payFail();
				Log.i("tag", "支付失败1");
				Toast.makeText(mActivity, productName + "支付失败  支付金额:" +productPrice, Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		public void onTradeFail(String tradeType, int tradeCode, Intent
				intent) {
			// TODO Auto-generated method stub
			Log.i("tag","Fail-tradeType="+tradeType);
			Log.i("tag","Fail-tradeCode="+tradeCode);
			Log.i("tag","Fail-intent="+intent);
			payFail();
		}
	};


	static ILoginCallback logincallback = new ILoginCallback() {

		@Override
		public void onLoginSuccess(String loginSuccess) {
			Log.i("tag","登录3");
			// TODO Auto-generated method stub
			Log.d("Welcome", "这是登陆成功的回调数据   ----->" + loginSuccess);
			Log.i("tag","登录4");
			if (!TextUtils.isEmpty(loginSuccess)) {
				Log.i("tag","登录5");
				try {
					JSONObject loginobject = new JSONObject(loginSuccess);
					String loginState = loginobject.getString(PaymentConstants.LOGIN_STATE);
					Log.d("登陆状态是：--->" , loginState);
					Log.i("tag","登录6");
					if (loginState != null && loginState.equals(PaymentConstants.STATE_SUCCESS)) {

						/**  uname:用户名， uid:用户ID
						 * token:是用来服务器验证登录，注册是不是成功，用seesion来解签,解签方法见文档
						 * 所有注册，一键注册，登录的接口成功最后都会走这个回调接口
						 * 在这里进入游戏
						 */
						String uname = loginobject.getString("uname");
						String uid = loginobject.getString("uid");
						String token = loginobject.getString("token");
						String session = loginobject.getString("session");
						Log.i("tag","uid="+uid);
						Log.i("tag","token="+token);
						Log.i("tag","uname="+uname);
						
						loginSuce(mActivity, uid, uname, token);
						Log.i("tag","登录8");


						Log.d("","用户的uid-->" + uid + " 用户名-->" + uname + " 获取token-->" + token + " 获取session-->" + session);

						//Intent go2play_intent = new Intent(WelcomeActivity.this, MainActivity.class);
						//startActivity(go2play_intent);
						//finish();
					}

				} catch (JSONException e) {
					Log.d("解析登陆回调异常" , e.toString());
					e.printStackTrace();
				}
			}
		}

		@Override
		public void onLoginFail(String status, String logiFail) {
			// TODO Auto-generated method stub
			loginFail();
			Log.d("Welcome", "这是登陆失败的回调数据  status ----->" + status);
			Log.d("Welcome", "这是登陆失败的回调数据  logiFail ----->" + logiFail);
			JSONObject statusobject = null;
			JSONObject logiFailobject = null;
			try {
				statusobject = new JSONObject(status);
				// 登录失败
				String loginStatus = statusobject.getString(PaymentConstants.LOGIN_STATE);
				if (!TextUtils.isEmpty(loginStatus) && loginStatus.equals(PaymentConstants.STATE_FAILED)) {

					Log.d("","登陆失败回调-----------");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		Log.i("tag","登录");
		if(isinit){
			Log.i("tag","登录1");
			 instance.go2Login(mactivity);
			Log.i("tag","登录2");
		}else{
			inintsdk(mactivity);
		}


	}
	
	/**
     * 悬浮窗   以及  用户中心的  注销账号的回调
     *
     * @param logoutSuccess
     */
	static ILogoutCallback logoutcallback = new ILogoutCallback() {
		
		@Override
		public void onLogoutSuccess(String logoutSuccess) {
			// TODO Auto-generated method stub
			 JSONObject mSuccJson = null;
		        try {
		            mSuccJson = new JSONObject(logoutSuccess);
		            String code = mSuccJson.getString("loginOutCode");
		            if (code.equals(PaymentConstants.LOGINOUT_SUCCESS)) {
		                String uid = mSuccJson.getString("uid");
		                String name = mSuccJson.getString("uname");
		                Log.d("注销成功账号信息", "注销账号：" + name + " uid:" + uid);
		            }

		        } catch (JSONException e) {
		            e.printStackTrace();
		        }
		}
		
		@Override
		public void onLogoutFail(String logoutFail) {
			// TODO Auto-generated method stub
			if (logoutFail.equals(PaymentConstants.LOGINOUT_FAILED)) {
	            Log.d("注销失败账号信息", "失败了~~~~~");
	        }
		}
	};
	
	
	

	 /**
     * 这个回调 主要是处理悬浮窗 请求权限后 的处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (requestCode == PaymentConstants.FLOAT_REQUEST_CODE) {
////                if (Settings.canDrawOverlays(mActivity)) {
//                    Log.d("悬浮窗有权限了", "回调里处理~~~~~");
//                    if (floatInteface != null) {
//                        floatInteface.show();
////                    }
//                }
//            }
//        }
//    }

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		Log.i("tag","退出start");
//		instance.exit();
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
//				Log.i("tag","退出start-1");
				// TODO Auto-generated method stub
				KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

					

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onError(int arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onLogout() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(User arg0, int arg1) {
						// TODO Auto-generated method stub
						Log.i("tag","退出Success1");
//						Process.killProcess(Process.myPid());
//						mActivity.finish();
//						System.exit(0);
						// 关闭悬浮框
						mUserCenter.closeFloat();
						instance.finish();

						callback.onExit();
						Log.i("tag","退出Success");
					}
				});
			}
		});
		Log.i("tag","退出");
		
	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		
//		mzoneId = zoneId;
//		mroleName = roleName;
//		mroleLevel = Integer.parseInt(roleLevel);
	}
	



	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Log.i("tag","pay-start");
		
//		if(mzoneId.equals("")){
//			Log.i("tag","设置角色名");
//			mzoneId = "A区";
//		}
//		if(mroleName.equals("")){
//			mroleName = "漩涡鸣人";
//		}
//		if(mroleLevel == 0){
//			mroleLevel = 99;
//		}
//		instance.setUserArea(mzoneId);
//		// 设置角色名
//		instance.setUserName(mroleName);
//		// 设置角色等级
//		instance.setUserLevel(mroleLevel);
//		instance.setUserArea("A区");
//		 instance.setUserName("漩涡鸣人");
//		instance.setUserLevel(99);
		mUserCenter.pay(mactivity, YYWMain.mOrder.goods, YYWMain.mOrder.money/100+"", morderid);
//		PaymentLog.getInstance().i("MobileSecurePayer-->pay()-->returnData:"+);
		Log.i("tag","pay-end");
		
	}
	
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
//		mUserCenter .checkLogin();
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		// 释放资源，退出程序
//				instance.exit();
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub
		// 显示悬浮框
		 //显示悬浮窗
		mUserCenter.showFloat();
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
//		mUserCenter.checkLogin();
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
