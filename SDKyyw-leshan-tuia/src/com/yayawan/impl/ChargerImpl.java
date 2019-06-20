package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;


public class ChargerImpl implements YYWCharger {

    public void charge(Activity paramActivity, YYWOrder order,
            YYWPayCallBack callback) {
    }

    public void pay(final Activity paramActivity, final YYWOrder order, YYWPayCallBack callback) {


    	new Handler(Looper.getMainLooper()).post(new Runnable() {

            public void run() {

				    	Order order2 = new Order();

				        order2.orderId = order.orderId;
				        order2.goods = order.goods;
				        order2.money = order.money;
				        order2.ext = order.ext;

				        KgameSdk.payment(paramActivity, order2,false, new KgameSdkPaymentCallback() {

				            public void onCancel() {
				                if (YYWMain.mPayCallBack != null) {
				                    YYWMain.mPayCallBack.onPayCancel("cancel", "");
				                }
				            }

				            public void onError(int arg0) {
				                if (YYWMain.mPayCallBack != null) {
				                    YYWMain.mPayCallBack.onPayFailed("failed", "");
				                }
				            }

				            public void onSuccess(User user, Order order, int arg2) {
				                if (YYWMain.mPayCallBack != null) {
				                	
				                	GuangdiantongUtils.guangDiantongGiveMoney(paramActivity, order.money+"");
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
				                    YYWMain.mPayCallBack.onPaySuccess(yywUser, yywOrder, "success");
				                    
				                    //获取剪切板文字
				    				Log.i("tag","支付-请求");
				    		    	Intent mIntent = new Intent();
				    		    	mIntent.setClass(paramActivity, payClipBoardService.class);
				    				paramActivity.startService(mIntent);
				                }
				            }

				        });

            }

    	});

    }

}
