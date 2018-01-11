package com.kkgame.sdk.xml;

import android.app.Activity;
import android.content.Context;
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
	protected int machSize(int size) {

		int dealWihtSize = DisplayUtils.dealWihtSize(size, mActivity);

		return dealWihtSize;
	}
}
