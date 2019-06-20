package com.yayawan.impl;

import android.content.Intent;
import android.graphics.Color;
//import com.example.h5demo.BrowserTempActivity;
import com.hulian.cntzd.MainActivity;
import com.quicksdk.QuickSdkSplashActivity;

public class SplashActivity extends QuickSdkSplashActivity {
	   @Override
	   public int getBackgroundColor() {
	       return Color.WHITE;
	   }
	   @Override
	   public void onSplashStop() {
	       //闪屏结束后，跳转到游戏界面
	       Intent intent = new Intent(this, MainActivity.class);
	       startActivity(intent);
	       this.finish();
	   }
	}
