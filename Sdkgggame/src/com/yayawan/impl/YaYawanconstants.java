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
	 * 请求上报角色信息
	 * 
	 */
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
							//							String re = EntityUtils.toString(httpResponse
							//									.getEntity());
							//							Log.i("tag", "re=" + re);
							//							JSONObject js = new JSONObject(re);
							//							Log.i("tag", "js=" + js);
							//							uid = js.getString("uid");
							//							Log.i("tag", "uid=" + uid);
							//							Log.i("tag", "token=" + token);
							//							loginSuce(mActivity, uid, uid, token);
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

	
	/**
	 * 
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	private static void HttpPost(final String sid) {
		token = sid;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpPost = new HttpPost(
							"https://api.sdk.75757.com/data/get_uid/");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("app_id", DeviceUtil
							.getAppid(mActivity)));
					params.add(new BasicNameValuePair("code", sid));

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
							String re = EntityUtils.toString(httpResponse
									.getEntity());
							Log.i("tag", "re=" + re);
							JSONObject js = new JSONObject(re);
							Log.i("tag", "js=" + js);
							uid = js.getString("uid");
							Log.i("tag", "uid=" + uid);
							Log.i("tag", "token=" + token);
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
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
}
