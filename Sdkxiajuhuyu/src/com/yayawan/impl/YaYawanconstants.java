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
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.huluxia.sdk.SdkConfig;
import com.huluxia.sdk.SdkEvent;
import com.huluxia.sdk.framework.base.log.HLog;
import com.huluxia.sdk.framework.base.notification.CallbackHandler;
import com.huluxia.sdk.framework.base.notification.EventNotifyCenter;
import com.huluxia.sdk.framework.base.utils.UtilsNetwork;
import com.huluxia.sdk.login.AccountMgr;
import com.huluxia.sdk.login.HttpMgr;
import com.huluxia.sdk.login.LoginCode;
import com.huluxia.sdk.login.LoginUserInfo;
import com.huluxia.sdk.login.ui.floatview.HlxFloatManager;
import com.huluxia.sdk.pay.OrderInfo;
import com.huluxia.sdk.pay.PayCode;
import com.huluxia.sdk.pay.PayMgr;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.sdktemplate.R;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;

	private static HlxFloatManager mHlxFloatManager;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		EventNotifyCenter.add(SdkEvent.class, mLoginCallbak);
		isinit = true;
	}


	private static CallbackHandler mLoginCallbak = new CallbackHandler() {

		//必须实现（登录功能相关）
		@EventNotifyCenter.MessageHandler(message = SdkEvent.EVENT_LOGIN)
		public void onLogin(LoginCode code, boolean fromLoginActivity) {
			Log.i("tag", "login result %d ="+ code.getCode().Value());
			if (code.isSucc()) {
				if (mHlxFloatManager == null) {
					mHlxFloatManager = new HlxFloatManager(mActivity);
					mHlxFloatManager.create();
				}
				else {
					HttpMgr.getInstance().getNoticeList(0, 20);
					HttpMgr.getInstance().getMsgCount();
				}

				AccountMgr.HlxToken hlxToken = AccountMgr.getInstance().getToken();
				LoginUserInfo userInfo = AccountMgr.getInstance().getLoginUserInfo();
				Log.i("tag", "hlxtoken device_code: " + hlxToken.device_code + "; key: " + hlxToken.key + "; uid: " + hlxToken.uid + "; userInfo: " + userInfo);
				String uid = hlxToken.uid+"";
				String username = hlxToken.device_code;
				String token = hlxToken.key+"";
				//				Log.i("tag","uid = " +uid);
				//				Log.i("tag","username = " +username);
				//				Log.i("tag","token = " +token);
				loginSuce(mActivity, uid, username, token);
			}  else if (!fromLoginActivity) {  //如果 LoginActivity未显示
				if (!UtilsNetwork.isNetworkConnected(mActivity)) {
					Toast(mActivity.getString(R.string.hlx_network_error));
					return;
				}
				login(mActivity);
				Toast(code.getMessage());
			}
		}

		//必须实现（支付功能相关）
		@EventNotifyCenter.MessageHandler(message = SdkEvent.EVENT_PAY)
		public void onPay(PayCode code, String orderNo) {
			HLog.info(mActivity, "pay result %d, orderNo(%s) msg(%s)", code.getCode().Value(), orderNo, code.getMessage());
			if (code.isSucc()){//此判断为true表示支付成功；失败时code.getMessage()是具体的错误内容
				paySuce();
				Toast("支付成功");
			}else{
				payFail();
				Toast("支付失败");
			}
		}

		//可选（退出登录功能）
		@EventNotifyCenter.MessageHandler(message = SdkEvent.EVENT_LOGOUT)
		public void onLogOut() {
			if (mHlxFloatManager != null) {
				mHlxFloatManager.destory();
				mHlxFloatManager = null;
			}
		}

		//仅供测试，建议从服务端进行校验（客户端校验token）
		@EventNotifyCenter.MessageHandler(message = SdkEvent.EVENT_VERIFY)
		public void onVerify(boolean ret, String msg) {
			HLog.info(this, "verify result %d, msg(%s)", ret ? 1 : 0, msg);
			Toast(msg);
		}
	};

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
			AccountMgr.getInstance().goToLoginActivity(mactivity, false);//建议第二个参数传false
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
//		if(!AccountMgr.getInstance().isLogin()){
//			Toast("未登录");
//			return;
//		}
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.subject = YYWMain.mOrder.goods;
		orderInfo.body = YYWMain.mOrder.goods;
		orderInfo.amount = YYWMain.mOrder.money/100+"";//银联方式最小金额是0.10
		orderInfo.notifyUrl = SdkConfig.getInstance().getNotifyUrl();//该url由游戏方填写，在程序初始化时设置
		orderInfo.orderNo = morderid;//订单号由游戏方生成，此处提供范例
		orderInfo.extraParam = "";//透传参数

		PayMgr.getInstance().goToPayActivity(mactivity, orderInfo);
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

		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){

		}else if(Integer.parseInt(ext) == 2){

		}else if(Integer.parseInt(ext) == 3){

		}
	}

	public static void onResume(Activity paramActivity) {
		if (mHlxFloatManager == null) {
			mHlxFloatManager = new HlxFloatManager(paramActivity);
			mHlxFloatManager.create();
		}
	}

	public static void onPause(Activity paramActivity) {
		if (mHlxFloatManager != null) {
			mHlxFloatManager.destory();
			mHlxFloatManager = null;
		}
	}

	public static void onDestroy(Activity paramActivity) {
		EventNotifyCenter.remove(mLoginCallbak);
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
