package com.yayawan.impl;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import android.util.Log;
import android.widget.Toast;

import com.ddgame.callback.YYWAnimCallBack;
import com.ddgame.callback.YYWPayCallBack;
import com.ddgame.callback.YYWUserCallBack;
import com.ddgame.domain.YYWOrder;
import com.ddgame.main.Kuwangame;
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
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Kuwangame.getInstance().initSdk(mActivity);
		shanping(mactivity);
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
			Kuwangame.getInstance().login(mactivity, new YYWUserCallBack() {

				@Override
				public void onLogout(Object arg0) {
//					Toast.makeText(GameActivity.this, "账号退出，回到登陆页面",
//							Toast.LENGTH_SHORT).show();
                       mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							loginOut();
						}
					});
				}

				@Override
				public void onLoginFailed(String arg0, Object arg1) {
					// TODO Auto-generated method stub
					System.out.println("失败");
//					Toast.makeText(GameActivity.this, "失败", Toast.LENGTH_SHORT)
//							.show();
					loginFail();
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					System.out.println("取消");
//					Toast.makeText(GameActivity.this, "取消", Toast.LENGTH_SHORT)
//							.show();
					loginFail();
				}

				@Override
				public void onLoginSuccess(com.ddgame.domain.YYWUser user,
						Object arg1) {
					// TODO Auto-generated method stub
					System.out.println("登录成功" + user);
					String uid=user.uid;
				String username=user.userName;
				String token=user.token;   
//				Toast.makeText(GameActivity.this, "登录成功" + user,
//						Toast.LENGTH_SHORT).show();
				loginSuce(mActivity, uid, username, token);
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
		YYWOrder order = new YYWOrder(morderid, YYWMain.mOrder.goods, YYWMain.mOrder.money,
				"");


		Kuwangame.getInstance().pay(mactivity, order, new YYWPayCallBack() {
			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付失败");
				payFail();
			}

			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付退出");
				payFail();
			}

			@Override
			public void onPaySuccess(com.ddgame.domain.YYWUser arg0,
					YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				paySuce();
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

		Kuwangame.getInstance().exit(paramActivity, new com.ddgame.callback.YYWExitCallback() {
			
			@Override
			public void onExit() {
				// TODO Auto-generated method stub
				paramActivity.finish();
			}
		});
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		//填入 1为角色登陆成功  2为角色创建  3为角色升级
		if (Integer.parseInt(ext) == 1){
			Kuwangame.getInstance().setData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime,"1"); 
		}else if (Integer.parseInt(ext) == 2){
			Kuwangame.getInstance().setData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime,"2"); 
		}else if (Integer.parseInt(ext) == 3){
			Kuwangame.getInstance().setData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime,"3"); 
		}
	}

	public static void onResume(Activity paramActivity) {
		 Kuwangame.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		Kuwangame.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		Kuwangame.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		 Kuwangame.getInstance().onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		Kuwangame.getInstance().onNewIntent(paramIntent);
	}

	public static void onStart(Activity paramActivity) {

	}

	public static void onRestart(Activity paramActivity) {
		Kuwangame.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		Kuwangame.getInstance().onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		Kuwangame.getInstance().onStop(paramActivity);
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
	protected static void shanping(Activity paramActivity) {
		// TODO Auto-generated method stub
		Kuwangame.getInstance().anim(paramActivity, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
//				Toast.makeText(GameActivity.this, "播放动画回调", Toast.LENGTH_SHORT)
//						.show();

			}

			@Override
			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

}
