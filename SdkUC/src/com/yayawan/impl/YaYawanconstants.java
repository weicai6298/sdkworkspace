package com.yayawan.impl;

import java.util.HashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import cn.uc.gamesdk.sa.UCGameSdk;
import cn.uc.gamesdk.sa.iface.open.SDKConst;
import cn.uc.gamesdk.sa.iface.open.UCCallbackListener;
import cn.uc.gamesdk.sa.iface.open.UCGameSDKStatusCode;
import cn.uc.paysdk.SDKCore;
import cn.uc.paysdk.face.commons.PayResponse;
import cn.uc.paysdk.face.commons.Response;
import cn.uc.paysdk.face.commons.SDKCallbackListener;
import cn.uc.paysdk.face.commons.SDKError;
import cn.uc.paysdk.face.commons.SDKProtocolKeys;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.main.YaYaWan;


public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	
	private static boolean ispayinit=false;

	private static Handler mHandler; //处理JY支付接口回调

	public final static int HANDLER_SHOW_ERROR_DIALOG = -1;// 显示错误提示
	public final static int HANDLER_INIT_SUCC = 1;    //支付SDK初始化成功
	public final static int HANDLER_PAY_CALLBACK = 2; //支付SDK支付回调
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		UCGameSdk.defaultSdk().setCallback(SDKConst.SDK_INIT_LISTENER, new UCCallbackListener<String>() {

			@Override
			public void callback(int statuscode, String msg) {
				switch (statuscode) {
				case UCGameSDKStatusCode.SUCCESS:
//					mIsInitSucess = true;
					break;

				default:
//					mBtnPay.setEnabled(false);
					break;
				}
//				addOutputResult(msg);
			}
		});

		try {
			Bundle payInitData = new Bundle();  //需要new出对象，不能为null
			UCGameSdk.defaultSdk().init(mActivity, payInitData);
			isinit = true;
			Log.i("tag","isinit="+isinit);
		}
		catch (Exception e) {

		}
		
		UCGameSdk.defaultSdk().setCallback(
				SDKConst.PAY_INIT_LISTENER, 
				new SDKCallbackListener() {

					@SuppressWarnings("unused")
					@Override
					public void onSuccessful(int statusCode, Response response) {
						// 支付成功回调
//						mBtnPay.setEnabled(true);
						if (response.getType() == Response.LISTENER_TYPE_INIT)  // 用于处理初始化消息
						{
//							mActivity.runOnUiThread(new Runnable() {
//								
//								@Override
//								public void run() {
//									Toast.makeText(mActivity, "支付初始化成功啦!", Toast.LENGTH_LONG).show();
									ispayinit = true;
									Log.i("tag","ispayinit="+ispayinit);
//								}
//							});
						}
						else if (response.getType() == Response.LISTENER_TYPE_PAY) // 用于处理 历史订单的响应确认消息
						{
							/**
							 *  当为支付回调时，除了返回订单的详细内容之外，
							 *  接入者必须设置response参数中的setMessage方法
							 *  此方法必须在回调的同一线程中调用，
							 *  并且不要进行耗时操作，否则会导致线程阻塞
							 *  相关异步操作可以在handler中进行，另起工作者线程
							 *  设置为
							 *      Response.OPERATE_SUCCESS_MSG
							 *              代表CP成功进行相关动作时，响应SDK
							 *      Response.OPERATE_FAIL_MSG
							 *              代表CP进行相关动作失败时，响应SDK
							 */
							response.setMessage(Response.OPERATE_SUCCESS_MSG); // 重要 确认收到

							try
							{
								//支付回调的订单详细内容
								JSONObject data = new JSONObject(response.getData());
								String orderId = data.getString(PayResponse.CP_ORDER_ID);   // CP 订单号
								String tradeId = data.getString(PayResponse.TRADE_ID);      // 交易号
								String payMoney = data.getString(PayResponse.PAY_MONEY);        // 支付金额
								String payType = data.getString(PayResponse.PAY_TYPE);                // 支付类型 207 支付宝快捷支付
								String orderStatus = data.getString(PayResponse.ORDER_STATUS); // 订单状态 00 成功 01 失败
								String orderFinishTime = data.getString(PayResponse.ORDER_FINISH_TIME); // 订单完成时间
								String productName = data.getString(PayResponse.PRO_NAME);// 道具名称
								String extendInfo = data.optString(PayResponse.EXT_INFO); //商品扩展信息
								String attachInfo = data.optString(PayResponse.ATTACH_INFO); // 附加透传信息

							}
							catch(JSONException ex)
							{
								ex.printStackTrace();
							}
						}
					}

					@Override
					public void onErrorResponse(SDKError error) {
						// 失败,该回调是在子线程中调用，UI操作需转到UI线程执行
						Log.i("tag", "error="+error);
//						mActivity.runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								Toast.makeText(mActivity, "支付初始化失败!", Toast.LENGTH_LONG).show();
//							}
//						});
					}
				});


	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub

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
			Log.i("tag","tempuid="+tempuid);
			Log.i("tag","uid="+uid);
			loginSuce(mactivity, uid, uid, uid);
			Log.i("tag","登录");
		}else {
			Log.i("tag","登录1");
			loginSuce(mactivity, tempuid, tempuid, tempuid);
			Log.i("tag","登录2");
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
		Intent payIntent = new Intent(); //支付参数封装，具体参数请参考接入文档
		//CP_ORDER_ID非必传，但是如果传了，需要保证唯一性，即不能重复下单
		payIntent.putExtra(SDKProtocolKeys.CP_ORDER_ID, morderid);

		String gamename = ""+DeviceUtil.getGameInfo(mActivity, "gamename");
		Log.i("tag","gamename="+gamename);
		//必传字段信息
		payIntent.putExtra(SDKProtocolKeys.APP_NAME, gamename);
		payIntent.putExtra(SDKProtocolKeys.PRODUCT_NAME, YYWMain.mOrder.goods);
		payIntent.putExtra(SDKProtocolKeys.AMOUNT, YYWMain.mOrder.money/100+""); //订单价格，数字类型(可含小数点)

//		 payIntent.putExtra(SDKProtocolKeys.NOTIFY_URL,"http://10.1.84.183/receiveNotify.jsp");// 如果需要设置服务端通知，可以设置订单的通知地址
		//payIntent.putExtra(SDKProtocolKeys.ATTACH_INFO, "ATTACHINFOtest"); //订单附加信息

		/* 如果不支持运营商短代支付，无需设置paycode参数
		 * 如果支持运营商sdk短代支付，那么需要先判断用户手机号，然后传入相对应运营商平台申请的paycode,
		 * 本demo对每种运营商短代支付各取一个测试计费点，游戏接入请根据实际情况设置
		 */
		//		payIntent.putExtra(SDKProtocolKeys.PAY_CODE, getPaycode(getApplicationContext())); 

		try {
			SDKCore.pay(mactivity, payIntent, new SDKCallbackListener() {

				@Override
				public void onSuccessful(int status, Response response) {
					/**
					 *  当为支付回调时，必须响应消息设置setMessage
					 *  必须及时响应，不要进行耗时操作，否则会导致线程阻塞
					 *  相关异步操作可以在handler中进行，另起工作者线程
					 *  设置为
					 *      Response.OPERATE_SUCCESS_MSG
					 *              代表CP成功进行相关动作时，响应SDK
					 *      Response.OPERATE_FAIL_MSG
					 *              代表CP进行相关动作失败时，响应SDK
					 */
					response.setMessage(Response.OPERATE_SUCCESS_MSG); // 重要 确认收到

					try {
						String dataStr = response.getData();
						//一定要进行判空，有可能手机空间满，找不到订单信息
						if (dataStr != null) {
							JSONObject data = new JSONObject(response.getData());
							String orderId = data.getString(PayResponse.CP_ORDER_ID);   // CP 订单号
							String tradeId = data.getString(PayResponse.TRADE_ID);      // 交易号
							String payMoney = data.getString(PayResponse.PAY_MONEY);        // 支付金额
							String payType = data.getString(PayResponse.PAY_TYPE);          // 支付类型
							String orderStatus = data.getString(PayResponse.ORDER_STATUS); // 订单状态 00 成功 01 失败 99 退款
							String orderFinishTime = data.getString(PayResponse.ORDER_FINISH_TIME); // 订单完成时间
							String productName = data.getString(PayResponse.PRO_NAME);// 道具名称
							String extendInfo = data.optString(PayResponse.EXT_INFO); //商品扩展信息
							String attachInfo = data.optString(PayResponse.ATTACH_INFO); // 附加透传信息
						}
						// 支付成功,该回调是在子线程中调用，UI操作需转到UI线程执行
						new Handler().post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(mActivity, "支付成功啦!", Toast.LENGTH_LONG).show();
								Log.i("tag","支付成功1");
								paySuce();
								Log.i("tag","支付成功2");
							}
						});
					}
					catch(JSONException ex)
					{
						ex.printStackTrace();
					}



				}

				@Override
				public void onErrorResponse(SDKError error) {
					// TODO Auto-generated method stub
					 // 支付失败，该回调是在子线程中调用，UI操作需转到UI线程执行
                    final String msg = error.getMessage();
					 new Handler().post(new Runnable() {
	                        @Override
	                        public void run() {
	                            Toast.makeText(mActivity, "opps! 支付失败了!" + msg, Toast.LENGTH_LONG).show();
	                            Log.i("tag","支付失败1");
	                            payFail();
	                            Log.i("tag","支付失败2");
	                        }
	                    });

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		UCGameSdk.defaultSdk().exit(paramActivity, new UCCallbackListener<String>() {
			@Override
			public void callback(int statuscode, String data) {
				if (UCGameSDKStatusCode.SDK_EXIT == statuscode) {
					Log.i("tag", "退出接口调用");
					callback.onExit();
				} else {
					//取消回到游戏
				}
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
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub

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


	


}
