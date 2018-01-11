package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Weibologinxml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private WebView wv_mWeiboview;
	private RelativeLayout rl_mLoading;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private LinearLayout ll_mPre;
	private TextView tv_zhuce;

	public Weibologinxml_po(Activity activity) {
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

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_paymenttitle.9.png", mContext));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0, mRelativeLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setClickable(true);
		ll_mPre.setGravity(Gravity_CENTER);
		
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0,
				0, 0, RelativeLayout.CENTER_VERTICAL);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		iv_mPre.setClickable(false);
		
		tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"微博登录", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		//链接状态布局
		rl_mLoading = new RelativeLayout(mContext);

		machineFactory.MachineView(rl_mLoading, MATCH_PARENT, WRAP_CONTENT,
				mLinearLayout);

		pb_mLoading = new ProgressBar(mContext);
		machineFactory.MachineView(pb_mLoading, 80, 80, 0, mRelativeLayout, 0,
				0, 0, 0, RelativeLayout.CENTER_IN_PARENT);

		bt_mReload = new Button(mContext);
		machineFactory.MachineButton(bt_mReload, 350, 96, 0, "连接失败,点击重新连接", 28,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_IN_PARENT);
		bt_mReload.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mReload.setTextColor(Color.WHITE);
		
		//TODO
		rl_mLoading.addView(pb_mLoading);
		rl_mLoading.addView(bt_mReload);
		
		
		wv_mWeiboview = new WebView(mContext);
		machineFactory.MachineView(wv_mWeiboview, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		baseLinearLayout.addView(rl_title);
		baseLinearLayout.addView(wv_mWeiboview);
		baseLinearLayout.addView(rl_mLoading);
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
	
	

}
