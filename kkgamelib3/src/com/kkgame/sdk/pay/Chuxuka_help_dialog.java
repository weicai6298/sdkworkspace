package com.kkgame.sdk.pay;



import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.login.StringConstants;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.MachineFactory;

public class Chuxuka_help_dialog extends Basedialogview {

	private LinearLayout ll_title;
	private LinearLayout ll_mHome;
	private LinearLayout ll_mPersonal;

	public Chuxuka_help_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 620, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 620, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		TextView chongzhihelp1 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp1, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHUXUKA_HELP1, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp2 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp2, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHUXUKA_HELP2, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp3 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp3, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHUXUKA_HELP3, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp4 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp4, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHUXUKA_HELP4, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		
		ll_content.addView(chongzhihelp1);
		ll_content.addView(chongzhihelp2);
		ll_content.addView(chongzhihelp3);
		ll_content.addView(chongzhihelp4);

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
