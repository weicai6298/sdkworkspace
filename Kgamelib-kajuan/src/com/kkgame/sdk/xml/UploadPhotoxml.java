package com.kkgame.sdk.xml;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UploadPhotoxml extends Basexml implements Layoutxml {

	private LinearLayout baselin;
	private LinearLayout ll_mChangehead;
	private TextView tv_fromcamera;
	private TextView tv_fromalbum;
	private TextView tv_cancel;

	public UploadPhotoxml(Activity activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 750, 650, "LinearLayout");
		// baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);
		//ll_content.setGravity(Gravity.BOTTOM);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 750, 650, "LinearLayout");
		// ll_content.setBackgroundColor(Color.WHITE);
		 ll_content.setGravity(Gravity.BOTTOM);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		ll_mChangehead = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mChangehead, MATCH_PARENT, WRAP_CONTENT,
				0, mLinearLayout, 0, 0, 0, 0, 100);
		ll_mChangehead.setOrientation(LinearLayout.VERTICAL);
		 ll_mChangehead.setBackgroundColor(Color.WHITE);

		tv_fromcamera = new TextView(mContext);
		machineFactory.MachineTextView(tv_fromcamera, MATCH_PARENT, 70, 0,
				"拍照", 26, mLinearLayout, 0, 0, 0, 0);
		tv_fromcamera.setTextColor(Color.parseColor("#666666"));

		tv_fromcamera.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei5 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei5, MATCH_PARENT, 2, mLinearLayout);
		ll_zhanwei5.setBackgroundColor(Color.parseColor("#e1e1e1"));

		tv_fromalbum = new TextView(mContext);
		machineFactory.MachineTextView(tv_fromalbum, MATCH_PARENT, 70, 0,
				"手机相册上传", 26, mLinearLayout, 0, 0, 0, 0);
		tv_fromalbum.setTextColor(Color.parseColor("#666666"));
		tv_fromalbum.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei6 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei6, MATCH_PARENT, 2, mLinearLayout);
		ll_zhanwei6.setBackgroundColor(Color.parseColor("#e1e1e1"));

		tv_cancel = new TextView(mContext);
		machineFactory.MachineTextView(tv_cancel, MATCH_PARENT, 70, 0, "取消",
				26, mLinearLayout, 0, 0, 0, 0);
		tv_cancel.setTextColor(Color.parseColor("#666666"));
		tv_cancel.setGravity(Gravity_CENTER);
		tv_cancel.setClickable(true);

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_mChangehead.setVisibility(View.GONE);
			}
		});

		ll_mChangehead.addView(tv_fromcamera);
		ll_mChangehead.addView(ll_zhanwei5);
		ll_mChangehead.addView(tv_fromalbum);
		ll_mChangehead.addView(ll_zhanwei6);
		ll_mChangehead.addView(tv_cancel);
		// ll_mChangehead.setVisibility(View.GONE);

		ll_content.addView(ll_mChangehead);

		baselin.addView(ll_content);

		return baselin;
	}

}
