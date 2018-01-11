package com.kkgame.utils;

import com.yayawan.impl.YYApplication;
import com.yayawan.proxy.GameApitest;
import com.yayawan.proxy.YYcontants;

import android.util.Log;

public class Yayalog {

	
	public static boolean canlog=false;
	
	public static void  loger(String msg){
		
		//DeviceUtil.isDebug(paramContext)
		if (canlog) {
			Log.e("Yayalog", msg);
		}
			
		
		
	}
	public static void  loger(String msg,String value){
		
		//DeviceUtil.isDebug(paramContext)
		if (canlog) {
			Log.d("Yayalog", msg+":"+value);
		}
			
		
		
	}
	
	public static void  logerlife(String msg){
		
		//DeviceUtil.isDebug(paramContext)
		if (YYcontants.ISDEBUG) {
			Log.d("Yayaloglife", msg);
			GameApitest.getGameApitestInstants().sendTest(msg);
		}
			
		
		
	}
	
	public static void setCanlog(boolean msg){
		canlog=msg;
	}
}
