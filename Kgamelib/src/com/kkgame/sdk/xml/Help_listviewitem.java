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

public class Help_listviewitem extends Basexml implements Layoutxml {

	private TextView textView;
	private ImageView imageView;
	private TextView tv_mQuestion;
	private TextView tv_mAs;


	public Help_listviewitem(Activity activity) {
		super(activity);
	}
	
	public Help_listviewitem(Context activity) {
		super(activity);
	}
	

	@Override
	public View initViewxml() {
		LinearLayout linearLayout = new LinearLayout(mContext);
		new android.widget.AbsListView.LayoutParams(MATCH_PARENT, MATCH_PARENT);
		/*linearLayout=(LinearLayout) machineFactory.MachineView(linearLayout, MATCH_PARENT, MATCH_PARENT,0,
				mRelativeLayout);*/
		linearLayout.setBackgroundColor(Color.WHITE);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		tv_mQuestion = new TextView(mContext);
		tv_mQuestion=machineFactory.MachineTextView(tv_mQuestion, MATCH_PARENT, WRAP_CONTENT, 0, "xxxxx", 28, mLinearLayout, 0, 15, 0, 0);
		tv_mQuestion.setGravity(Gravity.CENTER_VERTICAL);
		
		tv_mAs = new TextView(mContext);
		tv_mAs=machineFactory.MachineTextView(tv_mAs, MATCH_PARENT, WRAP_CONTENT, 0, "xxxxx", 28, mLinearLayout, 0, 15, 0, 0);
		tv_mAs.setGravity(Gravity.CENTER_VERTICAL);
		
		
		linearLayout.addView(tv_mQuestion);
		linearLayout.addView(tv_mAs);
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

	public TextView getTv_mQuestion() {
		return tv_mQuestion;
	}

	public void setTv_mQuestion(TextView tv_mQuestion) {
		this.tv_mQuestion = tv_mQuestion;
	}

	public TextView getTv_mAs() {
		return tv_mAs;
	}

	public void setTv_mAs(TextView tv_mAs) {
		this.tv_mAs = tv_mAs;
	}

	
	

}
