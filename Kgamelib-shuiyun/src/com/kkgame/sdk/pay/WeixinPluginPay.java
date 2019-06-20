package com.kkgame.sdk.pay;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

public class WeixinPluginPay {
	public static final String BROADCAST_ACTION = "com.yyw.weixinpay";
	private static String WEIXINPAYRESULT = "WEIXINPAYRESULT";
	/*	public static weiXinpayCallBack mYayaWanPaymentCallback;
	private static MyBroadcastReceiver mBroadcastReceiver;

	public static void startPay(Activity mActivity, PayReq req,
			weiXinpayCallBack yayaWanPaymentCallback) {
		mYayaWanPaymentCallback = yayaWanPaymentCallback;
		mBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_ACTION);
		mActivity.registerReceiver(mBroadcastReceiver, intentFilter);
		Intent localIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("sign", req.sign);
		bundle.putString("timestamp", req.timeStamp);
		bundle.putString("package", req.packageValue);
		bundle.putString("noncestr", req.nonceStr);
		bundle.putString("partnerid", req.partnerId);
		bundle.putString("appid", req.appId);
		bundle.putString("prepayid", req.prepayId);
		// bundle.putBoolean("PayReq", req);
		// localIntent.putExtra
		localIntent.putExtra("bundle", bundle);
		localIntent.setClassName("com.yyw.weixinpay",
				"com.yyw.yayaplugin.MyActivity");
		mActivity.startActivity(localIntent);

	}
	
	public static void finishPay(Activity mActivity){
		if (mBroadcastReceiver!=null) {
			mActivity.unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver=null;
		}
	
	}

	public static class MyBroadcastReceiver extends BroadcastReceiver {

		// public static int m = 1;
		private static String BROADCAST_ACTION = "com.yyw.weixinpay";

		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.w(TAG, "intent:" + intent);
			int payresultcode = intent.getIntExtra(WEIXINPAYRESULT, 1);
			// Log.w(TAG, "name:" + name + " m=" + m);
			// m++;
			switch (payresultcode) {
			case 0:
				//Toast.makeText(context, "支付成功", Toast.LENGTH_LONG).show();
				mYayaWanPaymentCallback.onsuce();
				break;

			case -1:
				Toast.makeText(context, "支付失败", Toast.LENGTH_LONG).show();
				mYayaWanPaymentCallback.onfail();
				break;
			case -2:
				Toast.makeText(context, "支付取消", Toast.LENGTH_LONG).show();
				mYayaWanPaymentCallback.oncancel();
				break;

			default:
				Toast.makeText(context, "支付失败", Toast.LENGTH_LONG).show();
				mYayaWanPaymentCallback.onfail();
				break;
			}

		}
	}
	
	public interface weiXinpayCallBack{
		public void onsuce();
		public void onfail();
		public void oncancel();
		
	}*/
	
}
