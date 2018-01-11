package com.yayawan.impl;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.CPCallBackMgr.MatrixCallBack;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static boolean landscape;

	public static String mAccessToken;

	public static String orderId;
	
	private static int isyoumeng;
	
	private static String dengluuid;
	
	private static String role_Id = "1";
	private static String role_Name = "1";
	private static String role_Level = "1";
	private static String role_CTime = "1";
	private static String zone_Id = "1";
	private static String zone_Name = "1";
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Boolean isLandscape = DeviceUtil.isLandscape(mActivity)?true:false;
		Matrix.setActivity(mactivity, mSDKCallback, false);
		String youmeng = DeviceUtil.getGameInfo(mActivity, "isyoumeng");
		isyoumeng = Integer.parseInt(youmeng);
		if(isyoumeng == 1){
			UMGameAgent.setDebugMode(true);
			UMGameAgent.init(mActivity);
		}

	}
	protected static boolean getLandscape(Context context) {
		if (context == null) {
			return false;
		}
		landscape = DeviceUtil.isLandscape(mActivity)?true:false;
		return landscape;
	}
	protected static MatrixCallBack mSDKCallback = new MatrixCallBack() {
		@Override
		public void execute(Context context, int functionCode, String functionParams) {
			if (functionCode == ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT) {
				//						doSdkSwitchAccount(getLandscape(context));
			}else if (functionCode == ProtocolConfigs.FUNC_CODE_INITSUCCESS) {
				//这里返回成功之后才能调用SDK 其它接口
				// ...
			}
			/* else if (functionCode == IntegrationConfigs.FUNC_CODE_LOGOUTSUCCESS) {
						// 融合需要，和360sdk本身业务无关
						// 注销成功,调用登录接口
						doSdkLogin(getLandscape(context));
					}*/
		}

	};
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
		Yayalog.loger("YaYawanconstantssdk登录");
		Matrix.execute(mactivity,
				getLoginIntent(DeviceUtil.isLandscape(mActivity)?true:false, mactivity, false),
				new IDispatcherCallback() {
			@Override
			public void onFinished(String data) {
				if (isCancelLogin(data)) {
					System.out.println("登陆返回");
					YYWMain.mUserCallBack.onLoginFailed(null,
							null);
					return;
				}

				// 显示一下登录结果
				// 解析access_token
				mAccessToken = parseAccessTokenFromLoginResult(data);

				if (!TextUtils.isEmpty(mAccessToken)) {
					getuserinfo(mactivity, mAccessToken);
				} else {
					YYWMain.mUserCallBack.onLoginFailed(null,
							null);
				}

			}

		});
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		orderId = morderid;
		Intent intent = getPayIntent(mactivity);

		// 必需参数，使用360SDK的支付模块。
		intent.putExtra(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_PAY);
		Matrix.invokeActivity(mactivity, intent, mPayCallback);
	}

	protected static Intent getPayIntent(Activity mactivity) {


		Bundle bundle = new Bundle();
		// 界面相关参数，360SDK界面是否以横屏显示。
		bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE,
				DeviceUtil.isLandscape(mactivity));

		String[] userids=YYWMain.mUser.uid.split("-");
		String userid=userids[1];
		// 必需参数，360账号id。
		bundle.putString(ProtocolKeys.QIHOO_USER_ID,userid);
		System.out.println("userid"+userid);
		// 必需参数，用户access token，要使用注意过期和刷新问题，最大64字符。 
		bundle.putString(ProtocolKeys.ACCESS_TOKEN, mAccessToken);

		bundle.putString(ProtocolKeys.AMOUNT, YYWMain.mOrder.money + "");
		// 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
		bundle.putString(ProtocolKeys.PRODUCT_NAME, YYWMain.mOrder.goods);

		// 必需参数，购买商品的商品id，应用指定，最大16字符。
		bundle.putString(ProtocolKeys.PRODUCT_ID, orderId);
		bundle.putString(ProtocolKeys.NOTIFY_URI,
				DeviceUtil.getGameInfo(mactivity, "callback"));
		bundle.putString(ProtocolKeys.APP_NAME,
				DeviceUtil.getGameInfo(mactivity, "gamename"));
		bundle.putString(ProtocolKeys.APP_USER_NAME, YYWMain.mUser.userName);
		bundle.putString(ProtocolKeys.APP_USER_ID, YYWMain.mUser.uid);
		bundle.putString(ProtocolKeys.APP_ORDER_ID, orderId);
		Intent intent = new Intent(mactivity, ContainerActivity.class);
		intent.putExtras(bundle);
		return intent;

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

		Bundle bundle = new Bundle();

		// 界面相关参数，360SDK界面是否以横屏显示。
		bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, landscape);

		// 可选参数，登录界面的背景图片路径，必须是本地图片路径
		bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");

		// 必需参数，使用360SDK的退出模块。
		bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);

		Intent intent = new Intent(mActivity, ContainerActivity.class);
		intent.putExtras(bundle);

		Matrix.invokeActivity(mActivity, intent, new IDispatcherCallback() {

			@Override
			public void onFinished(String arg0) {
				if (arg0.contains("2")) {
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(isyoumeng == 1){
								Log.i("tag", "友盟退出");
								MobclickAgent.onProfileSignOff();
								MobclickAgent.onKillProcess(mActivity);
							}
							doSdkGetUserInfoByCP("exitServer");
							callback.onExit();
						}
					});
				}


			}
		}); 

	}

	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		role_Id =roleId;
		role_Name = roleName;
		role_Level = roleLevel;
		role_CTime = roleCTime;
		zone_Id = zoneId;
		zone_Name = zoneName;
		 //1为角色登陆成功  2为角色创建  3为角色升级。
		if (Integer.parseInt(ext) == 1){
			if(isyoumeng ==1){
				Log.i("tag", "友盟进入游戏");
				MobclickAgent.onProfileSignIn(dengluuid);
			}
			doSdkGetUserInfoByCP("enterServer");
		}else if (Integer.parseInt(ext) == 2){
			doSdkGetUserInfoByCP("createRole");
		}else if(Integer.parseInt(ext) == 3){
			doSdkGetUserInfoByCP("levelUp");
		}
		
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		if(isyoumeng == 1){
			MobclickAgent.onResume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		if(isyoumeng == 1){
			MobclickAgent.onPause(paramActivity);
		}
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
		Matrix.destroy(paramActivity); 
	}

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub

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


	/**
	 * 生成调用360SDK登录接口的Intent
	 * 
	 * @param isLandScape
	 *            是否横屏
	 * @return intent
	 * 
	 *         isswich 是否切换账号
	 */
	public static Intent getLoginIntent(boolean isLandScape,
			Activity mActivity, boolean isswich) {

		Intent intent = new Intent(mActivity, ContainerActivity.class);

		// 界面相关参数，360SDK界面是否以横屏显示。
		intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, DeviceUtil.isLandscape(mActivity));

		// 必需参数，使用360SDK的登录模块。
		intent.putExtra(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_LOGIN);

		if (isswich) {
			intent.putExtra(ProtocolKeys.FUNCTION_CODE,
					ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT);
		}

		// 是否显示关闭按钮
		intent.putExtra(ProtocolKeys.IS_LOGIN_SHOW_CLOSE_ICON, false);

		// -- 以下参数仅仅针对自动登录过程的控制
		// 可选参数，自动登录过程中是否不展示任何UI，默认展示。
		// intent.putExtra(ProtocolKeys.IS_AUTOLOGIN_NOUI,
		// getCheckBoxBoolean(R.id.isAutoLoginHideUI));

		// 可选参数，静默自动登录失败后是否显示登录窗口，默认不显示
		// intent.putExtra(ProtocolKeys.IS_SHOW_LOGINDLG_ONFAILED_AUTOLOGIN,
		// getCheckBoxBoolean(R.id.isShowDlgOnFailedAutoLogin));
		// 测试参数，发布时要去掉
		//intent.putExtra(ProtocolKeys.IS_SOCIAL_SHARE_DEBUG, true);

		return intent;
	}
	public static boolean isCancelLogin(String data) {
		try {
			JSONObject joData = new JSONObject(data);
			int errno = joData.optInt("errno", -1);
			if (-1 == errno) {
				// Toast.makeText(mActivity, data, Toast.LENGTH_LONG).show();
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
	public static String parseAccessTokenFromLoginResult(String loginRes) {
		try {

			JSONObject joRes = new JSONObject(loginRes);
			JSONObject joData = joRes.getJSONObject("data");
			return joData.getString("access_token");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void getuserinfo(final Activity mactivity, final String token) {
		// TODO Auto-generated method stub
		Yibuhttputils yibuhttputils = new Yibuhttputils() {

			@Override
			public void sucee(String str) {
				// TODO Auto-generated method stub
				System.out.println(str);

				try {
					JSONObject object = new JSONObject(str);
					//object.getLong(name)
					Log.i("tag","object="+object);
					String id = object.isNull("id") ? null : object
							.getLong("id")+"";
					String name = object.isNull("name") ? null : object
							.getString("name");

					YYWMain.mUser = new YYWUser();
					String app_id = ""+DeviceUtil.getGameInfo(mactivity, "app_id");
					String uid = app_id.substring(2, app_id.length());
					Log.i("tag","uid="+uid);
					YYWMain.mUser.uid =uid + "-"+ id + "";
					dengluuid = YYWMain.mUser.uid;
					YYWMain.mUser.userName =uid + "-" +name + "";
					// YYWMain.mUser.nick = data.getNickName();
					YYWMain.mUser.token = JSONUtil.formatToken(mactivity,
							token, YYWMain.mUser.userName);
					if (YYWMain.mUserCallBack != null) {
						YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser,
								"success");
						Handle.login_handler(mactivity, YYWMain.mUser.uid,
								YYWMain.mUser.userName);
					}

					//doSdkAntiAddictionQuery(YYWMain.mUser.token, YYWMain.mUser.uid, mactivity);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					YYWMain.mUserCallBack.onLoginFailed(null, null);
				}

			}

			@Override
			public void faile(String err, String rescode) {
				// TODO Auto-generated method stub
				YYWMain.mUserCallBack.onLoginFailed(null, null);
			}
		};
		yibuhttputils.runHttp("https://openapi.360.cn/user/me?access_token="
				+ token, "", "GET", "");
	}

	/**
	 * 使用360SDK的切换账号接口
	 *
	 * @param isLandScape 是否横屏显示登录界面
	 */
	//    protected void doSdkSwitchAccount(boolean isLandScape) {
	//        Intent intent = getSwitchAccountIntent(isLandScape);
	//        IDispatcherCallback callback = mAccountSwitchCallback;
	////        if (getCheckBoxBoolean(R.id.isSupportOffline)) {
	//            callback = mAccountSwitchSupportOfflineCB;
	////        }
	//        Matrix.invokeActivity(this, intent, callback);
	//    }

	// 支付的回调
	protected static IDispatcherCallback mPayCallback = new IDispatcherCallback() {

		@Override
		public void onFinished(String data) {
			// Log.d(TAG, "mPayCallback, data is " + data);
			if (TextUtils.isEmpty(data)) {
				return;
			}

			boolean isCallbackParseOk = false;
			JSONObject jsonRes;
			try {
				jsonRes = new JSONObject(data);
				// error_code 状态码： 0 支付成功， -1 支付取消， 1 支付失败， -2 支付进行中,
				// 4010201和4009911 登录状态已失效，引导用户重新登录
				// error_msg 状态描述
				int errorCode = jsonRes.optInt("error_code");
				System.out.println(errorCode);
				isCallbackParseOk = true;
				switch (errorCode) {
				case 0:
					//						if (YYWMain.mPayCallBack != null) {
					//							YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser,
					//									YYWMain.mOrder, "success");
					//						}
					Log.i("tag", "支付成功");
					paySuce();
					Log.i("tag", "支付成功1");
					break;

				case 1:

					//						if (YYWMain.mPayCallBack != null) {
					//							YYWMain.mPayCallBack.onPayFailed(null, null);
					//						}
					Log.i("tag", "支付失败1");
					payFail();
					Log.i("tag", "支付失败1");
					break;

				case -1:
					//						if (YYWMain.mPayCallBack != null) {
					//							YYWMain.mPayCallBack.onPayCancel(null, null);
					//						}
					Log.i("tag", "支付失败1");
					payFail();
					Log.i("tag", "支付失败1");
					break;

				case -2:
					//						Myconstant.jfToast("正在支付中...稍后确认支付结果~", mactivity);
					tishi("正在支付中...稍后确认支付结果~");
					if (YYWMain.mPayCallBack != null) {
						YYWMain.mPayCallBack.onPayCancel(null, null);
					}

					break;
				case 4010201:
					//						Myconstant.jfToast("登陆过期.请退出重新登录", mactivity);
					tishi("登陆过期.请退出重新登录");
					break;
				case 4009911:
					//						Myconstant.jfToast("登陆过期.请退出重新登录", mactivity);
					tishi("登陆过期.请退出重新登录");
					break;
				default:
					if (YYWMain.mPayCallBack != null) {
						YYWMain.mPayCallBack.onPayCancel(null, null);
					}
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	public static void tishi(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void doSdkAntiAddictionQuery(String accessToken,
			String qihooUserId, final Activity mactivity) {
		Bundle bundle = new Bundle();
		// 必需参数，用户access token，要使用注意过期和刷新问题，最大64字符。
		bundle.putString(ProtocolKeys.ACCESS_TOKEN, accessToken);
		// 必需参数，360账号id。
		bundle.putString(ProtocolKeys.QIHOO_USER_ID, qihooUserId);
		// 必需参数，使用360SDK的防沉迷查询模块。
		bundle.putInt(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_ANTI_ADDICTION_QUERY);
		Intent intent = new Intent(mactivity, ContainerActivity.class);
		intent.putExtras(bundle);
		Matrix.execute(mactivity, intent, new IDispatcherCallback() {

			@Override
			public void onFinished(String data) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(data)) {
					try {
						JSONObject resultJson = new JSONObject(data);
						int errorCode = resultJson.optInt("error_code");
						if (errorCode == 0) {
							JSONObject contentData = resultJson
									.getJSONObject("content");
							if (contentData != null) {
								// 保存登录成功的用户名及密码
								JSONArray retData = contentData
										.getJSONArray("ret");
								if (retData != null && retData.length() > 0) {
									int status = retData.getJSONObject(0)
											.optInt("status");
									switch (status) {
									case 0:
										System.out.println("无结果");
										// 查询结果:无此用户数据
										doSdkRealNameRegister(
												YYWMain.mUser.uid, mactivity);
										break;
									case 1:
										System.out.println("未成年人");
										// 查询结果:未成年
										doSdkRealNameRegister(
												YYWMain.mUser.uid, mactivity);

										break;
									case 2:
										System.out.println("成年人");
										// 查询结果:已成年
										break;
									default:
										break;
									}
									return;
								}
							}
						} else {
							return;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				//
			}
		});

	}
	/**
	 * 使用360SDK实名注册接口
	 * 
	 * @param isLandScape
	 *            是否横屏显示登录界面
	 * @param qihooUserId
	 *            奇虎360用户ID
	 */
	protected static void doSdkRealNameRegister(String qihooUserId, Activity mactivity) {
		Bundle bundle = new Bundle();
		// 界面相关参数，360SDK界面是否以横屏显示。
		bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE,
				DeviceUtil.isLandscape(mactivity));
		// 必需参数，360账号id。
		bundle.putString(ProtocolKeys.QIHOO_USER_ID, qihooUserId);
		// 可选参数，登录界面的背景图片路径，必须是本地图片路径
		bundle.putString(ProtocolKeys.UI_BACKGROUND_PICTRUE, "");
		// 必需参数，使用360SDK的实名注册模块。
		bundle.putInt(ProtocolKeys.FUNCTION_CODE,
				ProtocolConfigs.FUNC_CODE_REAL_NAME_REGISTER);
		Intent intent = new Intent(mactivity, ContainerActivity.class);
		intent.putExtras(bundle);
		Matrix.invokeActivity(mactivity, intent, new IDispatcherCallback() {
			@Override
			public void onFinished(String data) {
				System.out.println(data);
			}
		});

	}
	
	/**
     * 角色信息采集接口
     */
    public static void doSdkGetUserInfoByCP(String type) {
    	
    	//----------------------------模拟数据------------------------------
    	//帐号余额
        JSONArray balancelist = new JSONArray();
        JSONObject balance1 = new JSONObject();
        JSONObject balance2 = new JSONObject();

        //好友关系
        JSONArray friendlist = new JSONArray();
        JSONObject friend1 = new JSONObject();
        JSONObject friend2 = new JSONObject();

        //排行榜列表
        JSONArray ranklist = new JSONArray();
        JSONObject rank1 = new JSONObject();
        JSONObject rank2 = new JSONObject();

        try {
            balance1.put("balanceid","1");
            balance1.put("balancename","bname1");
            balance1.put("balancenum","200");
            balance2.put("balanceid","2");
            balance2.put("balancename","bname2");
            balance2.put("balancenum","300");
            balancelist.put(balance1).put(balance2);

            friend1.put("roleid","1");
            friend1.put("intimacy","0");
            friend1.put("nexusid","300");
            friend1.put("nexusname","情侣");
            friend2.put("roleid","2");
            friend2.put("intimacy","0");
            friend2.put("nexusid","600");
            friend2.put("nexusname","情侣");
            friendlist.put(friend1).put(friend2);

            rank1.put("listid","1");
            rank1.put("listname","listname1");
            rank1.put("num","num1");
            rank1.put("coin","coin1");
            rank1.put("cost","cost1");
            rank2.put("listid","2");
            rank2.put("listname","listname2");
            rank2.put("num","num2");
            rank2.put("coin","coin2");
            rank2.put("cost","cost2");
            ranklist.put(rank1).put(rank2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> eventParams=new HashMap<String, String>();

        eventParams.put("type",type);  //（必填）角色状态（enterServer（登录），levelUp（升级），createRole（创建角色），exitServer（退出））
        eventParams.put("zoneid",zone_Id);  //（必填）游戏区服ID
        eventParams.put("zonename",zone_Name);  //（必填）游戏区服名称
        eventParams.put("roleid",role_Id);  //（必填）玩家角色ID
        eventParams.put("rolename",role_Name);  //（必填）玩家角色名
        eventParams.put("professionid","0");  //（必填）职业ID
        eventParams.put("profession","无");  //（必填）职业名称
        eventParams.put("gender","无");  //（必填）性别
        eventParams.put("professionroleid","0");  //（选填）职业称号ID
        eventParams.put("professionrolename","无");  //（选填）职业称号
        eventParams.put("rolelevel",role_Level);  //（必填）玩家角色等级
        eventParams.put("power","0");  //（必填）战力数值
        eventParams.put("vip","0");  //（必填）当前用户VIP等级
        eventParams.put("balance","0");  //（必填）帐号余额
        eventParams.put("partyid","0");  //（必填）所属帮派帮派ID
        eventParams.put("partyname","无");  //（必填）所属帮派名称
        eventParams.put("partyroleid","0");  //（必填）帮派称号ID
        eventParams.put("partyrolename","无");  //（必填）帮派称号名称
        eventParams.put("friendlist","无");  //（必填）好友关系
//        eventParams.put("ranking",ranklist.toString());  //（选填）排行榜列表
        //参数eventParams相关的 key、value键值对 相关具体使用说明，请参考文档。
        //----------------------------模拟数据------------------------------
    	Matrix.statEventInfo(mActivity, eventParams);
    }
}
