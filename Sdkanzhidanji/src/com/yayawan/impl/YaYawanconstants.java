package com.yayawan.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.security.auth.callback.Callback;
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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.anzhi.sdk.middle.single.manage.AnzhiSingleSDK;
import com.anzhi.sdk.middle.single.manage.CPInfo;
import com.anzhi.sdk.middle.single.manage.SingleGameCallBack;
import com.anzhi.sdk.middle.single.util.MD5;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static AnzhiSingleSDK midManage;

	private static String Appkey;

	private static String AppSecret;

	private static JSONObject gameInfoJson;
	
	private static YYWExitCallback exitcallback;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		final CPInfo info = new CPInfo();
		Appkey = ""+DeviceUtil.getGameInfo(mactivity, "Appkey");
		AppSecret = ""+DeviceUtil.getGameInfo(mactivity, "AppSecret");
		Log.i("tag", "Appkey="+Appkey);
		Log.i("tag", "AppSecret="+AppSecret);
		info.setAppKey(Appkey);
		info.setSecret(AppSecret);
		midManage = AnzhiSingleSDK.getInstance();
		midManage.init(mactivity, Appkey, AppSecret, callback);
		Log.i("tag", "初始化结束");
	}

	/**
	 * 游戏的服务器区
	 */
	public final static String GAME_AREA = "gameArea";
	/**
	 * 游戏的服务器区编码
	 */
	public final static String GAMEAREAID = "gameAreaId";

	/**
	 * 角色等级
	 */
	public final static String GAME_LEVEL = "gameLevel";

	/**
	 * 角色名称
	 */
	public final static String USER_ROLE = "userRole";
	/**
	 * 角色ID
	 */
	public final static String ROLE_ID = "roleId";
	
	public static Callback paycallback;

	static SingleGameCallBack callback = new SingleGameCallBack() {
		@Override
		public void callBack(final int type, final String result) {
			Log.i("Anzhi_SDK_TEST", "code: " + type + ", result: " + result);
			Log.i("tag", "code: " + type + ", result: " + result);
			switch (type) {
			case SingleGameCallBack.SDK_TYPE_INIT: // 初始化操作
//				midManage.login(mActivity);
				//                login(mActivity); // 调用登录方法，游戏方选择在合适位置调用
				midManage.addPop(mActivity);// 添加悬浮球
				break;
			case SingleGameCallBack.SDK_TYPE_LOGIN: // 登录操作
				try {
					JSONObject json = new JSONObject(result);
					if (json.optInt("code") == 200) { // 登录成功
						gameInfoJson = new JSONObject();
							String cptoken =json.getString("cptoken");
							String request_url =json.getString("requestUrl");
							String deviceid =json.getString("deviceId");
							
							HttpUtils httpUtil = new HttpUtils();
							String url = "https://api.sdk.75757.com/data/get_uid/";
							RequestParams requestParams = new RequestParams();
							requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
							requestParams.addBodyParameter("cptoken", cptoken);
							requestParams.addBodyParameter("request_url", request_url);
							requestParams.addBodyParameter("deviceid", deviceid);
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
												String uid = obj.getString("uid");
												Yayalog.loger("uid ="+uid);
												loginSuce(mActivity, uid, uid, uid);
												Toast("登录成功");
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									});
					}
				} catch (JSONException e) {
				}

				break;

				//
			case SingleGameCallBack.SDK_TYPE_PAY:
				Log.i("tag", "code: " + type + ", result: " + result);
				JSONObject JS = null;
				try {
					JS = new JSONObject(result);
					String paystatus = JS.getString("payStatus");
					if(paystatus.equals("1")){
						paySuce();
					}else if(paystatus.equals("3")){
						payFail();
					}
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
//				if(pays){}
				paySuce();
				//                mHandler.post(new Runnable() {
				//                    @Override
				//                    public void run() {
				//                        mTVCallback.setText("code: " + type + ", result: " + result);
				//                    }
				//                });
				break;
				
			case SingleGameCallBack.SDK_TYPE_CANCEL_PAY:
				payFail();
				
				break;

			case SingleGameCallBack.SDK_TYPE_EXIT_GAME: // 退出游戏操作
//				exitcallback.onExit();
				mActivity.finish();
				System.exit(0);
				//            	fs(mActivity, callback);
//				if (result != null) {
//					try {
//						JSONObject json = new JSONObject(result);
//						boolean killSelf = json.optBoolean("killSelf");
//						if (killSelf) { // 是否为完全退出，如果为true需要游戏方以杀进程方式退出
//							System.exit(0);
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
				break;
			case SingleGameCallBack.SDK_TYPE_CANCEL_EXIT_GAME: // 取消退出游戏操作
				break;
			case SingleGameCallBack.SDK_TYPE_LOGOUT: // 注销登录操作
				//                mHandler.post(new Runnable() {
				//                    public void run() {
				//                        btn_login.setVisibility(View.VISIBLE);
				//                    }
				//                });
				break;

			}
		}
	};

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		String time = System.currentTimeMillis()+"";
		String time1 = System.currentTimeMillis()%1000+"";
		Log.i("tag","time="+time);
		Log.i("tag","time=="+time1);
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		midManage.login(mActivity);
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Log.i("tag","zhifu20");
		Yayalog.loger("YaYawanconstantssdk支付");
		
		JSONObject json = new JSONObject();
		try {
			// 游戏方生成的订单号,可以作为与安智订单进行关联
			json.put("cpOrderId", morderid);
			json.put("cpOrderTime", System.currentTimeMillis());// 下单时间
			json.put("amount", YYWMain.mOrder.money);// 支付金额(单位：分)
			//json.put("cpCustomInfo", YYWMain.mOrder.goods);// 游戏方自定义数据
			json.put("productCount", 1);// 商品数量
			json.put("productName", YYWMain.mOrder.goods);// 游戏方商品名称
			
//			json.put("cpOrderId", "anzhi_" + System.currentTimeMillis());
//            json.put("cpOrderTime", System.currentTimeMillis());// 下单时间
//            json.put("amount", 1);// 支付金额(单位：分)
//            json.put("cpCustomInfo", "游戏方自定义数据");// 游戏方自定义数据
//            json.put("productCount", 1);// 商品数量
//            json.put("productName", "宝石");// 游戏方商品名称
//            json.put("productCode", "商品代码");// 游戏方商品代码
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = json.toString();
		Log.i("tag","AppSecret="+AppSecret);
		String Md5 = MD5.encodeToString(AppSecret);
		Log.i("tag","Md5="+Md5);
		AnzhiSingleSDK.getInstance().pay(Des3Util.encrypt(data, AppSecret), Md5);
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
		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				exitcallback = callback;
				midManage.exitGame(mActivity);
			}
		});

		//

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		try {
			gameInfoJson.put(GAMEAREAID, "zoneId");
			gameInfoJson.put(GAME_AREA, "zoneName");
			gameInfoJson.put(GAME_LEVEL, roleLevel);
			gameInfoJson.put(ROLE_ID, roleId);
			gameInfoJson.put(USER_ROLE, roleName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		midManage.subGameInfo(gameInfoJson.toString());
		Log.i("tag","gameInfoJson.toString()="+gameInfoJson.toString());
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		AnzhiSingleSDK.getInstance().onResumeInvoked();
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		AnzhiSingleSDK.getInstance().onPauseInvoked();
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		 AnzhiSingleSDK.getInstance().onActivityResultInvoked(paramInt1, paramInt2, paramIntent);
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



	public static void Toast(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
			}
		});
	}


}
