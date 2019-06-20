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
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.xmwsdk.asynchttp.AsyncHttpConnection;
import com.xmwsdk.asynchttp.support.ParamsWrapper;
import com.xmwsdk.control.XmwMatrix;
import com.xmwsdk.inface.XmwIDispatcherCallback;
import com.xmwsdk.model.GameRoleInfo;
import com.xmwsdk.model.PayInfo;
import com.xmwsdk.view.XmwProgressDialog;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;
	
	public static YYWExitCallback bf_exitcallback;

	private static boolean isinit = false;
	
	public static Boolean island;
	
	public static String token;
	
	public static String zone_id = "123";
	
	public static String zone_name = "123";
	
	public static String role_id = "123";
	
	public static String role_name = "123";
	
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		island = DeviceUtil.isLandscape(mActivity)?true:false;
		XmwMatrix.getInstance().initxmw(mactivity,island);
		
XmwMatrix.getInstance().setChangeUserCallBack(new XmwIDispatcherCallback() {
			
			@Override
			public void onFinished(int error_code, String data) {
				//游戏方自行处理登出游戏并回到sdk登录界面
				Log.i("tag : ", "sdk已登出 = error_code : "+error_code+":  data : " + data);
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
			XmwMatrix.getInstance().invokeLogin(mactivity, logincallback, island);// 无背景登陆
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid, String serial) {
		Yayalog.loger("YaYawanconstantssdk支付");
		PayInfo payinfo = new PayInfo();
		// 服务器返回的熊猫玩消费订单
		payinfo.setPurchase_serial(serial);
		// 金额
		payinfo.setAmount(YYWMain.mOrder.money/100+"");
		// 商品名称
		payinfo.setApp_subject(YYWMain.mOrder.goods);
//		// 商品描述
//		payinfo.setApp_description(YYWMain.mOrder.goods);
//		// 订单号
//		payinfo.setApp_order_id(morderid);
//		// 游戏内用户id
//		payinfo.setApp_user_id(role_id);

		XmwMatrix.getInstance().invokePay(mactivity, paycallback, payinfo);
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		bf_exitcallback = callback;
		XmwMatrix.getInstance().exitXMW(paramActivity, exitcallback);
	}

	/** 
	 * 设置角色信息
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);

		//1为角色登陆成功  2为角色创建  3为角色升级
		zone_id = zoneId;
		zone_name = zoneName;
		role_id = roleId;
		role_name = roleName;
		GameRoleInfo info = new GameRoleInfo();
		info.setAccountid(roleId);
		info.setServerid(zoneId);
		info.setServername(zoneName);
		info.setRolename(roleName);
		info.setRolelv(roleLevel);
		info.setRolevip("0级");
		
		if(Integer.parseInt(ext) == 1){
			XmwMatrix.getInstance().setGameRole(info);
		}else if(Integer.parseInt(ext) == 2){
			
		}else if(Integer.parseInt(ext) == 3){
			XmwMatrix.getInstance().upDataRole(info);
		}
	}

	public static void onResume(Activity paramActivity) {
		XmwMatrix.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		XmwMatrix.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		XmwMatrix.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {
		XmwMatrix.getInstance().onActivityResult(paramInt1,paramInt2,paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		XmwMatrix.getInstance().onNewIntent(paramIntent);
	}
	

	public static void onStart(Activity paramActivity) {
	}

	public static void onRestart(Activity paramActivity) {
		XmwMatrix.getInstance().onRestart(paramActivity);


	}

	public static void onCreate(Activity paramActivity) {
		XmwMatrix.getInstance().onCreate(paramActivity);
		XmwMatrix.getInstance().onStart(paramActivity);
	}

	public static void onStop(Activity paramActivity) {
		XmwMatrix.getInstance().onStop(paramActivity);
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
	
	// 初始化回调
		public XmwIDispatcherCallback initcallback = new XmwIDispatcherCallback() {
			@Override
			public void onFinished(int code, String data) {
				
			if (code == 0) {
					// 初始化 成功
					Log.i("tag","初始化 成功 data = "+data);
					isinit = true;
				}else {
					//初始化 异常
					Log.i("tag","初始化 异常 data = "+data);
				}
			}
		};
		// 退出、切换账号 回调
		public static XmwIDispatcherCallback exitcallback = new XmwIDispatcherCallback() {
			@Override
			public void onFinished(int code, String data) {
				
				Log.i("tag","退出、切换账号 回调"+"code:"+code+"data:"+data);
			if (code == 0) {
					// 退出
					bf_exitcallback.onExit();
				}else if (code == 1) {
					// 切换账号
//					doLogin(island);
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							loginOut();
							Log.i("tag","切换账号");
						}
					});
				}else {
					//取消退出
					System.out.println("code:"+code+"data:"+data);
				}
			}
		};
		// 登出回调
		public XmwIDispatcherCallback logoutcallback = new XmwIDispatcherCallback() {
			@Override
			public void onFinished(int code, String data) {
				Log.i("tag","登出回调"+"code:"+code+"data:"+data);
				if (code == 0) {
					// 登出 成功
					System.out.println("data:"+data);
//					doLogin(island);
					//System.exit(0);
mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							loginOut();
							Log.i("tag","切换账号");
						}
					});
				}else {
					//登出 异常
					System.out.println("data:"+data);
				}
			}
		};
		
		// 登录回调
		public static XmwIDispatcherCallback logincallback = new XmwIDispatcherCallback() {

			@Override
			public void onFinished(int error_code, String data) {
				if (error_code == 99) {
					// 用户取消登录
					loginFail();
					return; 
				}
				try {
					JSONObject json = new JSONObject(data);
					String auth_code = json.optString("authorization_code", "");
					Log.i("tag","auth_code = " +auth_code);
					HttpPost(auth_code);
//					if (!"".equalsIgnoreCase(auth_code)) {
//						getAccessToken(auth_code);
//					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		
		// 支付回调
		public static XmwIDispatcherCallback paycallback = new XmwIDispatcherCallback() {
			@Override
			public void onFinished(int code, String data) {
				System.out.println("code:" + code + " data:" + data);
				Log.e("XMW_TAG", "code:" + code + " data:" + data);
				if (code == 99) {
					// 用户取消支付或支付失败
					payFail();
					return;
				} else if (code == 1) {
					// 支付正在处理，充值卡重置可能比较慢 支付完成后会向服务器返回充值结果
					payFail();
					return;
				} else if (code == 0) {
					// 支付成功
//					Message msg = new Message();
//					msg.what = 3;
//					msg.obj = "支付成功！";
//					handler.sendMessageDelayed(msg, 1000);
					paySuce();
					return;
				}
			}
		};
		
		private static void HttpPost(final String sid) {
			HttpUtils httpUtil = new HttpUtils();
			String url = "https://api.sdk.75757.com/data/get_uid/";
			RequestParams requestParams = new RequestParams();
			requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
			requestParams.addBodyParameter("code", sid);
			Log.i("tag","app_id = " +DeviceUtil.getAppid(mActivity));
			Log.i("tag","code = " +sid);
			httpUtil.send(HttpMethod.POST, url,requestParams,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Yayalog.loger("请求失败"+arg1.toString());
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							try {
								Yayalog.loger("请求成功"+arg0.result);
								JSONObject obj = new JSONObject(arg0.result);
								String uid = obj.getString("xmw_open_id");
								token = obj.getString("access_token");
								Yayalog.loger("uid ="+uid);
								Yayalog.loger("token ="+token);
								loginSuce(mActivity, uid, uid, token);
								Toast("登录成功");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
}
