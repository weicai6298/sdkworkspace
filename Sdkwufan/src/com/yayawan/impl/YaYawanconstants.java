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
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.papa91.pay.callback.PPLoginCallBack;
import com.papa91.pay.callback.PPayCallback;
import com.papa91.pay.callback.PpaLogoutCallback;
import com.papa91.pay.pa.activity.PaayActivity;
import com.papa91.pay.pa.business.LoginResult;
import com.papa91.pay.pa.business.PPayCenter;
import com.papa91.pay.pa.business.PaayArg;
import com.papa91.pay.pa.business.PayArgsCheckResult;
import com.papa91.pay.pa.dto.LogoutResult;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	private static String uid;
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		PPayCenter.init(mactivity);
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
			PPayCenter.login(mactivity, new PPLoginCallBack() {
				@Override
				public void onLoginFinish(LoginResult result) {
					 switch (result.getCode()) {
	                    case LoginResult.LOGIN_CODE_APPID_NOT_FOUND:
	                        //没找到appid
	                        break;
	                    case LoginResult.LOGIN_CODE_SUCCESS://登录成功
	                        int openUid = result.getOpenUid();//返回openUid
	                        String token = result.getToken();
//	                        String username = result.get
	                        uid = openUid+"";
	                        loginSuce(mActivity, uid, uid, token);
	                        PPayCenter.loginFinish(mactivity);
	                        Toast("登录成功");
	                        break;
	                    case LoginResult.LOGIN_CODE_FAILED://登录失败
	                        String message = result.getMessage();//失败详情
	                        loginFail();
	                        Toast("登录失败");
	                        Log.i("tag","登录失败="+message);
	                        break;
	                    case LoginResult.LOGIN_CODE_CANCEL:// 登录取消
	                    	  loginFail();
		                        Toast("登录取消");
	                        break;
	                    case LoginResult.NOT_INIT://没有调用 PPayCenter.init(activity);
	                    	Log.i("tag","初始化失败");
	                        break;

	                }
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
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
        PaayArg paayArg = new PaayArg();
        paayArg.APP_NAME = DeviceUtil.getGameInfo(mActivity, "gamename");
        paayArg.APP_ORDER_ID = morderid;
        paayArg.APP_USER_ID = uid;
        paayArg.APP_USER_NAME = uid;
        paayArg.MONEY_AMOUNT = YYWMain.mOrder.money/100+"";
//        paayArg.NOTIFY_URI = "http://sdkapi.papa91.com/index.php/pay_center/test";
        paayArg.NOTIFY_URI = DeviceUtil.getGameInfo(mActivity, "callback");
        paayArg.PRODUCT_ID = "s_"+YYWMain.mOrder.goods;
        paayArg.PRODUCT_NAME = YYWMain.mOrder.goods;
        paayArg.PA_OPEN_UID = Integer.parseInt(uid);//调用登录方法，得到该值
        Log.i("tag","paayArg = " +paayArg.NOTIFY_URI);
        PPayCenter.pay(paayArg, new PPayCallback() {
            @Override
            public void onPayFinished(int status) {
                Log.e("支付结果", status + "");
                String mmm = "";
                switch (status) {
                    case PayArgsCheckResult.CHECK_RESULT_PAY_CALLBACK_NULL:
                        mmm = "参数错误:回调函数未配置";
//                        Log.i("tag","支付结果"+mmm);
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_AMOUNT:
                        mmm = "参数错误:金额无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_NAME:
                        mmm = "参数错误:游戏名称无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_ORDER_ID:
                        mmm = "参数错误:APP_APP_ORDER_ID无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_USER_ID:
                        mmm = "参数错误:APP_USER_ID无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_USER_NAME:
                        mmm = "参数错误:APP_USER_NAME无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_NOTIFY_URI:
                        mmm = "参数错误:NOTIFY_URI无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_OPEN_UID:
                        mmm = "参数错误:OPEN_UID无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_PRODUCT_ID:
                        mmm = "参数错误:PRODUCT_ID无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_PRODUCT_NAME:
                        mmm = "参数错误:PRODUCT_NAME无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_KEY:
                        mmm = "参数错误:APP_KEY无效";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_SUCCESS:
                        mmm = "支付成功";
                    	paySuce();
                    	Toast("支付成功");
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_FAILURE:
                        mmm = "支付失败";
                    	payFail();
                    	Toast("支付失败");
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_CANCEL:
                        mmm = "支付取消";
                    	payFail();
                    	Toast("支付取消");
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_WAIT:
                        mmm = "支付等待";
                    	payFail();
                    	Toast("支付等待中");
                        break;
                }
//                msg.setText(mmm + "");
                Log.i("tag","支付结果："+mmm);
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

		//当客户点击退出按钮时(用户想退出游戏时)调用,SDK显示退出确认窗口。当用户选择确定退出后，
    	//回调结果码返回LogoutResult.LOGOUT_CODE_OUT。这时，游戏进行退出游戏操作，关闭整个程序。
		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
        PPayCenter.loginOut(paramActivity,Integer.parseInt(uid), new PpaLogoutCallback() {
            @Override
            public void onLoginOut(LogoutResult logoutResult) {
                switch (logoutResult.getCode()) {
                    case LogoutResult.LOGOUT_CODE_OUT:
//                        finish();
                    	callback.onExit();
                        break;
                    case LogoutResult.LOGOUT_CODE_BBS:

                        break;
                }
                Log.e("退出登录", "是否是退出 " + logoutResult.getCode() + "  loggetMessage=" + logoutResult.getMessage());
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
		if(roleName.equals("")){
			roleName = "001";
		}
		//1为角色登陆成功  2为角色创建  3为角色升级
		if (Integer.parseInt(ext) == 1){
//		3.5  进入游戏接口【客户端调用】( 必接)
			PPayCenter.enterGame(roleName,0,Integer.parseInt(zoneId),roleId,"0");
		}else if (Integer.parseInt(ext) == 2){
			//  创建角色接口【客户端调用】
			PPayCenter. createRole(roleName,0,Integer.parseInt(zoneId));
		}
		
	}

	public static void onResume(Activity paramActivity) {
		PPayCenter.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		PPayCenter.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		PPayCenter.destroy();
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
