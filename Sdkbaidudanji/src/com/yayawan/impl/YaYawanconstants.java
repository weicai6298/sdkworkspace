package com.yayawan.impl;

import java.util.HashMap;
import java.util.jar.Attributes.Name;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.duoku.platform.single.DKPlatform;
import com.duoku.platform.single.DKPlatformSettings;
import com.duoku.platform.single.DkErrorCode;
import com.duoku.platform.single.DkProtocolKeys;
import com.duoku.platform.single.callback.IDKSDKCallBack;
import com.duoku.platform.single.item.DKCMMdoData;
import com.duoku.platform.single.item.GamePropsInfo;
import com.duoku.platform.single.util.SharedUtil;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;

	private static String code;

	private static String goods;

	private static int isyoumeng;

	private static String uid;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");

		IDKSDKCallBack initcompletelistener = new IDKSDKCallBack() {

			@Override
			public void onResponse(String paramString) {
				// TODO Auto-generated method stub
				Log.d("GameMainActivity", paramString);
				try {
					JSONObject jsonObject = new JSONObject(paramString);
					// 返回的操作状态码
					int mFunctionCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_CODE);

					//初始化完成
					if(mFunctionCode == DkErrorCode.BDG_CROSSRECOMMEND_INIT_FINSIH){
						//						login(mActivity);
						DKPlatform.getInstance().invokeBDInit(mActivity, loginlistener);
						isinit = true;
						//						DKPlatform.getInstance().invokeBDInit(mActivity, loginlistener);
						initAds();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		};
		//初始化函数
		Boolean isLandscape = DeviceUtil.isLandscape(mActivity)?true:false;
		Log.i("tag","isLandscape="+isLandscape);
		DKPlatform.getInstance().init(mActivity, isLandscape, DKPlatformSettings.SdkMode.SDK_PAY,null,null,null,initcompletelistener);

		String youmeng = DeviceUtil.getGameInfo(mActivity, "isyoumeng");
		isyoumeng = Integer.parseInt(youmeng);
		if(isyoumeng == 1){
			UMGameAgent.setDebugMode(true);
			UMGameAgent.init(mActivity);
		}
		Log.i("tag","初始化结束");
	}


	/**
	 * 品宣接口初始化
	 */
	private static void initAds(){
		DKPlatform.getInstance().bdgameInit(mActivity, new IDKSDKCallBack() {
			@Override
			public void onResponse(String paramString) {
				Log.d("GameMainActivity","bggameInit success");
			}
		});
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		//		DKPlatform.getInstance().invokeBDInitApplication(mActivity);
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			DKPlatform.getInstance().invokeBDLogin(mactivity, loginlistener);
		}else{
			inintsdk(mactivity);
		}
	}


	static IDKSDKCallBack loginlistener = new IDKSDKCallBack(){
		@Override
		public void onResponse(String paramString) {
			try {
				JSONObject jsonObject = new JSONObject(paramString);
				// 返回的操作状态码
				int mFunctionCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_CODE);
				// 返回的百度uid，供cp绑定使用
				String bduid = jsonObject.getString(DkProtocolKeys.BD_UID);
				uid = bduid;

				Log.i("tag", "mFunctionCode="+mFunctionCode);
				//登陆成功
				if(mFunctionCode == DkErrorCode.DK_ACCOUNT_LOGIN_SUCCESS){
					//隐藏登陆按钮，显示修改密码和切换账号按钮
					loginSuce(mActivity, bduid, bduid, mFunctionCode+"");
					Log.i("tag", "登录成功1");
					//						btnMyLogin.setVisibility(View.GONE);
					//						btnMyModify.setVisibility(View.VISIBLE);
					//						btnMySwitchAccount.setVisibility(View.VISIBLE);
					//登陆失败
				}else if(mFunctionCode == DkErrorCode.DK_ACCOUNT_LOGIN_FAIL){
					//显示登陆按钮，隐藏修改密码和切换账号按钮
					Log.i("tag", "登录失败");
					loginFail();
					Log.i("tag", "登录失败");
					//						btnMyLogin.setVisibility(View.VISIBLE);
					//						btnMyModify.setVisibility(View.GONE);
					//						btnMySwitchAccount.setVisibility(View.GONE);
					//快速注册成功
				}else if(mFunctionCode == DkErrorCode.DK_ACCOUNT_QUICK_REG_SUCCESS){
					//快速试玩登陆成功，都需要隐藏
					//						btnMyLogin.setVisibility(View.GONE);
					//						btnMyModify.setVisibility(View.VISIBLE);
					//						btnMySwitchAccount.setVisibility(View.GONE);
				}else {
					//						btnMyLogin.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		Log.i("tag","morderid="+morderid);

		Log.i("tag","pay-start");
		//		if(YYWMain.mOrder.goods.equals("月卡礼包")){
		//			paycode = "34986";
		//		}else if(YYWMain.mOrder.goods.equals("月卡续费")){
		//			paycode = "34993";
		//		}else if(YYWMain.mOrder.goods.equals("幸运福袋")){
		//			paycode = "34996";
		//		}else if(YYWMain.mOrder.goods.equals("起航礼包")){
		//			paycode = "35001";
		//		}else if(YYWMain.mOrder.goods.equals("首充大返利")){
		//			paycode = "35002";
		//		}else if(YYWMain.mOrder.goods.equals("2.5万金币")){
		//			paycode = "35003";
		//		}else if(YYWMain.mOrder.goods.equals("5万金币")){
		//			paycode = "35004";
		//		}else if(YYWMain.mOrder.goods.equals("15万金币")){
		//			paycode = "35005";
		//		}else if(YYWMain.mOrder.goods.equals("25万金币")){
		//			paycode = "35006";
		//		}else if(YYWMain.mOrder.goods.equals("50万金币")){
		//			paycode = "35007";
		//		}else if(YYWMain.mOrder.goods.equals("250万金币")){
		//			paycode = "35008";
		//		}else if(YYWMain.mOrder.goods.equals("500万金币")){
		//			paycode = "35009";
		//		}else if(YYWMain.mOrder.goods.equals("50钻石")){
		//			paycode = "35010";
		//		}else if(YYWMain.mOrder.goods.equals("100钻石")){
		//			paycode = "35011";
		//		}else if(YYWMain.mOrder.goods.equals("300钻石")){
		//			paycode = "35012";
		//		}else if(YYWMain.mOrder.goods.equals("500钻石")){
		//			paycode = "35013";
		//		}else if(YYWMain.mOrder.goods.equals("1000钻石")){
		//			paycode = "35014";
		//		}else if(YYWMain.mOrder.goods.equals("5000钻石")){
		//			paycode = "35015";
		//		}else if(YYWMain.mOrder.goods.equals("10000钻石")){
		//			paycode = "35016";
		//		}else if(YYWMain.mOrder.goods.equals("无限火力")){
		//			paycode = "35017";
		//		}else if(YYWMain.mOrder.goods.equals("炎龙炮")){
		//			paycode = "35018";
		//		}else if(YYWMain.mOrder.goods.equals("战神无双")){
		//			paycode = "35019";
		//		}else if(YYWMain.mOrder.goods.equals("满地红")){
		//			paycode = "35020";
		//		}else if(YYWMain.mOrder.goods.equals("6元礼包")){
		//			paycode = "35021";
		//		}else if(YYWMain.mOrder.goods.equals("12元礼包")){
		//			paycode = "35022";
		//		}else if(YYWMain.mOrder.goods.equals("30元礼包")){
		//			paycode = "35023";
		//		}else if(YYWMain.mOrder.goods.equals("新手特惠礼包")){
		//			paycode = "35024";
		//		}else if(YYWMain.mOrder.goods.equals("1元礼包")){
		//			paycode = "35025";
		//		}else if(YYWMain.mOrder.goods.equals("超值礼包")){
		//			paycode = "35026";
		//		}else if(YYWMain.mOrder.goods.equals("清凉武士")){
		//			paycode = "35027";
		//		}
//		goods = getgoodsname(YYWMain.mOrder.goods);
		code = getpaycode(YYWMain.mOrder.goods);
		// 支付接口
		Log.i("tag","goods = "+goods );
		Log.i("tag","code = "+code );
		Log.i("tag","YYWMain.mOrder.goods = "+YYWMain.mOrder.goods );
		GamePropsInfo propsSecond = new GamePropsInfo(code, ""+YYWMain.mOrder.money/100, YYWMain.mOrder.goods,morderid);
		Log.i("tag","YYWMain.mOrder.goods_id="+YYWMain.mOrder.goods_id);
		Log.i("tag","propsSecond="+propsSecond);
		DKPlatform.getInstance().invokePayCenterActivity(mactivity, 
				propsSecond,null,null,null,null,null,RechargeCallback);
	}




	/**
	 * 支付处理过程的结果回调函数
	 * */
	static IDKSDKCallBack RechargeCallback = new IDKSDKCallBack(){
		@Override
		public void onResponse(String paramString) {
			// TODO Auto-generated method stub
			Log.d("GamePropsActivity", paramString);
			try {
				JSONObject jsonObject = new JSONObject(paramString);
				// 支付状态码
				int mStatusCode = jsonObject.getInt(DkProtocolKeys.FUNCTION_STATUS_CODE);
				Log.i("tag","mStatusCode="+mStatusCode);
				if(mStatusCode == DkErrorCode.BDG_RECHARGE_SUCCESS){
					// 返回支付成功的状态码，开发者可以在此处理相应的逻辑

					// 订单ID
					String mOrderId = null;
					// 订单状态
					String mOrderStatus = null;
					// 道具ID
					String mOrderProductId = null;
					// 道具实际支付的价格
					String mOrderPrice = null;
					// 支付通道
					String mOrderPayChannel = null;
					//道具原始价格，若微信、支付宝未配置打折该值为空，
					String mOrderPriceOriginal = null;

					if(jsonObject.has(DkProtocolKeys.BD_ORDER_ID)){						
						mOrderId = jsonObject.getString(DkProtocolKeys.BD_ORDER_ID);	
						SharedUtil.getInstance(mActivity).saveString("payment_orderid", mOrderId);
					}
					if(jsonObject.has(DkProtocolKeys.BD_ORDER_STATUS)){
						mOrderStatus = jsonObject.getString(DkProtocolKeys.BD_ORDER_STATUS);
					}
					if(jsonObject.has(DkProtocolKeys.BD_ORDER_PRODUCT_ID)){			
						mOrderProductId = jsonObject.getString(DkProtocolKeys.BD_ORDER_PRODUCT_ID);
					}
					if(jsonObject.has(DkProtocolKeys.BD_ORDER_PAY_CHANNEL)){						
						mOrderPayChannel = jsonObject.getString(DkProtocolKeys.BD_ORDER_PAY_CHANNEL);
					}
					if(jsonObject.has(DkProtocolKeys.BD_ORDER_PRICE)){						
						mOrderPrice = jsonObject.getString(DkProtocolKeys.BD_ORDER_PRICE);
					}
					//int mNum = Integer.valueOf(mOrderPrice) * 10;
					if(jsonObject.has(DkProtocolKeys.BD_ORDER_PAY_ORIGINAL)){						
						mOrderPriceOriginal = jsonObject.getString(DkProtocolKeys.BD_ORDER_PAY_ORIGINAL);
					}
					int mNum = 0;
					if( "".equals(mOrderPriceOriginal) ||null==mOrderPriceOriginal){
						mNum = Integer.valueOf(mOrderPrice) * 10;
					}else{
						mNum = Integer.valueOf(mOrderPriceOriginal) * 10;
					}
					String propsType = "1";
					Log.i("tag","支付成功");
					paySuce();
					code = "";
					Log.i("tag","支付成功1");
					//					Toast.makeText(mActivity, "道具购买成功!\n金额:"+mOrderPrice+"元", Toast.LENGTH_LONG).show();

					DemoRecordData data = new DemoRecordData(mOrderProductId, mOrderPrice, propsType, String.valueOf(mNum));
					DemoDBDao.getInstance(mActivity).updateRechargeRecord(data);

				}else if(mStatusCode == DkErrorCode.BDG_RECHARGE_USRERDATA_ERROR){
					Log.i("tag","支付失败");
					payFail();
					code = "";
					Log.i("tag","支付失败1");

					//					Toast.makeText(mActivity, "用户透传数据不合法", Toast.LENGTH_LONG).show();

				}else if(mStatusCode == DkErrorCode.BDG_RECHARGE_ACTIVITY_CLOSED){

					// 返回玩家手动关闭支付中心的状态码，开发者可以在此处理相应的逻辑
					Log.i("tag","支付失败");
					payFail();
					code = "";
					Log.i("tag","支付失败1");
					//					Toast.makeText(mActivity, "玩家关闭支付中心", Toast.LENGTH_LONG).show();

				}else if(mStatusCode == DkErrorCode.BDG_RECHARGE_FAIL){ 
					if(jsonObject.has(DkProtocolKeys.BD_ORDER_ID)){			
						SharedUtil.getInstance(mActivity).saveString("payment_orderid", jsonObject.getString(DkProtocolKeys.BD_ORDER_ID));
					}
					// 返回支付失败的状态码，开发者可以在此处理相应的逻辑
					Log.i("tag","支付失败");
					payFail();
					code = "";
					Log.i("tag","支付失败1");
					//					Toast.makeText(mActivity, "购买失败", Toast.LENGTH_LONG).show();

				} else if(mStatusCode == DkErrorCode.BDG_RECHARGE_EXCEPTION){ 

					// 返回支付出现异常的状态码，开发者可以在此处理相应的逻辑
					Log.i("tag","支付失败");
					payFail();
					code = "";
					Log.i("tag","支付失败1");
					//					Toast.makeText(mActivity, "购买出现异常", Toast.LENGTH_LONG).show();

				} else if(mStatusCode == DkErrorCode.BDG_RECHARGE_CANCEL){ 

					// 返回取消支付的状态码，开发者可以在此处理相应的逻辑
					Log.i("tag","支付失败");
					payFail();
					code = "";
					Log.i("tag","支付失败1");
					//					Toast.makeText(mActivity, "玩家取消支付", Toast.LENGTH_LONG).show();

				} else {
					Log.i("tag","支付失败");
					payFail();
					code = "";
					Log.i("tag","支付失败1");
					//					Toast.makeText(mActivity, "未知情况", Toast.LENGTH_LONG).show();

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
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
				DKPlatform.getInstance().bdgameExit(paramActivity, new IDKSDKCallBack() {

					@Override
					public void onResponse(String paramString) {
						// TODO Auto-generated method stub
						if(isyoumeng == 1){
							Log.i("tag", "友盟退出");
							MobclickAgent.onProfileSignOff();
							MobclickAgent.onKillProcess(mActivity);
						}
						callback.onExit();
					}
				});
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
		if (Integer.parseInt(ext) == 1){
			if(isyoumeng ==1){
				Log.i("tag", "友盟进入游戏");
				MobclickAgent.onProfileSignIn(uid);
			}
		}
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
		DKPlatform.getInstance().resumeBaiduMobileStatistic(paramActivity); 
		if(isyoumeng == 1){
			MobclickAgent.onResume(paramActivity);
		}
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		//        DKPlatform.getInstance().bdgamePause(paramActivity, new IDKSDKCallBack() {
		//			
		//			@Override
		//			public void onResponse(String paramString) {
		//				// TODO Auto-generated method stub
		//				Log.d("GameMainActivity","bggamePause success");	
		//			}
		//		});
		DKPlatform.getInstance().pauseBaiduMobileStatistic(paramActivity); 
		if(isyoumeng == 1){
			MobclickAgent.onPause(paramActivity);
		}
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub

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


	private static String getpaycode(String goods) {
		String paycode = "";
		//捕鱼大世界
//		if(goods.equals("首充特惠礼包")){
//			paycode = "39845";
//		}else if(goods.equals("贵族礼包")){
//			paycode = "39846";
//		}else if(goods.equals("金币礼包(6元)")){
//			paycode = "39847";
//		}else if(goods.equals("金币礼包(12元)")){
//			paycode = "39848";
//		}else if(goods.equals("金币礼包(28元)")){
//			paycode = "39849";
//		}else if(goods.equals("金币礼包(50元)")){
//			paycode = "39850";
//		}else if(goods.equals("金币礼包(108元)")){
//			paycode = "39851";
//		}else if(goods.equals("金币礼包(328元)")){
//			paycode = "39852";
//		}else if(goods.equals("金币礼包(618元)")){
//			paycode = "39853";
//		}else if(goods.equals("钻石礼包(6元)")){
//			paycode = "39854";
//		}else if(goods.equals("钻石礼包(12元)")){
//			paycode = "39855";
//		}else if(goods.equals("钻石礼包(28元)")){
//			paycode = "39856";
//		}else if(goods.equals("钻石礼包(50元)")){
//			paycode = "39857";
//		}else if(goods.equals("钻石礼包(108元)")){
//			paycode = "39858";
//		}else if(goods.equals("钻石礼包(328元)")){
//			paycode = "39858";
//		}
		//我的便利店
		if(goods.equals("10钻石")){
			paycode = "43243";
		}else if(goods.equals("32钻石")){
			paycode = "43244";
		}else if(goods.equals("57钻石")){
			paycode = "43245";
		}else if(goods.equals("120钻石")){
			paycode = "43246";
		}else if(goods.equals("390钻石")){
			paycode = "43247";
		}else if(goods.equals("680钻石")){
			paycode = "43248";
		}else if(goods.equals("1450钻石")){
			paycode = "43249";
		}
		else if(goods.equals("首冲390钻石")){
			paycode = "43250";
		}
		else if(goods.equals("每日钻石套餐")){
			paycode = "43251";
		}
		else if(goods.equals("新手套餐")){
			paycode = "43252";
		}
		else if(goods.equals("实惠套餐")){
			paycode = "43253";
		}
		else if(goods.equals("高级套餐")){
			paycode = "43254";
		}
		else if(goods.equals("每日特惠周一")){
			paycode = "43255";
		}
		else if(goods.equals("每日特惠周二")){
			paycode = "43256";
		}
		else if(goods.equals("每日特惠周三")){
			paycode = "43257";
		}
		else if(goods.equals("每日特惠周四")){
			paycode = "43258";
		}
		else if(goods.equals("每日特惠周五")){
			paycode = "43259";
		}
		else if(goods.equals("每日特惠周六")){
			paycode = "43260";
		}
		else if(goods.equals("每日特惠周日")){
			paycode = "43261";
		}
		else if(goods.equals("首充礼包")){
			paycode = "43262";
		}
		return paycode;
	}


	private static String getgoodsname(String goods2) {
		String name ="";
		//捕鱼大世界
		if(goods2.equals("首充特惠礼包")){
			name = "1元特惠礼包";
		}else if(goods2.equals("贵族礼包")){
			name = "贵族礼包";
		}else if(goods2.equals("金币礼包(6元)")){
			name = "30000金币礼包";
		}else if(goods2.equals("金币礼包(12元)")){
			name = "60000金币礼包";
		}else if(goods2.equals("金币礼包(28元)")){
			name = "140000金币礼包";
		}else if(goods2.equals("金币礼包(50元)")){
			name = "250000金币礼包";
		}else if(goods2.equals("金币礼包(108元)")){
			name = "540000金币礼包";
		}else if(goods2.equals("金币礼包(328元)")){
			name = "1640000金币礼包";
		}else if(goods2.equals("金币礼包(618元)")){
			name = "3090000金币礼包";
		}else if(goods2.equals("钻石礼包(6元)")){
			name = "60钻石礼包";
		}else if(goods2.equals("钻石礼包(12元)")){
			name = "120钻石礼包";
		}else if(goods2.equals("钻石礼包(28元)")){
			name = "280钻石礼包";
		}else if(goods2.equals("钻石礼包(50元)")){
			name = "500钻石礼包";
		}else if(goods2.equals("钻石礼包(108元)")){
			name = "1080钻石礼包";
		}else if(goods2.equals("钻石礼包(328元)")){
			name = "3280钻石礼包";
		}
		return name;
	}
}
