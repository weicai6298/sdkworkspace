package com.yayawan.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cgamex.usdk.api.CGamexSDK;
import com.cgamex.usdk.api.GameInfo;
import com.cgamex.usdk.api.IEventHandler;
import com.cgamex.usdk.api.IExitGameListener;
import com.cgamex.usdk.api.PayParams;
import com.cgamex.usdk.api.SDKConfig;
import com.cgamex.usdk.api.UserInfo;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static int type = 1;
	
	public static String role_id;

	public static String role_name;

	public static String role_level;

	public static String zone_id;

	public static String zone_name;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Long kf_appid = Long.parseLong(DeviceUtil.getGameInfo(mactivity, "xy_appid"));
		String kf_appkey = DeviceUtil.getGameInfo(mactivity, "xy_appkey");
		Log.i("tag","kf_appid = " + kf_appid);
		Log.i("tag","kf_appkey = " + kf_appkey);
		SDKConfig sdkConfig = new SDKConfig();
        // 必填参数，该参数请找运营申请
        sdkConfig.setAppId(kf_appid);
        // 必填参数，该参数请找运营申请
        sdkConfig.setAppKey(kf_appkey);
        // 设置横竖屏，横屏：SDKConfig.LANDSCAPE，竖屏SDKConfig.PORTRAIT
        Boolean isLandscape = DeviceUtil.isLandscape(mActivity)?true:false;
        if(isLandscape){
        	type = 0;
        }else{
        	type = 1;
        }
        sdkConfig.setOrientation(type);
        
        // SDK初始化(如果你的游戏是H5游戏，并且采用H5接入方式时，只需传入mIEventHandler，不需要对handleEvent回调事件进行处理)
        boolean init = CGamexSDK.init(sdkConfig, mIEventHandler);

        Log.i("tag", "sdk init=" + init);
		isinit = true ;
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
	}

	// sdk事件回调接口，如果你的游戏是原生的（非H5游戏），需要处理该回调，H5游戏则不需要
    private static IEventHandler mIEventHandler = new IEventHandler() {
        @Override
        public void handleEvent(int eventCode, Bundle extra) {
            switch (eventCode) {
                case IEventHandler.EVENT_LOGIN_SUCCESS:
                    // 登录成功回调
                    UserInfo user = (UserInfo) extra.getSerializable(IEventHandler.KEY_USER);
                    // 用户id，可作为用户唯一标识，注意：不能用username作为唯一标识，它有可能会被修改
                    String userId = user.getUserId();
                    // token，sdk登录后，CP可以用它来进行身份二次验证，token验证接口查看服务端对接文档
                    String token = user.getToken();
                    loginSuce(mActivity, userId, userId, token);
                    Log.i("tag", "登录成功. userid=" + userId + ", token=" + token);
                    break;
                case IEventHandler.EVENT_LOGIN_FAILED:
                	loginFail();
                	Toast("登录失败");
                	login(mActivity);
                	break;
                case IEventHandler.EVENT_PAY_SUCCESS:
                    // 支付成功回调
                    // 充值成功后，强烈建议使用游戏服务端及时主动通知游戏客户端的方案刷新到账信息，而非依赖SDK回调再拉取刷新
                    String orderid = extra.getString(IEventHandler.KEY_OUT_ORDER_ID);
                    paySuce();
                    Log.i("tag", "支付成功，orderid=" + orderid);
                    break;
                case IEventHandler.EVENT_PAY_FAILED:
                    // 支付失败回调
                    String errorMsg = extra.getString(IEventHandler.KEY_MSG);
                    payFail();
                    Log.i("tag", "支付失败，errorMsg=" + errorMsg);
                    break;
                case IEventHandler.EVENT_PAY_CANCEL:
                    // 支付取消回调
                	payFail();
                    Log.i("tag", "支付取消");
                    break;
                case IEventHandler.EVENT_ACCOUNT_LOGOUT:
                    // 注销账号回调，可以在这里跳到游戏登录界面，并重新调用sdk的login接口，让用户重新登录
                    Log.i("tag", "注销账号");
                    // 跳到游戏登录界面，重新调用sdk的login接口
                    mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							loginOut();
						}
					});
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
			CGamexSDK.login(mactivity);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		PayParams payParams = new PayParams();

        // 价格，单位：元
        payParams.setPrice(Integer.parseInt(YYWMain.mOrder.money/100+""));
        payParams.setOrderId(morderid);
        payParams.setServerId(zone_id);
        payParams.setServerName(zone_name);
        // 角色ID，不能为空
        payParams.setRoleId(role_id);
        payParams.setRoleName(role_name);
        payParams.setRoleLevel(role_level);
        payParams.setExt1("ext1");
        payParams.setExt2("ext2");

        CGamexSDK.pay(mactivity, payParams);
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		GameInfo gameInfo = new GameInfo();

        gameInfo.setRoleId(role_id);
        gameInfo.setRoldName(role_name);
        gameInfo.setRoleLevel(role_level);
        gameInfo.setServerId(zone_id);
        gameInfo.setServerName(zone_name);

        CGamexSDK.exit(paramActivity, gameInfo, new IExitGameListener() {
            @Override
            public void onSdkExit() {
                // 即将退出游戏，如有必要，调用方可以在这里保存游戏数据
            	callback.onExit();
            }
        });
	}

	/** 
	 * 设置角色信息
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		role_id = roleId;
		role_name = roleName;
		role_level = roleLevel;
		zone_id = zoneId;
		zone_name = zoneName;
		//1为角色登陆成功  2为角色创建  3为角色升级
			GameInfo gameInfo = new GameInfo();
            gameInfo.setRoleId(role_id);
            gameInfo.setRoldName(role_name);
            gameInfo.setRoleLevel(role_level);
            gameInfo.setServerId(zone_id);
            gameInfo.setServerName(zone_name);
            CGamexSDK.submitGameInfo(gameInfo);
	}

	public static void onResume(Activity paramActivity) {
		CGamexSDK.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		CGamexSDK.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		CGamexSDK.onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {
		CGamexSDK.onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		CGamexSDK.onNewIntent(mActivity, paramIntent);
	}

	public static void onStart(Activity paramActivity) {
		CGamexSDK.onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		CGamexSDK.onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		CGamexSDK.onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		CGamexSDK.onStop(paramActivity);
	}
	
	public void onBackPressed() {
        CGamexSDK.onBackPressed(mActivity);
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
	public static void loginSuce(Activity mactivity, String uid,String username, String session) {

		YYWMain.mUser = new YYWUser();

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ uid + "";
		}

//		 YYWMain.mUser.nick = data.getNickName();
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

	/**
	 * 支付成功
	 * 
	 */
	public static void paySuce() {
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	/**
	 * 支付失败
	 * 
	 */
	public static void payFail() {
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

}
