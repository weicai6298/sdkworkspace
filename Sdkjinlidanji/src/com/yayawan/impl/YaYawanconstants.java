package com.yayawan.impl;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gionee.game.offlinesdk.AppInfo;
import com.gionee.game.offlinesdk.GamePlatform;
import com.gionee.game.offlinesdk.InitPluginCallback;
import com.gionee.game.offlinesdk.MessagePayCallback;
import com.gionee.game.offlinesdk.OrderInfo;
import com.gionee.game.offlinesdk.PayCallback;
import com.gionee.game.offlinesdk.QuitGameCallback;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;
	private static Application application;

	private static boolean isinit=false;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		
		InitPluginCallback initPluginCallback = new InitPluginCallback() {
		    @Override
		    public void onEvent(int event) {
		        if (InitPluginCallback. EVENT_INIT_PLUGIN_FINISH == event) {
		            // 插件初始化完成，请游戏根据实际情况处理
		        	Yayalog.loger("初始化插件完成");
		        }
		    }};
	}
	
	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		application = (Application)applicationContext;
		AppInfo appInfo = new AppInfo();
		String apiKey= ""+DeviceUtil.getGameInfo(application, "jinliapiKey");
		String privateKey= ""+DeviceUtil.getGameInfo(application, "jinliprivateKey");
		appInfo.setApiKey(apiKey);        // apiKey由开发者后台申请得到
		appInfo.setPrivateKey(privateKey);  //privateKey由开发者后台申请得到
		appInfo.setPayMode(AppInfo.PayMode.NO_ACCOUNT_BY_TRADE_DATA); //设置支付模式为“指定支付方式支付“
		GamePlatform.init(application, appInfo);
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		String tempuid = Sputils.getSPstring("uid", "tem", mactivity);
		Log.i("tag","tempuid="+tempuid);
		if (tempuid.equals("tem")) {
			String uidtemp=System.currentTimeMillis()+"kk";
			String uid=uidtemp.substring(4, uidtemp.length())+new Random().nextInt(10);
			Sputils.putSPstring("uid", uid, mactivity);
			loginSuce(mactivity, uid, uid, uid);
		}else {
			loginSuce(mactivity, tempuid, tempuid, tempuid);
		}
		
		
		
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		Log.i("tag", "发起支付");
		OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCpOrderNum(morderid);
        
      //商品标题
        orderInfo.setSubject(YYWMain.mOrder.goods);
      //商品总价格
      orderInfo.setTotalFee(YYWMain.mOrder.money/100+"");
      //商品实际价格
      orderInfo.setDealPrice(YYWMain.mOrder.money/100+"");
        
        Log.i("tag", "orderInfo="+orderInfo);
		GamePlatform.getInstance().pay(mactivity, orderInfo, new PayCallback() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Log.i("tag", "支付成功");
				paySuce();
				Log.i("tag", "支付成功1");
			}
			
			@Override
			public void onFail(String arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("tag", "支付失败1");
				payFail();
				Log.i("tag", "支付失败1");
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

		GamePlatform.getInstance().quitGame(paramActivity, new QuitGameCallback() {
			
			@Override
			public void onQuit() {
				// TODO Auto-generated method stub
				mActivity.finish();
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
		//

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
		Log.i("tag", "下发道具");
		if (YYWMain.mPayCallBack != null) {
			Log.i("tag", "下发道具1");
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
