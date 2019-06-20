package com.kkgame.sdk.pay;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;

public class Showgift_dialog extends Basedialogview {

	private LinearLayout ll_mBut;
	
	private TextView tv_mDescription;
	private TextView tv_mTime;
	private TextView tv_mHowto;
	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;

	private Button bt_mCancel;

	private Button bt_mOk;

	public Showgift_dialog(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 650;

		int height = 0;
		int with = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
			with = po_with;
		}

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, mLinearLayout,2,25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mActivity);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mActivity);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"礼包领取", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		ScrollView sv_giftdialog = new ScrollView(mContext);
		machineFactory.MachineView(sv_giftdialog, with, height - 230,
				mLinearLayout);

		LinearLayout ll_content1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content1, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout, 4, 30);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		TextView tv_text1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text1, MATCH_PARENT, WRAP_CONTENT, 0,
				"  礼包内容:", 27, mLinearLayout, 0, 10, 0, 0);
		tv_text1.setTextColor(Color.BLACK);

		tv_mDescription = new TextView(mContext);
		machineFactory.MachineTextView(tv_mDescription, MATCH_PARENT,
				WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);
		tv_mDescription.setTextColor(Color.GRAY);

		TextView tv_text2 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text2, MATCH_PARENT, WRAP_CONTENT, 0,
				"  使用期限:", 27, mLinearLayout, 0, 0, 0, 0);
		tv_text2.setTextColor(Color.BLACK);

		tv_mTime = new TextView(mContext);
		machineFactory.MachineTextView(tv_mDescription, MATCH_PARENT,
				WRAP_CONTENT, 0, "", 27, mLinearLayout, 0, 0, 0, 0);
		tv_mTime.setTextColor(Color.RED);

		TextView tv_text3 = new TextView(mContext);
		machineFactory.MachineTextView(tv_text3, MATCH_PARENT, WRAP_CONTENT, 0,
				"  使用说明:", 27, mLinearLayout, 0, 0, 0, 0);
		tv_text3.setTextColor(Color.BLACK);

		tv_mHowto = new TextView(mContext);
		machineFactory.MachineTextView(tv_mHowto, MATCH_PARENT, WRAP_CONTENT,
				0, "", 27, mLinearLayout, 0, 0, 0, 0);

		ll_content1.addView(tv_text1);
		ll_content1.addView(tv_mDescription);
		ll_content1.addView(tv_text2);
		ll_content1.addView(tv_mTime);
		ll_content1.addView(tv_text3);
		ll_content1.addView(tv_mHowto);

		sv_giftdialog.addView(ll_content1);

		ll_mBut = new LinearLayout(mActivity);
		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
				MATCH_PARENT, 100, 0, mLinearLayout, 20, 10, 20, 0, 100);

		// 取消按钮
		bt_mCancel = new Button(mActivity);
		bt_mCancel = machineFactory.MachineButton(bt_mCancel, 0,
				MATCH_PARENT, 1, "取消", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mCancel.setTextColor(Color.WHITE);
		bt_mCancel.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mCancel.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 20,
				MATCH_PARENT, mLinearLayout);

		// 领取按钮
		bt_mOk = new Button(mActivity);
		bt_mOk = machineFactory.MachineButton(bt_mOk, 0, MATCH_PARENT, 1,
				"领取", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_mBut.addView(bt_mCancel);
		ll_mBut.addView(ll_zhanwei);
		ll_mBut.addView(bt_mOk);

		ll_content.addView(rl_title);

		ll_content.addView(sv_giftdialog);

		ll_content.addView(ll_mBut);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

	}

	public LinearLayout getLl_mBut() {
		return ll_mBut;
	}

	public void setLl_mBut(LinearLayout ll_mBut) {
		this.ll_mBut = ll_mBut;
	}



	public TextView getTv_mDescription() {
		return tv_mDescription;
	}

	public void setTv_mDescription(TextView tv_mDescription) {
		this.tv_mDescription = tv_mDescription;
	}

	public TextView getTv_mTime() {
		return tv_mTime;
	}

	public void setTv_mTime(TextView tv_mTime) {
		this.tv_mTime = tv_mTime;
	}

	public TextView getTv_mHowto() {
		return tv_mHowto;
	}

	public void setTv_mHowto(TextView tv_mHowto) {
		this.tv_mHowto = tv_mHowto;
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

	public Button getBt_mCancel() {
		return bt_mCancel;
	}

	public void setBt_mCancel(Button bt_mCancel) {
		this.bt_mCancel = bt_mCancel;
	}

	public Button getBt_mOk() {
		return bt_mOk;
	}

	public void setBt_mOk(Button bt_mOk) {
		this.bt_mOk = bt_mOk;
	}
	
	

}
