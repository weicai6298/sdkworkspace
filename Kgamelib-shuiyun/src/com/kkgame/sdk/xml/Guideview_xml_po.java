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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Guideview_xml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private WebView wv_mWeiboview;
	private RelativeLayout rl_mLoading;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private LinearLayout ll_mPre;
	private TextView tv_zhuce;
	private LinearLayout ll_activity;
	private LinearLayout ll_strategy;
	private LinearLayout ll_content2;
	private LinearLayout ll_gamedata;
	private LinearLayout ll_discuz;

	public Guideview_xml_po(Activity activity) {
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
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		LinearLayout ll_content1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content1, MATCH_PARENT, 96,
				mLinearLayout, 2, 20);

		ll_activity = new LinearLayout(mContext);
		machineFactory.MachineView(ll_activity, 0, 96, 1, mLinearLayout, 20, 0,
				0, 0, 100);
		ll_activity.setGravity(Gravity.CENTER_VERTICAL);
		ll_activity.setBackgroundColor(Color.parseColor("#f1f1f1"));

		ImageView iv_activity = new ImageView(mContext);
		machineFactory.MachineView(iv_activity, 32, 32, mLinearLayout, 1, 20);
		iv_activity.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_activity.png", mActivity));

		TextView tv_activity = new TextView(mContext);
		machineFactory.MachineTextView(tv_activity, WRAP_CONTENT, WRAP_CONTENT,
				0, "活动", 32, mLinearLayout, 20, 0, 0, 0);
		tv_activity.setTextColor(Color.parseColor("#333333"));

		// TODO
		ll_activity.addView(iv_activity);
		ll_activity.addView(tv_activity);

		LinearLayout ll_zhanwei = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei, 50, MATCH_PARENT, 0,
				mLinearLayout);

		ll_strategy = new LinearLayout(mContext);
		machineFactory.MachineView(ll_strategy, 0, 96, 1, mLinearLayout, 0, 0,
				20, 0, 100);
		ll_strategy.setGravity(Gravity.CENTER_VERTICAL);
		ll_strategy.setBackgroundColor(Color.parseColor("#f1f1f1"));

		ImageView iv_strategy = new ImageView(mContext);
		machineFactory.MachineView(iv_strategy, 32, 32, mLinearLayout, 1, 20);
		iv_strategy.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_strategy.png", mActivity));

		TextView tv_strategy = new TextView(mContext);
		machineFactory.MachineTextView(tv_strategy, WRAP_CONTENT, WRAP_CONTENT,
				0, "游戏攻略", 32, mLinearLayout, 20, 0, 20, 0);
		tv_strategy.setTextColor(Color.parseColor("#333333"));

		// TODO
		ll_strategy.addView(iv_strategy);
		ll_strategy.addView(tv_strategy);

		// TODO
		ll_content1.addView(ll_activity);
		ll_content1.addView(ll_zhanwei);
		ll_content1.addView(ll_strategy);

		ll_content2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content2, MATCH_PARENT, 96,
				mLinearLayout, 2, 20);

		ll_gamedata = new LinearLayout(mContext);
		machineFactory.MachineView(ll_gamedata, 0, 96, 1, mLinearLayout, 20, 0,
				0, 0, 100);
		ll_gamedata.setGravity(Gravity.CENTER_VERTICAL);
		ll_gamedata.setBackgroundColor(Color.parseColor("#f1f1f1"));

		ImageView iv_gamedata = new ImageView(mContext);
		machineFactory.MachineView(iv_gamedata, 32, 32, mLinearLayout, 1, 20);
		iv_gamedata.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_gamedata.png", mActivity));

		TextView tv_gamedata = new TextView(mContext);
		machineFactory.MachineTextView(tv_gamedata, WRAP_CONTENT, WRAP_CONTENT,
				0, "游戏资料", 32, mLinearLayout, 20, 0, 0, 0);
		tv_gamedata.setTextColor(Color.parseColor("#333333"));

		LinearLayout ll_zhanwei2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei2, 50, MATCH_PARENT, 0,
				mLinearLayout);

		// TODO
		ll_gamedata.addView(iv_gamedata);
		ll_gamedata.addView(tv_gamedata);

		ll_discuz = new LinearLayout(mContext);
		machineFactory.MachineView(ll_discuz, 0, 96, 1, mLinearLayout, 0, 0,
				20, 0, 100);
		ll_discuz.setGravity(Gravity.CENTER_VERTICAL);
		ll_discuz.setBackgroundColor(Color.parseColor("#f1f1f1"));

		ImageView iv_discuz = new ImageView(mContext);
		machineFactory.MachineView(iv_discuz, 32, 32, mLinearLayout, 1, 20);
		iv_discuz.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_strategy.png", mActivity));

		TextView tv_discuz = new TextView(mContext);
		machineFactory.MachineTextView(tv_discuz, WRAP_CONTENT, WRAP_CONTENT,
				0, "游戏论坛", 32, mLinearLayout, 20, 0, 20, 0);
		tv_discuz.setTextColor(Color.parseColor("#333333"));

		// TODO
		ll_discuz.addView(iv_discuz);
		ll_discuz.addView(tv_discuz);

		// 占位三
		LinearLayout ll_zhanwei3 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei3, 0, 96, 1, mLinearLayout, 0, 0,
				20, 0, 100);

		// TODO
		ll_content2.addView(ll_gamedata);
		ll_content2.addView(ll_zhanwei2);
		ll_content2.addView(ll_discuz);

		baseLinearLayout.addView(ll_content1);
		baseLinearLayout.addView(ll_content2);
		return baseLinearLayout;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public ImageButton getIv_mPre() {
		return iv_mPre;
	}

	public void setIv_mPre(ImageButton iv_mPre) {
		this.iv_mPre = iv_mPre;
	}

	public LinearLayout getLl_discuz() {
		return ll_discuz;
	}

	public void setLl_discuz(LinearLayout ll_discuz) {
		this.ll_discuz = ll_discuz;
	}

	public WebView getWv_mWeiboview() {
		return wv_mWeiboview;
	}

	public void setWv_mWeiboview(WebView wv_mWeiboview) {
		this.wv_mWeiboview = wv_mWeiboview;
	}

	public RelativeLayout getRl_mLoading() {
		return rl_mLoading;
	}

	public void setRl_mLoading(RelativeLayout rl_mLoading) {
		this.rl_mLoading = rl_mLoading;
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

	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

	public TextView getTv_zhuce() {
		return tv_zhuce;
	}

	public void setTv_zhuce(TextView tv_zhuce) {
		this.tv_zhuce = tv_zhuce;
	}

	public LinearLayout getLl_activity() {
		return ll_activity;
	}

	public void setLl_activity(LinearLayout ll_activity) {
		this.ll_activity = ll_activity;
	}

	public LinearLayout getLl_strategy() {
		return ll_strategy;
	}

	public void setLl_strategy(LinearLayout ll_strategy) {
		this.ll_strategy = ll_strategy;
	}

	public LinearLayout getLl_content2() {
		return ll_content2;
	}

	public void setLl_content2(LinearLayout ll_content2) {
		this.ll_content2 = ll_content2;
	}

	public LinearLayout getLl_gamedata() {
		return ll_gamedata;
	}

	public void setLl_gamedata(LinearLayout ll_gamedata) {
		this.ll_gamedata = ll_gamedata;
	}

}
