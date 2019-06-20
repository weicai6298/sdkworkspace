package com.kkgame.sdk.login;

import com.kkgame.sdk.utils.AndroidDelegate;
import com.kkgame.sdk.utils.LogoWindow;
import com.kkgame.sdk.xml.SmallHelp_xml;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.RelativeLayout;

public class SmallHelpActivity extends Activity{

	
	private RelativeLayout rl_mLoading;
	public static int isclose;

	public Activity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SmallHelp_xml smallHelp_xml = new SmallHelp_xml(this);
		setContentView(smallHelp_xml.initViewxml());
		
		this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);  
		
		final WebView wv_mWeiboview = smallHelp_xml.getWv_mWeiboview();
		mActivity=this;
		rl_mLoading = smallHelp_xml.getRl_mLoading();
		String uid=AgentApp.mUser.uid+"";
		String token=AgentApp.mUser.token;
		String appid=DeviceUtil.getAppid(this);
		
		String url=ViewConstants.smallhelp+"?uid="+uid+"&token="+token+"&appid="+appid;
		WebSettings webSetting = wv_mWeiboview.getSettings();
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
		 AndroidDelegate mandroiddelegate =new AndroidDelegate(this);
		 wv_mWeiboview.addJavascriptInterface(mandroiddelegate, "androidDelegate");
		
		wv_mWeiboview.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println(url);
                
				
				if (url.contains(".apk")) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(url);   
					intent.setData(content_url);  
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
					mActivity.startActivity(intent);
				}
				
				if (url.contains("changeuser")) {
					//切换账号
					AgentApp.mUser = null;
					// ViewConstants.HADLOGOUT = true;
					Sputils.putSPint("ischanageacount", 0,
							ViewConstants.mMainActivity);
					if (KgameSdk.mUserCallback!=null) {
						KgameSdk.mUserCallback.onLogout();
					}
					isclose = 1;
					LogoWindow.mhandler.sendEmptyMessage(2);
					finish();
				}else {
					wv_mWeiboview.loadUrl(url);
				}
				
				
				
				
				 return super.shouldOverrideUrlLoading(view, url);

			}


			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				rl_mLoading.setVisibility(View.GONE);
			}


		});
		 url=ViewConstants.smallhelp+"?uid="+uid+"&token="+token+"&appid="+appid;
		switch (KgameSdk.managertype) {
		case 1:
			 url=ViewConstants.smallhelp+"?uid="+uid+"&token="+token+"&appid="+appid;
			break;
			
case 2:
	 url=ViewConstants.smallhelpgift+"?uid="+uid+"&token="+token+"&appid="+appid;	
			break;
case 3:
	 url=ViewConstants.smallhelpcustomer_service+"?uid="+uid+"&token="+token+"&appid="+appid;
	break;

		default:
			break;
		}
		wv_mWeiboview.loadUrl(url);
		//rl_mLoading.setVisibility(View.GONE);
		Yayalog.loger(url);
		
		
		smallHelp_xml.getBaseLinearLayout().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				setResult(020202, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
				isclose = 0;
				LogoWindow.mhandler.sendEmptyMessage(2);
				finish();
			}
		});
		
	}
}
