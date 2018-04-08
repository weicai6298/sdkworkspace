package com.yayawan.impl;

import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnExitListner;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppType;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOffline;
import com.xiaomi.gamecenter.sdk.entry.ScreenOrientation;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static String session;

	private static String paycode;;

	private static boolean isinit = false;
	
	private static int isyoumeng;

	private static String uid;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		String youmeng = DeviceUtil.getGameInfo(mActivity, "isyoumeng");
		isyoumeng = Integer.parseInt(youmeng);
		if(isyoumeng == 1){
			UMGameAgent.setDebugMode(true);
			UMGameAgent.init(mActivity);
		}
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		Log.i("tag", "初始化开始");
		Log.i("tag", "applicationContext=" + applicationContext);
		String AppID = "" + DeviceUtil.getGameInfo(applicationContext, "AppID");
		Log.i("tag", "AppID=" + AppID);
		String AppKey = ""+ DeviceUtil.getGameInfo(applicationContext, "AppKey");
		Log.i("tag", "AppKey=" + AppKey);

		MiAppInfo appInfo = new MiAppInfo();
		appInfo.setAppId(AppID);
		appInfo.setAppKey(AppKey);
		// appInfo.setOrientation( ScreenOrientation.horizontal); //横竖屏
		appInfo.setAppType(MiAppType.offline);
		MiCommplatform.Init(applicationContext, appInfo);
		isinit = true;
		Log.i("tag", "初始化结束");
	}

	static OnLoginProcessListener logincall = new OnLoginProcessListener() {

		@Override
		public void finishLoginProcess(int code, MiAccountInfo arg1) {
			// TODO Auto-generated method stub
			Log.i("tag", "code=" + code);
			Log.i("tag", "arg1=" + arg1);
			switch (code) {
			case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
				// 登陆成功
				// 获取用户的登陆后的UID（即用户唯一标识）
				uid = arg1.getUid() + "";
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
				// 登录操作正在进行中
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
		if (isinit) {
			MiCommplatform.getInstance().miLogin(mActivity, logincall);
		} else {
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

		Log.i("tag", "支付start");
		Log.i("tag", "YYWMain.mOrder.goods=" + YYWMain.mOrder.goods);// 游戏方商品名称
		paycode = getpaycode(YYWMain.mOrder.goods);
		Log.i("tag", "paycode=" + paycode);
		MiBuyInfoOffline offline = new MiBuyInfoOffline();
		offline.setCpOrderId(morderid);// 订单号唯一(不为空)
		offline.setProductCode(paycode);// 商品代码,开发者 申请获得(不为空)
		offline.setCount(1);// 购买数量 (商品数量最大 9999,最小 1)(不为空)
		MiCommplatform.getInstance().miUniPayOffline(mactivity, offline,
				new OnPayProcessListener() {

					@Override
					public void finishPayProcess(int code) {
						// TODO Auto-generated method stub
						switch (code) {
						case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS:
							// 购买成功 ,请处理发货
							mActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Log.i("tag", "支付成功");
									paySuce();
									paycode = "";
								}
							});
							break;
						case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_CANCEL:
							// 取消购买
	                        mActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Log.i("tag", "支付失败1");
									payFail();
									paycode = "";
								}
							});
							break;
						case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE:
							// 购买失败
							 mActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Log.i("tag", "支付失败2");
										payFail();
										paycode = "";
									}
								});
							break;
						case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
							// 操作正在执行
							  mActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Log.i("tag", "支付成功-操作正在执行");
										paySuce();
									}
								});
							break;
						default:
							// 购买失败
							 mActivity.runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										Log.i("tag", "支付失败3");
										payFail();
										paycode = "";
									}
								});
							break;
						}
					}
				});
	}

	private static String getpaycode(String goods) {
		// 街机捕鱼的
		// if(YYWMain.mOrder.goods.equals("月卡礼包")){
		// paycode = "a1";
		// }else if(YYWMain.mOrder.goods.equals("月卡续费")){
		// paycode = "a2";
		// }else if(YYWMain.mOrder.goods.equals("幸运福袋")){
		// paycode = "a3";
		// }else if(YYWMain.mOrder.goods.equals("起航礼包")){
		// paycode = "a4";
		// }else if(YYWMain.mOrder.goods.equals("首充大返利")){
		// paycode = "a5";
		// }else if(YYWMain.mOrder.goods.equals("2.5万金币")){
		// paycode = "a6";
		// }else if(YYWMain.mOrder.goods.equals("5万金币")){
		// paycode = "a7";
		// }else if(YYWMain.mOrder.goods.equals("15万金币")){
		// paycode = "a8";
		// }else if(YYWMain.mOrder.goods.equals("25万金币")){
		// paycode = "a9";
		// }else if(YYWMain.mOrder.goods.equals("50万金币")){
		// paycode = "a10";
		// }else if(YYWMain.mOrder.goods.equals("250万金币")){
		// paycode = "a11";
		// }else if(YYWMain.mOrder.goods.equals("500万金币")){
		// paycode = "a12";
		// }else if(YYWMain.mOrder.goods.equals("50钻石")){
		// paycode = "a13";
		// }else if(YYWMain.mOrder.goods.equals("100钻石")){
		// paycode = "a14";
		// }else if(YYWMain.mOrder.goods.equals("300钻石")){
		// paycode = "a15";
		// }else if(YYWMain.mOrder.goods.equals("500钻石")){
		// paycode = "a16";
		// }else if(YYWMain.mOrder.goods.equals("1000钻石")){
		// paycode = "a17";
		// }else if(YYWMain.mOrder.goods.equals("5000钻石")){
		// paycode = "a18";
		// }else if(YYWMain.mOrder.goods.equals("10000钻石")){
		// paycode = "a19";
		// }else if(YYWMain.mOrder.goods.equals("无限火力")){
		// paycode = "a20";
		// }else if(YYWMain.mOrder.goods.equals("炎龙炮")){
		// paycode = "a21";
		// }else if(YYWMain.mOrder.goods.equals("战神无双")){
		// paycode = "a22";
		// }else if(YYWMain.mOrder.goods.equals("满地红")){
		// paycode = "a23";
		// }else if(YYWMain.mOrder.goods.equals("6元礼包")){
		// paycode = "a24";
		// }else if(YYWMain.mOrder.goods.equals("12元礼包")){
		// paycode = "a25";
		// }else if(YYWMain.mOrder.goods.equals("30元礼包")){
		// paycode = "a26";
		// }else if(YYWMain.mOrder.goods.equals("新手特惠礼包")){
		// paycode = "a27";
		// }else if(YYWMain.mOrder.goods.equals("1元礼包")){
		// paycode = "a28";
		// }else if(YYWMain.mOrder.goods.equals("超值礼包")){
		// paycode = "a29";
		// }else if(YYWMain.mOrder.goods.equals("清凉武士")){
		// paycode = "a30";
		// }
		String paycode = "";
		//捕鱼大世界
//		if (goods.equals("首充特惠礼包")) {
//			paycode = "a1";
//		} else if (goods.equals("贵族礼包")) {
//			paycode = "a2";
//		} else if (goods.equals("金币礼包(6元)")) {
//			paycode = "a3";
//		} else if (goods.equals("金币礼包(12元)")) {
//			paycode = "a4";
//		} else if (goods.equals("金币礼包(28元)")) {
//			paycode = "a5";
//		} else if (goods.equals("金币礼包(50元)")) {
//			paycode = "a6";
//		} else if (goods.equals("金币礼包(108元)")) {
//			paycode = "a7";
//		} else if (goods.equals("金币礼包(328元)")) {
//			paycode = "a8";
//		} else if (goods.equals("金币礼包(618元)")) {
//			paycode = "a9";
//		} else if (goods.equals("钻石礼包(6元)")) {
//			paycode = "a10";
//		} else if (goods.equals("钻石礼包(12元)")) {
//			paycode = "a11";
//		} else if (goods.equals("钻石礼包(28元)")) {
//			paycode = "a12";
//		} else if (goods.equals("钻石礼包(50元)")) {
//			paycode = "a13";
//		} else if (goods.equals("钻石礼包(108元)")) {
//			paycode = "a14";
//		} else if (goods.equals("钻石礼包(328元)")) {
//			paycode = "a15";
//		}
		
		//全民大富豪
		if (goods.equals("60金币")) {
			paycode = "a1";
		} else if (goods.equals("300金币")) {
			paycode = "a2";
		} else if (goods.equals("980金币")) {
			paycode = "a3";
		} else if (goods.equals("1980金币")) {
			paycode = "a4";
		} else if (goods.equals("3280金币")) {
			paycode = "a5";
		} else if (goods.equals("6480金币")) {
			paycode = "a6";
		} else if (goods.equals("10金币")) {
			paycode = "a7";
		} else if (goods.equals("30金币")) {
			paycode = "a8";
		} else if (goods.equals("1元礼包")) {
			paycode = "a9";
		} else if (goods.equals("60金币")) {
			paycode = "a10";
		} else if (goods.equals("头衔福利礼包")) {
			paycode = "a11";
		} else if (goods.equals("星级员工礼包")) {
			paycode = "a12";
		} else if (goods.equals("部门猎聘礼包")) {
			paycode = "a13";
		} else if (goods.equals("建筑猎聘礼包")) {
			paycode = "a14";
		} else if (goods.equals("月卡")) {
			paycode = "a15";
		}else if (goods.equals("周卡")) {
			paycode = "a16";
		}else if (goods.equals("季卡")) {
			paycode = "a17";
		}else if (goods.equals("白银月卡")) {
			paycode = "a18";
		}else if (goods.equals("黄金月卡")) {
			paycode = "a19";
		}else if (goods.equals("铂金月卡")) {
			paycode = "a20";
		}else if (goods.equals("至尊月卡")) {
			paycode = "a21";
		}else if (goods.equals("新手大礼包")) {
			paycode = "a22";
		}else if (goods.equals("创业大礼包")) {
			paycode = "a23";
		}else if (goods.equals("企业家大礼包")) {
			paycode = "a24";
		}else if (goods.equals("富豪大礼包")) {
			paycode = "a25";
		}else if (goods.equals("新手大礼包2")) {
			paycode = "a26";
		}else if (goods.equals("创业大礼包2")) {
			paycode = "a27";
		}else if (goods.equals("企业家大礼包2")) {
			paycode = "a28";
		}else if (goods.equals("富豪大礼包2")) {
			paycode = "a29";
		}else if (goods.equals("新手大礼包3")) {
			paycode = "a30";
		}else if (goods.equals("创业大礼包3")) {
			paycode = "a31";
		}else if (goods.equals("企业家大礼包3")) {
			paycode = "a32";
		}else if (goods.equals("富豪大礼包3")) {
			paycode = "a33";
		}else if (goods.equals("新手大礼包4")) {
			paycode = "a34";
		}else if (goods.equals("创业大礼包4")) {
			paycode = "a35";
		}else if (goods.equals("企业家大礼包4")) {
			paycode = "a36";
		}else if (goods.equals("富豪大礼包4")) {
			paycode = "a37";
		}else if (goods.equals("6元礼包")) {
			paycode = "a38";
		}
		return paycode;
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
				MiCommplatform.getInstance().miAppExit(mActivity,
						new OnExitListner() {

							@Override
							public void onExit(int code) {
								Log.e("errorCode===", code + "");
								Log.e("tag", "code=" + code);
								if (code == MiErrorCode.MI_XIAOMI_EXIT) {
									// 执行退出的一些操作
									mActivity.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											if(isyoumeng == 1){
												Log.i("tag", "友盟退出");
												MobclickAgent.onProfileSignOff();
//												MobclickAgent.onKillProcess(mActivity);
											}
//											callback.onExit();
											mActivity.finish();
											System.exit(0);
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
	public static void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,
			String roleCTime, String ext) {
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		if (Integer.parseInt(ext) == 1){
			if(isyoumeng ==1){
				Log.i("tag", "友盟进入游戏");
				MobclickAgent.onProfileSignIn(uid);
			}
		}
	}

	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		if(isyoumeng == 1){
			MobclickAgent.onResume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		if(isyoumeng == 1){
			MobclickAgent.onPause(paramActivity);
		}
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

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
