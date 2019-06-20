package com.kkgame.sdk.pay;



import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kkgame.sdk.login.StringConstants;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.MachineFactory;

public class QQka_help_dialog extends Basedialogview {

	public QQka_help_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 650, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		
		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 650, "LinearLayout",2,25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		ScrollView sv_help = new ScrollView(mActivity);
		machineFactory.MachineView(sv_help, MATCH_PARENT, MATCH_PARENT, mLinearLayout);
		
		LinearLayout ll_svcontent = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_svcontent, MATCH_PARENT, MATCH_PARENT, mLinearLayout);
		ll_svcontent.setOrientation(LinearLayout.VERTICAL);
		
		sv_help.addView(ll_svcontent);
		
		TextView chongzhihelp1 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp1, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP1, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp2 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp2, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP2, 28,
				mLinearLayout, 20, 20, 20, 0);
		TextView chongzhihelp3 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp3, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP3, 28,
				mLinearLayout, 20, 20, 20, 0);
		TextView chongzhihelp4 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp4, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP4, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp5 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp5, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP5, 28,
				mLinearLayout, 20, 20, 20, 0);
		TextView chongzhihelp6 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp6, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP6, 28,
				mLinearLayout, 20, 20, 20, 0);
		TextView chongzhihelp7 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp7, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP7, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp8 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp8, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.QQ_HELP8, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		
		ll_svcontent.addView(chongzhihelp1);
		ll_svcontent.addView(chongzhihelp2);
		ll_svcontent.addView(chongzhihelp3);
		ll_svcontent.addView(chongzhihelp4);
		ll_svcontent.addView(chongzhihelp5);
		ll_svcontent.addView(chongzhihelp6);
		ll_svcontent.addView(chongzhihelp7);
		ll_svcontent.addView(chongzhihelp8);
		
		ll_content.addView(sv_help);

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

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
	}

}
