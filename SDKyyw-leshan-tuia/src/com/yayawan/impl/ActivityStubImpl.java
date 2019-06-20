package com.yayawan.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWActivityStub;


public class ActivityStubImpl implements YYWActivityStub {

	
	public static Activity mactivity;
	
	public static String url = "https://activity.tuia.cn/log/effect/v1";

    public void applicationInit(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

	public void onCreate(Activity paramActivity) {
        // TODO Auto-generated method stub
    	GuangdiantongUtils.guangDiantongInit(paramActivity.getApplicationContext());
    	//广点通激活
		//GuangdiantongUtils.guangDiantongActi(paramActivity);
    	mactivity = paramActivity;
    	KgameSdk.initSdk(paramActivity);
    	Handle.active_handler(paramActivity);
    	
    	//获取剪切板文字
    			Log.i("tag","启动激活-请求");
    	Intent mIntent = new Intent();
    	mIntent.setClass(paramActivity, ClipBoardService.class);
		paramActivity.startService(mIntent);
		
    }

    public void onStop(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onResume(Activity paramActivity) {
    	
    
    	
        KgameSdk.init(paramActivity);
    }

    public void onPause(Activity paramActivity) {
        KgameSdk.stop(paramActivity);
    }

    public void onRestart(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void applicationDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onActivityResult(Activity paramActivity, int paramInt1,
            int paramInt2, Intent paramIntent) {
        // TODO Auto-generated method stub

    }

    public void onNewIntent(Intent paramIntent) {
        // TODO Auto-generated method stub

    }

	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(Activity arg0) {
		// TODO Auto-generated method stub
		
	}
	
	 public static void HttpPost(final String time,final String copyValue){
			HttpUtils httpUtil = new HttpUtils();
			RequestParams requestParams = new RequestParams();
			requestParams.addBodyParameter("a_oId",copyValue);
			requestParams.addBodyParameter("a_timeStamp", time);
			requestParams.addBodyParameter("subType", "2");
			Log.i("tag","requestParams="+requestParams);
			httpUtil.send(HttpMethod.POST, url,requestParams,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							Log.i("tag","请求失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							try {
								JSONObject obj = new JSONObject(arg0.result);
								int record = obj.optInt("record");
								Log.i("tag","record = "+record);
								if(record == 0){
									Log.i("tag","请求成功");
								}else {
									Log.i("tag","请求失败record ="+record);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});

	 }

	 
}
