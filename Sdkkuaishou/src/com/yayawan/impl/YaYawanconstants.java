package com.yayawan.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
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
import com.kwai.opensdk.allin.client.AllInSDKClient;
import com.kwai.opensdk.allin.client.Code;
import com.kwai.opensdk.allin.client.listener.AllInInitListener;
import com.kwai.opensdk.allin.client.listener.AllInPayListener;
import com.kwai.opensdk.allin.client.listener.AllInUserListener;
import com.kwai.opensdk.allin.client.model.AccountModel;
import com.kwai.opensdk.allin.client.model.PayModel;
import com.kwai.opensdk.allin.client.model.PayResultModel;
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

	private static Activity mActivity;

	private static boolean isinit = false;

	public static String role_id = "123";

	public static String role_name = "123";

	public static String role_level = "123";

	public static String zone_id = "123";

	public static String zone_name = "123";

	public static String ks_uid;

	public static String ks_channelid;
	
	public static String uid;
	private static String bufanuid;
	private static String bufantoken;

	private static String token;
	
	private static String paystatus = "1";// 订单状态
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		AllInSDKClient.init(false,new AllInInitListener() {

			@Override
			public void onSuccess(String channel) {
				ks_channelid = channel;
				isinit = true ;
				Log.i("tag","ks_channelid = " +channel);
			}

			@Override
			public void onError(int code, String msg) {
				Log.i("tag","初始化失败code ="+code);
				Log.i("tag","初始化失败msg ="+msg);
			}
		}, DeviceUtil.getGameInfo(mActivity, "gamename"), new int[]{Code.PLUGIN_USER, Code.PLUGIN_PAY});
	}

	/**
	 * application的初始化
	 */
	public static void applicationInit(Context applicationContext) {

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			AllInSDKClient.login(new AllInUserListener() {

				@Override
				public void onSwitchAccount(AccountModel accountModel) {
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loginOut();
							Log.i("tag","登出成功");
						}
					});
				}

				@Override
				public void onSuccess(AccountModel accountModel) {
					ks_uid = accountModel.getSdkUserId();
					uid = ks_uid;
					token = accountModel.getSdkToken();
					String tokensign = accountModel.getSdkTokenSign();
					Log.i("tag","uid = "+ks_uid);
					Log.i("tag","token = "+token);
					Log.i("tag","tokensign = "+tokensign);
					loginSuce(mactivity, ks_uid, ks_uid, token);
					Toast("登录成功");

				}

				@Override
				public void onQueryResult(String arg0) {
					//用户查询防沉迷回调

				}

				@Override
				public void onLogout() {
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loginOut();
							Log.i("tag","登出成功");
						}
					});
				}

				@Override
				public void onError(int code, String arg1) {
					//操作出错
					loginFail();
					Toast("登录失败");
					Log.i("tag","登录失败code ="+code);
					Log.i("tag","登录失败arg1 ="+arg1);
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
	public static void pay(Activity mactivity, final String morderid,String server_info) {
		Yayalog.loger("YaYawanconstantssdk支付"+server_info);
		PayModel model = new PayModel();
		model.setServerInfo(server_info);
		model.setPoint(false);
		AllInSDKClient.pay(model, new AllInPayListener() {

			@Override
			public void onError(int arg0, String arg1) {
				payFail();
				Toast("支付失败");
				Log.i("tag","支付失败arg0 = " +arg0);
				Log.i("tag","支付失败arg1 = " +arg1);
			}

			@Override
			public void finish(PayResultModel arg0) {
				HttpPost(uid, token, morderid);
//				paySuce();
//				Toast("支付成功");
				Log.i("tag","支付成功arg0 = " +arg0);
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
		role_id = roleId;
		role_name = roleName;
		role_level = roleLevel;
		zone_id = zoneId;
		zone_name = zoneName;
		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){

		}else if(Integer.parseInt(ext) == 2){

		}else if(Integer.parseInt(ext) == 3){

		}
	}

	public static void onResume(Activity paramActivity) {
		AllInSDKClient.showFloat(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		AllInSDKClient.hideFloat(paramActivity);
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
	 * 请求获取支付订单结果
	 * 
	 * @param uid
	 *            为用户唯一id
	 * @param token
	 *            为登录返回的token
	 * @param orderid
	 *            为支付订单号
	 */
	public static void HttpPost(final String uid, final String token,
			final String orderid) {
		bufanuid = YYWMain.mUser.yywuid;
		bufantoken = YYWMain.mUser.yywtoken;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/pay/order_status/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("uid", bufanuid);
		requestParams.addBodyParameter("token", bufantoken);
		requestParams.addBodyParameter("billid", orderid);
		httpUtil.send(HttpMethod.POST, url,requestParams,
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
							paystatus = obj.getString("status");
							Yayalog.loger("status ="+paystatus);
							if ((paystatus.equals("2")) || (paystatus.equals("3"))) {
								paySuce();
								Log.i("tag", "支付成功");
								Toast("支付成功");
							} else {
								payFail();
								Toast("支付失败");
								Log.i("tag", "支付失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

}
