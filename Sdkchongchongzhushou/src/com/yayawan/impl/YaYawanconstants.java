package com.yayawan.impl;

import java.util.HashMap;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.lion.ccpay.bean.PlayUserInfo;
import com.lion.ccpay.sdk.CCPaySdk;
import com.lion.ccsdk.SdkExitAppListener;
import com.lion.ccsdk.SdkLoginListener;
import com.lion.ccsdk.SdkLogoutListener;
import com.lion.ccsdk.SdkPayListener;
import com.lion.ccsdk.SdkUser;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yayawan.main.Kgame;
import com.yayawan.main.YYWMain;
import com.yayawan.sdktemplate.MainActivity;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;
	

	private static boolean isinit=false;

	private static boolean islogin=false;
	
	private static String roleid = "123";
	private static String rolename  = "123";
	private static String rolelevel = "123";
	private static String zoneid = "123";
	private static String zonename = "123";
	private static String rolectime = "123";
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Log.i("tag","Activity初始化");
		CCPaySdk.getInstance().init(mactivity);
		
		CCPaySdk.getInstance().onCreate(mActivity);
		initCCPaySdkLoginOut();
		isinit = true ;
		Log.i("tag","Activity初始化结束");
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		Log.i("tag","application初始化结束");
		String APP_ID= ""+DeviceUtil.getGameInfo(applicationContext, "APP_ID");
		Log.i("tag","APP_ID="+APP_ID);
        DeviceUtil.appid = APP_ID.substring(2, APP_ID.length());
        DeviceUtil.gameid = APP_ID.substring(2, APP_ID.length());
        Log.i("tag","gameid="+DeviceUtil.gameid);
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if((isinit) && (!CCPaySdk.getInstance().isLogin())){
			CCPaySdk.getInstance().login(mactivity, false, new SdkLoginListener() {

				public void onLoginSuccess(SdkUser user) {
					ToastUtils.showLongToast(mactivity, "登录成功\n" + "uid:" + user.uid + "\ntoken:" + user.token + "\nuserName:" + user.userName);
					CCPaySdk.getInstance().showFloating(mactivity);
					String uid = user.uid;
					String username = user.userName;
					String token = user.token;
					loginSuce(mactivity, uid, username, token);
					islogin = true;
				}

				public void onLoginFail(String message) {
					ToastUtils.showLongToast(mactivity, "登录失败~");
					loginFail();
				}

				public void onLoginCancel() {
					ToastUtils.showLongToast(mactivity, "登录取消~");
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
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		 PlayUserInfo playUserInfo = getPlayUserInfo(PlayUserInfo.TYPE_ENTER_GAME);
		 Log.i("tag","playUserInfo = " + playUserInfo);
		CCPaySdk.getInstance().pay4OLGame(mactivity, morderid,"", YYWMain.mOrder.goods, YYWMain.mOrder.money/100+"","", playUserInfo, new SdkPayListener() {
			
			 @Override
	            public void onPayResult(int status, String tn, String money) {
	                String text = "";
	                switch (status) {
	                   case SdkPayListener.CODE_SUCCESS://支付成功
	                      text = "支付成功\n";
	                      paySuce();
	                      Log.i("tag","支付成功");
	                      break;
	                   case SdkPayListener.CODE_FAIL://支付失败
	                      text = "支付失败\n";
	                      Log.i("tag","支付失败");
	                      payFail();
	                      break;
	                   case SdkPayListener.CODE_CANCEL://支付取消
	                      text = "支付取消\n";
	                      Log.i("tag","支付取消");
	                      payFail();
	                      break;
	                   case SdkPayListener.CODE_UNKNOWN://支付结果未知
	                      text = "支付结果未知\n";
	                      Log.i("tag","支付结果未知");
	                      payFail();
	                      break;
	                }
//	                ToastUtils.showLongToast(mActivity, text + "status:" + status + "\ntn:" + tn + "\nmoney:" + money);
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
			CCPaySdk.getInstance().exitApp(paramActivity, true , new SdkExitAppListener() {
				@Override
				public void onExitApp() {
					CCPaySdk.getInstance().killApp(mActivity);
					//处理游戏APP退出业务
					callback.onExit();
				}
			});
//		}
	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		roleid = roleId;
		rolename =roleName;
		rolelevel =roleLevel;
		zoneid = zoneId;
		zonename = zoneName;
		rolectime = roleCTime;
		//1为角色登陆成功  2为角色创建  3为角色升级
		if (Integer.parseInt(ext) == 1){
//该方法将调用的时机分为几种类型：    1：选择服务器    2：创建角色    3：进入游戏    4：等级提升    5：退出游戏
			CCPaySdk.getInstance().submitExtraData(getPlayUserInfo(PlayUserInfo.TYPE_ENTER_GAME));
		}else if(Integer.parseInt(ext) == 2){
			CCPaySdk.getInstance().submitExtraData(getPlayUserInfo(PlayUserInfo.TYPE_CREATE_ROLE));
		}else if(Integer.parseInt(ext) == 3){
			CCPaySdk.getInstance().submitExtraData(getPlayUserInfo(PlayUserInfo.TYPE_LEVEL_UP));
		}
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		CCPaySdk.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		CCPaySdk.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		CCPaySdk.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
		CCPaySdk.getInstance().onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub

	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		CCPaySdk.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		CCPaySdk.getInstance().onStop(paramActivity);
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
		// 支付失败
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	 private static void initCCPaySdkLoginOut() {
		   CCPaySdk.getInstance().setOnLoginOutListener(new SdkLogoutListener() {

			  public void onLoginOut() {
				 ToastUtils.showLongToast(mActivity, "账号注销了~");
				 mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loginOut();
					}
				});
			  }
		   });
	    }


	 //该方法将调用的时机分为几种类型：    1：选择服务器    2：创建角色    3：进入游戏    4：等级提升    5：退出游戏
	    public static PlayUserInfo getPlayUserInfo(int type) {
		   PlayUserInfo playUserInfo = new PlayUserInfo();
		   playUserInfo.setDataType(type);
		   
		   playUserInfo.setServerID(Integer.parseInt(zoneid));
		   playUserInfo.setServerName(zonename);
		   
		   if (PlayUserInfo.TYPE_SELECT_SERVER != type) {
			  playUserInfo.setMoneyNum(500);
			  playUserInfo.setRoleCreateTime(Long.parseLong(rolectime));
			  playUserInfo.setRoleGender(1);
			  playUserInfo.setRoleID(roleid);
			  playUserInfo.setRoleLevel(Integer.parseInt(rolelevel));
//			  playUserInfo.setRoleLevelUpTime(1498115418);
			  playUserInfo.setRoleName(rolename);
//			  playUserInfo.setVip("");
		   }
		   return playUserInfo;
	    }

}
