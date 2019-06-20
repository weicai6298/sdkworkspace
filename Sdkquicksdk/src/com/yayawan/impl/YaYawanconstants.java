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
import com.quicksdk.notifier.i;
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
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		mactivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 设置横竖屏，游戏横屏为true，游戏竖屏为false(必接)
				QuickSDK.getInstance().setIsLandScape(DeviceUtil.isLandscape(mActivity)?true:false);

				// 设置通知，用于监听初始化，登录，注销，支付及退出功能的返回值(必接)
				initQkNotifiers();

				// 初始化接口，应在Activity的onCreate方法中调用(必接)
				// 请将下面语句中的第二与第三个参数，替换成QuickSDK后台申请的productCode和productKey值，目前的值仅作为示例

				String productCode = ""+DeviceUtil.getGameInfo(mactivity, "productCode");
				String productKey = ""+DeviceUtil.getGameInfo(mactivity, "productKey");
				com.quicksdk.Sdk.getInstance().init(mactivity, productCode, productKey);
			}
		});


	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		//虫虫助手处理
		Log.i("tag","application初始化结束");
		String APP_ID= ""+DeviceUtil.getGameInfo(applicationContext, "APP_ID");
		Log.i("tag","APP_ID="+APP_ID);
        DeviceUtil.appid = APP_ID.substring(2, APP_ID.length());
        DeviceUtil.gameid = APP_ID.substring(2, APP_ID.length());
        Log.i("tag","gameid="+DeviceUtil.gameid);

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		Log.i("tag","isinit = " +isinit);
		if(isinit){
			mactivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					com.quicksdk.User.getInstance().login(mactivity);
				}
			});
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(final Activity mactivity, final String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		mactivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
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
				int waresid = getWaresid(YYWMain.mOrder.goods);
				Log.i("tag","waresid = " +waresid);
				orderInfo.setGoodsID(waresid+""); // 产品ID，用来识别购买的产品
				orderInfo.setExtrasParams(""); // 透传参数

				com.quicksdk.Payment.getInstance().pay(mactivity, orderInfo, roleInfo);
			}
		});
	}
	private static int getWaresid(String goods) {
		int waresid = 0;
//		if(goods.equals("1800钻石")){
//			waresid = 1;
//		}else if(goods.equals("9000钻石")){
//			waresid = 2;
//		}else if(goods.equals("29400钻石")){
//			waresid = 3;
//		}else if(goods.equals("38400钻石")){
//			waresid = 4;
//		}else if(goods.equals("59400钻石")){
//			waresid = 5;
//		}else if(goods.equals("98400钻石")){
//			waresid = 6;
//		}else if(goods.equals("194400钻石")){
//			waresid = 7;
//		}else if(goods.equals("410400钻石")){
//			waresid = 8;
//		}else if(goods.equals("800400钻石")){
//			waresid = 9;
//		}else if(goods.equals("强者基金")){
//			waresid = 10;
//		}else if(goods.equals("至尊大礼包1")){
//			waresid = 11;
//		}else if(goods.equals("月卡")){
//			waresid = 12;
//		}else if(goods.equals("终卡")){
//			waresid = 13;
//		}
//
//		else if(goods.equals("1166400钻石")){
//			waresid = 14;
//		}else if(goods.equals("1766400钻石")){
//			waresid = 15;
//		}else if(goods.equals("2966400钻石")){
//			waresid = 16;
//		}
//		
//		else if(goods.equals("盛典礼包一")){
//			waresid = 17;
//		}else if(goods.equals("盛典礼包二")){
//			waresid = 18;
//		}else if(goods.equals("盛典礼包三")){
//			waresid = 19;
//		}else if(goods.equals("盛典礼包四")){
//			waresid = 20;
//		}else if(goods.equals("盛典礼包五")){
//			waresid = 21;
//		}else if(goods.equals("盛典礼包六")){
//			waresid = 22;
//		}else if(goods.equals("盛典礼包七")){
//			waresid = 23;
//		}else if(goods.equals("盛典礼包八")){
//			waresid = 24;
//		}
//		else if(goods.equals("盛世礼包1")){
//			waresid = 25;
//		}else if(goods.equals("盛世礼包2")){
//			waresid = 26;
//		}else if(goods.equals("盛世礼包3")){
//			waresid = 27;
//		}else if(goods.equals("盛世礼包4")){
//			waresid = 28;
//		}else if(goods.equals("盛世礼包5")){
//			waresid = 29;
//		}else if(goods.equals("盛世礼包6")){
//			waresid = 30;
//		}else if(goods.equals("盛世礼包7")){
//			waresid = 31;
//		}else if(goods.equals("盛世礼包8")){
//			waresid = 32;
//		}else if(goods.equals("盛世礼包9")){
//			waresid = 33;
//		}else if(goods.equals("盛世礼包10")){
//			waresid = 34;
//		}
//		
//		else if(goods.equals("狂欢礼包1")){
//			waresid = 35;
//		}else if(goods.equals("狂欢礼包2")){
//			waresid = 36;
//		}else if(goods.equals("狂欢礼包3")){
//			waresid = 37;
//		}else if(goods.equals("狂欢礼包4")){
//			waresid = 38;
//		}else if(goods.equals("狂欢礼包5")){
//			waresid = 39;
//		}else if(goods.equals("狂欢礼包6")){
//			waresid = 40;
//		}else if(goods.equals("狂欢礼包7")){
//			waresid = 41;
//		}else if(goods.equals("狂欢礼包8")){
//			waresid = 42;
//		}else if(goods.equals("狂欢礼包9")){
//			waresid = 43;
//		}else if(goods.equals("狂欢礼包10")){
//			waresid = 44;
//		}
//		
//		else if(goods.equals("2400钻石")){
//			waresid = 45;
//		}else if(goods.equals("12000钻石")){
//			waresid = 46;
//		}else if(goods.equals("39200钻石")){
//			waresid = 47;
//		}else if(goods.equals("51200钻石")){
//			waresid = 48;
//		}else if(goods.equals("2400钻石")){
//			waresid = 49;
//		}else if(goods.equals("12000钻石")){
//			waresid = 50;
//		}else if(goods.equals("特装强者基金")){
//			waresid = 51;
//		}
		
		//超能特战队
		if(goods.equals("红宝石120")){
			waresid = 1;
		}else if(goods.equals("红宝石600")){
			waresid = 2;
		}else if(goods.equals("红宝石1500")){
			waresid = 3;
		}else if(goods.equals("红宝石2820")){
			waresid = 4;
		}else if(goods.equals("红宝石4560")){
			waresid = 5;
		}else if(goods.equals("红宝石7550")){
			waresid = 6;
		}else if(goods.equals("红宝石15560")){
			waresid = 7;
		}else if(goods.equals("首充礼包")){
			waresid = 8;
		}else if(goods.equals("月卡商品30天")){
			waresid = 9;
		}else if(goods.equals("关卡礼包")){
			waresid = 10;
		}else if(goods.equals("超值礼包")){
			waresid = 11;
		}else if(goods.equals("高级成长礼包")){
			waresid = 12;
		}else if(goods.equals("高级道具礼包")){
			waresid = 13;
		}else if(goods.equals("高速成长礼包")){
			waresid = 14;
		}else if(goods.equals("灵魂转生券")){
			waresid = 15;
		}else if(goods.equals("活动礼包1")){
			waresid = 16;
		}else if(goods.equals("活动礼包2")){
			waresid = 17;
		}else if(goods.equals("活动礼包3")){
			waresid = 18;
		}else if(goods.equals("活动礼包4")){
			waresid = 19;
		}else if(goods.equals("活动礼包5")){
			waresid = 20;
		}else if(goods.equals("周卡礼包")){
			waresid = 21;
		}else if(goods.equals("提速礼包")){
			waresid = 22;
		}
		return waresid;

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

		// 先判断渠道是否有退出框，如果有则直接调用quick的exit接口
		if (QuickSDK.getInstance().isShowExitDialog()) {
			paramActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Sdk.getInstance().exit(paramActivity);
				}
			});
		} else {
			// 游戏调用自身的退出对话框，点击确定后，调用quick的exit接口
			//			new AlertDialog.Builder(MainActivity.this).setTitle("退出").setMessage("是否退出游戏?")
			//					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			//						@Override
			//						public void onClick(DialogInterface arg0, int arg1) {
			//							Sdk.getInstance().exit(MainActivity.this);
			//						}
			//					}).setNegativeButton("取消", null).show();
			paramActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
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
			});
		}
	}

	/**
	 * 设置角色信息
	 * 
	 */
	public static void setData(final Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");

		role_Id = roleId;
		role_Name = roleName;
		role_Level = roleLevel;
		zone_Id = zoneId;
		zone_Name = zoneName;


		final GameRoleInfo roleInfo = new GameRoleInfo();
		roleInfo.setServerID(zone_Id);// 服务器ID
		roleInfo.setServerName(zone_Name);// 服务器名称
		roleInfo.setGameRoleName(role_Name);// 角色名称
		roleInfo.setGameRoleID(role_Id);// 角色ID
		roleInfo.setGameUserLevel(role_Level);// 等级
		roleInfo.setVipLevel(""); // 设置当前用户vip等级，必须为整型字符串
		roleInfo.setGameBalance(""); // 角色现有金额
		//		roleInfo.setGameUserLevel("12"); // 设置游戏角色等级
		roleInfo.setPartyName(""); // 设置帮派，公会名称
		roleInfo.setRoleCreateTime(roleCTime); // UC与1881渠道必传，值为10位数时间戳
		roleInfo.setPartyId(""); // 360渠道参数，设置帮派id，必须为整型字符串
		roleInfo.setGameRoleGender(""); // 360渠道参数
		roleInfo.setGameRolePower(""); // 360渠道参数，设置角色战力，必须为整型字符串
		roleInfo.setPartyRoleId(""); // 360渠道参数，设置角色在帮派中的id
		roleInfo.setPartyRoleName(""); // 360渠道参数，设置角色在帮派中的名称
		roleInfo.setProfessionId(""); // 360渠道参数，设置角色职业id，必须为整型字符串
		roleInfo.setProfession(""); // 360渠道参数，设置角色职业名称
		roleInfo.setFriendlist("无"); //360渠道参数，设置好友关系列表，格式请参考：http://open.quicksdk.net/help/detail/aid/190

		//1为角色登陆成功  2为角色创建  3为角色升级
		if(Integer.parseInt(ext) == 1){
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					com.quicksdk.User.getInstance().setGameRoleInfo(paramActivity, roleInfo, false);
				}
			});
		}else if(Integer.parseInt(ext) == 2){
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					com.quicksdk.User.getInstance().setGameRoleInfo(paramActivity, roleInfo, true);
				}
			});
		}else if(Integer.parseInt(ext) ==3){
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					com.quicksdk.User.getInstance().setGameRoleInfo(paramActivity, roleInfo, false);
				}
			});
		}
	}

	public static void onResume(Activity paramActivity) {
		Log.i("tag","onResume");
		com.quicksdk.Sdk.getInstance().onStart(paramActivity);
		com.quicksdk.Sdk.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		Log.i("tag","onPause");
		com.quicksdk.Sdk.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		Log.i("tag","onDestroy");
		com.quicksdk.Sdk.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity,int paramInt1,
			int paramInt2, Intent paramIntent) {
		com.quicksdk.Sdk.getInstance().onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		Log.i("tag","onNewIntent");
		com.quicksdk.Sdk.getInstance().onNewIntent(paramIntent);
	}

	public static void onStart(Activity paramActivity) {
		Log.i("tag","onStart");
		com.quicksdk.Sdk.getInstance().onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		Log.i("tag","onRestart");
		com.quicksdk.Sdk.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
		Log.i("tag","onCreate");
		com.quicksdk.Sdk.getInstance().onCreate(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		Log.i("tag","onStop");
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
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (YYWMain.mPayCallBack != null) {
					YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
							"success");
				}
			}
		});
	}

	/**
	 * 支付失败
	 * 
	 */
	public static void payFail() {
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (YYWMain.mPayCallBack != null) {
					YYWMain.mPayCallBack.onPayFailed(null, null);
				}
			}
		});
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
				Log.i("tag","初始化成功");
				isinit = true ;
			}

			@Override
			public void onFailed(String message, String trace) {
				Log.i("tag","初始化失败:" + message);
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
					Log.i("tag","登陆成功");

				}
			}

			@Override
			public void onCancel() {
				loginFail();
				Log.i("tag","取消登陆");
			}

			@Override
			public void onFailed(final String message, String trace) {
				loginFail();
				Log.i("tag","登陆失败:" + message);
			}

		})
		// 3.设置注销通知(必接)
		.setLogoutNotifier(new LogoutNotifier() {

			@Override
			public void onSuccess() {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loginOut();
						Log.i("tag","注销成功");
					}
				});
			}

			@Override
			public void onFailed(String message, String trace) {
				Log.i("tag", "注销失败:" + message);
			}
		})
		// 4.设置切换账号通知(必接)
		.setSwitchAccountNotifier(new SwitchAccountNotifier() {

			@Override
			public void onSuccess(UserInfo userInfo) {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loginOut();
						Log.i("tag","注销成功");
					}
				});
				//						if (userInfo != null) {
				//							Toast("切换账号成功" + "\n\r" + "UserID:  " + userInfo.getUID() + "\n\r" + "UserName:  " + userInfo.getUserName()
				//									+ "\n\r" + "Token:  " + userInfo.getToken());
				//							
				//						}
			}

			@Override
			public void onFailed(String message, String trace) {
				Log.i("tag","切换账号失败:" + message);
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
			}

			@Override
			public void onCancel(String cpOrderID) {
				payFail();
				Log.i("tag","支付取消，cpOrderID:" + cpOrderID);
			}

			@Override
			public void onFailed(String cpOrderID, String message, String trace) {
				payFail();
				Log.i("tag","支付失败:" + "pay failed,cpOrderID:" + cpOrderID + ",message:" + message);
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
