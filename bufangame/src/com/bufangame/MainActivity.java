package com.bufangame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.bufangame.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 下载Apk 自动安装Apk
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends Activity {
	static File file;
	static ProgressDialog dialog;
//	static Boolean isdownload = false ;

	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:
				//				button1.setText("点击安装");
				//				down = 1;
				//				installApk();
				break;
			case 2:
				//				down = 2;
				//				button1.setText("打开");
				break;
			}
		}

	};

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);


		WebView mWebView = (WebView) findViewById(R.id.wv1);

		WebSettings webSettings = mWebView.getSettings();

		// 设置与Js交互的权限
		webSettings.setJavaScriptEnabled(true);
		// 设置允许JS弹窗
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		// 步骤1：加载JS代码
		// 格式规定为:file:///android_asset/文件名.html
		mWebView.loadUrl("http://game.bufan.com/wap");


		// 复写WebViewClient类的shouldOverrideUrlLoading方法
		mWebView.setWebViewClient(new WebViewClient() {
			@SuppressLint("NewApi")
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, final String url) {

				// 步骤2：根据协议的参数，判断是否是所需要的url
				// 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
				//假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

				Log.i("tag","url="+url);
				//				Uri uri = Uri.parse(url);
				String apk = url.substring(url.length()-4, url.length());
				Log.i("tag","apk="+apk);
				if(apk.equals(".apk")){
					//					new Thread(new Runnable() {
					//						public void run() {
					dialog = new ProgressDialog(MainActivity.this);
					dialog.setCancelable(false);
					dialog.setMessage("正在下载，请稍后...");
					dialog.show();
					downFile(url);
					//						}
					//					}).start();
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		}
				);
	}


	// 接收到安装完成apk的广播
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			System.out.println("接收到安装完成apk的广播");

			Message message = handler.obtainMessage();
			message.what = 2;
			handler.sendMessage(message);
		}
	};

	/**
	 * 后台在下面一个Apk 下载完成后返回下载好的文件
	 * 
	 * @param httpUrl
	 * @return
	 */
	public File downFile(final String httpUrl) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(httpUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					FileOutputStream fileOutputStream = null;
					InputStream inputStream;
					if (connection.getResponseCode() == 200) {
						inputStream = connection.getInputStream();

						if (inputStream != null) {
							file = getFile(httpUrl);
							fileOutputStream = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int length = 0;

							while ((length = inputStream.read(buffer)) != -1) {
								fileOutputStream.write(buffer, 0, length);
							}
							fileOutputStream.close();
							fileOutputStream.flush();
						}
						inputStream.close();
					}
					dialog.dismiss();
					System.out.println("已经下载完成");

					//安装
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
					startActivity(intent);


				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return file;
	}

	/**
	 * 安装APK
	 */
	public void installApk() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addDataScheme("package");

		// 注册一个广播
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 解除广播
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * 打开已经安装好的apk
	 */
	//	private void openApk(Context context, String url) {
	//		PackageManager manager = context.getPackageManager();
	//		// 这里的是你下载好的文件路径
	//		PackageInfo info = manager.getPackageArchiveInfo(Environment.getExternalStorageDirectory().getAbsolutePath()
	//				+ getFilePath(url), PackageManager.GET_ACTIVITIES);
	//		Log.i("tag","info="+info);
	//		if (info != null) {
	//			Intent intent = manager.getLaunchIntentForPackage(info.applicationInfo.packageName);
	//			startActivity(intent);
	//		}
	//	}

	/**
	 * 根据传过来url创建文件
	 * 
	 */
	private static File getFile(String url) {
		File files = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), getFilePath(url));
		return files;
	}

	/**
	 * 截取出url后面的apk的文件名
	 * 
	 * @param url
	 * @return
	 */
	private static String getFilePath(String url) {
		return url.substring(url.lastIndexOf("/"), url.length());
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
				//退出
				exit();
			
			return super.onKeyDown(keyCode, event);
		}  

		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		 AlertDialog.Builder builder = new Builder(MainActivity.this);  
		 builder.setMessage("确定要退出吗?");  
	        builder.setTitle("退出");  
	        builder.setPositiveButton("确认",  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	                android.os.Process.killProcess(android.os.Process.myPid()); 
	            }  
	        });  
	        builder.setNegativeButton("取消",  
	        new android.content.DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	                dialog.dismiss();  
	            }  
	        });  
	        builder.create().show();  
	}
	

}
