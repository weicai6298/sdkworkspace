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

import com.android.game.paymanager.PayListener;
import com.android.game.paymanager.ReqPayManager;
import com.android.tcyw.login.LoginListener;
import com.android.tcyw.login.TcManager;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;

	private static TcManager cmanager;

	public static String role_id = "123";
	public static String role_name = "123";
	public static String role_level = "123";
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		String gameid = DeviceUtil.getGameInfo(mactivity, "tc_gameid");
		String packageid = DeviceUtil.getGameInfo(mactivity, "tc_packageid");
		String channelid = DeviceUtil.getGameInfo(mactivity, "tc_channelid");
		String signkey = DeviceUtil.getGameInfo(mactivity, "tc_signkey");
		Log.i("tag","gameid="+gameid);
		Log.i("tag","packageid="+packageid);
		Log.i("tag","channelid="+channelid);
		Log.i("tag","signkey="+signkey);
		cmanager = TcManager.getInstance(mactivity, Integer.parseInt(gameid), Integer.parseInt(packageid), Integer.parseInt(channelid), signkey, "");
		isinit = true ;
	}

	private static LoginListener listener = new LoginListener() {

		@Override
		public void login_msg(int arg0, String arg1, String arg2) {
			// TODO Auto-generated method stub

			System.out.println("SessionId: " + arg0 + " 账号: " + arg1);
			Log.i("tag","arg0 = "+arg0);
			Log.i("tag","arg1 = "+arg1);
			Log.i("tag","arg2 = "+arg2);
			loginSuce(mActivity, arg1+"", arg1+"", arg0+"");
		}
	};

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
			int screenType= DeviceUtil.isLandscape(mActivity)?2:1;
			cmanager.show(screenType, listener);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		//		AppId微信支付需要的AppID(ps:现在支付为null)， 
		//		mcode 需要生产订单的金额编号，
		//		sum对应的金额数，
		//		gcid游戏订单编号，
		//		gname游戏角色名称，
		//		glv游戏角色等级，
		//		grid游戏角色id，
		//		order_type订单类型,
		//		extend_params透传参数(注:订单类型与透传参数不为必填)
		String goods = YYWMain.mOrder.goods;
		int waresid = getWaresid(goods);
		ReqPayManager.getInstance(mactivity).sendReq(null, waresid+"", Double.parseDouble(YYWMain.mOrder.money/100+""),
				morderid,role_name, Integer.parseInt(role_level), role_id, "quick", "",new PayListener() {

			@Override
			public void payResult(int arg0, String arg1) {
				/**
				 * arg0为支付代码 1为支付成功 0为支付失败 arg1为支付信息 支付成功 取消支付 支付失败
				 * */
				Log.i("tag","支付代码arg0 = " +arg0);
				if(arg0 == 1){
					paySuce();
					Toast("支付成功");
				}else{
					payFail();
					Toast("支付失败");
				}
			}
		});

	}
	private static int getWaresid(String goods) {
		int waresid = 0 ;
		//超能特战队
		if(goods.equals("红宝石120")){
			waresid = 1;
		}else if(goods.equals("红宝石600")){
			waresid = 2;
		}else if(goods.equals("红宝石1500")){
			waresid = 3;
		}else if(goods.equals("红宝石2820")){
			waresid = 4;
		}else if(goods.equals("红宝石4560")){
			waresid = 5;
		}else if(goods.equals("红宝石7550")){
			waresid = 6;
		}else if(goods.equals("红宝石15560")){
			waresid = 7;
		}else if(goods.equals("首充礼包")){
			waresid = 8;
		}else if(goods.equals("月卡商品30天")){
			waresid = 9;
		}else if(goods.equals("关卡礼包")){
			waresid = 10;
		}else if(goods.equals("超值礼包")){
			waresid = 11;
		}else if(goods.equals("高级成长礼包")){
			waresid = 12;
		}else if(goods.equals("高级道具礼包")){
			waresid = 13;
		}else if(goods.equals("高速成长礼包")){
			waresid = 14;
		}else if(goods.equals("灵魂转生券")){
			waresid = 15;
		}else if(goods.equals("活动礼包1")){
			waresid = 16;
		}else if(goods.equals("活动礼包2")){
			waresid = 17;
		}else if(goods.equals("活动礼包3")){
			waresid = 18;
		}else if(goods.equals("活动礼包4")){
			waresid = 19;
		}else if(goods.equals("活动礼包5")){
			waresid = 20;
		}
		return waresid;
	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

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

	/** 
	 * 设置角色信息
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
		//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		role_id = roleId;
		role_name = roleName;
		role_level = roleLevel;
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {

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

}
