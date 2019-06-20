package com.yayawan.implyy;

import java.lang.reflect.Method;
import java.math.BigInteger;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;

import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;

import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;

public class ChargerImplyylianhe implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {
	}

	private YYWOrder morder;

	// 渠道登陆，联合id idtoken支付
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {

				morder = order;
				pay_run(paramActivity,order);
				Yayalog.loger("lianhe");

			}

		});

	}


	

	private void pay_run(final Activity paramActivity,YYWOrder morder) {

		Order order2 = new Order();

		order2.orderId = morder.orderId;
		order2.goods = morder.goods;
		order2.money = morder.money;
		order2.ext =  morder.ext;
		AgentApp.mUser = new User();
		AgentApp.mUser.setUid(new BigInteger(YYWMain.mUser.yywuid));
		AgentApp.mUser.setUser_uid((YYWMain.mUser.yywuid));
		AgentApp.mUser.setUserName(YYWMain.mUser.yywusername);
		AgentApp.mUser.setToken(YYWMain.mUser.yywtoken);

		Yayalog.loger("yylianhe支付传入的uid：" + AgentApp.mUser.toString());
		
		if (DeviceUtil.isXiaomi(paramActivity)) {
			KgameSdk.GreenblueP(paramActivity, order2, 1,new KgameSdkPaymentCallback() {

	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
	            public void onCancel() {
	                if (YYWMain.mPayCallBack != null) {
	                    YYWMain.mPayCallBack.onPayCancel("cancel", "");
	                }
	            }

	            @Override
	            public void onError(int arg0) {
	                if (YYWMain.mPayCallBack != null) {
	                    YYWMain.mPayCallBack.onPayFailed("failed", "");
	                }
	            }

	            @Override
	            public void onSuccess(User user, Order order, int arg2) {
	                if (YYWMain.mPayCallBack != null) {
	                    YYWUser yywUser = new YYWUser();

	                    yywUser.uid = user.uid + "";
	                    yywUser.icon = user.icon;
	                    yywUser.body = user.body;
	                    yywUser.lasttime = user.lasttime;
	                    yywUser.money = user.money;
	                    yywUser.nick = user.nick;
	                    yywUser.password = user.password;
	                    yywUser.phoneActive = user.phoneActive;
	                    yywUser.success = user.success;
	                    yywUser.token = user.token;
	                    yywUser.userName = user.userName;

	                    YYWOrder yywOrder = new YYWOrder();

	                    yywOrder.orderId = order.orderId;
	                    yywOrder.ext = order.ext;
	                    yywOrder.gameId = order.gameId;
	                    yywOrder.goods = order.goods;
	                    yywOrder.id = order.id;
	                    yywOrder.mentId = order.mentId;
	                    yywOrder.money = order.money;
	                    yywOrder.paytype = order.paytype;
	                    yywOrder.serverId = order.serverId;
	                    yywOrder.status = order.status;
	                    yywOrder.time = order.time;
	                    yywOrder.transNum = order.transNum;
	                    
	                  
	                    pushUmengdata(order.money/100+"");
	                    
	                    YYWMain.mPayCallBack.onPaySuccess(yywUser, yywOrder, "success");
	                }
	            }

	        });
		}else {
		KgameSdk.payment(paramActivity, order2, false,
				new KgameSdkPaymentCallback() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onCancel() {
						if (YYWMain.mPayCallBack != null) {
							YYWMain.mPayCallBack.onPayCancel("cancel", "");
						}
					}

					@Override
					public void onError(int arg0) {
						if (YYWMain.mPayCallBack != null) {
							YYWMain.mPayCallBack.onPayFailed("failed", "");
						}
					}

					@Override
					public void onSuccess(User user, Order order, int arg2) {
						if (YYWMain.mPayCallBack != null) {
							YYWUser yywUser = new YYWUser();

							yywUser.uid = user.uid + "";
							yywUser.icon = user.icon;
							yywUser.body = user.body;
							yywUser.lasttime = user.lasttime;
							yywUser.money = user.money;
							yywUser.nick = user.nick;
							yywUser.password = user.password;
							yywUser.phoneActive = user.phoneActive;
							yywUser.success = user.success;
							yywUser.token = user.token;
							yywUser.userName = user.userName;

							YYWOrder yywOrder = new YYWOrder();

							yywOrder.orderId = order.orderId;
							yywOrder.ext = order.ext;
							yywOrder.gameId = order.gameId;
							yywOrder.goods = order.goods;
							yywOrder.id = order.id;
							yywOrder.mentId = order.mentId;
							yywOrder.money = order.money;
							yywOrder.paytype = order.paytype;
							yywOrder.serverId = order.serverId;
							yywOrder.status = order.status;
							yywOrder.time = order.time;
							yywOrder.transNum = order.transNum;

							pushUmengdata(order.money / 100 + "");

							YYWMain.mPayCallBack.onPaySuccess(yywUser,
									yywOrder, "success");
						}
					}

				});

		}
		
		
	}

	// 支付成功调用友盟
	public void pushUmengdata(String money) {
		try {
			Class<?> subclass = Class
					.forName("com.yayawan.impl.ActivityStubImpl");
			Method[] methods = subclass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].equals("payumenSucceed")) {
					new com.yayawan.impl.ActivityStubImpl()
							.payumenSucceed(money);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Yayalog.loger("未找到ActivityStubImpl");
		}
	}

	ProgressDialog progressDialog = null;


}
