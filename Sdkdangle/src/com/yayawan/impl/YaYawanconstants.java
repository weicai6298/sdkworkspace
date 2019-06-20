package com.yayawan.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.downjoy.CallbackListener;
import com.downjoy.CallbackStatus;
import com.downjoy.Downjoy;
import com.downjoy.LoginInfo;
import com.downjoy.LogoutListener;
import com.downjoy.ResultListener;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings({ "deprecation", "unused" })
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static Downjoy downjoy; // 当乐游戏中心实例

	public static String uid;
	public static String token;

	public static  String zone_Id = "2";//玩家区服id 没有传字符串1
	public static  String zone_Name = "测试区服";//玩家区服名称，没有传字符串001
	public static  String role_Id = "222222";//玩家角色Id 没有传字符串1
	public static String role_Name = "测试角色名";//玩家角色名称 没有传字符串001
		private static String  role_CTime = "";//角色创建时间戳。获取不了创建的时间戳，就传DEMO中的这个值。
	//	private static  String role_Level = "22";
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		initDownjoy();
		//		downjoy.openMemberCenterDialog(mactivity);
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
			downjoyLogin(mactivity);
		}else{
			inintsdk(mactivity);
		}
	}


	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid,String dl_orderId) {
		Yayalog.loger("YaYawanconstantssdk支付");
		// 打开支付界面,获得订单号
		downjoy.openServerPaymentDialog(mactivity, dl_orderId, new CallbackListener<String>() {

			@Override
			public void callback(int status, String data) {
				if (status == CallbackStatus.SUCCESS) {
					paySuce();
					Toast("支付成功");
				} else if (status == CallbackStatus.FAIL) {
					payFail();
					Toast("支付失败");
				} else if (status == CallbackStatus.CANCEL) {
					payFail();
					Toast("支付取消");
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
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				downjoy.openExitDialog(paramActivity,  new CallbackListener<String>() {
					@Override
					public void callback( int status, String data) {
						if (CallbackStatus. SUCCESS  == status) {
							mActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									callback.onExit();
								}
							});
						}  else if (CallbackStatus. CANCEL  == status) {
							Toast("继续游戏");
						}
					}
				});
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
		//4.3.5新增，登录成功后，提交游戏数据（也可选择从服务器提交，具体参见服务器端接入文档）
		//所有参数尽量不要填空值
		//关于什么时候上报游戏数据：确保每次登录上传一次数据即可，不一定要在登录后马上上传！
				        zone_Id = zoneId;//玩家区服id 没有传字符串1
				        zone_Name = zoneName;//玩家区服名称，没有传字符串001
				        role_Id = roleId;//玩家角色Id 没有传字符串1
				        role_Name = roleName;//玩家角色名称 没有传字符串001
//				        role_CTime = roleCTime;//角色创建时间戳。获取不了创建的时间戳，就传DEMO中的这个值。
		//		        long roleLevelMTime = 1480747870001l;//角色等级变化时间戳。获取不了等级变化时间，就跟角色创建时间传一样的值。
		//		        role_Level = roleLevel;//角色等级，如果没有这个值，传字符串1
		//1为角色登陆成功  2为角色创建  3为角色升级。
				        role_CTime = roleCTime+"000";
				        if(roleCTime.equals("")){
				        	role_CTime = "1480747870001";
				        }
				        Log.i("tag", "roleCTime = "+roleCTime);
				        Log.i("tag", "role_CTime = "+role_CTime);
		if (Integer.parseInt(ext) == 1){
			downjoy.submitGameRoleData(zoneId, zoneName, roleId, roleName, Long.parseLong(role_CTime), Long.parseLong(role_CTime), roleLevel,1, new ResultListener() {
				@Override
				public void onResult(Object result) {
					//上传角色结果
					String resultStr = (String) result;
					if (resultStr.equals("true")){
						//提交角色成功
						Yayalog.loger("提交角色成功");
					}
				}
			});
		}else if (Integer.parseInt(ext) == 2){
			downjoy.submitGameRoleData(zoneId, zoneName, roleId, roleName, Long.parseLong(role_CTime), Long.parseLong(role_CTime), roleLevel, 2, new ResultListener() {
				@Override
				public void onResult(Object result) {
					//上传角色结果
					String resultStr = (String) result;
					if (resultStr.equals("true")){
						//提交角色成功
						Yayalog.loger("提交角色成功");
					}
				}
			});
		}else if(Integer.parseInt(ext) == 3){
			downjoy.submitGameRoleData(zoneId, zoneName, roleId, roleName, Long.parseLong(role_CTime), Long.parseLong(role_CTime), roleLevel, 3, new ResultListener() {
				@Override
				public void onResult(Object result) {
					//上传角色结果
					String resultStr = (String) result;
					if (resultStr.equals("true")){
						//提交角色成功
						Yayalog.loger("提交角色成功");
					}
				}
			});
		}
	}

	public static void onResume(Activity paramActivity) {
		if ( downjoy !=  null) {
			downjoy.resume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		if ( downjoy !=  null) {
			downjoy.pause();
		}
	}

	public static void onDestroy(Activity paramActivity) {
		if (downjoy != null) {
			downjoy.destroy();
			downjoy = null;
		}
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		if (downjoy != null) {
            downjoy.onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
        }
	}

	public static void onNewIntent(Intent paramIntent) {
		 if (downjoy != null) {
	            downjoy.onNewIntent(mActivity, paramIntent);
	        }
	}

	public static void onStart(Activity paramActivity) {
		if (downjoy != null) {
            downjoy.onStart(paramActivity);
        }
	}

	public static void onRestart(Activity paramActivity) {
		if (downjoy != null) {
            downjoy.onRestart(paramActivity);
        }
	}

	public static void onCreate(Activity paramActivity) {
		if (downjoy != null) {
            downjoy.onCreate(paramActivity);
        }
		if (downjoy != null) {
            downjoy.onStart(paramActivity);
        }
	}

	public static void onStop(Activity paramActivity) {
		if (downjoy != null) {
            downjoy.onStop(paramActivity);
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
	/**
	 * 初始化当乐代码
	 */
	private static void initDownjoy() {
		// 崩溃日志：如果你们没拦截的话，那么会在SDK卡downjoy/crashlog目录下生成崩溃日志
		// 获取当乐实例，获取为NULL时，请确定自己是否有预先进入SdkLoadActivity界面。
		// 请参考AndroidMainfest的配置，设置此Activity为启动Activity，删除游戏的原有启动Activity。
		downjoy = Downjoy.getInstance();

		// 设置登录成功后是否显示当乐的悬浮按钮
		// 如果此处设置为false，登录成功后，不显示当乐游戏中心的悬浮按钮。
		// 注意：
		// 此处应在调用登录接口之前设置，默认值是true，即登录成功后自动显示当乐游戏中心的悬浮按钮。
		// 正常使用悬浮按钮还需要实现两个函数,onResume、onPause
		downjoy.showDownjoyIconAfterLogined(true);
		//设置悬浮窗显示位置，在服务器设置了悬浮窗位置时，此设置失效
		downjoy.setInitLocation(Downjoy.LOCATION_LEFT_CENTER_VERTICAL);
		//设置全局注销监听器，浮标中的注销也能接收到回调
		downjoy.setLogoutListener(mLogoutListener);

		isinit = true;
		// 调用登录对话框
		//		login(mActivity);
	}

	/**
	 * 登出回调
	 */
	private static LogoutListener mLogoutListener = new LogoutListener() {
		@Override
		public void onLogoutSuccess() {
			Log.i("tag","注销成功回调->注销成功");
			//            downjoyLogout();
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loginOut();
				}
			});
		}

		@Override
		public void onLogoutError(String msg) {
			Log.i("tag","注销失败回调->");
		}
	};

	/**
	 * 登录
	 */
	private static void downjoyLogin(final Activity mactivity) {
		if (null == downjoy){
			return;
		}
		downjoy.openLoginDialog(mactivity, new CallbackListener<LoginInfo>() {

			@Override
			public void callback(int status, LoginInfo data) {
				if (status == CallbackStatus.SUCCESS && data != null) {
					//当乐提供的openid，用户唯一标识
					uid = data.getUmid();
					String username = data.getUserName();
					String nickname = data.getNickName();
					Log.i("tag","uid = "+uid);
					Log.i("tag","username = "+username);
					Log.i("tag","nickname = "+nickname);

					//本次登录生成的token
					//必接，必须校验,具体看服务器端文档
					token = data.getToken();
					Log.i("tag","token = "+token);
					loginSuce(mactivity, uid, uid, token);
					Toast("登录成功");
					Log.i("tag","登录成功");
				} else if (status == CallbackStatus.FAIL && data != null) {
					loginFail();
					Toast("登录失败");
				} else if (status == CallbackStatus.CANCEL && data != null) {
					loginFail();
					Toast("登录取消");
				}
			}
		});
	}
}
