package com.kkgame.utils;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.CryptoUtil;
import com.kkgame.sdk.utils.RSACoder;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.callback.YYWLoginHandleCallback;

public class Handle {

	/**
	 * 注册回调
	 */
	public static void register_handler(final Context context,
			final String uid, final String userName) {
		// 访问网络,在线程中执行
		HttpUtils mhttp = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(context));
		requestParams.addBodyParameter("imei", DeviceUtil.getIMEI(context));

		requestParams.addBodyParameter("cur_ver", "1234");
		mhttp.send(HttpMethod.POST, UrlConstants.active,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 登录回调,直接return已经废弃
	 */
	public static void login_handler(final Context context, final String uid,
			final String userName) {

		
		
		
		return;
		
	}

	
	public static void login_handler(final Context context, final String uid,
			final String userName, final YYWLoginHandleCallback callback) {
		// 访问网络,在线程中执行
		HttpUtils mhttp = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(context));
		requestParams.addBodyParameter("imei", DeviceUtil.getIMEI(context));
		requestParams.addBodyParameter("uid", uid);
		Yayalog.loger("联合渠道丫丫玩登陆"+"app_id="+DeviceUtil.getAppid(context)+"imei="+DeviceUtil.getIMEI(context)+"uid:"+uid);
		mhttp.send(HttpMethod.POST, ViewConstants.unionloginurl,requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						callback.onFail(arg1, "");
					}

					@Override
					public void onSuccess(ResponseInfo<String> resz) {
						// TODO Auto-generated method stub

						callback.onSuccess(resz.result, "");

					}
				});

	}

	/**
	 * 激活回调
	 */
	public static void active_handler(final Context context) {
		// 访问网络,在线程中执行

		HttpUtils mhttp = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(context));
		requestParams.addBodyParameter("imei", DeviceUtil.getIMEI(context));
		requestParams.addBodyParameter("cur_ver",
				DeviceUtil.getVersionCode(context));
		Yayalog.loger(DeviceUtil.getAppid(context)+","+DeviceUtil.getIMEI(context)+","+DeviceUtil.getVersionCode(context));
		mhttp.send(HttpMethod.POST, ViewConstants.activeurl,requestParams,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> resz) {
						// TODO Auto-generated method stub

						try {
							JSONObject object = new JSONObject(resz.result);
							Yayalog.loger("kgame激活信息:" + resz.result);
							int recode;

							recode = object.getInt("err_code");

							if (recode == 0) {
								JSONObject data = object.getJSONObject("data");
								int webpay = data.optInt("toggle");// 服务器返回支付方式
								// String login_pay_level =
								// object.optString("login_pay_level", "");
								int level = data.optInt("toggle_level",0);
								Sputils.putSPint("login_type", webpay, context);//
								Sputils.putSPint("login_pay_level", level,
										context);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	/**
	 * 加密注册回调信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptRegisterData(Context context, String uid,
			String userName) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("game_id=")
				.append(DeviceUtil.getGameId(context)).append("&uid=")
				.append(uid).append("&union_id=")
				.append(DeviceUtil.getUnionid(context)).append("&brand=")
				.append(DeviceUtil.getBrand()).append("&device=")
				.append(DeviceUtil.getIMEI(context)).append("&mac=")
				.append(DeviceUtil.getMAC(context)).append("&model=")
				.append(DeviceUtil.getModel()).append("&username=")
				.append(userName).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return URLEncoder.encode(hexString, "UTF-8");

	}

}
