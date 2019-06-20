package com.yayawan.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

import com.kkgame.utils.Sputils;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author Administrator
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class payClipBoardService extends Service{

	private MyBinder binder = new MyBinder();
	public static String copyValue;

//	public static String url = "http://ishuiyun.com:8080/sy-service/ad-effect/v1";
	public static String url = "https://activity.tuia.cn/log/effect/v1";

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@SuppressWarnings("unused")
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate() {
		super.onCreate();
		//距离1970年的时间（单位：秒）
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
		String strDate = sdf.format(curDate);  
		java.util.Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}   
		String time = date.getTime()/1000L+"";
		Log.i("tag","time = " +time);
		final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		Log.i("tag","111");
		ClipData data = cm.getPrimaryClip();
		Log.i("tag","data = "+data);
		String tempuid = Sputils.getSPstring("a_old", "a_old", this);
		Log.i("tag", "tempuid=" + tempuid);
		if(data == null && tempuid == null){
			return;
		}else if(data == null && !(tempuid.equals("a_old"))){
			Log.i("tag", "tempuid请求");
			HttpPost(time,tempuid,"6");
			return;
		}else if(data != null){
			ClipData.Item item = data.getItemAt(0);
			copyValue = item.getText()+"";
			Log.i("tag", "========复制文字:"+copyValue);

			String copyString= copyValue.substring(0, 6);
			Log.i("tag", "copyString = "+copyString);

//			if(copyString.equals("a_oId=")){
				Log.i("tag","开始截取");
//				copyValue = copyValue.substring(6, copyValue.length());
				Log.i("tag","copyValue = " +copyValue);
				HttpPost(time,copyValue,"6");
//			}
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
	}

	@Override
	public void onDestroy() {
	}


	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder{
		payClipBoardService getService(){
			return payClipBoardService.this;
		}
	}

	//	public String getcontent(){
	//		String vlaueString = getstring();
	//		return vlaueString;
	//	}
	//	
	//	public String getstring(){
	//		ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
	//		ClipData data = cm.getPrimaryClip();
	//		ClipData.Item item = data.getItemAt(0);
	//		String content = item.getText().toString();
	//		return content;
	//	}


	public static void HttpPost(final String time,String copyValue,final String subType){
		HttpUtils httpUtil = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("a_oId",copyValue);
		requestParams.addBodyParameter("a_timeStamp", time);
		requestParams.addBodyParameter("type", "8");
		requestParams.addBodyParameter("subType", subType);
		requestParams.addBodyParameter("appId", "cqll");
		Log.i("tag","a_oId="+copyValue);
		Log.i("tag","a_timeStamp="+time);
		Log.i("tag","subType="+subType);
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
					Log.i("tag","obj = "+obj);
					String code = obj.getString("code");
					Log.i("tag","code = "+code);
					if(code.equals("0000000")){
						Log.i("tag","请求成功");
					}else if(code.equals("0000001")){
						Log.i("tag","请求失败-系统错误");
					}else if(code.equals("0110002")){
						Log.i("tag","请求失败-a_oId 缺失");
					}else if(code.equals("0110004")){
						Log.i("tag","请求失败-a_timeStamp 缺失");
					}else if(code.equals("0110007")){
						Log.i("tag","请求失败-类型缺失");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}


}
