package com.kkgame.sdk.login;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;

import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.utils.Yayalog;

public abstract class BaseView {

	protected Context mContext;
	protected Activity mActivity;
	private View view;
	protected KgameSdkUserCallback mUserCallback;
	protected SharedPreferences mSp;

	public BaseView(Activity mContext) {
		this.mContext = mContext;
		this.mActivity = mContext;
		view = initRootview();
		initView();
		logic();
		// mUserCallback = KgameSdk.mUserCallback;
		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	public abstract View initRootview();

	public View getView() {
		return view;
	}

	// 获取控件
	public abstract void initView();

	// 逻辑代码
	public abstract void logic();

	// start
	public void onStart() {
		Yayalog.loger("支付onstart");
	};

	

	public void onLogout() {

	}

	public void onDestroy() {
		Yayalog.loger("支付ondestroy");
	}

	public void onResume() {
		Yayalog.loger("支付onresume");
	}

	public void onStop() {
		Yayalog.loger("支付onstop");
	}

	public void startActivityForResult(Intent intent, int requestCode) {

	}

	public boolean onkeyDown(int keyCode, KeyEvent event) {
		
		return false;
	}

	public void onPause() {
		// TODO Auto-generated method stub
		Yayalog.loger("支付onpause");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

}
