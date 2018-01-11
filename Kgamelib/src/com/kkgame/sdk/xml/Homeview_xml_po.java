package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Homeview_xml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ListView lv_Homelist;
	private EditText et_mFabiao;
	private Button bt_mFabiao;
	private ImageView iv_xiaobai;
	private ProgressBar pb_mLoading;

	public Homeview_xml_po(Activity activity) {
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

		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, MATCH_PARENT, MATCH_PARENT, mLinearLayout);
		
		
		lv_Homelist = new ListView(mActivity);

		machineFactory.MachineView(lv_Homelist, MATCH_PARENT, MATCH_PARENT,0,
				mRelativeLayout,15,0,15,100,100);
		lv_Homelist.setDivider(null);
		

		pb_mLoading = new ProgressBar(mContext);
		machineFactory.MachineView(pb_mLoading, 50, 50, 0, mRelativeLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_IN_PARENT);
		/*
		bt_mReload = new Button(mContext);
		machineFactory.MachineButton(bt_mReload, 350, 96, 0, "连接失败,点击重新连接", 28,
				mLinearLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mContext));
		bt_mReload.setTextColor(Color.WHITE);*/

		LinearLayout ll_mFabiao = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mFabiao, MATCH_PARENT, 100, 0, mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_BOTTOM);
		ll_mFabiao.setGravity(Gravity.CENTER_VERTICAL);
		ll_mFabiao.setBackgroundColor(Color.parseColor("#f1f1f1"));
		
		iv_xiaobai = new ImageView(mActivity);
		machineFactory.MachineView(iv_xiaobai, 60, 60, mLinearLayout,1,20);
		iv_xiaobai.setImageBitmap(GetAssetsutils.getImageFromAssetsFile("yaya_zhaopian.png", mActivity));
		
		
		et_mFabiao = new EditText(mActivity);
		machineFactory.MachineEditText(et_mFabiao, 0, 70, 1, "说点什么吧~", 26, mLinearLayout, 20, 0, 0, 0);
		et_mFabiao.setBackgroundColor(Color.WHITE);
		et_mFabiao.setGravity(Gravity.CENTER_VERTICAL);
		et_mFabiao.setPadding(2, 2, 0, 0);
		
		bt_mFabiao = new Button(mContext);
		machineFactory.MachineButton(bt_mFabiao, 110, 80, 0, "发送", 28, mLinearLayout, 0, 0, 0, 0);
		bt_mFabiao.setTextColor(Color.parseColor("#1888d7"));
		bt_mFabiao.setBackgroundColor(Color.parseColor("#f1f1f1"));
		bt_mFabiao.setGravity(Gravity_CENTER);
		bt_mFabiao.setPadding(0, 2, 0, 0);
		
		//TODO
		ll_mFabiao.addView(iv_xiaobai);
		ll_mFabiao.addView(et_mFabiao);
		ll_mFabiao.addView(bt_mFabiao);
		
		rl_content.addView(pb_mLoading);
		rl_content.addView(lv_Homelist);
		rl_content.addView(ll_mFabiao);
		
		baseLinearLayout.addView(rl_content);

		return baseLinearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public ListView getLv_Homelist() {
		return lv_Homelist;
	}

	public void setLv_Homelist(ListView lv_Homelist) {
		this.lv_Homelist = lv_Homelist;
	}

	public EditText getEt_mFabiao() {
		return et_mFabiao;
	}

	public void setEt_mFabiao(EditText et_mFabiao) {
		this.et_mFabiao = et_mFabiao;
	}

	public Button getBt_mFabiao() {
		return bt_mFabiao;
	}

	public void setBt_mFabiao(Button bt_mFabiao) {
		this.bt_mFabiao = bt_mFabiao;
	}

	public ImageView getIv_xiaobai() {
		return iv_xiaobai;
	}

	public void setIv_xiaobai(ImageView iv_xiaobai) {
		this.iv_xiaobai = iv_xiaobai;
	}

	public ProgressBar getPb_mLoading() {
		return pb_mLoading;
	}

	public void setPb_mLoading(ProgressBar pb_mLoading) {
		this.pb_mLoading = pb_mLoading;
	}

	
	

	
	
}
