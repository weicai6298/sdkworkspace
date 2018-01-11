package com.kkgame.sdk.other;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.MachineFactory;



public class Exit_dialog extends Basedialogview {

	

	public Exit_dialog(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 550, 300, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, 550, 300, mLinearLayout);
		rl_content.setBackgroundColor(Color.rgb(57, 162,227));

		
		
	
		Button bt_cancel = new Button(mActivity);
		machineFactory.MachineButton(bt_cancel, 150, 60, 0, "取消", 32, mRelativeLayout, machSize(400), machSize(300), 0, 0);
		bt_cancel.setBackgroundColor(Color.parseColor("#ee5d7c"));
		
		Button bt_ok = new Button(mActivity);
		machineFactory.MachineButton(bt_ok, 150, 60,  0, "退出", 0, mRelativeLayout, machSize(70), machSize(350), 0, 0);
		rl_content.setBackgroundColor(Color.parseColor("#000000"));
		
		
		
		//rl_content.addView(bg_iv);
		rl_content.addView(bt_cancel);
		rl_content.addView(bt_ok);

		// ll_content.addView(chongzhihelp2);

		baselin.addView(rl_content);

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
