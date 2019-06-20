package com.kkgame.sdk.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;



public class Help_dissmiss_dialog extends Basedialogview {

	private static User mUser;

	protected static final int CLOSE = 111;

	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			// TODO
			switch (msg.what) {

			case CLOSE:

				dialogDismiss();
				
				break;

			default:
				break;
			}
		}

	};
	private TextView tv_message1;
	private TextView tv_userid;
	private Button bt_change;

	public Help_dissmiss_dialog(Activity activity) {
		super(activity);

	}

	/**
	 * 控制dialog三秒消失
	 */
	@Override
	public void dialogShow() {

		//int sPint = Sputils.getSPint("help_dissmiss_show", 0, mActivity);

		if (ViewConstants.ISSHOWDISMISSHELP) {
			dialog.show();

			new Thread(new Runnable() {

				@Override
				public void run() {

					mHandler.sendEmptyMessageDelayed(CLOSE, 2000);
				}
			}).start();
		}

		

	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		int ho_height = 100;
		int ho_with = 1000;
		int po_height = 100;
		int po_with = 600;

		int height = 0;
		int with = 0;
		int bt_with = 0;
		int bt_textsize = 0;
		int tv_textsize = 0;
		int maginbut = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {

			height = ho_height;
			with = ho_with;
			bt_with = 200;
			bt_textsize = 28;
			tv_textsize = 36;
			maginbut = 500;
		} else if ("portrait".equals(orientation)) {

			height = po_height;
			with = po_with;
			bt_with = 200;
			bt_textsize = 28;
			tv_textsize = 32;
			maginbut = 900;
		}

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, 0, mLinearLayout,
				0, 0, 0, maginbut, 100);
		// ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mActivity));
		ll_content.setGravity(Gravity.CENTER_VERTICAL);

//		LinearLayout ll_textline = new LinearLayout(mActivity);
//		machineFactory.MachineView(ll_textline, 0, MATCH_PARENT, 1,
//				mLinearLayout);
//		ll_textline.setGravity(Gravity.CENTER_VERTICAL);
//
//		tv_message1 = new TextView(mActivity);
//		machineFactory.MachineTextView(tv_message1, WRAP_CONTENT, WRAP_CONTENT,
//				0, "长按3秒小助手隐藏,摇一摇会重新出现哦.", tv_textsize, mLinearLayout, 10, 0, 0,
//				0);
//		tv_message1.setTextColor(Color.parseColor("#ec7600"));

//		tv_userid = new TextView(mActivity);
//		machineFactory.MachineTextView(tv_userid, WRAP_CONTENT, WRAP_CONTENT,
//				0, "", tv_textsize, mLinearLayout, 0, 0, 0, 0);
//		tv_userid.setTextColor(Color.parseColor("#ec7600"));

		// TODO
//		ll_textline.addView(tv_message1);
//		ll_textline.addView(tv_userid);
//
//		bt_change = new Button(mActivity);
//		machineFactory.MachineButton(bt_change, bt_with, 80, 0, "不再提示",
//				bt_textsize, mLinearLayout, 0, 0, 10, 0);
//		bt_change.setBackgroundDrawable(GetAssetsutils
//				.get9DrawableFromAssetsFile("yaya_greenbut.9.png", mContext));
//		bt_change.setTextColor(Color.WHITE);
		

		// TODO
//		ll_content.addView(ll_textline);
//		ll_content.addView(bt_change);

//		baselin.addView(ll_content);
//		dialog.setContentView(baselin);
//		Window dialogWindow = dialog.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		dialogWindow.setGravity(Gravity.CENTER);

//		lp.alpha = 0.9f; // 透明度

//		lp.dimAmount = 0f; // 设置背景色对比度

		// lp.y = 60;

//		dialogWindow.setAttributes(lp);

		dialog.setCanceledOnTouchOutside(true);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		// dialog.setCanceledOnTouchOutside(true);

//		initlog();

	}

	private void initlog() {

		bt_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ViewConstants.ISSHOWDISMISSHELP=false;
				dialogDismiss();
			}
		});
	}

}
