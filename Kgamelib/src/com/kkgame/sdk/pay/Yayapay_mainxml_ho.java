package com.kkgame.sdk.pay;



import com.kkgame.sdk.xml.Basexml;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Layoutxml;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Yayapay_mainxml_ho extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private TextView tv_mYuanbao;
	private TextView tv_mMoney;
	private RelativeLayout rl_mBluep;
	private RelativeLayout rl_mChuxuka;
	private RelativeLayout rl_mXinyongka;
	private RelativeLayout rl_mYidong;
	private RelativeLayout rl_mLiantong;
	private RelativeLayout rl_mDianxin;
	private RelativeLayout rl_mShengda;
	private RelativeLayout rl_mJunka;
	private RelativeLayout rl_mYaya;
	private RelativeLayout rl_mQbi;
	private LinearLayout ll_mPre;
	private TextView tv_mHelp;

	public Yayapay_mainxml_ho(Activity activity) {
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
				0, 0);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		iv_mPre.setClickable(false);
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mActivity.finish();
			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"付款", 40, mRelativeLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		tv_mHelp = new TextView(mContext);
		machineFactory.MachineTextView(tv_mHelp, WRAP_CONTENT, MATCH_PARENT, 0,
				"帮助", 36, mRelativeLayout, 0, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_mHelp.setTextColor(Color.parseColor("#267fc4"));
		tv_mHelp.setGravity(Gravity_CENTER);
		tv_mHelp.setClickable(true);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);
		rl_title.addView(tv_mHelp);

		LinearLayout ll_allcontent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_allcontent, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);

		LinearLayout ll_leftitem = new LinearLayout(mContext);
		machineFactory.MachineView(ll_leftitem, 250, MATCH_PARENT,
				mLinearLayout);
		ll_leftitem.setOrientation(LinearLayout.VERTICAL);

		// 多少元宝
		tv_mYuanbao = new TextView(mContext);
		machineFactory.MachineTextView(tv_mYuanbao, MATCH_PARENT, 140, 0,
				"300元宝", 32, mLinearLayout, 0, 0, 20, 0);
		tv_mYuanbao.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		// tv_mYuanbao.setGravity(Gravity.RIGHT);

		// 分割线
		TextView tv_hline = new TextView(mContext);
		machineFactory.MachineView(tv_hline, MATCH_PARENT, 1, mLinearLayout);
		tv_hline.setBackgroundColor(Color.parseColor("#d5d5d5"));

		// 金额多少
		tv_mMoney = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney, MATCH_PARENT, 140, 0, "金额:￥30",
				32, mLinearLayout, 20, 0, 0, 0);
		tv_mMoney.setGravity(Gravity.CENTER_VERTICAL);

		// TODO
		ll_leftitem.addView(tv_mYuanbao);
		ll_leftitem.addView(tv_hline);
		ll_leftitem.addView(tv_mMoney);

		

		ScrollView sv_mContent = new ScrollView(mContext);
		machineFactory.MachineView(sv_mContent, 720, MATCH_PARENT,
				mLinearLayout);

		LinearLayout ll_mContent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mContent, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_mContent.setOrientation(LinearLayout.VERTICAL);

		TextView tv_fastpay = markView("快捷支付");
		rl_mBluep = createItemView("支付宝", "yaya_zhifu.png");
		TextView tv_mBankpay = markView("银行卡支付");
		rl_mChuxuka = createItemView("储蓄卡", "yaya_chuxuka.png");
		rl_mXinyongka = createItemView("信用卡", "yaya_xinyongka.png");
		TextView tv_Chongzhika = markView("充值卡支付");
		rl_mYidong = createItemView("移动充值卡", "yaya_yidong.png");
		rl_mLiantong = createItemView("联通充值卡", "yaya_liantong.png");
		rl_mDianxin = createItemView("电信充值卡", "yaya_dianxin.png");
		rl_mShengda = createItemView("盛大充值卡", "yaya_shengda.png");
		rl_mJunka = createItemView("骏卡一网通", "yaya_junka.png");
		rl_mYaya = createItemView("Y币充值", "yaya_yayabi.png");
		rl_mQbi = createItemView("Q币充值", "yaya_qqpay.png");
		// TODO
		// ll_mContent.addView(ll_moneyitem);
		ll_mContent.addView(createLine());
		ll_mContent.addView(tv_fastpay);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mBluep);
		ll_mContent.addView(createLine());

		ll_mContent.addView(tv_mBankpay);
		ll_mContent.addView(createLine());

		ll_mContent.addView(rl_mChuxuka);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mXinyongka);
		ll_mContent.addView(createLine());

		ll_mContent.addView(tv_Chongzhika);
		ll_mContent.addView(createLine());

		ll_mContent.addView(rl_mYidong);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mLiantong);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mDianxin);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mShengda);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mJunka);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mYaya);
		ll_mContent.addView(createLine());
		ll_mContent.addView(rl_mQbi);
		ll_mContent.addView(createLine());

		sv_mContent.addView(ll_mContent);

		ll_allcontent.addView(ll_leftitem);
		ll_allcontent.addView(sv_mContent);
		baseLinearLayout.addView(rl_title);
		baseLinearLayout.addView(ll_allcontent);

		return baseLinearLayout;
	}

	private RelativeLayout createItemView(String name, String iconname) {
		RelativeLayout relativeLayout = new RelativeLayout(mContext);
		machineFactory.MachineView(relativeLayout, MATCH_PARENT, 100,
				mLinearLayout, 1, 20);

		ImageView iv_payicon = new ImageView(mContext);
		machineFactory.MachineView(iv_payicon, 60, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		iv_payicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				iconname, mActivity));

		TextView tv_bluepay = new TextView(mContext);

		machineFactory.MachineTextView(tv_bluepay, WRAP_CONTENT, MATCH_PARENT,
				0, name, 30, mRelativeLayout, 90, 0, 0, 0);
		tv_bluepay.setGravity(Gravity.CENTER_VERTICAL);

		ImageView iv_next_icon = new ImageView(mContext);
		android.widget.RelativeLayout.LayoutParams rlp = new android.widget.RelativeLayout.LayoutParams(
				machSize(40), MATCH_PARENT);
		rlp.setMargins(0, 0, machSize(30), 0);
		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
		rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		iv_next_icon.setLayoutParams(rlp);
		iv_next_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_iconnext.png", mActivity));

		// TODO
		relativeLayout.addView(iv_payicon);
		relativeLayout.addView(tv_bluepay);
		relativeLayout.addView(iv_next_icon);
		return relativeLayout;

	}

	private TextView markView(String name) {
		TextView textview = new TextView(mContext);
		machineFactory.MachineTextView(textview, MATCH_PARENT, 70, 0, name, 30,
				mLinearLayout, 20, 0, 0, 0);
		textview.setGravity(Gravity.CENTER_VERTICAL);
		return textview;
	}

	private TextView createLine() {
		// 水平分割线
		TextView tv_vline = new TextView(mContext);
		machineFactory.MachineView(tv_vline, MATCH_PARENT, 2, mLinearLayout);
		tv_vline.setBackgroundColor(Color.parseColor("#d5d5d5"));
		return tv_vline;
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

	public TextView getTv_mYuanbao() {
		return tv_mYuanbao;
	}

	public void setTv_mYuanbao(TextView tv_mYuanbao) {
		this.tv_mYuanbao = tv_mYuanbao;
	}

	public TextView getTv_mMoney() {
		return tv_mMoney;
	}

	public void setTv_mMoney(TextView tv_mMoney) {
		this.tv_mMoney = tv_mMoney;
	}

	public RelativeLayout getRl_mBluep() {
		return rl_mBluep;
	}

	public void setRl_mBluep(RelativeLayout rl_mBluep) {
		this.rl_mBluep = rl_mBluep;
	}

	public RelativeLayout getRl_mChuxuka() {
		return rl_mChuxuka;
	}

	public void setRl_mChuxuka(RelativeLayout rl_mChuxuka) {
		this.rl_mChuxuka = rl_mChuxuka;
	}

	public RelativeLayout getRl_mXinyongka() {
		return rl_mXinyongka;
	}

	public void setRl_mXinyongka(RelativeLayout rl_mXinyongka) {
		this.rl_mXinyongka = rl_mXinyongka;
	}

	public RelativeLayout getRl_mYidong() {
		return rl_mYidong;
	}

	public void setRl_mYidong(RelativeLayout rl_mYidong) {
		this.rl_mYidong = rl_mYidong;
	}

	public RelativeLayout getRl_mLiantong() {
		return rl_mLiantong;
	}

	public void setRl_mLiantong(RelativeLayout rl_mLiantong) {
		this.rl_mLiantong = rl_mLiantong;
	}

	public RelativeLayout getRl_mDianxin() {
		return rl_mDianxin;
	}

	public void setRl_mDianxin(RelativeLayout rl_mDianxin) {
		this.rl_mDianxin = rl_mDianxin;
	}

	public RelativeLayout getRl_mShengda() {
		return rl_mShengda;
	}

	public void setRl_mShengda(RelativeLayout rl_mShengda) {
		this.rl_mShengda = rl_mShengda;
	}

	public RelativeLayout getRl_mJunka() {
		return rl_mJunka;
	}

	public void setRl_mJunka(RelativeLayout rl_mJunka) {
		this.rl_mJunka = rl_mJunka;
	}

	public RelativeLayout getRl_mYaya() {
		return rl_mYaya;
	}

	public void setRl_mYaya(RelativeLayout rl_mYaya) {
		this.rl_mYaya = rl_mYaya;
	}

	public RelativeLayout getRl_mQbi() {
		return rl_mQbi;
	}

	public void setRl_mQbi(RelativeLayout rl_mQbi) {
		this.rl_mQbi = rl_mQbi;
	}

	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

	public TextView getTv_mHelp() {
		return tv_mHelp;
	}

	public void setTv_mHelp(TextView tv_mHelp) {
		this.tv_mHelp = tv_mHelp;
	}

}
