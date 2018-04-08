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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dianyou.data.bean.CPAUserDataBean;
import com.dianyou.openapi.DYSDK;
import com.dianyou.openapi.interfaces.IExitCallback;
import com.dianyou.openapi.interfaces.ILoginCallBack;
import com.dianyou.pay.ali.DYPaySDK;
import com.dianyou.pay.listener.DYOnlinePayResultListener;
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

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		DYSDK.init(applicationContext);
		isinit = true ;

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			doLogin();
		}else{
			inintsdk(mactivity);
		}
	}

	public static void doLogin() {
		DYSDK.login().popupLoginActivity(mActivity, new ILoginCallBack() {
			@Override
			public void onSuccess(CPAUserDataBean userInfo) {
				Toast.makeText(mActivity, "cp登陆成功", Toast.LENGTH_SHORT).show();
				//登录成功触发回调
				if (userInfo != null && userInfo.data != null) {
					Log.d("dy", "token：" + userInfo.data.userCertificate);
					Log.d("dy", "userid：" + userInfo.data.userid);
					String uid = userInfo.data.userid;
					String token = userInfo.data.userCertificate;
					loginSuce(mActivity, uid,uid,token);
					Toast("登录成功");
				}
			}

			@Override
			public void onCancel(Throwable t, int errorNo, String strMsg,
					boolean alert) {
				// TODO登录对话框取消触发回调
				Log.d("dy", "onCancel：" + errorNo + ":" + strMsg);
				loginFail();
				Toast("登录取消");
			}

			@Override
			public void onSwitchAccount() {
				// TODO当前登录用户已退出,应将“游戏场景”切换到未登录的状态(业务场景操作流程和doLogout中的onSuccess方法回调类似).
				// 联运SDK会触发登录对话框,无须二次调用
				Log.d("dy", "切换账号成功");

				// 弹出登录框
				//				doLogin();
				loginOut();
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
		/**
		 * 商品支付交易
		 *
		 * @param context
		 *            Activity的对象引用(****必填****)
		 * @param cpOrderId
		 *            CP订单ID(****必填****)
		 * @param cpBussinessId
		 *            CP业务ID(****可空****)
		 * @param cpCallbackUrl
		 *            CP回调URL(****必填****)
		 * @param goodsName
		 *            商品名称(****必填****)
		 * @param money
		 *            商品价格(****必填,商品单价类型是double，单位为元****)
		 * @param goodsDesc
		 *            商品描述(****必填****)
		 * @param payResultListener
		 *            支付回调接口(****必填****)
		 */
		DYPaySDK.payOrder(mactivity, morderid, "",
				DeviceUtil.getGameInfo(mActivity, "callback"),
				YYWMain.mOrder.goods,YYWMain.mOrder.money/100, YYWMain.mOrder.goods, new DYOnlinePayResultListener() {
			@Override
			public void onSuccess(String remain) {
				// TODO支付成功触发回调
				paySuce();
				Toast("支付成功");
				Log.d("dy", "pay onSuccess:" + remain);
			}

			@Override
			public void onFailed(String remain) {
				//支付失败触发回调
				/**
				 * 支付失败结果remain参数说明：
				 * 6001--取消支付
				 * 8001--支付失败
				 * 301--用户未登录或失效
				 */
				payFail();
				Log.d("dy", "pay onFailed:" + remain);
				if (!TextUtils.isEmpty(remain)) {
					if ("301".equals(remain)) {
						// 弹出登录框
						doLogin();
					}
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
				// TODO Auto-generated method stub


				DYSDK.login().exit(paramActivity, true, new IExitCallback() {
					@Override
					public void onExitSdk() {
						Log.d("dy", "onExitSdk...");
						mActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								callback.onExit();
							}
						});
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
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

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
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
			}
		});
	}
	/**
	 * 
	 * 请求上报角色信息
	 * 
	 */
	private static void HttpPost(final String roleId, final String roleName,final String roleLevel, final String zoneId, final String zoneName, final String roleCTime) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/user/roleinfo/");
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("roleId", roleId));
					params.add(new BasicNameValuePair("roleName", roleName));
					params.add(new BasicNameValuePair("roleLevel", roleLevel));
					params.add(new BasicNameValuePair("zoneId", zoneId));
					params.add(new BasicNameValuePair("zoneName", zoneName));
					params.add(new BasicNameValuePair("roleCTime", roleCTime));

					Log.i("tag", "params=" + params);
					try {
						// 设置httpPost请求参数
						httpPost.setEntity(new UrlEncodedFormEntity(params,
								HTTP.UTF_8));
						HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpPost);
						Log.i("tag",
								"httpResponse.getStatusLine().getStatusCode()="
										+ httpResponse.getStatusLine()
										.getStatusCode());
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							//							String re = EntityUtils.toString(httpResponse
							//									.getEntity());
							//							Log.i("tag", "re=" + re);
							//							JSONObject js = new JSONObject(re);
							//							Log.i("tag", "js=" + js);
							//							uid = js.getString("uid");
							//							Log.i("tag", "uid=" + uid);
							//							Log.i("tag", "token=" + token);
							//							loginSuce(mActivity, uid, uid, token);
							Toast("角色上报成功");
						}

					} catch (ClientProtocolException e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
