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
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.game91.framework.NineGame;
import com.game91.framework.callback.Callback;
import com.game91.framework.callback.OrderInfo;
import com.game91.framework.callback.PayInfo;
import com.game91.framework.callback.RoleInfo;
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

	public static String role_id = "123";
	public static String role_name = "123";
	public static String role_level = "123";
	public static String zone_id = "123";
	public static String zone_name = "123";

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		//sdk初始化
		//调用sdk初始化
		NineGame.getInstance().doInit(
				mactivity,
				new Callback<Integer>() {
					@Override
					public void call(Integer integer) {
						Log.i("tag","初始化成功");
						isinit = true ;
					}
				},
				new Callback<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						Log.i("tag","初始化失败 "+throwable.getMessage());
					}
				}
				);
		//添加登出回调
		NineGame.getInstance().setLogoutCallback(new Callback<Integer>() {
			@Override
			public void call(Integer integer) {
				mactivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loginOut();
						Log.i("tag","登出成功");
					}
				});
			}
		});
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
			NineGame.getInstance().doLogin(mactivity, new Callback<Integer>() {
				@Override
				public void call(Integer integer) {
					//	                Toast.makeText(
					//	                        getApplication(),
					//	                        "登录成功:\nuid=" +NineGame.getInstance().getUserInfo().getUserId() +
					//	                                "\ntoken:" + NineGame.getInstance().getUserInfo().getToken(),Toast.LENGTH_SHORT).show();
					//	                showRole();
					String uid = NineGame.getInstance().getUserInfo().getUserId();
					String token = NineGame.getInstance().getUserInfo().getToken();
					Log.i("tag","uid = " +uid);
					Log.i("tag","token = " +token);
					loginSuce(mactivity, uid, uid, token);
					Toast("登录成功");
				}
			}, new Callback<Throwable>() {
				@Override
				public void call(Throwable throwable) {
					//	                Toast.makeText(getApplication(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
					loginFail();
					Log.i("tag","登录失败="+throwable.getMessage());
					Toast("登录失败");
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
		
		PayInfo payInfo = new PayInfo()
		.setOrderId(morderid)
		.setProductId("s_"+YYWMain.mOrder.goods)
		.setProductName(YYWMain.mOrder.goods)
		.setProductDesc(YYWMain.mOrder.goods)
		.setPrice(YYWMain.mOrder.money/100+"")
		.setBuyNum(1)
		.setCoinNum("0")
		.setServerID(zone_id)
		.setServerName(zone_name)
		.setRoleID(role_id)
		.setRoleName(role_name)
		.setRoleLevel(role_level)
		.setVip("")
		.setNotifyUrl(DeviceUtil.getGameInfo(mActivity, "callback"))
		.setExtension("");
		//支付
		NineGame.getInstance().doPay(mactivity, payInfo, new Callback<OrderInfo>() {
			@Override
			public void call(OrderInfo orderInfo) {
				paySuce();
				Log.i("tag","订单号:"+orderInfo.getOrderId());
				Toast("支付成功");
			}
		}, new Callback<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				//        Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
				payFail();
				Log.i("tag","支付错误信息:"+throwable.getMessage());
				Toast("支付失败");
			}
		});
		
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
paramActivity.runOnUiThread(new Runnable() {
	
	@Override
	public void run() {
		NineGame.getInstance().doExit(paramActivity, new Callback<Integer>() {
			@Override
			public void call(Integer integer) {
				callback.onExit();
			}
		}, new Callback<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				Toast("继续游戏");
			}
		});
		}
	});
	}

	/** 
	 * 设置角色信息
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		role_id = roleId;
		role_name = roleName;
		role_level = roleLevel;
		zone_id = zoneId;
		zone_name = zoneName;

		//		1为角色登陆成功  2为角色创建  3为角色升级
		if (Integer.parseInt(ext) == 1){
			RoleInfo roleInfo = new RoleInfo()
			.setDataType(RoleInfo.SUBMIT_EXTRA_ENTER_GAME)//提交类型 
			.setCpUid(NineGame.getInstance().getUserInfo().getUserId())//游戏方id
			.setRoleID(roleId)//角色id
			.setRoleName(roleName)//角色名称
			.setRoleLevel(roleLevel)//角色等级
			.setVip("0")//vip等级
			.setUnion("暂无")//工会名
			.setServerID(zoneId)//服务器id
			.setServerName(zoneName)//服务器名称
			.setCreateRoleTime(Long.parseLong(roleCTime+"000"));//角色创建时间
			NineGame.getInstance().submitRoleInfo(roleInfo);
			Log.i("tag","登陆 - 上报成功");
		}else if(Integer.parseInt(ext) == 2){
			RoleInfo roleInfo = new RoleInfo()
			.setDataType(RoleInfo.SUBMIT_EXTRA_CREATE_ROLE)//提交类型 
			.setCpUid(NineGame.getInstance().getUserInfo().getUserId())//游戏方id
			.setRoleID(roleId)//角色id
			.setRoleName(roleName)//角色名称
			.setRoleLevel(roleLevel)//角色等级
			.setVip("0")//vip等级
			.setUnion("暂无")//工会名
			.setServerID(zoneId)//服务器id
			.setServerName(zoneName)//服务器名称
			.setCreateRoleTime(Long.parseLong(roleCTime+"000"));//角色创建时间
			NineGame.getInstance().submitRoleInfo(roleInfo);
			Log.i("tag","创建 - 上报成功");
		}else if(Integer.parseInt(ext) == 3){
			RoleInfo roleInfo = new RoleInfo()
			.setDataType(RoleInfo.SUBMIT_EXTRA_LEVEL_UP)//提交类型 
			.setCpUid(NineGame.getInstance().getUserInfo().getUserId())//游戏方id
			.setRoleID(roleId)//角色id
			.setRoleName(roleName)//角色名称
			.setRoleLevel(roleLevel)//角色等级
			.setVip("0")//vip等级
			.setUnion("暂无")//工会名
			.setServerID(zoneId)//服务器id
			.setServerName(zoneName)//服务器名称
			.setCreateRoleTime(Long.parseLong(roleCTime+"000"));//角色创建时间
			NineGame.getInstance().submitRoleInfo(roleInfo);
			Log.i("tag","升级 - 上报成功");
		}
	}

	public static void onResume(Activity paramActivity) {
		NineGame.getInstance().onStart(paramActivity);
		NineGame.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		NineGame.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		NineGame.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {
		NineGame.getInstance().onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		NineGame.getInstance().onNewIntent(mActivity, paramIntent);
	}

	public static void onStart(Activity paramActivity) {
	}

	public static void onRestart(Activity paramActivity) {
		NineGame.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		NineGame.getInstance().onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		NineGame.getInstance().onStop(paramActivity);
	}

	public void onConfigurationChanged(Configuration newConfig) {
		//生命周期
		NineGame.getInstance().onConfigurationChanged(mActivity, newConfig);
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
