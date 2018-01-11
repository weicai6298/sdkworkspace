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
import com.lenovo.lsf.gamesdk.GamePayRequest;
import com.lenovo.lsf.gamesdk.IAuthResult;
import com.lenovo.lsf.gamesdk.IPayResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi;
import com.lenovo.lsf.lenovoid.LenovoIDApi;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;

	private static String appid;
	private static String appkey;
//	private static int isguanggao; //是否有广告
//	private static String interstitial_id;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		//SDK初始化
		appid= ""+DeviceUtil.getGameInfo(mactivity, "lenovo.open.appid");
		appkey= ""+DeviceUtil.getGameInfo(mactivity, "appkey");
		Log.i("tag", "appid="+appid);
		LenovoGameApi.doInit(mactivity,appid);
		isinit = true ;
		
//		String guanggao = DeviceUtil.getGameInfo(mActivity, "isguanggao");//是否有广告，1有，0没有
//		interstitial_id = DeviceUtil.getGameInfo(mActivity, "interstitial_id");
//		isguanggao = Integer.parseInt(guanggao);
//		if(isguanggao == 1){
//			startIntersititial(mactivity,interstitial_id);
//		}

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
			LenovoGameApi.doAutoLogin(mactivity, new IAuthResult() {

				@Override
				public void onFinished(boolean ret, final String data) {
					// TODO Auto-generated method stub
					Log.i("tag", "ret = "+ret);
					Log.i("tag", "data = "+data);

					if(ret){
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub


								//登录成功
								try {
									HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/data/get_uid/");
									List<NameValuePair> params = new ArrayList<NameValuePair>(); 
									params.add(new BasicNameValuePair("app_id", DeviceUtil.getAppid(mActivity))); 
									params.add(new BasicNameValuePair("code", data)); 
//									params.add(new BasicNameValuePair("app_key", appkey)); 

									Log.i("tag","params="+params);
									try { 
										// 设置httpPost请求参数 
										Log.i("tag","httpPost1");
										httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
										Log.i("tag","httpPost2");
										HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost); 
										Log.i("tag","httpResponse.getStatusLine().getStatusCode()="+httpResponse.getStatusLine().getStatusCode());
										if(httpResponse.getStatusLine().getStatusCode() == 200){
											String re = EntityUtils.toString(httpResponse.getEntity());
											Log.i("tag","re="+re);
											JSONObject js = new JSONObject(re);
											Log.i("tag","js="+js);
											String uid = js.getString("uid");
											Log.i("tag","uid="+uid);
											loginSuce(mActivity, uid, uid, data);
											Log.i("tag","登录成功");
											Toast("登录成功");
										}

									}catch(ClientProtocolException e){
										e.printStackTrace();
									}

								}catch (Exception e) {
									e.printStackTrace();
								}
							}
						}).start();
					}else{
						// 登录失败(失败原因开启飞行模式、 网络不通等)
						loginFail();
						Toast("登录失败");
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
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");


		String goods = YYWMain.mOrder.goods;
		int waresid = getWaresid(goods);
		Log.i("tag","waresid = " + waresid);
		GamePayRequest payRequest = new GamePayRequest();
		// 请填写商品自己的参数
//		payRequest.addParam("notifyurl", DeviceUtil.getGameInfo(mActivity, "callback"));//当前版本暂时不用，传空String
//		payRequest.addParam("notifyurl", "");//当前版本暂时不用，传空String
		payRequest.addParam("appid", appid);
		payRequest.addParam("waresid", waresid);
		payRequest.addParam("exorderno", morderid);
		payRequest.addParam("price", Integer.parseInt(YYWMain.mOrder.money+""));
		Log.i("tag","payRequest="+payRequest);
		Log.i("tag","appkey="+appkey);
		LenovoGameApi.doPay(mactivity, appkey, payRequest, new IPayResult() {

			@Override
			public void onPayResult(int resultCode, String signValue,
					String resultInfo) {
				if (LenovoGameApi.PAY_SUCCESS == resultCode) {
					paySuce();
					Toast.makeText(mActivity,"支付成功", Toast.LENGTH_SHORT).show();
				} else if (LenovoGameApi.PAY_CANCEL == resultCode) {
					payFail();
					Toast.makeText(mActivity, "取消支付",Toast.LENGTH_SHORT).show();
					// 取消支付处理，默认采用finish()，请根据需要修改
					//					Log.e(Config.TAG, "return cancel");
				} else {
					payFail();
					Toast.makeText(mActivity, "支付失败",Toast.LENGTH_SHORT).show();
					// 计费失败处理，默认采用finish()，请根据需要修改
					//					Log.e(Config.TAG, "return Error");
				}
			}
		});
	}

	private static int getWaresid(String goods) {
		int waresid = 0 ;
		//决战九天
//		if(goods.equals("100元宝")){
//			waresid = 181015;
//		}else if(goods.equals("300元宝")){
//			waresid = 181016;
//		}else if(goods.equals("500元宝")){
//			waresid = 181017;
//		}else if(goods.equals("1000元宝")){
//			waresid = 181018;
//		}else if(goods.equals("2000元宝")){
//			waresid = 181019;
//		}else if(goods.equals("5000元宝")){
//			waresid = 181020;
//		}else if(goods.equals("10000元宝")){
//			waresid = 181021;
//		}else if(goods.equals("20000元宝")){
//			waresid = 181022;
//		}else if(goods.equals("月卡")){
//			waresid = 181023;
//		}else if(goods.equals("霸主特权")){
//			waresid = 181024;
//		}else if(goods.equals("99元红装礼包")){
//			waresid = 181025;
//		}else if(goods.equals("149元红装礼包")){
//			waresid = 181026;
//		}else if(goods.equals("289元红装礼包")){
//			waresid = 181027;
//		}else if(goods.equals("299元红装礼包")){
//			waresid = 181028;
//		}else if(goods.equals("199元红装材料礼包")){
//			waresid = 181029;
//		}else if(goods.equals("599元红装材料礼包")){
//			waresid = 181030;
//		}else if(goods.equals("589元红装材料礼包")){
//			waresid = 181031;
//		}else if(goods.equals("449元红装材料礼包")){
//			waresid = 181032;
//		}else if(goods.equals("12元今日特惠礼包")){
//			waresid = 181033;
//		}else if(goods.equals("20元今日特惠礼包")){
//			waresid = 181034;
//		}else if(goods.equals("32元今日特惠礼包")){
//			waresid = 181035;
//		}else if(goods.equals("56元今日特惠礼包")){
//			waresid = 181036;
//		}else if(goods.equals("60元今日特惠礼包")){
//			waresid = 181037;
//		}else if(goods.equals("86元今日特惠礼包")){
//			waresid = 181038;
//		}else if(goods.equals("116元今日特惠礼包")){
//			waresid = 181039;
//		}else if(goods.equals("118元今日特惠礼包")){
//			waresid = 181040;
//		}else if(goods.equals("128元今日特惠礼包")){
//			waresid = 181041;
//		}else if(goods.equals("130元今日特惠礼包")){
//			waresid = 181042;
//		}else if(goods.equals("138元今日特惠礼包")){
//			waresid = 181043;
//		}else if(goods.equals("226元今日特惠礼包")){
//			waresid = 181044;
//		}else if(goods.equals("228元今日特惠礼包")){
//			waresid = 181045;
//		}else if(goods.equals("238元今日特惠礼包")){
//			waresid = 181046;
//		}else if(goods.equals("258元今日特惠礼包")){
//			waresid = 181047;
//		}else if(goods.equals("538元今日特惠礼包")){
//			waresid = 181048;
//		}else if(goods.equals("558元今日特惠礼包")){
//			waresid = 181049;
//		}else if(goods.equals("1080元今日特惠礼包")){
//			waresid = 181050;
//		}else if(goods.equals("2280元今日特惠礼包")){
//			waresid = 181051;
//		}else if(goods.equals("108武器时装礼包")){
//			waresid = 181052;
//		}else if(goods.equals("148坐骑幻化礼包")){
//			waresid = 181053;
//		}
		//无双战纪
		if(goods.equals("600元宝")){
			waresid = 188945;
		}else if(goods.equals("3000元宝")){
			waresid = 188946;
		}else if(goods.equals("6800元宝")){
			waresid = 188947;
		}else if(goods.equals("12800元宝")){
			waresid = 188948;
		}else if(goods.equals("19800元宝")){
			waresid = 188949;
		}else if(goods.equals("32800元宝")){
			waresid = 188950;
		}else if(goods.equals("64800元宝")){
			waresid = 188951;
		}
		return waresid;
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

		LenovoGameApi.doQuit(paramActivity, new IAuthResult() {

			@Override
			public void onFinished(boolean ret, String data) {
				// TODO Auto-generated method stub
				if(ret){
//					callback.onExit();
					mActivity.finish();
					System.exit(0);
				}else{
					//					Toast("继续游戏");
				}
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
//		institial.destroyIntersititial();
	}

	public static void onActivityResult(Activity paramActivity) {

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
		//		mActivity.runOnUiThread(new Runnable() {
		//
		//			@Override
		//			public void run() {
		Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
		//			}
		//		});
	}

	
//	private static void startIntersititial(final Activity mactivity,String placementID_Interstitial) {
//		
//		institial = new Interstitial(mactivity, placementID_Interstitial, new InterstitialListener() {
//
//			@Override
//			public void onInterstitialShowSuccess(String msg) {
//				Toast.makeText(mactivity, "插屏展示成功", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onInterstitialRequestFailed(String msg) {
//				Toast.makeText(mactivity, "插屏请求失败 "+msg, Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onInterstitialDismiss() {
//				Toast.makeText(mactivity, "插屏关闭成功", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}

}
