package com.kkgame.sdk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.xml.DisplayUtils;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdk.xml.Toastxml_po;
import com.kkgame.utils.Yayalog;


public class Utilsjf {

	private static Dialog dialog;
	private static LinearLayout baselin;
	protected static int MATCH_PARENT = -1;
	protected static int WRAP_CONTENT = -2;
	protected static String mLinearLayout = "LinearLayout";
	protected static String mRelativeLayout = "RelativeLayout";
	private static ImageView iv_loading;
	private static TextView tv_message;

	/**
	 * 将720像素转成其他像素值
	 * 
	 * @param size
	 * @return
	 */
	public static int machSize(int size, Activity mActivity) {

		int dealWihtSize = DisplayUtils.dealWihtSize(size, mActivity);

		return dealWihtSize;
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
	 * 
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
		iv_imageview.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_dunpai.png", mActivity));
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

	private static PackageInfo packageInfo;
	private static Signature[] signatures;
	private static String signature;
	private static LinearLayout ll_mBut;
	private static Button bt_mPhonelogin;
	private static Button bt_mlogin;
	

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

	/**
	 * 检测是否是小米手机
	 * 
	 * @param propName
	 * @return
	 */
	public static String getSystemProperty() {
		String line;
		BufferedReader input = null;
		String propName = "ro.miui.ui.version.name";
		try {
			Process p = Runtime.getRuntime().exec("getprop " + propName);
			input = new BufferedReader(
					new InputStreamReader(p.getInputStream()), 1024);
			line = input.readLine();
			input.close();
		} catch (IOException ex) {
			Log.e("", "Unable to read sysprop " + propName, ex);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Log.e("", "Exception while closing InputStream", e);
				}
			}
		}
		return line;
	}

	/**
	 * 判断Android系统API的版本
	 * 
	 * @return
	 */
	public static int getAPIVersion() {
		int APIVersion;
		try {
			APIVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			APIVersion = 0;
		}
		return APIVersion;
	}
	
	/**
	 * 将字符串转成二进制
	 * 
	 * @param str
	 * @return
	 */
	public static String StrToBinstr(String str) {
		char[] strChar = str.toCharArray();
		String result = "";
		for (int i = 0; i < strChar.length; i++) {
			result += Integer.toBinaryString(strChar[i]) + "";
		}
		return result;
	}
	
	/**
	 * 输入一个字符串根据其二进制计算出10进制的最后一位数
	 * @param username
	 * @return
	 */
	public static int getRanknumber(String username){
		String k=new BigInteger(StrToBinstr(username), 2).toString();
		//k.subSequence(start, end)
	//	System.out.println();
		//Yayalog.loger(k+"");
		return Integer.parseInt(k.substring(k.length()-1, k.length()));
	}
	
	
	/**
	 * 获取当前包签名
	 * @param mActivity
	 * @return
	 */
	 public static String getSignature(Activity mActivity) {
	       String pkgname =mActivity.getPackageName();
	       boolean isEmpty = TextUtils.isEmpty(pkgname);
	       if (isEmpty) {
	           //Toast.makeText(this, "应用程序的包名不能为空！", Toast.LENGTH_SHORT);
	    	   return null;
	       } else {
	           try {
	               /** 通过包管理器获得指定包名包含签名的包信息 **/
	               packageInfo =mActivity.getPackageManager().getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
	               /******* 通过返回的包信息获得签名数组 *******/
	               signatures = packageInfo.signatures;
	               /******* 循环遍历签名数组拼接应用签名 *******/
	               StringBuffer builder=new StringBuffer();
	               for (Signature signature : signatures) {
	                   builder.append(signature.toCharsString());
	               }
	               /************** 得到应用签名 **************/
	               String sign = builder.toString();
	               //tv_signature.setText(signature);
	               try {
	            	   Yayalog.loger(sign);
					return MD5.MD5(sign);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           } catch (NameNotFoundException e) {
	               e.printStackTrace();
	           }
	       }
		return null;
	   }

	 
	 /**
		 * 对话确认
		 * 
		 * @param mContext
		 * @param message
		 */
		public static void cretipDialog(final Activity mContext,String message,final PayQuesionCallBack callBack) {

			dialog = new Dialog(mContext);

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			baselin = new LinearLayout(mContext);
			baselin.setOrientation(LinearLayout.VERTICAL);
			MachineFactory machineFactory = new MachineFactory(mContext);
			machineFactory.MachineView(baselin, 550, 300, "LinearLayout");
			baselin.setBackgroundColor(Color.TRANSPARENT);
			baselin.setGravity(Gravity.CENTER_VERTICAL);

			
			
			// 中间内容
			LinearLayout ll_content = new LinearLayout(mContext);
			machineFactory.MachineView(ll_content, 550, 300, 0, mRelativeLayout, 0,
					0, 0, 0, 100);
			// ll_content.setBackgroundColor(Color.WHITE);
			ll_content.setBackgroundDrawable(GetAssetsutils
					.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mContext));
			ll_content.setGravity(Gravity.CENTER_VERTICAL);
			ll_content.setOrientation(LinearLayout.VERTICAL);

		
			// tiptextview
			TextView tv_tip = new TextView(mContext);
			machineFactory.MachineTextView(tv_tip, MATCH_PARENT, 100, 0,
					message, 20, mLinearLayout, 0, 0, 0, 0);
			tv_tip.setTextColor(Color.parseColor("#6a6961"));
			tv_tip.setGravity(Gravity.CENTER);
			
			
			
			ll_mBut = new LinearLayout(mContext);
			ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
					MATCH_PARENT, 100, 0, mLinearLayout, 20, 20, 20, 0, 100);

			// 横版手机登录按钮
			bt_mPhonelogin = new Button(mContext);
			bt_mPhonelogin = machineFactory.MachineButton(bt_mPhonelogin, 120,
					80, 1, "取消", 30, mLinearLayout, 0, 0, 0, 0);
			bt_mPhonelogin.setTextColor(Color.WHITE);
			bt_mPhonelogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
					"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mContext));
			bt_mPhonelogin.setGravity(Gravity.CENTER);

			LinearLayout ll_zhanwei = new LinearLayout(mContext);
			ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 20,
					MATCH_PARENT, mLinearLayout);
			bt_mPhonelogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					callBack.onPayCancel();
				}
			});
			
			// button的登录按钮
			bt_mlogin = new Button(mContext);
			bt_mlogin = machineFactory.MachineButton(bt_mlogin, 120, 80, 1,
					"安装", 30, mLinearLayout, 0, 0, 0, 0);
			bt_mlogin.setTextColor(Color.WHITE);
			bt_mlogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
					"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
					mContext));
			bt_mlogin.setGravity(Gravity.CENTER);
			bt_mlogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					callBack.onPaySuccess();
				}
			});
			
			// TODO
			ll_mBut.addView(bt_mPhonelogin);
			ll_mBut.addView(ll_zhanwei);
			ll_mBut.addView(bt_mlogin);

			
			ll_content.addView(tv_tip);
			ll_content.addView(ll_mBut);

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
		
		
		 /**
		 * 对话确认
		 * 
		 * @param mContext
		 * @param message
		 */
		public static void creQuestionDialog(final Activity mContext,final PayQuesionCallBack callBack) {

			dialog = new Dialog(mContext);

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			baselin = new LinearLayout(mContext);
			baselin.setOrientation(LinearLayout.VERTICAL);
			MachineFactory machineFactory = new MachineFactory(mContext);
			machineFactory.MachineView(baselin, 550, 200, "LinearLayout");
			baselin.setBackgroundColor(Color.TRANSPARENT);
			baselin.setGravity(Gravity.CENTER_VERTICAL);

			// 中间内容
			LinearLayout ll_content = new LinearLayout(mContext);
			machineFactory.MachineView(ll_content, 550, 200, 0, mRelativeLayout, 0,
					0, 0, 0, 100);
			// ll_content.setBackgroundColor(Color.WHITE);
			ll_content.setBackgroundDrawable(GetAssetsutils
					.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mContext));
			ll_content.setGravity(Gravity.CENTER_VERTICAL);

		
			ll_mBut = new LinearLayout(mContext);
			ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
					MATCH_PARENT, 100, 0, mLinearLayout, 20, 20, 20, 0, 100);

			// 横版手机登录按钮
			bt_mPhonelogin = new Button(mContext);
			bt_mPhonelogin = machineFactory.MachineButton(bt_mPhonelogin, 120,
					80, 1, "取消", 30, mLinearLayout, 0, 0, 0, 0);
			bt_mPhonelogin.setTextColor(Color.WHITE);
			bt_mPhonelogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
					"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mContext));
			bt_mPhonelogin.setGravity(Gravity.CENTER);

			LinearLayout ll_zhanwei = new LinearLayout(mContext);
			ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 20,
					MATCH_PARENT, mLinearLayout);
			bt_mPhonelogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					callBack.onPayCancel();
				}
			});
			
			// button的登录按钮
			bt_mlogin = new Button(mContext);
			bt_mlogin = machineFactory.MachineButton(bt_mlogin, 120, 80, 1,
					"支付完成", 30, mLinearLayout, 0, 0, 0, 0);
			bt_mlogin.setTextColor(Color.WHITE);
			bt_mlogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
					"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
					mContext));
			bt_mlogin.setGravity(Gravity.CENTER);
			bt_mlogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					callBack.onPaySuccess();
				}
			});
			
			// TODO
			ll_mBut.addView(bt_mPhonelogin);
			ll_mBut.addView(ll_zhanwei);
			ll_mBut.addView(bt_mlogin);

			
			
			ll_content.addView(ll_mBut);

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
		
		
		public interface PayQuesionCallBack {
		    public abstract void onPaySuccess();

		 

		    public abstract void onPayCancel();

		}

}
