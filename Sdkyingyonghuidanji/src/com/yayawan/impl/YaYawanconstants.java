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
import android.util.Log;
import android.widget.Toast;
import com.appchina.usersdk.Account;
import com.appchina.usersdk.ErrorMsg;
import com.appchina.usersdk.GlobalUtils;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yyh.sdk.AccountCallback;
import com.yyh.sdk.CPInfo;
import com.yyh.sdk.LoginCallback;
import com.yyh.sdk.PayParams;
import com.yyh.sdk.PayResultCallback;
import com.yyh.sdk.YYHSDKAPI;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static CPInfo mCpInfo;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		  Log. i ( "tag" , "YaYawanconstants初始化sdk");
		 mCpInfo = new CPInfo();
		 mCpInfo.needAccount =true ;
		 mCpInfo.loginId = Integer.parseInt(DeviceUtil.getGameInfo(mActivity, "yyh_loginid"));
		 mCpInfo.loginKey = DeviceUtil.getGameInfo(mActivity, "yyh_loginkey");
		 
	        // 支付参数设置
	        mCpInfo.appid = DeviceUtil.getGameInfo(mActivity, "yyh_appid");
	        mCpInfo.privateKey = DeviceUtil.getGameInfo(mActivity, "yyh_privateKey");
	        mCpInfo.publicKey = DeviceUtil.getGameInfo(mActivity, "yyh_publicKey");
		 
	     // 横竖屏设置,只针对启动页和登陆页
	        Log. i ( "tag" , "YaYawanconstants初始化sdk12");
//					mCpInfo.orientation = DeviceUtil.isLandscape(mActivity)?CPInfo.LANDSCAPE:CPInfo.PORTRAIT;
					 Log. i ( "tag" , "YaYawanconstants初始化sdk13");
//					YYHSDKAPI.startSplash(mActivity, mCpInfo.orientation, 3000);
					 Log. i ( "tag" , "YaYawanconstants初始化sdk14");
	        
	     // SDK初始化
	        YYHSDKAPI.singleInit(mactivity, mCpInfo,  new AccountCallback() {
	        @Override
	        public  void onSwitchAccount(Account old, Account curret) {
	        // 在个人中心点击切换小号之后的回调
	        Log. i ("tag", "old:"+old.userName + "\tn current:"+curret.userName);
	        }
	        @Override
	        public  void onLogout() {
	        // 在个人中心点击退出
	        Log. i ( "tag" , "logout success");
	        loginOut();
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
			YYHSDKAPI.login(mactivity,new LoginCallback() {
				@Override
				public  void onLoginSuccess(Activity activity, Account account) {
				// 显示悬浮框
				YYHSDKAPI. showToolbar ( true);
				// 登录成功
				String uid = account.userId+"";
				String token = account.ticket;
				loginSuce(mactivity, uid, uid, token);
				GlobalUtils. showToast (activity, "登录成功");
				}
				@Override
				public  void onLoginCancel() {
				// 取消登录
					loginFail();
				GlobalUtils. showToast (mactivity, "登录取消");
				}
				@Override
				public void onLoginError(Activity arg0, ErrorMsg arg1) {
					// TODO Auto-generated method stub
					// 登录失败
					loginFail();
					GlobalUtils. showToast (mactivity, arg1.message);
					
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
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		if(YYHSDKAPI.isLogined()){
			PayParams params =  new PayParams();
			int paycode = getPaycode(YYWMain.mOrder.goods);
			params.buildWaresid(paycode)
			.buildCporderid(morderid);
			YYHSDKAPI.startPay(mactivity, params,  new PayResultCallback() {
				
				@Override
				public void onPaySuccess(int resultCode, String resultInfo) {
					paySuce();
					Log.i("tag", "支付成功="+resultCode+"---->"+resultInfo);
				}
				
				@Override
				public void onPayFaild(int resultCode, String resultInfo) {
					payFail();
					Log.i("tag", "支付失败="+resultCode+"---->"+resultInfo);
				}
			});
		}
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

	public static void onCreate(final Activity paramActivity) {
		
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
	private static int getPaycode(String goods) {
		int paycode = 0 ;
		//我的便利店
		if(goods.equals("10钻石")){
			paycode = 1;
		}else if(goods.equals("32钻石")){
			paycode = 2;
		}else if(goods.equals("57钻石")){
			paycode = 3;
		}else if(goods.equals("120钻石")){
			paycode = 4;
		}else if(goods.equals("390钻石")){
			paycode = 5;
		}else if(goods.equals("680钻石")){
			paycode = 6;
		}else if(goods.equals("1450钻石")){
			paycode = 7;
		}
		else if(goods.equals("首冲390钻石")){
			paycode = 8;
		}
		else if(goods.equals("每日钻石套餐")){
			paycode = 9;
		}
		else if(goods.equals("新手套餐")){
			paycode = 10;
		}
		else if(goods.equals("实惠套餐")){
			paycode = 11;
		}
		else if(goods.equals("高级套餐")){
			paycode = 12;
		}
		else if(goods.equals("每日特惠周一")){
			paycode = 13;
		}
		else if(goods.equals("每日特惠周二")){
			paycode = 14;
		}
		else if(goods.equals("每日特惠周三")){
			paycode = 15;
		}
		else if(goods.equals("每日特惠周四")){
			paycode = 16;
		}
		else if(goods.equals("每日特惠周五")){
			paycode = 17;
		}
		else if(goods.equals("每日特惠周六")){
			paycode = 18;
		}
		else if(goods.equals("每日特惠周日")){
			paycode = 19;
		}
		else if(goods.equals("首充礼包")){
			paycode = 20;
		}
		else if(goods.equals("新手礼包")){
			paycode = 21;
		}
		else if(goods.equals("加油礼包")){
			paycode = 22;
		}
		else if(goods.equals("进取礼包")){
			paycode = 23;
		}
		else if(goods.equals("超值礼包")){
			paycode = 24;
		}
		return paycode;
	}
}
