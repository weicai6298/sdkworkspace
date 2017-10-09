package com.kkgame.sdk.xml;



import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Toastxml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageView iv_imageview;
	private TextView tv_message;

	public Toastxml_po(Activity activity) {
		super(activity);

	}

	@Override
	public View initViewxml() {

		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		baseLinearLayout.setBackgroundColor(Color.TRANSPARENT);
		baseLinearLayout.setLayoutParams(layoutParams);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		// 主要内容
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		rl_title.setBackgroundColor(Color.TRANSPARENT);

		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 250, 250, 0, mRelativeLayout, 0,
				0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		ll_content.setOrientation(LinearLayout.VERTICAL);
		ll_content.setGravity(Gravity_CENTER);
		ll_content.setBackgroundDrawable(GetAssetsutils
				.getDrawableFromAssetsFile("yaya_toast_background.png", mActivity));

		iv_imageview = new ImageView(mContext);
		machineFactory.MachineView(iv_imageview, 80, 80, mLinearLayout);

		tv_message = new TextView(mContext);
		machineFactory.MachineTextView(tv_message, MATCH_PARENT, WRAP_CONTENT,
				0, "", 28, mLinearLayout, 0, 25, 0, 0);
		tv_message.setGravity(Gravity_CENTER);

		ll_content.addView(iv_imageview);
		ll_content.addView(tv_message);

		rl_title.addView(ll_content);
		baseLinearLayout.addView(rl_title);

		return baseLinearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public ImageView getIv_imageview() {
		return iv_imageview;
	}

	public void setIv_imageview(ImageView iv_imageview) {
		this.iv_imageview = iv_imageview;
	}

	public TextView getTv_message() {
		return tv_message;
	}

	public void setTv_message(TextView tv_message) {
		this.tv_message = tv_message;
	}

}
