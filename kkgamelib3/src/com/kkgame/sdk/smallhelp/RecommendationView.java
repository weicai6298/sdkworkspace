package com.kkgame.sdk.smallhelp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kkgame.sdk.bean.GameInfo;
import com.kkgame.sdk.xml.Recomendationview_xml_po;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;


public class RecommendationView extends BaseContentView {

	private Recomendationview_xml_po mThisview;
	private Button bt_mReload;
	private ProgressBar pb_mLoading;
	private ListView lv_Recommendationlist;
	private ArrayList<GameInfo> mGameList;
	private WebView wv_mWebview;
	protected static final int MOREGAMELIST = 2;

	public RecommendationView(Activity activity) {
		super(activity);
	}

	@Override
	public View initview() {
		mThisview = new Recomendationview_xml_po(mActivity);

		return mThisview.initViewxml();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 111:
				wv_mWebview.setVisibility(View.GONE);
				pb_mLoading.setVisibility(View.GONE);
				bt_mReload.setVisibility(View.VISIBLE);

				/*
				 * Toast.makeText(mContext, "执行了handler.show方法" + data, 0)
				 * .show();
				 */
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	class Handle {
		@JavascriptInterface
		public void show(String data) {
			// Toast.makeText(mContext, "执行了handler.show方法" + data, 0).show();

			if (data.contains("找不到网页")) {

				wv_mWebview.setVisibility(View.GONE);
				pb_mLoading.setVisibility(View.GONE);
				bt_mReload.setVisibility(View.VISIBLE);

			}
			// new
			// AlertDialog.Builder(WebViewActivity.this).setMessage(data).create().show();
		}
	}

	@Override
	public void initdata() {
		wv_mWebview = mThisview.getWv_mWebview();
		pb_mLoading = mThisview.getPb_mLoading();

		bt_mReload = mThisview.getBt_mReload();

		WebSettings settings = wv_mWebview.getSettings();
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true); // 启用JS脚本
		// settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//
		// 关闭webview中缓存
		wv_mWebview.addJavascriptInterface(new Handle(), "handler");

		wv_mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// System.out.println(1 + "这是错误代码++++++++++++++++");

				wv_mWebview.setVisibility(View.GONE);
				pb_mLoading.setVisibility(View.VISIBLE);
				bt_mReload.setVisibility(View.GONE);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				wv_mWebview.setVisibility(View.VISIBLE);

				pb_mLoading.setVisibility(View.GONE);
				bt_mReload.setVisibility(View.GONE);

				if (View.GONE == bt_mReload.getVisibility()) {
					wv_mWebview
							.loadUrl("javascript:window.handler.show(document.body.innerHTML);");

				}

				// super.onPageFinished(view, url);

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

			}

			@Override
			public void onReceivedHttpAuthRequest(WebView view,
					HttpAuthHandler handler, String host, String realm) {

				super.onReceivedHttpAuthRequest(view, handler, host, realm);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//System.out.println("----------overideurl" + url);
				if (url.endsWith(".apk")) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					mContext.startActivity(intent);
				}
				return super.shouldOverrideUrlLoading(view, url);
		
			}

		});

		// wv_mWeiboview.setc
		// 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
		wv_mWebview.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& wv_mWebview.canGoBack()) {
						wv_mWebview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});

		// final EditText url = (EditText) findViewById(R.id.url);

		// Button loadURL = (Button) findViewById(R.id.loadURL);

		Intent intent = mActivity.getIntent();
		// mobile&token=xxxx&app_id=xxxx&uid=xxxxx

		// https://passport.yayawan.com/uv/rc_game?display=mobile&token=xxxx&app_id=xxxx&uid=xxxxx
		// https://passport.yayawan.com/uv/recharge?display=mobile&token=xxxx&app_id=xxxx&uid=xxxxx
		String url = "https://passport.yayawan.com/uv/rc_game?display=mobile"
				+ "&token=" + AgentApp.mUser.token + "&app_id="
				+ DeviceUtil.getGameId(mActivity) + "&uid="
				+ AgentApp.mUser.uid;

		//System.out.println(url);
		wv_mWebview.loadUrl(url); // 加载url
		wv_mWebview.requestFocus(); // 获取焦点

	}

}
