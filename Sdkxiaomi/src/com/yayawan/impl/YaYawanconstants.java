package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnExitListner;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppType;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;
import com.xiaomi.gamecenter.sdk.entry.ScreenOrientation;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static String session;

//	private static String paycode;

	private static boolean isinit=false;
	
	public static MiAppInfo appInfo;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		String AppID = ""+DeviceUtil.getGameInfo(mactivity, "AppID");
		Log.i("tag","AppID="+AppID);
		String AppKey = ""+DeviceUtil.getGameInfo(mactivity, "AppKey");
		Log.i("tag","AppKey="+AppKey);
		appInfo = new MiAppInfo();
		appInfo.setAppId( AppID );
		appInfo.setAppKey( AppKey );
		appInfo.setAppType(MiAppType.online);
		appInfo.setOrientation( DeviceUtil.isLandscape(mactivity)?ScreenOrientation.horizontal:ScreenOrientation.vertical ); //横竖屏
		MiCommplatform.Init( mactivity, appInfo );
		isinit = true;
//		login(mactivity);
		Log.i("tag","初始化结束");
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		
	}

	static OnLoginProcessListener logincall = new OnLoginProcessListener() {

		@Override
		public void finishLoginProcess( int code, MiAccountInfo arg1 ) {
			Log.i("tag", "code="+code);
			Log.i("tag", "arg1="+arg1);
			switch( code )
			{
			case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
				// 登陆成功
				//获取用户的登陆后的UID（即用户唯一标识）
				String uid = arg1.getUid()+"";
				session = arg1.getSessionId();
				loginSuce(mActivity, uid, uid, session);
				Log.i("tag", "登录成功1");
				break;
			case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL:
				// 登陆失败
				loginFail();
				Log.i("tag", "登录失败");
				break;
			case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_CANCEL:
				// 取消登录
				loginFail();
				Log.i("tag", "登录失败");
				break;
			case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:	
				//登录操作正在进行中
				break;		
			default:
				// 登录失败
				loginFail();
				Log.i("tag", "登录失败");
				break;
			}
		}
	};

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");

		if(isinit){
			MiCommplatform.getInstance().miLogin(mActivity, logincall);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay( Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");

		Log.i("tag","支付start");		
		Log.i("tag", "YYWMain.mOrder.goods="+YYWMain.mOrder.goods);// 游戏方商品名称
		MiBuyInfo miBuyInfo = new MiBuyInfo();
		miBuyInfo.setCpOrderId(morderid);
		miBuyInfo.setCpUserInfo( "" );
		int money = Integer.parseInt(YYWMain.mOrder.money/100+"");
		miBuyInfo.setAmount(money);
		MiCommplatform.getInstance().miUniPay(mactivity, miBuyInfo, new OnPayProcessListener() {
			
			@Override
			public void finishPayProcess(int code ) {
				switch( code ) {
				case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
					//购买成功 ,请处理发货
					Log.i("tag", "支付成功");
					paySuce();
					Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
					Log.i("tag", "支付成功1");
					break;
				case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_CANCEL:
					//取消购买
					payFail();
					break;
				case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE:
					//购买失败
					payFail();
					break;
				case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
					//操作正在执行
					break;
				default:
					//购买失败
					payFail();
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
		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				MiCommplatform.getInstance().miAppExit( mActivity, new OnExitListner(){

					@Override
					public void onExit( int code ){
						Log.e( "errorCode===", code + "" );
						Log.e( "tag", "code="+code );
						if ( code == MiErrorCode.MI_XIAOMI_EXIT ){
							// 执行退出的一些操作
							mActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									callback.onExit();
								}
							});
						}
					}
				});
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

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

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
}
