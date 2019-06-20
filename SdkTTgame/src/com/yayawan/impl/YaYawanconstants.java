package com.yayawan.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.wett.cooperation.container.SdkCallback;
import com.wett.cooperation.container.TTSDKV2;
import com.wett.cooperation.container.bean.GameInfo;
import com.wett.cooperation.container.bean.PayInfo;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	public static String uid;
	
	public static String role_id = "123";
	public static String role_name = "123";
	public static String role_level = "123";
	public static String zone_id = "123";
	public static String zone_name = "123";
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		int isLandscape = DeviceUtil.isLandscape(mActivity)?Configuration.ORIENTATION_LANDSCAPE:Configuration.ORIENTATION_PORTRAIT;
		 GameInfo gameInfo = new GameInfo();
		    //isDebug为true则走测试服务器
		 TTSDKV2.getInstance().init(mactivity, gameInfo, false, isLandscape , new SdkCallback<String>() {
		        @Override
		        protected boolean onResult(int i, String s) {
		            if (i == 0) {
		                Log.i("tag","init成功");
//		                Toast.makeText(instance, "init成功", Toast.LENGTH_SHORT).show();
		                TTSDKV2.getInstance().onCreate(mActivity);
		                isinit = true ;
		                TTSDKV2.getInstance().setLogoutListener(new SdkCallback<String>() {
		                    @Override
		                     protected boolean onResult(int i, String s) {
		                         if (i == 0) {
		                             //游戏自己的登出操作
		                             //....
//		                             Toast.makeText(mActivity, "logout成功", Toast.LENGTH_SHORT).show();
		                       	  mActivity.runOnUiThread(new Runnable() {
		       						
		       						@Override
		       						public void run() {
		       							loginOut();
		       							Log.i("tag","登出成功");
		       						}
		       					});
		                         } else {
//		                             Toast.makeText(mActivity, "logout失败 i=" + i + " s=" + s, Toast.LENGTH_SHORT).show();
		                       	  Log.i("tag","logout失败 i= " + i + " s=" + s);
		                         }
		                         return false;
		                     }
		                 });
		            } else {
//		                Toast.makeText(instance, "init失败", Toast.LENGTH_SHORT).show();
		            	Log.i("tag","init失败");
		            }
		            return false;
		        }
		    });
		 
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {

		TTSDKV2.getInstance().prepare(applicationContext);

	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			TTSDKV2.getInstance().login(mactivity,"TT", new SdkCallback<String>() {
                @Override
                protected boolean onResult(int i, String s) {
                	Log.i("tag","i = " +i);
                    if (i == 0) {
                        uid = TTSDKV2.getInstance().getUid();
                        String token =  TTSDKV2.getInstance().getSession();
                        Log.i("tag","uid = " +uid);
                        Log.i("tag","token = " +token);
                        loginSuce(mactivity, uid, uid, token);
                        Toast("登录成功");
                    } else {
                        loginFail();
                        Toast("登录失败");
                        Log.i("tag","login失败 i=" + i + " s=" + s);
                    }
                    return false;
                }
            });

		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(final Activity mactivity, String morderid ,String paytoken) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Yayalog.loger("paytoken =" +paytoken);
		
		 //以下参数都是必须传入的（支付回调通过服务器配置）
        PayInfo payInfo = new PayInfo();
        payInfo.setRoleId(role_id);//角色id
        payInfo.setRoleName(role_name); //角色名称
        payInfo.setBody(YYWMain.mOrder.goods); //商品描述
        payInfo.setCpFee(YYWMain.mOrder.money/100);//CP订单金额
        payInfo.setCpTradeNo(morderid);//CP订单号
        payInfo.setServerId(Integer.parseInt(zone_id));//游戏服务器id
        payInfo.setServerName(zone_name);//游戏服务器名称
        payInfo.setExInfo(""); //CP扩展信息，该字段将会在支付成功后原样返回给CP
        payInfo.setSubject(YYWMain.mOrder.goods);//订单商品名称
        payInfo.setPayMethod(payInfo.PAY_METHOD_ALL); //支付方式
        payInfo.setChargeDate(new Date().getTime());//cp充值时间
        payInfo.setTs(paytoken);//通过服务器下单回传的字段（详情见服务器对接文档）
//        payInfo.setCpCallbackUrl(DeviceUtil.getGameInfo(mActivity, "callback"));
        TTSDKV2.getInstance().pay(mactivity, payInfo, new SdkCallback<String>() {
            @Override
            protected boolean onResult(int i, String payResponse) {
                if (i == 0) {
                    paySuce();
                    Toast("支付成功");
                } else {
                    payFail();
                    Toast("支付失败");
                }
                return true;
            }
        });

	}

	/**
	 * 退出
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				TTSDKV2.getInstance().uninit(paramActivity, new SdkCallback<String>() {
			        @Override
			        protected boolean onResult(int i, String s) {
			            if (i == 0) {
			            	Log.i("tag","uninit成功");
			            	callback.onExit();
			            } 
			            return false;
			        }

				});
				
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
		zone_id = zoneId;
		zone_name = zoneName;
//		1为角色登陆成功  2为角色创建  3为角色升级。
		if(Integer.parseInt(ext) == 1){
			Map<String,String> exMapParams = new ArrayMap<String, String>();
            exMapParams.put("scene_Id","");
            exMapParams.put("zoneId","");
            exMapParams.put("balance","");
            exMapParams.put("Vip","");
            exMapParams.put("partyName","");
            JSONObject jsonObject = new JSONObject(exMapParams);
            String exInfo = jsonObject.toString();
            TTSDKV2.getInstance().submitGameRoleInfo(paramActivity,"enter",Integer.parseInt(zoneId),zoneName, roleId, roleName, Integer.parseInt(roleLevel),1, Long.valueOf(10000),exInfo);
		}else if(Integer.parseInt(ext) == 2){
			Map<String,String> exMapParams = new ArrayMap<String, String>();
            exMapParams.put("scene_Id","");
            exMapParams.put("zoneId","");
            exMapParams.put("balance","");
            exMapParams.put("Vip","");
            exMapParams.put("partyName","");
            JSONObject jsonObject = new JSONObject(exMapParams);
            String exInfo = jsonObject.toString();
            TTSDKV2.getInstance().submitGameRoleInfo(paramActivity,"create",Integer.parseInt(zoneId),zoneName, roleId, roleName, Integer.parseInt(roleLevel),1, Long.valueOf(10000),exInfo);
		}else if(Integer.parseInt(ext) == 3){
			Map<String,String> exMapParams = new ArrayMap<String, String>();
            exMapParams.put("scene_Id","");
            exMapParams.put("zoneId","");
            exMapParams.put("balance","");
            exMapParams.put("Vip","");
            exMapParams.put("partyName","");
            JSONObject jsonObject = new JSONObject(exMapParams);
            String exInfo = jsonObject.toString();
            TTSDKV2.getInstance().submitGameRoleInfo(paramActivity,"upgrade",Integer.parseInt(zoneId),zoneName, roleId, roleName, Integer.parseInt(roleLevel),1, Long.valueOf(10000),exInfo);
		}
	}

	public static void onResume(Activity paramActivity) {
		TTSDKV2.getInstance().onResume(paramActivity);
		if(TTSDKV2.getInstance().isLogin()){
		    TTSDKV2.getInstance().showFloatView(paramActivity);
		  }

	}

	public static void onPause(Activity paramActivity) {
		TTSDKV2.getInstance().onPause(paramActivity);
		  TTSDKV2.getInstance().hideFloatView(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		TTSDKV2.getInstance().onDestroy(paramActivity);

	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {
		 TTSDKV2.getInstance().onActivityResult(paramActivity,paramInt1,paramInt2,paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		TTSDKV2.getInstance().onNewIntent(paramIntent);

	}

	public static void onStart(Activity paramActivity) {
		TTSDKV2.getInstance().onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		TTSDKV2.getInstance().onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {
	}

	public static void onStop(Activity paramActivity) {
		TTSDKV2.getInstance().onStop(paramActivity);
	}
	
	public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		TTSDKV2.getInstance().onRequestPermissionsResult(mActivity,requestCode, permissions, grantResults);
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
