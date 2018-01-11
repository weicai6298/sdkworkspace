package com.kkgame.sdk.xml;



import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.kkgame.utils.DeviceUtil;

public class DisplayUtils {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 得到的屏幕的宽度
	 */
	public static int getWidthPx(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.widthPixels;
	}

	/**
	 * 得到的屏幕的宽度2
	 */
	public static int getWidthPx2(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		WindowManager wm = activity.getWindowManager();
		return wm.getDefaultDisplay().getWidth();
	}

	/**
	 * 得到的屏幕的高度
	 */
	public static int getHeightPx(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		WindowManager wm = activity.getWindowManager();
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 得到的屏幕的高度2
	 */
	public static int getHeightPx2(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.heightPixels;
	}

	/**
	 * 得到屏幕的dpi
	 * 
	 * @param activity
	 * @return
	 */
	public static int getDensityDpi(Activity activity) {
		// DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
		DisplayMetrics displaysMetrics = new DisplayMetrics();// 初始化一个结构
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaysMetrics);// 对该结构赋值
		return displaysMetrics.densityDpi;
	}

	/**
	 * 返回状态栏/通知栏的高度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}

	/**
	 * 将720的像素值转换成其他屏幕的像素值
	 * 
	 * @param size
	 * @param activity
	 * @return
	 */
	public static int dealWihtSize(int size, Activity activity) {

		float widthPx = 720;

		

		// 判断屏幕放向
		String orientation = DeviceUtil.getOrientation(activity);
		
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {

			widthPx = getHeightPx(activity);
		} else if ("portrait".equals(orientation)) {
			   // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			widthPx = getWidthPx(activity);

		}

		// Log.e("前size", size+"++++++++++++++");
		// Log.e("widthPx", widthPx + "++++++++++++++");
		if (widthPx == 720) {
			return size;
		}
		/*if (widthPx>=1080) {
			widthPx=1080;
		}*/
		float bili = 720 / widthPx;
		// Log.e("bili", bili+"++++++++++++++");
		int resize = (int) ((size / bili) + 0.5);
		// Log.e("后size", resize+"++++++++++++++");
		return resize;

	}

}
