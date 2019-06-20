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
import com.leshan.sdk.LeshanApi;
import com.leshan.sdk.LoginCallBack;
import com.leshan.sdk.PayCallBack;
import com.leshan.sdk.QuitCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

import floatball.FloatBallManager;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	public static LeshanApi LeshanApi;

	private static String logintoken;
	
	public static String appid;
	
	private static FloatBallManager mFloatballManager;

	/**
	 * 初始化sdk
	 */
	@SuppressWarnings("static-access")
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//		gamekey = DeviceUtil.getGameInfo(mactivity, "GAMEKEY");
		appid = DeviceUtil.getGameInfo(mactivity, "APPID");
		Log.i("tag","appId = " +appid);
//		Log.i("tag","gameKey = " +gamekey);
		LeshanApi = LeshanApi.getInstance(mactivity,mactivity,appid);
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
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					LeshanApi.login(appid, new LoginCallBack() {

						@Override
						public void Fail() {
							loginFail();
							Toast("登录失败");
						}

						@Override
						public void Complete(String login) {
							Log.e("sdk", "login" + login);
							//					 {"uid":"262552","token":"81d992e88975e90bebcd1ba9419cf9e9"}
							try {
								JSONObject obj = new JSONObject(login);
								String uid = (String) obj.get("uid");
								logintoken = (String) obj.get("token");
								loginSuce(mactivity, uid, uid, logintoken);
								Toast("登录成功");
								if(mFloatballManager == null){
								mFloatballManager =LeshanApi.floatBallShow(mActivity, mActivity,new LoginCallBack() {
								    @Override
								    public void Complete(String login) {
								    }

								    @Override
								    public void Fail() {
								    }
								} , new QuitCallBack() {
								    @Override
								    public void Quit() {
								    	LeshanApi.quit(appid, new QuitCallBack() {
										    @Override
										    public void Quit() {
										    	Log.i("tag","账号登出");
												loginOut();
										    }
										});
								    }
								});
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
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
		String token = logintoken;
		String goodName = YYWMain.mOrder.goods;
		String fee = YYWMain.mOrder.money+"";
		String gameOrdersn = morderid;
		String extCp = null;
		LeshanApi.pay(token, goodName, fee, gameOrdersn, extCp, new PayCallBack() {

			@Override
			public void PayComplete(String payOk) {
				paySuce();
				Toast("支付成功");
			}

			@Override
			public void PayCancel() {
				payFail();
				Toast("支付取消");
			}

			@Override
			public void PayFail(String arg0) {
				payFail();
				Toast("支付失败");
				Log.i("tag","支付失败arg0 = " +arg0);
			}
		});
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
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
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);

		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){
			LeshanApi.upload("1", logintoken, roleId, roleName, zoneId, zoneName, roleLevel, "0", "0", "0");
		}else if(Integer.parseInt(ext) == 2){
			LeshanApi.upload("6", logintoken, roleId, roleName, zoneId, zoneName, roleLevel, "0", "0", "0");
		}else if(Integer.parseInt(ext) == 3){
			LeshanApi.upload("2", logintoken, roleId, roleName, zoneId, zoneName, roleLevel, "0", "0", "0");
		}
	}

	public static void onResume(Activity paramActivity) {
//		if(mFloatballManager != null){
//			mFloatballManager.show(paramActivity);
//		}
	}

	public static void onPause(Activity paramActivity) {
//		if(mFloatballManager != null){
//			mFloatballManager.hide(paramActivity);
//		}
	}

	public static void onDestroy(Activity paramActivity) {
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {

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
	public static void loginSuce(Activity mactivity, String uid,String username, String session) {

		YYWMain.mUser = new YYWUser();

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ uid + "";
		}

		//		 YYWMain.mUser.nick = data.getNickName();
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
