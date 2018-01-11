package com.kkgame.sdk.smallhelp;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public abstract class BaseContentView {

	private View rootview;
	protected Activity mActivity;
	protected Context mContext;
	protected Personal_dialog_ho personal_dialog;

	public BaseContentView(Activity activity) {
		mActivity = activity;
		mContext = activity;

		rootview = initview();

	}

	public BaseContentView(Activity activity, Personal_dialog_ho personal_dialog) {
		mActivity = activity;
		mContext = activity;
		this.personal_dialog = personal_dialog;
		rootview = initview();

	}

	public abstract View initview();

	public abstract void initdata();

	public View getRootview() {
		return rootview;
	}

	public void onResume() {

	}
}
