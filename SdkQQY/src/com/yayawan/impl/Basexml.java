package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;

public class Basexml {

	protected static int MATCH_PARENT=-1;
	protected static int WRAP_CONTENT=-2;
	protected  static String mLinearLayout="LinearLayout";
	protected static String mRelativeLayout="RelativeLayout";
	
	protected static int Gravity_CENTER=Gravity.CENTER;
	public static Context mContext;
	public static Activity mActivity;
	protected MachineFactory machineFactory;
	public Basexml(Activity activity) {
		this.mContext=activity;
		this.mActivity=activity;
		
		machineFactory = new MachineFactory(mActivity);
		
	}
	
	public Basexml(Context activity) {
		this.mContext=activity;
		this.mActivity=(Activity) activity;
		
		machineFactory = new MachineFactory(mActivity);
	
	
	}
	
	
	
	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	private int machSize(int size) {
		float widthPx = 720;

		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		int height = dm.heightPixels;// 高度

		if (width < height) {
			widthPx = width;
		} else {
			widthPx = height;
		}
		if (widthPx == 720) {
			return size;
		}
		if (widthPx > 1080) {
			widthPx = 1080;
		}
		float bili = 720 / widthPx;
		// Log.e("bili", bili+"++++++++++++++");
		int resize = (int) ((size / bili) + 0.5);
		// Log.e("后size", resize+"++++++++++++++");
		return resize;

	}
}
