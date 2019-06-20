package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yuecheng.sdk.Manager;
import com.yuecheng.sdk.bean.User;
import com.yuecheng.sdk.listener.ExitGame;
import com.yuecheng.sdk.listener.LoginListener;
import com.yuecheng.sdk.listener.PayListener;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	
	private static User user;
	
	private static String changetime="";
	private static String serverNamea="";
	private static String serverIda="";
	private static String roleIda="";
	private static String roleNamea="";
	private static String vipa="0";
	private static String levela="";
	private static String partyNamea="不存在";
	private static String roleCTimea="";
	private static int tuichu=0;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Manager.init(mactivity);
//		Manager.splash(mactivity,true);

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
        Manager.login(mactivity, new LoginListener() {
			
			public void onLoginSucess(User arg0) {
				// TODO Auto-generated method stub
				Yayalog.loger("成功");
				user=arg0;
				String token= user. getSessionid();
				String uid=""+user.getId();
				tuichu=1;
				loginSuce(mActivity, uid, uid, token);
			}
			
			public void onLoginCancelled() {
				Yayalog.loger("失败");
				loginFail();

			}

			@Override
			public void onLoginFailed() {
				// TODO Auto-generated method stub
				Yayalog.loger("失败");
				loginFail();
				
			}
		});
		
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		Manager.pay(mactivity,morderid,serverIda,serverNamea,roleIda,roleNamea,levela,"不存在","0",YYWMain.mOrder.goods,YYWMain.mOrder.goods,10,String.valueOf(YYWMain.mOrder.money/100),morderid,new PayListener() {
			
			@Override
			public void onPaySuccess() {
				// TODO Auto-generated method stub
				paySuce();
			}
			
			@Override
			public void onPayCancel() {
				// TODO Auto-generated method stub
				payFail();
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
		if(tuichu==1){
			Log.i("tag","tuichu==1");
			Manager.exitGame (paramActivity,serverIda,serverNamea,roleIda,roleNamea,levela,"不存在","0","0",Manager.EXITGAME,roleCTimea,changetime);
		}
		
        Manager.exitGame(paramActivity, new ExitGame() {
			
			@Override
			public void exitGame() {
				Log.i("tag","tuichu==0");
				callback.onExit();
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
		serverIda=zoneId;
		serverNamea=zoneName;
		roleIda=roleId;
		roleNamea=roleName;
		levela=roleLevel;
		roleCTimea=roleCTime;
		
		changetime = ""+System.currentTimeMillis()/1000;
		if (Integer.parseInt(ext) == 1){
			Manager.enterGame (paramActivity,zoneId,zoneName,roleId,roleName,roleLevel,"不存在","0","0",Manager.ENTERGAME,roleCTime,changetime);
			
		}else if (Integer.parseInt(ext) == 2){
			Manager.createRole(paramActivity,zoneId,zoneName,roleId,roleName,roleLevel,"不存在","0","0",Manager.CREATEROLE,roleCTime,changetime);
			
		}else if (Integer.parseInt(ext) == 3){
			Manager.levelChange (paramActivity,zoneId,zoneName,roleId,roleName,roleLevel,"不存在","0","0",Manager.LEVELCHANGE,roleCTime,changetime);
			
		}
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		Manager.onResume(paramActivity);

	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		Manager.onPause();

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
