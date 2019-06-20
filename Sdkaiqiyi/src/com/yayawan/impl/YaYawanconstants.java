package com.yayawan.impl;

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
import android.util.Log;
import android.widget.Toast;

import com.iqiyi.sdk.listener.GamePlatformInitListener;
import com.iqiyi.sdk.listener.LoginListener;
import com.iqiyi.sdk.listener.PayListener;
import com.iqiyi.sdk.platform.GamePlatform;
import com.iqiyi.sdk.platform.GameSDKResultCode;
import com.iqiyi.sdk.platform.GameUser;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static YYWExitCallback ExitCallback;
	
	private static GamePlatform platform;
	
	private static String role_id = "123";
	
	private static String zone_id = "123";

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		platform = GamePlatform.getInstance();
		String gameId = DeviceUtil.getGameInfo(mactivity, "aiqiyi_gameid");
		platform.initPlatform(mactivity, gameId,new GamePlatformInitListener() {
			@Override
			public void onSuccess() {
				isinit = true ;
				Log.i("tag","初始化成功");
				platform.addLoginListener(new LoginListener() {

                    @Override
                    public void exitGame() {
                        //退出游戏时回调
                        Log.i("tag", "exitGame");
                        ExitCallback.onExit();
//                        finish();
//                        System.exit(0);
                    }

                    @Override
                    public void logout() {
                        //注销帐号时回调
                        Log.i("tag", "logout");
                        mActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								loginOut();
							}
						});
                    }

                    @Override
                    public void loginResult(int resultCode, GameUser user) {
                        //登录结果回调
                        Log.i("tag", "Login loginResult");
                        if(resultCode == GameSDKResultCode.SUCCESSLOGIN && user != null){
                        	//登录成功后初始化侧边栏浮标
                        	platform.initSliderBar(mActivity);
                        	String uid = user.uid;
                        	int timestamp = user.timestamp;
                        	String sign = user.sign;
                        	Log.i("tag","uid =" +uid);
                        	Log.i("tag","timestamp =" +timestamp);
                        	Log.i("tag","sign =" +sign);
                        	loginSuce(mActivity, uid, uid, sign);
                        }
                    }
                });

                platform.addPaymentListener(new PayListener() {

                    @Override
                    public void paymentResult(int resultCode) {
                        Log.i("tag", "Payment paymentResult : " + resultCode);
                        if(resultCode == GameSDKResultCode.SUCCESSPAYMENT){
                        	paySuce();
                        	Toast("支付成功");
                        }else if(resultCode == GameSDKResultCode.ERRORPAYMENT){
                        	payFail();
                        	Toast("支付失败");
                        }else {
                        	payFail();
                        	Toast("支付失败");
                        }
                    }

                    @Override
                    public void leavePlatform() {
                    	//离开SDK支付平台
                        Log.i("tag", "Payment leavePlatform");
                        payFail();
                    }
                });
			}
			@Override
			public void onFail(String arg0) {
				Log.i("tag","初始化失败= " +arg0);
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
		Log.i("tag","isinit = "+isinit);
		if(isinit){
			platform.iqiyiUserLogin(mactivity);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(final Activity mactivity, final String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		platform.iqiyiPayment(mactivity, Integer.parseInt(YYWMain.mOrder.money/100+""), role_id, "ppsmobile_s1"+zone_id, YYWMain.mOrder.goods, morderid);
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		ExitCallback = callback;
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				platform.iqiyiUserLogout(paramActivity);
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
		role_id = roleId;
		zone_id = zoneId;
		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){
			platform.enterGame(paramActivity, "ppsmobile_s"+zoneId);
		}else if(Integer.parseInt(ext) == 2){
			platform.createRole(paramActivity, "ppsmobile_s"+zoneId);
		}else if(Integer.parseInt(ext) == 3){

		}
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

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
