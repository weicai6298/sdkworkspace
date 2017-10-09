package com.yayawan.impl;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.meizu.atlas.server.handle.icontentprovider.methods.call;
import com.meizu.gamesdk.model.callback.MzExitListener;
import com.meizu.gamesdk.model.callback.MzLoginListener;
import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.LoginResultCode;
import com.meizu.gamesdk.model.model.MzAccountInfo;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.online.core.MzGameBarPlatform;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;
import com.meizu.gamesdk.online.model.model.MzBuyInfo;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static String AppID;

	private static String AppKey;

	public static String uid;

	private static String name;

	private static String token;
	
	 //gamebar操作实例
	private static MzGameBarPlatform mzGameBarPlatform;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		  mzGameBarPlatform = new MzGameBarPlatform(mactivity, MzGameBarPlatform.GRAVITY_LEFT_TOP);
	        mzGameBarPlatform.onActivityCreate();
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		AppID = ""+DeviceUtil.getGameInfo(applicationContext, "AppID");
		AppKey = ""+DeviceUtil.getGameInfo(applicationContext, "AppKey");
		Log.i("tag", "AppID="+AppID);
		Log.i("tag", "AppKey="+AppKey);
		MzGameCenterPlatform.init(applicationContext,AppID,AppKey);
        
		isinit = true;
		Log.i("tag","魅族初始化结束");
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			MzGameCenterPlatform.login(mactivity, new MzLoginListener() {
				@Override
				public void onLoginResult(int code, final MzAccountInfo accountInfo, String errorMsg) {
					// TODO 登录结果回调。注意，该回调跑在应用主线程，不能在这里做耗时操作
					switch(code){
					case LoginResultCode.LOGIN_SUCCESS:
						// TODO 登录成功，拿到uid 和 session到自己的服务器去校验session合法性
						mActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								uid = accountInfo.getUid();
								name = accountInfo.getName();
								token = accountInfo.getSession();
								Log.i("tag","uid="+uid);
								Log.i("tag","name="+name);
								Log.i("tag","token="+token);
								Log.i("tag","登录-成功");
								loginSuce(mactivity, uid, name, "");
								tishi("登录成功");
							}
						});
						//				displayMsg("登录成功！\r\n 用户名：" + accountInfo.getName() + "\r\n Uid：" +
						//				accountInfo.getUid() + "\r\n session：" + accountInfo.getSession());
						break;
					case LoginResultCode.LOGIN_ERROR_CANCEL:
						// TODO 用户取消登陆操作
						loginFail();
						tishi("登录取消");
						break;
					default:
						// TODO 登陆失败，包含错误码和错误消息。
						// TODO 注意，错误消息(errorMsg)需要由游戏展示给用户，提示失败原因
						//				displayMsg("登录失败 : " + errorMsg + " , code = " + code);
						loginFail();
						tishi("登录失败");
						Log.i("tag","登录失败 : " + errorMsg + " , code = " + code);
						break;
					} 
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
	 */
	public static void pay(Activity mactivity, String morderid,String sign,String time) {
		Log.i("tag","pay-create_time="+time);
		Yayalog.loger("YaYawanconstantssdk支付");
		String orderId = morderid; // cp_order_id (不能为空)
		String Sign = sign; // sign (不能为空)
		String signType = "MD5"; // sign_type (不能为空)
		int buyCount = 1; // buy_amount
//		String cpUserInfo = ""; // user_info
		String amount = YYWMain.mOrder.money/100+".0"; // total_price
		String productId = "a2"; // product_id
		String productSubject = YYWMain.mOrder.goods; // product_subject
		String productBody = YYWMain.mOrder.goods; // product_body
		String productUnit = YYWMain.mOrder.goods; // product_unit
		String appid = AppID; // app_id (不能为空)
		String Uid = uid; // uid (不能为空)flyme账号用户ID
		String perPrice = YYWMain.mOrder.money/100+""; // product_per_price
		long createTime = Long.valueOf(time); // create_time
		int payType = 0; //pay_type：0-定额；1-不定额
		Bundle buyBundle = new MzBuyInfo().setBuyCount(buyCount)
		.setOrderAmount(amount).setOrderId(orderId).setPerPrice(perPrice)
		.setProductBody(productBody).setProductId(productId).setProductSubject(productSubject)
		.setProductUnit(productUnit).setSign(Sign).setSignType(signType)
		.setAppid(appid).setUserUid(Uid).setPayType(payType) .setCreateTime(createTime).toBundle();
		Log.i("tag","buyBundle="+buyBundle);
		MzGameCenterPlatform.payOnline(mactivity, buyBundle, new MzPayListener() { 
			@Override
			public void onPayResult(int code, Bundle info, String errorMsg) {
			// TODO 支付结果回调，该回调跑在应用主线程。注意，该回调跑在应用主线程，不能在这里
			switch(code){
			case PayResultCode.PAY_SUCCESS:
			// TODO 如果成功，接下去需要到自己的服务器查询订单结果
			MzBuyInfo payInfo = MzBuyInfo.fromBundle(info);
//			displayMsg("支付成功 : " + payInfo.getOrderId());
			paySuce();
			tishi("支付成功 ");
			Log.i("tag","支付成功 : " + payInfo.getOrderId());
			
			break;
			case PayResultCode.PAY_ERROR_CANCEL:
			// TODO 用户主动取消支付操作，不需要提示用户失败
				payFail();
				tishi("取消支付 ");
			break;
			default:
			// TODO 支付失败，包含错误码和错误消息。
			// TODO 注意，错误消息(errorMsg)需要由游戏展示给用户，提示失败原因
//			displayMsg("支付失败 : " + errorMsg + " , code = " + code);
				payFail();
				tishi("支付失败");
				Log.i("tag","支付失败 : " + errorMsg + " , code = " + code);
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


		MzGameCenterPlatform.exitSDK(mActivity, new MzExitListener() {
			public void callback(int code, String msg) {
				if (code == MzExitListener.CODE_SDK_LOGOUT) {
					//TODO 在这里处理退出逻辑
					callback.onExit();
					//                    System.exit(0);
				} else if (code == MzExitListener.CODE_SDK_CONTINUE) {
					//TODO 继续游戏
					Toast.makeText(mActivity, "继续游戏", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		 //调一下onActivityResume
        mzGameBarPlatform.onActivityResume();
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		 //调一下onActivityPause
        mzGameBarPlatform.onActivityPause();
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		 //调一下onActivityDestroy
        mzGameBarPlatform.onActivityDestroy();
	}

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub

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


	/**1200x0.04
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



	public static void tishi(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}


	 
	
}
