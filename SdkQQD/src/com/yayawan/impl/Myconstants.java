package com.yayawan.impl;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;


public class Myconstants {

	public static boolean ISFASTLOGIN=true;
	private static ProgressDialog mAutoLoginWaitingDlg;
	
	//是否第一次支付
	public static boolean ISFIRSTPAY=true;
	
	public static LoginImpl mloImpl=null;
	
	public static String platform=null;
	
	public static Activity mainactivity;
	
	public static Payinfo mpayinfo=null;
	public static Dialog dialog;
	public static String openId;
	public static String nickname;
	public static String accessToken;
	
	public static void startWaiting(Activity mActivity) {
	       // Logger.d("startWaiting");
	        mAutoLoginWaitingDlg = new ProgressDialog(mActivity);
	        stopWaiting();
	        mAutoLoginWaitingDlg.setTitle("自动登录中...");
	        mAutoLoginWaitingDlg.show();
	    }
	 
	 public static void stopWaiting() {
	       // Logger.d("stopWaiting");
	        if (mAutoLoginWaitingDlg != null && mAutoLoginWaitingDlg.isShowing()) {
	            mAutoLoginWaitingDlg.dismiss();
	        }
	    }
}
