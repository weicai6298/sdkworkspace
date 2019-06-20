package com.kkgame.sdk.login;

import java.math.BigInteger;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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
import android.widget.Toast;



import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.xml.Weibologinxml_po;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;

public class Weibologin_jf extends BaseView implements KgameSdkUserCallback {

	private Weibologinxml_po mThisview;
	private WebView wv_mWeiboview;
	private ImageButton iv_mPre;
	private RelativeLayout rl_mLoading;
	private Button bt_mReload;
	private ProgressBar pb_mLoading;
	String url = "";
	private int mTempcode = 0;
	private LinearLayout ll_mPre;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 111:
				wv_mWeiboview.setVisibility(View.GONE);
				rl_mLoading.setVisibility(View.VISIBLE);
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
	private TextView tv_titil;
	private int sreentype;

	class Handle {
		@JavascriptInterface
		public void show(String data) {
			// Toast.makeText(mContext, "执行了handler.show方法" + data, 0).show();

			if (data.contains("找不到网页")) {

				if (View.GONE == bt_mReload.getVisibility()) {

					handler.sendEmptyMessage(111);

				}

			}
			// new
			// AlertDialog.Builder(WebViewActivity.this).setMessage(data).create().show();
		}
	}

	public Weibologin_jf(Activity mContext) {
		super(mContext);
	}

	@Override
	public View initRootview() {
		mThisview = new Weibologinxml_po(mActivity);
		return mThisview.initViewxml();
	}

	@SuppressLint("NewApi") @Override
	public void initView() {
		// DialogUtil.showProgressDlg("正在加载...", mContext);

		mUserCallback = KgameSdk.mUserCallback;
		
		Theme theme = mActivity.getTheme();
		theme.applyStyle(R.style.Theme_Holo_Light, true);

		iv_mPre = mThisview.getIv_mPre();
		rl_mLoading = mThisview.getRl_mLoading();
		bt_mReload = mThisview.getBt_mReload();
		pb_mLoading = mThisview.getPb_mLoading();
		tv_titil = mThisview.getTv_zhuce();

		wv_mWeiboview = mThisview.getWv_mWeiboview();
		WebSettings settings = wv_mWeiboview.getSettings();
		settings.setAllowFileAccess(true);
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true); // 启用JS脚本
	
		wv_mWeiboview.addJavascriptInterface(new Handle(), "handler");
		
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setUseWideViewPort(true);
		settings.setSupportMultipleWindows(true);
	
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
	
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(false);
		settings.setUseWideViewPort(true);
		settings.setSupportMultipleWindows(false);
	
		settings.setAppCacheEnabled(true);
	
		settings.setDomStorageEnabled(true);
		settings.setGeolocationEnabled(true);
		settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		wv_mWeiboview.setVisibility(View.VISIBLE);
		rl_mLoading.setVisibility(View.GONE);
		pb_mLoading.setVisibility(View.GONE);
		bt_mReload.setVisibility(View.GONE);
		wv_mWeiboview.setWebViewClient(new WebViewClient() {

			
			
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.e("xinkai:", url);
			
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// System.out.println(3 + "这是错误代码++++++++++++++++");

				
						 super.onPageFinished(view, url);

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
				// TODO Auto-generated method stub
				Log.e("现在的host:", url);
				// yayawan://success?uid=8964218049340922879&username=aa86528798&token=2dddbdf4ed081efc73828efb566b1482&money=0

				if (url.startsWith("yayawan://success")) {
					int indexOf3 = url.indexOf("uid=");
					int indexOf4 = url.indexOf("&username=");
					// &money=
					int indexOf5 = url.indexOf("&token=");
					int indexOf6 = url.indexOf("&money=");
					String uid = url.substring(indexOf3 + 4, indexOf4);
					
					String username = url.substring(indexOf4 + 10, indexOf5);
					
					String token = url.substring(indexOf5 + 7,indexOf6);
					String money = url.substring(indexOf6 + 7);
					
					User mUser = new User(username, new BigInteger(uid), token,
							1, "", 0 + money);
					System.out.println(mUser.toString());
					// 将用户信息保存到全局变量
					AgentApp.mUser = mUser;

					// 开启悬浮窗服务
					//YayaWan.init(mActivity);

					
					//onSuccess(mUser, 1);
					Login_success_dialog login_success_dialog = new Login_success_dialog(
							ViewConstants.mMainActivity);
					
					login_success_dialog.dialogShow();
					mActivity.finish();
				}

				if (url.startsWith("yayawan://cancel")) {

					Intent intent = new Intent(mContext,
							BaseLogin_Activity.class);
					// intent.putExtra("url",
					// "https://passport.yayawan.com/oauthclient?type=1&display=mobile");
					intent.putExtra("isfirstlogin", ViewConstants.NOFIRSTLOGIN);
					intent.putExtra("type", ViewConstants.LOGIN_VIEW);
					mActivity.startActivity(intent);
					Toast.makeText(mContext, "授权失败...", Toast.LENGTH_SHORT)
							.show();
					mActivity.finish();

					// return true;
				}else if (url.startsWith("yayawan://cancel")) {

					Intent intent = new Intent(mContext,
							BaseLogin_Activity.class);
					// intent.putExtra("url",
					// "https://passport.yayawan.com/oauthclient?type=1&display=mobile");
					intent.putExtra("isfirstlogin", ViewConstants.NOFIRSTLOGIN);
					intent.putExtra("type", ViewConstants.LOGIN_VIEW);
					mActivity.startActivity(intent);
					Toast.makeText(mContext, "授权失败...", Toast.LENGTH_SHORT)
							.show();
					mActivity.finish();

					// return true;
				}else if(url.startsWith("wtloginmqq")){
					mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				}else {
					
				}
			

				return super.shouldOverrideUrlLoading(view, url);
			}
			
			          

			

		});

		// wv_mWeiboview.setc
		// 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
		wv_mWeiboview.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& wv_mWeiboview.canGoBack()) {
						wv_mWeiboview.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});

		// final EditText url = (EditText) findViewById(R.id.url);

		// Button loadURL = (Button) findViewById(R.id.loadURL);

		Intent intent = mActivity.getIntent();
		url = intent.getStringExtra("url");
		sreentype = intent.getIntExtra("screen", 0);

		if (url.equals(ViewConstants.WEIBOLOGINURL)) {
			tv_titil.setText("微博登录");
		} else {
			tv_titil.setText("QQ登录");
		}

		wv_mWeiboview.loadUrl(url); // 加载url
		wv_mWeiboview.requestFocus(); // 获取焦点

	}

	@Override
	public void logic() {
		bt_mReload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wv_mWeiboview.reload();
			}
		});

		ll_mPre = mThisview.getLl_mPre();
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Login_ho_dialog login_ho_dialog = new Login_ho_dialog(ViewConstants.mMainActivity);
				login_ho_dialog.dialogShow();

				mActivity.finish();
			}
		});

	}

	private void onSuccess(int status, Bundle paramObject) {
		// TODO Auto-generated method stub
		/*
		 * User user = new User();
		 * user.setUserName(paramObject.getString("username")); user.setUid(new
		 * BigInteger(paramObject.getString("uid")));
		 * user.setToken(paramObject.getString("token")); Util.user = user; if
		 * (yayaCallback != null) { yayaCallback.onSuccess(user, paramInt); }
		 */

	}

	@Override
	public void onDestroy() {
		wv_mWeiboview.stopLoading();
		wv_mWeiboview.destroy();
		super.onDestroy();
	}

	@Override
	public void onSuccess(User paramUser, int paramInt) {
		if (mUserCallback != null) {
			mUserCallback.onSuccess(paramUser, paramInt);
		}
		mUserCallback = null;
		mActivity.finish();
	}

	@Override
	public void onError(int paramInt) {
		// TODO Auto-generated method stub
		if (mUserCallback != null) {
			mUserCallback.onError(paramInt);
		}
		mUserCallback = null;
		mActivity.finish();
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		if (mUserCallback != null) {
			mUserCallback.onCancel();
		}
		mUserCallback = null;
		mActivity.finish();
	}

}
