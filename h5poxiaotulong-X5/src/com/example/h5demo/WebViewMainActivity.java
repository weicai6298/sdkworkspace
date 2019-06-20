package com.example.h5demo;



import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.example.h5demo.ScreenListener.ScreenStateListener;
import com.kkgame.utils.DeviceUtil;
import com.syhl.pxtl.R;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebViewClient;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.main.Kgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;

import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;



public class WebViewMainActivity extends Activity{



   // public static String url="http://danjiyou.duapp.com/Api/Yigui/index";
    public static String url="https://gamebox.yayawan.com";
     // public static String url="http://test.gamebox.yayawan.com";
    //
   // public static String url="https://map.baidu.com/";
//    https://map.baidu.com/
    public static String uploadfileurl="https://rest.yayawan.com/user/upload_icon/";
    public static X5WebView mwebview;
    public static X5WebView paywebview;
    private static Activity mActivity;
    public static final String mHomeUrl = "http://u8g8g.com/h5game/public/?pid=485&gid=1003740";
    public static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获得刚才发送的Message对象，然后在这里进行UI操作
            Log.i("tag","------------> msg.what = " + msg.what);
            switch (msg.what) {
			case 1:
				rootview.removeView(iv);
				break;
			default:
				break;
			}
        }
    };
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view_main);
        mActivity = this;
        Kgame.getInstance().onCreate(this);
        Kgame.getInstance().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						LogoWindow(mActivity);
					}
				});
				init();
			}

			@Override
			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}
		});
//        rl_webview = (RelativeLayout) findViewById(R.id.rl_webview);
//
//        rl_webview.setVisibility(View.GONE);
		ScreenListener();
    }
    static ImageView tv;
    static RelativeLayout rl_webview;
    
    private void init() {
    	mwebview= (X5WebView) findViewById(R.id.webView1);
    	mwebview.addJavascriptInterface(new GameApi(this, mwebview), "GameApi");
    	mwebview.loadUrl(mHomeUrl);
    	mwebview.setWebViewClient(new WebViewClient() {
    	   		
			public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
				Log.i("tag","加载完毕 == " +url);
				super.onPageFinished(view, url);
			}
    	});
    }
   
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Kgame.getInstance().exit(this, new YYWExitCallback() {
				@Override
				public void onExit() {
					finish();
				}
			});
			//Toast.makeText(getApplicationContext(), "退出窗口", 0).show();
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
		//return super.onKeyDown(keyCode, event);
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Kgame.getInstance().onActivityResult(this, requestCode, resultCode,
				data);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		Kgame.getInstance().onNewIntent(intent);
		if (intent == null || mwebview == null || intent.getData() == null)
			return;
		mwebview.loadUrl(intent.getData().toString());
	}

	@Override
	protected void onDestroy() {

		if (mwebview != null)
			mwebview.destroy();
		super.onDestroy();
		Kgame.getInstance().onDestroy(this);
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
	
	private static void ScreenListener(){
		ScreenListener screenlistener = new ScreenListener(mActivity);
		screenlistener.begin(new ScreenStateListener() {

            @Override
            public void onUserPresent() {// 解锁
                Log.e("onUserPresent", "onUserPresent");
//                Toast.makeText(mActivity, "解锁了" , Toast.LENGTH_SHORT ).show();
                mwebview.onResume();
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
                mwebview.onPause();
//                mWebView.pauseTimers();
               
            }
        });
	}
	
	
	static Activity con;
	private static ViewGroup rootview;
	private static ImageView iv;
	private static android.widget.FrameLayout.LayoutParams lp;
	public static void LogoWindow(Activity co) {

		con = co;
		rootview = (ViewGroup) con.getWindow().getDecorView();

		createView();
	}

	private static void createView() {

		iv = new ImageView(con);
		
		lp = new android.widget.FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// lp.setMargins(machSize(10), machSize(80), 0, 0);
		iv.setLayoutParams(lp);
		AssetManager assetManager = con.getAssets();

		InputStream istr = null;
		try {

				istr = assetManager.open("logo.png");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(istr);

		iv.setBackgroundColor(Color.parseColor("#f7faf1"));
		iv.setImageBitmap(bitmap);
		iv.setScaleType(ScaleType.CENTER_CROP);
		rootview.addView(iv);

	}
}
