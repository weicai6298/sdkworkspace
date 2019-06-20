package com.yayawan.impl;

import org.json.JSONException;
import org.json.JSONObject;

import pay.winner.cn.paylibrary.utils.PaySDK;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.utils.DeviceUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;

public class ChargerImpl implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {

	}

	@Override
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
					
				createOrder(paramActivity);
				// System.err.println("pay start");

			}
		});

	}

	String orderId = null;

	public void createOrder(final Activity paramActivity) {
		progress(paramActivity);
		HttpUtils httpUtil = new HttpUtils();
		String goods = YYWMain.mOrder.goods;
		int paycode = getPaycode(goods);
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",
				DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("uid", "" + YYWMain.mUser.yywuid);
		requestParams.addBodyParameter("amount", "" + YYWMain.mOrder.money);
		requestParams.addBodyParameter("remark", YYWMain.mOrder.ext);
		requestParams.addBodyParameter("transid", YYWMain.mOrder.orderId);
		requestParams.addBodyParameter("username", YYWMain.mUser.userName);
		
		requestParams.addBodyParameter("propid", paycode+"");
		
		Log.i("tag","propid = " +paycode);
		requestParams.addBodyParameter("txnitem", "1");
		requestParams.addBodyParameter("channelid", DeviceUtil.getGameInfo(paramActivity, "yinlian_channelkey"));
		requestParams.addBodyParameter("secretkey", PaySDK.getParam());
		requestParams.addBodyParameter("orderdesc", "购买"+ YYWMain.mOrder.goods);
		
		Yayalog.loger("uid:"+ YYWMain.mUser.yywuid);
		Yayalog.loger("username:"+YYWMain.mUser.userName);
		Yayalog.loger("app_id:"+DeviceUtil.getAppid(paramActivity));
		Yayalog.loger("amount:"+YYWMain.mOrder.money);
		Yayalog.loger("remark:"+YYWMain.mOrder.ext);
		Yayalog.loger("transid:"+YYWMain.mOrder.orderId);
		Yayalog.loger("url:"+ ViewConstants.unionmakeorder);
		httpUtil.send(HttpMethod.POST, ViewConstants.unionmakeorder,requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Yayalog.loger("下单失败"+arg1.toString());
						disprogress();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						disprogress();
						try {
							Yayalog.loger("下单成功"+arg0.result);
							JSONObject obj = new JSONObject(arg0.result);
							int err_code = obj.optInt("err_code");
							if (err_code == 0) {
								JSONObject data = obj.getJSONObject("data");
								orderId = data.optString("tn");

								new Handler(Looper.getMainLooper())
										.post(new Runnable() {

											@Override
											public void run() {
												pay_run(paramActivity);

											}
										});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	private void pay_run(final Activity paramActivity) {

		YaYawanconstants.pay(paramActivity, orderId);

	}

	ProgressDialog progressDialog = null;

	private void progress(Activity paramActivity) {
		progressDialog = new ProgressDialog(paramActivity);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		// progressDialog.setTitle("提示");
		// 设置ProgressDialog 提示信息
		progressDialog.setMessage("订单处理中");
		// 设置ProgressDialog 标题图标
		// progressDialog.setIcon(R.drawable.a);
		// 设置ProgressDialog 的进度条是否不明确
		progressDialog.setIndeterminate(true);
		// 设置ProgressDialog 是否可以按退回按键取消
		progressDialog.setCancelable(false);
		// 设置ProgressDialog 的一个Button
//		 progressDialog.setButton("确定", new SureButtonListener());
		// 让ProgressDialog显示
		try {
			progressDialog.show();
		} catch (Exception e) {

		}
	}

	private void disprogress() {
		if (progressDialog != null) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}
	}
	
	private static int getPaycode(String goods) {
		int paycode = 0 ;
		//我的便利店
		if(goods.equals("10钻石")){
			paycode = 11516;
		}else if(goods.equals("32钻石")){
			paycode = 11517;
		}else if(goods.equals("57钻石")){
			paycode = 11518;
		}else if(goods.equals("120钻石")){
			paycode = 11519;
		}else if(goods.equals("390钻石")){
			paycode = 11520;
		}else if(goods.equals("680钻石")){
			paycode = 11521;
		}else if(goods.equals("1450钻石")){
			paycode = 11522;
		}
		else if(goods.equals("首冲390钻石")){
			paycode = 11523;
		}
		else if(goods.equals("每日钻石套餐")){
			paycode = 11524;
		}
		else if(goods.equals("新手套餐")){
			paycode = 11525;
		}
		else if(goods.equals("实惠套餐")){
			paycode = 11526;
		}
		else if(goods.equals("高级套餐")){
			paycode = 11527;
		}
		else if(goods.equals("每日特惠周一")){
			paycode = 11528;
		}
		else if(goods.equals("每日特惠周二")){
			paycode = 11529;
		}
		else if(goods.equals("每日特惠周三")){
			paycode = 11530;
		}
		else if(goods.equals("每日特惠周四")){
			paycode = 11531;
		}
		else if(goods.equals("每日特惠周五")){
			paycode = 11532;
		}
		else if(goods.equals("每日特惠周六")){
			paycode = 11533;
		}
		else if(goods.equals("每日特惠周日")){
			paycode = 11534;
		}
		else if(goods.equals("首充礼包")){
			paycode = 11535;
		}
		else if(goods.equals("新手礼包")){
			paycode = 11549;
		}
		else if(goods.equals("加油礼包")){
			paycode = 11550;
		}
		else if(goods.equals("进取礼包")){
			paycode = 11551;
		}
		else if(goods.equals("超值礼包")){
			paycode = 11552;
		}
		return paycode;
	}
}
