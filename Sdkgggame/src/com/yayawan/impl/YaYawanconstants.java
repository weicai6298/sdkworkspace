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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.id;
import android.R.integer;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.IAppPay;
import com.iplay.josdk.JOSdk;
import com.iplay.josdk.interfaces.LoginListener;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	public static boolean isinit = false;
	
	public static String appId;
	public static String uid;
	public static String token;
	private static Context context;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		int screenType= DeviceUtil.isLandscape(mActivity)?IAppPay.LANDSCAPE:IAppPay.PORTRAIT;
		appId = DeviceUtil.getGameInfo(mactivity, "appId");
		Log.i("tag","appId="+appId);
		IAppPay.init(mactivity, screenType, appId);
	}

	/**
	 * application初始化
	 */
	@SuppressWarnings("static-access")
	public static void applicationInit(Context applicationContext) {
		context =applicationContext;
		Application application = (Application)applicationContext;
		String gameid = DeviceUtil.getGameInfo(applicationContext, "gameid");
		Log.i("tag","gameid="+gameid);
		JOSdk.getInstance().init(application,gameid,"production");
		YaYawanconstants.isinit = true ;

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			JOSdk.getInstance().login(mactivity,new LoginListener() {
				/**
				 * 登录成功回调
				 * @param responseCode 登录成功状态码
				 * @param gameToken 登录成功返回Token
				 */
				@Override
				public void onSuccess(int responseCode, String gameToken) {
//					Toast.makeText(mactivity,gameToken,Toast.LENGTH_LONG).show();
					HttpPost(gameToken);
				}

				/**
				 * 登录失败回调
				 * @param responseCode 登录失败状态码
				 * @param errorMsg 登录失败状态码匹配的失败信息
				 */
				@Override
				public void onFail(int responseCode, String errorMsg) {
					loginFail();
					Toast("登录失败");
				}

			});
		}else{
			applicationInit(context);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid,String ipay_orderId) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Yayalog.loger("YaYawanconstantssdk支付ipay_orderId="+ipay_orderId);
		String parm = "transid="+ipay_orderId+"&appid="+appId;
		IAppPay.startPay(mactivity, parm , new IPayResultCallback() {

			@Override
			public void onPayResult(int resultCode, String signvalue, String resultInfo) {
				// TODO Auto-generated method stub
				Log.i("tag","resultCode="+resultCode);
				Log.i("tag","resultInfo = "+resultInfo);
				Log.i("tag","signvalue = "+signvalue);
				if(resultCode == IAppPay.PAY_SUCCESS){
					paySuce();
					Toast("支付成功");
				}else if(resultCode == IAppPay.PAY_ERROR){
					payFail();
					Toast("支付失败");
					Log.i("tag","支付失败的信息resultInfo = "+resultInfo);
					Log.i("tag","支付失败签名数据signvalue = "+signvalue);
				}else{
					payFail();
					Toast("支付取消");
					
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
	public static void exit(final Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {

KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
			
			@Override
			public void onSuccess(User arg0, int arg1) {
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						callback.onExit();
					}
				});
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
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
	}

	public static void onResume(Activity paramActivity) {
		JOSdk.getInstance().onActivityResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		JOSdk.getInstance().onActivityPaused(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		JOSdk.getInstance().onActivityDestroy(paramActivity);
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
		JOSdk.getInstance().onActivityCreate(paramActivity,null);
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
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	private static void HttpPost(final String sid) {
		token = sid;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/data/get_uid/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("code", sid);
		httpUtil.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Yayalog.loger("请求失败"+arg1.toString());
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						try {
							Yayalog.loger("请求成功"+arg0.result);
							JSONObject obj = new JSONObject(arg0.result);
							String uid = obj.getString("uid");
							Yayalog.loger("uid ="+uid);
							loginSuce(mActivity, uid, uid, sid);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
