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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.coolcloud.uac.android.api.Coolcloud;
import com.coolcloud.uac.android.api.ErrInfo;
import com.coolcloud.uac.android.api.OnResultListener;
import com.coolcloud.uac.android.common.Constants;
import com.coolcloud.uac.android.common.Params;
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
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yulong.android.paysdk.api.CoolpayApi;
import com.yulong.android.paysdk.base.IPayResult;
import com.yulong.android.paysdk.base.common.CoolPayResult;
import com.yulong.android.paysdk.base.common.CoolYunAccessInfo;
import com.yulong.android.paysdk.base.common.PayInfo;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static Coolcloud coolcloud = null;
	
	private static String uid;
	private static String token;
	private static String openid;  //用户唯一标识,服务端返回

	private static String appid;
	private static String paykey;
	private static int pay_style;  //支付模式  0页面  1窗口

	//	private static int screen = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	//	private static int screen = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	private static int screen = 0;

	private static CoolpayApi api ;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		screen = DeviceUtil.isLandscape(mActivity)?0:1;
		appid = ""+DeviceUtil.getGameInfo(mactivity, "appid");
		paykey = ""+DeviceUtil.getGameInfo(mactivity, "paykey");
		String pay = ""+DeviceUtil.getGameInfo(mactivity, "pay");
		pay_style = Integer.parseInt(pay);
		Log.i("tag", "appid = " +appid);
		coolcloud = Coolcloud.get(mactivity, appid);
		isinit = true ;
//		login(mactivity);

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
			Bundle input = new Bundle();
			input.putInt(Constants.KEY_SCREEN_ORIENTATION, screen);
			input.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
			// 设置登录方式，这里采用新建账户登录
			input.putString(Constants.KEY_RESPONSE_TYPE,
					Constants.RESPONSE_TYPE_CODE);
			coolcloud.login(mactivity, input, new Handler(), coolyunListenser);
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
		api = CoolpayApi.createCoolpayApi(mactivity, appid);
		String goods = YYWMain.mOrder.goods;
		int paycode = getPaycode(goods);
		//设置酷云信息
//		if (0 == rtnCode) {
		CoolYunAccessInfo accessInfo = new CoolYunAccessInfo();
		accessInfo.setAccessToken(token);
		accessInfo.setOpenId(uid);
//		}
		PayInfo payInfo = new PayInfo();
		//设置商品信息
		payInfo.setAppId(appid);//设置appId
		payInfo.setPayKey(paykey);//设置paykey
		payInfo.setName(YYWMain.mOrder.goods);
		payInfo.setPrice(Integer.parseInt(YYWMain.mOrder.money+""));
		payInfo.setPoint(paycode);	//设置商品编号
		//设置商品数量（当前不支持多数量支付，请设置为1）	
		payInfo.setQuantity(1);
		payInfo.setCpOrder(morderid);
		//设置订单有效期
		payInfo.setValidityPeriod(15);
		//调用支付接口	
		api.startPay(mactivity, payInfo, accessInfo, payResult,
				pay_style, screen);
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
//				callback.onExit();
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
});
	}

	/**
	 * 设置角色信息
	 * 
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

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {

		if (null != api) {
			Log.d("zqll", "paramInt1 | paramInt2 | paramIntent[text]:"
					+ paramInt1 + " | " + paramInt2 + " | " + paramIntent);
			api.onPayResult(paramInt1, paramInt2, paramIntent);
		}

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

	private static OnResultListener coolyunListenser = new OnResultListener() {

		@Override
		public void onResult(Bundle paramBundle) {
			String code = paramBundle.getString(Params.KEY_AUTHCODE);
			Log.i("tag", "code = " + code);
			Log.i("tag", "paramBundle = " + paramBundle);
            HttpPost(code);
             
		}


		@Override
		public void onError(ErrInfo arg0) {
			Log.i("tag", "登录失败error:" + arg0.getMessage());
			loginFail();
			Toast("登录失败");
		}

		@Override
		public void onCancel() {
			loginFail();
			Toast("登录取消");
		}


	};


	/**
	 * 切换账号 飘浮窗和游戏中的切换账号都在这里边实现
	 */
	public static void doSwitchAccount() {
		Bundle mInput = new Bundle();
		// 设置屏幕横竖屏默认为竖屏
		mInput.putInt(Constants.KEY_SCREEN_ORIENTATION, screen);
		// 设置获取类型
		mInput.putString(Constants.KEY_RESPONSE_TYPE,
				Constants.RESPONSE_TYPE_CODE);
		// 设置需要权限 一般都为get_basic_userinfo这个常量
		mInput.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
		coolcloud.loginNew(mActivity, mInput, new Handler(),
				coolyunListenser);
	}

	/**
	 * 成功0；失败-1
	 */
	private static IPayResult payResult = new IPayResult() {
		@Override
		public void onResult(CoolPayResult result) {
			//			coolpad_demo_shadow.setVisibility(View.GONE);
			Log.i("tag", "result = "+ result);
			if (null != result) {
				String resultStr = result.getResult();  //支付内容
				int ResultStatus = result.getResultStatus(); 
				Log.i("tag", "resultStr = "+ resultStr);

				Log.i("tag", "ResultStatus = " + ResultStatus);
				//  0-成功  -1失败 -2取消
				if(ResultStatus == 0 ){
					paySuce();
					Toast("支付成功");
				}else if(ResultStatus == -1 ){
					payFail();
					Toast("支付失败");
				}else if(ResultStatus == -2 ){
					payFail();
					Toast("支付失败");
				}
			}
		}
	};
	
	private  static void HttpPost(final String code){
		token = code;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/data/get_uid/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("code", code);
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
							token = obj.getString("access_token");
							Yayalog.loger("uid ="+uid);
							loginSuce(mActivity, uid, uid, token);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
	
	
	private static int getPaycode(String goods) {
		int paycode = 0 ;
		//决战九天
//		if(goods.equals("100元宝")){
//			paycode = 1;
//		}else if(goods.equals("300元宝")){
//			paycode = 2;
//		}else if(goods.equals("500元宝")){
//			paycode = 3;
//		}else if(goods.equals("1000元宝")){
//			paycode = 4;
//		}else if(goods.equals("2000元宝")){
//			paycode = 5;
//		}else if(goods.equals("5000元宝")){
//			paycode = 6;
//		}else if(goods.equals("10000元宝")){
//			paycode = 7;
//		}else if(goods.equals("20000元宝")){
//			paycode = 8;
//		}else if(goods.equals("月卡")){
//			paycode = 9;
//		}else if(goods.equals("霸主特权")){
//			paycode = 10;
//		}else if(goods.equals("99元红装礼包")){
//			paycode = 11;
//		}else if(goods.equals("149元红装礼包")){
//			paycode = 12;
//		}else if(goods.equals("289元红装礼包")){
//			paycode = 13;
//		}else if(goods.equals("299元红装礼包")){
//			paycode = 14;
//		}else if(goods.equals("199元红装材料礼包")){
//			paycode = 15;
//		}else if(goods.equals("599元红装材料礼包")){
//			paycode = 16;
//		}else if(goods.equals("589元红装材料礼包")){
//			paycode = 17;
//		}else if(goods.equals("449元红装材料礼包")){
//			paycode = 18;
//		}else if(goods.equals("12元今日特惠礼包")){
//			paycode = 19;
//		}else if(goods.equals("20元今日特惠礼包")){
//			paycode = 20;
//		}else if(goods.equals("32元今日特惠礼包")){
//			paycode = 21;
//		}else if(goods.equals("56元今日特惠礼包")){
//			paycode = 22;
//		}else if(goods.equals("60元今日特惠礼包")){
//			paycode = 23;
//		}else if(goods.equals("86元今日特惠礼包")){
//			paycode = 24;
//		}else if(goods.equals("116元今日特惠礼包")){
//			paycode = 25;
//		}else if(goods.equals("118元今日特惠礼包")){
//			paycode = 26;
//		}else if(goods.equals("128元今日特惠礼包")){
//			paycode = 27;
//		}else if(goods.equals("130元今日特惠礼包")){
//			paycode = 28;
//		}else if(goods.equals("138元今日特惠礼包")){
//			paycode = 29;
//		}else if(goods.equals("226元今日特惠礼包")){
//			paycode = 30;
//		}else if(goods.equals("228元今日特惠礼包")){
//			paycode = 31;
//		}else if(goods.equals("238元今日特惠礼包")){
//			paycode = 32;
//		}else if(goods.equals("258元今日特惠礼包")){
//			paycode = 33;
//		}else if(goods.equals("538元今日特惠礼包")){
//			paycode = 34;
//		}else if(goods.equals("558元今日特惠礼包")){
//			paycode = 35;
//		}else if(goods.equals("1080元今日特惠礼包")){
//			paycode = 36;
//		}else if(goods.equals("2280元今日特惠礼包")){
//			paycode = 37;
//		}else if(goods.equals("108武器时装礼包")){
//			paycode = 38;
//		}else if(goods.equals("148坐骑幻化礼包")){
//			paycode = 39;
//		}
		//无双战纪
//		if(goods.equals("600元宝")){
//			paycode = 1;
//		}else if(goods.equals("3000元宝")){
//			paycode = 2;
//		}else if(goods.equals("6800元宝")){
//			paycode = 3;
//		}else if(goods.equals("12800元宝")){
//			paycode = 4;
//		}else if(goods.equals("19800元宝")){
//			paycode = 5;
//		}else if(goods.equals("32800元宝")){
//			paycode = 6;
//		}else if(goods.equals("64800元宝")){
//			paycode = 7;
//		}
		
		//我的便利店
				if(goods.equals("10钻石")){
					paycode = 2;
				}else if(goods.equals("32钻石")){
					paycode = 3;
				}else if(goods.equals("57钻石")){
					paycode = 4;
				}else if(goods.equals("120钻石")){
					paycode = 5;
				}else if(goods.equals("390钻石")){
					paycode = 6;
				}else if(goods.equals("680钻石")){
					paycode = 7;
				}else if(goods.equals("1450钻石")){
					paycode = 8;
				}
				else if(goods.equals("首冲390钻石")){
					paycode = 9;
				}
				else if(goods.equals("每日钻石套餐")){
					paycode = 10;
				}
				else if(goods.equals("新手套餐")){
					paycode = 11;
				}
				else if(goods.equals("实惠套餐")){
					paycode = 12;
				}
				else if(goods.equals("高级套餐")){
					paycode = 13;
				}
				else if(goods.equals("每日特惠周一")){
					paycode = 14;
				}
				else if(goods.equals("每日特惠周二")){
					paycode = 15;
				}
				else if(goods.equals("每日特惠周三")){
					paycode = 16;
				}
				else if(goods.equals("每日特惠周四")){
					paycode = 17;
				}
				else if(goods.equals("每日特惠周五")){
					paycode = 18;
				}
				else if(goods.equals("每日特惠周六")){
					paycode = 19;
				}
				else if(goods.equals("每日特惠周日")){
					paycode = 20;
				}
				else if(goods.equals("首充礼包")){
					paycode = 21;
				}
				else if(goods.equals("新手礼包")){
					paycode = 22;
				}
				else if(goods.equals("加油礼包")){
					paycode = 23;
				}
				else if(goods.equals("进取礼包")){
					paycode = 24;
				}
				else if(goods.equals("超值礼包")){
					paycode = 25;
				}
		
//		if(goods.equals("60元宝")){
//			paycode = 1;
//		}else if(goods.equals("280元宝")){
//			paycode = 2;
//		}else if(goods.equals("680元宝")){
//			paycode = 3;
//		}else if(goods.equals("1280元宝")){
//			paycode = 4;
//		}else if(goods.equals("3280元宝")){
//			paycode = 5;
//		}else if(goods.equals("6480元宝")){
//			paycode = 6;
//		}else if(goods.equals("9980元宝")){
//			paycode = 7;
//		}
//		else if(goods.equals("20480元宝")){
//			paycode = 8;
//		}
//		else if(goods.equals("周卡")){
//			paycode = 9;
//		}
//		else if(goods.equals("月卡")){
//			paycode = 10;
//		}
//		else if(goods.equals("至尊卡")){
//			paycode = 11;
//		}
//		else if(goods.equals("一元礼包")){
//			paycode = 12;
//		}
		return paycode;
	}

}
