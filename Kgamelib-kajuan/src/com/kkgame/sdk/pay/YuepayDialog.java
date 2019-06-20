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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;

public class YuepayDialog extends Basedialogview {

	private LinearLayout ll_mHelp;
	private LinearLayout ll_mDele;
	private TextView tv_mYue;
	private TextView tv_mTip;
	private Button bt_mOk;
	private TextView tv_mZhifu;

	public YuepayDialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 550, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 550, "LinearLayout", 2, 25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 删除行
		LinearLayout ll_deleline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_deleline, MATCH_PARENT, 50, 0,
				mLinearLayout, 8, 20, 10, 0, 100);
		ll_deleline.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
				mLinearLayout);

		ll_mHelp = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mHelp, 70, MATCH_PARENT, mLinearLayout);
		ll_mHelp.setGravity(Gravity_CENTER);

		ImageView iv_help = new ImageView(mActivity);
		machineFactory.MachineView(iv_help, 50, 50, mLinearLayout, 3, 0);
		iv_help.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_help.png", mActivity));

		// TODO
		ll_mHelp.addView(iv_help);

		ll_mDele = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDele, 60, MATCH_PARENT, mLinearLayout);
		ll_mDele.setGravity(Gravity_CENTER);
		ll_mDele.setClickable(true);

		ImageView iv_dele = new ImageView(mActivity);
		machineFactory.MachineView(iv_dele, 40, 40, mLinearLayout);
		iv_dele.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_leftpre.png", mActivity));

		iv_dele.setClickable(false);

		ll_mDele.addView(iv_dele);
		ll_mDele.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogDismiss();
			}
		});

		// TODO
		ll_deleline.addView(ll_mDele);
		ll_deleline.addView(ll_zhanwei);
	//	ll_deleline.addView(ll_mHelp);

		LinearLayout ll_yue = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_yue, MATCH_PARENT, 100, 0, mLinearLayout,
				20, 10, 20, 0, 100);
		ll_yue.setBackgroundDrawable(GetAssetsutils.get9DrawableFromAssetsFile(
				"yaya_biankuang.9.png", mContext));

		TextView et_yue = new TextView(mActivity);
		machineFactory.MachineTextView(et_yue, 0, MATCH_PARENT, 1, "可支付余额:",
				28, mLinearLayout, 6, 0, 0, 0);
		et_yue.setTextColor(Color.GRAY);
		et_yue.setGravity(Gravity.CENTER_VERTICAL);

		// 钱字符
		TextView tv_yue1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_yue1, WRAP_CONTENT, MATCH_PARENT, 0,
				"￥", 32, mLinearLayout, 0, 0, 0, 0);
		tv_yue1.setTextColor(Color.GRAY);
		tv_yue1.setGravity(Gravity_CENTER);

		// 余额
		tv_mYue = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mYue, WRAP_CONTENT, MATCH_PARENT, 0,
				"80", 32, mLinearLayout, 0, 0, 6, 0);
		tv_mYue.setTextColor(Color.parseColor("#ff9900"));
		tv_mYue.setGravity(Gravity_CENTER);

		// TODO
		ll_yue.addView(et_yue);
		ll_yue.addView(tv_yue1);
		ll_yue.addView(tv_mYue);

		LinearLayout ll_zhifu = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_zhifu, MATCH_PARENT, 100, 0,
				mLinearLayout, 20, 0, 20, 0, 100);
		ll_zhifu.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang.9.png", mContext));

		TextView et_zhifu = new TextView(mActivity);
		machineFactory.MachineTextView(et_zhifu, 0, MATCH_PARENT, 1, "本次支付金额:",
				28, mLinearLayout, 6, 0, 0, 0);
		et_zhifu.setTextColor(Color.GRAY);
		et_zhifu.setGravity(Gravity.CENTER_VERTICAL);

		// 钱字符
		TextView tv_zhifu = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhifu, WRAP_CONTENT, MATCH_PARENT, 0,
				"￥", 32, mLinearLayout, 0, 0, 0, 0);
		tv_zhifu.setTextColor(Color.GRAY);
		tv_zhifu.setGravity(Gravity_CENTER);

		// 余额
		tv_mZhifu = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mZhifu, WRAP_CONTENT, MATCH_PARENT,
				0, "80", 32, mLinearLayout, 0, 0, 6, 0);
		tv_mZhifu.setTextColor(Color.parseColor("#ff9900"));
		tv_mZhifu.setGravity(Gravity_CENTER);

		// TODO
		ll_zhifu.addView(et_zhifu);
		ll_zhifu.addView(tv_zhifu);
		ll_zhifu.addView(tv_mZhifu);

		LinearLayout ll_tip = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_tip, MATCH_PARENT, 50, mLinearLayout);

		TextView tv_tip1 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip1, WRAP_CONTENT, MATCH_PARENT, 0,
				"如余额不足.可通过", 22, mLinearLayout, 26, 0, 0, 0);
		tv_tip1.setTextColor(Color.GRAY);
		tv_tip1.setGravity(Gravity_CENTER);

		tv_mTip = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mTip, WRAP_CONTENT, MATCH_PARENT, 0,
				"http://www.yayawan.com", 22, mLinearLayout, 0, 0, 0, 0);
		tv_mTip.setTextColor(Color.parseColor("#1888d7"));
		tv_mTip.setGravity(Gravity_CENTER);

		TextView tv_tip2 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip2, WRAP_CONTENT, MATCH_PARENT, 0,
				"充值", 22, mLinearLayout, 0, 0, 0, 0);
		tv_tip2.setTextColor(Color.GRAY);
		tv_tip2.setGravity(Gravity_CENTER);

		// TODO
		ll_tip.addView(tv_tip1);
		ll_tip.addView(tv_mTip);
		ll_tip.addView(tv_tip2);

		// button的确认
		bt_mOk = new Button(mContext);
		bt_mOk = machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 90, 0,
				"确认", 30, mLinearLayout, 20, 70, 20, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		ll_content.addView(ll_deleline);
		ll_content.addView(ll_yue);
		ll_content.addView(ll_zhifu);

		ll_content.addView(ll_tip);
		ll_content.addView(bt_mOk);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		// dialog.
		// dialog.addContentView(view, params);
		// dialog.setTitle("Custom Dialog");

		/*
		 * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
		 * 对象,这样这可以以同样的方式改变这个Activity的属性.
		 */
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		/*
		 * lp.x与lp.y表示相对于原始位置的偏移.
		 * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
		 * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
		 * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
		 * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
		 * 当参数值包含Gravity.CENTER_HORIZONTAL时
		 * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
		 * 当参数值包含Gravity.CENTER_VERTICAL时
		 * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
		 * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
		 * Gravity.CENTER_VERTICAL.
		 * 
		 * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
		 * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了, Gravity.LEFT, Gravity.TOP,
		 * Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
		 */
		/*
		 * lp.x = 200; // 新位置X坐标 lp.y = 200; // 新位置Y坐标 lp.width = 600; // 宽度
		 * lp.height = 600; // 高度
		 */
		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// dialog.onWindowAttributesChanged(lp);
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);
		/*
		 * 将对话框的大小按屏幕大小的百分比设置
		 */
		// WindowManager m = getWindowManager();
		// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		// WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
		// p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
		// dialogWindow.setAttributes(p);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		// ap2.addRule(RelativeLayout.BELOW, );
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		// dialog.show();

	}

	public LinearLayout getLl_mHelp() {
		return ll_mHelp;
	}

	public void setLl_mHelp(LinearLayout ll_mHelp) {
		this.ll_mHelp = ll_mHelp;
	}

	public LinearLayout getLl_mDele() {
		return ll_mDele;
	}

	public void setLl_mDele(LinearLayout ll_mDele) {
		this.ll_mDele = ll_mDele;
	}

	public TextView getTv_mYue() {
		return tv_mYue;
	}

	public void setTv_mYue(TextView tv_mYue) {
		this.tv_mYue = tv_mYue;
	}

	public TextView getTv_mTip() {
		return tv_mTip;
	}

	public void setTv_mTip(TextView tv_mTip) {
		this.tv_mTip = tv_mTip;
	}

	public Button getBt_mOk() {
		return bt_mOk;
	}

	public void setBt_mOk(Button bt_mOk) {
		this.bt_mOk = bt_mOk;
	}

	public TextView getTv_mZhifu() {
		return tv_mZhifu;
	}

	public void setTv_mZhifu(TextView tv_mZhifu) {
		this.tv_mZhifu = tv_mZhifu;
	}

}
