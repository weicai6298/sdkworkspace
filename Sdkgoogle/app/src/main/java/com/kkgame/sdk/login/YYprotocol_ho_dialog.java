package com.kkgame.sdk.login;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.common.CommonData;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;


public class YYprotocol_ho_dialog extends Basedialogview {

	public YYprotocol_ho_dialog(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private WebView wv_mWebview;
	private Button bt_mReload;
	private ProgressBar pb_mLoading;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 111:
				wv_mWebview.setVisibility(View.GONE);
				pb_mLoading.setVisibility(View.GONE);
				bt_mReload.setVisibility(View.VISIBLE);

				/*
				 * Toast.makeText(mActivity, "执行了handler.show方法" + data, 0)
				 * .show();
				 */
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private WebView mContent;

	class Handle {
		@JavascriptInterface
		public void show(String data) {
			// Toast.makeText(mActivity, "执行了handler.show方法" + data, 0).show();

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
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		int height = 560;
		int with = 630;

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
		//machineFactory.MachineView(baselin, with, height, "LinearLayout");
		//baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);
		baselin.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title,
				MATCH_PARENT, 78, 0, mLinearLayout, 35, 28, 35, 0, 100);
		rl_title.setBackgroundColor(Color.parseColor("#ffffff"));

		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 46, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mActivity);
		machineFactory.MachineView(iv_mPre, 46, 46, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya1_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		

		// 注册textview
		TextView tv_zhuce = new TextView(mActivity);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"服务协议", 44, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.parseColor("#c05011"));
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mActivity);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				MATCH_PARENT, MATCH_PARENT, 0, mLinearLayout, 0, 0, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);
		ll_content1.setGravity(Gravity_CENTER);

		wv_mWebview = new WebView(mActivity);
		machineFactory.MachineView(wv_mWebview, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		pb_mLoading = new ProgressBar(mActivity);
		machineFactory.MachineView(pb_mLoading, 80, 80, 0, mLinearLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_IN_PARENT);

		bt_mReload = new Button(mActivity);
		machineFactory.MachineButton(bt_mReload, 350, 96, 0, "连接失败,点击重新连接", 28,
				mLinearLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mReload.setTextColor(Color.WHITE);

		ll_content1.addView(wv_mWebview);
		//ll_content1.addView(bt_mReload);
		//ll_content1.addView(pb_mLoading);

		ll_content.addView(rl_title);

		ll_content.addView(ll_content1);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {

		mContent = wv_mWebview;

		mContent.getSettings().setSavePassword(false);
		mContent.getSettings().setSaveFormData(false);
		mContent.getSettings().setAllowFileAccess(true);
		mContent.getSettings().setBuiltInZoomControls(false);
		mContent.getSettings().setDatabaseEnabled(true);
		mContent.getSettings().setDomStorageEnabled(true);
		mContent.getSettings().setAppCacheMaxSize(12582912L);
		mContent.getSettings().setAppCacheEnabled(true);
		mContent.getSettings().setCacheMode(-1);
		mContent.getSettings().setSupportZoom(false);
		mContent.getSettings().setDefaultTextEncodingName("utf-8");
		mContent.getSettings().setJavaScriptEnabled(true);
		mContent.getSettings().setRenderPriority(RenderPriority.HIGH);
		mContent.getSettings().setBlockNetworkImage(true);
		mContent.getSettings().setLoadWithOverviewMode(true);
		mContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mContent.setVerticalScrollBarEnabled(false);
		mContent.setHorizontalScrollBarEnabled(false);
		mContent.setBackgroundColor(-1);
		mContent.setVisibility(0);
		mContent.requestFocus();

	
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

		mContent.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String paramString1,
					String paramString2, JsResult paramJsResult) {
				Toast.makeText(view.getContext(), paramString2,
						Toast.LENGTH_SHORT).show();
				paramJsResult.confirm();
				return true;
			}

			@Override
			public void onReceivedTitle(WebView paramAnonymousWebView,
					String paramAnonymousString) {
				super.onReceivedTitle(paramAnonymousWebView,
						paramAnonymousString);
			}

			@Override
			public void onExceededDatabaseQuota(String url,
					String databaseIdentifier, long currentQuota,
					long estimatedSize, long totalUsedQuota,
					WebStorage.QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(estimatedSize * 2);
			}

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				Log.d("MyApplication", message + " -- From line " + lineNumber
						+ " of " + sourceID);
			}

			@Override
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication",
						cm.message() + " -- From line " + cm.lineNumber()
								+ " of " + cm.sourceId());
				return true;
			}
		});

		mContent.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				Log.i("onLoadResource", "onLoadResource:" + url);
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				mContent.getSettings().setBlockNetworkImage(false);
				super.onPageFinished(view, url);
				CookieSyncManager.getInstance().sync();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				if (url.startsWith("lidroid")) {
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.e("eee", "Error: " + description);
				super.onReceivedError(view, errorCode, description, failingUrl);
				view.loadData("<html><body></body></html>", "text/html",
						"UTF-8");
			}

		});
		mContent.loadUrl(CommonData.YONGHUXIEYI);// 加载协议页面

	}
}
