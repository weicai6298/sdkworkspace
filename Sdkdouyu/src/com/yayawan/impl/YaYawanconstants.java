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
import com.douyu.gamesdk.DouyuCallback;
import com.douyu.gamesdk.DouyuGameSdk;
import com.douyu.gamesdk.DouyuSdkParams;
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

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static String token;
	
	public static String role_Id = "1";
	public static String role_Name = "1";
	public static String role_Level = "1";
	public static String zone_Id = "1";
	public static String zone_Name = "1";


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//		DouyuGameSdk.getInstance().checkSdkValid(mactivity);
		DouyuGameSdk.getInstance().setSdkCallback(new DouyuCallback() {
			@Override
			public void onSuccess(DouyuSdkParams data) {
				//预留，暂不处理
				isinit = true;
			}

			@Override
			public void onError(String code, String msg) {
				//                Toast.makeText(MainActivity.this, "游戏客户端 SDK回调 code:"+code+" msg:"+msg, Toast.LENGTH_LONG).show();
				if (DouyuCallback.CODE_SWITCH_ACCOUNT.equals(code)) {
					//游戏方可用sid直接切换账号
					//                    showGamePage();

				}
				else {
					//错误包括登录态过期，服务器异等，可统一处理为退出游戏，重新登录
					//                    showLoginPage();
				}
			}
		});
		String douyu_debug = DeviceUtil.getGameInfo(mActivity, "douyu_debug");
		int debug = Integer.parseInt(douyu_debug);
		if(debug == 1){
			DouyuGameSdk.debugMode(true);
			Log.i("tag","debug模式");
		}else {
			DouyuGameSdk.debugMode(false);
		}
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
			DouyuGameSdk.getInstance().login(mactivity, new DouyuCallback() {
				@Override
				public void onSuccess(DouyuSdkParams data){
					Log.i("tag", " 游 戏 客 户 端 登 录 成 功 data:"+data);
					String sid = data.get(DouyuSdkParams.SID, "");
					HttpPost(sid);
				}
				@Override
				public void onError(String code, String msg) {
//					Toast.makeText(mactivity, "游戏客户端 登录失败 code:"+code+"msg:"+msg, Toast.LENGTH_LONG).show();
					Log.i("tag", "游戏客户端 登录失败 code:"+code+"msg:"+msg);
					loginFail();
				}
			});
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 * @throws JSONException 
	 */
	public static void pay(Activity mactivity, String morderid,String payinfo) throws JSONException {
		Yayalog.loger("YaYawanconstantssdk支付");
		JSONObject json = new JSONObject(payinfo);
		DouyuSdkParams payParams2 = new DouyuSdkParams();
		payParams2.put(DouyuSdkParams.METHOD_VERSION, DouyuSdkParams.METHOD_V2);
		payParams2.put(DouyuSdkParams.THIRD_ORDER_ID, json.get("order_id")+"");
		payParams2.put(DouyuSdkParams.AREA_ID, json.get("area_id")+"");
		//游戏角色名，必传
		payParams2.put(DouyuSdkParams.ROLE_NAME, json.get("role_name")+"");
		//订单标题，必传
		payParams2.put(DouyuSdkParams.TITLE, json.get("title")+"");
		//订单金额，必传
		payParams2.put(DouyuSdkParams.AMOUNT, json.get("amount")+"");
		//订单签名，必传
		payParams2.put(DouyuSdkParams.SIGN, json.get("sign")+"");
		//回调信息，必传
		payParams2.put(DouyuSdkParams.CALLBACK, json.get("title")+"");
		
		
		DouyuGameSdk.getInstance().pay(payParams2, new DouyuCallback() {
			
			@Override
			public void onSuccess(DouyuSdkParams data) {
				Log.i("tag"," 游 戏 客 户 端 支 付 成 功 data:"+data);
				paySuce();
			}
			
			@Override
			public void onError(String code, String msg) {
				payFail();
				Log.i("tag","游戏客户端 支付失败 code:"+code+"msg:"+msg);
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
				DouyuGameSdk.getInstance().showExitDialog(paramActivity, new DouyuCallback() {
					@Override
					public void onSuccess(DouyuSdkParams data) {
						//推出游戏
						callback.onExit();
					}
					@Override
					public void onError(String code, String msg) {
						//继续游戏
					}
				});
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
		role_Id =roleId;
		role_Level =roleLevel;
		role_Name =roleName;
		zone_Id =zoneId;
		zone_Name = zoneName;
		Log.i("tag", "role_Id ="+role_Id);
		Log.i("tag", "role_Level ="+role_Level);
		Log.i("tag", "role_Name ="+role_Name);
		Log.i("tag", "zone_Id ="+zone_Id);
		Log.i("tag", "zone_Name ="+zone_Name);
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

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
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	private static void HttpPost(final String sid) {
		token = sid;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/data/get_uid/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("sid", sid);
		requestParams.addBodyParameter("sdk_version", DouyuGameSdk.getSDKVersion());
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
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

}
