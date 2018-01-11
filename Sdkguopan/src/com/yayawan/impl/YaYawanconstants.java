package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.flamingo.sdk.access.Callback;
import com.flamingo.sdk.access.GPApiFactory;
import com.flamingo.sdk.access.GPExitResult;
import com.flamingo.sdk.access.GPPayResult;
import com.flamingo.sdk.access.GPSDKGamePayment;
import com.flamingo.sdk.access.GPSDKInitResult;
import com.flamingo.sdk.access.GPSDKPlayerInfo;
import com.flamingo.sdk.access.GPUploadPlayerInfoResult;
import com.flamingo.sdk.access.GPUserResult;
import com.flamingo.sdk.access.IGPApi;
import com.flamingo.sdk.access.IGPExitObsv;
import com.flamingo.sdk.access.IGPPayObsv;
import com.flamingo.sdk.access.IGPSDKInitObsv;
import com.flamingo.sdk.access.IGPSDKInnerEventObserver;
import com.flamingo.sdk.access.IGPUploadPlayerInfoObsv;
import com.flamingo.sdk.access.IGPUserObsv;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

//	private static final Context mactivity = null;
//
//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static IGPApi mIGPApi;

	private static String uid;
	private static String username;
	private static String token;

	private static String role_Level = "1";
	private static String role_Id = "1";
	private static String role_Name = "1";
	private static String zone_Id = "1";
	private static String zone_Name = "1";

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		// 这是兼容当targetSdk设置为23或者以上的情况，如果targetSdk在22或者以下则可以直接同步调用GPApiFactory.getGPApi()即可
		GPApiFactory.getGPApiForMarshmellow(mactivity, new Callback() {
			@Override
			public void onCallBack(IGPApi igpApi) {
				
				mIGPApi = igpApi;
				
				//打开日志、发布状态切记不要打开
				mIGPApi.setLogOpen(false);
				
				mIGPApi.onCreate(mactivity);
				
				// sdk内部事件回调接口
				mIGPApi.setSDKInnerEventObserver(new IGPSDKInnerEventObserver() {
					@Override
					public void onSdkLogout() {
						// sdk内部登出了，游戏应该回到登录界面，然后重新调用登陆
						// writeLog("sdk登出回调:登录成功");
						// mIGPApi.login(mactivity, mUserObsv);
						Log.i("tag","注销1");
						loginOut();
//						GPApiFactory.getGPApi().reLogin(mActivity, mUserObsv);
					}

					@Override
					public void onSdkSwitchAccount() {
						// sdk内部切换了账号，这个时候游戏也应该回到登录界面，然后重新获取新的账号的参数（相当于sdk登录成功回调了）无需重新调用登录
						// writeLog("sdk切换回调:登录成功");
						// writeLog("可通过getLoginUin获取用户唯一uid");
						// writeLog("可通过getLoginToken获取用户的令牌");
						loginOut();
//						loginSuce(mActivity, mIGPApi.getLoginUin(), mIGPApi.getAccountName(), mIGPApi.getLoginToken());
						Log.i("tag","切换了账号2");
					}
				});
				// 回调之后才可调用初始化、登陆等接口
			}
		});
		String app_id= ""+DeviceUtil.getGameInfo(mactivity, "appid");
		String app_key= ""+DeviceUtil.getGameInfo(mactivity, "appkey");
		GPApiFactory.getGPApi().initSdk(mactivity, app_id, app_key, mInitObsv);
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
		
		if (isinit) {
			GPApiFactory.getGPApi().login(mactivity, mUserObsv);
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

		GPSDKGamePayment payParam = new GPSDKGamePayment();
		payParam.mItemName = YYWMain.mOrder.goods;
		payParam.mPaymentDes = YYWMain.mOrder.goods;
		payParam.mItemPrice = Float.parseFloat(YYWMain.mOrder.money/100+".00");
		payParam.mItemOrigPrice = Float.parseFloat(YYWMain.mOrder.money/100+".00");
		payParam.mItemId = "s_"+YYWMain.mOrder.goods;
		payParam.mSerialNumber = morderid;
		payParam.mCurrentActivity = mactivity;
		payParam.mPlayerId = role_Id; //角色id
		payParam.mPlayerNickName = role_Name; //角色名
		payParam.mServerId = zone_Id;    //服务器id
		payParam.mServerName = zone_Name;  //服务器名
		Float mRate= Float.parseFloat(DeviceUtil.getGameInfo(mactivity, "rate"));
		payParam.mRate = mRate;       //人民币兑换游戏内货币的比例，比如1元可购入10钻石，那就是10。

		Log.i("tag","payParam="+payParam );
		GPApiFactory.getGPApi().buy(payParam, mPayObsv);
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

		GPApiFactory.getGPApi().exit(mExitObsv);

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		role_Id =roleId;
		role_Name = roleName;
		role_Level = roleLevel;
		zone_Id =zoneId;
		zone_Name = zoneName;
		if(role_Id.equals("")){
			role_Id = "9527";
		}
		if (Integer.parseInt(ext) == 1){
			GPSDKPlayerInfo gpsdkPlayerInfo = new GPSDKPlayerInfo();
			gpsdkPlayerInfo.mType = GPSDKPlayerInfo.TYPE_ENTER_GAME; // 这个字段根据调用时机的不同，填入不同的类型
			gpsdkPlayerInfo.mGameLevel = role_Level;
			gpsdkPlayerInfo.mPlayerId = role_Id;
			gpsdkPlayerInfo.mPlayerNickName = role_Name;
			gpsdkPlayerInfo.mServerId = zone_Id;
			gpsdkPlayerInfo.mServerName = zone_Name;
			gpsdkPlayerInfo.mBalance = 0;
			gpsdkPlayerInfo.mGameVipLevel = "0";
			gpsdkPlayerInfo.mPartyName = "";
			//        writeLog("上报的信息为:" + gpsdkPlayerInfo.mGameLevel + ";" + gpsdkPlayerInfo.mPlayerId + ";" + gpsdkPlayerInfo.mPlayerNickName + ";" + gpsdkPlayerInfo.mServerId + ";" + gpsdkPlayerInfo.mServerName);
			// 第一次创建角色调用createPlayerInfo，后续同一个角色调用uploadPlayerInfo
			GPApiFactory.getGPApi().uploadPlayerInfo(gpsdkPlayerInfo, mGPUploadPlayerInfoObsv);
		}else if (Integer.parseInt(ext) == 2){
			GPSDKPlayerInfo gpsdkPlayerInfo = new GPSDKPlayerInfo();
			gpsdkPlayerInfo.mType = GPSDKPlayerInfo.TYPE_CREATE_ROLE; // 这个字段根据调用时机的不同，填入不同的类型
			gpsdkPlayerInfo.mGameLevel = role_Level;
			gpsdkPlayerInfo.mPlayerId = role_Id;
			gpsdkPlayerInfo.mPlayerNickName = role_Name;
			gpsdkPlayerInfo.mServerId = zone_Id;
			gpsdkPlayerInfo.mServerName = zone_Name;
			gpsdkPlayerInfo.mBalance = 0;
			gpsdkPlayerInfo.mGameVipLevel = "0";
			gpsdkPlayerInfo.mPartyName = "";
			//        writeLog("上报的信息为:" + gpsdkPlayerInfo.mGameLevel + ";" + gpsdkPlayerInfo.mPlayerId + ";" + gpsdkPlayerInfo.mPlayerNickName + ";" + gpsdkPlayerInfo.mServerId + ";" + gpsdkPlayerInfo.mServerName);
			// 第一次创建角色调用createPlayerInfo，后续同一个角色调用uploadPlayerInfo
			GPApiFactory.getGPApi().uploadPlayerInfo(gpsdkPlayerInfo, mGPUploadPlayerInfoObsv);
		}else if (Integer.parseInt(ext) == 3){
			GPSDKPlayerInfo gpsdkPlayerInfo = new GPSDKPlayerInfo();
			gpsdkPlayerInfo.mType = GPSDKPlayerInfo.TYPE_LEVEL_UP; // 这个字段根据调用时机的不同，填入不同的类型
			gpsdkPlayerInfo.mGameLevel = role_Level;
			gpsdkPlayerInfo.mPlayerId = role_Id;
			gpsdkPlayerInfo.mPlayerNickName = role_Name;
			gpsdkPlayerInfo.mServerId = zone_Id;
			gpsdkPlayerInfo.mServerName = zone_Name;
			gpsdkPlayerInfo.mBalance = 0;
			gpsdkPlayerInfo.mGameVipLevel = "0";
			gpsdkPlayerInfo.mPartyName = "";
			//        writeLog("上报的信息为:" + gpsdkPlayerInfo.mGameLevel + ";" + gpsdkPlayerInfo.mPlayerId + ";" + gpsdkPlayerInfo.mPlayerNickName + ";" + gpsdkPlayerInfo.mServerId + ";" + gpsdkPlayerInfo.mServerName);
			// 第一次创建角色调用createPlayerInfo，后续同一个角色调用uploadPlayerInfo
			GPApiFactory.getGPApi().uploadPlayerInfo(gpsdkPlayerInfo, mGPUploadPlayerInfoObsv);
		}
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
	 * 登录回调接口
	 */
	public static IGPUserObsv mUserObsv = new IGPUserObsv() {
		@Override
		public void onFinish(final GPUserResult result) {
			switch (result.mErrCode) {
			case GPUserResult.USER_RESULT_LOGIN_FAIL:
				//                    writeLog("登录回调:登录失败");
				loginFail();
				Toast("登录失败");
				break;
			case GPUserResult.USER_RESULT_LOGIN_SUCC:
				//                    writeLog("登录回调:登录成功");
				//                    writeLog("可通过getLoginUin获取用户唯一uid");
				//                    writeLog("可通过getLoginToken获取用户的令牌");
				uid = mIGPApi.getLoginUin();
				username = mIGPApi.getAccountName();
				token = mIGPApi.getLoginToken();
				loginSuce(mActivity, uid, username, token);
				Toast("登录成功");
				break;
			}
		}
	};

	/**
	 * 初始化回调接口
	 */
	private static IGPSDKInitObsv mInitObsv = new IGPSDKInitObsv() {
		@Override
		public void onInitFinish(GPSDKInitResult initResult) {
			//            Log.i(TAG, "GPSDKInitResult mInitErrCode: " + initResult.mInitErrCode);
			//            Log.i(TAG, "loginToken" + GPApiFactory.getGPApi().getLoginToken());
			switch (initResult.mInitErrCode) {
			case GPSDKInitResult.GPInitErrorCodeConfig:
				//                    writeLog("初始化回调:初始化配置错误");
				Log.i("tag","初始化回调:初始化配置错误");
				break;
			case GPSDKInitResult.GPInitErrorCodeNeedUpdate:
				//                    writeLog("初始化回调:游戏需要更新");
				Log.i("tag","初始化回调:游戏需要更新");
				break;
			case GPSDKInitResult.GPInitErrorCodeNet:
				//                    writeLog("初始化回调:初始化网络错误");
				Log.i("tag","初始化回调:初始化网络错误");
				break;
			case GPSDKInitResult.GPInitErrorCodeNone:
				//只有回调是成功的时候才能进行下面的操作
				//                    writeLog("初始化回调:初始化成功");
				Log.i("tag","初始化回调:初始化成功");
				isinit = true;
				//                    mGoToLogin.performClick();
				login(mActivity);
				break;
			}
		}
	};


	/**
	 * 上报用户信息回调接口
	 */
	private static IGPUploadPlayerInfoObsv mGPUploadPlayerInfoObsv = new IGPUploadPlayerInfoObsv() {
		@Override
		public void onUploadFinish(final GPUploadPlayerInfoResult gpUploadPlayerInfoResult) {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (gpUploadPlayerInfoResult.mResultCode == GPUploadPlayerInfoResult.GPSDKUploadSuccess){
						//                        writeLog("上报数据回调:成功");
						//						Toast.makeText(mActivity, "上报数据成功", Toast.LENGTH_SHORT).show();
						Log.i("tag","上报数据成功");
					}else{
						//                        writeLog("上报数据回调:失败");
						//						Toast.makeText(mActivity, "上报数据失败", Toast.LENGTH_SHORT).show();
						Log.i("tag","上报数据失败");
					}
				}
			});

		}
	};

	/**
	 * 退出界面回调接口
	 */
	private static IGPExitObsv mExitObsv = new IGPExitObsv() {
		@Override
		public void onExitFinish(GPExitResult exitResult) {
			switch (exitResult.mResultCode) {
			case GPExitResult.GPSDKExitResultCodeError:
				//                    writeLog("退出回调:调用退出弹框失败");
				Toast("弹出退出框失败");
				break;
			case GPExitResult.GPSDKExitResultCodeExitGame:
				//                    writeLog("退出回调:调用退出游戏，请执行退出逻辑");
				//                    Toast.makeText(MainActivity.this, "GPSDKExitResultCodeExitGame", Toast.LENGTH_SHORT).show();
				//                    Intent startMain = new Intent(Intent.ACTION_MAIN);
				//                    startMain.addCategory(Intent.CATEGORY_HOME);
				//                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//                    startActivity(startMain);
				//                    System.exit(0);
				GPSDKPlayerInfo gpsdkPlayerInfo = new GPSDKPlayerInfo();
				gpsdkPlayerInfo.mType = GPSDKPlayerInfo.TYPE_EXIT_GAME; // 这个字段根据调用时机的不同，填入不同的类型
				gpsdkPlayerInfo.mGameLevel = role_Level;
				gpsdkPlayerInfo.mPlayerId = role_Id;
				gpsdkPlayerInfo.mPlayerNickName = role_Name;
				gpsdkPlayerInfo.mServerId = zone_Id;
				gpsdkPlayerInfo.mServerName = zone_Name;
				gpsdkPlayerInfo.mBalance = 0;
				gpsdkPlayerInfo.mGameVipLevel = "0";
				gpsdkPlayerInfo.mPartyName = "";
				// 第一次创建角色调用createPlayerInfo，后续同一个角色调用uploadPlayerInfo
				GPApiFactory.getGPApi().uploadPlayerInfo(gpsdkPlayerInfo, mGPUploadPlayerInfoObsv);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mActivity.finish();
						System.exit(0);
					}
				}, 100);
				break;
			case GPExitResult.GPSDKExitResultCodeCloseWindow:
				//                    writeLog("退出回调:调用关闭退出弹框");
				Toast.makeText(mActivity, "继续游戏", Toast.LENGTH_SHORT).show();

				break;
			}
		}
	};

	/**
	 * 支付回调接口
	 */
	private static IGPPayObsv mPayObsv = new IGPPayObsv() {
		@Override
		public void onPayFinish(GPPayResult payResult) {
			if (payResult == null) {
				return;
			}
			showPayResult(payResult);
		}
	};

	static void showPayResult(final GPPayResult result) {
		String tips = "";
		switch (result.mErrCode) {
		case GPPayResult.GPSDKPayResultCodeSucceed:
			//	                show(MainActivity.this, tips + "succ");
			Log.i("tag","支付回调:购买成功");
			Toast("支付成功");
			paySuce();
			break;
		case GPPayResult.GPSDKPayResultCodePayBackground:
			//	                writeLog("支付回调:后台正在轮循购买");
			//	                show(MainActivity.this, tips + "后台正在轮循购买");
			payFail();
			Toast("支付失败");
			Log.i("tag",tips + "后台正在轮循购买");
			break;
		case GPPayResult.GPSDKPayResultCodeBackgroundSucceed:
			//	                show(MainActivity.this, tips + "后台购买成功");
			Log.i("tag","支付回调:后台购买成功");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodeBackgroundTimeOut:
			//	                show(MainActivity.this, tips + "后台购买超时");
			Log.i("tag","支付回调:后台购买超时");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodeCancel:
			//	                show(MainActivity.this, tips + "用户取消");
			Log.i("tag","支付回调:用户取消");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodeNotEnough:
			//	                show(MainActivity.this, tips + "余额不足");
			Log.i("tag","支付回调:余额不足");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodeOtherError:
			//	                show(MainActivity.this, tips + "其他错误");
			Log.i("tag","支付回调:其他错误");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodePayForbidden:
			//	                show(MainActivity.this, tips + "用户被限制");
			Log.i("tag","支付回调:用户被限制");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodePayHadFinished:
			//	                show(MainActivity.this, tips + "该订单已经完成");
			Log.i("tag","支付回调:该订单已经完成");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodeServerError:
			//	                show(MainActivity.this, tips + "服务器错误");
			Log.i("tag","支付回调:服务器错误");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultNotLogined:
			//	                show(MainActivity.this, tips + "无登陆");
			Log.i("tag","支付回调:无登陆");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultParamWrong:
			//	                show(MainActivity.this, tips + "参数错误");
			Log.i("tag","支付回调:参数错误");
			payFail();
			Toast("支付失败");
			break;
		case GPPayResult.GPSDKPayResultCodeLoginOutofDate:
			//	                show(MainActivity.this, tips + "登录态失效");
			Log.i("tag","支付回调:登录态失效");
			payFail();
			Toast("支付失败");
			break;
		default:
			//	                show(MainActivity.this, tips + "fail " + result.toString());
			Log.i("tag","支付回调:未知错误 " + result.toString());
			payFail();
			Toast("支付失败");
			break;
		}
	}
}
