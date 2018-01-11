package com.kkgame.sdk.xml;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Loginpo_listviewitem extends Basexml implements Layoutxml {

	private TextView textView;
	private ImageView imageView;
	private LinearLayout linearLayout;


	public Loginpo_listviewitem(Activity activity) {
		super(activity);
	}
	
	public Loginpo_listviewitem(Context activity) {
		super(activity);
	}
	

	@Override
	public View initViewxml() {
		linearLayout = new LinearLayout(mActivity);
		new android.widget.AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		/*linearLayout=(LinearLayout) machineFactory.MachineView(linearLayout, MATCH_PARENT, MATCH_PARENT,0,
				mRelativeLayout);*/
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		
		textView = new TextView(mContext);
		textView=machineFactory.MachineTextView(textView, 0, 100, 1, "xxxxx", 28, mLinearLayout, 60, 0, 0, 0);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		
		imageView = new ImageView(mContext);
		imageView=(ImageView) machineFactory.MachineView(imageView, 40,
				40, 0, mLinearLayout, 0, 0, 20, 0, 100);
		
		linearLayout.addView(textView);
		linearLayout.addView(imageView);
		return linearLayout;
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public LinearLayout getLinearLayout() {
		return linearLayout;
	}

	public void setLinearLayout(LinearLayout linearLayout) {
		this.linearLayout = linearLayout;
	}
	
	

}
