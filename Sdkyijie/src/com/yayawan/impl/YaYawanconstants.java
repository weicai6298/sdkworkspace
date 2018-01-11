package com.yayawan.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
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
import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	// private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		SFOnlineHelper.onCreate(mactivity, new SFOnlineInitListener() {
			@Override
			public void onResponse(String tag, String value) {
				if (tag.equalsIgnoreCase("success")) {
					// 初始化成功的回调
					isinit = true;
				} else if (tag.equalsIgnoreCase("fail")) {
					// 初始化失败的回调，value：如果SDK返回了失败的原因，会给value赋值
				}
			}
		});

		SFOnlineHelper.setLoginListener(mactivity, new SFOnlineLoginListener() {
			@Override
			public void onLoginSuccess(SFOnlineUser user, Object customParams) {
				// 登陆成功回调
				// String uid = user.getChannelUserId();
				String id = user.getChannelUserId();
				String stoken = user.getChannelId() + "||" + user.getToken();
				String uid = null;
				String token = null;
				try {
					uid = URLEncoder.encode(id, "UTF-8");
					token = URLEncoder.encode(stoken, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Log.i("tag", "uid =" + uid);
				Log.i("tag", "token =" + token);
				loginSuce(mactivity, uid, uid, token);
				Log.i("tag", "user.getProductCode() =" + user.getProductCode());
				Log.i("tag", "user.getChannelId() =" + user.getChannelId());
				Log.i("tag",
						"user.getChannelUserId() =" + user.getChannelUserId());
				Log.i("tag", "user.getToken() =" + user.getToken());
				Toast("登录成功");
			}

			@Override
			public void onLoginFailed(String reason, Object customParams) {
				// 登陆失败回调
				Log.i("tag", "登录失败reason =" + reason);
				loginFail();
				Toast("登录失败");
			}

			@Override
			public void onLogout(Object customParams) {
				// 登出回调
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loginOut();
					}
				});
			}
		});
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
			SFOnlineHelper.login(mactivity, "Login");
		} else {
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
		int money = Integer.parseInt(YYWMain.mOrder.money + "");
		String goods = YYWMain.mOrder.goods;
		int count = 1;
		String callBackInfo = morderid;
		String callBackUrl = DeviceUtil.getGameInfo(mactivity, "callback");
		SFOnlineHelper.pay(mactivity, money, goods, count, callBackInfo,
				callBackUrl, new SFOnlinePayResultListener() {

					@Override
					public void onSuccess(String remain) {
						Log.i("tag", "支付成功remain=" + remain);
						paySuce();
						Toast("支付成功");
					}

					@Override
					public void onOderNo(String remain) {
						Log.i("tag", "支付失败remain=" + remain);
						payFail();
						Toast("支付失败");
					}

					@Override
					public void onFailed(String remain) {
						Log.i("tag", "支付失败remain=" + remain);
						payFail();
						Toast("支付失败");
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
				SFOnlineHelper.exit(paramActivity, new SFOnlineExitListener() {
					/*
					 * onSDKExit
					 * 
					 * @description 当SDK有退出方法及界面，回调该函数
					 * 
					 * @param bool SDK是否退出标志位
					 */
					@Override
					public void onSDKExit(boolean arg0) {
						// SDK已经退出，此处可以调用游戏的退出函数
						if (arg0) {
							callback.onExit();
						}
					}

					/*
					 * onNoExiterProvide
					 * 
					 * @description SDK没有退出方法及界面，回调该函数，可在此使用游戏退出界面
					 */
					@Override
					public void onNoExiterProvide() {
						KgameSdk.Exitgame(paramActivity,
								new KgameSdkCallback() {

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
		});
	}

	/**
	 * 设置角色信息
	 * 
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		Yayalog.loger("YaYawanconstants设置角色信息");
		// 1为角色登陆成功 2为角色创建 3为角色升级。
		if (Integer.parseInt(ext) == 1) {
			SFOnlineHelper.setRoleData(paramActivity, roleId, roleName,
					roleLevel, zoneId, zoneName);
		}
		if (Integer.parseInt(ext) == 1) {
			JSONObject roleInfo = new JSONObject();
			try {
				roleInfo.put("roleId", roleId);
				// 当前登录的玩家角色 ID，必须为数字
				roleInfo.put("roleName", roleName); // 当前登录的玩家角色名，不能为空，不能为 null
				roleInfo.put("roleLevel", roleLevel); // 当前登录的玩家角色等级，必须为数字，且不能为
														// 0，若无，传入 1
				roleInfo.put("zoneId", zoneId); // 当前登录的游戏区服 ID，必须为数字，且不能为
												// 0，若无，传入 1
				roleInfo.put("zoneName", zoneName); // 当前登录的游戏区服名称，不能为空，不能为null
				roleInfo.put("balance", "0"); // 用户游戏币余额，必须为数字，若无，传入 0
				roleInfo.put("vip", "1"); // 当前用户 VIP 等级，必须为数字，若无，传入 1
				roleInfo.put("partyName", "无帮派"); // 当前角色所属帮派，不能为空，不能为
													// null，若无，传入“无帮派”
				roleInfo.put("roleCTime", roleCTime); // 单位为秒，创建角色的时间
				roleInfo.put("roleLevelMTime", "54456556"); // 单位为秒，角色等级变化时间
				SFOnlineHelper.setData(paramActivity, "enterServer",
						roleInfo.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (Integer.parseInt(ext) == 2) {
			JSONObject roleInfo = new JSONObject();
			try {
				roleInfo.put("roleId", roleId);
				// 当前登录的玩家角色 ID，必须为数字
				roleInfo.put("roleName", roleName); // 当前登录的玩家角色名，不能为空，不能为 null
				roleInfo.put("roleLevel", roleLevel); // 当前登录的玩家角色等级，必须为数字，且不能为
														// 0，若无，传入 1
				roleInfo.put("zoneId", zoneId); // 当前登录的游戏区服 ID，必须为数字，且不能为
												// 0，若无，传入 1
				roleInfo.put("zoneName", zoneName); // 当前登录的游戏区服名称，不能为空，不能为null
				roleInfo.put("balance", "0"); // 用户游戏币余额，必须为数字，若无，传入 0
				roleInfo.put("vip", "1"); // 当前用户 VIP 等级，必须为数字，若无，传入 1
				roleInfo.put("partyName", "无帮派"); // 当前角色所属帮派，不能为空，不能为
													// null，若无，传入“无帮派”
				roleInfo.put("roleCTime", roleCTime); // 单位为秒，创建角色的时间
				roleInfo.put("roleLevelMTime", "54456556"); // 单位为秒，角色等级变化时间
				SFOnlineHelper.setData(paramActivity, "createrole",
						roleInfo.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (Integer.parseInt(ext) == 3) {
			JSONObject roleInfo = new JSONObject();
			try {
				roleInfo.put("roleId", roleId);
				roleInfo.put("roleName", roleName); // 当前登录的玩家角色名，不能为空，不能为 null
				roleInfo.put("roleLevel", roleLevel); // 当前登录的玩家角色等级，必须为数字，且不能为
														// 0，若无，传入 1
				roleInfo.put("zoneId", zoneId); // 当前登录的游戏区服 ID，必须为数字，且不能为
												// 0，若无，传入 1
				roleInfo.put("zoneName", zoneName); // 当前登录的游戏区服名称，不能为空，不能为null
				roleInfo.put("balance", "0"); // 用户游戏币余额，必须为数字，若无，传入 0
				roleInfo.put("vip", "1"); // 当前用户 VIP 等级，必须为数字，若无，传入 1
				roleInfo.put("partyName", "无帮派"); // 当前角色所属帮派，不能为空，不能为
													// null，若无，传入“无帮派”
				roleInfo.put("roleCTime", roleCTime); // 单位为秒，创建角色的时间
				roleInfo.put("roleLevelMTime", "54456556"); // 单位为秒，角色等级变化时间
				SFOnlineHelper.setData(paramActivity, "levelup",
						roleInfo.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void onResume(Activity paramActivity) {
		SFOnlineHelper.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		SFOnlineHelper.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		SFOnlineHelper.onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity paramActivity) {

	}

	public static void onRestart(Activity paramActivity) {
		SFOnlineHelper.onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		SFOnlineHelper.onStop(paramActivity);
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
	public static void Toast(final String msg) {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
