package com.yayawan.impl;

import java.util.Random;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.callback.SinglePayCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.platform.opensdk.pay.PayResponse;
//import com.oppo.mobad.api.MobAdManager;
//import com.oppo.mobad.api.ad.BannerAd;
//import com.oppo.mobad.api.ad.InterstitialAd;
//import com.oppo.mobad.api.listener.IInterstitialAdListener;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	
//	private static BannerAd mBannerAd;
	
//	private static InterstitialAd mInterstitialAd;
	
//	private static String banner_id;
	
	private static String interstitial_id;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//        initInterstitialAd();
       
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		Log.i("tag","application初始化");
		String appSecret = ""+DeviceUtil.getGameInfo(applicationContext, "appSecret");
		String appid = ""+DeviceUtil.getGameInfo(applicationContext, "appid");
//		banner_id = ""+DeviceUtil.getGameInfo(applicationContext, "banner_id");
//		interstitial_id = ""+DeviceUtil.getGameInfo(applicationContext, "interstitial_id");
		Log.i("tag","appSecret="+appSecret);
		Log.i("tag","广告appid="+appid);
//		Log.i("tag","interstitial_id="+interstitial_id);
		GameCenterSDK.init(appSecret, applicationContext);
		isinit = true ;
//		MobAdManager.getInstance().init(applicationContext, appid);
		Log.i("tag","oppo初始化结束");
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
	 */
	public static void pay(Activity mactivity, String morderid) {

		Yayalog.loger("YaYawanconstantssdk支付");
		// cp支付参数
				int amount = Integer.parseInt(YYWMain.mOrder.money+"");; // 支付金额，单位分
				PayInfo payInfo = new PayInfo(morderid, "", amount);
//				payInfo.setProductDesc("商品描述");
				payInfo.setProductName(YYWMain.mOrder.goods);
				// 支付结果服务器回调地址，不通过服务端回调发货的游戏可以不用填写~
				payInfo.setCallbackUrl(DeviceUtil.getGameInfo(mActivity, "callback"));

				GameCenterSDK.getInstance().doSinglePay(mactivity, payInfo,new SinglePayCallback() {

							@Override
							public void onSuccess(String resultMsg) {
								// add OPPO 支付成功处理逻辑~
								paySuce();
								Toast.makeText(mActivity, "支付成功",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(String resultMsg, int resultCode) {
								// add OPPO 支付失败处理逻辑~
								if (PayResponse.CODE_CANCEL != resultCode) {
									payFail();
									Toast.makeText(mActivity, "支付失败",
											Toast.LENGTH_SHORT).show();
								} else {
									// 取消支付处理
									payFail();
									Toast.makeText(mActivity, "支付取消",
											Toast.LENGTH_SHORT).show();
								}
							}
							@Override
							public void onCallCarrierPay(PayInfo payInfo, boolean bySelectSMSPay) {
								Toast.makeText(mActivity, "运营商支付",
										Toast.LENGTH_SHORT).show();
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
		GameCenterSDK.getInstance().onExit(paramActivity, new GameExitCallback() {
			
			@Override
			public void exitGame() {
				mActivity.finish();
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
		GameCenterSDK.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		GameCenterSDK.getInstance().onPause();
	}

	public static void onDestroy(Activity paramActivity) {
//		 if (null != mBannerAd) {
//	            mBannerAd.destroyAd();
//	     }
//	        if (null != mInterstitialAd) {
//	            mInterstitialAd.destroyAd();
//	        }
	}

	public static void onActivityResult(Activity paramActivity) {

	}

	public static void onNewIntent(Intent paramIntent) {

	}

	public static void onStart(Activity mActivity2) {

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
//	 private static void initBannerAd() {
//	        /**
//	         * new bannerAd
//	         */
//	        mBannerAd = new BannerAd(mActivity, banner_id);
//	        /**
//	         * set banner action listener.
//	         */
//	        mBannerAd.setAdListener(new IBannerAdListener() {
//	            @Override
//	            public void onAdShow() {
//	                Log.i("tag", "onAdShow");
//	            }
//
//	            @Override
//	            public void onAdFailed(String errMsg) {
//	            	Log.i("tag", "onAdFailed:errMsg=" + (null != errMsg ? errMsg : ""));
//	            }
//
//	            @Override
//	            public void onAdReady() {
//	            	Log.i("tag", "onAdReady");
//	            }
//
//	            @Override
//	            public void onAdClick() {
//	            	Log.i("tag", "onAdClick");
//	            }
//
//	            @Override
//	            public void onAdClose() {
//	            	Log.i("tag", "onAdClose");
//	            }
//	        });
//	        mBannerAd.loadAd();
//	 }
//	        private static void initInterstitialAd() {
//	            /**
//	             * new InterstitialAd.
//	             */
//	        	Log.i("tag","interstitial_id=="+interstitial_id);
//	            mInterstitialAd = new InterstitialAd(mActivity, interstitial_id);
//	            /**
//	             * set InterstitialAd action listener.
//	             */
//	            mInterstitialAd.loadAd();
//	            mInterstitialAd.showAd();
//	            mInterstitialAd.setAdListener(new IInterstitialAdListener() {
//					
//					@Override
//					public void onAdShow() {
//						 Log.i("tag", "onAdShow");
//						 mInterstitialAd.showAd();
//					}
//					
//					@Override
//					public void onAdReady() {
//						Log.i("tag", "onAdReady");
//					}
//					
//					@Override
//					public void onAdFailed(String arg0) {
//						Log.i("tag", "onAdFailed="+arg0);
//					}
//					
//					@Override
//					public void onAdClose() {
//						 Log.i("tag", "onAdClose");
//					}
//					
//					@Override
//					public void onAdClick() {
//						Log.i("tag", "onAdClick");
//					}
//				});
//	            
//	        }
}
