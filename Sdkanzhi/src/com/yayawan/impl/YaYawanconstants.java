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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.anzhi.sdk.middle.manage.AnzhiSDK;
import com.anzhi.sdk.middle.manage.CPInfo;
import com.anzhi.sdk.middle.manage.GameCallBack;
import com.anzhi.sdk.middle.util.MD5;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	 private static AnzhiSDK midManage;

	 private static String Appkey;

		private static String AppSecret;
		
		private static JSONObject gameInfoJson;
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
			midManage = AnzhiSDK.getInstance();
	        midManage.init(mactivity, Appkey, AppSecret, callback);
	}
	static GameCallBack callback = new GameCallBack() {

        @Override
        public void callBack(final int type, final String result) {

            Log.i("Anzhi_SDK_TEST", "code: " + type + ", result: " + result);
            switch (type) {
            case GameCallBack.SDK_TYPE_INIT: // 初始化操作
                isinit = true;
                midManage.addPop(mActivity);// 添加悬浮球
                break;
            case GameCallBack.SDK_TYPE_LOGIN: // 登录操作
            	try {
					JSONObject json = new JSONObject(result);
					if (json.optInt("code") == 200) { // 登录成功
						gameInfoJson = new JSONObject();
						try {
							Log.i("tag","gameInfoJson="+gameInfoJson.toString());
							Log.i("tag","json="+json);
							String cptoken =json.getString("cptoken");
							String request_url =json.getString("requestUrl");
							String deviceid =json.getString("deviceId");
							Log.i("tag","cptoken="+cptoken);
							Log.i("tag","request_url="+request_url);
							Log.i("tag","deviceid="+request_url);
							HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/data/get_uid/");
							 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
						        params.add(new BasicNameValuePair("app_id", DeviceUtil.getAppid(mActivity))); 
						        params.add(new BasicNameValuePair("cptoken", cptoken)); 
						        params.add(new BasicNameValuePair("request_url", request_url));
						        params.add(new BasicNameValuePair("deviceid", deviceid));
						        
						        Log.i("tag","params="+params);
						        try { 
						            // 设置httpPost请求参数 
						        	Log.i("tag","httpPost1");
						            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
						            Log.i("tag","httpPost2");
						            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost); 
						            Log.i("tag","httpPost3");
						            Log.i("tag","httpResponse.getStatusLine().getStatusCode()="+httpResponse.getStatusLine().getStatusCode());
						            if(httpResponse.getStatusLine().getStatusCode() == 200){
						            	String re = EntityUtils.toString(httpResponse.getEntity());
						            	Log.i("tag","re="+re);
						            	JSONObject js = new JSONObject(re);
						            	Log.i("tag","js="+js);
						            	String uid = js.getString("uid");
						            	Log.i("tag","uid="+uid);
						            	loginSuce(mActivity, uid, uid, uid);
						            	
						            }
						        }catch(ClientProtocolException e){
						        	e.printStackTrace();
						        }

						}catch (Exception e) {

						}
					}
				} catch (JSONException e) {
				}
                break;

            case GameCallBack.SDK_TYPE_PAY:
            	Log.i("tag", "code: " + type + ", result: " + result);
				JSONObject JS = null;
				try {
					JS = new JSONObject(result);
					Log.i("tag", "paystatus+JS=: " + JS);
					String paystatus = JS.getString("payStatus");
					Log.i("tag", "paystatus: " + paystatus);
					if(paystatus.equals("1")){
						Log.i("tag", "支付成功");
						paySuce();
						Log.i("tag", "支付成功1");
					}else if(paystatus.equals("3")){
						Log.i("tag", "支付失败1");
						payFail();
						Log.i("tag", "支付失败1");
					}
				} catch (JSONException e2) {
					e2.printStackTrace();
				}
				Log.i("tag",  "json+result: " + JS);
				paySuce();
				break;
            case GameCallBack.SDK_TYPE_CANCEL_PAY:
				Log.i("tag", "支付取消1");
				payFail();
				Log.i("tag", "支付取消1");
				
				break;
            case GameCallBack.SDK_TYPE_EXIT_GAME: // 取消退出游戏操作
            	mActivity.finish();
				if (result != null) {
					try {
						JSONObject json = new JSONObject(result);
						boolean killSelf = json.optBoolean("killSelf");
						if (killSelf) { // 是否为完全退出，如果为true需要游戏方以杀进程方式退出
							System.exit(0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
                break;
                
            case GameCallBack.SDK_TYPE_CANCEL_EXIT_GAME: // 取消退出游戏操作
            	Toast("继续游戏");
            	break;
            case GameCallBack.SDK_TYPE_CANCEL_LOGIN: // 取消登录操作
            	Toast("取消登录");
            	break;
            case GameCallBack.SDK_TYPE_LOGOUT: // 注销登录操作
            	mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
					loginOut();
					}
				});
                break;

            }
        }
    };
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
			midManage.login(mactivity);
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
		JSONObject json = new JSONObject();
		try {
			// 游戏方生成的订单号,可以作为与安智订单进行关联
			json.put("cpOrderId", morderid);
			json.put("cpOrderTime", System.currentTimeMillis());// 下单时间
			json.put("amount", YYWMain.mOrder.money);// 支付金额(单位：分)
			json.put("productCount", 1);// 商品数量
			json.put("productName", YYWMain.mOrder.goods);// 游戏方商品名称
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = json.toString();
		Log.i("tag","AppSecret="+AppSecret);
		String Md5 = MD5.encodeToString(AppSecret);
		Log.i("tag","Md5="+Md5);
		midManage.pay(Des3Util.encrypt(data, AppSecret), Md5);
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
				midManage.exitGame(mActivity);
			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
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
	
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		try {
			gameInfoJson.put(GAMEAREAID, zoneId);
			gameInfoJson.put(GAME_AREA, zoneName);
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
midManage.onResumeInvoked();
	}

	public static void onPause(Activity paramActivity) {
midManage.onPauseInvoked();
	}

	public static void onDestroy(Activity paramActivity) {
midManage.onDestoryInvoked();
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

	}

	public static void onNewIntent(Intent paramIntent) {
midManage.onNewIntentInvoked(paramIntent);
	}

	public static void onStart(Activity paramActivity) {
midManage.onStartInvoked();
	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
midManage.onStopInvoked();
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
	@SuppressWarnings("unused")
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
