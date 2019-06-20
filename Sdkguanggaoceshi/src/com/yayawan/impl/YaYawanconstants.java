package com.yayawan.impl;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.vivo.mobilead.R;
import com.vivo.mobilead.banner.VivoBannerAd;
import com.vivo.mobilead.interstitial.VivoInterstialAd;
import com.vivo.mobilead.listener.IAdListener;
import com.vivo.mobilead.manager.VivoAdManager;
import com.vivo.mobilead.model.VivoAdError;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	 private static VivoInterstialAd mVivoInterstialAd;
	 
	 private static ViewGroup mContainer;
	    private static VivoBannerAd mVivoBanner;
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		isinit = true ;
	}

	

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {

		VivoAdManager.getInstance().init(applicationContext, "91aab8b980ea453ba9b35cd7f465b4f8");
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
//			InterstialAd();
			mContainer = new FrameLayout(mActivity);
			 displayAD();
			
		}else{
			inintsdk(mactivity);
		}
	}

	private static void InterstialAd() {
		mVivoInterstialAd = new VivoInterstialAd(mActivity, Constans.VIVO_INTERSTIAL_ID, new IAdListener() {
			
			@Override
			public void onAdShow() {
				 Log.i("tag", "onAdShow");
			}
			
			@Override
			public void onAdReady() {
				Log.i("tag", "onAdReady");
		        if (mVivoInterstialAd != null) {
		            mVivoInterstialAd.showAd();
		        }
			}
			
			@Override
			public void onAdFailed(VivoAdError arg0) {
				 Log.i("tag", "reason: " + arg0);
			}
			
			@Override
			public void onAdClosed() {
				 Log.i("tag", "onAdClosed");
			}
			
			@Override
			public void onAdClick() {
				 Log.i("tag", "onAdClick");
			}
		});
        mVivoInterstialAd.load();
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		
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
	 * 
	 * 请求上报角色信息
	 * 
	 */
	@SuppressWarnings("unused")
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

	
	private static void displayAD() {
        /**
         * 请求Banner广告。
         */
        mVivoBanner = new VivoBannerAd(mActivity, Constans.VIVO_BANNER_ID, new IAdListener() {
			
			@Override
			public void onAdShow() {
				Log.i("tag", "onAdShow");
			}
			
			@Override
			public void onAdReady() {
				Log.i("tag", "onAdReady");
			}
			
			@Override
			public void onAdFailed(VivoAdError arg0) {
				Log.i("tag", "reason: " + arg0);
			}
			
			@Override
			public void onAdClosed() {
				 Log.i("tag", "onAdClosed");
			}
			
			@Override
			public void onAdClick() {
				 Log.i("tag", "onAdClick");
			}
		});

        /**
         * 设置Banner显示关闭按钮。
         */
        mVivoBanner.setShowClose(true);
        /**
         * 设置Banner刷新频率。
         */
        mVivoBanner.setRefresh(30);
        /**
         * 获取Banner广告View。
         */
        View adView = mVivoBanner.getAdView();
      
        /**
         * 注意：只有adView不等于null时，才能把View添加到你的布局中显示。
         * 创建完广告必须调用addView添加广告视图，不然会导致曝光率低。
         */
        if (null != adView) {
            /**
             * 创建广告后必须调用改方法
             */
        	Log.i("tag", "1111111111111111111111111");
            mContainer.addView(adView);
        }
    }

    private void closeAD() {
        mContainer.removeAllViews();
        if (mVivoBanner != null) {
            mVivoBanner.destroy();
        }
    }

 
}
