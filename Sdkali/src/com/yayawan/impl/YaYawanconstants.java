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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.even.SDKEventKey;
import cn.uc.gamesdk.even.SDKEventReceiver;
import cn.uc.gamesdk.even.Subscribe;
import cn.uc.gamesdk.exception.AliLackActivityException;
import cn.uc.gamesdk.exception.AliNotInitException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.UCOrientation;
import cn.uc.gamesdk.param.SDKParamKey;
import cn.uc.gamesdk.param.SDKParams;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	// private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static Handler handler;

	public static boolean mRepeatCreate = false;

	private static String uid;
	private static String bufanuid;
	private static String bufantoken;

	private static String sign;

	private static int isyoumeng;

	private static YYWExitCallback ExitCallback;

	private static String token;

	private static String paystatus = "1";// 订单状态

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		if ((mActivity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			// Log.i(TAG, "onCreate with flag FLAG_ACTIVITY_BROUGHT_TO_FRONT");
			mRepeatCreate = true;
			mActivity.finish();
			return;
		}
		// ButterKnife.bind(mactivity);

		ucNetworkAndInitUCGameSDK((getPullupInfo(mActivity.getIntent())));
		handler = new Handler(Looper.getMainLooper());
		UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);

		String youmeng = DeviceUtil.getGameInfo(mActivity, "isyoumeng");
		isyoumeng = Integer.parseInt(youmeng);
		if (isyoumeng == 1) {
			UMGameAgent.setDebugMode(true);
			UMGameAgent.init(mActivity);
		}

		isinit = true;
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
			try {
				UCGameSdk.defaultSdk().login(mactivity, null);
			} catch (AliLackActivityException e) {
				e.printStackTrace();
			} catch (AliNotInitException e) {
				e.printStackTrace();
			}
		} else {
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid, String sign) {
		Yayalog.loger("YaYawanconstantssdk支付");
		SDKParams sdkParams = new SDKParams();
		sdkParams.put(SDKParamKey.CALLBACK_INFO, "");
		sdkParams.put(SDKParamKey.NOTIFY_URL, "");
		sdkParams.put(SDKParamKey.AMOUNT, YYWMain.mOrder.money / 100 + ".00");
		sdkParams.put(SDKParamKey.CP_ORDER_ID, morderid);
		sdkParams.put(SDKParamKey.ACCOUNT_ID, uid);
		sdkParams.put(SDKParamKey.SIGN_TYPE, "MD5");
		sdkParams.put(SDKParamKey.SIGN, sign);
		Log.i("tag", "sdkParams = " + sdkParams);
		// 以上字段的值都需要由游戏服务器生成,各字段详细说明，见第12页SDKParamKey参数表
		try {
			UCGameSdk.defaultSdk().pay(mactivity, sdkParams);
		} catch (IllegalArgumentException e) {
			// 传入参数错误异常处理
		} catch (AliNotInitException e) {
			// 未初始化或正在初始化时，异常处理
		} catch (Exception e) {
			// 未初始化或正在初始化时，异常处理
		}

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
			public void run() {
				try {
					ExitCallback = callback;
					UCGameSdk.defaultSdk().exit(paramActivity, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		Yayalog.loger("YaYawanconstants设置角色信息");
		SDKParams sdkParams = new SDKParams();
		sdkParams.put(SDKParamKey.STRING_ROLE_ID, roleId);
		sdkParams.put(SDKParamKey.STRING_ROLE_NAME, roleName);
		sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, roleLevel);
		sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, roleCTime);
		sdkParams.put(SDKParamKey.STRING_ZONE_ID, zoneId);
		sdkParams.put(SDKParamKey.STRING_ZONE_NAME, zoneName);
		// 1为角色登陆成功 2为角色创建 3为角色升级。
		if (Integer.parseInt(ext) == 1) {
			if (isyoumeng == 1) {
				Log.i("tag", "友盟进入游戏");
				MobclickAgent.onProfileSignIn(uid);
			}
			try {
				UCGameSdk.defaultSdk().submitRoleData(paramActivity, sdkParams);
				Log.i("tag", "登陆成功,数据已提交,查看数据是否正确，请到开放平台接入联调工具查看");
			} catch (AliNotInitException e) {
				e.printStackTrace();
			} catch (AliLackActivityException e) {
				e.printStackTrace();
			}
		} else if (Integer.parseInt(ext) == 2) {
			try {
				UCGameSdk.defaultSdk().submitRoleData(paramActivity, sdkParams);
				Log.i("tag", "角色创建,数据已提交,查看数据是否正确，请到开放平台接入联调工具查看");
			} catch (AliNotInitException e) {
				e.printStackTrace();
			} catch (AliLackActivityException e) {
				e.printStackTrace();
			}
		} else if (Integer.parseInt(ext) == 3) {
			try {
				UCGameSdk.defaultSdk().submitRoleData(paramActivity, sdkParams);
				Log.i("tag", "角色升级,数据已提交,查看数据是否正确，请到开放平台接入联调工具查看");
			} catch (AliNotInitException e) {
				e.printStackTrace();
			} catch (AliLackActivityException e) {
				e.printStackTrace();
			}
		}
	}

	public static void onResume(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "onResume is repeat activity!");
			return;
		}
		if (isyoumeng == 1) {
			MobclickAgent.onResume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "AppActivity:onPause is repeat activity!");
			return;
		}
		if (isyoumeng == 1) {
			MobclickAgent.onPause(paramActivity);
		}
	}

	public static void onDestroy(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "onDestroy is repeat activity!");
			return;
		}

	}

	public static void onActivityResult(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "onActivityResult is repeat activity!");
			return;
		}

	}

	public static void onNewIntent(Intent paramIntent) {
		if (mRepeatCreate) {
			Log.i("tag", "onNewIntent is repeat activity!");
			return;
		}

	}

	public static void onStart(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "onStart is repeat activity!");
			return;
		}

	}

	public static void onRestart(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "onRestart is repeat activity!");
			return;
		}

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		if (mRepeatCreate) {
			Log.i("tag", "onStop is repeat activity!");
			return;
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
		mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (YYWMain.mPayCallBack != null) {
					YYWMain.mPayCallBack.onPayFailed(null, null);
				}
			}
		});
	}

	/*
	 * Toast提示
	 */
	public static void Toast(final String msg) {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private static String getPullupInfo(Intent intent) {
		if (intent == null) {
			return null;
		}
		String pullupInfo = intent.getDataString();
		if (TextUtils.isEmpty(pullupInfo)) {
			pullupInfo = intent.getStringExtra("data");
		}

		return pullupInfo;
	}

	static SDKEventReceiver receiver = new SDKEventReceiver() {

		@Subscribe(event = SDKEventKey.ON_INIT_SUCC)
		private void onInitSucc() {
			// 初始化成功
			handler.post(new Runnable() {

				@Override
				public void run() {
					// startGame();
					isinit = true;
					login(mActivity);
				}
			});
		}

		@Subscribe(event = SDKEventKey.ON_INIT_FAILED)
		private void onInitFailed(String data) {
			// 初始化失败
			Toast.makeText(mActivity, "init failed", Toast.LENGTH_SHORT).show();
			ucNetworkAndInitUCGameSDK(null);
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
		private void onLoginSucc(String sid) {
			// Toast.makeText(mActivity, "login succ,sid=" + sid,
			// Toast.LENGTH_SHORT).show();
			Log.i("tag", "sid = " + sid);
			HttpPost(sid);
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
		private void onLoginFailed(String desc) {
			Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
			loginFail();
			// printMsg(desc);
		}

		@SuppressWarnings("unchecked")
		@Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
		private void onCreateOrderSucc(OrderInfo orderInfo) {
			// dumpOrderInfo(orderInfo);
			if (orderInfo != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("'orderId':'%s'",
						orderInfo.getOrderId()));
				sb.append(String.format("'orderAmount':'%s'",
						orderInfo.getOrderAmount()));
				sb.append(String.format("'payWay':'%s'", orderInfo.getPayWay()));
				sb.append(String.format("'payWayName':'%s'",
						orderInfo.getPayWayName()));
				Log.i("tag",
						"此处为订单生成回调，客户端无支付成功回调，订单是否成功以服务端回调为准: callback orderInfo = "
								+ sb);
				
				
			}
		}

		@Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
		private void onPayUserExit(OrderInfo orderInfo) {
			// dumpOrderInfo(orderInfo);
			if (orderInfo != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("'orderId':'%s'",
						orderInfo.getOrderId()));
				sb.append(String.format("'orderAmount':'%s'",
						orderInfo.getOrderAmount()));
				sb.append(String.format("'payWay':'%s'", orderInfo.getPayWay()));
				sb.append(String.format("'payWayName':'%s'",
						orderInfo.getPayWayName()));
				final String orderid = orderInfo.getOrderId();
				HttpPost(uid, token, orderid);
				Log.i("tag", "支付界面关闭: callback orderInfo = " + sb);
			}
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
		private void onLogoutSucc() {
			Toast.makeText(mActivity, "logout succ", Toast.LENGTH_SHORT).show();
			// AccountInfo.instance().setSid("");
			// ucSdkLogin();
			loginOut();
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
		private void onLogoutFailed() {
			Toast.makeText(mActivity, "logout failed", Toast.LENGTH_SHORT)
					.show();
			// printMsg("注销失败");
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
		private void onExit(String desc) {
			// Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
			// if (isyoumeng == 1) {
			// Log.i("tag", "友盟退出");
			// MobclickAgent.onProfileSignOff();
			// MobclickAgent.onKillProcess(mActivity);
			// }
			// mActivity.finish();
			ExitCallback.onExit();
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
		private void onExitCanceled(String desc) {
			Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
		}

	};

	public static void ucNetworkAndInitUCGameSDK(String pullUpInfo) {
		// !!!在调用SDK初始化前进行网络检查
		// 当前没有拥有网络
		if (false == APNUtil.isNetworkAvailable(mActivity)) {
			AlertDialog.Builder ab = new AlertDialog.Builder(mActivity);
			ab.setMessage("网络未连接,请设置网络");
			ab.setPositiveButton("设置", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent("android.settings.SETTINGS");
					mActivity.startActivityForResult(intent, 0);
				}
			});
			ab.setNegativeButton("退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
				}
			});
			ab.show();
		} else {
			ucSdkInit(pullUpInfo);// 执行UCGameSDK初始化
		}
	}


	private static void ucSdkInit(String pullUpInfo) {
		GameParamInfo gameParamInfo = new GameParamInfo();
		// gameParamInfo.setCpId(UCSdkConfig.cpId);已废用
		int gameid = Integer.parseInt(""
				+ DeviceUtil.getGameInfo(mActivity, "gameId"));
		gameParamInfo.setGameId(gameid);
		gameParamInfo.setEnableUserChange(true);// 开启账号切换功能
		// gameParamInfo.setServerId(UCSdkConfig.serverId);已废用
		gameParamInfo
				.setOrientation(DeviceUtil.isLandscape(mActivity) ? UCOrientation.LANDSCAPE
						: UCOrientation.PORTRAIT);

		SDKParams sdkParams = new SDKParams();

		sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
		sdkParams.put(SDKParamKey.PULLUP_INFO, pullUpInfo);

		// 联调环境已经废用
		// sdkParams.put(SDKParamKey.DEBUG_MODE, UCSdkConfig.debugMode);

		try {
			UCGameSdk.defaultSdk().initSdk(mActivity, sdkParams);
		} catch (AliLackActivityException e) {
			e.printStackTrace();
		}
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
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpPost = new HttpPost(
							"https://api.sdk.75757.com/data/get_uid/");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("app_id", DeviceUtil
							.getAppid(mActivity)));
					params.add(new BasicNameValuePair("sid", sid));

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
							String re = EntityUtils.toString(httpResponse
									.getEntity());
							Log.i("tag", "re=" + re);
							JSONObject js = new JSONObject(re);
							Log.i("tag", "js=" + js);
							uid = js.getString("uid");
							Log.i("tag", "uid=" + uid);
							Log.i("tag", "token=" + token);
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					bufanuid = YYWMain.mUser.yywuid;
					bufantoken = YYWMain.mUser.yywtoken;
					HttpPost httpPost = new HttpPost(
							"https://api.sdk.75757.com/pay/order_status/");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("app_id", DeviceUtil
							.getAppid(mActivity)));
					params.add(new BasicNameValuePair("uid", bufanuid));
					params.add(new BasicNameValuePair("token", bufantoken));
					params.add(new BasicNameValuePair("billid", orderid));

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
							String re = EntityUtils.toString(httpResponse
									.getEntity());
							Log.i("tag", "re=" + re);
							JSONObject js = new JSONObject(re);
							Log.i("tag", "js=" + js);
							paystatus = js.getString("status");
//							int paystatus = js.getInt("status");
							Log.i("tag", "paystatus支付=" + paystatus);
							if ((paystatus.equals("2")) || (paystatus.equals("3"))) {
								paySuce();
								Log.i("tag", "支付成功");
								Toast("支付成功");
							} else {
								payFail();
								Toast("支付失败");
								Log.i("tag", "支付失败");
							}

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
