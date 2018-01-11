package com.yayawan.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

public class Toastutils_jf {

	private static Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 110:
				
				break;

			default:
				break;
			}
		}
	};
	public static void  toastString(Activity mActivity,String msg){
		Message msg1 = new Message();
		msg1.what=110;
		msg1.obj=msg;
		mHandler.sendMessage(msg1);
	}
}
