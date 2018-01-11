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
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.jolo.sdk.JoloSDK;
import com.jolosdk.home.activity.BeforeLoginActivity;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.CommonGameProxy;


public class YaYawanconstants{

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	// 用户登录信息
		private static String gamecode; 
		// 用户登录信息
		private static String userName; // 用户名
		private static String userId; // 用户ID
		private static String session; // 用户登录session
		private static String account; // 用户帐号信息
		private static String accountSign; // 用户帐号信息签名(聚乐公钥验签)

		private static String publickey; 
		private static Handler mHandler = new Handler();
		
		private static String order; // 支付申请订单
		private static String sign; // 支付订单签名(CP私钥签名)
		private static String resultOrder;// 支付回执订单
		private static String resultSign;// 支付回执订单签名(聚乐公钥验签)
		
		private static String bufanuserId; // 用户ID
		private static String bufantoken; // 用户ID
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		gamecode = ""+DeviceUtil.getGameInfo(mactivity, "gamecode");
		publickey = ""+DeviceUtil.getGameInfo(mactivity, "publickey");
		Log.i("tag","gamecode="+gamecode);
		Log.i("tag","publickey="+publickey);
		JoloSDK.initJoloSDK(mActivity, gamecode);
		isinit = true;
//		mHandler.postDelayed(goNextUiRunnable, 100);
		Log.i("tag","初始化结束");
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		Log.i("tag","isinit="+isinit);
		if(isinit){
			// 清除游戏的用户信息
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					resetUserInfo();
					JoloSDK.logoff(mactivity);
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
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Order or = new Order();
		// 注意：参数里，不要出现类似“1元=10000个金币”的字段，因为“=”原因，会导致微信支付校验失败
		or.setAmount(YYWMain.mOrder.money+""); // 设置支付金额，单位分
		or.setGameCode(gamecode); // 设置游戏唯一ID,由Jolo提供
		String gamename = ""+DeviceUtil.getGameInfo(mactivity, "gamename");
		or.setGameName(gamename); // 设置游戏名称
		or.setGameOrderid(morderid); // 设置游戏订单号
		or.setNotifyUrl(DeviceUtil.getGameInfo(mActivity, "callback")); // 设置支付通知
		or.setProductDes(YYWMain.mOrder.goods); // 设置产品描述
		or.setProductID("s_"+YYWMain.mOrder.goods); // 设置产品ID
		or.setProductName(YYWMain.mOrder.goods); // 设置产品名称
		or.setSession(session); // 设置用户session
		or.setUsercode(userId); // 设置用户ID
		order = or.toJsonOrder(); // 生成Json字符串订单
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
		try{
		HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/data/get_uid/");
		Log.i("tag","httpPost="+httpPost);
		bufanuserId =YYWMain.mUser.yywuid;;
		 bufantoken = YYWMain.mUser.yywtoken;
		Log.i("tag","bufanuserId="+bufanuserId);
		Log.i("tag","bufantoken="+bufantoken);
		 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
	        params.add(new BasicNameValuePair("xstr", order)); 
	        params.add(new BasicNameValuePair("app_id", DeviceUtil.getAppid(mActivity))); 
	        params.add(new BasicNameValuePair("uid", bufanuserId)); 
	        params.add(new BasicNameValuePair("token ", bufantoken)); 
	        Log.i("tag","params="+params);
	        try { 
	            // 设置httpPost请求参数 
	        	Log.i("tag","httpPost1");
//	        	HttpClient hc = new DefaultHttpClient();
	            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
	            Log.i("tag","httpPost2");
//	            HttpGet hg = new HttpGet("http://gameapi.weisuiyu.com/");
	            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost); 
	            Log.i("tag","httpPost3");
	            Log.i("tag","httpResponse.getStatusLine().getStatusCode()="+httpResponse.getStatusLine().getStatusCode());
	            if(httpResponse.getStatusLine().getStatusCode() == 200){
	            	String re = EntityUtils.toString(httpResponse.getEntity());
//	            	Log.i("tag","re="+re);
	            	JSONObject js = new JSONObject(re);
//	            	Log.i("tag","js="+js);
	            	sign = js.getString("sign");
//	            	Log.i("test", "order = " + order);
//	            	Log.i("test", "sign = " + sign);
	            	JoloSDK.startPay(mActivity, order, sign); // 启动支付
	            }
	        }catch(ClientProtocolException e){
	        	e.printStackTrace();
	        }

	}catch (Exception e) {

	}
			}
		}).start();
		
//		String privatekey = ""+DeviceUtil.getGameInfo(mactivity, "privatekey");
//		sign = RsaSign.sign(order, privatekey); // 签名
//		Log.i("test", "order = " + order);
//		Log.i("test", "sign = " + sign);
//		JoloSDK.startPay(mactivity, order, sign); // 启动支付
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


        KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
			
			@Override
			public void onSuccess(User arg0, int arg1) {
				// TODO Auto-generated method stub
//				callback.onExit();
				mActivity.finish();
				System.exit(0);
			}
			
			@Override
			public void onLogout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
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

	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(goNextUiRunnable);
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		/* 释放资源 */
		JoloSDK.releaseJoloSDK();
	}

//	public static void onActivityResult(Activity paramActivity) {
//		// TODO Auto-generated method stub
//		Log.i("tag","结果处理1");
//
//	}

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
		public static void onActivityResult(Activity paramActivity,int requestCode, int resultCode, Intent data) {
		Log.i("tag","结果处理");
		if (resultCode != mActivity.RESULT_OK || data == null) {
			if (requestCode == JoloSDK.PAY_REQUESTCODE) {
				Log.i("test", "取消支付");
			}
			return;
		}
		switch (requestCode) {
		case JoloSDK.ACCOUNT_REQUESTCODE: {// 登录
			// 用户账号名
			userName = data.getStringExtra(JoloSDK.USER_NAME);
			// 用户账号ID
			userId = data.getStringExtra(JoloSDK.USER_ID);
			// 账号的session，支付时使用
			session = data.getStringExtra(JoloSDK.USER_SESSION);
			Log.i("tag","userId="+userId);
			Log.i("tag","userName="+userName);
			Log.i("tag","session="+session);
			loginSuce(mActivity, userId, userName, session);
			
			// 用户帐号信息签名(聚乐公钥验签)，密文，CP对该密文用公钥进行校验
			accountSign = data.getStringExtra(JoloSDK.ACCOUNT_SIGN);
			// 用户帐号信息，明文，用户加密的字符串
			account = data.getStringExtra(JoloSDK.ACCOUNT);

			Log.i("test", "account = " + account);
			Log.i("test", "account_sign = " + accountSign);
			// 账号合法性检验，注意：安全性考虑，建议将该代码放到服务器进行校验
			doCheckSign();
		}
			break;
		case JoloSDK.PAY_REQUESTCODE: {// 支付
			resultOrder = data.getStringExtra(JoloSDK.PAY_RESP_ORDER);
			resultSign = data.getStringExtra(JoloSDK.PAY_RESP_SIGN);
			Log.i("test", "resultOrder = " + resultOrder);
			Log.i("test", "resultSign = " + resultSign);
			if (RsaSign.doCheck(resultOrder, resultSign, publickey)) {
				// 校验支付订单后，解析订单内容
				ResultOrder or = new ResultOrder(resultOrder);
				String joloorderid = or.getJoloOrderID(); // jolo唯一订单号
				String amount = or.getRealAmount(); // 用户实际支付的金额
				int resultcode = or.getResultCode(); // 返回码, == 200为支付成功
				String resultmsg = or.getResultMsg(); // 返回提示信息
				Log.i("test", "joloorderid = " + joloorderid);
				Log.i("test", "amount = " + amount);
				Log.i("test", "resultcode = " + resultcode);
				Log.i("test", "resultmsg = " + resultmsg);
				paySuce();
				Toast("支付成功");
				Log.i("tag","支付结果签名校验成功" + ",金额 = " + amount + "分");
			} else {
				payFail();
				Toast("支付失败");
				Log.i("tag","支付结果签名校验失败");
			}
		}
			break;
		default:
			break;
		}
	}
	private static void doCheckSign() {
		if (RsaSign.doCheck(account, accountSign, publickey)) {
//			updateCurUserInfo();
//			Toast.makeText(mActivity, "签名校验成功，用户合法", Toast.LENGTH_SHORT).show();
			Log.i("tag","签名校验成功，用户合法");
		} else {
//			Toast.makeText(mActivity, "签名校验失败，用户不合法", Toast.LENGTH_SHORT).show();
			Log.i("tag","签名校验失败，用户不合法");
		}
	};
	
	private static Runnable goNextUiRunnable = new Runnable() {

		@Override
		public void run() {
			goNextUi();
		}
	};

	private static void goNextUi() {
		Intent intent = new Intent(mActivity, BeforeLoginActivity.class);
		mActivity.startActivityForResult(intent, JoloSDK.ACCOUNT_REQUESTCODE);
	}
	
//	private void updateCurUserInfo() {
//		userNameTv.setText(userName);
//		userIdTv.setText(userId);
//		sessionId.setText(session);
//	}

	/**
	 * 清除游戏的用户信息，注销
	 */
	private static void resetUserInfo() {
		userName = null;
		userId = null;
		session = null;
	}
}
