package com.kkgame.sdk.other;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CircleProgressView;
import com.kkgame.sdk.xml.MachineFactory;



public class UpdateProgress_dialog extends Basedialogview {

	private CircleProgressView circleProgressView;

	public UpdateProgress_dialog(Activity activity) {
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
		machineFactory.MachineView(baselin, 400, 400, "LinearLayout");
		baselin.setBackgroundColor(Color.parseColor("#99ffffff"));
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, 400, 400, mLinearLayout);
		rl_content.setBackgroundColor(Color.TRANSPARENT);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 400, 400, "LinearLayout");
		ll_content.setBackgroundColor(Color.TRANSPARENT);
		ll_content.setGravity(Gravity.CENTER);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		circleProgressView = new CircleProgressView(mActivity);

		machineFactory.MachineView(circleProgressView, 360, 360, mLinearLayout);
		circleProgressView.setProgress(0);
		circleProgressView.setmTxtHint1("下载中");
		
		ll_content.addView(circleProgressView);
		
		rl_content.addView(ll_content);

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

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

	}

	public CircleProgressView getCircleProgressView() {
		return circleProgressView;
	}

	public void setCircleProgressView(CircleProgressView circleProgressView) {
		this.circleProgressView = circleProgressView;
	}

}
