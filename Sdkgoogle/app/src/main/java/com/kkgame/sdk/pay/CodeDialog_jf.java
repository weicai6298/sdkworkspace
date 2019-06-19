package com.kkgame.sdk.pay;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;

public class CodeDialog_jf extends Basedialogview {

	private LinearLayout ll_mHelp;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private ImageButton ib_mAgreedbox;
	private Button bt_mOk;
	private TextView tv_mLoadtext;

	public CodeDialog_jf(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(Activity mActivity) {
		// TODO Auto-generated method stub
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 620, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, 650, 620, mLinearLayout,2,25);
		rl_content.setBackgroundColor(Color.WHITE);
		// rl_content.setGravity(Gravity.CENTER_HORIZONTAL);
		// rl_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 620, 0, mRelativeLayout,
				20, 0, 20, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 删除行
		LinearLayout ll_deleline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_deleline, MATCH_PARENT, 50,
				mLinearLayout, 2, 9);
		ll_deleline.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
				mLinearLayout);

		ll_mHelp = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mHelp, 60, MATCH_PARENT, mLinearLayout);
		ll_mHelp.setGravity(Gravity_CENTER);

		ImageView iv_help = new ImageView(mActivity);
		machineFactory.MachineView(iv_help, 50, 50, mLinearLayout, 3, 0);
		iv_help.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_help.png", mActivity));

		// TODO
		ll_mHelp.addView(iv_help);

		ll_mDele = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDele, 60, MATCH_PARENT, mLinearLayout,
				3, 0);
		ll_mDele.setGravity(Gravity_CENTER);
		ll_mDele.setClickable(true);

		ImageView iv_dele = new ImageView(mActivity);
		machineFactory.MachineView(iv_dele, 50, 50, mLinearLayout);
		iv_dele.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_dele.png", mActivity));
		iv_dele.setClickable(false);

		ll_mDele.addView(iv_dele);
		ll_mDele.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogDismiss();
			}
		});

		tv_mLoadtext = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mLoadtext, MATCH_PARENT,
				WRAP_CONTENT, 0, "", 28, mLinearLayout, 0, 5, 0, 0);
		tv_mLoadtext.setGravity(Gravity_CENTER);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mContext);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout, 2, 50);

		// 手机号码输入框
		et_mPhone = new EditText(mContext);
		machineFactory.MachineEditText(et_mPhone, 360, MATCH_PARENT, 0,
				"请输入验证码", 32, mLinearLayout, 0, 0, 0, 0);
		et_mPhone.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mContext));
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

		// 确定按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 100, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils
				.crSelectordraw("yaya_yellowbutton.9.png",
						"yaya_yellowbutton1.9.png", mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_deleline.addView(ll_zhanwei);

		ll_deleline.addView(ll_mDele);

		ll_content.addView(ll_deleline);
		ll_content.addView(tv_mLoadtext);
		ll_content.addView(ll_phone);

		ll_content.addView(bt_mOk);

		rl_content.addView(ll_content);

		baselin.addView(rl_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度

		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
	}

	public void setPayNow(OnClickListener listener) {
		bt_mOk.setOnClickListener(listener);
	}

	public void setNumText(String text) {
		et_mPhone.setHint(text);
	}

	public void setGetCode(OnClickListener listener) {
		bt_mGetsecurity.setOnClickListener(listener);
	}

	public void setGetCodeEnable(boolean flag) {
		if (flag) {
			bt_mGetsecurity.setVisibility(View.VISIBLE);
		} else {
			bt_mGetsecurity.setVisibility(View.GONE);
		}

	}

	public void setCancel(OnClickListener listener) {
		ll_mDele.setOnClickListener(listener);
	}

	public LinearLayout getLl_mHelp() {
		return ll_mHelp;
	}

	public void setLl_mHelp(LinearLayout ll_mHelp) {
		this.ll_mHelp = ll_mHelp;
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

	public TextView getTv_mLoadtext() {
		return tv_mLoadtext;
	}

	public void setTv_mLoadtext(TextView tv_mLoadtext) {
		this.tv_mLoadtext = tv_mLoadtext;
	}

	public void setCancelable(boolean b) {
			dialog.setCancelable(b);
	}

	public void setLoadText(String error_msg) {
		tv_mLoadtext.setText(error_msg);
		
	}

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		// TODO Auto-generated method stub
		dialog.setOnDismissListener(onDismissListener);
	}

}
