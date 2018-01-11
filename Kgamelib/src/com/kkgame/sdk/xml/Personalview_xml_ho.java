package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.Myimageview;
import com.kkgame.utils.DeviceUtil;

public class Personalview_xml_ho extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private Myimageview iv_mHead;
	private TextView tv_mChangeiv;
	private TextView tv_mUsename;
	private TextView tv_mUsename1;
	private TextView tv_mLogintime1;
	private TextView tv_mLogintime;
	private TextView tv_mBalance;
	private LinearLayout ll_mResetpassword;
	private LinearLayout ll_mBindphone;
	private LinearLayout ll_mBill;
	private LinearLayout ll_mGivebill;
	private LinearLayout ll_mQuesion;
	private LinearLayout ll_mAboutus;
	private LinearLayout ll_zhanwei1;
	private LinearLayout ll_zhanwei2;
	private LinearLayout ll_zhanwei3;
	private LinearLayout ll_zhanwei4;
	private LinearLayout ll_mChangehead;
	private TextView tv_fromcamera;
	private TextView tv_fromalbum;
	private TextView tv_cancel;
	private Button bt_mSwitchuser;
	private TextView tv_bindphone;
	private LinearLayout ll_mResetPhone;
	private TextView tv_mChongzhi;
	private LinearLayout ll_mRealNameAuthentication;
	private TextView tv_mRealNameAuthentication;

	public Personalview_xml_ho(Activity activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {

		int funtionmaginbut_ho = 0;
		int swichbut_ho = 0;
		int funtionmaginbut_po = 30;
		int swichbut_po = 100;
		int funtionmaginbut = 0;
		int swichbut = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			funtionmaginbut = funtionmaginbut_ho;
			swichbut = swichbut_ho;
		} else if ("portrait".equals(orientation)) {
			funtionmaginbut = funtionmaginbut_po;
			swichbut = swichbut_po;
		}

		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		android.view.ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
				MATCH_PARENT, MATCH_PARENT);

		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setLayoutParams(layoutParams);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		// 中间内容层
		LinearLayout ll_content = new LinearLayout(mContext);
		ll_content = (LinearLayout) machineFactory.MachineView(ll_content,
				MATCH_PARENT, MATCH_PARENT, 0, mRelativeLayout, 0, 30, 0, 0,
				LinearLayout.VERTICAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		LinearLayout ll_perdetail = new LinearLayout(mContext);
		machineFactory.MachineView(ll_perdetail, MATCH_PARENT, 130,
				mLinearLayout);

		LinearLayout ll_head = new LinearLayout(mContext);
		machineFactory.MachineView(ll_head, 130, MATCH_PARENT, mLinearLayout);
		ll_head.setOrientation(LinearLayout.VERTICAL);
		ll_head.setGravity(Gravity.CENTER_HORIZONTAL);

		iv_mHead = new Myimageview(mActivity, null);
		machineFactory.MachineView(iv_mHead, 80, 80, mLinearLayout, 2, 0);
		iv_mHead.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_default_head.png", mActivity));

		// 编辑头像文字的包裹外围
		LinearLayout ll_textedhead = new LinearLayout(mContext);
		machineFactory.MachineView(ll_textedhead, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		ll_textedhead.setOrientation(LinearLayout.VERTICAL);

		// 编辑头像的文字
		tv_mChangeiv = new TextView(mContext);
		machineFactory.MachineTextView(tv_mChangeiv, WRAP_CONTENT,
				WRAP_CONTENT, 0, "编辑头像", 22, mLinearLayout, 0, 0, 0, 0);
		tv_mChangeiv.setGravity(Gravity_CENTER);

		// 横线
		LinearLayout ll_line = new LinearLayout(mContext);
		machineFactory.MachineView(ll_line, MATCH_PARENT, 5, mLinearLayout, 2,
				3);
		ll_line.setBackgroundColor(Color.parseColor("#e0ec8e"));

		// TODO
		ll_textedhead.addView(tv_mChangeiv);
		ll_textedhead.addView(ll_line);

		// TODO
		ll_head.addView(iv_mHead);
		ll_head.addView(ll_textedhead);

		// 用户名列
		LinearLayout ll_username = new LinearLayout(mContext);
		machineFactory.MachineView(ll_username, 340, MATCH_PARENT,
				mLinearLayout);

		// 用户名左边列
		LinearLayout ll_usernamelist1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_usernamelist1, 120, WRAP_CONTENT,
				mLinearLayout, 1, 9);
		ll_usernamelist1.setOrientation(LinearLayout.VERTICAL);

		tv_mUsename1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_mUsename1, MATCH_PARENT,
				WRAP_CONTENT, 0, "用户名:", 26, mLinearLayout, 0, 0, 0, 0);

		tv_mLogintime1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_mLogintime1, MATCH_PARENT,
				WRAP_CONTENT, 0, "最后登录:", 26, mLinearLayout, 0, 10, 0, 0);

		// TODO
		ll_usernamelist1.addView(tv_mUsename1);
		ll_usernamelist1.addView(tv_mLogintime1);

		// 用户名右边列
		LinearLayout ll_usernamelist2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_usernamelist2, MATCH_PARENT,
				WRAP_CONTENT, mLinearLayout);
		ll_usernamelist2.setOrientation(LinearLayout.VERTICAL);

		tv_mUsename = new TextView(mContext);
		machineFactory.MachineTextView(tv_mUsename, MATCH_PARENT, WRAP_CONTENT,
				0, "张珈凡", 26, mLinearLayout, 0, 0, 0, 0);
		tv_mUsename.setTextColor(Color.parseColor("#ec7600"));

		tv_mLogintime = new TextView(mContext);
		machineFactory.MachineTextView(tv_mLogintime, MATCH_PARENT,
				WRAP_CONTENT, 0, "2015/05/27", 26, mLinearLayout, 0, 10, 0, 0);
		tv_mLogintime.setTextColor(Color.parseColor("#ec7600"));
		// TODO
		ll_usernamelist2.addView(tv_mUsename);
		ll_usernamelist2.addView(tv_mLogintime);

		// TODO
		ll_username.addView(ll_usernamelist1);
		ll_username.addView(ll_usernamelist2);

		// 余额列表
		LinearLayout ll_yue = new LinearLayout(mContext);
		machineFactory.MachineView(ll_yue, 400, MATCH_PARENT, mLinearLayout);
		ll_yue.setOrientation(LinearLayout.VERTICAL);

		LinearLayout ll_balance = new LinearLayout(mContext);
		machineFactory.MachineView(ll_balance, MATCH_PARENT, WRAP_CONTENT,
				mLinearLayout);

		TextView tv_balance1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_balance1, WRAP_CONTENT, WRAP_CONTENT,
				0, "余额:", 26, mLinearLayout, 0, 0, 0, 0);
		tv_mBalance = new TextView(mContext);
		machineFactory.MachineTextView(tv_mBalance, WRAP_CONTENT, WRAP_CONTENT,
				0, "$300", 26, mLinearLayout, 10, 0, 0, 0);
		tv_mBalance.setTextColor(Color.parseColor("#ec7600"));

		// TODO
		ll_balance.addView(tv_balance1);
		ll_balance.addView(tv_mBalance);

		tv_mChongzhi = new TextView(mContext);
		machineFactory.MachineTextView(tv_mChongzhi, WRAP_CONTENT,
				WRAP_CONTENT, 0, "点击充值丫丫币", 25, mLinearLayout, 0, 10, 0, 0);
		tv_mChongzhi.setTextColor(Color.parseColor("#ec7600"));

		ll_yue.addView(ll_balance);
		ll_yue.addView(tv_mChongzhi);

		// TODO
		ll_perdetail.addView(ll_head);
		ll_perdetail.addView(ll_username);
		ll_perdetail.addView(ll_yue);

		LinearLayout ll_funtion = new LinearLayout(mContext);
		machineFactory.MachineView(ll_funtion, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_funtion.setBackgroundColor(Color.parseColor("#f1f1f1"));
		ll_funtion.setOrientation(LinearLayout.VERTICAL);

		LinearLayout ll_funtionline1 = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_funtionline1, MATCH_PARENT, 140,
				mLinearLayout, 2, funtionmaginbut);
		// ll_funtionline1.setGravity(Gravity_CENTER);

		// 重设密码
		ll_mResetpassword = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mResetpassword, 0, MATCH_PARENT, 1,
				mLinearLayout);
		ll_mResetpassword.setGravity(Gravity_CENTER);
		ll_mResetpassword.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_resetpassword = new ImageView(mActivity);
		machineFactory.MachineView(iv_resetpassword, 80, 80, mLinearLayout);
		iv_resetpassword.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_resetpassword.png", mActivity));
		TextView tv_resetpassword = new TextView(mContext);
		machineFactory.MachineTextView(tv_resetpassword, WRAP_CONTENT,
				WRAP_CONTENT, 0, "修改密码", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mResetpassword.addView(iv_resetpassword);
		ll_mResetpassword.addView(tv_resetpassword);

		// 绑定手机
		ll_mBindphone = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mBindphone, 0, MATCH_PARENT, 1,
				mLinearLayout);
		ll_mBindphone.setGravity(Gravity_CENTER);
		ll_mBindphone.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_bindphone = new ImageView(mActivity);
		machineFactory.MachineView(iv_bindphone, 80, 80, mLinearLayout);
		iv_bindphone.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_bindphone.png", mActivity));

		tv_bindphone = new TextView(mContext);
		machineFactory.MachineTextView(tv_bindphone, WRAP_CONTENT,
				WRAP_CONTENT, 0, "绑定手机", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mBindphone.addView(iv_bindphone);
		ll_mBindphone.addView(tv_bindphone);

		// 消费记录
		ll_mBill = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mBill, 0, MATCH_PARENT, 1, mLinearLayout);
		ll_mBill.setGravity(Gravity_CENTER);
		ll_mBill.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_bill = new ImageView(mActivity);
		machineFactory.MachineView(iv_bill, 80, 80, mLinearLayout);
		iv_bill.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_bill.png", mActivity));

		TextView tv_bill = new TextView(mContext);
		machineFactory.MachineTextView(tv_bill, WRAP_CONTENT, WRAP_CONTENT, 0,
				"消费记录", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mBill.addView(iv_bill);
		ll_mBill.addView(tv_bill);

		// 充值记录
		ll_mGivebill = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mGivebill, 0, MATCH_PARENT, 1,
				mLinearLayout);
		ll_mGivebill.setGravity(Gravity_CENTER);
		ll_mGivebill.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_givebill = new ImageView(mActivity);
		machineFactory.MachineView(iv_givebill, 80, 80, mLinearLayout);
		iv_givebill.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_givebill.png", mActivity));

		TextView tv_givebill = new TextView(mContext);
		machineFactory.MachineTextView(tv_givebill, WRAP_CONTENT, WRAP_CONTENT,
				0, "充值记录", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mGivebill.addView(iv_givebill);
		ll_mGivebill.addView(tv_givebill);

		// 问题反馈
		ll_mQuesion = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mQuesion, 0, MATCH_PARENT, 1,
				mLinearLayout);
		ll_mQuesion.setGravity(Gravity_CENTER);
		ll_mQuesion.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_Question = new ImageView(mActivity);
		machineFactory.MachineView(iv_Question, 80, 80, mLinearLayout);
		iv_Question.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_quesion.png", mActivity));

		TextView tv_Question = new TextView(mContext);
		machineFactory.MachineTextView(tv_Question, WRAP_CONTENT, WRAP_CONTENT,
				0, "问题反馈", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mQuesion.addView(iv_Question);
		ll_mQuesion.addView(tv_Question);

		// TODO
		ll_funtionline1.addView(ll_mResetpassword);
		ll_funtionline1.addView(ll_mBindphone);
		ll_funtionline1.addView(ll_mBill);
		ll_funtionline1.addView(ll_mGivebill);
		ll_funtionline1.addView(ll_mQuesion);

		LinearLayout ll_funtionline2 = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_funtionline2, MATCH_PARENT, 120,
				mLinearLayout, 2, funtionmaginbut);
		// ll_funtionline2.setGravity(Gravity.CENTER);
		ll_funtionline2.setOrientation(LinearLayout.HORIZONTAL);

		ll_mAboutus = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mAboutus, 0, MATCH_PARENT, 1,
				mLinearLayout);
		ll_mAboutus.setGravity(Gravity.CENTER);
		ll_mAboutus.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_mAboutus = new ImageView(mActivity);
		machineFactory.MachineView(iv_mAboutus, 80, 80, mLinearLayout);
		iv_mAboutus.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_aboutus.png", mActivity));
		TextView tv_mAboutus = new TextView(mContext);
		machineFactory.MachineTextView(tv_mAboutus, WRAP_CONTENT, WRAP_CONTENT,
				0, "客服中心", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mAboutus.addView(iv_mAboutus);
		ll_mAboutus.addView(tv_mAboutus);

		ll_mRealNameAuthentication = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mRealNameAuthentication, 0, MATCH_PARENT,
				1, mLinearLayout);
		ll_mRealNameAuthentication.setGravity(Gravity.CENTER);
		ll_mRealNameAuthentication.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_mRealNameAuthentication = new ImageView(mActivity);
		machineFactory.MachineView(iv_mRealNameAuthentication, 80, 80,
				mLinearLayout);
		iv_mRealNameAuthentication.setImageBitmap(GetAssetsutils
				.getImageFromAssetsFile("yaya_certification.png", mActivity));
		tv_mRealNameAuthentication = new TextView(mContext);
		machineFactory.MachineTextView(tv_mRealNameAuthentication,
				WRAP_CONTENT, WRAP_CONTENT, 0, "实名认证", 24, mLinearLayout, 3, 3,
				0, 0);
		tv_mRealNameAuthentication.setTextColor(Color.parseColor("#fc3a6d"));

		ll_mRealNameAuthentication.addView(iv_mRealNameAuthentication);
		ll_mRealNameAuthentication.addView(tv_mRealNameAuthentication);

		ll_mResetPhone = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mResetPhone, 0, MATCH_PARENT, 1,
				mLinearLayout);
		ll_mResetPhone.setGravity(Gravity.CENTER);
		ll_mResetPhone.setOrientation(LinearLayout.VERTICAL);

		ImageView iv_mResetPhone = new ImageView(mActivity);
		machineFactory.MachineView(iv_mResetPhone, 80, 80, mLinearLayout);
		iv_mResetPhone.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_bindphone.png", mActivity));
		TextView tv_mResetPhone = new TextView(mContext);
		machineFactory.MachineTextView(tv_mResetPhone, WRAP_CONTENT,
				WRAP_CONTENT, 0, "更换手机", 24, mLinearLayout, 0, 3, 0, 0);

		ll_mResetPhone.addView(iv_mResetPhone);
		ll_mResetPhone.addView(tv_mResetPhone);

		// 占位1
		ll_zhanwei1 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei1, 0, MATCH_PARENT, 1,
				mLinearLayout);
		// 占位1
		ll_zhanwei2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei2, 0, MATCH_PARENT, 1,
				mLinearLayout);

		// 占位1
		ll_zhanwei3 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei3, 0, MATCH_PARENT, 1,
				mLinearLayout);

		// 占位1
		ll_zhanwei4 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei4, 0, MATCH_PARENT, 1,
				mLinearLayout);

		// TODO
		ll_funtionline2.addView(ll_mAboutus);
		ll_funtionline2.addView(ll_mRealNameAuthentication);
		ll_funtionline2.addView(ll_zhanwei2);
		ll_funtionline2.addView(ll_zhanwei3);
		ll_funtionline2.addView(ll_zhanwei4);

		LinearLayout ll_funtionline3 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_funtionline3, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_funtionline3.setGravity(Gravity.CENTER_HORIZONTAL);

		bt_mSwitchuser = new Button(mContext);
		machineFactory.MachineButton(bt_mSwitchuser, 300, 70, 0, "切换账号", 26,
				mLinearLayout, 0, swichbut, 0, 0);
		bt_mSwitchuser.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_swichbackground.9.png",
						mContext));
		bt_mSwitchuser.setTextColor(Color.WHITE);

		if (ViewConstants.nochangeacount) {

		} else {
			ll_funtionline3.addView(bt_mSwitchuser);
		}

		ll_funtion.addView(ll_funtionline1);
		ll_funtion.addView(ll_funtionline2);
		ll_funtion.addView(ll_funtionline3);

		// TODO
		ll_content.addView(ll_perdetail);
		ll_content.addView(ll_funtion);

		ll_mChangehead = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mChangehead, MATCH_PARENT, WRAP_CONTENT,
				0, mRelativeLayout, 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_BOTTOM);
		ll_mChangehead.setOrientation(LinearLayout.VERTICAL);
		ll_mChangehead.setBackgroundColor(Color.WHITE);

		tv_fromcamera = new TextView(mContext);
		machineFactory.MachineTextView(tv_fromcamera, MATCH_PARENT, 70, 0,
				"拍照", 26, mLinearLayout, 0, 0, 0, 0);
		tv_fromcamera.setTextColor(Color.parseColor("#666666"));

		tv_fromcamera.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei5 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei5, MATCH_PARENT, 2, mLinearLayout);
		ll_zhanwei5.setBackgroundColor(Color.parseColor("#e1e1e1"));

		tv_fromalbum = new TextView(mContext);
		machineFactory.MachineTextView(tv_fromalbum, MATCH_PARENT, 70, 0,
				"手机相册上传", 26, mLinearLayout, 0, 0, 0, 0);
		tv_fromalbum.setTextColor(Color.parseColor("#666666"));
		tv_fromalbum.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei6 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_zhanwei6, MATCH_PARENT, 2, mLinearLayout);
		ll_zhanwei6.setBackgroundColor(Color.parseColor("#e1e1e1"));

		tv_cancel = new TextView(mContext);
		machineFactory.MachineTextView(tv_cancel, MATCH_PARENT, 70, 0, "取消",
				26, mLinearLayout, 0, 0, 0, 0);
		tv_cancel.setTextColor(Color.parseColor("#666666"));
		tv_cancel.setGravity(Gravity_CENTER);
		tv_cancel.setClickable(true);

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_mChangehead.setVisibility(View.GONE);
			}
		});

		ll_mChangehead.addView(tv_fromcamera);
		ll_mChangehead.addView(ll_zhanwei5);
		ll_mChangehead.addView(tv_fromalbum);
		ll_mChangehead.addView(ll_zhanwei6);
		ll_mChangehead.addView(tv_cancel);
		ll_mChangehead.setVisibility(View.GONE);

		rl_content.addView(ll_content);
		rl_content.addView(ll_mChangehead);

		baseLinearLayout.addView(rl_content);

		ll_textedhead.setClickable(true);
		ll_textedhead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (View.GONE == ll_mChangehead.getVisibility()) {
					ll_mChangehead.setVisibility(View.VISIBLE);
				} else {
					ll_mChangehead.setVisibility(View.GONE);
				}
			}
		});

		return baseLinearLayout;
	}

	public LinearLayout getLl_mRealNameAuthentication() {
		return ll_mRealNameAuthentication;
	}

	public void setLl_mRealNameAuthentication(
			LinearLayout ll_mRealNameAuthentication) {
		this.ll_mRealNameAuthentication = ll_mRealNameAuthentication;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public TextView getTv_mChangeiv() {
		return tv_mChangeiv;
	}

	public void setTv_mChangeiv(TextView tv_mChangeiv) {
		this.tv_mChangeiv = tv_mChangeiv;
	}

	public TextView getTv_mUsename() {
		return tv_mUsename;
	}

	public void setTv_mUsename(TextView tv_mUsename) {
		this.tv_mUsename = tv_mUsename;
	}

	public TextView getTv_mUsename1() {
		return tv_mUsename1;
	}

	public void setTv_mUsename1(TextView tv_mUsename1) {
		this.tv_mUsename1 = tv_mUsename1;
	}

	public TextView getTv_mLogintime1() {
		return tv_mLogintime1;
	}

	public void setTv_mLogintime1(TextView tv_mLogintime1) {
		this.tv_mLogintime1 = tv_mLogintime1;
	}

	public TextView getTv_mLogintime() {
		return tv_mLogintime;
	}

	public void setTv_mLogintime(TextView tv_mLogintime) {
		this.tv_mLogintime = tv_mLogintime;
	}

	public TextView getTv_mBalance() {
		return tv_mBalance;
	}

	public void setTv_mBalance(TextView tv_mBalance) {
		this.tv_mBalance = tv_mBalance;
	}

	public LinearLayout getLl_mResetpassword() {
		return ll_mResetpassword;
	}

	public void setLl_mResetpassword(LinearLayout ll_mResetpassword) {
		this.ll_mResetpassword = ll_mResetpassword;
	}

	public LinearLayout getLl_mBindphone() {
		return ll_mBindphone;
	}

	public void setLl_mBindphone(LinearLayout ll_mBindphone) {
		this.ll_mBindphone = ll_mBindphone;
	}

	public LinearLayout getLl_mBill() {
		return ll_mBill;
	}

	public void setLl_mBill(LinearLayout ll_mBill) {
		this.ll_mBill = ll_mBill;
	}

	public LinearLayout getLl_mGivebill() {
		return ll_mGivebill;
	}

	public void setLl_mGivebill(LinearLayout ll_mGivebill) {
		this.ll_mGivebill = ll_mGivebill;
	}

	public LinearLayout getLl_mQuesion() {
		return ll_mQuesion;
	}

	public void setLl_mQuesion(LinearLayout ll_mQuesion) {
		this.ll_mQuesion = ll_mQuesion;
	}

	public LinearLayout getLl_mAboutus() {
		return ll_mAboutus;
	}

	public void setLl_mAboutus(LinearLayout ll_mAboutus) {
		this.ll_mAboutus = ll_mAboutus;
	}

	public LinearLayout getLl_zhanwei1() {
		return ll_zhanwei1;
	}

	public void setLl_zhanwei1(LinearLayout ll_zhanwei1) {
		this.ll_zhanwei1 = ll_zhanwei1;
	}

	public LinearLayout getLl_zhanwei2() {
		return ll_zhanwei2;
	}

	public void setLl_zhanwei2(LinearLayout ll_zhanwei2) {
		this.ll_zhanwei2 = ll_zhanwei2;
	}

	public LinearLayout getLl_zhanwei3() {
		return ll_zhanwei3;
	}

	public void setLl_zhanwei3(LinearLayout ll_zhanwei3) {
		this.ll_zhanwei3 = ll_zhanwei3;
	}

	public LinearLayout getLl_zhanwei4() {
		return ll_zhanwei4;
	}

	public void setLl_zhanwei4(LinearLayout ll_zhanwei4) {
		this.ll_zhanwei4 = ll_zhanwei4;
	}

	public LinearLayout getLl_mChangehead() {
		return ll_mChangehead;
	}

	public void setLl_mChangehead(LinearLayout ll_mChangehead) {
		this.ll_mChangehead = ll_mChangehead;
	}

	public TextView getTv_fromcamera() {
		return tv_fromcamera;
	}

	public void setTv_fromcamera(TextView tv_fromcamera) {
		this.tv_fromcamera = tv_fromcamera;
	}

	public TextView getTv_fromalbum() {
		return tv_fromalbum;
	}

	public void setTv_fromalbum(TextView tv_fromalbum) {
		this.tv_fromalbum = tv_fromalbum;
	}

	public TextView getTv_cancel() {
		return tv_cancel;
	}

	public void setTv_cancel(TextView tv_cancel) {
		this.tv_cancel = tv_cancel;
	}

	public Myimageview getIv_mHead() {
		return iv_mHead;
	}

	public void setIv_mHead(Myimageview iv_mHead) {
		this.iv_mHead = iv_mHead;
	}

	public Button getBt_mSwitchuser() {
		return bt_mSwitchuser;
	}

	public void setBt_mSwitchuser(Button bt_mSwitchuser) {
		this.bt_mSwitchuser = bt_mSwitchuser;
	}

	public TextView getTv_bindphone() {
		return tv_bindphone;
	}

	public LinearLayout getLl_mResetPhone() {
		return ll_mResetPhone;
	}

	public void setLl_mResetPhone(LinearLayout ll_mResetPhone) {
		this.ll_mResetPhone = ll_mResetPhone;
	}

	public void setTv_bindphone(TextView tv_bindphone) {
		this.tv_bindphone = tv_bindphone;
	}

	public TextView getTv_mChongzhi() {
		return tv_mChongzhi;
	}

	public void setTv_mChongzhi(TextView tv_mChongzhi) {
		this.tv_mChongzhi = tv_mChongzhi;
	}

	public TextView getTv_mRealNameAuthentication() {
		return tv_mRealNameAuthentication;
	}

	public void setTv_mRealNameAuthentication(
			TextView tv_mRealNameAuthentication) {
		this.tv_mRealNameAuthentication = tv_mRealNameAuthentication;
	}

}
