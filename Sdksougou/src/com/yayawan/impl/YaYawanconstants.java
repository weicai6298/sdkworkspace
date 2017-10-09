package com.yayawan.impl;

import java.util.HashMap;
import java.util.Map;

import android.R.integer;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.sogou.gamecenter.sdk.SogouGamePlatform;
import com.sogou.gamecenter.sdk.bean.SogouGameConfig;
import com.sogou.gamecenter.sdk.bean.UserInfo;

import com.sogou.gamecenter.sdk.listener.InitCallbackListener;
import com.sogou.gamecenter.sdk.listener.LoginCallbackListener;
import com.sogou.gamecenter.sdk.listener.OnExitListener;
import com.sogou.gamecenter.sdk.listener.PayCallbackListener;
import com.sogou.gamecenter.sdk.listener.SwitchUserListener;
import com.sogou.gamecenter.sdk.views.FloatMenu;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;
	private static SogouGamePlatform mSogouGamePlatform = SogouGamePlatform.getInstance();
	
	private static String TAG="yayalog";
	
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("初始化sdk");
		SogouGameConfig config = new SogouGameConfig();		
		// 开发模式为true，false是正式环境
	    // 请注意，提交版本设置正式环境 
		config.devMode = false;
		config.gid = Integer.parseInt(DeviceUtil.getGameInfo(mactivity,"gid"));
		config.appKey=""+DeviceUtil.getGameInfo(mactivity, "Appkey");
		config.gameName=""+DeviceUtil.getGameInfo(mactivity, "gamename");
		// SDK准备初始化
		mSogouGamePlatform.prepare(mactivity, config);
		
		// SDK初始化，整个接入流程只调用一次
		mSogouGamePlatform.init(mactivity, new InitCallbackListener() {

					public void initSuccess() {
						isinit=true;
						//login(mactivity);
					}

					public void initFail(int code, String msg) {
						// SDK初始化失败再此关闭游戏即可
						Toast.makeText(mactivity, msg, Toast.LENGTH_LONG).show();
						//finish();
					}
				});
		
	}
	
	private static void initsouSdk(final Activity mactivity){
	
		
		
	}

	private static boolean isinit=false;
	private static FloatMenu mFloatMenu;
	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("sdk登录");
		if (isinit) {
			mSogouGamePlatform.login(mactivity, new LoginCallbackListener() {

				

				public void loginSuccess(int code, UserInfo userInfo) {
					
					mActivity.runOnUiThread(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							mFloatMenu = mSogouGamePlatform.createFloatMenu(mactivity, true);
							mFloatMenu.show();
							// 浮动设置切换帐号监听器
							mFloatMenu.setSwitchUserListener(new SwitchUserListener() {
								public void switchSuccess(int code, UserInfo userInfo) {
									Log.d(TAG, "FloatMenus witchSuccess code:" + code + " userInfo:" + userInfo);
									System.out.println("搜狗切换账号成功");
									YaYawanconstants.loginOut();
								}

								public void switchFail(int code, String msg) {
									Log.e(TAG, "FloatMenus switchFail code:" + code + " msg:" + msg);
								}
							});
						}
					});
					loginSuce(mactivity, userInfo.getUserId()+"", userInfo.getUserId()+"",userInfo.getSessionKey());
					// 创建浮动菜单务必在SDK登台态情况下 当前是全屏模式，isFullscreen为true
					
					
				}

				public void loginFail(int code, String msg) {
					//Log.d(TAG, "CP loginFail:" + msg);
					YaYawanconstants.loginFail();
				}
			});
		}else {
			inintsdk(mactivity);
		}
		
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("sdk支付");
		paynow( mactivity, false, morderid);
		
	}
	/**
	 * 搜狗支付中心调用实例
	 * 
	 * @param isAmountEditable
	 *            金额是否可以编辑
	 */
	public static void paynow(Activity mactivity, boolean isAmountEditable,String orderid) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 游戏货币名字（必传）
		data.put("currency", DeviceUtil.getGameInfo(mactivity, "gamemoneyname"));
		// 人民币兑换比例（必传）,小数 比例得加 f
		data.put("rate", 10f);

		// 购买商品名字（必传）
		data.put("product_name", YYWMain.mOrder.goods);
		// 充值金额,单位是元，在手游中数据类型为整型（必传）
		data.put("amount", YYWMain.mOrder.money/100);
		// 透传参数,游戏方自行定义（必传）
		data.put("app_data", orderid);
		// 是否可以编辑支付金额（可选）如果不传递将表示金额不可以编辑
		data.put("appmodes", isAmountEditable);
		mSogouGamePlatform.pay(mActivity, data, new PayCallbackListener() {

			// 支付成功回调,游戏方可以做后续逻辑处理
			// 收到该回调说明提交订单成功，但成功与否要以服务器回调通知为准
			public void paySuccess(String orderId, String appData) {
				// orderId是订单号，appData是游戏方自己传的透传消息
				Log.d(TAG, "paySuccess orderId:" + orderId + " appData:" + appData);
				paySuce();
			}

			public void payFail(int code, String orderId, String appData) {
				// 支付失败情况下,orderId可能为空
				YaYawanconstants.payFail();
				if (orderId != null) {
					Log.e(TAG, "payFail code:" + code + "orderId:" + orderId + " appData:" + appData);
				} else {
					Log.e(TAG, "payFail code:" + code + " appData:" + appData);
				}
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
		Yayalog.loger("sdk退出");
		mActivity.runOnUiThread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				mSogouGamePlatform.exit(new OnExitListener(paramActivity) {
					
					@Override
					public void onCompleted() {
						// TODO Auto-generated method stub
						callback.onExit();
						
					}
				});
			}
		});
		
		
		//

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setRoleData(Activity arg0) {
		// TODO Auto-generated method stub

	}

	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		// 当前界面切换到前台，调用浮动菜单show方法
				if (mFloatMenu != null) {
					// 默认浮在右上角位置，距左边为10，距下边为100位置，单位为像素
					mFloatMenu.setParamsXY(10, 100);
					mFloatMenu.show();
					
				}
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		if (mFloatMenu != null)
			mFloatMenu.hide();
	}

	public static void onDestroy(Activity paramActivity) {
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
			System.out.println("进来了yayalogu");
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

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

}
