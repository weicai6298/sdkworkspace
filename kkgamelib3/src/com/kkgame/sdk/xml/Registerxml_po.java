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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.login.YYprotocol_ho_dialog;

public class Registerxml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre; // 返回
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private ImageButton ib_mAgreedbox;
	private Button bt_mOk;
	private TextView tv_mRegisterclick;
	private LinearLayout ll_mPre;
	private ImageButton ib_mNotAgreedbox;

	public Registerxml_po(Activity activity) {
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
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

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
				"注册", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
		LinearLayout ll_content = new LinearLayout(mContext);
		ll_content = (LinearLayout) machineFactory.MachineView(ll_content, 660,
				MATCH_PARENT, 0, mLinearLayout, 0, 70, 0, 0,
				LinearLayout.VERTICAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mContext);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout);

		// 手机号码输入框
		et_mPhone = new EditText(mContext);
		machineFactory.MachineEditText(et_mPhone, 400, MATCH_PARENT, 0,
				"请输入手机号", 32, mLinearLayout, 0, 0, 0, 0);
		et_mPhone.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang2.9.png", mContext));
		et_mPhone.setPadding(machSize(20), 0, 0, 0);

		// 获取验证码按钮
		bt_mGetsecurity = new Button(mContext);
		bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 240,
				MATCH_PARENT, 0, "获取验证码", 32, mLinearLayout, 30, 0, 0, 0);
		bt_mGetsecurity.setTextColor(Color.WHITE);
		bt_mGetsecurity.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));

		// TODO
		ll_phone.addView(et_mPhone);
		ll_phone.addView(bt_mGetsecurity);

		// 验证码输入框
		et_mSecurity = new EditText(mContext);
		machineFactory.MachineEditText(et_mSecurity, MATCH_PARENT, 96, 0,
				"请输入验证码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mSecurity.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang2.9.png", mContext));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 条款
		LinearLayout ll_clause = new LinearLayout(mContext);
		machineFactory.MachineView(ll_clause, MATCH_PARENT, 50, mLinearLayout,
				2, 20);
		ll_clause.setGravity(Gravity.CENTER_VERTICAL);

		// 同意服务条款
		ib_mAgreedbox = new ImageButton(mContext);
		machineFactory.MachineView(ib_mAgreedbox, 40, 40, mLinearLayout, 2, 5);
		ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkedbox.png", mActivity));
		ib_mAgreedbox.setBackgroundDrawable(null);

		// 不同意服务条款
		ib_mNotAgreedbox = new ImageButton(mContext);
		machineFactory.MachineView(ib_mNotAgreedbox, 40, 40, mLinearLayout, 2,
				5);
		ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkbox.png", mActivity));
		ib_mNotAgreedbox.setBackgroundDrawable(null);
		ib_mNotAgreedbox.setVisibility(View.GONE);

		TextView tv_agree = new TextView(mContext);
		machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
				"同意YY玩服务条款协议", 30, mLinearLayout, 6, 0, 0, 0);
		tv_agree.setTextColor(Color.GRAY);
		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
		tv_agree.setClickable(true);
		tv_agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YYprotocol_ho_dialog yYprotocol_ho_dialog = new YYprotocol_ho_dialog(
						mActivity);
				yYprotocol_ho_dialog.dialogShow();
			}
		});

		// TODO
		ll_clause.addView(ib_mAgreedbox);
		ll_clause.addView(ib_mNotAgreedbox);
		ll_clause.addView(tv_agree);
		ib_mAgreedbox.setClickable(true);
		ib_mAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mAgreedbox.setVisibility(View.GONE);
				ib_mNotAgreedbox.setVisibility(View.VISIBLE);
			}
		});
		ib_mNotAgreedbox.setClickable(true);
		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mNotAgreedbox.setVisibility(View.GONE);
				ib_mAgreedbox.setVisibility(View.VISIBLE);
			}
		});

		// 确定按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 50, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils
				.crSelectordraw("yaya_yellowbutton.9.png",
						"yaya_yellowbutton1.9.png", mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// 账号注册
		LinearLayout ll_accountregist = new LinearLayout(mContext);
		machineFactory.MachineView(ll_accountregist, MATCH_PARENT, 50,
				mLinearLayout, 2, 30);
		ll_accountregist.setOrientation(LinearLayout.HORIZONTAL);

		TextView tv_accountregist1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_accountregist1, WRAP_CONTENT,
				WRAP_CONTENT, 0, "未收到验证码,点击", 30, mLinearLayout, 0, 0, 0, 0);
		tv_accountregist1.setTextColor(Color.GRAY);

		// 账号注册点击textview
		tv_mRegisterclick = new TextView(mContext);
		machineFactory.MachineTextView(tv_mRegisterclick, WRAP_CONTENT,
				WRAP_CONTENT, 0, "账号注册", 30, mLinearLayout, 0, 0, 0, 0);
		tv_mRegisterclick.setTextColor(Color.parseColor("#66c4ef"));
		tv_mRegisterclick.setClickable(true);

		// TODO
		ll_accountregist.addView(tv_accountregist1);
		ll_accountregist.addView(tv_mRegisterclick);

		// TODO
		ll_content.addView(ll_phone);
		ll_content.addView(et_mSecurity);
		ll_content.addView(ll_clause);
		ll_content.addView(bt_mOk);
		ll_content.addView(ll_accountregist);

		baseLinearLayout.addView(rl_title);

		baseLinearLayout.addView(ll_content);

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

	public EditText getEt_mPhone() {
		return et_mPhone;
	}

	public void setEt_mPhone(EditText et_mPhone) {
		this.et_mPhone = et_mPhone;
	}

	public Button getBt_mGetsecurity() {
		return bt_mGetsecurity;
	}

	public void setBt_mGetsecurity(Button bt_mGetsecurity) {
		this.bt_mGetsecurity = bt_mGetsecurity;
	}

	public EditText getEt_mSecurity() {
		return et_mSecurity;
	}

	public void setEt_mSecurity(EditText et_mSecurity) {
		this.et_mSecurity = et_mSecurity;
	}

	public ImageButton getIb_mAgreedbox() {
		return ib_mAgreedbox;
	}

	public void setIb_mAgreedbox(ImageButton ib_mAgreedbox) {
		this.ib_mAgreedbox = ib_mAgreedbox;
	}

	public Button getBt_mOk() {
		return bt_mOk;
	}

	public void setBt_mOk(Button bt_mOk) {
		this.bt_mOk = bt_mOk;
	}

	public TextView getTv_mRegisterclick() {
		return tv_mRegisterclick;
	}

	public void setTv_mRegisterclick(TextView tv_mRegisterclick) {
		this.tv_mRegisterclick = tv_mRegisterclick;
	}

	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

}
