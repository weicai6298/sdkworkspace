package com.yayawan.proxy;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;

import com.kkgame.sdk.utils.FileUtils;
import com.kkgame.utils.FileIOUtils;
import com.kkgame.utils.Yayalog;

public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;

	public static String TestFilePath = "test";

	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "GameUserData"
			+ File.separator
			+ TestFilePath + File.separator + "test.log";

	// DB_DIR

	public static String DB_DIR = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ "GameUserData"
			+ File.separator
			+ TestFilePath;
	static {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			DB_DIR = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + "GameUserData" + File.separator
					+ TestFilePath;
		} else {
			DB_DIR = Environment.getRootDirectory().getPath() + File.separator
					+ "GameUserData" + File.separator + TestFilePath;
		}

		File dbFolder = new File(DB_DIR);
		if (!dbFolder.exists()) {
			dbFolder.mkdirs();
		}
	}

	public static GameApitest getGameApitestInstants(Activity mactivity) {
		if (mGameapitest != null) {
			mActivity = mactivity;

			return mGameapitest;
		} else {
			mActivity = mactivity;

			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}
	public static GameApitest getGameApitestInstants() {
		if (mGameapitest != null) {
			//mActivity = mactivity;

			return mGameapitest;
		} else {
			//mActivity = mactivity;

			mGameapitest = new GameApitest();

			return mGameapitest;
		}
	}
	
	public static String tempstring="";
	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {

		if (type.equals("YYApplicationoncreate")) {
			File file = new File(DB_DIRPATH);
			file.delete();
		}
		if (tempstring.contains(type)) {
			return;
		}
		tempstring=tempstring+"—temp-"+type;
		
		if (YYcontants.ISDEBUG) {

			File file = new File(DB_DIRPATH);

			
			if (file.exists()) {
				FileIOUtils
						.writeFileFromString(DB_DIRPATH, type + "\r\n", true);

			}else {
				try {
					file.createNewFile();
					FileIOUtils
					.writeFileFromString(DB_DIRPATH, type + "\r\n", true);
					Yayalog.loger("创建了测试文件");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

}
