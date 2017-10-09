package com.yayawan.impl;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.hjr.sdkkit.framework.mw.entity.DataTypes;
import com.hjr.sdkkit.framework.mw.entity.ParamsContainer;
import com.hjr.sdkkit.framework.mw.entity.ParamsKey;
import com.hjr.sdkkit.framework.mw.openapi.HJRSDKKitPlateformCore;
import com.hjr.sdkkit.framework.mw.openapi.callback.HJRSDKKitPlateformCallBack;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	static HJRSDKKitPlateformCore sdkObj = null;
	static YYWExitCallback callback1 = null;
	static boolean islogout=true;
	
	
	
	
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("kuaifa初始化sdk");

		sdkObj = HJRSDKKitPlateformCore.initHJRPlateform(mActivity,new HJRSDKKitPlateformCallBack(){

			@Override
			public void exitGameCallBack(int retStatus, String retMessage) {
				// TODO Auto-generated method stub
				if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
					callback1.onExit();
				}
			}

			@Override
			public void getOrderResultCallBack(String arg0, int arg1, String arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void initCallBack(int retStatus, String retMessage) {
				// TODO Auto-generated method stub
				if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
					//成功， 只有在sdk初始化成功之后才能调用sdk的登录
					isinit = true; 
				}else {
					//失败
				}
				
			}

			@Override
			public void loginCallBack(String loginUserId, 
									String loginUserName,
									String loginAuthToken,
									String loginOpenId,
									boolean switchUserFlag ,
									int retStatus,
									String retMessage) 
			{
				// TODO Auto-generated method stub
				if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
					//登录成功 
					if (switchUserFlag){
						System.out.println("statusswitchUserFlag");
						Yayalog.loger("statusswitchUserFlag");
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									Thread.sleep(1000);
									sdkObj.User.logout();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
						//switchUserFlag=false;
						islogout=true;
						//YaYawanconstants.loginOut();
						
					}
					else{
						
						Yayalog.loger("statusswitchUserFlag+++++++++++");
						loginSuce(mActivity, loginOpenId, loginUserName,loginAuthToken);
					}
				
				}else {
					//登陆失败
					if (switchUserFlag) { 
						System.out.println("witchUserFlag");
						sdkObj.User.logout();
						//YaYawanconstants.loginOut();
					}else {
						YaYawanconstants.loginFail();
					}
				}
			}

			@Override
			public void logoutCallBack(int retStatus, String retMessage) {
				// TODO Auto-generated method stub
				Yayalog.loger("logout");
				if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
					YaYawanconstants.loginOut();
				}
			}

			@Override
			public void payCallBack(String payKitOrderId, int retStatus,String retMessage) {
				// TODO Auto-generated method stub
				if (retStatus == HJRSDKKitPlateformCallBack.STATUS_SUCCESS) {
					//支付成功
					YaYawanconstants.paySuce();
					
					ParamsContainer pc = new ParamsContainer();
					pc.putInt(ParamsKey.KEY_AMOUNT, Integer.parseInt(String.valueOf(YYWMain.mOrder.money/100)));
					pc.putString(ParamsKey.KEY_ORDERNUMBER, payKitOrderId);
					pc.putString(ParamsKey.KEY_PRODUCT_DESC, YYWMain.mOrder.goods);
					sdkObj.Collections.onDatas(DataTypes.DATA_PAY, pc);
					//调用sdk的支付统计接口：hjrSDK.Collections.onDatas(DataTypes.DATA_PAY, pc);
				}
				else {
					System.out.println("支付失败");
					//支付失败
					YaYawanconstants.payFail();
				}
			}

			@Override
			public void pushReceiveCallBack(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
		});

	}
	
	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("kuaifa登录");
		
		if (isinit) {
			sdkObj.User.login(mactivity);
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

		Yayalog.loger("kuaifa支付");
		
		ParamsContainer pc = new ParamsContainer();
		// 所购买商品金额, 以元为单位。
		pc.putInt(ParamsKey.KEY_PAY_AMOUNT,  Integer.parseInt(String.valueOf(YYWMain.mOrder.money/100)));
		// 购买数量 ，通常都是1
		pc.putInt(ParamsKey.KEY_PAY_PRODUCT_NUM, 1);
		// 订单号， 没有传""
		pc.putString(ParamsKey.KEY_PAY_ORDER_ID, morderid);
		//商品ID，请注意值一定是整型
		pc.putInt(ParamsKey.KEY_PAY_PRODUCT_ID, 1);
		// 所购买商品名称
		pc.putString(ParamsKey.KEY_PAY_PRODUCT_NAME, YYWMain.mOrder.goods);
		//商品描述
		pc.putString(ParamsKey.KEY_PRODUCT_DESC, "商品");
		// 扩展参数, 会作为透传给cp服务端，可以为""
		pc.putString(ParamsKey.KEY_EXTINFO, morderid);
		
		sdkObj.Pay.pay(pc);
	}

	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("kuaifa退出");
		callback1 = callback;
		sdkObj.Base.exitGame(paramActivity);
		//sdkObj.User.logout();
	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("kuaifa设置角色信息"+YYWMain.mRole.toString());
		if (Integer.parseInt(ext) == 1){
			//登陆成功
			ParamsContainer pc = new ParamsContainer();
			pc.putString(ParamsKey.KEY_ROLE_ID, roleId);
			pc.putString(ParamsKey.KEY_ROLE_NAME, roleName);
			pc.putInt(ParamsKey.KEY_ROLE_LEVEL, Integer.parseInt(roleLevel));  
			pc.putString(ParamsKey.KEY_SERVER_ID, zoneId);
			pc.putString(ParamsKey.KEY_SERVER_NAME, zoneName);
			pc.put(ParamsKey.KEY_ROLE_CREATETIME, 0L);
			pc.put(ParamsKey.KEY_ROLE_UPGRADETIME, 0L);
			sdkObj.Collections.onDatas(DataTypes.DATA_ENTER_GAME, pc);
		}
		else if (Integer.parseInt(ext) == 2){
			//角色创建
			ParamsContainer pc = new ParamsContainer();
			pc.putString(ParamsKey.KEY_ROLE_ID, roleId);
			pc.putString(ParamsKey.KEY_ROLE_NAME, roleName);
			pc.putString(ParamsKey.KEY_SERVER_ID, zoneId);
			pc.putString(ParamsKey.KEY_SERVER_NAME, zoneName);
			sdkObj.Collections.onDatas(DataTypes.DATA_CREATE_ROLE, pc);
			
			ParamsContainer pc1 = new ParamsContainer();
			pc1.putString(ParamsKey.KEY_ROLE_ID, roleId);
			pc1.putString(ParamsKey.KEY_ROLE_NAME, roleName);
			pc1.putInt(ParamsKey.KEY_ROLE_LEVEL, Integer.parseInt(roleLevel));  
			pc1.putString(ParamsKey.KEY_SERVER_ID, zoneId);
			pc1.putString(ParamsKey.KEY_SERVER_NAME, zoneName);
			pc1.put(ParamsKey.KEY_ROLE_CREATETIME, 0L);
			pc1.put(ParamsKey.KEY_ROLE_UPGRADETIME, 0L);
			sdkObj.Collections.onDatas(DataTypes.DATA_ENTER_GAME, pc1);
		}
		else if (Integer.parseInt(ext) == 3){
			//角色升级
			ParamsContainer pc = new ParamsContainer();
			
			pc.putString(ParamsKey.KEY_ROLE_LEVEL, roleLevel+"");

			pc.put(ParamsKey.KEY_ROLE_CREATETIME, 0L);
			pc.put(ParamsKey.KEY_ROLE_UPGRADETIME, 0L);
			System.out.println("快发升级接口："+pc.toString());
			sdkObj.Collections.onDatas(DataTypes.DATA_ROLE_UPGRADE, pc);
		}

	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
	    if (sdkObj != null) {
	        sdkObj.LifeCycle.onResume();
	    }
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
	    if (sdkObj != null) {
	        sdkObj.LifeCycle.onPause();
	    }
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
	    if (sdkObj != null) {
	        sdkObj.LifeCycle.onDestroy();
	    }
	}
	
	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		// TODO Auto-generated method stub
	    if (sdkObj != null) {
	        sdkObj.LifeCycle.onActivityResult(paramInt1, paramInt2, paramIntent);
	    }
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
	    if (sdkObj != null) {
	        sdkObj.LifeCycle.onNewIntent(paramIntent);
	    }
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
	    if (sdkObj != null) {
	        sdkObj.LifeCycle.onStop();
	    }
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

	
	

	private void Toast(String msg){
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
	}

	public static void logout(Activity paramActivity) {
		// TODO Auto-generated method stub
		System.out.println("logout");
		sdkObj.User.logout();
	}

}
