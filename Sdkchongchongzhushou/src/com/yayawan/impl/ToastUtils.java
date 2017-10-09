package com.yayawan.impl;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

	private static Toast textToast;
	private static String toastText;
	private static long textShowTime;

	public static synchronized void showShortToast(Context context, int resId) {
		showToast(context, context.getString(resId), Toast.LENGTH_SHORT);
	}

	public static synchronized void showShortToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_SHORT);
	}
	
	public static synchronized void showLongToast(Context context, int resId) {
		showToast(context, context.getString(resId), Toast.LENGTH_LONG);
	}
	
	
	public static synchronized void showLongToast(Context context, String text) {
		showToast(context, text, Toast.LENGTH_LONG);
	}

	public synchronized static void showToast(Context context, String text, int duration) {
		if(TextUtils.isEmpty(text)) return;
		if (textToast != null) {
			if (toastText.equals(text)) {
				long interval = System.currentTimeMillis() - textShowTime;
				if (interval < 1000 * 2) {
					return;
				}
			}
			textToast.setText(text);
		} else {
			textToast = Toast.makeText(context.getApplicationContext(), text, duration);
		}
		toastText = text;
		textToast.show();
		textShowTime = System.currentTimeMillis();
	}

}
