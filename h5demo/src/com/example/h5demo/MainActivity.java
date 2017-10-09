package com.example.h5demo;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.yayawan.main.Kgame;
import com.yayawan.proxy.GameApi;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Kgame.getInstance().onCreate(this);
        setContentView(R.layout.activity_main);
        final WebView webview = (WebView) findViewById(R.id.wb_webview);
        
        WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("UTF-8");
		webSettings.setDomStorageEnabled(true);
		webSettings.setBlockNetworkImage(false);
		webSettings.setLoadWithOverviewMode(true);
		Log.i("tag","17");
		webview.addJavascriptInterface(new GameApi(MainActivity.this,webview),
				"GameApi");

		webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webview.setVerticalScrollBarEnabled(false);
		//https://api.sdk.75757.com/web/profile/?uid=3867385116174336225&token=49651f5888ae6ae016669a8441873cc4&appid=2585027502

		webview.loadUrl("http://jump.h5.jiulingwan.com:81/webserver/07073/android/index.html");
		webview.loadUrl("https://api.sdk.75757.com/web/profile/?uid=3867385116174336225&token=49651f5888ae6ae016669a8441873cc4&appid=2585027502");
		
		
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("tag","10");
				System.out.println(url);
				webview.loadUrl(url);
				return false;

			}

			
		});
    }


	@Override
	protected void onRestart() {
		super.onRestart();
		Kgame.getInstance().onRestart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onresume");
		// Toast.makeText(MainActivity.this, "退出回调"+this.hasWindowFocus(),
		// Toast.LENGTH_SHORT).show();

		Kgame.getInstance().onResume(this);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Kgame.getInstance().onDestroy(this);

	}

    
}
