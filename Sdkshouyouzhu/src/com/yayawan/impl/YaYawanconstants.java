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

import android.R.integer;
import android.app.Activity;
import android.app.Application;
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
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.uu898.gamesdk.UGSdk;
import com.uu898.gamesdk.support.listener.UGChangeAccountListener;
import com.uu898.gamesdk.support.listener.UGExitListener;
import com.uu898.gamesdk.support.listener.UGLoginListener;
import com.uu898.gamesdk.support.listener.UGPayListener;
import com.uu898.gamesdk.support.model.UGPayModel;
import com.uu898.gamesdk.support.result.UGPayResult;
import com.uu898.gamesdk.utils.FloatViewHelper;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	public static boolean isinit = false;
	public static String uid;
	public static String token;
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		UGSdk.getInstance().changeAccount(new UGChangeAccountListener() {
			
			@Override
			public void onChange() {
				// TODO Auto-generated method stub
				//游戏中执行退出游戏账户，返回游戏初始页，调用sdk登录接口
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
        Application application = (Application) applicationContext;
		String uu_appid = ""+DeviceUtil.getGameInfo(applicationContext, "uu_appid"); 
		String uu_key = ""+DeviceUtil.getGameInfo(applicationContext, "uu_key"); 
		UGSdk.getInstance().init(application,uu_appid,uu_key,false);
		YaYawanconstants.isinit = true;
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			UGSdk.getInstance().login(new UGLoginListener() {
				
				@Override
				public void onSuccess(String token) {
					//token 不作为手游猪用户唯一标识
					//token 作为 第三方 请求 手游猪服务端 获取 手游猪用户唯一标识 的请求参数，
					//具体获取手游猪用户唯一标识方式：请参照服务端文档
					HttpPost(token);
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
		UGPayModel model = new UGPayModel();
		model.cpOrderNo = morderid;
		model.title = YYWMain.mOrder.goods;
		model.originalPrice = Integer.parseInt(YYWMain.mOrder.money+"");
//		model.number = 1;
		model.describe = YYWMain.mOrder.goods;
		model.productID = "s_"+ YYWMain.mOrder.goods;
		model.reserved = "";
		UGSdk.getInstance().pay(model, new UGPayListener() {
			
			@Override
			public void onPayDone(UGPayResult result) {
			switch (result.resultCode) {
			case UGPayResult.CODE_SUCCESS:
				paySuce();
				Toast("支付成功");
			break;
			case UGPayResult.CODE_FAIL:
				payFail();
				Toast("支付失败");
				break;
			case UGPayResult.CODE_CANCLE:
				payFail();
				Toast("支付取消");
				break;
			default:
				break;
			}
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

		UGSdk.getInstance().exit(new UGExitListener() {
			
			@Override
			public void onExit() {
				callback.onExit();
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

	public static void onResume(Activity paramActivity) {
       FloatViewHelper.showFloatingView();//显示悬浮窗
	}

	public static void onPause(Activity paramActivity) {
     FloatViewHelper.hideFloatingView();//隐藏悬浮窗 
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
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("code", sid);
		Log.i("tag","requestParams="+requestParams);
		httpUtil.send(HttpMethod.POST, "https://api.sdk.75757.com/data/get_uid/",requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.i("tag","请求失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject js = new JSONObject(arg0.result);
							Log.i("tag", "js=" + js);
							uid = js.getString("openid");
							Log.i("tag", "uid=" + uid);
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}


}
