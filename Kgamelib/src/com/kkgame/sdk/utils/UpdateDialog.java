package com.kkgame.sdk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.kkgame.sdk.xml.Update_xml;



public class UpdateDialog extends Dialog {
	private Context mContext;

	private LayoutInflater inflater;

	private LayoutParams lp;

	private TextView mMessage;

	private Button mSubmit;

	private Button mCancel;

	private Activity mActivity;

	public UpdateDialog(Context context) {
		// super(context, ResourceUtil.getStyleId(context,
		// "CustomProgressDialog"));
		super(context);
		this.mContext = context;
		mActivity = (Activity) context;
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Update_xml update_xml = new Update_xml(mActivity);
		View view = update_xml.initViewxml();

		mMessage = update_xml.getTv_message();
		mSubmit = update_xml.getBt_mok();
		mCancel = update_xml.getBt_mCancel();

		setContentView(view);

		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = (float) 0.5; // 设置背景遮盖
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);

	}

	public void setMessage(String message) {
		mMessage.setText(message);
	}

	public void setSubmit(String name,
			android.view.View.OnClickListener listener) {
		mSubmit.setText(name);
		mSubmit.setOnClickListener(listener);
	}

	public void setCancle(String name,
			android.view.View.OnClickListener listener) {
		mCancel.setText(name);
		mCancel.setOnClickListener(listener);
	}

}
