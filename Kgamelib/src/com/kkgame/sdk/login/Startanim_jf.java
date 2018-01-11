package com.kkgame.sdk.login;

import java.io.File;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.Theme;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.kkgame.sdk.callback.KgameSdkStartAnimationCallback;
import com.kkgame.sdk.db.DataTransfermationDao;
import com.kkgame.sdk.db.OldSDBHelper;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Startanima_xml;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;

public class Startanim_jf extends BaseView {

	private static final int ANIMSTOP = 10; // 动画播放完成

	private KgameSdkStartAnimationCallback mStartAnimationCallback; // 动画回调

	private SharedPreferences mSp;

	private static final String ACTIVE = "active";

	protected static final int ANIMERROR = 0;

	private Startanima_xml mThisview;
	private ImageView iv_loading;

	private ImageView iv_text;

	public Startanim_jf(Activity mContext) {
		super(mContext);
	}

	@Override
	public View initRootview() {
		mThisview = new Startanima_xml(mActivity);

		return mThisview.initViewxml();
	}

	@Override
	public void initView() {

		mSp = mActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
		mStartAnimationCallback = KgameSdk.mStartAnimationCallback;

		// 把主题换成全屏的
		Theme theme = mActivity.getTheme();
		theme.applyStyle(R.style.Theme_Holo_Light, true);

		iv_loading = mThisview.getIv_loading();
		iv_text = mThisview.getIv_text();

		iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_ani.png", mActivity));
		iv_text.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_logotext.png", mActivity));

		// 数据库添加一列
		UserDao.getInstance(mActivity).upDateclume();

		// 设置横竖屏tupian
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			 iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
			 "yaya_start_ho.jpg", mActivity));

		} else if ("portrait".equals(orientation)) {
			 iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
			 "yaya_start_po.jpg", mActivity));

		}

	}

	@Override
	public void logic() {
		Yayalog.loger("Startanim_js_logic");
		onSuccess();
	}

	public void onSuccess() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onSuccess();
		}
		mStartAnimationCallback = null;
	}

	public void onError() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onError();
		}
		mStartAnimationCallback = null;
	}

	public void onCancel() {
		if (mStartAnimationCallback != null) {

			mStartAnimationCallback.onCancel();
		}
	}

	/**
	 * 监听用户点击返回键,
	 */
	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) {
	 * 
	 * onCancel(); return true; } return super.onKeyDown(keyCode, event); }
	 */

	@Override
	public boolean onkeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			onCancel();
			return true;
		}
		return super.onkeyDown(keyCode, event);
	}

	public void onClick(View v) {

	}

}
