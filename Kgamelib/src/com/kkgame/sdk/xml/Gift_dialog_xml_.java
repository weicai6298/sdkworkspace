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

public class Gift_dialog_xml_ extends Basexml implements Layoutxml {

	

	private LinearLayout baseLinearLayout;
	private TextView tv_mDescription;
	private TextView tv_mTime;
	private TextView tv_mHowto;

	public Gift_dialog_xml_(Activity activity) {
		super(activity);

	}

	@Override
	public View initViewxml() {

		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				720, 650);
		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setLayoutParams(layoutParams);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		LinearLayout ll_content1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content1, 720, 650, mLinearLayout);
		
		ScrollView sv_giftdialog = new ScrollView(mContext);
		machineFactory.MachineView(sv_giftdialog, 720, 650, mLinearLayout);
		
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content,MATCH_PARENT, MATCH_PARENT, mLinearLayout,4,30);
		ll_content.setOrientation(LinearLayout.VERTICAL);
		
		
		TextView tv_text1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text1, MATCH_PARENT, WRAP_CONTENT, 0, "礼包内容:", 27, mLinearLayout, 0, 0, 0, 0);
		tv_text1.setTextColor(Color.BLACK);
		
		tv_mDescription = new TextView(mContext);
		machineFactory.MachineTextView(tv_mDescription, MATCH_PARENT, WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);
		tv_mDescription.setTextColor(Color.GRAY);
		
		TextView tv_text2 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text2, MATCH_PARENT, WRAP_CONTENT, 0, "使用期限:", 27, mLinearLayout, 0, 0, 0, 0);
		tv_text2.setTextColor(Color.BLACK);
		
		tv_mTime = new TextView(mContext);
		machineFactory.MachineTextView(tv_mDescription, MATCH_PARENT, WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);
		tv_mTime.setTextColor(Color.RED);
		
		TextView tv_text3 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text3, MATCH_PARENT, WRAP_CONTENT, 0, "使用说明:", 27, mLinearLayout, 0, 0, 0, 0);
		tv_text3.setTextColor(Color.BLACK);
		
		tv_mHowto = new TextView(mContext);
		machineFactory.MachineTextView(tv_mHowto, MATCH_PARENT, WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);
		
		ll_content.addView(tv_text1);
		ll_content.addView(tv_mDescription);
		ll_content.addView(tv_text2);
		ll_content.addView(tv_mTime);
		ll_content.addView(tv_text3);
		ll_content.addView(tv_mHowto);
		
		sv_giftdialog.addView(ll_content);
		
		ll_content1.addView(sv_giftdialog);
		baseLinearLayout.addView(ll_content1);
		
		
		
		return baseLinearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public TextView getTv_mDescription() {
		return tv_mDescription;
	}

	public void setTv_mDescription(TextView tv_mDescription) {
		this.tv_mDescription = tv_mDescription;
	}

	public TextView getTv_mTime() {
		return tv_mTime;
	}

	public void setTv_mTime(TextView tv_mTime) {
		this.tv_mTime = tv_mTime;
	}

	public TextView getTv_mHowto() {
		return tv_mHowto;
	}

	public void setTv_mHowto(TextView tv_mHowto) {
		this.tv_mHowto = tv_mHowto;
	}

	
}
