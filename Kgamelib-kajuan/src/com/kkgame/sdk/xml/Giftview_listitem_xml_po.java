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
import android.widget.TextView;

public class Giftview_listitem_xml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private TextView tv_gift1;
	private TextView tv_gift2;
	private TextView tv_gift3;

	public Giftview_listitem_xml_po(Activity activity) {
		super(activity);

	}

	@Override
	public View initViewxml() {

		// 基类布局
		LinearLayout linearLayout = new LinearLayout(mContext);
		new android.widget.AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		/*
		 * linearLayout=(LinearLayout) machineFactory.MachineView(linearLayout,
		 * MATCH_PARENT, MATCH_PARENT,0, mRelativeLayout);
		 */
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);

		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, MATCH_PARENT, 80, 0,
				mLinearLayout, 20, 0, 20, 0, 100);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, MATCH_PARENT, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, 100);

		tv_gift1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_gift1, WRAP_CONTENT, 80, 0,
				"", 26, mLinearLayout, 0, 0, 0, 0,100);
		tv_gift1.setGravity(Gravity_CENTER);
		tv_gift1.setTextColor(Color.parseColor("#ec7600"));

		tv_gift2 = new TextView(mContext);
		machineFactory.MachineTextView(tv_gift2, WRAP_CONTENT, 80, 0, "", 20,
				mLinearLayout, 0, 0, 0, 0,100);
		tv_gift2.setGravity(Gravity_CENTER);
		tv_gift2.setTextColor(Color.parseColor("#999999"));

		ll_content.addView(tv_gift1);
		ll_content.addView(tv_gift2);

		tv_gift3 = new TextView(mContext);
		machineFactory.MachineTextView(tv_gift3, WRAP_CONTENT, 80, 0, "", 20,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_gift3.setGravity(Gravity_CENTER);
		tv_gift3.setTextColor(Color.parseColor("#999999"));

		rl_content.addView(ll_content);
		rl_content.addView(tv_gift3);
		linearLayout.addView(rl_content);

		return linearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public TextView getTv_gift1() {
		return tv_gift1;
	}

	public void setTv_gift1(TextView tv_gift1) {
		this.tv_gift1 = tv_gift1;
	}

	public TextView getTv_gift2() {
		return tv_gift2;
	}

	public void setTv_gift2(TextView tv_gift2) {
		this.tv_gift2 = tv_gift2;
	}

	public TextView getTv_gift3() {
		return tv_gift3;
	}

	public void setTv_gift3(TextView tv_gift3) {
		this.tv_gift3 = tv_gift3;
	}

}
