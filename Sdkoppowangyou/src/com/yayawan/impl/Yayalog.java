package com.yayawan.impl;

import android.util.Log;

public class Yayalog {

	private static boolean canlog = true;

	public static void loger(String msg) {
		if (canlog) {
			Log.e("Yayalog", msg);
		}

	}

	public void setCanlog(boolean msg) {
		canlog = msg;
	}
}
