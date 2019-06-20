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

import android.R.string;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.game.sdk.YTSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.PaymentCallbackInfo;
import com.game.sdk.domain.PaymentErrorMsg;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.game.sdk.domain.OnPaymentListener;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	public static YTSDKManager sdkManager;

	public static String role_Id = "roleid";
	public static int server_id = 2;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		sdkManager = YTSDKManager.getInstance(mactivity);
		sdkManager.setIsPortrait(DeviceUtil.isLandscape(mActivity)?0:1);
		isinit = true ;
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
			sdkManager.showLogin(mactivity, true, new OnLoginListener() {
				@Override
				public void loginSuccess(LogincallBack logincallback) {
					String user_id = logincallback.mem_id;
					String user_token = logincallback.user_token;
					loginSuce(mactivity, user_id, user_id, user_token);
					Toast("登录成功");
					// 登陆完成后,显示浮点，根据需要使用
					sdkManager.showFloatView();
				}
				@Override
				public void loginError(LoginErrorMsg errorMsg) {
					// 登录失败的借口回调,CP自行调用
					int code = errorMsg.code;// 登录失败的状态码
					String msg = errorMsg.msg;// 登录失败的消息提示
					loginFail();
					Toast("登录失败");
					Log.i("tag","登录失败状态码="+code);
					Log.i("tag","登录失败消息提示="+msg);
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
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		sdkManager.showPay(mActivity, role_Id, YYWMain.mOrder.money/100+"",server_id+"", YYWMain.mOrder.goods, YYWMain.mOrder.goods, "", morderid, new OnPaymentListener() {
			@Override
			public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
				double money = callbackInfo.money;
				String msg = callbackInfo.msg;

				
				paySuce();
//				Toast("支付成功");
				// 弹出支付成功信息，一般不用
				/*
				 * Toast.makeText( getApplication(), "充值金额数：" +
				 * callbackInfo.money + " 消息提示：" + callbackInfo.msg,
				 * Toast.LENGTH_LONG).show();
				 */
			}

			@Override
			public void paymentError(PaymentErrorMsg errorMsg) {
				// TODO Auto-generated method stub

				int code = errorMsg.code;
				double money = errorMsg.money;
				String msg = errorMsg.msg;

				payFail();
//				Toast("支付失败");
				Log.i("tag","充值失败：code:" + code);
				Log.i("tag","充值失败：ErrorMsg:" + msg);
				Log.i("tag","预充值的金额:" + money);
				// 弹出支付失败信息，一般不用
				/*
				 * Toast.makeText( getApplication(), "充值失败：code:" +
				 * errorMsg.code + "  ErrorMsg:" + errorMsg.msg +
				 * "  预充值的金额：" + errorMsg.money, Toast.LENGTH_LONG).show();
				 */
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
		// TODO Auto-generated method stub
		
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
		server_id = Integer.parseInt(zoneId);
		if(role_Id.equals("")){
			role_Id = "roleId";
		}
		
		Log.i("tag","role_Id = "+role_Id);
		Log.i("tag","server_id = "+server_id);
	}

	public static void onResume(Activity paramActivity) {
		// 显示浮点
				sdkManager.showFloatView();
	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		sdkManager.recycle();//游戏退出必须调用
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
		// 从窗体移除浮点
				sdkManager.removeFloatView();
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

}
