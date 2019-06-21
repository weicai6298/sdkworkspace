package com.lidroid.jxutils.http;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.support.v4.content.ContextCompat;
import android.util.Log;

public class Jxutilsinit {
	public static boolean isdebug=false;
	public static String imei="";
	public static String androidid="";
	public static String mac="";
	public static String deviceinfo="";
	public static void init(Context mcontext){
		
		try {
			
			
			if (checkPermission(mcontext, Manifest.permission.READ_PHONE_STATE)) {
				if (isdebug) {
					
				}else {
//					androidid=DeviceTool.instance().getUDID(mcontext);
//					imei=DeviceTool.instance().getIMEI(mcontext);
//					mac=DeviceTool.instance().getMac(mcontext);
//					deviceinfo=DeviceTool.instance().getPhoneStatus(mcontext);
				}
				
			}
			
		} catch (Exception e) {
			Log.e("游戏", "获取设备信息失败");
		}
		
	}
	  /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    public static boolean checkPermission(Context context, String permission) {
    	
    	if ((context.getApplicationInfo().targetSdkVersion)>21) {
    		if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
                return true;
            else
                return false;
		}else {
			return true;
		}
    	
        
    }
}
