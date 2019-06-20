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
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.game.handler.LoginHandler;
import com.huawei.android.hms.agent.game.handler.SaveInfoHandler;
import com.huawei.android.hms.agent.pay.PaySignUtil;
import com.huawei.android.hms.agent.pay.handler.GetOrderHandler;
import com.huawei.android.hms.agent.pay.handler.PayHandler;
import com.huawei.hms.support.api.entity.game.GamePlayerInfo;
import com.huawei.hms.support.api.entity.game.GameUserData;
import com.huawei.hms.support.api.entity.pay.OrderRequest;
import com.huawei.hms.support.api.entity.pay.PayReq;
import com.huawei.hms.support.api.entity.pay.PayStatusCodes;
import com.huawei.hms.support.api.pay.OrderResult;
import com.huawei.hms.support.api.pay.PayResultInfo;
import com.huawei.updatesdk.sdk.service.download.h;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

public class YaYawanconstants {

	//	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit = false;
	private static String APP_ID;
	private static String CP_APP_ID;
	//	private static String publickey;
	//	private static String privatekey;
	private static String uid;
	private static String token;
	private static String bufanuid;
	private static String bufantoken;
	private static String paystatus = "1";// 订单状态
	private static int connectcode = 0;

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(final Activity mactivity) {
		mActivity = mactivity;
		HMSAgent.init(mactivity);
		Yayalog.loger("YaYawanconstants初始化sdk");
		// 首个界面需要调用connect进行连接
		//		Connect(mactivity);
		Log.i("tag","YaYawanconstants初始化sdk结束");
	}


	public static void Connect(final Activity mactivity) {
		Log.i("tag", "Connect+Connect");
		HMSAgent.connect(mactivity, new ConnectHandler() {
			@Override
			public void onConnect(int rst) {
				Log.i("tag", "HMS connect end:" + rst);
				if(rst == 13){
					Connect(mactivity);
				}else {
					//					isinit = true ;
					//					HMSAgent.checkUpdate(mActivity);
					HMSAgent.checkUpdate(mActivity, new CheckUpdateHandler() {
						@Override
						public void onResult(int rst) {
							Log.i("tag","checkUpdate-onResult = " +rst);
						}
					});
					HMSAgent.Game.showFloatWindow(mActivity);
					loginstart();
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
		Connect(mactivity);
		Log.i("tag","isinit = " +isinit);
		//		if(isinit){
		APP_ID = "" + DeviceUtil.getGameInfo(mActivity, "appid");
		CP_APP_ID = "" + DeviceUtil.getGameInfo(mActivity, "cpid");
		Log.i("tag","APP_ID = " +APP_ID);
		Log.i("tag","CP_APP_ID = " +CP_APP_ID);
		//			loginstart();
		//		}else{
		//			inintsdk(mactivity);
		//		}
	}

	private static void loginstart() {
		HMSAgent.Game.login(new LoginHandler() {
			@Override
			public void onResult(int retCode, final GameUserData userData) {
				Log.i("tag","retCode = "+retCode);
				Log.i("tag","userData = "+userData);
				if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && userData != null) {
					Log.i("tag", "game login: onResult: retCode=" + retCode + "  user=" + userData.getDisplayName() + "|" + userData.getPlayerId() + "|" + userData.getIsAuth() + "|" + userData.getPlayerLevel());
					// 当登录成功时，此方法会回调2次，
					// 第1次：只回调了playerid；特点：速度快；在要求快速登录，并且对安全要求不高时可以用此playerid登录
					// 第2次：回调了所有信息，userData.getIsAuth()为1；此时需要对登录结果进行验签
					if (userData.getIsAuth() == 1) {
						token = userData.getTs()+"##"+userData.getPlayerLevel()+"##"+userData.getGameAuthSign();
						uid = userData.getPlayerId();
						Log.i("tag", "uid =" + uid);
						Log.i("tag", "token =" + token);

						Log.i("tag", "userData.getTs() =" + userData.getTs());
						Log.i("tag", "userData.getPlayerLevel() =" + userData.getPlayerLevel());
						Log.i("tag", "userData.getGameAuthSign() =" + userData.getGameAuthSign());
						loginSuce(mActivity, userData.getPlayerId(), userData.getPlayerId(), token);
						//						loginSuce(mActivity, userData.getPlayerId(), userData.getPlayerId(), "");//捕鱼风暴华为
						//	                    	HMSAgent.Game.showFloatWindow(mActivity);
						Toast("登录成功");
					}
				} else {
					Log.i("tag", "game login: onResult: retCode=" + retCode);
					loginFail();
					Toast("登录失败");
					//					loginstart();
				}
			}

			@Override
			public void onChange() {
				// 此处帐号登录发生变化，需要重新登录
				//	                showLog("game login: login changed!");
				Log.i("tag", "game login: login changed!");
				login(mActivity);
			}

		}, 1);
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	//	public static void pay(Activity mactivity, final String morderid,String privatekey,String publickey) {
	public static void pay(Activity mactivity, final String morderid,String privatekey) {
		Yayalog.loger("YaYawanconstantssdk支付");
		Yayalog.loger("YaYawanconstantssdk支付morderid"+morderid);
		Yayalog.loger("YaYawanconstantssdk支付privatekey"+privatekey);
		PayReq payReq = createPayReq(YYWMain.mOrder.money/100+"", morderid,privatekey);
		//		final String pay_pub_key = "" + DeviceUtil.getGameInfo(mActivity, "paypubkey");
		Log.i("tag", "pay-payReq = "+payReq);
		//		Sputils.putSPstring("orderid", morderid, mactivity);
		//		Sputils.putSPstring("privatekey", privatekey, mactivity);
		//		Sputils.putSPstring("publickey", publickey, mactivity);
		HMSAgent.Pay.pay(payReq, new PayHandler() {
			@Override
			public void onResult(int retCode, PayResultInfo payInfo) {
				Log.i("tag", "pay-retCode = "+retCode);
				Log.i("tag", "pay-payInfo = "+payInfo);
				if (retCode == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && payInfo != null) {
					//                    boolean checkRst = PaySignUtil.checkSign(payInfo, pay_pub_key);
					//                    showLog("game pay: onResult: pay success and checksign=" + checkRst);
					//                    if (checkRst) {
					// 支付成功并且验签成功，发放商品
					//                    	paySuce();
					//                    	Toast("支付成功");
					//                    } else {
					// 签名失败，需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。
					//                    	payFail();
					//                    	Toast("支付失败");
					//                    }
					Log.i("tag", "pay success and checksign");
					HttpPost(uid, token, morderid);
				} else if (retCode == HMSAgent.AgentResultCode.ON_ACTIVITY_RESULT_ERROR
						|| retCode == PayStatusCodes.PAY_STATE_TIME_OUT
						|| retCode == PayStatusCodes.PAY_STATE_NET_ERROR) {
					// 需要查询订单状态：对于没有服务器的单机应用，调用查询订单接口查询；其他应用到开发者服务器查询订单状态。
					Log.i("tag", "需要查询订单状态");
					HttpPost(uid, token, morderid);
				} else {
					//                    showLog("game pay: onResult: pay fail=" + retCode);
					// 其他错误码意义参照支付api参考
					Log.i("tag", "其他错误码="+retCode);
					HttpPost(uid, token, morderid);
				}
			}

		});
	}

	private static PayReq createPayReq(String money,String orderid,String privatekey) {
		PayReq payReq = new PayReq();
		//商品名称
		payReq.productName = YYWMain.mOrder.goods;
		//商品描述
		payReq.productDesc = YYWMain.mOrder.goods;
		// 商户ID
		payReq.merchantId = CP_APP_ID;
		// 应用ID
		payReq.applicationID = APP_ID;
		// 支付金额
		payReq.amount = money + ".00";
		// 支付订单号
		payReq.requestId = orderid;
		// 国家码
		payReq.country = "CN";
		//币种
		payReq.currency = "CNY";
		// 渠道号
		payReq.sdkChannel = 1;
		// 回调接口版本号
		payReq.urlVer = "2";

		// 商户名称，必填，不参与签名。会显示在支付结果页面
		payReq.merchantName = ""
				+ DeviceUtil.getGameInfo(mActivity, "companyname");
		//分类，必填，不参与签名。该字段会影响风控策略
		// X4：主题,X5：应用商店,	X6：游戏,X7：天际通,X8：云空间,X9：电子书,X10：华为学习,X11：音乐,X12 视频,
		// X31 话费充值,X32 机票/酒店,X33 电影票,X34 团购,X35 手机预购,X36 公共缴费,X39 流量充值
		payReq.serviceCatalog = "X6";
		//商户保留信息，选填不参与签名，支付成功后会华为支付平台会原样 回调CP服务端
		//	        payReq.extReserved = "这是测试支付的功能";

		//对支付请求信息进行签名,建议CP在服务器端储存签名私钥，并在服务器端进行签名操作。
		//下面调用的工具方法，供实现参考
		payReq.sign = PaySignUtil.calculateSignString(payReq, privatekey);

		return payReq;
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
		//		chaxun();
		paramActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

					@Override
					public void onSuccess(User arg0, int arg1) {
						callback.onExit();
						//						mActivity.finish();
						//						System.exit(0);
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
		if (Integer.parseInt(ext) == 1) {
			//			if (isyoumeng == 1) {
			//				Log.i("tag", "友盟进入游戏");
			//				MobclickAgent.onProfileSignIn(uid);
			//			}
			GamePlayerInfo gpi = new GamePlayerInfo();
			gpi.area = roleId;
			gpi.rank = roleLevel;
			gpi.role = roleName;
			gpi.sociaty = zoneName;
			HMSAgent.Game.savePlayerInfo(gpi, new SaveInfoHandler() {
				@Override
				public void onResult(int retCode) {
					Log.i("tag", "game savePlayerInfo: onResult=" + retCode);
				}
			});
		}
	}

	public static void onResume(Activity paramActivity) {
		HMSAgent.Game.showFloatWindow(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		HMSAgent.Game.hideFloatWindow(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		HMSAgent.destroy();
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
		mActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (YYWMain.mPayCallBack != null) {
					YYWMain.mPayCallBack.onPayFailed(null, null);
				}
			}
		});
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
	 * 请求获取支付订单结果
	 * 
	 * @param uid
	 *            为用户唯一id
	 * @param token
	 *            为登录返回的token
	 * @param orderid
	 *            为支付订单号
	 */
	public static void HttpPost(final String uid, final String token,
			final String orderid) {
		//					bufanuid = YYWMain.mUser.yywuid;
		//					bufantoken = YYWMain.mUser.yywtoken;
		//					params.add(new BasicNameValuePair("app_id", DeviceUtil
		//							.getAppid(mActivity)));
		//					params.add(new BasicNameValuePair("uid", bufanuid));
		//					params.add(new BasicNameValuePair("token", bufantoken));
		//					params.add(new BasicNameValuePair("billid", orderid));
		//
		//							if ((paystatus.equals("2")) || (paystatus.equals("3"))) {
		//								paySuce();
		//								Log.i("tag", "支付成功");
		//								Toast("支付成功");
		//							} else {
		//								payFail();
		//								Toast("支付失败");
		//								Log.i("tag", "支付失败");
		//							}
		bufanuid = YYWMain.mUser.yywuid;
		bufantoken = YYWMain.mUser.yywtoken;
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/pay/order_status/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("uid", bufanuid);
		requestParams.addBodyParameter("token", bufantoken);
		requestParams.addBodyParameter("billid", orderid);
		httpUtil.send(HttpMethod.POST, url,requestParams,
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
					paystatus = obj.getString("status");
					Yayalog.loger("status ="+paystatus);
					if ((paystatus.equals("2")) || (paystatus.equals("3"))) {
						paySuce();
						Log.i("tag", "支付成功");
						Toast("支付成功");
					} else {
						payFail();
						Toast("支付失败");
						Log.i("tag", "支付失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	//	public static void chaxun(){
	//		final String reqId = Sputils.getSPstring("orderid", "orderid", mActivity);
	//		String pay_priv_key = Sputils.getSPstring("privatekey", "privatekey", mActivity);
	//		final String pay_pub_key = Sputils.getSPstring("publickey", "publickey", mActivity);
	//		OrderRequest or = new OrderRequest();
	//
	//Log.i("tag", "reqid = " + reqId);
	//Log.i("tag", "pay_priv_key = " + pay_priv_key);
	//Log.i("tag", "pay_pub_key = " + pay_pub_key);
	//		or.setMerchantId(CP_APP_ID);
	//		or.setRequestId(reqId);
	//		or.setTime(String.valueOf(System.currentTimeMillis()));
	//		or.setKeyType("1");
	//		//对查询订单请求信息进行签名,建议CP在服务器端储存签名私钥，并在服务器端进行签名操作。| To sign the query order request information, it is recommended that CP store the signature private key on the server side and sign the operation on the server side.
	//		//在服务端进行签名的cp可以将getStringForSign返回的待签名字符串传给服务端进行签名 | The CP, signed on the server side, can pass the pending signature string returned by Getstringforsign to the service side for signature
	//		or.sign = PaySignUtil.rsaSign(PaySignUtil.getStringForSign(or), pay_priv_key);
	//		HMSAgent.Pay.getOrderDetail(or, new GetOrderHandler() {
	//	        @Override
	//	        public void onResult(int retCode, OrderResult checkPayResult) {
	//	            showLog("game checkPay: requId="+reqId+"  retCode=" + retCode);
	//	            if (checkPayResult != null && checkPayResult.getReturnCode() == retCode) {
	//	                // 处理支付业务返回码
	//	                if (retCode == 0) {
	//	                    boolean checkRst = PaySignUtil.checkSign(checkPayResult, pay_pub_key);
	//	                    if (checkRst) {
	//	                        // 支付成功，发放对应商品
	//	                        showLog("支付成功，发放商品");
	//	                    } else {
	//	                        // 验签失败，当支付失败处理
	//	                        showLog("验证签名失败，支付失败");
	//	                    }
	//
	//	                    // 不需要再查询
	//	                    removeCacheRequestId(checkPayResult.getRequestId());
	//	                    
	//	                } else if (retCode == PayStatusCodes.ORDER_STATUS_HANDLING
	//	                        || retCode == PayStatusCodes.ORDER_STATUS_UNTREATED
	//	                        || retCode == PayStatusCodes.PAY_STATE_TIME_OUT) {
	//	                    // 未处理完，需要重新查询。如30分钟后再次查询。超过24小时当支付失败处理
	//	                	chaxun();
	//	                } else if (retCode == PayStatusCodes.PAY_STATE_NET_ERROR) {
	//	                    // 网络失败，需要重新查询
	////	                	chaxun();
	//	                } else {
	//	                    // 支付失败，不需要再查询
	//	                	payFail();
	//	                    removeCacheRequestId(reqId);
	//	                }
	//	            } else {
	//	                // 没有结果回来，需要重新查询。如30分钟后再次查询。超过24小时当支付失败处理
	//	            }
	//	        }
	//
	//	    });
	//	}
	//	private void addRequestIdToCache(String requestId) {
	//        SharedPreferences sp = mActivity.getSharedPreferences("pay_request_ids", 0);
	//        sp.edit().putBoolean(requestId, false).commit();
	//    }
	//	  private static void removeCacheRequestId(String reqId) {
	//	        SharedPreferences sp = mActivity.getSharedPreferences("pay_request_ids", 0);
	//	        sp.edit().remove(reqId).commit();
	//	    }
}
