package com.kkgame.sdk.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.kkgame.sdk.pay.Payment_jf;
import com.kkgame.sdk.pay.Yayapaymain_jf;
import com.kkgame.sdk.pay.Yinlian;
import com.kkgame.sdk.smallhelp.Personal_dialog_ho;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class BaseLogin_Activity extends Activity {

	private final int WEIBOLOGIN = 4;
	protected Context mContext = this;
	protected BaseView mBaseview;
	private Personal_dialog_ho personal_dialog_ho;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去标题头
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);

		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if ("portrait".equals(orientation)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		Intent intent = this.getIntent();
		type = intent.getIntExtra("type", 0);

		System.out.println(type);
		switch (type) {
		case ViewConstants.STARTANIMATION:
			mBaseview = new Startanim_jf(this);
			setContentView(mBaseview.getView());
			break;
		
		case WEIBOLOGIN:
			mBaseview = new Weibologin_jf(this);
			setContentView(mBaseview.getView());
			break;

		case ViewConstants.YAYAPAYMAIN:
			mBaseview = new Yayapaymain_jf(this);
			
			setContentView(mBaseview.getView());
			break;

		case ViewConstants.PAYMENT_JF:
			mBaseview = new Payment_jf(this);
			this.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
			
			setContentView(mBaseview.getView());
			
			break;
		case ViewConstants.YINLIANPAY_ACTIVITY:
			mBaseview = new Yinlian(this);
			setContentView(mBaseview.getView());
			break;
		
		case ViewConstants.ACCOUNTMANAGER:
			personal_dialog_ho = new Personal_dialog_ho(this);
			personal_dialog_ho.dialogShow();
			break;

		default:
			break;
		}

		// setView(savedInstanceState);

	}

	@Override
	protected void onStart() {
		Yayalog.loger("baseviewonstart");
		if (mBaseview != null) {
			mBaseview.onStart();
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Yayalog.loger("baseviewonstop");
		if (mBaseview != null) {
			mBaseview.onStop();
		}

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Yayalog.loger("baseviewondestory");
		if (mBaseview != null) {
			mBaseview.onDestroy();
		}

		super.onDestroy();
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Yayalog.loger("baseviewonresume");
		if (mBaseview != null) {
			mBaseview.onResume();
		}
		if (personal_dialog_ho != null) {
			personal_dialog_ho.onResume();
		}
		super.onResume();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub

		if (mBaseview != null) {
			mBaseview.startActivityForResult(intent, requestCode);
		}

		super.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (mBaseview != null) {
			mBaseview.onActivityResult(requestCode, resultCode,data);
		}

		super.onActivityResult(requestCode, resultCode, data);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// super.onKeyDown(keyCode, event);
		return mBaseview.onkeyDown(keyCode, event);
		//
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

}
