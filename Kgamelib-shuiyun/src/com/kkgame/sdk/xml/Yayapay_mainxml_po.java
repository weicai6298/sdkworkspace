package com.kkgame.sdk.xml;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;

@SuppressLint("NewApi")
public class Yayapay_mainxml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private TextView tv_mYuanbao;
	private TextView tv_mMoney;
	private RelativeLayout rl_mAlipay;
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
	private TextView tv_mMoney1;
	private RelativeLayout rl_mYinlianpay;
	private RelativeLayout rl_mWxpay;
	private Button bt_mMorepay;
	private GridLayout gl_mPlaylist;
	private String mpaytostring;
	private RelativeLayout rl_mWxpluin;

	public TextView getTv_mMoney1() {
		return tv_mMoney1;
	}

	public void setTv_mMoney1(TextView tv_mMoney1) {
		this.tv_mMoney1 = tv_mMoney1;
	}

	public Button getBt_mMorepay() {
		return bt_mMorepay;
	}

	public void setBt_mMorepay(Button bt_mMorepay) {
		this.bt_mMorepay = bt_mMorepay;
	}

	public GridLayout getGl_mPlaylist() {
		return gl_mPlaylist;
	}

	public void setGl_mPlaylist(GridLayout gl_mPlaylist) {
		this.gl_mPlaylist = gl_mPlaylist;
	}

	public String getMpaytostring() {
		return mpaytostring;
	}

	public void setMpaytostring(String mpaytostring) {
		this.mpaytostring = mpaytostring;
	}

	public RelativeLayout getRl_mWxpluin() {
		return rl_mWxpluin;
	}

	public void setRl_mWxpluin(RelativeLayout rl_mWxpluin) {
		this.rl_mWxpluin = rl_mWxpluin;
	}

	public Yayapay_mainxml_po(Activity activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {
		// long currentTimeMillis = System.currentTimeMillis();

		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);

		machineFactory.MachineView(baseLinearLayout,
				ViewConstants.getHoldActivityWith(mContext),
				ViewConstants.getHoldActivityHeight(mContext), "LinearLayout");
		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

		// 设置长度需要baseliner和relative两个设置
		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);

		// ViewConstants.getHoldActivityWith(mContext)全局的所有窗口化activity的宽

		machineFactory.MachineView(rl_title,
				ViewConstants.getHoldActivityWith(mContext), 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#3385FF"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_RIGHT);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 45, 45, 0, mLinearLayout, 0, 0, 0,
				0, 0);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_cancel_icon.png", mActivity));

		ll_mPre.addView(iv_mPre);
		iv_mPre.setClickable(false);

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"充值", 40, mRelativeLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.parseColor("#ffffff"));
		tv_zhuce.setGravity(Gravity_CENTER);

		tv_mHelp = new TextView(mContext);
		machineFactory.MachineTextView(tv_mHelp, WRAP_CONTENT, MATCH_PARENT, 0,
				"帮助", 28, mRelativeLayout, 20, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);
		tv_mHelp.setTextColor(Color.parseColor("#fffffa"));
		tv_mHelp.setGravity(Gravity_CENTER);
		tv_mHelp.setClickable(true);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		//rl_title.addView(tv_mHelp);

		ScrollView sv_mContent = new ScrollView(mContext);
		machineFactory.MachineView(sv_mContent, MATCH_PARENT,
				ViewConstants.getHoldActivityHeight(mContext) - 96,
				mLinearLayout);

		LinearLayout ll_mContent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mContent, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_mContent.setOrientation(LinearLayout.VERTICAL);
		ll_mContent.setGravity(Gravity_CENTER);

		// 顶部金额的条目
		LinearLayout ll_moneyitem = new LinearLayout(mContext);
		machineFactory.MachineView(ll_moneyitem, MATCH_PARENT, 100,
				mLinearLayout);
		ll_moneyitem.setOrientation(LinearLayout.HORIZONTAL);

		// 多少元宝
		tv_mYuanbao = new TextView(mContext);
		machineFactory.MachineTextView(tv_mYuanbao, 0, MATCH_PARENT, 1,
				"300元宝", 32, mLinearLayout, 0, 0, 20, 0);
		tv_mYuanbao.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		// tv_mYuanbao.setGravity(Gravity.RIGHT);

		// 分割线
		TextView tv_hline = new TextView(mContext);
		machineFactory.MachineView(tv_hline, 2, MATCH_PARENT, mLinearLayout);
		tv_hline.setBackgroundColor(Color.parseColor("#d5d5d5"));

		// 金额多少
		tv_mMoney1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney1, WRAP_CONTENT, MATCH_PARENT,
				0, "金额:￥", 32, mLinearLayout, 20, 0, 0, 0);
		tv_mMoney1.setGravity(Gravity.CENTER_VERTICAL);

		tv_mMoney = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney, 0, MATCH_PARENT, 1, "", 32,
				mLinearLayout, 0, 0, 0, 0);
		tv_mMoney.setGravity(Gravity.CENTER_VERTICAL);
		tv_mMoney.setTextColor(Color.parseColor("#ff9900"));

		// TODO
		ll_moneyitem.addView(tv_mYuanbao);
		ll_moneyitem.addView(tv_hline);
		ll_moneyitem.addView(tv_mMoney1);
		ll_moneyitem.addView(tv_mMoney);

		TextView tv_fastpay = markView("请选择支付方式：如支付失败，请换种支付方式("+ViewConstants.SDKVERSION+"):");

		// 创建每种支付的布局
		rl_mAlipay = createItemView("支付宝支付", "yaya_zhifu.png",
				DeviceUtil.ALIPAYMSPCODE);
		rl_mYinlianpay = createItemView("银联卡支付", "yaya_yinlian.png",
				DeviceUtil.YINLIAN);

		rl_mChuxuka = createItemView("储蓄卡支付", "yaya_chuxuka.png",
				DeviceUtil.YIBAOCODE);
		rl_mXinyongka = createItemView("信用卡支付", "yaya_xinyongka.png",
				DeviceUtil.YIBAOCODE);
		rl_mYidong = createItemView("充值卡支付", "yaya_yidong.png",
				DeviceUtil.YIDONGCODE);

		rl_mLiantong = createItemView("联通充值卡", "yaya_liantong.png",
				DeviceUtil.LIANTONGCODE);
		rl_mDianxin = createItemView("电信充值卡", "yaya_dianxin.png",
				DeviceUtil.DIANXINCODE);
		rl_mShengda = createItemView("盛大充值卡", "yaya_shengda.png",
				DeviceUtil.SHENGDACODE);

		rl_mJunka = createItemView("骏卡一网通", "yaya_junka.png",
				DeviceUtil.JUNWANGCODE);
		rl_mYaya = createItemView("丫丫币充值", "yaya_yayabi.png",
				DeviceUtil.YAYABICODE);
		rl_mQbi = createItemView("QQ卡充值", "yaya_qqpay.png", DeviceUtil.QQCODE);

		rl_mWxpay = createItemView("微信支付", "yaya_weixinpay.png",
				DeviceUtil.WXPAYCODE);

		rl_mWxpluin = createItemView("安全微支付", "yaya_weixinpay.png",
				DeviceUtil.YAYAWANWEIXINPLUIN);
		// TODO
		ll_mContent.addView(ll_moneyitem);
		ll_mContent.addView(createLine());
		ll_mContent.addView(tv_fastpay);

		gl_mPlaylist = new GridLayout(mContext);
		machineFactory.MachineView(gl_mPlaylist, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		gl_mPlaylist.setOrientation(GridLayout.VERTICAL);
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			gl_mPlaylist.setColumnCount(3);
		} else if ("portrait".equals(orientation)) {
			gl_mPlaylist.setColumnCount(2);
		}

		gl_mPlaylist.setPadding(10, 10, 10, 15);

		// 这里获取初始化的时候得到的可以开启的支付类型
		// AgentApp.mPayMethods = DeviceUtil.getYayaWanMethod(mContext);
		//
		// mpaytostring = AgentApp.mPayMethods.toString();
		// 如果支付类型存在，则加入到列表中
		Yayalog.loger("初始化得到的支付方式有：" + mpaytostring);

		gl_mPlaylist.addView(rl_mAlipay);

		gl_mPlaylist.addView(rl_mWxpay);

		
		gl_mPlaylist.addView(rl_mYinlianpay);
		/*
		 * if (mpaytostring.contains("yaya_qq")) {
		 * gl_mPlaylist.addView(rl_mQbi); }
		 * 
		 * if (mpaytostring.contains("yaya_yinlian")) {
		 * gl_mPlaylist.addView(rl_mYinlianpay); }
		 * 
		 * if (mpaytostring.contains("yaya_visa")) {
		 * gl_mPlaylist.addView(rl_mXinyongka); }
		 * 
		 * if (mpaytostring.contains("yaya_cash")) {
		 * gl_mPlaylist.addView(rl_mChuxuka); }
		 */

		// button更多支付按钮
		/*
		 * bt_mMorepay = new Button(mActivity); bt_mMorepay =
		 * machineFactory.MachineButton(bt_mMorepay, 300, 76, 1, "更多支付方式", 30,
		 * mLinearLayout, 0, 10, 0, 0); bt_mMorepay.setTextColor(Color.WHITE);
		 * bt_mMorepay.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
		 * "yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png", mActivity));
		 * bt_mMorepay.setGravity(Gravity_CENTER);
		 * 
		 * bt_mMorepay.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * if (mpaytostring.contains("yaya_liantong")) {
		 * gl_mPlaylist.addView(rl_mLiantong); }
		 * 
		 * if (mpaytostring.contains("yaya_dianxin")) {
		 * gl_mPlaylist.addView(rl_mDianxin); }
		 * 
		 * if (mpaytostring.contains("yaya_yidong")) {
		 * gl_mPlaylist.addView(rl_mYidong); } if
		 * (mpaytostring.contains("yaya_junwang")) {
		 * gl_mPlaylist.addView(rl_mJunka); }
		 * 
		 * if (mpaytostring.contains("yaya_shengda")) {
		 * gl_mPlaylist.addView(rl_mShengda); } if
		 * (mpaytostring.contains("yaya_wxpluin")) {
		 * gl_mPlaylist.addView(rl_mWxpluin); }
		 * bt_mMorepay.setVisibility(View.GONE); } });
		 */

		ll_mContent.addView(gl_mPlaylist);
		//ll_mContent.addView(bt_mMorepay);

		sv_mContent.addView(ll_mContent);

		baseLinearLayout.addView(rl_title);
		baseLinearLayout.addView(sv_mContent);
		// Yayalog.loger("启动时间："+(System.currentTimeMillis()-currentTimeMillis));
		return baseLinearLayout;
	}

	public RelativeLayout getRl_mWxpay() {
		return rl_mWxpay;
	}

	public void setRl_mWxpay(RelativeLayout rl_mWxpay) {
		this.rl_mWxpay = rl_mWxpay;
	}

	/**
	 * 创建一个支付item布局
	 * 
	 * @param name
	 *            支付名字
	 * @param iconname
	 *            icon名字
	 * @param paytype
	 *            支付方式id 用来判断是否九五折
	 * @return
	 */
	private RelativeLayout createItemView(String name, String iconname,
			int paytype) {
		RelativeLayout relativeLayout = new RelativeLayout(mContext);

		machineFactory.MachineView(relativeLayout, 300, 100, 0, "GridLayout",
				15, 15, 0, 0, 0);
		relativeLayout.setGravity(Gravity_CENTER);

		ImageView iv_payicon = new ImageView(mContext);
		machineFactory.MachineView(iv_payicon, 60, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_LEFT);
		iv_payicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				iconname, mActivity));

		TextView tv_alipay = new TextView(mContext);
	
		name = name + "      ";
		machineFactory.MachineTextView(tv_alipay, WRAP_CONTENT, MATCH_PARENT,
				0, name, 22, mRelativeLayout, 90, 0, 0, 0);
		

		tv_alipay.setGravity(Gravity.CENTER_VERTICAL);

		// TODO
		relativeLayout.addView(iv_payicon);
		relativeLayout.addView(tv_alipay);

		relativeLayout.setBackground(GetAssetsutils.get9DrawableFromAssetsFile(
				"yaya_paynormal_bg.9.png", mContext));

		return relativeLayout;

	}

	private TextView markView(String name) {
		TextView textview = new TextView(mContext);
		machineFactory.MachineTextView(textview, MATCH_PARENT, 70, 0, name, 20,
				mLinearLayout, 20, 0, 0, 0);
		textview.setTextColor(Color.parseColor("#737373"));
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

	public RelativeLayout getRl_mAlipay() {
		return rl_mAlipay;
	}

	public void setRl_mAlipay(RelativeLayout rl_mAlipay) {
		this.rl_mAlipay = rl_mAlipay;
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

	public RelativeLayout getRl_mYinlianpay() {
		return rl_mYinlianpay;
	}

	public void setRl_mYinlianpay(RelativeLayout rl_mYinlianpay) {
		this.rl_mYinlianpay = rl_mYinlianpay;
	}

}
