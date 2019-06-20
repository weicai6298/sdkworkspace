package com.yayawan.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReportUserGameInfoParam;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {


	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static int appidcharge = 0;


	private static String uid;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		String appSecret = ""
				+ DeviceUtil.getGameInfo(mactivity, "appSecret");
		GameCenterSDK.init(appSecret, mactivity);
		isinit = true;
		Log.i("tag", "oppo初始化结束");
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
				}
			});
		} else {
			inintsdk(mactivity);
		}
	}
	private static String stoken;

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
					try {
						stoken = URLEncoder.encode(token, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
//					getVerifiedInfo();
					 doGetUserInfoByCpClient(token, uid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String content, int resultCode) {

			}
		});
	}
	private static void doGetUserInfoByCpClient(String token, String ssoid) {
		GameCenterSDK.getInstance().doGetUserInfo(
				new ReqUserInfoParam(token, ssoid), new ApiCallback() {

					@Override
					public void onSuccess(String resultMsg) {
//						获取用户信息成功 = {"userName":"U197689309","adId":"0","channel":0,"email":"","mobile":"137******98","ssoid":"300789837"}
						Log.i("tag","获取用户信息成功 = " +resultMsg);
						try {
							JSONObject json = new JSONObject(resultMsg);
							int adId = Integer.parseInt(json.getString("adId"));
							int channel = Integer.parseInt(json.getString("channel"));
							Log.i("tag","adId = " +adId);
							Log.i("tag","channel = " +channel);
							if(channel == 3){
								//channel == 3 时更换appid跟支付回调
								String APP_ID= ""+DeviceUtil.getGameInfo(mActivity, "gg_appid");
								Log.i("tag","APP_ID="+APP_ID);
						        DeviceUtil.appid = APP_ID.substring(2, APP_ID.length());
						        DeviceUtil.gameid = APP_ID.substring(2, APP_ID.length());
						        Log.i("tag","gameid="+DeviceUtil.gameid);
						        appidcharge = 1;
							}
							Log.i("tag","uid="+uid);
							Log.i("tag","stoken="+stoken);
							Log.i("tag", "登录成功");
							loginSuce(mActivity, uid, uid, stoken);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {
						Log.i("tag","获取用户信息失败  resultMsg = " +resultMsg + "resultCode" +resultCode);
					}
				});
	}
//	protected static void getVerifiedInfo() {
//		GameCenterSDK.getInstance().doGetVerifiedInfo(new ApiCallback() {
//			@Override
//			public void onSuccess(String resultMsg) {
//				try {
//					//解析年龄（age）
//					int age = Integer.parseInt(resultMsg);
//					if (age < 18) {
//						//已实名但未成年，CP开始处理防沉迷
//					} else {
//						//已实名且已成年，尽情玩游戏吧
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(String resultMsg, int resultCode) {
//				if(resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_RESUME_GAME){
//					//实名认证失败，但还可以继续玩游戏
//				}else if(resultCode ==
//						ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_STOP_GAME){
//					//实名认证失败，不允许继续游戏，CP需自己处理退出游戏
//				}
//			}
//		});
//
//	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		int amount = Integer.parseInt(YYWMain.mOrder.money + ""); // 支付金额，单位分
		PayInfo payInfo = new PayInfo(morderid, "", amount);
		payInfo.setProductName(YYWMain.mOrder.goods);
		if(appidcharge == 1){
			payInfo.setCallbackUrl(DeviceUtil.getGameInfo(mActivity, "gg_callback"));
		}else {
			payInfo.setCallbackUrl(DeviceUtil.getGameInfo(mActivity, "callback"));
		}
		Log.i("tag","payinfo = " + payInfo);
		GameCenterSDK.getInstance().doPay(mactivity, payInfo,
				new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
				paySuce();
				Log.i("tag","支付成功 = " +resultMsg);
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				payFail();
				Log.i("tag", "支付失败resultMsg = "+resultMsg);
				Log.i("tag", "支付失败resultCode = "+resultCode);
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
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
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
		Yayalog.loger("YaYawanconstants设置角色信息");
		if (Integer.parseInt(ext) == 1) {

			GameCenterSDK.getInstance().doReportUserGameInfoData(
				    new ReportUserGameInfoParam(roleId, roleName,Integer.parseInt(roleLevel), zoneId, zoneName, "chapter", null), 
				       new ApiCallback() {

							@Override
							public void onSuccess(String resultMsg) {
								Log.i("tag","上报数据成功resultMsg = " +resultMsg);
							}

							@Override
							public void onFailure(String resultMsg, int resultCode) {
								Log.i("tag","上报数据失败resultMsg = " +resultMsg);
								Log.i("tag","上报数据失败resultCode = " +resultCode);
							}
						});
		}

	}

	public static void onResume(Activity paramActivity) {
//		GameCenterSDK.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
//		GameCenterSDK.getInstance().onPause();
	}

	public static void onDestroy(Activity paramActivity) {
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity2) {

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
