package com.kkgame.sdk.xml;

import java.sql.Types;

import android.app.Activity;
import android.graphics.Color;
import android.renderscript.Type;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Loginxml_po extends Basexml implements Layoutxml {

	private RelativeLayout baseRelativeLayout;
	private LinearLayout ll_certerContent;
	private ImageView mLogo;
	private LinearLayout ll_mUser;
	private ImageView iv_mUn_icon;
	private EditText et_mUn;
	private ImageView iv_mUn_down;
	private LinearLayout ll_mPassword;
	private ImageView iv_mPs_icon;
	private EditText et_mPs;
	private LinearLayout ll_mBut;
	private Button bt_mRegister;
	private Button bt_mlogin;
	private TextView tv_mForgetpassword;
	private ImageView iv_mWeibo;
	private ImageView iv_mQq;
	private ListView lv_mHistoryuser;
	private LinearLayout ll_mDown;
	private TextView tv_Mphonelogin;

	public Loginxml_po(Activity mContext) {
		super(mContext);

	}

	@Override
	public View initViewxml() {

		// float k=System.currentTimeMillis();

		baseRelativeLayout = new RelativeLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);
		baseRelativeLayout.setBackgroundColor(Color.WHITE);

		baseRelativeLayout.setLayoutParams(layoutParams);

		// 中间内容
		ll_certerContent = new LinearLayout(mContext);
		ll_certerContent = (LinearLayout) machineFactory.MachineView(
				ll_certerContent, 700, WRAP_CONTENT, 0, "RelativeLayout", 20,
				180, 20, 0, RelativeLayout.CENTER_HORIZONTAL);
		ll_certerContent.setOrientation(LinearLayout.VERTICAL);
		ll_certerContent.setGravity(Gravity.CENTER);

		// 设置logo图片
		mLogo = new ImageView(mContext);
		mLogo.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_LOGO.png", mActivity));
		mLogo = (ImageView) machineFactory.MachineView(mLogo, 145, 200,
				"LinearLayout");

		ll_certerContent.addView(mLogo);

		// 设置username的ll 注意背景的兼容性问题
		ll_mUser = new LinearLayout(mContext);
		ll_mUser = (LinearLayout) machineFactory.MachineView(ll_mUser,
				MATCH_PARENT, 100, 0, "LinearLayout", 0, 20, 0, 0, 100);

		ll_mUser.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mContext));

		ll_mUser.setGravity(Gravity.CENTER);

		// username 的icon
		iv_mUn_icon = new ImageView(mContext);
		iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 40,
				40, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_username.png", mActivity));

		// username的edtext
		et_mUn = new EditText(mContext);
		et_mUn = machineFactory.MachineEditText(et_mUn, 0, MATCH_PARENT, 1,
				"请输入用户名", 28, mLinearLayout, 0, 6, 0, 0);
		et_mUn.setBackgroundColor(Color.TRANSPARENT);

		ll_mDown = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDown, 60, MATCH_PARENT, mLinearLayout,
				3, 10);
		ll_mDown.setGravity(Gravity_CENTER);
		ll_mDown.setClickable(true);

		// username的下拉图片
		iv_mUn_down = new ImageView(mContext);
		iv_mUn_down = (ImageView) machineFactory.MachineView(iv_mUn_down, 40,
				40, 0, mLinearLayout, 0, 0, 0, 0, 100);
		iv_mUn_down.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_down.png", mActivity));
		iv_mUn_down.setClickable(false);

		ll_mDown.addView(iv_mUn_down);

		// TODO
		ll_mUser.addView(iv_mUn_icon);
		ll_mUser.addView(et_mUn);
		ll_mUser.addView(ll_mDown);

		// 设置password的ll 注意背景的兼容性问题
		ll_mPassword = new LinearLayout(mContext);
		ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
				MATCH_PARENT, 100, 0, "LinearLayout", 0, 20, 0, 0, 100);

		ll_mPassword.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mContext));
		// Drawable drawable =new
		// BitmapDrawable(Biankuang.createBiankuang(machSize(700),
		// machSize(100)));
		// ll_mUser.setBackground(drawable);
		// ll_mUser.setBackground(mActivity.getResources().getDrawable(R.drawable.yaya_biankuang));
		ll_mPassword.setGravity(Gravity.CENTER);

		// password 的icon
		iv_mPs_icon = new ImageView(mContext);
		iv_mPs_icon = (ImageView) machineFactory.MachineView(iv_mPs_icon, 40,
				40, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mPs_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_password.png", mActivity));

		// password的edtext
		et_mPs = new EditText(mContext);
		et_mPs = machineFactory.MachineEditText(et_mPs, 0, MATCH_PARENT, 1,
				"请输入密码", 28, mLinearLayout, 0, 6, 0, 0);
		et_mPs.setBackgroundColor(Color.TRANSPARENT);
		et_mPs.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		// TODO
		ll_mPassword.addView(iv_mPs_icon);
		ll_mPassword.addView(et_mPs);

		// but 的lin

		ll_mBut = new LinearLayout(mContext);
		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
				MATCH_PARENT, 100, mLinearLayout, 2, 30);

		// button的注册按钮
		bt_mRegister = new Button(mContext);
		bt_mRegister = machineFactory.MachineButton(bt_mRegister, 0,
				MATCH_PARENT, 1, "手机注册", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mRegister.setTextColor(Color.WHITE);
		bt_mRegister.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));
		bt_mRegister.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei = new LinearLayout(mContext);
		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 20,
				MATCH_PARENT, mLinearLayout);

		// button的登录按钮
		bt_mlogin = new Button(mContext);
		bt_mlogin = machineFactory.MachineButton(bt_mlogin, 0, MATCH_PARENT, 1,
				"立即登录", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mlogin.setTextColor(Color.WHITE);
		bt_mlogin.setBackgroundDrawable(GetAssetsutils
				.crSelectordraw("yaya_yellowbutton.9.png",
						"yaya_yellowbutton1.9.png", mActivity));
		bt_mlogin.setGravity(Gravity_CENTER);

		// TODO
		ll_mBut.addView(bt_mRegister);
		ll_mBut.addView(ll_zhanwei);
		ll_mBut.addView(bt_mlogin);

		// 忘记密码的LinearLayout
		LinearLayout ll_forgetpassword = new LinearLayout(mContext);
		ll_forgetpassword = (LinearLayout) machineFactory.MachineView(
				ll_forgetpassword, MATCH_PARENT, WRAP_CONTENT, mLinearLayout,
				2, 10);

		//手机登陆
		tv_Mphonelogin = new TextView(mContext);
		tv_Mphonelogin = (TextView) machineFactory.MachineTextView(tv_Mphonelogin, 0,
				80, 1, "手机登陆", 25, mLinearLayout, 0, 0, 0, 0);
		tv_Mphonelogin.setTextColor(Color.parseColor("#66c4ef"));
		tv_Mphonelogin.setClickable(true);

		// 占位LinearLayout
		LinearLayout ll_zhanwei2 = new LinearLayout(mContext);
		ll_zhanwei2 = (LinearLayout) machineFactory.MachineView(ll_zhanwei2,
				40, MATCH_PARENT, mLinearLayout);

		// 忘记密码
		tv_mForgetpassword = new TextView(mContext);
		tv_mForgetpassword = machineFactory.MachineTextView(tv_mForgetpassword,
				0, 80, 1, "忘记密码?", 25, mLinearLayout, 0, 0, 0, 0);
		tv_mForgetpassword.setTextColor(Color.GRAY);

		// TODO
		ll_forgetpassword.addView(tv_Mphonelogin);
		ll_forgetpassword.addView(ll_zhanwei2);
		ll_forgetpassword.addView(tv_mForgetpassword);

		// im登录
		LinearLayout ll_im_login = new LinearLayout(mContext);
		ll_im_login = (LinearLayout) machineFactory.MachineView(ll_im_login,
				MATCH_PARENT, WRAP_CONTENT, 0, mRelativeLayout, 0, 0, 0, 30,
				RelativeLayout.ALIGN_PARENT_BOTTOM);
		ll_im_login.setGravity(Gravity.CENTER);

		// 微博登录图片按钮
		iv_mWeibo = new ImageView(mContext);
		machineFactory.MachineView(iv_mWeibo, 70, 70, mLinearLayout, 3, 5);
		iv_mWeibo.setBackgroundDrawable(null);
		iv_mWeibo.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_sina.png", mActivity));

		// QQ登录图片按钮
		iv_mQq = new ImageView(mContext);
		machineFactory.MachineView(iv_mQq, 70, 70, mLinearLayout, 1, 12);
		iv_mQq.setBackgroundDrawable(null);
		iv_mQq.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_qq.png", mActivity));

		// TODO
		ll_im_login.addView(iv_mWeibo);
		ll_im_login.addView(iv_mQq);

		ll_certerContent.addView(ll_mUser);
		ll_certerContent.addView(ll_mPassword);
		ll_certerContent.addView(ll_mBut);
		ll_certerContent.addView(ll_forgetpassword);

		// 下拉选择历史账户
		lv_mHistoryuser = new ListView(mContext);
		machineFactory.MachineView(lv_mHistoryuser, 700, WRAP_CONTENT, 0,
				"RelativeLayout", 20, 500, 20, 0,
				RelativeLayout.CENTER_HORIZONTAL);
		lv_mHistoryuser.setVisibility(View.GONE);

		/*
		 * android.view.ViewGroup.LayoutParams layoutParams1 = new
		 * ViewGroup.LayoutParams( MATCH_PARENT, WRAP_CONTENT);
		 * relativeLayout.setLayoutParams(layoutParams1);
		 */

		baseRelativeLayout.addView(ll_certerContent);
		baseRelativeLayout.addView(ll_im_login);
		baseRelativeLayout.addView(lv_mHistoryuser);

		// float y=System.;

		// Log.e("一共用了时间", k+"++++++++++++"+y+"++++++++++++++++++++++++++");
		return baseRelativeLayout;

	}

	public void setListviewheight(int size) {
		lv_mHistoryuser.getLayoutParams().height = machSize((size * 100));
	}

	public RelativeLayout getBaseRelativeLayout() {
		return baseRelativeLayout;
	}

	public void setBaseRelativeLayout(RelativeLayout baseRelativeLayout) {
		this.baseRelativeLayout = baseRelativeLayout;
	}

	public LinearLayout getLl_certerContent() {
		return ll_certerContent;
	}

	public void setLl_certerContent(LinearLayout ll_certerContent) {
		this.ll_certerContent = ll_certerContent;
	}

	public ImageView getmLogo() {
		return mLogo;
	}

	public void setmLogo(ImageView mLogo) {
		this.mLogo = mLogo;
	}

	public LinearLayout getLl_mUser() {
		return ll_mUser;
	}

	public void setLl_mUser(LinearLayout ll_mUser) {
		this.ll_mUser = ll_mUser;
	}

	public ImageView getIv_mUn_icon() {
		return iv_mUn_icon;
	}

	public void setIv_mUn_icon(ImageView iv_mUn_icon) {
		this.iv_mUn_icon = iv_mUn_icon;
	}

	public EditText getEt_mUn() {
		return et_mUn;
	}

	public void setEt_mUn(EditText et_mUn) {
		this.et_mUn = et_mUn;
	}

	public ImageView getIv_mUn_down() {
		return iv_mUn_down;
	}

	public void setIv_mUn_down(ImageView iv_mUn_down) {
		this.iv_mUn_down = iv_mUn_down;
	}

	public LinearLayout getLl_mPassword() {
		return ll_mPassword;
	}

	public void setLl_mPassword(LinearLayout ll_mPassword) {
		this.ll_mPassword = ll_mPassword;
	}

	public ImageView getIv_mPs_icon() {
		return iv_mPs_icon;
	}

	public void setIv_mPs_icon(ImageView iv_mPs_icon) {
		this.iv_mPs_icon = iv_mPs_icon;
	}

	public EditText getEt_mPs() {
		return et_mPs;
	}

	public void setEt_mPs(EditText et_mPs) {
		this.et_mPs = et_mPs;
	}

	public LinearLayout getLl_mBut() {
		return ll_mBut;
	}

	public void setLl_mBut(LinearLayout ll_mBut) {
		this.ll_mBut = ll_mBut;
	}

	public Button getBt_mRegister() {
		return bt_mRegister;
	}

	public void setBt_mRegister(Button bt_mRegister) {
		this.bt_mRegister = bt_mRegister;
	}

	public Button getbt_mlogin() {
		return bt_mlogin;
	}

	public void setbt_mlogin(Button bt_mlogin) {
		this.bt_mlogin = bt_mlogin;
	}

	public TextView getTv_mForgetpassword() {
		return tv_mForgetpassword;
	}

	public void setTv_mForgetpassword(TextView tv_mForgetpassword) {
		this.tv_mForgetpassword = tv_mForgetpassword;
	}

	public ImageView getIv_mWeibo() {
		return iv_mWeibo;
	}

	public void setIv_mWeibo(ImageView iv_mWeibo) {
		this.iv_mWeibo = iv_mWeibo;
	}

	public ImageView getIv_mQq() {
		return iv_mQq;
	}

	public void setIv_mQq(ImageView iv_mQq) {
		this.iv_mQq = iv_mQq;
	}

	public ListView getLv_mHistoryuser() {
		return lv_mHistoryuser;
	}

	public void setLv_mHistoryuser(ListView lv_mHistoryuser) {
		this.lv_mHistoryuser = lv_mHistoryuser;
	}

	public Button getBt_mlogin() {
		return bt_mlogin;
	}

	public void setBt_mlogin(Button bt_mlogin) {
		this.bt_mlogin = bt_mlogin;
	}

	public LinearLayout getLl_mDown() {
		return ll_mDown;
	}

	public void setLl_mDown(LinearLayout ll_mDown) {
		this.ll_mDown = ll_mDown;
	}

	public TextView getTv_Mphonelogin() {
		return tv_Mphonelogin;
	}

	public void setTv_Mphonelogin(TextView tv_Mphonelogin) {
		this.tv_Mphonelogin = tv_Mphonelogin;
	}
	
	

}
