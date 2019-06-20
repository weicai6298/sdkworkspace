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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.BDGameSDKSetting;
import com.baidu.gamesdk.OnGameExitListener;
import com.baidu.gamesdk.BDGameSDKSetting.Domain;
import com.baidu.gamesdk.BDGameSDKSetting.Orientation;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.ResultCode;
import com.baidu.platformsdk.PayOrderInfo;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static int islogin = 0;

	public static String uid;
	public static String token;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		BDGameSDKSetting mBDGameSDKSetting = new BDGameSDKSetting();
		String BD_appid = DeviceUtil.getGameInfo(mActivity, "BD_appid");
		String BD_appkey = DeviceUtil.getGameInfo(mActivity, "BD_appkey");
		mBDGameSDKSetting.setAppID(Integer.parseInt(BD_appid)); // APPID设置
		mBDGameSDKSetting.setAppKey(BD_appkey); // APPKEY设置
		mBDGameSDKSetting.setDomain(Domain.RELEASE); // 设置为正式模式
		mBDGameSDKSetting.setOrientation(DeviceUtil.isLandscape(mActivity)?Orientation.LANDSCAPE:Orientation.PORTRAIT);
        
		BDGameSDK.init(mactivity, mBDGameSDKSetting, new IResponse<Void>() {

			@Override
			public void onResponse(int resultCode, String resultDesc,
					Void extraData) {
				switch (resultCode) {
				case ResultCode.INIT_SUCCESS:
					// 初始化成功
					isinit = true;
					Log.i("tag","初始化成功");
					BDGameSDK.getAnnouncementInfo(mactivity);
					if(islogin == 0){ //0 未登录
								login(mactivity);
					}
					break;

				case ResultCode.INIT_FAIL:
				default:
					Log.i("tag","初始化失败");
					mActivity.finish();
					// 初始化失败
				}

			}
		});
		BDGameSDK.closeFloatView(mactivity); // 关闭悬浮窗

		setSuspendWindowChangeAccountListener(); // 设置切换账号事件监听（个人中心界面切换账号）
		//		
		setSessionInvalidListener(); // 设置会话失效监听

	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {

	}

	/**
	 * 登录
	 */
	public static void login(Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		//		if((isinit) && (!BDGameSDK.isLogined())){
		//		Log.i("tag","islogin == " +islogin);
		if(isinit && islogin==0 && YYWMain.mUserCallBack != null){
			Log.i("tag","YYWMain.mUserCallBack = "+YYWMain.mUserCallBack);
			BDGameSDK.login(mActivity, new IResponse<Void>() {
				@Override
				public void onResponse(int resultCode, String resultDesc,
						Void extraData) {
					Log.d("login", "this resultCode is " + resultCode);
					switch (resultCode) {
					case ResultCode.LOGIN_SUCCESS:
						uid = BDGameSDK.getLoginUid();
						token = BDGameSDK.getLoginAccessToken();
						loginSuce(mActivity, uid, uid, token);
						Toast("登录成功");
						islogin = 1;
						BDGameSDK.showFloatView(mActivity); // 显示悬浮窗
						break;
					case ResultCode.LOGIN_CANCEL:
						loginFail();
						Toast("登录取消");
						break;
					case ResultCode.LOGIN_FAIL:
						loginFail();
						Toast("登录失败");
					default:
						loginFail();
						Toast("登录失败");
					}
				}
			});
		}
//		else{
//			inintsdk(mactivity);
//		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		PayOrderInfo payOrderInfo = new PayOrderInfo();
		payOrderInfo.setCooperatorOrderSerial(morderid);
		payOrderInfo.setProductName(YYWMain.mOrder.goods);
		payOrderInfo.setTotalPriceCent(YYWMain.mOrder.money);
		payOrderInfo.setRatio(1); //兑换比例，此时不生效 
		payOrderInfo.setExtInfo("购买"+YYWMain.mOrder.goods);//该字段在支付通知中原样返回,不超过 500 个字符
		Log.i("tag","支付uid="+uid);
		payOrderInfo.setCpUid(uid);//登录成功后获取的 uid
		BDGameSDK.pay(mactivity, payOrderInfo, null, new IResponse<PayOrderInfo>() {

			@Override
			public void onResponse(int
					resultCode, String resultDesc,PayOrderInfo
					extraData) {
				switch (resultCode) {
				case ResultCode.PAY_SUCCESS: // 支付成功
					paySuce();
					Toast("支付成功");
					break;
				case ResultCode.PAY_CANCEL: // 订单支付取消
					payFail();
					Toast("取消支付");
					break;
				case ResultCode.PAY_FAIL: // 订单支付失败
					payFail();
					Toast("支付失败");
					Log.i("tag","支付失败：" + resultDesc);
					break;
				case ResultCode.PAY_SUBMIT_ORDER: // 订单已经提交，支付结果未知（比如：已经请求了，但是查询超时）
					payFail();
					Toast("订单已经提交，支付结果未知");
					break;
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

		BDGameSDK.gameExit(paramActivity, new OnGameExitListener() {

			@Override
			public void onGameExit() {
				BDGameSDK.closeFloatView(paramActivity); // 关闭悬浮窗
				islogin = 0;
				callback.onExit();
				System.exit(0);
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
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
	}

	public static void onResume(Activity paramActivity) {
		BDGameSDK.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		BDGameSDK.onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

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
	private static void setSuspendWindowChangeAccountListener() {
		BDGameSDK.setSuspendWindowChangeAccountListener(mActivity, new IResponse<Void>() {

			@Override
			public void onResponse(int resultCode, String resultDesc, Void extraData) {
				switch ( resultCode){ 
				case ResultCode.LOGIN_SUCCESS :
					//  登录成功，不管之前是什么登录状态，游戏内部都要切换成新的用户
					//					uid = BDGameSDK.getLoginUid();
					//					token = BDGameSDK.getLoginAccessToken();
					//					loginSuce(mActivity, uid, uid, token);
					//					Toast("切换用户成功");
					//					islogin = true;
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							loginOut();
							islogin = 0;
						}
					});
					//					BDGameSDK.showFloatView(mActivity); // 显示悬浮窗
					Log.i("tag","切换用户成功");
					break ; 
				case ResultCode. LOGIN_FAIL :
					//  登录失败，游戏内部之前如果是已经登录的，要清楚自己记录的登录状 
					loginFail();
					//					Toast("切换用户失败");
					Log.i("tag","切换用户失败");
					break ; 
				case ResultCode. LOGIN_CANCEL :
					//  操作前后的登录状态没变化
					loginFail();
					//					Toast("切换用户取消");
					Log.i("tag","切换用户取消");
					break ;
				}
			}
		});
	}

	/**
	 * @Description: 监听session失效时重新登录
	 */
	private static void setSessionInvalidListener() {
		BDGameSDK.setSessionInvalidListener(new IResponse<Void>() {

			@Override
			public void onResponse(int resultCode, String resultDesc,
					Void extraData) {
				Log.i("tag","监听session失效时重新登录");
				BDGameSDK.showFloatView(mActivity); // 显示悬浮窗
				if (resultCode == ResultCode.SESSION_INVALID) {
					// 会话失效，开发者需要重新登录或者重启游戏
					loginOut();
					islogin = 0;
				}

			}

		});
	}

}
