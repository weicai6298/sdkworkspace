package com.yayawan.impl;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWAnimation;

public class AnimationImpl implements YYWAnimation {

	@Override
	public void anim(final Activity paramActivity) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "播放动画", Toast.LENGTH_SHORT).show();
		System.err.println("播放动画");

		// String MY_PKG_NAME = paramActivity.getPackageName();

		// Intent paramYayawanStartAnimationCallback = new
		// Intent(paramActivity.getApplicationContext(), logoAnimation.class);
		// paramActivity.startActivity(paramYayawanStartAnimationCallback);

		// setContentView(paramActivity.getResources().getIdentifier("logo_start",
		// "layout", MY_PKG_NAME));

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				new LogoWindow(paramActivity);

			}
		});

	}

}

/*
 * class logoAnimation extends Activity{
 * 
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * 
 * setContentView(this.getResources().getIdentifier("logo_start", "layout",
 * getPackageName())); }
 * 
 * 
 * }
 */

class LogoWindow {
	private WindowManager wm;
	private WindowManager.LayoutParams params;
	Activity con;
	boolean isadd = false;

	private LinearLayout myview;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:

				rootview.removeView(iv);
				YYWMain.mAnimCallBack.onAnimSuccess("success", "");
				break;
			}
		}
	};

	private ViewGroup rootview;
	private ImageView iv;
	private android.widget.FrameLayout.LayoutParams lp;

	public LogoWindow(Activity co) {

		this.con = co;
		rootview = (ViewGroup) con.getWindow().getDecorView();

		createView();
	}

	private void createView() {

		iv = new ImageView(con);
		
		lp = new android.widget.FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// lp.setMargins(machSize(10), machSize(80), 0, 0);
		iv.setLayoutParams(lp);
		AssetManager assetManager = con.getAssets();

		InputStream istr = null;
		try {
			if(DeviceUtil.isLandscape(con)){
				istr = assetManager.open("heng.png");
			}else{
				istr = assetManager.open("shu.png");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(istr);

//		iv.setBackgroundColor(Color.parseColor("#f7faf1"));
		iv.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
		iv.setImageBitmap(bitmap);
		iv.setScaleType(ScaleType.CENTER);
		rootview.addView(iv);

		mHandler.sendEmptyMessageDelayed(1, 3000L);
	}
}
