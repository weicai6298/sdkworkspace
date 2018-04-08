package com.yayawan.proxy;

import java.io.File;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.R.color;
import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.kkgame.sdk.db.SDBHelper;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.FileIOUtils;
import com.kkgame.utils.FileUtils;
import com.yayawan.main.YYWMain;

public class GameApitest {

	public static GameApitest mGameapitest;
	public static Activity mActivity;

	public static String TestFilePath = "test";
	public static String Rootpath = SDBHelper.Rootpath;
	public static String DB_DIRPATH = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ Rootpath
			+ File.separator
			+ TestFilePath + File.separator + "test.log";

	// DB_DIR

	public static String DB_DIR = Environment.getExternalStorageDirectory()
			.getPath()
			+ File.separator
			+ Rootpath
			+ File.separator
			+ TestFilePath;
	static {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			DB_DIR = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + Rootpath + File.separator + TestFilePath;
		} else {
			DB_DIR = Environment.getRootDirectory().getPath() + File.separator
					+ Rootpath + File.separator + TestFilePath;
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

	String temp = "自检步骤\r\n 1.打开游戏，闪屏，登陆游戏\r\n 2.点击小助手，点击切换账号 \r\n (sdk会回调登陆中onLogout方法)\r\n 3.点击支付， 关闭支付\r\n 4.点击游戏内自带切换账号按钮 \r\n 5.按返回键，弹出退出框，点击取消\r\n";

	/**
	 * 
	 * @param type
	 */
	public void sendTest(String type) {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++"
				+ type);
		if (mActivity != null) {
			if (DeviceUtil.isDebug(mActivity)) {
				if (!applicationisinit) {
					Toast.makeText(
							mActivity,
							"===========================\r\n警告：application未接入成功\r\n=======================",
							0).show();
				}
			}
		}

		if (YYcontants.ISDEBUG) {

			try {
				File file = new File(DB_DIRPATH);
				if (!FileUtils.isFileExists(DB_DIRPATH)) {
					file.createNewFile();
				}
				if (type.contains("Application")) {
					file.delete();
					file.createNewFile();
				}

				if (type.equals("onCreate")) {
					addButton();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (temp.contains(type)) {
				return;
			}
			temp = temp + "-" + type + "\r\n";

			FileIOUtils.writeFileFromString(DB_DIRPATH, type, true);
			if (textView != null) {
				textView.setText(temp);
			}
		}

	}

	private void addButton() {

		textView = new TextView(mActivity);
		textView.setText("自检窗口");
		textView.setBackgroundColor(Color.RED);
		textView.setTextColor(Color.WHITE);
		textView.setVisibility(View.GONE);

		android.widget.FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(
				500, 600);
		layoutParams3.setMargins(300, 100, 10, 10);
		textView.setLayoutParams(layoutParams3);

		// TODO Auto-generated method stub
		Button showcheckButton = new Button(mActivity);
		showcheckButton.setText("显示自检窗口");
		showcheckButton.setBackgroundColor(Color.RED);
		showcheckButton.setTextColor(Color.WHITE);
		showcheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// anim(mLinearLayout);
				textView.setVisibility(View.VISIBLE);
			}
		});

		android.widget.FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				200, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(50, 250, 10, 10);
		showcheckButton.setLayoutParams(layoutParams);

		Button dissmisscheckButton = new Button(mActivity);
		dissmisscheckButton.setText("关闭自检窗口");
		dissmisscheckButton.setBackgroundColor(Color.RED);
		dissmisscheckButton.setTextColor(Color.WHITE);
		dissmisscheckButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// anim(mLinearLayout);
				textView.setVisibility(View.GONE);
			}
		});
		android.widget.FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(
				200, LayoutParams.WRAP_CONTENT);
		layoutParams1.setMargins(50, 450, 10, 10);
		dissmisscheckButton.setLayoutParams(layoutParams1);

		FrameLayout decorView = (FrameLayout) mActivity.getWindow()
				.getDecorView();
		decorView.addView(showcheckButton);
		decorView.addView(dissmisscheckButton);
		decorView.addView(textView);

	}

	public void sendTest(String type, String value) {

		if (mActivity != null) {
			if (DeviceUtil.isDebug(mActivity)) {
				if (!applicationisinit) {
					Toast.makeText(
							mActivity,
							"===========================\r\n警告：application未接入成功\r\n=======================",
							0).show();
				}
			}
		}

		if (YYcontants.ISDEBUG) {
			if (!FileUtils.isFileExists(DB_DIRPATH)) {
				File file = new File(DB_DIRPATH);
			}
			FileIOUtils.writeFileFromString(DB_DIRPATH, type + ":" + value
					+ "\r\n", true);
		}

	}

	public static boolean applicationisinit = false;
	private TextView textView;

	public static void initOnapplication(YYWApplication yywApplication) {
		// TODO Auto-generated method stub
		applicationisinit = true;
	}

}
