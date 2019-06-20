package com.yayawan.impl;

import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.game.sdk.HuosdkManager;
import com.game.sdk.domain.CustomPayParam;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.PaymentCallbackInfo;
import com.game.sdk.domain.PaymentErrorMsg;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.SubmitRoleInfoCallBack;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.listener.OnLogoutListener;
import com.game.sdk.listener.OnPaymentListener;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit=false;
	
	private static HuosdkManager sdkManager;
	
	private static String Role_id = "9527";
	
	private static String Role_level = "1";
	
	private static String role_Name = "1";
	
	private static String Server_id = "1";
	
	private static String server_Name = "1";
	
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		sdkManager = HuosdkManager.getInstance();
		//设置是否使用直接登陆,true为使用：第一次调用登陆时自动生成一个账号登陆
        
		sdkManager.initSdk(mactivity, new OnInitSdkListener() {
			
			@Override
			public void initSuccess(String code, String msg) {
				isinit = true;
				Log.i("tag","初始化成功 = " +msg);
			}
			
			@Override
			public void initError(String code, String msg) {
				Log.i("tag","初始化失败 = " +msg);
			}
		});
		
		sdkManager.addLoginListener(new OnLoginListener() {
            @Override
            public void loginSuccess( LogincallBack logincBack) {
                Log.i("tag","登陆成功 uid=" +
                        logincBack.mem_id + "  token=" + logincBack.user_token);
                String uid = logincBack.mem_id;
                String token = logincBack.user_token;
                loginSuce(mActivity, uid, uid, token);
                Toast.makeText(mActivity,"登陆成功",Toast.LENGTH_SHORT).show();
                //一般登陆成功后需要显示浮点
                sdkManager.showFloatView();
            }
            @Override
            public void loginError(LoginErrorMsg loginErrorMsg) {
            	loginFail();
                Log.i("tag", " code=" + loginErrorMsg.code + "  msg=" + loginErrorMsg.msg);
                Toast.makeText(mActivity,"登陆失败",Toast.LENGTH_SHORT).show();
            }
        });
		
		sdkManager.addLogoutListener(new OnLogoutListener() {
            @Override
            public void logoutSuccess(int type, String code, String msg) {
                Log.i("tag","登出成功，类型type="+type+" code="+code+" msg="+msg);
                if(type==OnLogoutListener.TYPE_NORMAL_LOGOUT){//正常退出成功
                    Toast.makeText(mActivity,"退出成功",Toast.LENGTH_SHORT).show();
                    loginOut();
                }
                if(type==OnLogoutListener.TYPE_SWITCH_ACCOUNT){//切换账号退出成功
                    //游戏此时可跳转到登陆页面，让用户进行切换账号
                    Toast.makeText(mActivity,"退出登陆",Toast.LENGTH_SHORT).show();
                    loginOut();
                }
                if(type==OnLogoutListener.TYPE_TOKEN_INVALID){//登陆过期退出成功
                    //游戏此时可跳转到登陆页面，让用户进行重新登陆
                    sdkManager.showLogin(true);
                }
            }

            @Override
            public void logoutError(int type, String code, String msg) {
                Log.i("tag","登出失败，类型type="+type+" code="+code+" msg="+msg);
                if(type==OnLogoutListener.TYPE_NORMAL_LOGOUT){//正常退出失败

                }
                if(type==OnLogoutListener.TYPE_SWITCH_ACCOUNT){//切换账号退出失败

                }
                if(type==OnLogoutListener.TYPE_TOKEN_INVALID){//登陆过期退出失败

                }
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
		Log.i("tag","isinit="+isinit);
		if(isinit){
			Log.i("tag","登录开始");
			sdkManager.showLogin(true);
		}else{
//			inintsdk(mactivity);
			loginFail();
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		String money = YYWMain.mOrder.money/100+"";
         CustomPayParam customPayParam = new CustomPayParam();
         initTestParam(customPayParam, money,morderid);
         customPayParam.setRoleinfo(initTestRoleInfo());
         sdkManager.showPay(customPayParam, new OnPaymentListener() {
             @Override
             public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
//                 double money = callbackInfo.money;
//                 String msg = callbackInfo.msg;

                 // 弹出支付成功信息，一般不用
                 paySuce();
                 Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                 Log.i("tag","充值金额数：" +callbackInfo.money + " 消息提示：" + callbackInfo.msg);
             }

             @Override
             public void paymentError(PaymentErrorMsg errorMsg) {
//                 int code = errorMsg.code;
//                 double money = errorMsg.money;
//                 String msg = errorMsg.msg;
                 // 弹出支付失败信息，一般不用
                 payFail();
                 Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                 Log.i("tag","充值失败：code:" +errorMsg.code + "  ErrorMsg:" + errorMsg.msg +"  预充值的金额：" + errorMsg.money);
             }
         });
	}
	
	 private static RoleInfo initTestRoleInfo() {
	        RoleInfo roleInfo = new RoleInfo();
	        roleInfo.setRolelevel_ctime("" + System.currentTimeMillis() / 1000);
	        roleInfo.setRolelevel_mtime("" + System.currentTimeMillis() / 1000);
	        roleInfo.setParty_name("");
	        roleInfo.setRole_balence(1.00f);
	        roleInfo.setRole_id(Role_id);
	        roleInfo.setRole_level(Integer.parseInt(Role_level));
	        roleInfo.setRole_name(role_Name);
	        roleInfo.setRole_vip(0);
	        roleInfo.setServer_id(Server_id);
	        roleInfo.setServer_name(server_Name);
	        Log.i("tag","initTestRoleInfo-roleInfo="+roleInfo);
	        return roleInfo;
	    }
	
	private static void initTestParam(CustomPayParam payParam, String money,String morderid) {
        payParam.setCp_order_id(morderid);
        payParam.setProduct_price(Float.parseFloat(money));
        payParam.setProduct_count(1);
        payParam.setProduct_id("s_"+YYWMain.mOrder.goods);
        payParam.setProduct_name(YYWMain.mOrder.goods);
        payParam.setProduct_desc(YYWMain.mOrder.goods);
        payParam.setExchange_rate(1);
        String gamemoneyname = ""+DeviceUtil.getGameInfo(mActivity, "gamemoneyname");
        payParam.setCurrency_name(gamemoneyname);
        payParam.setExt("");
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

		 KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {
				
				@Override
				public void onSuccess(User arg0, int arg1) {
//					callback.onExit();
					mActivity.finish();
					System.exit(0);
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
	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		Yayalog.loger("YaYawanconstants设置角色信息1");
		Role_id = roleId;
		role_Name = roleName;
		Server_id = zoneId;
		server_Name = zoneName;
		Role_level = roleLevel;
		if(Role_id.equals("")){
			Role_id = "9527";
		}
				
//				RoleInfo roleInfo = initTestRoleInfo();
//				roleInfo.setRole_type(1);
		if (Integer.parseInt(ext) == 1 || Integer.parseInt(ext) == 2 ||Integer.parseInt(ext) == 3){
		 RoleInfo roleInfo = new RoleInfo();
	        roleInfo.setRolelevel_ctime("" + System.currentTimeMillis() / 1000);
	        roleInfo.setRolelevel_mtime("" + System.currentTimeMillis() / 1000);
	        roleInfo.setParty_name("");
	        roleInfo.setRole_balence(1.00f);
	        roleInfo.setRole_id(Role_id);
	        roleInfo.setRole_level(Integer.parseInt(roleLevel));
	        roleInfo.setRole_name(roleName);
	        roleInfo.setRole_vip(0);
	        roleInfo.setServer_id(zoneId);
	        roleInfo.setServer_name(zoneName);
         roleInfo.setRole_type(1);
				sdkManager.setRoleInfo(roleInfo, new SubmitRoleInfoCallBack() {
					public void submitSuccess() {
						Yayalog.loger("提交成功");
					}
					
					public void submitFail(String msg) {
						Yayalog.loger("提交失败="+msg);
					}
				});		
		
          
		}
	}
	public static void onResume(Activity paramActivity) {
		sdkManager.showFloatView();
	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {
		sdkManager.recycle();
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity2) {

	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		 sdkManager.removeFloatView();
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



}
