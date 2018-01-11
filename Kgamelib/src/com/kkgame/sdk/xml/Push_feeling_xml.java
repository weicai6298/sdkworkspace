package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Push_feeling_xml extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private Button bt_mOk;
	private EditText et_mFeel;
	private LinearLayout ll_mPicture1;
	private ImageView bt_mAadpic;
	private LinearLayout ll_mPicture2;

	public Push_feeling_xml(Activity activity) {
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
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_paymenttitle.9.png", mContext));

		rl_title.setGravity(Gravity.CENTER_VERTICAL);

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"编辑", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// 发表按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, 120, MATCH_PARENT, 0, "发表", 34,
				mRelativeLayout, 0, 15, 20, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		bt_mOk.setGravity(Gravity_CENTER);
		bt_mOk.setTextColor(Color.WHITE);

		bt_mOk.setBackgroundDrawable(null);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);
		rl_title.addView(bt_mOk);

		// 中间内容层
		LinearLayout ll_content = new LinearLayout(mContext);
		ll_content = (LinearLayout) machineFactory.MachineView(ll_content, 660,
				MATCH_PARENT, 0, mLinearLayout, 0, 30, 0, 0,
				LinearLayout.VERTICAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		et_mFeel = new EditText(mContext);
		machineFactory.MachineEditText(et_mFeel, MATCH_PARENT, 240, 0,
				"说些什么吧~", 26, mLinearLayout, 0, 0, 0, 0);
		et_mFeel.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang.9.png", mActivity));
		et_mFeel.setGravity(Gravity.START);
		et_mFeel.setPadding(5, 5, 5, 5);
		et_mFeel.setTextColor(Color.BLACK);

		ll_mPicture1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPicture1, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);

		ll_mPicture2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPicture2, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);

		// TODO
		ll_mPicture2.addView(ll_mPicture1);
		
		bt_mAadpic = getImageview();
		bt_mAadpic.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_tianjiapic.png", mActivity));
		ll_mPicture2.addView(bt_mAadpic);

		
		ll_content.addView(et_mFeel);
		// ll_content.addView(ll_mPicture1);
		ll_content.addView(ll_mPicture2);
		// ll_content.addView(bt_mAadpic);

		baseLinearLayout.addView(rl_title);

		baseLinearLayout.addView(ll_content);

		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.finish();

			}
		});

		return baseLinearLayout;
	}

	/**
	 * 得到一个有属性的imageview控件
	 * 
	 * @return
	 */
	public ImageView getImageview() {
		ImageView iv_imageview = new ImageView(mContext);
		machineFactory.MachineView(iv_imageview, 100, 100, 0, mLinearLayout,
				20, 10, 0, 0, 100);
		return iv_imageview;

	}

	public Button getBt_mOk() {
		return bt_mOk;
	}

	public void setBt_mOk(Button bt_mOk) {
		this.bt_mOk = bt_mOk;
	}

	public EditText getEt_mFeel() {
		return et_mFeel;
	}

	public void setEt_mFeel(EditText et_mFeel) {
		this.et_mFeel = et_mFeel;
	}

	public LinearLayout getLl_mPicture1() {
		return ll_mPicture1;
	}

	public void setLl_mPicture1(LinearLayout ll_mPicture1) {
		this.ll_mPicture1 = ll_mPicture1;
	}

	
	public LinearLayout getLl_mPicture2() {
		return ll_mPicture2;
	}

	public void setLl_mPicture2(LinearLayout ll_mPicture2) {
		this.ll_mPicture2 = ll_mPicture2;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

	public ImageButton getIv_mPre() {
		return iv_mPre;
	}

	public void setIv_mPre(ImageButton iv_mPre) {
		this.iv_mPre = iv_mPre;
	}

	public ImageView getBt_mAadpic() {
		return bt_mAadpic;
	}

	public void setBt_mAadpic(ImageView bt_mAadpic) {
		this.bt_mAadpic = bt_mAadpic;
	}
	

}
