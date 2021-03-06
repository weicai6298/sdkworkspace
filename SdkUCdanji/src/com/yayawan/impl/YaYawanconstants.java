package com.yayawan.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Build;
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
import cn.uc.paysdk.face.commons.SDKProtocolKeys;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings({ "deprecation", "unused" })
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static Handler handler;
	
	public static boolean mRepeatCreate = false;
	
	private static YYWExitCallback ExitCallback;
	
	private static String paystatus = "1";// 订单状态
	
	private static String uid;
	private static String bufanuid;
	private static String bufantoken;
	
	private static String token;
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		
		if ((mactivity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            Log.i("tag", "onCreate with flag FLAG_ACTIVITY_BROUGHT_TO_FRONT");
            mRepeatCreate = true;
            mActivity.finish();
            return;
        }
        handler = new Handler(Looper.getMainLooper());
        UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
        ucSdkInit(getPullupInfo(mActivity.getIntent()));
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
			String tempuid = Sputils.getSPstring("uid", "tem", mactivity);
			Log.i("tag","tempuid="+tempuid);
			if (tempuid.equals("tem")) {
				String uidtemp=System.currentTimeMillis()+"kk";
				String uid=uidtemp.substring(4, uidtemp.length())+new Random().nextInt(10);
				Sputils.putSPstring("uid", uid, mactivity);
//				loginSuce(mactivity, uid, uid, uid);
				HttpPost(uid);
			}else {
//				loginSuce(mactivity, tempuid, tempuid, tempuid);
				HttpPost(tempuid);
			}
			}else{
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
		SDKParams params = new SDKParams();
		params.put(SDKProtocolKeys.APP_NAME, DeviceUtil.getGameInfo(mActivity, "gamename"));
		params.put(SDKProtocolKeys.PRODUCT_NAME, YYWMain.mOrder.goods);
		params.put(SDKProtocolKeys.AMOUNT, YYWMain.mOrder.money/100);
		params.put(SDKProtocolKeys.NOTIFY_URL, "");
		params.put(SDKProtocolKeys.ATTACH_INFO, "");
		params.put(SDKProtocolKeys.CP_ORDER_ID, morderid);
		// 各字段说明，见SDKProtocolKeys参数表

		try {
		UCGameSdk.defaultSdk().pay(mactivity, params);
		} catch (IllegalArgumentException e) {
				//传入参数错误异常处理
		} catch (AliNotInitException e) {
				//未初始化或正在初始化时，异常处理
		} catch (Exception e) {
				//activity为空，异常处理
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
					paramActivity.finish();
				}
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
	
	
	static SDKEventReceiver receiver = new SDKEventReceiver() {

		@Subscribe(event = SDKEventKey.ON_INIT_SUCC)
		private void onInitSucc() {
			// 初始化成功
			handler.post(new Runnable() {

				@Override
				public void run() {
					isinit = true;
//					login(mActivity);
				}
			});
		}

		@Subscribe(event = SDKEventKey.ON_INIT_FAILED)
		private void onInitFailed(String data) {
			// 初始化失败
			Toast.makeText(mActivity, "初始化失败", Toast.LENGTH_SHORT).show();
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
		private void onLoginSucc(String sid) {
			Log.i("tag", "sid = " + sid);
//			HttpPost(sid);
		}

		@Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
		private void onLoginFailed(String desc) {
			Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
//			loginFail();
		}

		@Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
		private void onCreateOrderSucc(OrderInfo orderInfo) {
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
			loginOut();
		}

		@Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
		private void onLogoutFailed() {
			Toast.makeText(mActivity, "logout failed", Toast.LENGTH_SHORT)
					.show();
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
		private void onExit(String desc) {
			// if (isyoumeng == 1) {
			// Log.i("tag", "友盟退出");
			// MobclickAgent.onProfileSignOff();
			// MobclickAgent.onKillProcess(mActivity);
			// }
			 mActivity.finish();
//			ExitCallback.onExit();
		}

		@Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
		private void onExitCanceled(String desc) {
			Toast.makeText(mActivity, desc, Toast.LENGTH_SHORT).show();
		}
	};

	public static void onResume(Activity paramActivity) {
		if (mRepeatCreate) {
	        Log.i("tag", "onResume is repeat activity!");
	        return;
	    }
	}

	public static void onPause(Activity paramActivity) {
		 if (mRepeatCreate) {
		        Log.i("tag", "AppActivity:onPause is repeat activity!");
		        return;
		    }
	}

	public static void onDestroy(Activity paramActivity) {
		UCGameSdk.defaultSdk().unregisterSDKEventReceiver(receiver);
		if (mRepeatCreate) {
	        Log.i("tag", "onDestroy is repeat activity!");
	        return;
	    }
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
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
	
	
	
	
	/**
	 * 
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	private static void HttpPost(final String sid) {
		Log.i("tag", "sid=" + sid);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpPost = new HttpPost(
							"https://api.sdk.75757.com/data/login_handler/");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("app_id", DeviceUtil
							.getAppid(mActivity)));
					params.add(new BasicNameValuePair("uid", sid));

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
							JSONObject data = js.getJSONObject("data");
							Log.i("tag", "data=" + data);
							uid = data.optString("uid");
							token = data.optString("token");
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
