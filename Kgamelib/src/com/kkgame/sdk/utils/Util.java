package com.kkgame.sdk.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public final class Util {

	@SuppressWarnings("deprecation")
    public static Bundle decodeUrl(String paramString) {
		Bundle localBundle = new Bundle();
		String[] arrayOfString1 = null;
		int i = 0;
		if (paramString != null) {
			arrayOfString1 = paramString.split("&");
			i = arrayOfString1.length;
		}
		for (int j = 0;; j++) {
			if (j >= i)
				return localBundle;
			String[] arrayOfString2 = arrayOfString1[j].split("=");
			localBundle.putString(URLDecoder.decode(arrayOfString2[0]),
			        arrayOfString2.length > 1 ? URLDecoder.decode(arrayOfString2[1]) : "");
		}
	}

	public static Bundle parseUrl(String paramString) {
		// String str = paramString.replace("yayaconnect", "http");
		try {
			URL localURL = new URL(paramString);
			Bundle localBundle = decodeUrl(localURL.getQuery());
			localBundle.putAll(decodeUrl(localURL.getRef()));
			return localBundle;
		} catch (MalformedURLException localMalformedURLException) {
		}
		return new Bundle();
	}

	public static Bundle getMetaDataInfo(Context paramContext) {

		ApplicationInfo appInfo = null;
		try {
			appInfo = paramContext.getPackageManager()
					.getApplicationInfo(paramContext.getPackageName(),
							PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (appInfo.metaData == null)
			return null;
		return appInfo.metaData;
	}

	public static String getGameId(Context paramContext) {
		Bundle DataInfo;
		if (((DataInfo = getMetaDataInfo(paramContext)) == null)
				|| (DataInfo.get("yayawan_game_id") == null)) {
			throw new IllegalArgumentException("must set the yayawan_game_id");
		}
		return DataInfo.getString("yayawan_game_id").replace("yaya", "");
	}

	public static String getSourceId(Context paramContext) {
		Bundle DataInfo;
		if (((DataInfo = getMetaDataInfo(paramContext)) == null)
				|| (DataInfo.get("yayawan_source_id") == null)) {
			return null;
		}
		return DataInfo.get("yayawan_source_id").toString();
	}

	// =============================================================//shw2013-0725

	public static String getSourceId1(Context paramContext) {
		Bundle DataInfo;
		if (((DataInfo = getMetaDataInfo(paramContext)) == null)
				|| (DataInfo.get("yayawan_game_key") == null)) {
			return null;
		}
		return DataInfo.get("yayawan_game_key").toString();
	}

	public static String getIMEI(Context paramContext) {
		return ((TelephonyManager) paramContext.getSystemService("phone"))
				.getDeviceId();
		// return ((TelephonyManager)
		// getSystemService(paramContext.TELEPHONY_SERVICE)).getDeviceId();
		// return paramContext = (paramContext =
		// (TelephonyManager)paramContext.getSystemService("phone")).getDeviceId();
	}

	public static String getPackageName(Context paramContext) {
		PackageManager localPackageManager = paramContext.getPackageManager();
		try {
			return localPackageManager.getPackageInfo(
					paramContext.getPackageName(), 0).packageName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getSim(Context paramContext) {
		return ((TelephonyManager) paramContext.getSystemService("phone"))
				.getSimSerialNumber();
	}

	public static String getMAC(Context paramContext) {
		return (((WifiManager) paramContext.getSystemService("wifi"))
				.getConnectionInfo()).getMacAddress();
	}

	public static String getModel() {
		String model = Build.MODEL;
		return model;
	}

	public static String getBrand() {
		return Build.BRAND;
	}

}
