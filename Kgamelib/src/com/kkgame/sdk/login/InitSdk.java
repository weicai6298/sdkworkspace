package com.kkgame.sdk.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;


import com.kkgame.sdk.db.UserDao;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;


public class InitSdk {

	
	public static String getSystemProperty(String propName) {
		String line;
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec("getprop " + propName);
			input = new BufferedReader(
					new InputStreamReader(p.getInputStream()), 1024);
			line = input.readLine();
			input.close();
		} catch (IOException ex) {
			Log.e("jf", "Unable to read sysprop " + propName, ex);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Log.e("jf", "Exception while closing InputStream", e);
				}
			}
		}
		return line;
	}

	private static SharedPreferences mSp;

	private static final String ACTIVE = "active";
	public  static  void SendinitData(final Context mContext){
		/**
		 * 创建线程,给服务器发送数据激活,并延迟3秒,让动画播放完成
		 */
	/*	new Thread() {
			@Override
			public void run() {
				try {
					mSp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
					boolean flag = mSp.getBoolean(ACTIVE, false);
					if (!flag) {
						ObtainData.active(mContext);
						initData(mContext);
					}
					

					//mHandler.sendEmptyMessage(ANIMSTOP);

				} catch (Exception e) {
					e.printStackTrace();
					//onError();
				}
			}
		}.start();*/
	}
	
	

	public static void getAnnouncement(final Activity mActivity) {
		// TODO Auto-generated method stub
		//final String url="http://www.yayawan.com/output/notice/"+"1186266787";
		//Yayalog.loger(url);
		HttpUtils httpUtils = new HttpUtils();
		final String url="http://www.yayawan.com/output/notice/"+DeviceUtil.getGameId(mActivity);
		
		//final String url="http://www.yayawan.com/output/notice/"+"1186266787";
		Yayalog.loger(url);
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result=responseInfo.result;
				Yayalog.loger(responseInfo.result);
				if (result.contains("success")&&result.contains("error_code")) {
					
				}else {
					if (result.contains("!DOCTYPE HTML")) {
						new Announcement_dialog(mActivity, url).dialogShow();
					}
					
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
