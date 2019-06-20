package com.yayawan.proxy;

import java.io.File;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;



import com.kkgame.utils.FileIOUtils;
import com.kkgame.utils.FileUtils;
import com.kkgame.utils.Yayalog;





public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;
	public static Context mContext;

	public static String TestFilePath = "test";

	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "GameTest"
			+ File.separator
			+ TestFilePath + ".txt";

	public static GameApitest getGameApitestInstants(Context mcontext) {
		mContext=mcontext;
		if (mGameapitest != null) {
			return mGameapitest;
		} else {
			mGameapitest = new GameApitest();
			return mGameapitest;
		}
	}

	public static GameApitest getGameApitestInstants(Activity mactivity) {
		
		if (mGameapitest != null) {
				return mGameapitest;
		} else {
			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}

	public static GameApitest getGameApitestInstants() {
		
		if (mGameapitest != null) {
			// mActivity = mactivity;

			return mGameapitest;
		} else {
			// mActivity = mactivity;

			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}

	public static String tempstring = "";

	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

		
		Yayalog.loger("中间件："+type);
		if (!YYcontants.ISDEBUG) {
			return;
		}
		
		if (type.contains("Application")) {
			
			
			File file = new File(DB_DIRPATH);
			file.delete();
		}
		if (tempstring.contains(type)) {
			return;
		}
		tempstring = tempstring + "—temp-" + type;

		File file = new File(DB_DIRPATH);

		if (file.exists()) {
			FileIOUtils.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

		} else {
			try {
				Yayalog.loger(file.getAbsolutePath());
				FileUtils.createOrExistsFile(DB_DIRPATH);
				//file.createNewFile();
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);
				Yayalog.loger("创建了测试文件");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void sendTest(String type, String value) {

		if (YYcontants.ISDEBUG) {
			File file = new File(DB_DIRPATH);

			if (file.exists()) {
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

			}
		}

	}
	
	public static void sendTest2(final Activity mactivity){
	
		
	}
	
    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 过滤自定义的App和已下载的App
     * @param packageManager
     * @return
     */
    public static boolean isContentYaboxApp(PackageManager packageManager) {
     
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤指定的app
                String tempPackageName=packageInfo.packageName;
               if (tempPackageName.contains("qihoo360.mobilesafe")) {
				return true;
			}
            }
           
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
    }

}
