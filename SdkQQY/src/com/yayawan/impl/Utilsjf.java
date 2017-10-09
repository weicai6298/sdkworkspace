package com.yayawan.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class Utilsjf {

	private static Dialog dialog;
	private static LinearLayout baselin;
	protected static int MATCH_PARENT = -1;
	protected static int WRAP_CONTENT = -2;
	protected  static String mLinearLayout="LinearLayout";
	protected static String mRelativeLayout="RelativeLayout";
	private static ImageView iv_loading;
	private static TextView tv_message;
	
	
	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	private int machSize(int size,Activity mactivity) {
		float widthPx = 720;

		DisplayMetrics dm = new DisplayMetrics();
		mactivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
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
	
	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		String input1 = stringFilter(input);
		char[] c = input1.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 全部进度
	 * @param mContext
	 * @param message
	 */
	public static void creDialogpro(final Activity mContext, String message) {

		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mContext);
		machineFactory.MachineView(baselin, 450, 150, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 450, 150, 0, mRelativeLayout, 0,
				0, 0, 0, 100);
		// ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mContext));
		ll_content.setGravity(Gravity.CENTER_VERTICAL);

		iv_loading = new ImageView(mContext);
		machineFactory.MachineView(iv_loading, 100, 100, mLinearLayout, 1, 10);
		iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_loading(1).png", mContext));

		RotateAnimation rotateAnimation = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setDuration(1000);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);

		iv_loading.setAnimation(rotateAnimation);
		iv_loading.startAnimation(rotateAnimation);

		tv_message = new TextView(mContext);
		machineFactory.MachineTextView(tv_message, MATCH_PARENT, WRAP_CONTENT,
				0, "", 36, mLinearLayout, 10, 0, 0, 0);
		tv_message.setTextColor(Color.parseColor("#666666"));
		tv_message.setText(message);
		// TODO
		ll_content.addView(iv_loading);
		ll_content.addView(tv_message);

		baselin.addView(ll_content);
		dialog.setContentView(baselin);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度

		dialogWindow.setAttributes(lp);

		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.show();
	}
	
	
	public static void safePaydialog(final Activity mActivity, String message) {

		dialog = new Dialog(mActivity);

		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Toastxml_po toastxml_po = new Toastxml_po(mActivity);
		View initViewxml = toastxml_po.initViewxml();
		ImageView iv_imageview = toastxml_po.getIv_imageview();
		TextView tv_message = toastxml_po.getTv_message();
		iv_imageview.setImageBitmap(GetAssetsutils.getImageFromAssetsFile("yaya_dunpai.png", mActivity));
		tv_message.setText("检测安全支付...");
		tv_message.setTextColor(Color.WHITE);
		
		dialog.setContentView(initViewxml);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度

		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		// ap2.addRule(RelativeLayout.BELOW, );
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.show();
	}

	public static void stopDialog() {
		if (dialog != null) {

			dialog.dismiss();
			dialog = null;
		}

	}

	public static int getOrientation(Activity mActivity) {
		int orientation = mActivity.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			return 0;
		} else {
			return 1;
		}
		// == Configuration.ORIENTATION_PORTRAIT
	}

	public Drawable getSelector() {
		StateListDrawable drawable = new StateListDrawable();
		return drawable;

		/*
		 * //Non focused states drawable.addState(new
		 * int[]{-android.R.attr.state_focused, -android.R.attr.state_selected,
		 * -android.R.attr.state_pressed}, Color.parseColor("#666666"));
		 * drawable.addState(new int[]{-android.R.attr.state_focused,
		 * android.R.attr.state_selected, -android.R.attr.state_pressed},
		 * getResources().getDrawable(R.drawable.contact_sel)); //Focused states
		 * drawable.addState(new
		 * int[]{android.R.attr.state_focused,-android.R.attr.state_selected,
		 * -android.R.attr.state_pressed},
		 * getResources().getDrawable(R.drawable.contact_sel));
		 * drawable.addState(new
		 * int[]{android.R.attr.state_focused,android.R.attr.state_selected,
		 * -android.R.attr.state_pressed},
		 * getResources().getDrawable(R.drawable.contact_sel)); //Pressed
		 * drawable.addState(new int[]{android.R.attr.state_selected,
		 * android.R.attr.state_pressed},
		 * getResources().getDrawable(R.drawable.contact_sel));
		 * drawable.addState(new int[]{android.R.attr.state_pressed},
		 * getResources().getDrawable(R.drawable.contact_sel));
		 * 
		 * TextView textView = (TextView) findViewById(R.id.TextView_title);
		 * 
		 * textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
		 * null, null);
		 */
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

}
