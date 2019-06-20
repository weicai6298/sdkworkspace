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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.app.yjy.game.OneNineGame;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.onenine.game.lib.callback.OneNineGameCallback;
import com.onenine.game.lib.callback.OneNineGameExitCallback;
import com.onenine.game.lib.callback.OneNineGameLogoutListener;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static String uid;
	
	private static String gamename;
	public static int tuichu = 0;
	
	private static String roleid = "123";
	private static String rolename  = "123";
	private static String rolelevel = "123";
	private static String zoneid = "123";
	private static String zonename = "123";
	private static String rolectime = "";
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//		rolectime = System.currentTimeMillis()/1000+"";
		OneNineGame.splash(mactivity,DeviceUtil.isLandscape(mactivity)?true:false);
		gamename = DeviceUtil.getGameInfo(mActivity, "gamename");
		OneNineGame.onCreate(mactivity, new OneNineGameLogoutListener() {
			
			@Override
			public void onLogoutSuccess() {
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loginOut();
					}
				});
			}
		});
		isinit = true ;
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
			OneNineGame.login(mactivity, new OneNineGameCallback() {
				@Override
				public void onOneNineGameSucess(String msg) {
				//获取uid和sessionid后向我方服务器做登录校验用户合法性
					try {
						JSONObject obj = new JSONObject(msg);
						int result = obj.optInt("result");
						if(1==result){
						uid=obj.optString("uid");
	String sessionid = obj.optString("session_id");
	Log.i("tag","uid = "+uid);
	Log.i("tag","sessionid = "+sessionid);
	tuichu=1;
	loginSuce(mactivity, uid, uid, sessionid);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onOneNineGameFailure() {
					loginFail();
				}
				@Override
				public void onOneNineGameCancel() {
					loginFail();
				}
			});

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
		OneNineGame.pay(mactivity, uid, morderid, Integer.parseInt(YYWMain.mOrder.money+""), 
				"无", "无", YYWMain.mOrder.goods, DeviceUtil.getGameInfo(mActivity, "goodsname"), 
				"无", Integer.parseInt(rolelevel), "无", roleid, rolename, zoneid,
				zonename, "无", "无", DeviceUtil.getGameInfo(mActivity, "gamename"),new OneNineGameCallback() {
					
					@Override
					public void onOneNineGameSucess(String arg0) {
						paySuce();
						Toast("支付成功");
					}
					
					@Override
					public void onOneNineGameFailure() {
						payFail();
						Toast("支付失败");
					}
					
					@Override
					public void onOneNineGameCancel() {
						payFail();
						Toast("支付取消");
					}
				});
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
		OneNineGame.exitGame(paramActivity, new OneNineGameExitCallback() {
			
			@Override
			public void exitGame() {
				if(tuichu == 1){
					Log.i("tag","19退出数据上报");
					OneNineGame.exitGame(paramActivity,uid,zoneid,zonename,roleid,
							rolename,rolelevel,"无",gamename,
							"无","无","无",OneNineGame.EXITGAME,
							Long.parseLong(rolectime), 
							Long.parseLong(System.currentTimeMillis()/1000+""));
				}
				
				
				callback.onExit();
			}
		});

	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		roleid = roleId;
		rolename =roleName;
		rolelevel =roleLevel;
		zoneid = zoneId;
		zonename = zoneName;
		rolectime = roleCTime;
		//角色创建时间
		//1为角色登陆成功  2为角色创建  3为角色升级
		if (Integer.parseInt(ext) == 1){
			OneNineGame.enterGame(paramActivity,uid,zoneId,zoneName,roleId,roleName,roleLevel,"无",gamename,"无","无",
					"无",OneNineGame.ENTERGAME,Long.parseLong(roleCTime),Long.parseLong(System.currentTimeMillis()/1000+""));
		}else if(Integer.parseInt(ext) == 2){
			OneNineGame.createRole(paramActivity, uid, zoneId, zoneName, roleId, roleName, roleLevel, "无", 
					gamename, "无", "无", "无", 
					OneNineGame.CREATEROLE, Long.parseLong(roleCTime), Long.parseLong(System.currentTimeMillis()/1000+""));
		}else if(Integer.parseInt(ext) == 3){
			OneNineGame.levelChange(paramActivity,uid,zoneId,zoneName,roleId,roleName,roleLevel,"无",gamename,"无","无",
					"无",OneNineGame.LEVELCHANGE,Long.parseLong(roleCTime), Long.parseLong(System.currentTimeMillis()/1000+""));
		}
	}

	public static void onResume(Activity paramActivity) {
		OneNineGame.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		OneNineGame.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		OneNineGame.onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		OneNineGame.onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);

	}

	public static void onNewIntent(Intent paramIntent) {
		OneNineGame.onNewIntent(paramIntent,mActivity);
	}

	public static void onStart(Activity paramActivity) {
		OneNineGame.onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		OneNineGame.onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		OneNineGame.onStop(paramActivity);
	}

	 public static void onBackPressed() {
	    	OneNineGame.onBackPressed(mActivity);
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
