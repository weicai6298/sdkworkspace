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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.sdk.tysdk.TYFactory;
import com.sdk.tysdk.bean.ErrorMsg;
import com.sdk.tysdk.bean.LoginSucParam;
import com.sdk.tysdk.bean.PaymentCallbackInfo;
import com.sdk.tysdk.bean.PaymentErrorMsg;
import com.sdk.tysdk.bean.RealNameInfo;
import com.sdk.tysdk.bean.TYParam;
import com.sdk.tysdk.bean.TYPayParam;
import com.sdk.tysdk.interfaces.InitSDKListener;
import com.sdk.tysdk.interfaces.NeedLogoutCallBack;
import com.sdk.tysdk.interfaces.OnLoginListener;
import com.sdk.tysdk.interfaces.OnPaymentListener;
import com.sdk.tysdk.interfaces.UserLoginOutFinishCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	
	public static String role_id = "roleid";
	public static String server_id = "2";
	

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		
		TYParam mTyParam = new TYParam();
		mTyParam.TY_APPID = ""+DeviceUtil.getGameInfo(mactivity, "TY_APPID");                     //此处为申请的    TY_APPID
		mTyParam.TY_CLIENTID = ""+DeviceUtil.getGameInfo(mactivity, "TY_CLIENTID");                 //此处为申请的    TY_CLIENTID
		mTyParam.TY_CLIENTKEY = ""+DeviceUtil.getGameInfo(mactivity, "TY_CLIENTKEY");                 //此处为申请的    TY_CLIENTKEY
		TYFactory.getTYApi()
		        .initSDK(
		                mactivity,          //传入当前Activity实例
		                mTyParam,                    //SDK接入参数  
		                false,   //是否是调试模式 true 调试模式,fasle 正式模式
		                new InitSDKListener() {    //是否初始化成功回调
		    @Override
		    public void onSuccess() {
		        //初始化成功 在等待闪屏结束后调用登录
		    	isinit = true ;
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
			TYFactory.getTYApi().Login(mactivity, new OnLoginListener() {
		    @Override
		    public void loginSuccess(LoginSucParam logincallback, RealNameInfo realNameInfo) {
		//用户身份信息
		        String mem_id = logincallback.mem_id;           //用户账户ID
		        String user_token = logincallback.user_token;   //用户登录token
loginSuce(mactivity, mem_id, mem_id, user_token);
		        //用户实名信息,可能为null 未实名的情况 实名信息为空不用实名则无需要理会
		        String realName = realNameInfo.getRealName();       //名字
		        String realNameID = realNameInfo.getRealNameID();   //实名证件号码
		 
		       //调用浮点显示 不能去掉
		        TYFactory.getTYApi().showFloatView(mactivity);
		        
		    }
		    @Override
		    public void loginError(ErrorMsg errorMsg) {
		        // 登录失败的回调
		        int code = errorMsg.code;// 登录失败的状态码
		        String msg = errorMsg.msg;// 登录失败的消息提示
		        loginFail();
		        Log.i("tag","登录失败状态码="+code);
				Log.i("tag","登录失败消息提示="+msg);
		    }
		 
		}, new NeedLogoutCallBack() {
		    @Override
		    public void onLoginOut() {
		        //强制退出回调,token失效或者账户在其他地点登录
		        //执行退出逻辑
		    	mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loginOut();
					}
				});
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
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		TYPayParam payParam = new TYPayParam();
		payParam.roleid = role_id;                  //角色id           (必填)
		payParam.money = (double) (YYWMain.mOrder.money/100);                 //购买游戏币        (必填)
		payParam.serverid =server_id;                 //服务器id          (必填)
		payParam.productname = YYWMain.mOrder.goods;             //产品名称          (必填)
		payParam.productdesc = YYWMain.mOrder.goods;             //产品描述          (必填)
		payParam.attach = morderid;               //商品扩展参数如游戏订单编号(必填)
		payParam.remark = "";                  //备注
		TYFactory.getTYApi().Pay(
		        mactivity,       //传入当前activity实例 
		        payParam,                //支付参数
		        new OnPaymentListener() {//支付回调
		    @Override
		    public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
			//注意不能作为凭证 要以服务器的回调为准
		        //充值金额数
		        String msg = callbackInfo.msg;
		        //充值成功的提示
		        double money = callbackInfo.money;
		        paySuce();
		        Toast("支付成功");
		    }

		    @Override
		    public void paymentError(PaymentErrorMsg errorMsg) {
		        //预充值的金额
		        double money = errorMsg.money;
		        //支付失败提示消息
		        String msg = errorMsg.msg;
		        //支付失败状态码
		        int code = errorMsg.code;
		        
		        payFail();
				Toast("支付失败");
				Log.i("tag","充值失败：code:" + code);
				Log.i("tag","充值失败：ErrorMsg:" + msg);
				Log.i("tag","预充值的金额:" + money);
		    }
		});

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
				
		 TYFactory.getTYApi().LogOut(
	                paramActivity,      //当前activity实例
	                new UserLoginOutFinishCallBack() {  //调用退出回调
	                    @Override
	                    public Activity LoginOut() {
	                        //返回最外层activity的实例,由SDK来关闭app
	                        //如果传null 则由调用者自行决定是否关闭app
	                        return paramActivity;
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
		role_id = roleId;
		server_id = zoneId;
		if(role_id.equals("")){
			role_id = "123";
		}
		if(server_id.equals("")){
			server_id = "1";
		}
		Log.i("tag","role_id = "+role_id);
		Log.i("tag","server_id = "+server_id);
		JSONObject jsonObject = new JSONObject();
		try {
		    jsonObject.put("roleid",role_id);     //当前登录的玩家角色 ID，必须为数字
		    jsonObject.put("rolename",roleName);  //当前登录的玩家角色名，不能为空，不能为 null
		    jsonObject.put("rolelevel",roleLevel); //当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入 1
		    jsonObject.put("zoneid", server_id); //当前登录的游戏区服 ID，必须为数字，且不能为 0，  若无，传入 1
		    jsonObject.put("zonename", zoneName);      //当前登录的游戏区服名称，不能为空，不能为 null
		    jsonObject.put("balance", "0");       //用户游戏币余额，必须为数字，若无，传入 0
		    jsonObject.put("vip", "1");            //当前用户 VIP 等级，必须为数字，若无，传入 1
		    jsonObject.put("partyname", "无帮派");       //当前角色所属帮派，不能为空，不能为 null，若无， 传入“无帮派”
		    jsonObject.put("rolectime", roleCTime);        //单位为秒，创建角色的时间（时间戳）
		    jsonObject.put("rolelevelimtime", "");  //单位为秒，角色等级变化时间
		    Log.i("tag","调用上传信息");
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		TYFactory.getTYApi().setInfo(paramActivity, jsonObject);
	}

	public static void onResume(Activity paramActivity) {
		// 显示悬浮窗  传入当前Activity实例
	    TYFactory.getTYApi().showFloatView(paramActivity);

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
		// 移除悬浮窗  传入当前Activity实例
	    TYFactory.getTYApi().removeFloatView(paramActivity);

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

}
