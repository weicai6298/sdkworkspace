package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.le.accountoauth.utils.LeUserInfo;
import com.le.legamesdk.LeGameSDK;
import com.le.legamesdk.LeGameSDK.CertificationCallback;
import com.le.legamesdk.LeGameSDK.CertificationStatusCallback;
import com.le.legamesdk.LeGameSDK.ExitCallback;
import com.le.legamesdk.LeGameSDK.LoginCallback;
import com.le.legamesdk.LeGameSDK.PayCallback;
import com.letv.lepaysdk.smart.LePayInfo;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;
	
	private static Context mContext;

	public static boolean isinit=false;

	private static LeGameSDK letvGameSDK;

	public static LetvUser letvUser = null;

	private static String uid;

	private static String name;

	private static String token;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
//		Log.i("tag","初始化开始");
		mActivity = mactivity;
		mContext = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//		log("mContext="+mContext);
		Log.i("tag","mContext="+mContext);
		LeGameSDK.init(mContext);
		letvGameSDK = LeGameSDK.getInstance();
		isinit = true;
		Log.i("tag","初始化结束");
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
//	    Log.i("tag","初始化开始");
//		LeGameSDK.init(applicationContext);
//		isinit = true;
//		Log.i("tag","初始化结束");
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			// 第三个参数为false，表示直接登录账号
			letvGameSDK.doLogin(mactivity, loginCallback, false);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 账号接口(包括直接登录和切换账号)接口回调
	 */
	public static LoginCallback loginCallback = new LoginCallback() {
		@Override
		public void onLoginSuccess(LeUserInfo userInfo) {
			//			Log.i(TAG, "===onLoginSuccess is called===");
			log("===onLoginSuccess is called===");
			if (userInfo != null) {

								letvUser = new LetvUser();

				// 账号授权凭证access_token,
				// 游戏需使用此凭证前往https://sso.le.com/oauthopen/userbasic进行登录状态校验，并且获取letvid,nickname,avatar等用户信息
								letvUser.setAccess_token(userInfo.getAccessToken());
				uid = userInfo.getUserId();
				name = userInfo.getUserName();
				token = userInfo.getAccessToken();
				log("uid="+uid);
				log("name="+name);
				log("token="+token);

				// 乐视用户的平台唯一标识,不建议使用此信息，请使用access_token前往乐视账号服务端获取letvId等；
								letvUser.setLetvId(userInfo.getUserId());

				// 乐视用户昵称，不建议使用此信息，请使用access_token前往乐视账号服务端获取letvId等；
								letvUser.setNick_name(userInfo.getUserName());

				log("登录成功\n access_token:" + letvUser.getAccess_token());

				// 此处仅为方便展示，所以使用客户端数据，请实际使用时，使用通过服务端获取的用户信息；
				Toast("登录成功");
				loginSuce(mActivity, uid, name, token);
				doRealNameCertificationStatusCheck();
			} else {
				log("===登录失败，UserInfo为null===");
				loginFail();
//				Toast("登录失败，UserInfo为null，请尝试重新登录。");
				Toast("登录失败，请尝试重新登录。");
			}
		}

		@Override
		public void onLoginFailure(int errorCode, String errorMsg) {
			log("===onLoginFailure is called, error_code:" + errorCode+ ", error_message:" + errorMsg);
			loginFail();
//			Toast("登录失败! \n异常状态码:" + errorCode+ "\n异常状态信息:" + errorMsg);
			Toast("登录失败，请尝试重新登录。");
		}

		@Override
		public void onLoginCancel() {
			log("===onLoginCancel is called===");
			loginFail();
			Toast("登录取消");
		}
	};

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		doPay(morderid);
	}

	/**
	 * 支付
	 */
	private static void doPay(String morderid) {
		//		Log.i(TAG, "===doPay is called===");

		if (letvUser == null || TextUtils.isEmpty(letvUser.getLetvId())
				|| TextUtils.isEmpty(letvUser.getAccess_token())) {
			log("未登录账号，需要先登录账号");
			Toast("请先登录账号");
			return;
		}

		log("发起支付");

		LePayInfo payInfo = new LePayInfo();

		// 需要确保access_token校验通过才可使用；
		payInfo.setLetv_user_access_token(token);

		// 此处letvId使用客户端数据仅为方便，实际情况下请通过乐视账号服务端获取；
		payInfo.setLetv_user_id(uid);// 乐视平台用户唯一标识；

		// 支付回调地址，暂仅支持GET
		
		payInfo.setNotify_url(DeviceUtil.getGameInfo(mActivity, "callback"));

		// CP自定义订单号，需保证唯一性
		payInfo.setCooperator_order_no(morderid);

		log("YYWMain.mOrder.goods_id="+YYWMain.mOrder.goods_id);
		// 订单总金额，单位：元
		payInfo.setPrice(YYWMain.mOrder.money/100+"");

		// 商品id
		// 请CP自行维护游戏内商品详情（包括商品id，商品名称，金额，类型等等），并保证商品id唯一，商品id和商品名称一一对应；
		payInfo.setProduct_id("s_"+YYWMain.mOrder.goods);// 鍟嗗搧id

		// 商品名称
		// 请CP自行维护游戏内商品详情（包括商品id，商品名称，金额，类型等等），并保证商品id唯一，商品id和商品名称一一对应；
		if(YYWMain.mOrder.goods.equals("yb601")){
			YYWMain.mOrder.goods = "60钻石";
		}else if(YYWMain.mOrder.goods.equals("yb3001")){
			YYWMain.mOrder.goods = "300钻石";
		}else if(YYWMain.mOrder.goods.equals("yb9801")){
			YYWMain.mOrder.goods = "980钻石";
		}else if(YYWMain.mOrder.goods.equals("yb19801")){
			YYWMain.mOrder.goods = "1980钻石";
		}else if(YYWMain.mOrder.goods.equals("yb32801")){
			YYWMain.mOrder.goods = "3280钻石";
		}else if(YYWMain.mOrder.goods.equals("yb64801")){
			YYWMain.mOrder.goods = "6480钻石";
		}
		payInfo.setProduct_name(YYWMain.mOrder.goods);

		// 商品描述，请如实填写，不可为null和""
		payInfo.setProduct_desc(YYWMain.mOrder.goods);

		// 默认值：21600
		payInfo.setPay_expire("21600");

		// 货币种类，请填写：RMB
		payInfo.setCurrency("RMB");

		// 商品图片地址，请填入非null,非""字符串即可
		payInfo.setProduct_urls("NONE");

		// CP自定义信息，在支付异步通知中原样返回
		payInfo.setExtro_info("");

		letvGameSDK.doPay(mActivity, payInfo, payCallback);
	}

	/**
	 * 支付结果回调
	 */
	private static PayCallback payCallback = new PayCallback() {

		@Override
		public void onPayResult(String status, String errorMessage) {
			//			Log.i(TAG, "===onPayResult is called=== \nStatus:" + status
			//					+ "\nerrorMessage:" + errorMessage);
			log("===onPayResult is called=== \nStatus:" + status+ "\nerrorMessage:" + errorMessage);
			if (status.equals("SUCCESS")) {
				paySuce();
				log("支付成功 \n" + errorMessage);
				Toast("支付成功");
			} else if (status.equals("FAILT")) {
				payFail();
				log("支付失败 \n" + errorMessage);
				Toast("支付失败");
			} else if (status.equals("PAYED")) {
				payFail();
				log("已经支付 \n" + errorMessage);
				Toast("已经支付");
			} else if (status.equals("WAITTING")) {
				payFail();
				log("等待支付 \n" + errorMessage);
				Toast("等待支付");
			} else if (status.equals("NONETWORK")) {
				payFail();
				log("网络错误 \n" + errorMessage);
				Toast("网络错误");
			} else if (status.equals("NONE")) {
				payFail();
				log("未知错误 \n" + errorMessage);
				Toast("未知错误");
			} else if (status.equals("CANCEL")) {
				payFail();
				log("取消支付 \n" + errorMessage);
				Toast("取消支付");
			} else if (status.equals("COINLOCKUSER")) {
				payFail();
				log("金钻支付受限 \n"+ errorMessage);
				Toast("金钻支付受限");
			} else if(status.equals("CLOSURE")){
				payFail();
				log("支付受限 \n"+ errorMessage);
				Toast("支付受限");
			}else {
				payFail();
				log("支付失败 \nStatus:" + status+ "\nerrorMessage" + errorMessage);
				Toast("支付失败 \nStatus:" + status+ "\nerrorMessage" + errorMessage);
			}
		}
	};

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");


		// 监听游戏主界面Activity的回退事件，在此处调用SDK的退出接口，并在退出回调中处理逻辑；
				letvGameSDK.onExit(paramActivity, new ExitCallback() {
					
					@Override
					public void onSdkExitConfirmed() {
						log("===onSdkExitConfirmed is called===");
//						Toast("确认退出游戏");
						letvUser = null;
						mActivity.finish();
						System.exit(0);
//						callback.onExit();
//						Process.killProcess(Process.myPid());
					}
					
					@Override
					public void onSdkExitCanceled() {
						log("===onSdkExitCanceled is called===");
						// 玩家取消退出游戏;
						Toast("继续游戏");
					}
				});

	}

	
	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
	}
	public static void onResume(Activity paramActivity) {
		log("===onResume is called===");
		letvGameSDK.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		log("===onPause is called===");
		letvGameSDK.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		letvGameSDK.onDestory(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity2) {

	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {
		letvGameSDK.onCreate(paramActivity, new LeGameSDK.ActionCallBack() {
			@Override
			public void onExitApplication() {
			// 在此回调中，暂时不需要处理逻辑
			}
			});
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

			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
			}
		});
	}
	/*
	 * 打印日志
	 */
	public static void log(final String msg){
		new Thread(new Runnable() {
            @Override
			public void run() {
				Log.i("tag",msg);
			}
		});
	}

	/**
	 * 发起实名认证
	 */
	private static void doRealNameCertificate() {
		log("===doRealNameCertificate is called===");

		if (letvUser == null || TextUtils.isEmpty(letvUser.getLetvId())
				|| TextUtils.isEmpty(letvUser.getAccess_token())) {
			log("未登录账号，需要先登录账号");
			Toast("请先登录账号");
			return;
		}

		letvGameSDK.startUserCertification(new CertificationCallback() {
			@Override
			public void onCertificationSuccess() {
				Toast("认证成功");
			}

			@Override
			public void onCertificationFailure(int code, String message) {
//				Toast("认证失败\n" + "error_code:"+ code + "\nerror_message:" + message);
			}
		});
	}
	
	/**
	 * 校验用户的实名认证状态
	 */
	private static void doRealNameCertificationStatusCheck() {
		log("===doRealNameCertificationStatusCheck is called===");

		if (letvUser == null || TextUtils.isEmpty(letvUser.getLetvId())
				|| TextUtils.isEmpty(letvUser.getAccess_token())) {
			log("未登录账号，需要先登录账号");
			Toast("请先登录账号");
			return;
		}

		letvGameSDK
				.getUserCertificationStatus(new CertificationStatusCallback() {
					@Override
					public void onCertificationStatusSuccess(boolean certified,
							boolean adult) {
//						Toast("获取用户实名认证状态：\n"+ "是否认证：" + certified + "\n是否成人: " + adult);
						if(!adult){
							doRealNameCertificate();
						}
					}

					@Override
					public void onCertificationStatusFailure(int code,
							String message) {
//						Toast("请求失败\n"+ "error_code:" + code + "\nerror_message:"+ message);
					}
				});
	}

}
