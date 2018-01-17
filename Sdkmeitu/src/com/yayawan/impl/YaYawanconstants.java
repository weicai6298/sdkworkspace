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
import com.meitu.library.gamecenter.MTOnlineGameSDK;
import com.meitu.library.gamecenter.MTOnlineGameSDKConfig;
import com.meitu.library.gamecenter.ResultCode;
import com.meitu.library.gamecenter.callback.IExitGameCallback;
import com.meitu.library.gamecenter.callback.ISDKResponse;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import static com.meitu.library.gamecenter.ResultCode.CODE_PAY_CANCEL;
import static com.meitu.library.gamecenter.ResultCode.CODE_PAY_FAIL;
import static com.meitu.library.gamecenter.ResultCode.CODE_PAY_SUCCESS;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	public static  String token;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		isinit = true ;
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		Yayalog.loger("application初始化");
		//初始化联运SDK配置
		String meitu_appid= ""+DeviceUtil.getGameInfo(applicationContext, "meitu_appid");
		String meitu_appkey= ""+DeviceUtil.getGameInfo(applicationContext, "meitu_appkey");
		MTOnlineGameSDKConfig config = new MTOnlineGameSDKConfig(meitu_appid
				, meitu_appkey);
		//游戏方接入需要设置为false
		config.setDebugMode(false);
		//        DeviceUtil.isLandscape(applicationContext)?MTOnlineGameSDKConfig.MTScreenOrientation.LANDSCAPE:MTOnlineGameSDKConfig.MTScreenOrientation.PORTRAIT
		config.setScreenOrientation(DeviceUtil.isLandscape(applicationContext)?
				MTOnlineGameSDKConfig.MTScreenOrientation.LANDSCAPE:MTOnlineGameSDKConfig.MTScreenOrientation.PORTRAIT);
		//初始化联运SDK
		MTOnlineGameSDK.initApplication(applicationContext, config);

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			MTOnlineGameSDK.login(mactivity, new ISDKResponse<Void>() {
				@Override
				public void onResponse(int i, String s, Void aVoid) {
					switch (i) {
					case ResultCode.CODE_LOGIN_FAIL:
						// 登录失败
						loginFail();
						Toast("登录失败");
						break;
					case ResultCode.CODE_LOGIN_SUCCESS:
						// 美图帐号登录成功,可以请求登录游戏。
//						loginGame();
						String uid = MTOnlineGameSDK.getUid(mactivity);
						token = MTOnlineGameSDK.getToken(mactivity); 
						Log.i("tag", "uid = " +uid);
						Log.i("tag", "token = " +token);
						loginSuce(mactivity, uid, uid, token);
						Toast("登录成功");
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
	public static void pay(Activity mactivity, String morderid ,String payinfo) {
		Yayalog.loger("YaYawanconstantssdk支付");
////		String productInfo = new ;
		Log.i("tag","YaYawanconstantssdk支付-payinfo = "+payinfo);
		MTOnlineGameSDK.pay(mactivity, payinfo , new ISDKResponse<Void>() {
			@Override
			public void onResponse(int resultCode, String resultDesc, Void aVoid) {
				switch (resultCode) {
				case CODE_PAY_FAIL:
					payFail();
//					Toast("支付失败");
				case CODE_PAY_CANCEL:
					payFail();
//					Toast("支付取消");
				case CODE_PAY_SUCCESS:
					// 3. 支付完成，支付结果不可靠，需要以游戏服务器的订单状态为准，需要查询游戏服务器作为最终结果。
					paySuce();
//					Toast("支付完成");
					break;
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
		
	
		MTOnlineGameSDK.exitGame(paramActivity, new IExitGameCallback() {
			@Override
			public void onExitGameConfirm() {
				// 做退出的处理。
						callback.onExit();
//				Toast("退出-游戏");
					}

			@Override
			public void onExitGameCancel() {
				// 退出游戏被取消
				Toast("继续游戏");
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
		MTOnlineGameSDK.onDestroy(paramActivity);
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

}
