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
import com.anzhi.sdk.middle.single.manage.AnzhiSingleSDK;
import com.anzhi.sdk.middle.single.manage.CPInfo;
import com.anzhi.sdk.middle.single.manage.SingleGameCallBack;
import com.anzhi.sdk.middle.single.util.MD5;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
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
						try {
							//                            gameInfoJson.put(GAMEAREAID, "服务区编号");
							//                            gameInfoJson.put(GAME_AREA, "服务区1");
							//                            gameInfoJson.put(GAME_LEVEL, "99级");
							//                            gameInfoJson.put(ROLE_ID, "123");
							//                            gameInfoJson.put(USER_ROLE, "xy");
							Log.i("tag","gameInfoJson="+gameInfoJson.toString());
							Log.i("tag","json="+json);
							//                        	String tempuid = Sputils.getSPstring("uid", "tem", mActivity);
							//                        	if (tempuid.equals("tem")) {
							//                        	String uidtemp=System.currentTimeMillis()+"kk";
							//                			String uid=uidtemp.substring(4, uidtemp.length())+new Random().nextInt(10);
							//                			Sputils.putSPstring("uid", uid, mActivity);
							String cptoken =json.getString("cptoken");
							String request_url =json.getString("requestUrl");
							String deviceid =json.getString("deviceId");
							Log.i("tag","cptoken="+cptoken);
							Log.i("tag","request_url="+request_url);
							Log.i("tag","deviceid="+request_url);
							HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/data/get_uid/");
//							Log.i("tag","httpPost="+httpPost);
							 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
						        params.add(new BasicNameValuePair("app_id", DeviceUtil.getAppid(mActivity))); 
						        params.add(new BasicNameValuePair("cptoken", cptoken)); 
						        params.add(new BasicNameValuePair("request_url", request_url));
						        params.add(new BasicNameValuePair("deviceid", deviceid));
						        
						        Log.i("tag","params="+params);
						        try { 
						            // 设置httpPost请求参数 
						        	Log.i("tag","httpPost1");
//						        	HttpClient hc = new DefaultHttpClient();
						            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
						            Log.i("tag","httpPost2");
//						            HttpGet hg = new HttpGet("http://gameapi.weisuiyu.com/");
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
						//                        midManage.subGameInfo(gameInfoJson.toString());
						//                        mHandler.post(new Runnable() {
						//                            public void run() {
						//                                btn_login.setVisibility(View.GONE);
						//                            }
						//                        });
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
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Log.i("tag",  "json+result: " + JS);
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
				Log.i("tag", "支付取消1");
				payFail();
				Log.i("tag", "支付取消1");
				
				break;

			case SingleGameCallBack.SDK_TYPE_EXIT_GAME: // 退出游戏操作
				mActivity.finish();
				//            	fs(mActivity, callback);
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






}
