package com.yayawan.impl;

import java.util.HashMap;
import java.util.Random;

import android.R.integer;
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
import com.kkgame.utils.Sputils;
import com.lenovo.paysdk.IPayResultCallback;
import com.lenovo.paysdk.PayRequest;
import com.lenovo.paysdk.PaySDKApi;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		PaySDKApi.init(mActivity);
		isinit = true;
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		Log.i("tag","Application初始化");
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			String tempuid = Sputils.getSPstring("uid", "tem", mactivity);
			Log.i("tag","tempuid="+tempuid);
			if (tempuid.equals("tem")) {
				String uidtemp=System.currentTimeMillis()+"kk";
				String uid=uidtemp.substring(4, uidtemp.length())+new Random().nextInt(10);
				Sputils.putSPstring("uid", uid, mactivity);
				loginSuce(mactivity, uid, uid, uid);
			}else {
				loginSuce(mactivity, tempuid, tempuid, tempuid);
			}
		}else{
			inintsdk(mactivity);
		}

	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 * @param morderid
	 */
	public static void pay(Activity mactivity, String morderid) {
		Log.i("tag","支付发起");
		Yayalog.loger("YaYawanconstantssdk支付");
		log("pay-start");
		log("pay-start");
		/* 支付 */
		PayRequest payRequest = new PayRequest();
		// 填写商品对应的支付点,此处假定当前支付行为对应的支付点为”1”
		// “1”代表自服务平台中计费点列表里面的“计费点编号”，不！是！价！格！
		// 此项为必填项
		String goods = YYWMain.mOrder.goods;
		int id= getWaresid(goods);
		String paycode = id+"";
		payRequest.addParam(PayRequest.CHARGE_POINT, paycode);
		// 是否显示SDK内置的支付结果对话框，默认为显示
		payRequest.addParam(PayRequest.DISPLAY_RESULT_DIALOG, true);
		// 设置 CP自定义订单号
		// 如果 CP设置了订单号，SDK会启动支付完成服务端回调功能，并联网将该订单号上传到服务器
		// 关于服务端回调接口，请参照“服务端回调接口“。
		//		payRequest.addParam(PayRequest.ORDER_NO, "1234567890");
		log("morderid = " +morderid);
		payRequest.addParam(PayRequest.ORDER_NO, morderid);
		log("支付开始");
		// 在自定义订单号的基础上，CP还可添加其它自定义数据，该数据在服务端回调时会附加到 attach参数里
		PaySDKApi.pay(mactivity, payRequest, new IPayResultCallback() {
			@Override
			public void onPayResult(ResultBean result) {
				if(result != null && result.isSuccess()) {
					// 支付成功
					paySuce();
					Toast("支付成功");
				}else{
					// 支付失败,通过 result.getDetailCode()可查询具体错误代码
					payFail();
					Toast("支付失败");
					log(result.getDetailCode()+"");
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
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

			@Override
			public void onSuccess(User arg0, int arg1) {
				// TODO Auto-generated method stub
				callback.onExit();
			}

			@Override
			public void onLogout() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

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
	}

	public static void onResume(Activity paramActivity) {

	}

	public static void onPause(Activity paramActivity) {

	}

	public static void onDestroy(Activity paramActivity) {

	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity) {

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
	 */
	public static void paySuce() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	/**
	 * 支付失败
	 */
	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	/**
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
	 * 打印日志
	 */
	public static void log(final String msg){
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.i("tag",msg);
			}
		});
	}

	private static int getWaresid(String goods) {
		int waresid = 0 ;
		if(goods.equals("100元宝")){
			waresid = 181015;
		}else if(goods.equals("300元宝")){
			waresid = 181016;
		}else if(goods.equals("500元宝")){
			waresid = 181017;
		}else if(goods.equals("1000元宝")){
			waresid = 181018;
		}else if(goods.equals("2000元宝")){
			waresid = 181019;
		}else if(goods.equals("5000元宝")){
			waresid = 181020;
		}else if(goods.equals("10000元宝")){
			waresid = 181021;
		}else if(goods.equals("20000元宝")){
			waresid = 181022;
		}else if(goods.equals("月卡")){
			waresid = 181023;
		}else if(goods.equals("霸主特权")){
			waresid = 181024;
		}else if(goods.equals("99元红装礼包")){
			waresid = 181025;
		}else if(goods.equals("149元红装礼包")){
			waresid = 181026;
		}else if(goods.equals("289元红装礼包")){
			waresid = 181027;
		}else if(goods.equals("299元红装礼包")){
			waresid = 181028;
		}else if(goods.equals("199元红装材料礼包")){
			waresid = 181029;
		}else if(goods.equals("599元红装材料礼包")){
			waresid = 181030;
		}else if(goods.equals("589元红装材料礼包")){
			waresid = 181031;
		}else if(goods.equals("449元红装材料礼包")){
			waresid = 181032;
		}else if(goods.equals("12元今日特惠礼包")){
			waresid = 181033;
		}else if(goods.equals("20元今日特惠礼包")){
			waresid = 181034;
		}else if(goods.equals("32元今日特惠礼包")){
			waresid = 181035;
		}else if(goods.equals("56元今日特惠礼包")){
			waresid = 181036;
		}else if(goods.equals("60元今日特惠礼包")){
			waresid = 181037;
		}else if(goods.equals("86元今日特惠礼包")){
			waresid = 181038;
		}else if(goods.equals("116元今日特惠礼包")){
			waresid = 181039;
		}else if(goods.equals("118元今日特惠礼包")){
			waresid = 181040;
		}else if(goods.equals("128元今日特惠礼包")){
			waresid = 181041;
		}else if(goods.equals("130元今日特惠礼包")){
			waresid = 181042;
		}else if(goods.equals("138元今日特惠礼包")){
			waresid = 181043;
		}else if(goods.equals("226元今日特惠礼包")){
			waresid = 181044;
		}else if(goods.equals("228元今日特惠礼包")){
			waresid = 181045;
		}else if(goods.equals("238元今日特惠礼包")){
			waresid = 181046;
		}else if(goods.equals("258元今日特惠礼包")){
			waresid = 181047;
		}else if(goods.equals("538元今日特惠礼包")){
			waresid = 181048;
		}else if(goods.equals("558元今日特惠礼包")){
			waresid = 181049;
		}else if(goods.equals("1080元今日特惠礼包")){
			waresid = 181050;
		}else if(goods.equals("2280元今日特惠礼包")){
			waresid = 181051;
		}else if(goods.equals("108武器时装礼包")){
			waresid = 181052;
		}else if(goods.equals("148坐骑幻化礼包")){
			waresid = 181053;
		}
		return waresid;
	}

}
