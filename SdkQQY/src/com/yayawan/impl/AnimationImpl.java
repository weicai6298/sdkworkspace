package com.yayawan.impl;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWAnimation;

public class AnimationImpl implements YYWAnimation {

	@Override
	public void anim(Activity paramActivity) {
		// TODO Auto-generated method stub
		// Toast.makeText(paramActivity, "播放动画", Toast.LENGTH_SHORT).show();

		YYWMain.mAnimCallBack.onAnimSuccess("success", "");
		
	}

}

class LogoWindow {
	private WindowManager wm;
	private WindowManager.LayoutParams params;
	Context con;
	boolean isadd = false;

	private LinearLayout myview;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (isadd)
					wm.removeView(myview);
				break;
			}
		}
	};

	public LogoWindow(Context co) {

		this.con = co;
		createView();
	}

	private void createView() {

		if (myview == null) {

			myview = new LinearLayout(this.con);
			myview.setBackgroundColor(Color.parseColor("#F7FAF1"));
			myview.setLayoutParams(new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

			myview.setOrientation(LinearLayout.VERTICAL);
			myview.setGravity(Gravity.CENTER);

			ImageView iv = new ImageView(this.con);
			// iv.setLayoutParams(new LayoutParams(source))

			AssetManager assetManager = con.getAssets();

			InputStream istr = null;
			try {
				istr = assetManager.open("logo_start.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap bitmap = BitmapFactory.decodeStream(istr);


			iv.setImageBitmap(bitmap);

			iv.setScaleType(ImageView.ScaleType.CENTER);

			iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

			myview.addView(iv);


			wm = ((WindowManager) this.con.getSystemService("window"));
			params = new WindowManager.LayoutParams();

			params.type = 2002;
			params.flags = 40;

			params.flags |= 262144;
			params.flags |= 512;
			params.gravity = 83;
			isadd = true;
		}

		if (myview != null) {

			wm.addView(myview, params);
		}

		mHandler.sendEmptyMessageDelayed(1, 3000L);
	}
}
