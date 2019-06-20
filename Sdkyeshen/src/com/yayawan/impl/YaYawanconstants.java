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
import android.util.Log;
import android.widget.Toast;

import com.bignox.sdk.NoxSDKPlatform;
import com.bignox.sdk.NoxStatus;
import com.bignox.sdk.export.entity.KSAppEntity;
import com.bignox.sdk.export.entity.KSConsumeEntity;
import com.bignox.sdk.export.entity.KSUserEntity;
import com.bignox.sdk.export.entity.KSUserRoleEntity;
import com.bignox.sdk.export.listener.NoxEvent;
import com.bignox.sdk.export.listener.OnConsumeListener;
import com.bignox.sdk.export.listener.OnCreateRoleListener;
import com.bignox.sdk.export.listener.OnEntryListener;
import com.bignox.sdk.export.listener.OnExitListener;
import com.bignox.sdk.export.listener.OnInitListener;
import com.bignox.sdk.export.listener.OnLoginListener;
import com.bignox.sdk.export.listener.OnLogoutListener;
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

	private static KSUserRoleEntity userRoleEntity;
	
	private static String role_id = "123";
	private static String role_name = "123";
	private static String role_level = "123";
	private static String zone_id = "123";
	private static String zone_name = "123";
	private static long role_ctime = 123;
	
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		intsdk(mactivity);
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
           sdklogin(mactivity);
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
		KSConsumeEntity ksConsumeEntity=new KSConsumeEntity();
		ksConsumeEntity.setGoodsTitle(YYWMain.mOrder.goods); //必填项-商品名称
		ksConsumeEntity.setGoodsOrderId(morderid); //非必填项-商户订单id
		ksConsumeEntity.setGoodsDesc(YYWMain.mOrder.goods);//非必填项-商品描述
		ksConsumeEntity.setPrivateInfo(""); //非必填项-商户私有信息。
		ksConsumeEntity.setOrderCoin(YYWMain.mOrder.money); //必填项-订单金额（大于0）单位为分
		ksConsumeEntity.setNotifyUrl(""); //非必填项-设置游戏服务器的后端回调通知地址
		
		 KSUserRoleEntity userRoleEntity = new KSUserRoleEntity();
			userRoleEntity.setRoleId(role_id);
			userRoleEntity.setRoleName(role_name);
			userRoleEntity.setRoleGrade(role_level);
			userRoleEntity.setRoleCreateTime(role_ctime);
			
			//如果游戏只有一个区服信息，设置serverId与serverName即可，如果区和服分别有信息，则分别设置server和zone相关信息
			userRoleEntity.setServerId(zone_id);
			userRoleEntity.setServerName(zone_name);
//			userRoleEntity.setZoneId("区域ID");
//			userRoleEntity.setZoneName("区域名称");
			
			userRoleEntity.setVip("10");
			NoxSDKPlatform.getInstance().noxConsume(ksConsumeEntity,userRoleEntity, new OnConsumeListener() {
                @Override
                public void finish(NoxEvent<KSConsumeEntity> noxEvent) {
                    //消费结束之后回调客户端
                    if (noxEvent.getStatus() == NoxStatus.STATE_CONSUME_SUCCESS) {
                    	paySuce();
                    	Toast("支付成功");
                    	Log.i("tag","成功购买,status:" + noxEvent.getStatus());
                    } else if (noxEvent.getStatus() == NoxStatus.STATE_CONSUME_INVALIDMONEY) {
                    	payFail();
                    	Toast("支付失败");
                    	Log.i("tag","消费金额不合法,status:" + noxEvent.getStatus());
                    } else if (noxEvent.getStatus() == NoxStatus.STATE_CONSUME_CANCEL) {
                    	payFail();
                    	Toast("支付失败");
                    	Log.i("tag","您取消了购买,status:" + noxEvent.getStatus());
                    } else if (noxEvent.getStatus() == NoxStatus.STATE_CONSUME_FAILED) {
                    	payFail();
                    	Toast("支付失败");
                    	Log.i("tag","购买失败,status:" + noxEvent.getStatus());
                    } else {
                    	payFail();
                    	Toast("支付失败");
                    	Log.i("tag","status:" + noxEvent.getStatus());
                    }
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
				NoxSDKPlatform.getInstance().noxExit(new OnExitListener() {

					@Override
					public void finish(NoxEvent<KSAppEntity> event) {
						if (event.getStatus() == NoxStatus.STATE_EXIT_CANCEL) {
							// 用户取消退出
							return;
						}
						if (event.getStatus() == NoxStatus.STATE_EXIT_NOT_IMPLEMENT) {
							// 渠道未实现，调用游戏的退出
							Log.i("tag","1");
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
						} else {
							if (event.getStatus() == NoxStatus.STATE_EXIT_SUCCESS) {
								Log.i("tag","2");
								callback.onExit();
							}
						}
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
		//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		role_id = roleId;
		role_name = roleName;
		role_level = roleLevel;
		zone_id = zoneId;
		zone_name = zoneName;
		role_ctime = Long.parseLong(roleCTime);
		userRoleEntity = new KSUserRoleEntity();
		userRoleEntity.setRoleId(role_id);
		userRoleEntity.setRoleName(role_name);
		userRoleEntity.setRoleGrade(role_level);
		userRoleEntity.setRoleCreateTime(role_ctime);
		
		//如果游戏只有一个区服信息，设置serverId与serverName即可，如果区和服分别有信息，则分别设置server和zone相关信息
		userRoleEntity.setServerId(zone_id);
		userRoleEntity.setServerName(zone_name);
//		userRoleEntity.setZoneId("区域ID");
//		userRoleEntity.setZoneName("区域名称");
		
		userRoleEntity.setVip("10");

		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){
			//进入游戏
			NoxSDKPlatform.getInstance().noxEntryGame(userRoleEntity,
					new OnEntryListener() {

						@Override
						public void finish(NoxEvent<KSUserRoleEntity> event) {
							Log.i("tag","进入游戏：状态码 = " + event.getStatus());
						}
					});
		}else if(Integer.parseInt(ext) == 2){
			NoxSDKPlatform.getInstance().noxCreateRole(userRoleEntity,
					new OnCreateRoleListener() {

						@Override
						public void finish(NoxEvent<KSUserRoleEntity> event) {
							Log.i("tag","创建角色成功：状态码= " + event.getStatus());
						}
					});
		}else if(Integer.parseInt(ext) == 3){

		}
	}

	public static void onResume(Activity paramActivity) {
		NoxSDKPlatform.getInstance().noxResume();
	}

	public static void onPause(Activity paramActivity) {
		NoxSDKPlatform.getInstance().noxPause();
	}

	public static void onDestroy(Activity paramActivity) {
		NoxSDKPlatform.getInstance().noxDestroy();
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity paramActivity) {

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
	public static void intsdk(final Activity activity){
		KSAppEntity appInfo = new KSAppEntity();
		appInfo.setAppId(DeviceUtil.getGameInfo(activity, "yeshen_appid"));
		appInfo.setAppKey(DeviceUtil.getGameInfo(activity, "yeshen_appkey"));
		/**
		 * sdk初始化，确保参数与平台上一致
		 */
		NoxSDKPlatform.init(appInfo, activity, new OnInitListener() {

			@Override
			public void finish(NoxEvent<KSAppEntity> event) {
//				Toast.makeText(activity,
//						"初始化完成，状态：" + event.getStatus(), Toast.LENGTH_LONG)
//						.show();
				/**
				 * 浮窗内注销实现，当浮窗内点击注销时，会出发该方法注册的监听
				 */
				NoxSDKPlatform.getInstance().registerLogoutListener(
						new OnLogoutListener() {

							@Override
							public void finish(final NoxEvent<KSUserEntity> arg0) {
								// 回到登陆页面
								mActivity.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										loginOut();
										Log.i("tag","登出成功 = " +arg0.getStatus());
									}
								});

							}

						});
				switch (event.getStatus()) {
				case NoxStatus.STATE_NETWORK_ERROR:
					// 网络不可用
					Log.i("tag","网络不可用 ");
					break;
				case NoxStatus.STATE_REQUEST_TIMEOUT:
					// 请求链接超时
					Log.i("tag","请求链接超时 ");
					break;
				case NoxStatus.STATE_INIT_SUCCESS:
					// SDK 初始化成功
					isinit = true;
					Log.i("tag","SDK 初始化成功 ");
					break;
				case NoxStatus.STATE_INIT_NO_INIT:
					// SDK 未初始化
					Log.i("tag","SDK 未初始化 ");
					break;
				case NoxStatus.STATE_INIT_INITING:
					// SDK 初始化进行中
					Log.i("tag","SDK 初始化进行中 ");
					break;
				case NoxStatus.STATE_INIT_FAILED:
					// SDK 初始化失败
					Log.i("tag","SDK 初始化失败");
					break;
				}
			}

		});
	}

    public static void sdklogin(final Activity activity){
    	NoxSDKPlatform.getInstance().noxLogin(new OnLoginListener() {
			@Override
			public void finish(NoxEvent<KSUserEntity> event) {
				KSUserEntity user = event.getObject();
				if (event.getStatus() == NoxStatus.STATE_LOGIN_SUCCESS) {
					//登录成功
					String uid = user.getUid();
					String username = user.getUserName();
					String token = user.getAccessToken();
					loginSuce(activity, uid, username, token);
					Toast("登录成功");
				} else if (event.getStatus() == NoxStatus.STATE_LOGIN_CANCEL) {
					//登录失败
					loginFail();
					Toast("登录失败");
					Log.i("tag","登录失败 状态码 = "+event.getStatus());
				} else if (event.getStatus() == NoxStatus.STATE_INIT_NO_INIT) {
					//SDK 未初始化
					loginFail();
					Toast("未初始化");
					Log.i("tag","登录失败 状态码 = "+event.getStatus());
				} else if (event.getStatus() == NoxStatus.STATE_INIT_INITING) {
					//SDK 正在初始化
					loginFail();
					Toast("正在初始化初始化");
					Log.i("tag","登录失败 状态码 = "+event.getStatus());
				} else if (event.getStatus() == NoxStatus.STATE_INIT_FAILED) {
					//SDK 初始化失败
					loginFail();
					Toast("初始化失败");
					Log.i("tag","登录失败 状态码 = "+event.getStatus());
				} else {
					loginFail();
					Toast("登录失败");
					Log.i("tag","登录失败 状态码 = "+event.getStatus());
				}
			}
		});
    }
}
