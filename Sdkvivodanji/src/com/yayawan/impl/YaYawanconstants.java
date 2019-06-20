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
import android.content.pm.LauncherApps.Callback;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
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

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;

	private static String APPID;

	private static String uid;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		APPID = ""+DeviceUtil.getGameInfo(mactivity, "vivo_appid");
		VivoUnionSDK.initSdk(mactivity, APPID, false);
		isinit = true ;
		logincallback(mactivity);
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
			VivoUnionSDK.login(mactivity);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid, String assessKey, String order_id) {
		Yayalog.loger("YaYawanconstantssdk支付");
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
//                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
            	Log.i("tag", "支付失败1");
				payFail();
				Log.i("tag", "支付失败1");
//                Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
        };
    };

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
		VivoUnionSDK.exit(paramActivity, new VivoExitCallback() {
			
			@Override
			public void onExitConfirm() {
				callback.onExit();
			}
			
			@Override
			public void onExitCancel() {
				
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
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);

		//1为角色登陆成功  2为角色创建  3为角色升级
		
		if(Integer.parseInt(ext) == 1){
			VivoUnionSDK.reportRoleInfo(new VivoRoleInfo(roleId, roleLevel, roleName, zoneId, zoneName));
		}else if(Integer.parseInt(ext) == 2){
			
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
	/**
	 * 
	 * 请求上报角色信息
	 * 
	 */
	@SuppressWarnings("unused")
	private static void HttpPost(final String roleId, final String roleName,final String roleLevel, final String zoneId, final String zoneName, final String roleCTime) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/user/roleinfo/");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("roleId", roleId));
					params.add(new BasicNameValuePair("roleName", roleName));
					params.add(new BasicNameValuePair("roleLevel", roleLevel));
					params.add(new BasicNameValuePair("zoneId", zoneId));
					params.add(new BasicNameValuePair("zoneName", zoneName));
					params.add(new BasicNameValuePair("roleCTime", roleCTime));

					Log.i("tag", "params=" + params);
					try {
						// 设置httpPost请求参数
						httpPost.setEntity(new UrlEncodedFormEntity(params,
								HTTP.UTF_8));
						HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpPost);
						Log.i("tag",
								"httpResponse.getStatusLine().getStatusCode()="
										+ httpResponse.getStatusLine()
										.getStatusCode());
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							Toast("角色上报成功");
						}

					} catch (ClientProtocolException e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


	private static void logincallback(final Activity mactivity){
		VivoUnionSDK.registerAccountCallback(mactivity, new VivoAccountCallback() {
			
			@Override
			public void onVivoAccountLogout(int arg0) {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						loginOut();
					}
				});
			}
			
			@Override
			public void onVivoAccountLoginCancel() {
				loginFail();
				Toast("登录取消");
			}
			
			@Override
			public void onVivoAccountLogin(String username, String openid, String authtoken) {
				Log.i("tag", "openid = " +openid);
				Log.i("tag", "username = " +username);
				Log.i("tag", "authtoken = " +authtoken);
				uid = openid;
				loginSuce(mactivity, openid, username, authtoken);
				Toast("登录成功");
			}
		});
	}
}
