package com.yayawan.impl;

import java.util.ArrayList;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import com.hanfeng.guildsdk.Constants;
import com.hanfeng.nsdk.NSdk;
import com.hanfeng.nsdk.NSdkListener;
import com.hanfeng.nsdk.NSdkStatusCode;
import com.hanfeng.nsdk.bean.NSAppInfo;
import com.hanfeng.nsdk.bean.NSLoginResult;
import com.hanfeng.nsdk.bean.NSPayInfo;
import com.hanfeng.nsdk.bean.NSRoleInfo;
import com.hanfeng.nsdk.exception.NSdkException;
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
import com.unionpay.tsmservice.request.InitRequestParams;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	 private static NSdk nsdk = null;
	 
	 private static YYWExitCallback exitcallback;
	 
	 public static String uid;
	 public static String token;
	 
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
		init(mactivity);
		
		 //初始化回调监听事件
        initCallBack();
	}

	public static void init(Activity mactivity){
		//横竖屏配置 true表示竖屏，false表示横屏 
//		（闪屏的横竖屏改变需要在 assets/hfnsdk/config/sdkconf.xml 这个文件里的landscape配置）
		 Constants.isPORTRAIT = DeviceUtil.isLandscape(mActivity)?false:true; 
		NSAppInfo appinfo = new NSAppInfo();
		// TODO: 在实际开发中，请填上实际分配的appId和appKey
		appinfo.appId = DeviceUtil.getGameInfo(mactivity, "hf_appid");
		appinfo.appKey = DeviceUtil.getGameInfo(mactivity, "hf_appkey");;
		 nsdk = NSdk.getInstance();
		try {
            nsdk.init(mActivity, appinfo, new NSdkListener<String>() {
                @Override
                public void callback(int code, String response) {
                	if (code == NSdkStatusCode.INIT_SUCCESS) {
        				//初始化成功处理，可按照以下方式处理:
                		isinit = true ;
        				Log.i("tag","初始化成功");
        			} else {
        				//初始化失败处理，可按照以下方式处理:
        				Log.i("tag","初始化失败");
        			}
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	 private static void initCallBack() {
	        //切换账号监听
	        nsdk.setAccountSwitchListener(new NSdkListener<String>() {
	            @Override
	            public void callback(int code, String resp) {
	                Log.i("tag", "切换账号监听 resp = " +resp);
	                if (code == NSdkStatusCode.SWITCH_SUCCESS) {
	                    //todo清空游戏信息，回到登陆界面
	                	mActivity.runOnUiThread(new Runnable() {
							public void run() {
								loginOut();
								Log.i("tag","登出成功");
							}
						});
	                } else if (code == NSdkStatusCode.SWITCH_FAILURE) {
	                	Log.i("tag","登出失败");
	                }
	            }
	        });
	        //注销监听
	        nsdk.setLogoutListener(new NSdkListener<String>() {
	            @Override
	            public void callback(int code, String resp) {
	            	 Log.i("tag", "注销监听 resp = " +resp);
	                if (code == NSdkStatusCode.LOGOUT_SUCCESS) {
	                    //todo清空游戏信息
	                	mActivity.runOnUiThread(new Runnable() {
							public void run() {
								loginOut();
								Log.i("tag","登出成功");
							}
						});
	                } else if (code == NSdkStatusCode.LOGOUT_FAILURE) {
	                	Log.i("tag","登出失败");
	                }
	            }
	        });
	        //退出监听
	        nsdk.setOnExitListener(new NSdkListener<String>() {
	            @Override
	            public void callback(int code, String resp) {
	            	Log.i("tag", "退出监听 resp = " +resp);
	                if (code == NSdkStatusCode.EXIT_COMFIRM) {
	                    //todo清空游戏信息
	                    exitcallback.onExit();
	                } else if (code == NSdkStatusCode.EXIT_CANCLE) {

	                }
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
			 try {
			nsdk.login(mactivity, new NSdkListener<NSLoginResult>() {

                @Override
                public void callback(int code, NSLoginResult response) {
                	Log.i("tag", "登录返回 = " +response.msg);
                    Toast.makeText(mActivity, response.msg, Toast.LENGTH_LONG).show();
                    switch (code) {
                        case NSdkStatusCode.LOGIN_SUCCESS:
                           String sid = response.sid;
                           String hfuid = response.uid;
                           HttpPost(sid,hfuid);
//                           loginSuce(mactivity, uid, uid, sid);
                            break;
                        case NSdkStatusCode.LOGIN_FAILURE:
                				// TODO: NSDK登录失败处理，可按照以下处理方式处理：
                        	loginFail();
                				break;
                        case NSdkStatusCode.LOGIN_CANCEL:
                        	loginFail();
                            break;
                        case NSdkStatusCode.LOGIN_OTHER:
                        	loginFail();
                            break;
                        case NSdkStatusCode.INIT_FAILURE:
                        	loginFail();
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (NSdkException e) {
            e.printStackTrace();
        }
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
		String goods = YYWMain.mOrder.goods;
		int waresid = getWaresid(goods);
		 final NSPayInfo info = new NSPayInfo();
	        info.gameName = DeviceUtil.getGameInfo(mactivity, "gamename");
	        info.productId = waresid+"";
	        info.productName = YYWMain.mOrder.goods;
	        info.productDesc = YYWMain.mOrder.goods;
	        info.price = Integer.parseInt(YYWMain.mOrder.money+"");//金额
	        info.ratio = 20; //1元RMB对应金币数量
	        info.buyNum = 1;//购买数量
	        info.coinNum = 10;//金币数量
	        info.uid = uid; //传sid验证后的uid
	        info.serverId = Integer.parseInt(zone_id); //区服ID 必传
	        info.roleId = role_id;//角色id 必传
	        info.roleName = role_name; //角色名 必传
	        info.privateField = morderid;//订单透传字段
	        info.roleLevel = Integer.parseInt(role_level);
		 try {
	            nsdk.pay(mactivity, info, new NSdkListener<String>() {
	                @Override
	                public void callback(int code, String response) {
	                	Log.i("tag", "code=" + code + ", response=" + response);
	        			switch (code) {
	        				case NSdkStatusCode.PAY_SUCCESS:
	        					// TODO: 支付成功结果处理，例如：
	        					paySuce();
	        					Toast("支付成功");
	        					break;
	        				case NSdkStatusCode.PAY_FAILURE:
	        					// TODO: 支付失败结果处理，例如：
	        					payFail();
	        					Toast("支付失败");
	        					break;
	        				case NSdkStatusCode.PAY_CANCEL:
	        					// TODO: 取消支付结果处理
	        					payFail();
	        					Toast("支付取消");
	        					break;
	        				case NSdkStatusCode.PAY_PAYING:
	        					// TODO: 正在支付，支付结果未知结果处理
	        					payFail();
	        					Toast("正在处理该笔订单");
	        					break;
	        				case NSdkStatusCode.PAY_NOCALLBACK:
	        					// TODO: 某些渠道支付无回调情况处理
	        					payFail();
	        					Toast("支付失败");
	        					break;
	        				case NSdkStatusCode.PAY_OTHER:
	        					payFail();
	        					Toast("支付失败");
	        				default:
	        					// TODO: 其他支付错误回调结果
	        					payFail();
	        					Toast("支付失败");
	        					break;

	                }
	            }
	            });
	        } catch (Exception e) {
	            e.printStackTrace();
	            Log.i("tag", "支付出现异常："+ e);
	        }
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
	public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");
		exitcallback = callback;
paramActivity.runOnUiThread(new Runnable() {
	
	@Override
	public void run() {
		//退出
        try {
            nsdk.exit(paramActivity);
        } catch (NSdkException e) {
            e.printStackTrace();
        }
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
		
		//1为角色登陆成功  2为角色创建  3为角色升级。
		if(Integer.parseInt(ext) == 1){
			submitGameInfo(paramActivity , "1");
		}else if(Integer.parseInt(ext) == 2){
			submitGameInfo(paramActivity , "2");
		}else if(Integer.parseInt(ext) == 3){
			submitGameInfo(paramActivity , "3");
		}
	}
	
	  private static void submitGameInfo(Activity mActivity,String type) {
	        NSRoleInfo roleinfo = new NSRoleInfo();
	        roleinfo.userId = uid; //传Sid验证后的uid
	        roleinfo.roleId = role_id; //角色ID
	        roleinfo.roleName = role_name;//角色名
	        roleinfo.roleLevel = role_level;//角色等级
	        roleinfo.zoneId = Integer.parseInt(zone_id);//区服ID
	        roleinfo.zoneName = zone_name;//区服名字
	        roleinfo.dataType = type;//数据类型 1，进入游戏（登录后）；2，创建角色；3角色升级
	        Log.i("tag", "上传信息：" + roleinfo.toJson());
	        nsdk.submitGameInfo(mActivity, roleinfo);
	    }

	public static void onResume(Activity paramActivity) {
		nsdk.onResume(paramActivity);
		NSdk.getInstance().showToolBar(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		nsdk.onPause(paramActivity);
		NSdk.getInstance().hideToolBar(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		nsdk.onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {
		nsdk.onActivityResult(paramActivity, paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		nsdk.onNewIntent(mActivity,paramIntent);
	}

	public static void onStart(Activity paramActivity) {
		nsdk.onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		nsdk.onRestart(paramActivity);
	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		nsdk.onStop(paramActivity);
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
		nsdk.onConfigurationChanged(mActivity, newConfig);
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
	/**
	 * 
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	private static void HttpPost(final String sid ,final String hfuid) {
		token = sid;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/data/get_uid/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("hf_sid", sid);
		requestParams.addBodyParameter("hf_version", nsdk.getSdkVersion());
		requestParams.addBodyParameter("hf_uid", hfuid);
		requestParams.addBodyParameter("hf_channel", nsdk.getChannel());
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
