package com.kkgame.sdk.xml;

import android.app.Activity;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Update_xml extends Basexml implements Layoutxml {

	private LinearLayout ll_mBut;

	private LinearLayout baselin;
	private TextView tv_message;
	private Button bt_mok;
	private Button bt_mCancel;

	private LinearLayout ll_mPre;

	private ImageButton iv_mPre;

	public Update_xml(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initViewxml() {

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 550, 450, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_content, 550, 450, mLinearLayout);
		rl_content.setBackgroundColor(Color.WHITE);

		// 标题栏
		LinearLayout rl_title = new LinearLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 76, mRelativeLayout);
		//rl_title.setBackgroundColor(Color.parseColor("#999999"));
		rl_title.setOrientation(LinearLayout.VERTICAL);
		
		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 76, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, 70, 0,
				"更新提示", 28, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.parseColor("#f8b551"));
		tv_zhuce.setGravity(Gravity_CENTER);
		
		// 注册textview
		TextView tv_line = new TextView(mContext);
		machineFactory.MachineTextView(tv_line, MATCH_PARENT, 2, 0,
				"", 28, mLinearLayout, 0, 0, 0, 0);
		tv_line.setBackgroundColor(Color.parseColor("#f8b551"));
		tv_line.setGravity(Gravity_CENTER);

		// TODO
		//rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);
		rl_title.addView(tv_line);

		ScrollView sv_content = new ScrollView(mContext);
		machineFactory.MachineView(sv_content, 550, 260, "LinearLayout");
		LinearLayout ll_message = new LinearLayout(mContext);
		machineFactory.MachineView(ll_message, 550, 260, "LinearLayout");
		ll_message.setOrientation(LinearLayout.VERTICAL);
		// 中间内容
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, 550, 450, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		tv_message = new TextView(mContext);
		machineFactory.MachineTextView(tv_message, MATCH_PARENT, 0, 1, "", 28,
				mLinearLayout, 20, 5, 10, 0);

		// but 的lin

		ll_mBut = new LinearLayout(mActivity);
		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
				MATCH_PARENT, 80, 0, mLinearLayout, 20, 20, 20, 30, 100);

		bt_mok = new Button(mActivity);
		bt_mok = machineFactory.MachineButton(bt_mok, 0, MATCH_PARENT, 1, "",
				30, mLinearLayout, 0, 0, 0, 0);
		bt_mok.setTextColor(Color.WHITE);
		bt_mok.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png", mActivity));
		bt_mok.setGravity(Gravity_CENTER);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 20,
				MATCH_PARENT, mLinearLayout);

		// button的登录按钮
		bt_mCancel = new Button(mActivity);
		
		bt_mCancel = machineFactory.MachineButton(bt_mCancel, 0, MATCH_PARENT,
				1, "", 30, mLinearLayout, 0, 0, 0, 0);
		bt_mCancel.setTextColor(Color.WHITE);
		/*bt_mCancel.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));*/
		bt_mCancel.setBackgroundColor(Color.GRAY);
		bt_mCancel.setGravity(Gravity_CENTER);

		// TODO
		ll_mBut.addView(bt_mok);
		ll_mBut.addView(ll_zhanwei);
		ll_mBut.addView(bt_mCancel);

		ll_content.addView(rl_title);
		ll_message.addView(tv_message);
		sv_content.addView(ll_message);
		ll_content.addView(sv_content);
		//ll_content.addView(tv_message);
		ll_content.addView(ll_mBut);

		//rl_content.addView(rl_title);
		
		//rl_content.addView(sv_content);
		rl_content.addView(ll_content);
		baselin.addView(rl_content);
		return baselin;
	}

	public LinearLayout getLl_mBut() {
		return ll_mBut;
	}

	public void setLl_mBut(LinearLayout ll_mBut) {
		this.ll_mBut = ll_mBut;
	}

	public LinearLayout getBaselin() {
		return baselin;
	}

	public void setBaselin(LinearLayout baselin) {
		this.baselin = baselin;
	}

	public TextView getTv_message() {
		return tv_message;
	}

	public void setTv_message(TextView tv_message) {
		this.tv_message = tv_message;
	}

	public Button getBt_mok() {
		return bt_mok;
	}

	public void setBt_mok(Button bt_mok) {
		this.bt_mok = bt_mok;
	}

	public Button getBt_mCancel() {
		return bt_mCancel;
	}

	public void setBt_mCancel(Button bt_mCancel) {
		this.bt_mCancel = bt_mCancel;
	}

}
