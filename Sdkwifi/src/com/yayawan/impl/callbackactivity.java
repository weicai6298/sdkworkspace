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

import com.kkgame.utils.DeviceUtil;
import com.lantern.auth.stub.WkSDKFeature;
import com.lantern.auth.stub.WkSDKResp;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class callbackactivity extends Activity{
	
	private static Activity mActivity;
	
	public static String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity =this;
		WkSDKResp resp = WkSDKResp.decode(getIntent());
		Log.i("tag","resp = " +resp);
		if(resp != null){
			if (WkSDKFeature.WHAT_LOGIN.equals(resp.mWhat)) {

				// 登录返回信息，个业务方后续自行处理自身逻辑

				if (resp.mData != null && resp.mData.length() > 10) {

					String mAuthcode = resp.mData;//即为授权码
					Log.i("tag","mAuthcode = " +mAuthcode);
					HttpPost(mAuthcode);
				}
			}else if (WkSDKFeature.WHAT_PAY.equals(resp.mWhat)) {
				int resultCode = resp.mRetCode;//支付结果返回码，具体参见码表
				Log.i("tag","resultCode = " +resultCode);
				if(resultCode == 0){
					YaYawanconstants.paySuce();
					Toast("支付成功");
					mActivity.finish();
				}else{
					YaYawanconstants.payFail();
					Toast("支付失败");
					mActivity.finish();
				}
			}
		}
	}
	
	/**
	 * 
	 * 请求获取用户uid
	 * 
	 * @param sid
	 *            为登录返回的token
	 */
	public static void HttpPost(final String sid) {
		
		HttpUtils httpUtil = new HttpUtils();
		String url = "https://api.sdk.75757.com/data/get_uid/";
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id",DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("code", sid);
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
							uid = obj.getString("uid");
							Yayalog.loger("uid ="+uid);
							YaYawanconstants.loginSuce(mActivity, uid, uid, sid);
							Toast("登录成功");
						} catch (JSONException e) {
							e.printStackTrace();
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
}
