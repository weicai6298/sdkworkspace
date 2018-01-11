package com.yayawan.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.ApiResult;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReportUserGameInfoParam;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static int isyoumeng;
	
	private static String uid;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		String youmeng = DeviceUtil.getGameInfo(mActivity, "isyoumeng");
		isyoumeng = Integer.parseInt(youmeng);
		if(isyoumeng == 1){
			UMGameAgent.setDebugMode(true);
			UMGameAgent.init(mActivity);
		}
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		Log.i("tag", "application初始化");
		String appSecret = ""
				+ DeviceUtil.getGameInfo(applicationContext, "appSecret");
		Log.i("tag", "appSecret=" + appSecret);
		GameCenterSDK.init(appSecret, applicationContext);
		isinit = true;
		Log.i("tag", "oppo初始化结束");
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if (isinit) {
			Log.i("tag", "登录-start");
			GameCenterSDK.getInstance().doLogin(mactivity, new ApiCallback() {

				@Override
				public void onSuccess(String resultMsg) {
					// 登录成功
					doGetTokenAndSsoid();
				}

				@Override
				public void onFailure(String resultMsg, int resultCode) {
					// 登录失败
					Log.i("tag", "登录失败");
					loginFail();
					Log.i("tag", "登录失败1");
				}
			});
		} else {
			inintsdk(mactivity);
		}
	}

	// 获取Token和SsoId
	public static void doGetTokenAndSsoid() {
		GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
				try {
					JSONObject json = new JSONObject(resultMsg);
					String token = json.getString("token");
					String ssoid = json.getString("ssoid");
					uid = ssoid;
					String stoken = null;
					try {
						stoken = URLEncoder.encode(token, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("tag", "token=" + token);
					Log.i("tag", "stoken=" + stoken);
					Log.i("tag", "ssoid=" + ssoid);
					Log.i("tag", "登录成功");
					loginSuce(mActivity, ssoid, ssoid, stoken);
					// getVerifiedInfo();
					Log.i("tag", "登录成功1");
					// doGetUserInfoByCpClient(token, ssoid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String content, int resultCode) {

			}
		});
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		int amount = Integer.parseInt(YYWMain.mOrder.money + ""); // 支付金额，单位分
		PayInfo payInfo = new PayInfo(morderid, "", amount);
		// payInfo.setProductDesc("商品描述");
		payInfo.setProductName(YYWMain.mOrder.goods);
		payInfo.setCallbackUrl(DeviceUtil.getGameInfo(mActivity, "callback"));
		GameCenterSDK.getInstance().doPay(mactivity, payInfo,
				new ApiCallback() {

					@Override
					public void onSuccess(String arg0) {
						// TODO Auto-generated method stub
						Log.i("tag", "支付成功");
						paySuce();
						Log.i("tag", "支付成功1");
					}

					@Override
					public void onFailure(String arg0, int arg1) {
						// TODO Auto-generated method stub
						Log.i("tag", "支付失败");
						payFail();
						Log.i("tag", "支付失败1");
					}
				});
	}

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		GameCenterSDK.getInstance().onExit(paramActivity,
				new GameExitCallback() {

					@Override
					public void exitGame() {
						// TODO Auto-generated method stub
						mActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(isyoumeng == 1){
									Log.i("tag", "友盟退出");
									MobclickAgent.onProfileSignOff();
									MobclickAgent.onKillProcess(mActivity);
								}
//								callback.onExit();
								mActivity.finish();
								System.exit(0);
							}
						});
					}
				});

		//

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		if (Integer.parseInt(ext) == 1) {
			if(isyoumeng ==1){
				Log.i("tag", "友盟进入游戏");
				MobclickAgent.onProfileSignIn(uid);
			}
			
			GameCenterSDK.getInstance().doReportUserGameInfoData(
					new ReportUserGameInfoParam("3574641", zoneId, roleName,
							roleLevel), new ApiCallback() {
						@Override
						public void onSuccess(String resultMsg) {
							// success
						}

						@Override
						public void onFailure(String resultMsg, int resultCode) {
							// failure
						}
					});
		}

	}

	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		GameCenterSDK.getInstance().onResume(paramActivity);
		if(isyoumeng == 1){
			MobclickAgent.onResume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		GameCenterSDK.getInstance().onPause();
		if(isyoumeng == 1){
			MobclickAgent.onPause(paramActivity);
		}
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub

	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub

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
		;
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

	/*
	 * 支付成功
	 */
	public static void paySuce() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

}
