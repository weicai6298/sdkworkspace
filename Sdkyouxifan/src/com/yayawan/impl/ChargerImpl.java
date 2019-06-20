package com.yayawan.impl;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


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
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.main.YaYaWan;
import com.yayawan.proxy.YYWCharger;
import com.yayawan.sdktemplate.MainActivity;


public class ChargerImpl implements YYWCharger {

	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {

	}

	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			public void run() {
					
				createOrder(paramActivity);
				// System.err.println("pay start");

			}
		});

	}

	String orderId = null;
	String sign = null;

	public void createOrder(final Activity paramActivity) {
		progress(paramActivity);
		HttpUtils httpUtil = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",
				DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("uid", "" + YYWMain.mUser.yywuid);
		requestParams.addBodyParameter("amount", "" + YYWMain.mOrder.money);
		requestParams.addBodyParameter("remark", YYWMain.mOrder.ext);
		requestParams.addBodyParameter("transid", YYWMain.mOrder.orderId);
		requestParams.addBodyParameter("username", YYWMain.mUser.userName);
		
		requestParams.addBodyParameter("yxf_uid", YaYawanconstants.yxf_uid);
		requestParams.addBodyParameter("roleid", YaYawanconstants.role_Id);
		requestParams.addBodyParameter("zoneid", YaYawanconstants.zone_Id);
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
						// TODO Auto-generated method stub
						Yayalog.loger("下单失败"+arg1.toString());
						disprogress();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						disprogress();
						try {
							Yayalog.loger("下单成功"+arg0.result);
							JSONObject obj = new JSONObject(arg0.result);
							int err_code = obj.optInt("err_code");
							if (err_code == 0) {
								JSONObject data = obj.getJSONObject("data");
								Log.i("tag", "data = " +data);
								orderId = data.optString("id");
								JSONObject json_sign = data.getJSONObject("sign");
								sign = json_sign.optString("sign");
								Log.i("tag", "sign = " +sign);

								new Handler(Looper.getMainLooper())
										.post(new Runnable() {

											public void run() {
												pay_run(paramActivity);

											}
										});
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	private void pay_run(final Activity paramActivity) {

		YaYawanconstants.pay(paramActivity, orderId, sign);

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
		// progressDialog.setButton("确定", new SureButtonListener());
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
}
