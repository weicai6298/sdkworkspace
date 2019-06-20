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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.weibo.game.eversdk.core.EverSDK;
import com.weibo.game.eversdk.core.EverUser;
import com.weibo.game.eversdk.core.GameProduct;
import com.weibo.game.eversdk.core.GameUser;
import com.weibo.game.eversdk.http.InitRequest.InitParameter;
import com.weibo.game.eversdk.interfaces.listener.IEverPaymentListener;
import com.weibo.game.eversdk.interfaces.listener.IEverSystemListener;
import com.weibo.game.eversdk.interfaces.listener.IEverUserListener;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static YYWExitCallback ExitCallback;
	private static String uid;
	
	 //用户相关的监听器
    private static IEverUserListener userListener = new IEverUserListener() {
        @Override
        public void onLogout(Object data) {
            //TODO 与uinity层进行通讯
            //UnityPlayer.UnitySendMessage("NativeManager", "OnLogout", data.toString());
//            Toast.makeText(mActivity, "通知unity执行logout", 1).show();
        	mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					loginOut();
				}
			});
        }
        /**
         * 此处的user对象为新浪融合SDKuser对象
         * */
        @Override
        public void onLoginSuccess(EverUser user, Object pt) {
            JSONObject jobj = null;
//            try {
//                jobj = new JSONObject();
//                //userId - 用户id
//                jobj.put("userID", user.getUID());
//                jobj.put("channel", user.getChannel());
//                jobj.put("token", user.getToken());
//                jobj.put("userName", user.getUserName());
//                jobj.put("pt", pt.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (jobj != null) {
              //TODO 与uinity层进行通讯
             //  UnityPlayer.UnitySendMessage("NativeManager", "LoginSuc", jobj.toString());
//                Toast.makeText(mActivity, "通知unity执行loginSuccess", 1).show();
            Log.i("tag"," uid =" + user.getUID());
            Log.i("tag"," username =" + user.getUserName());
            Log.i("tag"," token =" + user.getToken());
            loginSuce(mActivity, user.getUID(), user.getUserName(), user.getToken());
//            }
        }

        @Override
        public void onLoginFailed(String error, Object pt) {
            //TODO 与unity层进行通讯
           //UnityPlayer.UnitySendMessage("NativeManager", "LoginFailed", error + "|" + pt.toString());
//            Toast.makeText(mActivity, "通知unity执行loginFail", 1).show();
        	loginFail();
        	Log.i("tag","LoginFailed - error= "+ error);
        	Log.i("tag","LoginFailed - Object= "+ pt.toString());
        	Toast("登录失败");
        }

        @Override
        public void onLoginCancel(Object pt) {
          //TODO 与unity层进行通讯
          // UnityPlayer.UnitySendMessage("NativeManager", "LoginCancel", pt.toString());
//            Toast.makeText(mActivity, "通知unity执行loginCancel", 1).show();
        	Toast("登录取消");
        	Log.i("tag","LoginCancel = "+ pt.toString());
        }
    };
    
    //系统相关的监听器
    private static IEverSystemListener systemListener = new IEverSystemListener() {

        @Override
        public void onInitSuccess() {
        	Log.i("tag","初始化成功");
        	isinit=true;
        }

        @Override
        public void onInitFailed(String reason) {
        	Log.i("tag","初始化失败");
        }

        @Override
        public void onExitSuccess() {
          //TODO 与unity层进行通讯
          //UnityPlayer.UnitySendMessage("NativeManager", "ExitSuccess", "");
//            Toast.makeText(mActivity, "通知unity执行ExitSuccess", 1).show();
//            System.exit(0);
        	ExitCallback.onExit();
        }

        @Override
        public void onExitFailed(String reason) {
          //TODO 与unity层进行通讯
          //UnityPlayer.UnitySendMessage("NativeManager", "ExitFailed", reason);
//            Toast.makeText(mActivity, "通知unity执行ExitFail", 1).show();
        	Toast("继续游戏");
        }
    };
    
    //支付相关的监听器
    private static IEverPaymentListener payListener = new IEverPaymentListener() {

        @Override
        public void onPaySuccess(String orderId) {
          //TODO 与unity层进行通讯
            //UnityPlayer.UnitySendMessage("NativeManager", "PaySuccess", orderId);
//            Toast.makeText(mActivity, "通知unity执行PaySuccess", 1).show();
        	paySuce();
        	Toast("支付成功");
        }

        @Override
        public void onPayFailed(String orderId, String error) {
          //TODO 与unity层进行通讯
//            UnityPlayer.UnitySendMessage("NativeManager", "PayFailed", orderId + "|" + error);
//            Toast.makeText(mActivity, "通知unity执行PayFailed", 1).show();
        	payFail();
        	Toast("支付失败");
        	Log.i("tag","PayFailed"+orderId + "|" + error);
        }

        @Override
        public void onPayCancel(String orderId) {
          //TODO 与unity层进行通讯
//            UnityPlayer.UnitySendMessage("NativeManager", "PayCancel", orderId);
//            Toast.makeText(mActivity, "通知unity执行PayCancel", 1).show();
        	payFail();
        	Toast("支付取消");
        	Log.i("tag","PayCancel = "+orderId);
        }
    };

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Init(mactivity);
	}

	private static void Init(Activity mactivity) {
		 //初始化的时候设置相关模块的监听器
	     EverSDK.getInstance().setSystemListener(systemListener);
	     EverSDK.getInstance().setUserListener(userListener);
	     EverSDK.getInstance().setPayListener(payListener);
	     //调用初始化
	     EverSDK.getInstance().initSDK(mactivity);
	     //请在初始化SDK之后务必加上onCreate这段代码
	     EverSDK.getInstance().onCreate(mactivity);  
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
		if(isinit){
			EverSDK.getInstance().login(mactivity, "");
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
		if (isLogin()) {
			GameUser gameUser = new GameUser();
			 GameProduct gameProduct = new GameProduct();
			  gameProduct.setOrderId(morderid);
			  gameProduct.setAmount(YYWMain.mOrder.money);
			  gameProduct.setProductDesc(YYWMain.mOrder.goods);
			  gameProduct.setProductId(YYWMain.mOrder.goods_id);
			  gameProduct.setProductName(YYWMain.mOrder.goods);
			  gameProduct.setPt("");
			 //GameUser信息与GameProduct信息填充完成，且账户已经是登录状态，调用支付
			  EverSDK.getInstance().pay(mactivity, gameUser, gameProduct);
		}else{
			login(mactivity);
		}
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
				ExitCallback = callback;
				EverSDK.getInstance().exit(paramActivity);
			}
		});
	}
	// 是否登录过
    public static boolean isLogin() {
        return EverSDK.getInstance().isLogin(mActivity);
    }

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		if(roleName.equals("")){
			roleName="001";
		}
		if (Integer.parseInt(ext) == 1){
		 GameUser user = new GameUser();
	        JSONObject jobj = null;
	        jobj = new JSONObject();
			user.setUid(jobj.optString(uid));
			user.setName(jobj.optString(roleName));
			user.setLevel(jobj.optString(roleLevel));
			user.setVip(jobj.optString("vip"));
			user.setBalance(jobj.optString("balance"));
			user.setServerName(jobj.optString(zoneName));
			user.setPartyName(jobj.optString("partyName"));
	        //上传游戏内信息 
	        EverSDK.getInstance().reportGameUser(paramActivity, user);
		}
	}

	public static void onResume(Activity paramActivity) {
		EverSDK.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		 EverSDK.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		EverSDK.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		EverSDK.getInstance().onActivityResult(paramActivity,paramInt1,paramInt2,paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		 EverSDK.getInstance().onNewIntent(paramIntent);
	}

	public static void onStart(Activity paramActivity) {

	}

	public static void onRestart(Activity paramActivity) {
		EverSDK.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		EverSDK.getInstance().onStop(paramActivity);
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
