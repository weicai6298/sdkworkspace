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
import android.app.Activity;
import android.app.Application;
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
import com.lantern.auth.openapi.IWkAPI;
import com.lantern.auth.openapi.WkLogin;
import com.lantern.auth.openapi.WkPay;
import com.lantern.auth.openapi.WkSDKParams;
import com.lantern.auth.stub.WkSDKFeature;
import com.lantern.auth.stub.WkSDKResp;
import com.wifi.openapi.WKConfig;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;

	private static IWkAPI mApi;
	
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		Application app = (Application) applicationContext;
		String APP_ID = DeviceUtil.getGameInfo(applicationContext, "wifi_appid");
		String AES_KEY = DeviceUtil.getGameInfo(applicationContext, "wifi_aeskey");
		String AES_IV = DeviceUtil.getGameInfo(applicationContext, "wifi_aesiv");
		String MD5_KEY  = DeviceUtil.getGameInfo(applicationContext, "wifi_md5key");
		WKConfig.build(app,APP_ID, AES_KEY, AES_IV, MD5_KEY, "wifi_push_cq")
		.setOverSea(false)//默认为false，如果是海外版本请设置为true
		.init();
		isinit = true ;

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					WkLogin.login(mactivity);//请确保在UI线程调用
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
	public static void pay(Activity mactivity, String morderid,String sign,String ip) {
		Yayalog.loger("YaYawanconstantssdk支付");
		mApi = WkPay.createPayAPI(mactivity);
		final WkSDKParams req = WkPay.createPayRequest();
		req.mAppId = DeviceUtil.getGameInfo(mactivity, "wifi_appid");
		req.mAppName = DeviceUtil.getGameInfo(mactivity, "gamename");// 应用名
		req.mOpenId = callbackactivity.uid;// 通过万能钥匙授权登录后拿到的openid，若没有，建议填写用户身份的标识
		req.mPackageName = mactivity.getPackageName();
		req.mGoodsName = YYWMain.mOrder.goods;// 商品名称
		req.mOrderAmount = YYWMain.mOrder.money/100+"";// 商品金额
		req.mMerchantOrderNo = morderid;// 订单
		req.mNotifyUrl = DeviceUtil.getGameInfo(mactivity, "callback");// 订单支付结果服务端回执结果
		// 正式商户号和key
		req.mMerchantNo = DeviceUtil.getGameInfo(mactivity, "wifi_merchantNo");// 商户号
		
		req.mSign = sign; // 以下加签 ,此处仅为参考，应在服务端进行
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mApi.sendReq(req);// 发起支付，确保该方法在UI线程中调用
			}
		});
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

			@Override
			public void onSuccess(User arg0, int arg1) {
//				mApi.onRelease();
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

	/** 
	 * 设置角色信息
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);

		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){

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

	
}
