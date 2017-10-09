package com.kkgame.sdk.xml;


import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Giftview_xml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private ListView lv_giftlist;
	private TextView tv_mNogift;

	public Giftview_xml_po(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
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

		lv_giftlist = new ListView(mActivity);

		machineFactory.MachineView(lv_giftlist, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		pb_mLoading = new ProgressBar(mContext);
		machineFactory.MachineView(pb_mLoading, 50, 50, 0, mLinearLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_IN_PARENT);

		bt_mReload = new Button(mContext);
		machineFactory.MachineButton(bt_mReload, 400, 96, 0, "该游戏暂时无礼包,点击刷新", 24,
				mLinearLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mReload.setTextColor(Color.WHITE);

		
		tv_mNogift = new TextView(mContext);
		machineFactory.MachineTextView(tv_mNogift, MATCH_PARENT, WRAP_CONTENT, 0, "该游戏暂时无礼包", 24, mLinearLayout, 0, 0, 0, 0);
		tv_mNogift.setGravity(Gravity_CENTER);
		tv_mNogift.setTextColor(Color.parseColor("#e78959"));
		tv_mNogift.setVisibility(View.GONE);
		
		baseLinearLayout.addView(lv_giftlist);
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

	public ListView getLv_giftlist() {
		return lv_giftlist;
	}

	public void setLv_giftlist(ListView lv_giftlist) {
		this.lv_giftlist = lv_giftlist;
	}

	public TextView getTv_mNogift() {
		return tv_mNogift;
	}

	public void setTv_mNogift(TextView tv_mNogift) {
		this.tv_mNogift = tv_mNogift;
	}

	
	
}
