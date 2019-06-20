package com.example.h5demo;

import static java.lang.Integer.parseInt;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.example.h5demo.ScreenListener.ScreenStateListener;
import com.syhl.rxhj.R;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.main.Kgame;
import com.yayawan.proxy.GameApi;

@SuppressWarnings("deprecation")
@SuppressLint("Override")
@TargetApi(21)
public class BrowserTempActivity extends Activity {
	/**
	 * 作为一个浏览器的示例展示出来，采用android+web的模式
	 */
	private static final String mHomeUrl = "https://mfrk.lyzbh5.jiulingwan.com/37/login2.php"; //专服
	
	private static  Activity mActivity;
	private static Button bt1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//
		try {
			if (parseInt(android.os.Build.VERSION.SDK) >= 11) {
				getWindow()
						.setFlags(
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
								android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			}
		} catch (Exception e) {
		}
		/*
		 * getWindow().addFlags( android.view.WindowManager.LayoutParams.);
		 */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_x5view);
		mActivity = this;

		Kgame.getInstance().anim(this, new YYWAnimCallBack() {

			public void onAnimSuccess(String arg0, Object arg1) {
                Log.i("tag","1");
				init();
			}

			public void onAnimFailed(String arg0, Object arg1) {

			}

			public void onAnimCancel(String arg0, Object arg1) {

			}
		});

		Kgame.getInstance().onCreate(this);

		rl_webview = (RelativeLayout) findViewById(R.id.rl_webview);
		
		bt1 = (Button) findViewById(R.id.bt1);
		bt1.setVisibility(View.GONE);
		bt1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mWebView.loadUrl(mHomeUrl);
			}
		});

		rl_webview.setVisibility(View.VISIBLE);
		ScreenListener();
	}

	static RelativeLayout rl_webview;
	private static WebView mWebView;

	private void init() {
		Log.i("tag","登陆1");
		mWebView = (WebView) findViewById(R.id.webView1);
		mWebView.addJavascriptInterface(new GameApi(this, mWebView), "GameApi");

		//登录
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println(url);
				Log.i("tag","登录url = " +url);
				if (url.contains("uid=")) {
					rl_webview.setVisibility(View.VISIBLE);
				}
				mWebView.loadUrl(url);
				return false;

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			View myVideoView;
			View myNormalView;
			CustomViewCallback callback;

			/**
			 * 全屏播放配置
			 */
			@Override
			public void onShowCustomView(View view,
					CustomViewCallback customViewCallback) {
				Log.i("tag","4");
				FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
				ViewGroup viewGroup = (ViewGroup) normalView.getParent();
				viewGroup.removeView(normalView);
				viewGroup.addView(view);
				myVideoView = view;
				myNormalView = normalView;
				callback = customViewCallback;
			}

			@Override
			public void onHideCustomView() {
				Log.i("tag","5");
				if (callback != null) {
					callback.onCustomViewHidden();
					callback = null;
				}
				if (myVideoView != null) {
					ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
					viewGroup.removeView(myVideoView);
					viewGroup.addView(myNormalView);
				}
			}
			
			@Override 
			public void onReceivedTitle(WebView view, String title) { 
				super.onReceivedTitle(view, title); 
				if (title.equals("网页无法打开")||title.equals("404 Not Found")) { 
//				view.stopLoading(); 
//				webView.loadUrl("file:///android_asset/index.html"); //加载本地页面 
					mWebView.stopLoading();
					bt1.setVisibility(View.VISIBLE);
				}else {
					bt1.setVisibility(View.GONE);
				}
			}
		});

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		// webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
				.getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);

		// mWebView.addJavascriptInterface(new InJavaScriptLocalObj(),
		// "local_obj");

		mWebView.loadUrl(mHomeUrl);
		Log.i("tag","7");
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().sync();
	}

	public void clearWebViewCache() {
		// 清除cookie即可彻底清除缓存
		Log.i("tag","12");
		CookieSyncManager cookieSyncManager = CookieSyncManager
				.createInstance(getApplicationContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();
		cookieManager.removeAllCookie();

	}

	boolean[] m_selected = new boolean[] { true, true, true, true, false,
			false, true };

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("tag","退出");
			Kgame.getInstance().exit(this, new YYWExitCallback() {
				public void onExit() {
					Log.i("tag","退出2");
					finish();
				}
			});
			return true;
		}
		return super.onKeyDown(keyCode, event);
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Kgame.getInstance().onActivityResult(this, requestCode, resultCode,
				data);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		Kgame.getInstance().onNewIntent(intent);
		if (intent == null || mWebView == null || intent.getData() == null)
			return;
		Log.i("tag","11");
		mWebView.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy() {

		if (mWebView != null)
			mWebView.destroy();
		super.onDestroy();
		Kgame.getInstance().onDestroy(this);
		ScreenListener.unregisterListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Kgame.getInstance().onResume(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Kgame.getInstance().onRestart(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		Kgame.getInstance().onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Kgame.getInstance().onStop(this);
	}

	public void onActivityReenter(int resultCode, Intent data) {
		super.onActivityReenter(resultCode, data);
	}

	public static final int MSG_OPEN_TEST_URL = 0;
	public static final int MSG_INIT_UI = 1;
	private final int mUrlStartNum = 0;
	private int mCurrentUrl = mUrlStartNum;
	
	private static void ScreenListener(){
		ScreenListener screenlistener = new ScreenListener(mActivity);
		screenlistener.begin(new ScreenStateListener() {

            @Override
            public void onUserPresent() {// 解锁
                Log.e("onUserPresent", "onUserPresent");
//                Toast.makeText(mActivity, "解锁了" , Toast.LENGTH_SHORT ).show();
                mWebView.onResume();
//                mWebView.resumeTimers();
            }

            @Override
            public void onScreenOn() {// 开屏
                Log.e("onScreenOn", "onScreenOn");
//                Toast.makeText(mActivity, "屏幕打开了" , Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onScreenOff() {// 锁屏
                Log.e("onScreenOff", "onScreenOff");
//                Toast.makeText(mActivity, "屏幕关闭了" , Toast.LENGTH_SHORT ).show();
                mWebView.onPause();
//                mWebView.pauseTimers();/
            }
        });
	}

}
