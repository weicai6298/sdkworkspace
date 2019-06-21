package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class Bluep_paymentxml extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private WebView wv_mWebview;
	private ImageView iv_back;
	private ProgressBar pb_loading;

	public Bluep_paymentxml(Activity activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {
		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setLayoutParams(layoutParams);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		// 支付第二次支付确定页面
		RelativeLayout rl_bluepaycontent = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_bluepaycontent, MATCH_PARENT,
				MATCH_PARENT, mLinearLayout);

		wv_mWebview = new WebView(mContext);
		machineFactory.MachineView(wv_mWebview, MATCH_PARENT, MATCH_PARENT,
				mRelativeLayout);

		iv_back = new ImageView(mContext);
		machineFactory.MachineView(iv_back, 90,90 , 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_LEFT);
		iv_back.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_cancel_icon.png", mActivity));

		pb_loading = new ProgressBar(mContext);
		machineFactory.MachineView(pb_loading, WRAP_CONTENT, WRAP_CONTENT, 0,
				mRelativeLayout, 0, 10, 10, 0, RelativeLayout.CENTER_IN_PARENT);
		
		rl_bluepaycontent.addView(wv_mWebview);
		rl_bluepaycontent.addView(iv_back);
		rl_bluepaycontent.addView(pb_loading);
		
		baseLinearLayout.addView(rl_bluepaycontent);

		return baseLinearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public WebView getWv_mWebview() {
		return wv_mWebview;
	}

	public void setWv_mWebview(WebView wv_mWebview) {
		this.wv_mWebview = wv_mWebview;
	}

	public ImageView getIv_back() {
		return iv_back;
	}

	public void setIv_back(ImageView iv_back) {
		this.iv_back = iv_back;
	}

	public ProgressBar getPb_loading() {
		return pb_loading;
	}

	public void setPb_loading(ProgressBar pb_loading) {
		this.pb_loading = pb_loading;
	}
	
	

}
