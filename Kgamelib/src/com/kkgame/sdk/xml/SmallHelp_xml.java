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

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.utils.DeviceUtil;

public class SmallHelp_xml extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private WebView wv_mWeiboview;
	

	private LinearLayout ll_mPre;
	private TextView tv_zhuce;
	private RelativeLayout rl_mLoading;
	private ProgressBar pb_mLoading;
	private Button bt_mReload;
	private LinearLayout baselin;

	public SmallHelp_xml(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
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
		baseLinearLayout.setGravity(Gravity.CENTER);

		
		int height = 560;
		int with = 630;
		
		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height,
				mLinearLayout);
		baselin.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		baselin.setGravity(Gravity.CENTER);
		wv_mWeiboview = new WebView(mContext);
//		if (DeviceUtil.isLandscape(mContext)) {
			machineFactory.MachineView(wv_mWeiboview, with, height,
					mLinearLayout);
//		}else {
//			baseLinearLayout.setGravity(Gravity.CENTER);
//			machineFactory.MachineView(wv_mWeiboview, ViewConstants.getHoldActivityWith(mActivity), (ViewConstants.getHoldActivityHeight(mActivity)/4)*3+50,
//					mLinearLayout);
//		}
		//链接状态布局
				rl_mLoading = new RelativeLayout(mContext);
				rl_mLoading.setBackgroundColor(Color.WHITE);
				machineFactory.MachineView(rl_mLoading, WRAP_CONTENT, WRAP_CONTENT,
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
				
				bt_mReload.setVisibility(View.GONE);
				//TODO
				rl_mLoading.addView(pb_mLoading);
				rl_mLoading.addView(bt_mReload);

				
		/*baseLinearLayout.addView(rl_mLoading);
		baseLinearLayout.addView(wv_mWeiboview);*/
		baselin.addView(rl_mLoading);
		baselin.addView(wv_mWeiboview);
		baseLinearLayout.addView(baselin);
		return baseLinearLayout;
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
