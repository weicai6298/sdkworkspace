package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.game.sdk.YXFSDKManager;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.OnLoginListener;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.RolecallBack;
import com.game.sdk.domain.onRoleListener;
import com.game.sdk.util.Constants;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yaoyue.release.ICallback;
import com.yaoyue.release.YYReleaseSDK;
import com.yaoyue.release.model.GameInfo;
import com.yaoyue.release.model.GamePayInfo;
import com.yaoyue.release.model.UserInfoModel;
import com.yaoyue.release.service.InitService;
import com.yaoyue.release.util.SDKUtils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	

	private static Activity mActivity;

	private static boolean isinit=false;
	
	private static GameInfo gameInfo;
	
	public static String yxf_uid;
	
	
	public static String role_Id = "123";
	private static String role_Name = "123";
	private static String role_Level = "123";
	public static String zone_Id = "123";
	private static String zone_Name = "123";

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		YYReleaseSDK.getInstance().sdkInit(mactivity, callback);
	}
	
	private static ICallback callback = new ICallback() {
        private String userId;

        @Override
        public void paySuccess(String orderId) {
//            writeLog("支付成功 orderId = " + orderId);
        	paySuce();
        }
        @Override
        public void onError(int type, String message) {
            switch (type) {
                case ICallback.INIT:
                    Log.i("tag","初始化失败 " + message);
                    break;
                case ICallback.LOGIN:
                   Log.i("tag","登陆失败 " + message);
                   loginFail();
                    break;
                case ICallback.CREATE_ROLE:
                    Log.i("tag","创建角色失败 " + message);
                    break;
                case ICallback.UPLOAD_GAME_INFO:
                	Log.i("tag","更新角色信息失败 " + message);
                    break;
                case ICallback.PAY:
                    Log.i("tag","支付失败 " + message);
                    payFail();
                    break;
                default:
                   Log.i("tag","onError type = " + type + "  , message = " + message);
                    break;
            }
        }
        @Override
        public void logoutSuccess() {
        	Log.i("tag","登出成功");
            mActivity.runOnUiThread(new Runnable() {
				public void run() {
					loginOut();
				}
			});
        }



        @Override
        public void initSuccess() {
            isinit = true;
            Log.i("tag","初始化成功");
        }

        @Override
        public void loginSuccess(UserInfoModel userInfoModel) {

            // userInfoModel.id 在5.0 已经废弃,需要通过 sessionId从服务器获取

            Log.i("tag","登陆成功,  " +   " sessionId = " + userInfoModel.sessionId);
            //登陆验证 token的有效时间为2分钟
            //对userId进行判断，如果userId发生改变，重新回到选服界面
            if (userId == null || userId == userInfoModel.id) {
            	yxf_uid = userInfoModel.id;
                loginSuce(mActivity, userInfoModel.id, userInfoModel.userName, userInfoModel.sessionId);
                //进入游戏
            } else {
                //清除角色信息，回到选服界面
                userId = userInfoModel.id;
                Log.i("tag","userId == null || userId == userInfoModel.id");
            }
        }

        @Override
        public void setGameInfoSuccess(String loginTime) {
        	Log.i("tag","调用进入游戏 setGameInfo Success  loginTime =" + loginTime);
        }

        @Override
        public void exitSuccess() {
//        	Log.i("tag","退出成功");
//            exitCallback.onExit();
        }

        @Override
        public void createRoleSuccess() {
        	Log.i("tag","创建角色成功");
        }

    };
	
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
			YYReleaseSDK.getInstance().sdkLogin(mactivity, callback);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(final Activity mactivity, String morderid, String sign) {

		Yayalog.loger("YaYawanconstantssdk支付");
		 GamePayInfo payInfo = new GamePayInfo();
	        payInfo.setExtInfo("pay");
	        payInfo.setMoney(YYWMain.mOrder.money+"");
	        payInfo.setNotifyUrl(DeviceUtil.getGameInfo(mActivity, "callback"));
	        payInfo.setCpOrderId(morderid);
	        payInfo.setProductCount(1);
	        payInfo.setProductId("s_"+YYWMain.mOrder.goods);
	        payInfo.setProductName(YYWMain.mOrder.goods);
//	        String appId = SDKUtils.getAppId(mactivity);
//	        String sign_key="";//这个可以在对接群中的参数中
//	        String sign = sign(appId, InitService.mUserInfoModel.id, "353535", "111111", "123", mMonnyEdit.getText().toString(), sign_key);
	        payInfo.setSign(sign);
		YYReleaseSDK.getInstance().doPay(mactivity, gameInfo, payInfo, callback);
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
//		exitCallback = callback;
//		Log.i("tag","退出-gameInfo =" +gameInfo);
//		YYReleaseSDK.getInstance().onSdkExit(paramActivity, gameInfo, (ICallback) callback);
		KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

			@Override
			public void onSuccess(User arg0, int arg1) {
				callback.onExit();
			}

			@Override
			public void onLogout() {
			}

			@Override
			public void onError(int arg0) {
			}

			@Override
			public void onCancel() {
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
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		role_Id = roleId;
		role_Name = roleName;
		role_Level = roleLevel;
		zone_Id = zoneId;
		zone_Name = zoneName;
		Log.i("tag","role_Id = " +role_Id);
		Log.i("tag","role_Name = " +role_Name);
		Log.i("tag","role_Level = " +role_Level);
		Log.i("tag","zone_Id = " +zone_Id);
		Log.i("tag","zone_Name = " +zone_Name);
		
		gameInfo = new GameInfo();
        gameInfo.setRoleId(roleId);
        gameInfo.setRoleLevel(roleLevel);
        gameInfo.setRoleName(roleName);
        gameInfo.setZoneId(zoneId);
        gameInfo.setServerId(zoneId);
        gameInfo.setZoneName(zoneName);
        gameInfo.setVipLevel("1");
		if (Integer.parseInt(ext)==1) {
			YYReleaseSDK.getInstance().setGameInfo(paramActivity, gameInfo, true, callback);
		}else if(Integer.parseInt(ext)==2) {
			YYReleaseSDK.getInstance().createRole(paramActivity, gameInfo, callback);
		}else if(Integer.parseInt(ext)==3){
			YYReleaseSDK.getInstance().levelUpdate(paramActivity,gameInfo);
		}
	}
	public static void onResume(Activity paramActivity) {
		YYReleaseSDK.getInstance().onSdkResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		YYReleaseSDK.getInstance().onSdkPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		YYReleaseSDK.getInstance().onSdkDestory(paramActivity);
	}
	
	@SuppressWarnings("deprecation")
	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		YYReleaseSDK.getInstance().onActivityResult(paramInt1, paramInt2, paramIntent);
	}

	@SuppressWarnings("deprecation")
	public static void onNewIntent(Intent paramIntent) {
		YYReleaseSDK.getInstance().onNewIntent(paramIntent);
	}

	public static void onStart(Activity mActivity2) {
		
	}

	public static void onRestart(Activity paramActivity) {
		YYReleaseSDK.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		YYReleaseSDK.getInstance().onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		YYReleaseSDK.getInstance().onSdkStop(paramActivity);
	}

	public static void onBackPressed() {
	    // 提示，这个api很重要，要实现
	    YYReleaseSDK.getInstance().onBackPressed();
	}


	public static void attachBaseContext(Context newBase) {
	    // 提示，这个api很重要，要实现
	    YYReleaseSDK.getInstance().attachBaseContext(mActivity);
	}

	public static void onConfigurationChanged() {
	    // 提示，这个api很重要，要实现
	    YYReleaseSDK.getInstance().onConfigurationChanged();
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
