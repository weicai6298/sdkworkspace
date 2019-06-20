package com.kkgame.sdk.pay;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
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

import com.kkgame.sdk.bean.BillResult;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.login.BaseView;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.Yinlianpay_xml_po;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Yayalog;

public class Yinlian extends BaseView implements KgameSdkUserCallback {

	private Yinlianpay_xml_po mThisview;
	private WebView wv_mWeiboview;
	private ImageButton iv_mPre;
	private RelativeLayout rl_mLoading;
	private Button bt_mReload;
	private ProgressBar pb_mLoading;
	String url = "";
	private int mTempcode = 0;
	private LinearLayout ll_mPre;


	private final int BILLRESULT=2001;
	private final int DATAERROR=2002;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			
			case BILLRESULT:
				
				if (mBillResult != null) {
					if (mBillResult.success == 1) {
						// 第二次确认操作失败
						if (KgameSdk.mPaymentCallback!=null) {
							KgameSdk.mPaymentCallback.onError(0);
							KgameSdk.mPaymentCallback=null;
						}
						
						ToastUtil.showError(mActivity, mBillResult.error_msg);
						
					} else if (mBillResult.success == 0) {

						if (KgameSdk.mPaymentCallback!=null) {
							KgameSdk.mPaymentCallback.onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
							KgameSdk.mPaymentCallback=null;
						}
						// 支付操作成功等待到账
						//KgameSdk.mPaymentCallback.onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
						
						ToastUtil.showSuccess(mActivity, mBillResult.body);
					}
					
				}
				ViewConstants.mPayActivity.finish();
				mActivity.finish();
				

				break;
			case DATAERROR:
				
				Toast.makeText(mActivity, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();
				ViewConstants.mPayActivity.finish();
				mActivity.finish();
				
				//

				break;
			default:
				Toast.makeText(mActivity, "数据异常，请再次支付。", Toast.LENGTH_LONG)
						.show();
				ViewConstants.mPayActivity.finish();

				mActivity.finish();
				//
				break;
			}
		}

	};
	private TextView tv_titil;
	private int sreentype;

	

	private String mHtml;

	public Yinlian(Activity mContext) {
		super(mContext);

	}

	@Override
	public View initRootview() {
		mThisview = new Yinlianpay_xml_po(mActivity);
		return mThisview.initViewxml();
	}

	private int pageposition = 0;
	private LinearLayout ll_mPre2;
	private BillResult mBillResult;
	@Override
	public void initView() {
		// DialogUtil.showProgressDlg("正在加载...", mContext);

		// mUserCallback = KgameSdk.mUserCallback;

		ll_mPre2 = mThisview.getLl_mPre();
		rl_mLoading = mThisview.getRl_mLoading();
		bt_mReload = mThisview.getBt_mReload();
		pb_mLoading = mThisview.getPb_mLoading();
		tv_titil = mThisview.getTv_zhuce();
		tv_titil.setText("银联收银台");

		ll_mPre2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pageposition>3) {
					checkOrder();
				}else {
					mActivity.finish();
				}
				
			}
		});

		wv_mWeiboview = mThisview.getWv_mWeiboview();
		WebSettings settings = wv_mWeiboview.getSettings();
		settings.setSupportZoom(true); // 支持缩放
		settings.setBuiltInZoomControls(true); // 启用内置缩放装置
		settings.setJavaScriptEnabled(true); // 启用JS脚本
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 关闭webview中缓存
		// wv_mWeiboview.addJavascriptInterface(new Handle(), "handler");

		wv_mWeiboview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if (pageposition > 2) {
					rl_mLoading.setVisibility(View.GONE);
				}
				//System.out.println("" + pageposition);
				pageposition = pageposition + 1;

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println(url);
				if (url.startsWith("yayapayment://success?status=0")) {
					// KgameSdk.mPaymentCallback.onSuccess(paramUser, paramOrder,
					// paramInt);
					checkOrder();
				}else {
					view.loadUrl(url);
				}

				
				
				return true;
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
		mHtml = intent.getStringExtra("url");
		String sub = mHtml.substring(mHtml.indexOf("id=\"orderNumber\" value=\"")+24, mHtml.indexOf("id=\"orderNumber\" value=\"")+30+24);
		// wv_mWeiboview.loadUrl(url); // 加载url
		String orderid = sub.substring(0, sub.indexOf("\""));
		//System.out.println("你懂的1"+sub);
		AgentApp.mPayOrder.id=orderid;
		//System.out.println("你懂的"+orderid);
		//System.out.println(mHtml);
		wv_mWeiboview.loadData(mHtml, "text/html", "utf-8");
		
		wv_mWeiboview.requestFocus(); // 获取焦点

	}

	@Override
	public void logic() {

	}
	
	/**
	 * 检测当前订单是否支付成功
	 */
	public void checkOrder(){
		Utilsjf.creDialogpro(mActivity, "验证支付结果...");
		// 查询订单状态
		/*new Thread() {
		
			@Override
			public void run() {
				try {
					Thread.sleep(6 * 1000);
					Yayalog.loger("第一次查看orderid:"+AgentApp.mPayOrder.id);
					mBillResult = ObtainData.getBillResult(
							mActivity, AgentApp.mUser,
							AgentApp.mPayOrder);
					if (mBillResult.error_code == 701) {

						Thread.sleep(5 * 1000);
						mBillResult = ObtainData.getBillResult(
								mActivity, AgentApp.mUser,
								AgentApp.mPayOrder);

					}
					mHandler.sendEmptyMessage(BILLRESULT);
				} catch (Exception e) {
					e.printStackTrace();
					Yayalog.loger(e.toString());
					mHandler.sendEmptyMessage(DATAERROR);
				}
			}

		}.start();*/
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

		mActivity.finish();
	}

	@Override
	public void onError(int paramInt) {

		mActivity.finish();
	}

	@Override
	public void onCancel() {

		mActivity.finish();
	}

}
