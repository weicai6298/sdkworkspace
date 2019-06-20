package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class Giftcdkey_dialog_xml_ extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private TextView tv_gift_gettime;
	private TextView tv_gift_releasetime;
	private EditText tv_gift_cdkey;

	public Giftcdkey_dialog_xml_(Activity activity) {
		super(activity);

	}

	@Override
	public View initViewxml() {

		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setLayoutParams(layoutParams);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		ScrollView sv_giftdialog = new ScrollView(mContext);
		machineFactory.MachineView(sv_giftdialog, 720, 650,
				mLinearLayout);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		TextView tv_text1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text1, MATCH_PARENT, WRAP_CONTENT, 0,
				"礼包领取时间:", 27, mLinearLayout, 0, 0, 0, 0);

		tv_gift_gettime = new TextView(mContext);
		machineFactory.MachineTextView(tv_gift_gettime, MATCH_PARENT,
				WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);

		TextView tv_text2 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text2, MATCH_PARENT, WRAP_CONTENT, 0,
				"礼包过期时间:", 27, mLinearLayout, 0, 0, 0, 0);

		tv_gift_releasetime = new TextView(mContext);
		machineFactory.MachineTextView(tv_gift_releasetime, MATCH_PARENT,
				WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);
		tv_gift_releasetime.setTextColor(Color.RED);

		TextView tv_text3 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text3, MATCH_PARENT, WRAP_CONTENT, 0,
				"礼包激活码:", 27, mLinearLayout, 0, 0, 0, 0);

		tv_gift_cdkey = new EditText(mContext);
		machineFactory.MachineTextView(tv_gift_cdkey, MATCH_PARENT,
				WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);

		ll_content.addView(tv_text1);
		ll_content.addView(tv_gift_gettime);
		ll_content.addView(tv_text2);
		ll_content.addView(tv_gift_releasetime);
		ll_content.addView(tv_text3);
		ll_content.addView(tv_gift_cdkey);

		sv_giftdialog.addView(ll_content);

		baseLinearLayout.addView(sv_giftdialog);

		return baseLinearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public TextView getTv_gift_gettime() {
		return tv_gift_gettime;
	}

	public void setTv_gift_gettime(TextView tv_gift_gettime) {
		this.tv_gift_gettime = tv_gift_gettime;
	}

	public TextView getTv_gift_releasetime() {
		return tv_gift_releasetime;
	}

	public void setTv_gift_releasetime(TextView tv_gift_releasetime) {
		this.tv_gift_releasetime = tv_gift_releasetime;
	}

	public EditText getTv_gift_cdkey() {
		return tv_gift_cdkey;
	}

	public void setTv_gift_cdkey(EditText tv_gift_cdkey) {
		this.tv_gift_cdkey = tv_gift_cdkey;
	}

}
