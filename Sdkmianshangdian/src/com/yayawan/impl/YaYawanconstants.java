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

import android.R.integer;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.snail.sdk.core.Const;
import com.snailgame.mobilesdk.OnCommitCallback;
import com.snailgame.mobilesdk.OnInitCompleteListener;
import com.snailgame.mobilesdk.OnLoginProcessListener;
import com.snailgame.mobilesdk.OnPayProcessListener;
import com.snailgame.mobilesdk.SnailCommplatform;
import com.snailgame.mobilesdk.SnailErrorCode;
import com.snailgame.mobilesdk.entry.SnailAppInfo;
import com.snailgame.mobilesdk.entry.SnailBuyInfo;
import com.snailgame.mobilesdk.open.OnCloseListener;
import com.snailgame.mobilesdk.open.OnRealNameListener;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	// private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static int orientationType;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		init();
		SnailCommplatform.getInstance().createFloatView(mactivity, true, 0.3f);
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
			SnailCommplatform.getInstance().snailLogin(mactivity,
					new OnLoginProcessListener() {
						@Override
						public void finishLoginProcess(int code) {
							Log.i("tag", "finishLoginProcess = " + code);
							if (SnailErrorCode.SNAIL_COM_PLATFORM_SUCCESS == code) {
								// 登录成功，可以进行支付
								Log.i("tag", "login success");
								String sessionId = SnailCommplatform
										.getInstance().getSessionId();
								String uin = SnailCommplatform.getInstance()
										.getLoginUin();
								Log.i("tag", "session id is " + sessionId);
								Log.i("tag", "uin is " + uin);
								loginSuce(mactivity, uin, uin, sessionId);
								checkrealname();
								Toast("登录成功");
							} else if (SnailErrorCode.SNAIL_COM_PLATFORM_ERROR_CANCEL == code) {
								// 登录取消
								// resultText.setText("login canceled. code is "
								// + code);
								loginFail();
								Toast("登录取消");
							} else {
								// 登录失败
								// resultText.setText("login failed. code is " +
								// code);
								Log.i("tag", "login failed code is " + code);
								loginFail();
								Toast("登录失败");
							}
						}
					});
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
		SnailBuyInfo buyInfo = new SnailBuyInfo();
		buyInfo.setSerial(morderid);
		buyInfo.setProductId("s_" + YYWMain.mOrder.goods);// 产品ID
		buyInfo.setProductName(YYWMain.mOrder.goods);// 产品名称
		buyInfo.setProductPrice(YYWMain.mOrder.money / 100);// 产品价格
		buyInfo.setPayDescription(YYWMain.mOrder.goods);// 描述
		
		 // 调用异步支付接口
        int payAsynError = SnailCommplatform.getInstance().snailUniPayAsyn(
        		buyInfo, mactivity, new OnPayProcessListener() {
                    @Override
                    public void finishPayProcess(int code) {
                        switch (code) {
                            case SnailErrorCode.SNAIL_COM_PLATFORM_SUCCESS:// 支付成功
                            	paySuce();
    							Toast("支付成功");
                                break;
                            case SnailErrorCode.SNAIL_COM_PLATFORM_ERROR_PAY_CANCEL:// 支付取消
                            	payFail();
    							Toast("支付取消");
                                break;
                            case SnailErrorCode.SNAIL_COM_PLATFORM_ERROR_PAY_REQUEST_SUBMITTED:
                            	payFail();
    							Toast("订单已提交，请稍后！");
                            	break;
                            default:// 支付失败
                            	payFail();
    							Toast("支付失败");
                                break;
                        }
                    }
                });
        if (payAsynError != 0) {
        	Log.i("tag","pay failed. payError is " + payAsynError);
        	payFail();
			Toast("支付失败");
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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		SnailCommplatform.getInstance().onLogout(paramActivity,
				new OnCloseListener() {
					@Override
					public void cancel() {
						Log.i("tag", "用户取消退出！！！！");
						Toast("继续游戏");
					}

					@Override
					public void close() {
						Log.i("tag", "用户退出游戏！！！！");
						// finish();
//						callback.onExit();
						mActivity.finish();
						System.exit(0);
					}
				});
	}
});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(final Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		Yayalog.loger("YaYawanconstants设置角色信息");
		// 角色创建时间
		// HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		if (Integer.parseInt(ext) == 1){
			 SnailCommplatform.getInstance().snailLevelUpload(Integer.parseInt(roleLevel), roleName,
                     new OnCommitCallback() {
                         @Override
                         public void result(int code) {
//                             Toast.makeText(paramActivity, "上传等级 code:" + code, Toast.LENGTH_SHORT).show();
                        	 Log.i("tag","上传等级 code = " + code);
                         }
                     });
			 SnailCommplatform.getInstance().snailSetGameServerInfo(zoneId, zoneName);
			 Log.i("tag","上传玩家服务器信息");
		}
	}

	public static void onResume(Activity paramActivity) {
		SnailCommplatform.getInstance().snailOnResume();
	}

	public static void onPause(Activity paramActivity) {
		SnailCommplatform.getInstance().snailOnPause();
	}

	public static void onDestroy(Activity paramActivity) {
		SnailCommplatform.getInstance().destoryFloatView(paramActivity);
		SnailCommplatform.getInstance().snailOnDestroy();
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity paramActivity) {
		SnailCommplatform.getInstance().showFloatView(true);
		SnailCommplatform.getInstance().onStart();

	}

	public static void onRestart(Activity paramActivity) {
	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		 SnailCommplatform.getInstance().onStop();
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

	private static void init() {
		int screenType = DeviceUtil.isLandscape(mActivity) ? SnailCommplatform.SCREEN_ORIENTATION_LANDSCAPE
				: SnailCommplatform.SCREEN_ORIENTATION_PORTRAIT;
		Log.i("tag", "screenType=" + screenType);
		int type = 0;
		if (screenType == 0) {
			type = 1;
		} else if (screenType == 1) {
			type = 0;
		} else {
			type = 2;
		}
		Log.i("tag", "type=" + type);
		orientationType = mActivity.getIntent().getIntExtra(
				IntentKey.ORIENTATION_TYPE, type);
		SnailAppInfo appInfo = new SnailAppInfo();
		String appid = DeviceUtil.getGameInfo(mActivity, "tushangdian_appid");
		String appkey = DeviceUtil.getGameInfo(mActivity, "tushangdian_appkey");
		appInfo.setAppId(Integer.parseInt(appid)); // 应用ID
		appInfo.setAppKey(appkey); // 应用Key

		SnailCommplatform.getInstance().snailInit(mActivity, appInfo,
				new OnInitCompleteListener() {

					@Override
					public void onComplete(int code) {
						switch (code) {
						case OnInitCompleteListener.FLAG_NORMAL:
							// initGame(); // 初始化自己的游戏
							isinit = true;

							// 根据Demo的横竖屏值设置SDK的横竖屏值
							int tempOrientation = SnailCommplatform.SCREEN_ORIENTATION_PORTRAIT;
							switch (orientationType) {
							case OrientationType.PORTRAIT:
								// 默认值不做处理
								break;
							case OrientationType.LANDSCAPE:
								tempOrientation = SnailCommplatform.SCREEN_ORIENTATION_LANDSCAPE;
								break;
							case OrientationType.SENSOR_LANDSCAPE:
								tempOrientation = SnailCommplatform.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
								break;
							default:
								break;
							}
							Log.i("tag", "tempOrientation = " + tempOrientation);
							SnailCommplatform.getInstance()
									.snailSetScreenOrientation(tempOrientation);
							break;
						case OnInitCompleteListener.FLAG_FORCE_CLOSE:
							// 初始化失败，建议直接退出游戏(以下为示例处理)
							AlertDialog dialog = new AlertDialog.Builder(
									mActivity).create();
							dialog.setTitle("提示");
							dialog.setMessage("初始化失败！");
							dialog.setCancelable(false);
							dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
									"退出",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mActivity.finish();
											System.exit(0);
										}
									});
							dialog.show();
							break;

						default:
							break;
						}
					}
				});
	}

	public static interface OrientationType {

		int PORTRAIT = 0;

		int LANDSCAPE = 1;

		int SENSOR_LANDSCAPE = 2;

	}

	public static interface IntentKey {
		String ORIENTATION_TYPE = "orientation_type";

		String APP_ID_KEY = "app_id_key";
	}

	public static void checkrealname() {
		SnailCommplatform.getInstance().showRealName(mActivity, true,
				new OnRealNameListener() {

					@Override
					public void onSuccess() {
						Toast("实名认证成功！");
					}

					@Override
					public void onFailure(int arg0) {
						Toast("实名认证失败：" + arg0);
					}

					@Override
					public void onClose() {
						Toast("实名认证开关关闭！");

					}

					@Override
					public void onAuthentication() {
						Toast("实名认证验证中！");

					}
				});
	}
}
