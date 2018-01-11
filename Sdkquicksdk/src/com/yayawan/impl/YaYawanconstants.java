package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.quicksdk.QuickSDK;
import com.quicksdk.Sdk;
import com.quicksdk.entity.GameRoleInfo;
import com.quicksdk.entity.OrderInfo;
import com.quicksdk.entity.UserInfo;
import com.quicksdk.notifier.ExitNotifier;
import com.quicksdk.notifier.InitNotifier;
import com.quicksdk.notifier.LoginNotifier;
import com.quicksdk.notifier.LogoutNotifier;
import com.quicksdk.notifier.PayNotifier;
import com.quicksdk.notifier.SwitchAccountNotifier;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static String zone_Id = "1";
	private static String zone_Name = "1";
	private static String role_Name = "1";
	private static String role_Id = "1";
	private static String role_Level = "1";
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		
		// 设置横竖屏，游戏横屏为true，游戏竖屏为false(必接)
				QuickSDK.getInstance().setIsLandScape(DeviceUtil.isLandscape(mActivity)?true:false);

				// 设置通知，用于监听初始化，登录，注销，支付及退出功能的返回值(必接)
				initQkNotifiers();

				// 初始化接口，应在Activity的onCreate方法中调用(必接)
				// 请将下面语句中的第二与第三个参数，替换成QuickSDK后台申请的productCode和productKey值，目前的值仅作为示例
				
				String productCode = ""+DeviceUtil.getGameInfo(mactivity, "productCode");
				String productKey = ""+DeviceUtil.getGameInfo(mactivity, "productKey");
				com.quicksdk.Sdk.getInstance().init(mactivity, productCode, productKey);

		
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
			com.quicksdk.User.getInstance().login(mactivity);
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
		
		GameRoleInfo roleInfo = new GameRoleInfo();
		roleInfo.setServerID(zone_Id);// 服务器ID，其值必须为数字字符串
		roleInfo.setServerName(zone_Name);// 服务器名称
		roleInfo.setGameRoleName(role_Name);// 角色名称
		roleInfo.setGameRoleID(role_Id);// 角色ID
		roleInfo.setGameUserLevel(role_Level);// 等级
		roleInfo.setVipLevel("");// VIP等级
		roleInfo.setGameBalance("");// 角色现有金额
		roleInfo.setPartyName("");// 公会名字
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCpOrderID(morderid);// 游戏订单号
		orderInfo.setGoodsName(YYWMain.mOrder.goods);// 产品名称
		// orderInfo.setGoodsName("月卡");
		orderInfo.setCount(1);// 购买数量，如购买"10元宝"则传10	
		// orderInfo.setCount(1);// 购买数量，如购买"月卡"则传1
		orderInfo.setAmount(YYWMain.mOrder.money/100); // 总金额（单位为元）
		orderInfo.setGoodsID("s_"+YYWMain.mOrder.goods); // 产品ID，用来识别购买的产品
		orderInfo.setExtrasParams(""); // 透传参数

		com.quicksdk.Payment.getInstance().pay(mactivity, orderInfo, roleInfo);
		
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

		// 先判断渠道是否有退出框，如果有则直接调用quick的exit接口
		if (QuickSDK.getInstance().isShowExitDialog()) {
			Sdk.getInstance().exit(paramActivity);
		} else {
			// 游戏调用自身的退出对话框，点击确定后，调用quick的exit接口
//			new AlertDialog.Builder(MainActivity.this).setTitle("退出").setMessage("是否退出游戏?")
//					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface arg0, int arg1) {
//							Sdk.getInstance().exit(MainActivity.this);
//						}
//					}).setNegativeButton("取消", null).show();
			
			KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
				
				@Override
				public void onSuccess(User arg0, int arg1) {
					// TODO Auto-generated method stub
					Sdk.getInstance().exit(mActivity);
				}
				
				@Override
				public void onLogout() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onError(int arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		
		role_Id = roleId;
		role_Name = roleName;
		role_Level = roleLevel;
		zone_Id = zoneId;
		zone_Name = zoneName;
		
		
		GameRoleInfo roleInfo = new GameRoleInfo();
		roleInfo.setServerID(zone_Id);// 服务器ID
		roleInfo.setServerName(zone_Name);// 服务器名称
		roleInfo.setGameRoleName(role_Name);// 角色名称
		roleInfo.setGameRoleID(role_Id);// 角色ID
		roleInfo.setGameUserLevel(role_Level);// 等级
		roleInfo.setVipLevel(""); // 设置当前用户vip等级，必须为整型字符串
		roleInfo.setGameBalance(""); // 角色现有金额
//		roleInfo.setGameUserLevel("12"); // 设置游戏角色等级
		roleInfo.setPartyName(""); // 设置帮派，公会名称
		roleInfo.setRoleCreateTime("1473141432"); // UC与1881渠道必传，值为10位数时间戳
		roleInfo.setPartyId(""); // 360渠道参数，设置帮派id，必须为整型字符串
		roleInfo.setGameRoleGender(""); // 360渠道参数
		roleInfo.setGameRolePower(""); // 360渠道参数，设置角色战力，必须为整型字符串
		roleInfo.setPartyRoleId(""); // 360渠道参数，设置角色在帮派中的id
		roleInfo.setPartyRoleName(""); // 360渠道参数，设置角色在帮派中的名称
		roleInfo.setProfessionId(""); // 360渠道参数，设置角色职业id，必须为整型字符串
		roleInfo.setProfession(""); // 360渠道参数，设置角色职业名称
		roleInfo.setFriendlist("无"); //360渠道参数，设置好友关系列表，格式请参考：http://open.quicksdk.net/help/detail/aid/190
		com.quicksdk.User.getInstance().setGameRoleInfo(paramActivity, roleInfo, true);
	}

	public static void onResume(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity,int paramInt1,
			int paramInt2, Intent paramIntent) {
		com.quicksdk.Sdk.getInstance().onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		com.quicksdk.Sdk.getInstance().onNewIntent(paramIntent);
	}

	public static void onStart(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		com.quicksdk.Sdk.getInstance().onStop(paramActivity);
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
		//		mActivity.runOnUiThread(new Runnable() {
		//
		//			@Override
		//			public void run() {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
		//			}
		//		});
	}


	/**
	 * 设置通知，用于监听初始化，登录，注销，支付及退出功能的返回值
	 */
	private static void initQkNotifiers() {
		QuickSDK.getInstance()
		// 1.设置初始化通知(必接)
				.setInitNotifier(new InitNotifier() {

					@Override
					public void onSuccess() {
						Toast("初始化成功");
					}

					@Override
					public void onFailed(String message, String trace) {
						Toast("初始化失败:" + message);
					}
				})
				// 2.设置登录通知(必接)
		.setLoginNotifier(new LoginNotifier() {

					@Override
					public void onSuccess(UserInfo userInfo) {
						if (userInfo != null) {
							Log.i("tag","登陆成功" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName()
									+ "\n\r" + "Token:  " + userInfo.getToken());
loginSuce(mActivity, userInfo.getUID(), userInfo.getUserName(), userInfo.getToken());
Toast("登陆成功");

setUserinfo();
							// 登录成功之后，进入游戏时，需要向渠道提交用户信息
//							setUserInfo();
						}
					}

					private void setUserinfo() {
						GameRoleInfo roleInfo = new GameRoleInfo();
						roleInfo.setServerID(zone_Id);// 服务器ID
						roleInfo.setServerName(zone_Name);// 服务器名称
						roleInfo.setGameRoleName(role_Name);// 角色名称
						roleInfo.setGameRoleID(role_Id);// 角色ID
						roleInfo.setGameUserLevel(role_Level);// 等级
						roleInfo.setVipLevel(""); // 设置当前用户vip等级，必须为整型字符串
						roleInfo.setGameBalance(""); // 角色现有金额
//						roleInfo.setGameUserLevel("12"); // 设置游戏角色等级
						roleInfo.setPartyName(""); // 设置帮派，公会名称
						roleInfo.setRoleCreateTime("1473141432"); // UC与1881渠道必传，值为10位数时间戳
						roleInfo.setPartyId(""); // 360渠道参数，设置帮派id，必须为整型字符串
						roleInfo.setGameRoleGender(""); // 360渠道参数
						roleInfo.setGameRolePower(""); // 360渠道参数，设置角色战力，必须为整型字符串
						roleInfo.setPartyRoleId(""); // 360渠道参数，设置角色在帮派中的id
						roleInfo.setPartyRoleName(""); // 360渠道参数，设置角色在帮派中的名称
						roleInfo.setProfessionId(""); // 360渠道参数，设置角色职业id，必须为整型字符串
						roleInfo.setProfession(""); // 360渠道参数，设置角色职业名称
						roleInfo.setFriendlist("无"); //360渠道参数，设置好友关系列表，格式请参考：http://open.quicksdk.net/help/detail/aid/190
						com.quicksdk.User.getInstance().setGameRoleInfo(mActivity, roleInfo, true);
						
					}

					@Override
					public void onCancel() {
						loginFail();
						Toast("取消登陆");
					}

					@Override
					public void onFailed(final String message, String trace) {
						loginFail();
						Log.i("tag","登陆失败:" + message);
						Toast("登陆失败");
					}

				})
				// 3.设置注销通知(必接)
				.setLogoutNotifier(new LogoutNotifier() {

					@Override
					public void onSuccess() {
						loginOut();
						Toast("注销成功");
					}

					@Override
					public void onFailed(String message, String trace) {
						Log.i("tag", "注销失败:" + message);
						Toast("注销失败");
					}
				})
				// 4.设置切换账号通知(必接)
				.setSwitchAccountNotifier(new SwitchAccountNotifier() {

					@Override
					public void onSuccess(UserInfo userInfo) {
						loginOut();
//						if (userInfo != null) {
//							Toast("切换账号成功" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName()
//									+ "\n\r" + "Token:  " + userInfo.getToken());
//							
//						}
					}

					@Override
					public void onFailed(String message, String trace) {
						Log.i("tag","切换账号失败:" + message);
						Toast("切换账号失败");
					}

					@Override
					public void onCancel() {
						Log.i("tag","取消切换账号");
					}
				})
				// 5.设置支付通知(必接)
				.setPayNotifier(new PayNotifier() {

					@Override
					public void onSuccess(String sdkOrderID, String cpOrderID, String extrasParams) {
						paySuce();
						Log.i("tag","支付成功，sdkOrderID:" + sdkOrderID + ",cpOrderID:" + cpOrderID);
						Toast("支付成功");
					}

					@Override
					public void onCancel(String cpOrderID) {
						payFail();
						Log.i("tag","支付取消，cpOrderID:" + cpOrderID);
						Toast("支付取消");
					}

					@Override
					public void onFailed(String cpOrderID, String message, String trace) {
						payFail();
						Log.i("tag","支付失败:" + "pay failed,cpOrderID:" + cpOrderID + ",message:" + message);
						Toast("支付失败");
					}
				})
				// 6.设置退出通知(必接)
				.setExitNotifier(new ExitNotifier() {

					@Override
					public void onSuccess() {
						// 进行游戏本身的退出操作，下面的finish()只是示例
						mActivity.finish();
					}

					@Override
					public void onFailed(String message, String trace) {
						Log.i("tag","退出失败：" + message);
					}
				});
	}
	
}
