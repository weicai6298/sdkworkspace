package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Recomendationview_xml_po extends Basexml implements Layoutxml {



	private LinearLayout baseLinearLayout;
	private WebView wv_mWebview;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;

	public Recomendationview_xml_po(Activity activity) {
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
		baseLinearLayout.setGravity(Gravity.CENTER);

		wv_mWebview = new WebView(mActivity);
		machineFactory.MachineView(wv_mWebview, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		

		pb_mLoading = new ProgressBar(mContext);
		machineFactory.MachineView(pb_mLoading, 50, 50, 0, mLinearLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_IN_PARENT);

		bt_mReload = new Button(mContext);
		machineFactory.MachineButton(bt_mReload, 350, 96, 0, "连接失败,点击重新连接", 28,
				mLinearLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mReload.setTextColor(Color.WHITE);

		baseLinearLayout.addView(wv_mWebview);
		baseLinearLayout.addView(pb_mLoading);
		baseLinearLayout.addView(bt_mReload);

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

	public ProgressBar getPb_mLoading() {
		return pb_mLoading;
	}

	public void setPb_mLoading(ProgressBar pb_mLoading) {
		this.pb_mLoading = pb_mLoading;
	}

	public Button getBt_mReload() {
		return bt_mReload;
	}

	public void setBt_mReload(Button bt_mReload) {
		this.bt_mReload = bt_mReload;
	}

	

	
	
}
