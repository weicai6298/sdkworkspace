package com.yayawan.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.samsung.interfaces.callback.IPayResultCallback;
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
import com.samsung.interfaces.callback.ILoginResultCallback;
import com.samsung.sdk.main.IAppPay;
import com.samsung.sdk.main.IAppPayOrderUtils;
import com.samsung.sdk.notice.SamsungNoticeSdk;
import com.samsung.sdk.notice.callback.SamsungNoticeLoginCallback;
import com.samsung.sdk.notice.callback.SamsungNoticeQuitCallback;
import com.samsung.sdk.notice.main.SamsungNoticeSignUtils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	private static String appId;
	public static String uid;
	private static String token;
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		int screenType= DeviceUtil.isLandscape(mActivity)?IAppPay.LANDSCAPE:IAppPay.PORTRAIT;
		appId = DeviceUtil.getGameInfo(mactivity, "sanxing_appId");
		IAppPay.init(mactivity, screenType, appId, "");
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
			String privateKey = DeviceUtil.getGameInfo(mactivity, "sanxing_privateKey");
			String packageStr = mactivity.getPackageName();
			String loginParams = IAppPayOrderUtils.getLoginParams(appId,packageStr,privateKey);
			IAppPay.startLogin(mactivity, loginParams, new ILoginResultCallback() {
				
				@Override
				public void onSuccess(String signValue, Map<String, String> resultMapStr) {
//					  Toast.makeText(GoodsListActivity.this, "获取到的signValue：" + signValue, Toast.LENGTH_SHORT).show();
		                Log.d("GoodsListActivity","获取到的signValue:" + signValue);
		                //接入方app ---signValue--> 接入方服务器 ---signValue--> 爱贝服务器
		                //接入方app <---用户信息--  接入方服务器  <---用户信息-- 爱贝服务器
		                HttpPost(signValue);
				}
				
				@Override
				public void onFaild(String errorCode, String errorMessage) {
					loginFail();
					Log.i("tag","登录失败，错误信息:" + errorMessage + ",错误代码:" + errorCode);
					Toast("登录失败");
				}
				
				@Override
				public void onCanceled() {
					loginFail();
					Toast("登录取消");
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
	public static void pay(Activity mactivity, String morderid,String ipay_orderId) {
		Yayalog.loger("YaYawanconstantssdk支付");
		String parm = "transid="+ipay_orderId+"&appid="+appId;
		String privateKey = DeviceUtil.getGameInfo(mactivity, "sanxing_privateKey");
		String packageStr = mactivity.getPackageName();
		String loginParams = IAppPayOrderUtils.getLoginParams(appId,packageStr,privateKey);
		
		IAppPay.startPay(mactivity,loginParams,parm, new IPayResultCallback(){

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
				SamsungNoticeSdk.showQuitNotice(paramActivity, new SamsungNoticeQuitCallback() {
		            @Override
		            public void noticeQuitCallBack(int code, String reason) {
		                switch (code){
		                    case SamsungNoticeSdk.NOTICE_PARAMETER_ERROR:
		                        //参数有问题，检查传入接口的参数。
		                    	Log.i("tag","参数有问题，检查传入接口的参数");
		                        break;
		                    case SamsungNoticeSdk.NOTICE_CANCEL:
		                        //用户主动点击弹窗上的取消按钮，此时关闭弹窗不退出游戏
		                    	Log.i("tag","用户主动点击弹窗上的取消按钮，此时关闭弹窗不退出游戏");
		                        break;
		                    case SamsungNoticeSdk.NOTICE_QUIT:
		                        //用户主动点击弹窗上的退出游戏按钮，此时退出游戏
		                    	Log.i("tag","退出游戏");
		                    	callback.onExit();
		                        break;
		                    default:
		                        break;
		                }
		                Log.i("tag","退出游戏reason = " +reason);
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
							uid = obj.getString("uid");
							Yayalog.loger("uid ="+uid);
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	
	 /**
     * 1、登录之后调用
     * 登录三星账号之后，调起登录公告弹窗展示登录活动信息
     */
    private static void showLoginNotice(){
        /**
         * 获取登录公告弹窗接口参数
         * 1、登录弹窗接口参数是 appid和package的签名值：
         * 	 String data = AppID +"&"+ 包名。
         *   String signData = sign(data);
         *
         * 2、重要说明：
         *   这里是为了方便演示在客户端生成 signData，所以Demo中加签过程直接放在客户端完成。
         *   真实App里，privateKey等数据严禁放在客户端，加签过程务必在服务端完成；（服务端代码示例有签名代码）
         *   防止商户私密数据泄露，造成不必要的损失。
         */
        String privateKey = DeviceUtil.getGameInfo(mActivity, "sanxing_privateKey");
		String packageStr = mActivity.getPackageName();
		String signData = SamsungNoticeSignUtils.getNoticeParams(appId,packageStr,privateKey);

        //如果不需要登录公告弹窗的回调可以传null
        SamsungNoticeSdk.showLoginNotice(mActivity, appId, signData, new SamsungNoticeLoginCallback() {
            @Override
            public void noticeLoginCallBack(int code, String reason) {
                switch (code){
                    case SamsungNoticeSdk.NOTICE_PARAMETER_ERROR:
                        //参数有问题，检查传入接口的参数。
                    	Log.i("tag","参数有问题，检查传入接口的参数");
                        break;
                    case SamsungNoticeSdk.NOTICE_NO_DATA:
                         //没有配置公告数据，弹窗不显示。
                    	Log.i("tag","没有配置公告数据，弹窗不显示");
                        break;
                    case SamsungNoticeSdk.NOTICE_NETWORK_ERROR:
                        //获取公告数据时网络错误，弹窗不显示。
                    	Log.i("tag","获取公告数据时网络错误，弹窗不显示");
                        break;
                    case SamsungNoticeSdk.NOTICE_CANCEL:
                        //如果需要在登录公告弹窗关闭后处理一些逻辑，可以在此操作。
                    	Log.i("tag","登录公告弹窗关闭");
                        break;
                    default:
                        break;
                }
//                Toast.makeText(OpenDialogActivity.this,reason,Toast.LENGTH_SHORT).show();
                Log.i("tag","登录公告弹窗reason = " + reason);
            }
        });
    }
}
