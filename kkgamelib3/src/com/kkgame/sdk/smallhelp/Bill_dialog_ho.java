package com.kkgame.sdk.smallhelp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;



public class Bill_dialog_ho extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private WebView wv_mWebview;
	private Button bt_mReload;
	private ProgressBar pb_mLoading;

	public Bill_dialog_ho(Activity activity) {
		super(activity);
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
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		
		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 700;

		int height=0;
		int with=0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height=ho_height;
			with=ho_with;
		} else if ("portrait".equals(orientation)) {
			height=po_height;
			with=po_with;
		}
		
		
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout",2,25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"消费记录", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mContext);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				MATCH_PARENT, MATCH_PARENT, 0, mLinearLayout, 0, 0, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);
		ll_content1.setGravity(Gravity_CENTER);

		wv_mWebview = new WebView(mActivity);
		machineFactory.MachineView(wv_mWebview, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		pb_mLoading = new ProgressBar(mContext);
		machineFactory.MachineView(pb_mLoading, 80, 80, 0, mLinearLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_IN_PARENT);

		bt_mReload = new Button(mContext);
		machineFactory.MachineButton(bt_mReload, 350, 96, 0, "连接失败,点击重新连接", 28,
				mLinearLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mReload.setTextColor(Color.WHITE);

		ll_content1.addView(wv_mWebview);
		ll_content1.addView(bt_mReload);
		ll_content1.addView(pb_mLoading);

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

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {

		WebSettings settings = wv_mWebview.getSettings();
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true); // 启用JS脚本
		//settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 关闭webview中缓存
		wv_mWebview.addJavascriptInterface(new Handle(), "handler");
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);  
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

		String url = "https://passport.yayawan.com/uv/finance?display=mobile"
				 + "&token=" + AgentApp.mUser.token
				+ "&app_id=" + DeviceUtil.getGameId(mActivity) + "&uid="
				+ AgentApp.mUser.uid;

		//System.out.println(url);
		wv_mWebview.loadUrl(url); // 加载url
		wv_mWebview.requestFocus(); // 获取焦点

	}

}
