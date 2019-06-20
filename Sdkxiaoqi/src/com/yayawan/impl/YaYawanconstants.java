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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
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
import com.smwl.smsdk.abstrat.SMInitListener;
import com.smwl.smsdk.abstrat.SMLoginListener;
import com.smwl.smsdk.abstrat.SMPayListener;
import com.smwl.smsdk.app.SMPlatformManager;
import com.smwl.smsdk.bean.PayInfo;
import com.smwl.smsdk.bean.SMUserInfo;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	private static boolean islogin = false;
	
	public static String uid;
	private static String token;
	
	private static String role_Id = "1";
	private static String role_Name = "1";
	private static String role_Level = "1";
	private static String zone_Id = "1";
	private static String zone_Name = "1";
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//		mActivity.runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
		String appkey = DeviceUtil.getGameInfo(mActivity, "appkey");
		SMPlatformManager.getInstance().init(mactivity, appkey, new SMInitListener() {
			
			@Override
			public void onSuccess() {
				isinit = true ;
			}
			
			@Override
			public void onFail(String arg0) {
				System.out.println("初始化失败："+arg0);
			}
		});
//			}
//		});
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
			SMPlatformManager.getInstance().Login(mactivity, new SMLoginListener() {
				
				@Override
				public void onLogoutSuccess() {
					loginOut();
				}
				
				@Override
				public void onLoginSuccess(SMUserInfo arg0) {
					String token = arg0.getTokenkey();
					HttpPost(token);
				}
				
				@Override
				public void onLoginFailed(String arg0) {
					loginFail();
				}
				
				@Override
				public void onLoginCancell(String arg0) {
					loginFail();
				}
			});
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 * @throws JSONException 
	 */
	public static void pay(Activity mactivity, String morderid ,String payinfo) throws JSONException {
		Yayalog.loger("YaYawanconstantssdk支付");
		JSONObject json =new JSONObject(payinfo);
//		String info = json.getString("extends_info_data");
//		String game_area = json.getString("game_area");
//		String game_level = json.getString("game_level");
//		String game_orderid = json.getString("game_orderid");
//		String game_price = json.getString("game_price");
//		String notify_id = json.getString("notify_id");
//		String subject = json.getString("subject");
//		String game_sign = json.getString("game_sign");
//		String game_guid = json.getString("game_guid");
		
		PayInfo payinfo1 =new PayInfo();
		payinfo1.extends_info_data = json.getString("extends_info_data");
		payinfo1.game_area = json.getString("game_area");
		payinfo1.game_level = json.getString("game_level");
		payinfo1.game_orderid = json.getString("game_orderid");
		payinfo1.game_price = json.getString("game_price");
		payinfo1.game_role_id = json.getString("game_role_id");
		payinfo1.game_role_name = json.getString("game_role_name");
		payinfo1.notify_id = json.getString("notify_id");
		payinfo1.subject = json.getString("subject");
		payinfo1.game_sign = json.getString("game_sign");
		payinfo1.game_guid = json.getString("game_guid");
		
		SMPlatformManager.getInstance().Pay(mactivity,payinfo1, new SMPayListener() {
			
			@Override
			public void onPaySuccess(Object obj) {
				// TODO Auto-generated method stub
				System.out.println("回调了成功："+(String)obj);
				paySuce();
			}
			
			@Override
			public void onPayFailed(Object obj) {
				// TODO Auto-generated method stub
				System.out.println("回调了失败:"+(String)obj);	
				payFail();
			}
			
			@Override
			public void onPayCancell(Object obj) {
				// TODO Auto-generated method stub
				System.out.println("回调了取消："+(String)obj);
				payFail();
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
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		role_Id =roleId;
		role_Level =role_Level;
		role_Name =roleName;
		zone_Id =zoneId;
		zone_Name = zoneName;
	}

	public static void onResume(Activity paramActivity) {
		if(islogin){
			SMPlatformManager. getInstance ().Float(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

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
		if(islogin){
			SMPlatformManager. getInstance().hintFloat();
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
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (YYWMain.mPayCallBack != null) {
					YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
							"success");
				}
			}
		});
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
						Yayalog.loger("请求失败"+arg1.toString());
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							Yayalog.loger("请求成功"+arg0.result);
							JSONObject obj = new JSONObject(arg0.result);
							uid = obj.getString("uid");
							Yayalog.loger("uid ="+uid);
							loginSuce(mActivity, uid, uid, token);
							islogin = true;
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
