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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdk.login.Startlogin_dialog;
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
import com.muzhiwan.sdk.common.utils.CallbackCode;
import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwInitCallback;
import com.muzhiwan.sdk.core.callback.MzwLoignCallback;
import com.muzhiwan.sdk.core.callback.MzwPayCallback;
import com.muzhiwan.sdk.service.MzwOrder;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	private static Handler mHandler;
	private static final int MSG_INIT = 0X01;

	private static ProgressDialog pd;
	private static String uid;
	private static String token;
	


	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		Handle.active_handler(mactivity);

		pd = new ProgressDialog(mactivity);
		pd.setTitle("提示");
		pd.setMessage("正在初始化资源");
		pd.show();
		int orientation=0;
		if (DeviceUtil.isLandscape(mactivity)) {
			orientation=MzwSdkController.ORIENTATION_HORIZONTAL;
		}else {
			orientation=MzwSdkController.ORIENTATION_VERTICAL;

		}
		MzwSdkController.getInstance().init(mactivity,
				orientation, new MzwInitCallback() {


			@Override
			public void onResult(final int code, String arg1) {
				// TODO Auto-generated method stub
				mactivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (!mactivity.isFinishing() && pd.isShowing()) {
							pd.dismiss();
						}
						if (code == 1) {
							System.out.println("拇指玩初始化成功");
							isinit=true;
						} else {
							System.out.println("拇指玩初始化失败");
							isinit=false;
						}
					}
				});
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
		if(isinit){
			MzwSdkController.getInstance().doLogin(new MzwLoignCallback() {

				@Override
				public void onResult(int code, String msg) {
					Log.i("tag","msg = "+msg);
					if(code == 1){
						HttpPost(msg);
					}else if(code == 0){
						Toast("登录失败");
					}else if(code == 4){
						Toast("登录取消");
					}else if(code == 6){
						Toast("登出成功");
						loginOut();
					}									
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
	public static void pay(final Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		MzwOrder order = new MzwOrder();
		order.setMoney(Double.parseDouble(YYWMain.mOrder.money/100+""));
		order.setProductname(YYWMain.mOrder.goods);
		order.setProductdesc(YYWMain.mOrder.goods);
		order.setExtern(morderid);
		MzwSdkController.getInstance().doPay(order, new MzwPayCallback() {
			@Override
			public void onResult(int code, MzwOrder order) {
				Log.i("tag", "paycallback----code:" + code + "--------order:" + order);
				if(code == CallbackCode.SUCCESS){
					//成功
					Yayalog.loger("支付成功"+order.toString());
					paySuce();
					Toast("支付成功");
				}else if(code == CallbackCode.PREPARE){
					//支付发起状态
					Yayalog.loger("正在支付"+order.toString());
					payFail();
					Toast("支付正在确认中...");
				}else if(code == CallbackCode.CANCEL){
					Yayalog.loger("取消支付"+order.toString());
					payFail();
				}else {
					payFail();
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
		
		KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

			@Override
			public void onSuccess(User arg0, int arg1) {
				callback.onExit();
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
		//			if (android.os.Build.VERSION.SDK_INT >=23) {
		//				//检查权限，开启必要权限
		//				checkPermissions(needPermissions);
		//			}else
		//			{
		//				checkNetwork();
		//			}
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
	/**
	 * 检查权限
	 */
	//		private static void checkPermissions(String... permissions) {
	//			List<String> needRequestPermissonList = findDeniedPermissions(permissions);
	//			if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
	//				Activity.requestPermissions(mActivity, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
	//			}else
	//			{
	//				checkNetwork();
	//			}
	//		}
	/**
	 * 获取权限集中需要申请权限的列表
	 *
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 */
	//		private static List<String> findDeniedPermissions(String[] permissions) {
	//			List<String> needRequestPermissonList = new ArrayList<String>();
	//			for (String perm : permissions) {
	//				if (ContextCompat.checkSelfPermission(mActivity, perm) != PackageManager.PERMISSION_GRANTED || Activity.shouldShowRequestPermissionRationale(this, perm)) {
	//					needRequestPermissonList.add(perm);
	//				}
	//			}
	//			return needRequestPermissonList;
	//		}

	/**
	 * 检测是否有的权限都已经授权
	 *
	 * @param grantResults
	 */
	//		private boolean verifyPermissions(int[] grantResults) {
	//			for (int result : grantResults) {
	//				if (result != PackageManager.PERMISSION_GRANTED) {
	//					return false;
	//				}
	//			}
	//			return true;
	//		}
	//
	//		public void onRequestPermissionsResult(int requestCode,
	//				String[] permissions, int[] paramArrayOfInt) {
	//			if (requestCode == PERMISSON_REQUESTCODE) {
	//				if (!verifyPermissions(paramArrayOfInt)) {
	//					Toast("请先允许权限,否则会影响程序正常使用");
	//				}
	//			}
	//		}
	public static void checkNetwork() {
		// !!!在调用SDK初始化前进行网络检查
		// 当前没有拥有网络
		if (false == isNetworkAvailable(mActivity)) {
			AlertDialog.Builder ab = new AlertDialog.Builder(mActivity);
			ab.setMessage("网络未连接,请设置网络");
			ab.setPositiveButton("设置", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent("android.settings.SETTINGS");
					mActivity.startActivityForResult(intent, 0);
				}
			});
			ab.setNegativeButton("退出", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					System.exit(0);
				}
			});
			ab.show();
		} else {
			MzwSdkController.getInstance().init(mActivity, MzwSdkController.ORIENTATION_HORIZONTAL, new MzwInitCallback() {

				public void onResult(int code, String msg) {
					Message message = new Message();
					message.what = MSG_INIT;
					message.arg1 = code;
					mHandler.handleMessage(message);
				}
			});
		}
	}

	/**
	 * 检测是否有网络
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.getState() == NetworkInfo.State.CONNECTED)
			return true;
		return false;
	}
	
	/**
	 * 
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	private static void HttpPost(final String sid) {
		token = sid;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/data/get_uid/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("code", sid);
		httpUtil.send(HttpMethod.POST, url, requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Yayalog.loger("请求失败"+arg1.toString());
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						try {
							Yayalog.loger("请求成功"+arg0.result);
							JSONObject obj = new JSONObject(arg0.result);
							uid = obj.getString("uid");
							Yayalog.loger("uid ="+uid);
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
