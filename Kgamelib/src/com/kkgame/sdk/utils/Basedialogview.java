package com.kkgame.sdk.utils;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import com.kkgame.sdk.bean.Discuss;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.xml.Basexml;
import com.kkgame.sdkmain.KgameSdk;

public abstract class Basedialogview extends Basexml {

	public KgameSdkUserCallback mUserCallback;
	protected SharedPreferences mSp;

	protected Discuss discuss;
	protected boolean dialogisshow=false;

	public Basedialogview(Activity activity) {
		super(activity);
		mUserCallback = KgameSdk.mUserCallback;
		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);

		createDialog(mActivity);
		
		
		ViewConstants.mDialogs.add(dialog);
	}

	public Basedialogview(Activity activity, Discuss discuss) {
		super(activity);
		this.discuss = discuss;
		mUserCallback = KgameSdk.mUserCallback;
		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		createDialog(mActivity);
		ViewConstants.mDialogs.add(dialog);

	}

	public  Dialog dialog;
	protected LinearLayout baselin;
	protected LinearLayout ll_mDele;

	public abstract void createDialog(Activity mActivity);
	
	public  void logic(Activity mActivity){
		
	}

	public void dialogShow() {
		
		if (dialog != null) {
			dialog.show();
			
			//logic(mActivity);
		}
	}

	public  void dialogDismiss() {

		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public void allDismiss() {
		for (int i = 0; i < ViewConstants.mDialogs.size(); i++) {
			if (ViewConstants.mDialogs.get(i) != null) {
				ViewConstants.mDialogs.get(i).dismiss();
			}

			// ViewConstants.mDialogs.remove(0);
		}

		ViewConstants.mDialogs.clear();
	}

	public void onSuccess(User paramUser, int paramInt) {

		if (mUserCallback != null) {
			mUserCallback.onSuccess(paramUser, paramInt);
		}
		mUserCallback = null;
		// mActivity.finish();

		for (int i = 0; i < ViewConstants.mDialogs.size(); i++) {
			if (ViewConstants.mDialogs.get(i) != null) {
				ViewConstants.mDialogs.get(i).dismiss();
			}

			// ViewConstants.mDialogs.remove(0);
		}

		ViewConstants.mDialogs.clear();
		// dialog.dismiss();
	}

	public void onError(int paramInt) {
		if (mUserCallback != null) {
			mUserCallback.onError(paramInt);
		}
		mUserCallback = null;
	}

	public void onCancel() {
		if (mUserCallback != null) {
			mUserCallback.onCancel();
		}
		mUserCallback = null;

	}

	public void onLogout() {
		if (mUserCallback != null) {
			System.out.println("我调用了logout方法");
			mUserCallback.onLogout();
		}
		mUserCallback = null;
		// dialogDismiss();
		dialogDismiss();

	}

}
