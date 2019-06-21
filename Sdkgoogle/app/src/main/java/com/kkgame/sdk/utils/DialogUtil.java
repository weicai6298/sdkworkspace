package com.kkgame.sdk.utils;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.xml.MachineFactory;

public class DialogUtil {

	private static ProgressDialog mPb;

	private static ArrayList<ProgressDialog> mPbs = new ArrayList<ProgressDialog>();

	private static ProgressDialog progressDlg;

	public static void showDialog(Context context, String message) {

		Activity activity = (Activity) context;

		if (activity.getParent() != null) {
			activity = activity.getParent();
		}

		mPb = new ProgressDialog(activity);

		mPb.setMessage(message);

		mPb.setCancelable(false);
		mPb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (!mPb.isShowing() && !activity.isFinishing()) {
			mPb.show();
		}

	}

	public static void dismissDialog() {

		// if (mPbs != null && mPbs.size() > 0) {

		// for (ProgressDialog pb : mPbs) {
		if (mPb != null && mPb.isShowing()) {
			mPb.dismiss();
			mPb = null;
		}
		// }
		// mPbs.clear();
		// }
	}

	/**
	 * 弹出进度对话框，message是显示的字。ctx是activity
	 * 
	 * @param strMessage
	 * @param ctx
	 */
	public static void showProgressDlg(String strMessage, Context ctx) {

		stopProgressDlg();
		if (null == progressDlg) {
			progressDlg = new ProgressDialog(ctx);
			// 设置进度条样式
			progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 提示的消息
			progressDlg.setMessage(strMessage);

			progressDlg.setIndeterminate(false);
			progressDlg.setCancelable(false);
			progressDlg.show();
		}
	}

	/**
	 * 结束进度条
	 */
	public static void stopProgressDlg() {
		if (null != progressDlg) {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}

	private static Dialog dialog;

	private static LinearLayout baselin;

	private static CodeCountDown mCodeCountDown;

	private static int time = 20;
	private static Handler mHandler = new Handler() {

		private CodeCountDown mCodeCountDown;

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			// TODO
			DialogUtil.dismissDialog();
			switch (msg.what) {

			case 201:
				if (time > 0) {
					time = time - 1;
					mTime.setText("" + time);
					mHandler.sendEmptyMessageDelayed(201, 1500);

				} else {
					time = 20;
				}

				break;

			default:
				break;
			}
		}

	};
	protected static int MATCH_PARENT = -1;
	protected static int WRAP_CONTENT = -2;
	private static TextView mTime;

	@SuppressLint("NewApi")
	public static void createDialog(final Activity mContext, String message) {

		dialog = new Dialog(mContext);

		// setContentView可以设置为一个View也可以简单地指定资源ID
		// LayoutInflater
		// li=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		// View v=li.inflate(R.layout.dialog_layout, null);
		// dialog.setContentView(v);
		// 去除title
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mContext);
		machineFactory.MachineView(baselin, 360, 190, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER);
		// dialog.setContentView(R.layout.dialog_layout);

		TextView mTitile = new TextView(mContext);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 360, 190, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		machineFactory.MachineTextView(mTitile, WRAP_CONTENT, WRAP_CONTENT, 0,
				message, 28, "LinearLayout", 0, 0, 0, 0);
		mTitile.setGravity(Gravity.CENTER);
		mTitile.setTextColor(Color.GRAY);
		mTitile.setBackgroundColor(Color.WHITE);

		mTime = new TextView(mContext);

		machineFactory.MachineTextView(mTime, WRAP_CONTENT, WRAP_CONTENT, 0,
				"", 28, "LinearLayout", 0, 0, 0, 0);
		mTime.setGravity(Gravity.CENTER);
		mTime.setTextColor(Color.GRAY);
		mTime.setBackgroundColor(Color.WHITE);

		mHandler.sendEmptyMessageDelayed(201, 1500);

		ll_content.addView(mTitile);
		ll_content.addView(mTime);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		// dialog.
		// dialog.addContentView(view, params);
		// dialog.setTitle("Custom Dialog");

		/*
		 * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
		 * 对象,这样这可以以同样的方式改变这个Activity的属性.
		 */
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		/*
		 * lp.x与lp.y表示相对于原始位置的偏移.
		 * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
		 * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
		 * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
		 * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
		 * 当参数值包含Gravity.CENTER_HORIZONTAL时
		 * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
		 * 当参数值包含Gravity.CENTER_VERTICAL时
		 * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
		 * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
		 * Gravity.CENTER_VERTICAL.
		 * 
		 * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
		 * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了, Gravity.LEFT, Gravity.TOP,
		 * Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
		 */
		/*
		 * lp.x = 200; // 新位置X坐标 lp.y = 200; // 新位置Y坐标 lp.width = 600; // 宽度
		 * lp.height = 600; // 高度
		 */
		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// dialog.onWindowAttributesChanged(lp);
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);
		/*
		 * 将对话框的大小按屏幕大小的百分比设置
		 */
		// WindowManager m = getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
		// dialogWindow.setAttributes(p);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		// ap2.addRule(RelativeLayout.BELOW, );
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.show();
	}

	public static void createDialognotime(final Activity mContext,
			String message) {

		dialog = new Dialog(mContext);

		// setContentView可以设置为一个View也可以简单地指定资源ID
		// LayoutInflater
		// li=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		// View v=li.inflate(R.layout.dialog_layout, null);
		// dialog.setContentView(v);
		// 去除title
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mContext);
		machineFactory.MachineView(baselin, 360, 190, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER);
		// dialog.setContentView(R.layout.dialog_layout);

		TextView mTitile = new TextView(mContext);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 360, 190, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		machineFactory.MachineTextView(mTitile, WRAP_CONTENT, WRAP_CONTENT, 0,
				message, 28, "LinearLayout", 0, 0, 0, 0);
		mTitile.setGravity(Gravity.CENTER);
		mTitile.setTextColor(Color.GRAY);
		mTitile.setBackgroundColor(Color.WHITE);

		ll_content.addView(mTitile);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);
		
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// dialog.onWindowAttributesChanged(lp);
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
			time = 20;
			dialog.dismiss();
			dialog = null;
		}

	}
	


}
