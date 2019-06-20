package com.kkgame.utils;

import java.util.ArrayList;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.kkgame.common.CommonData;
import com.kkgame.sdk.bean.PayMethod;

public class DeviceUtil {

	private static final String APP_ID = CommonData.sdkid;

	private static final String GAME_ID = "yayawan_game_id";

	private static final String UNION_GAME_ID = "union_game_id";

	private static final String UNION_ID = "union_id";

	private static final String SOURCE_ID = "yayawan_source_id";

	private static final String GAME_KEY = "yayawan_game_key";

	private static final String YAYAWAN_HELPER = "yayawan_helper";

	private static final String ISLANDSCAPE = "isLandscape";

	private static final String ISDEBUG = "isdebug";
	private static final String YAYAWAN_ORIENTATION = "yayawan_orientation";

	public static String gameid;
	public static String appid;

	public static final int BLUEPMSPCODE = 31;

	public static final int YAYABICODE = 4;

	public static final int YIDONGCODE = 11;

	public static final int DIANXINCODE = 15;

	public static final int LIANTONGCODE = 12;

	public static final int YIBAOCODE = 7;

	public static final int JUNWANGCODE = 16;

	public static final int SHENGDACODE = 13;

	public static final int QQCODE = 20;

	public static final int WXPAYCODE = 32;
	public static final int DAIJINJUANPAY = 39;
	public static final int YINLIAN = 21;
	public static final int YAYAWANWEIXINPLUIN = 10;

	/**
	 * 获取横竖屏信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getOrientation(Context paramContext) {
		
		
		if (DeviceUtil.isLandscape(paramContext)) {
			return "landscape";
		}else {
			return "portrait";
		}
		//return dataInfo.getString(YAYAWAN_ORIENTATION);
	}

	/**
	 * 获取支付方式 初始化链接网络时候获取了支付方式列表
	 * 
	 * @param paramContext
	 * @return
	 */
	public static ArrayList<PayMethod> getYayaWanMethod(Context paramContext) {
		Bundle dataInfo;
		ArrayList<PayMethod> paymethods = initPayMethod(paramContext);

		return paymethods;
	}

	/**
	 * 初始化支付方式列表
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<PayMethod> initPayMethod(Context context) {
		ArrayList<PayMethod> paymethods = new ArrayList<PayMethod>();
		// if (Utils.isExistMsp(context)) {
		paymethods.add(new PayMethod("yaya_bluepay", BLUEPMSPCODE));
		// } else {
		// paymethods.add(new PayMethod("yaya_bluepay", BLUEPCODE));
		// }
		// paymethods.add(new PayMethod("yaya_bluepay", ResourceUtil
		// .getDrawableId(context, "bluepay_icon"), BLUEPCODE));
		paymethods.add(new PayMethod("yaya_visa", YIBAOCODE));
		paymethods.add(new PayMethod("yaya_yayabi", YAYABICODE));
		paymethods.add(new PayMethod("yaya_cash", YIBAOCODE));
		paymethods.add(new PayMethod("yaya_yidong", YIDONGCODE));
		paymethods.add(new PayMethod("yaya_liantong", LIANTONGCODE));
		paymethods.add(new PayMethod("yaya_dianxin", DIANXINCODE));
		paymethods.add(new PayMethod("yaya_shengda", SHENGDACODE));
		paymethods.add(new PayMethod("yaya_junwang", JUNWANGCODE));
		paymethods.add(new PayMethod("yaya_qq", QQCODE));
		paymethods.add(new PayMethod("yaya_wxpay", WXPAYCODE));
		paymethods.add(new PayMethod("yaya_yinlian", YINLIAN));
		paymethods.add(new PayMethod("yaya_wxpluin", YAYAWANWEIXINPLUIN));
		return paymethods;
	}

	/**
	 * 获取清单文件中的<meta-data>标签信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static Bundle getMetaDataInfo(Context paramContext) {
		ApplicationInfo appInfo = null;
		try {
			appInfo = paramContext.getPackageManager()
					.getApplicationInfo(paramContext.getPackageName(),
							PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (appInfo.metaData == null)
			return null;
		return appInfo.metaData;
	}

	/**
	 * 获取gameid信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getGameInfo(Context paramContext, String name) {
		
		
		if(name.equals("yayawan_orientation")){
			if (DeviceUtil.isLandscape(paramContext)) {
				return "landscape";
			}else {
				return "portrait";
			}
		}else {
			Bundle dataInfo = getMetaDataInfo(paramContext);
			if (((dataInfo = getMetaDataInfo(paramContext)) == null)
					|| (dataInfo.get(name) == null)) {
				throw new IllegalArgumentException("must set the "+name);
			}
			return dataInfo.getString(name).replace("string", "");
		}
		
		
	}

	/**
	 * 获取gameid信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getGameId(Context paramContext) {
		if (gameid != null) {
			return gameid;
		}
		Bundle dataInfo = getMetaDataInfo(paramContext);
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (dataInfo.get(APP_ID) == null)) {
			throw new IllegalArgumentException("must set the appid");
		}
		return dataInfo.getString(APP_ID).replace("kk", "");
	}

	/**
	 * 获取appid信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getAppid(Context paramContext) {
		if (appid != null) {
			return appid;
		}
		Bundle dataInfo = getMetaDataInfo(paramContext);
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (dataInfo.get(APP_ID) == null)) {
			throw new IllegalArgumentException("must set the app_id");
		}
		return dataInfo.getString(APP_ID).replace("kk", "");
	}

	/**
	 * 获取UNION_ID
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getUnionid(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (dataInfo.get(UNION_ID) == null)) {
			return null;
		}
		return dataInfo.get(UNION_ID).toString().replace("yaya", "");
	}

	/**
	 * 获取UNION_GAME_ID
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getUnionGameid(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (dataInfo.get(UNION_GAME_ID) == null)) {
			return null;
		}
		return dataInfo.get(UNION_GAME_ID).toString().replace("yaya", "");
	}

	/**
	 * 获取gamekey
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getGameKey(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (dataInfo.get(GAME_KEY) == null)) {
			return null;
		}
		return dataInfo.get(GAME_KEY).toString();
	}

	/**
	 * 获取手机IMEI
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getIMEI(Context paramContext) {
		// 获取设备的imei号
		
		int isfirstin=Sputils.getSPint("isfirstin", 1, paramContext);
		if (isfirstin==1) {
			if (!PermissionUtils.checkPermission(paramContext, Manifest.permission.READ_PHONE_STATE)) {
				Yayalog.loger("无READ_PHONE_STATE授权");
				
				return "";
			}else {
				// 获取设备的imei号
				String deviceId = ((TelephonyManager) paramContext
						.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
				// 如果imei为空,获取mac地址
				if (deviceId == null || "0".equals(deviceId)) {
					WifiManager wifi = (WifiManager) paramContext
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo info = wifi.getConnectionInfo();
					deviceId = getDEC(info.getMacAddress());
				}
				return deviceId;
			}
			
		}else {
			if (!PermissionUtils.checkAndRequestPermission(paramContext, Manifest.permission.READ_PHONE_STATE,PermissionUtils.READ_PHONE_STATE)) {
				Yayalog.loger("无READ_PHONE_STATE授权");
				return "";
			}else {
				// 获取设备的imei号
				String deviceId = ((TelephonyManager) paramContext
						.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
				// 如果imei为空,获取mac地址
				if (deviceId == null || "0".equals(deviceId)) {
					WifiManager wifi = (WifiManager) paramContext
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo info = wifi.getConnectionInfo();
					deviceId = getDEC(info.getMacAddress());
				}
				return deviceId;
			}
			
		}
	}

	/**
	 * 获取versioncode
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		String versionCode = "";
		try {
			packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			versionCode = packageInfo.versionCode + "";
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 判断是否是手机
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean isPhone(Context paramContext) {
		// 获取设备的imei号
		String deviceId = ((TelephonyManager) paramContext
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		// 如果imei为空,获取mac地址
		if (deviceId == null || "0".equals(deviceId)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断横竖屏
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean isLandscape(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (!dataInfo.getBoolean(ISLANDSCAPE))) {
			return false;
		}
		return dataInfo.getBoolean(ISLANDSCAPE);
	}

	/**
	 * 判断是否debug模式
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean isDebug(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (!dataInfo.getBoolean(ISDEBUG))) {
			return false;
		}
		return dataInfo.getBoolean(ISDEBUG);
	}

	/**
	 * 判断是否要退出框
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean isHaveexit(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (!dataInfo.getBoolean("isHaveexit"))) {
			return false;
		}
		return dataInfo.getBoolean("isHaveexit");
	}

	/**
	 * 判断是否支持账号切换
	 * 
	 * @param paramContext
	 * @return
	 */
	public static boolean changeAcount(Context paramContext) {
		Bundle dataInfo;
		if (((dataInfo = getMetaDataInfo(paramContext)) == null)
				|| (!dataInfo.getBoolean("yayawan_nochangecount"))) {
			return false;
		}
		return dataInfo.getBoolean("yayawan_nochangecount");
	}

	/**
	 * 获取程序包名
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getPackageName(Context paramContext) {
		PackageManager localPackageManager = paramContext.getPackageManager();
		try {
			return localPackageManager.getPackageInfo(
					paramContext.getPackageName(), 0).packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取sim卡信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getSim(Context paramContext) {
		return ((TelephonyManager) paramContext
				.getSystemService(Context.TELEPHONY_SERVICE))
				.getSimSerialNumber();
	}

	/**
	 * 获取MAC地址
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getMAC(Context paramContext) {
		return (((WifiManager) paramContext
				.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo())
				.getMacAddress();
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getModel() {
		String model = Build.MODEL;
		return model;
	}

	/**
	 * 获取手机品牌
	 * 
	 * @return
	 */
	public static String getBrand() {
		return Build.BRAND;
	}

	/**
	 * 获取mac地址的十进制
	 * 
	 * @param mac
	 * @return
	 */
	public static String getDEC(String mac) {
		String[] split = mac.split(":");
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < split.length; i++) {
			int ii = Integer.parseInt(split[i], 16);
			buffer.append(ii);
		}
		return buffer.toString();
	}

	public static boolean isXiaomi(Activity paramActivity) {
		// TODO Auto-generated method stub
		Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramActivity)) == null)
                || (!dataInfo.getBoolean("isxiaomi"))) {
            return false;
        }
        return dataInfo.getBoolean("isxiaomi");
	}

}
