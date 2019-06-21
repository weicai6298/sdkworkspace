package com.kkgame.sdk.login;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.common.CommonData;
import com.kkgame.sdk.gfutil.LoginGF;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.LoginUtils;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Loginpo_listviewitem;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
//import com.kkgame.kkgamelib.R;

public class Login_ho_dialog extends Basedialogview {

	private LinearLayout ll_mUser;
	private ImageView iv_mUn_icon;
	private EditText et_mUn;
	private LinearLayout ll_mDown;
	private ImageView iv_mUn_down;
	private LinearLayout ll_mPassword;
	private ImageView iv_mPs_icon;
	private EditText et_mPs;
	private LinearLayout ll_mBut;
	private Button bt_mPhonelogin;
	private Button bt_mlogin;
	private ArrayList<String> mNames;
	private String mSelectUser;
	private String mPassword;
	private ListView lv_mHistoryuser;
	private String mName;
	protected static final int ERROR = 11;

	private TextView tv_fogetpassword;
	private RelativeLayout rl_fogetpassword;
	private Login_ho_dialog login_ho_dialog;

	private LinearLayout ll_weibologin;
	private ImageView iv_weibologin;
	private TextView tv_weibologin;
	private LinearLayout ll_qqlogin;
	private ImageView iv_qqlogin;
	private TextView tv_qqlogin;
	private LinearLayout ll_mPasswordicon;
	private ImageView iv_mPasswordicon;

	private boolean isdisplaypassword=false;//是否显示password
	private TextView tv_QQlogin;
	private TextView tv_register;
	private TextView tv_clouse;
	private TextView tv_contactcustomerservice;
	private RelativeLayout rl_contactcustomerservice;

	private ImageView iv_gg;
	private ImageView iv_fb;

	String qqhao;

	private TextView tv_startregister;
	private RelativeLayout rl_startregister;

	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	@Override
	public void createDialog(final Activity mActivity) {

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


		int height = 560;
		int with = 630;


		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);


		baselin.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));



		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_content, with, height, mLinearLayout, 2,
				0);
		//rl_content.setBackgroundColor(Color.WHITE);


		rl_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));


		// 中间内容
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		//ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 头部
		LinearLayout ll_title = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_title, MATCH_PARENT, 68, mLinearLayout,2,30);
		ll_title.setGravity(Gravity_CENTER);
		ll_title.setOrientation(LinearLayout.VERTICAL);

		// 头部icon
		ImageView iv_icon = new ImageView(mActivity);
		machineFactory.MachineView(iv_icon, WRAP_CONTENT, 68,
				mLinearLayout);

		//如果是sdktpye为1的话，就隐藏背景
		if (KgameSdk.sdktype==1 || KgameSdk.sdktype==3) {

		}else {
			iv_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
					"yaya1_logo.png", mActivity));		
		}

		// TODO
		ll_title.addView(iv_icon);

		// 设置username的ll 注意背景的兼容性问题
		ll_mUser = new LinearLayout(mActivity);
		ll_mUser = (LinearLayout) machineFactory.MachineView(ll_mUser,
				MATCH_PARENT, 70, 0, "LinearLayout", 35, 30, 35, 0, 100);

		ll_mUser.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

		ll_mUser.setGravity(Gravity.CENTER);

		// username 的icon
		iv_mUn_icon = new ImageView(mActivity);
		iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 35,
				35, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_username.png", mActivity));

		// username的edtext
		et_mUn = new EditText(mActivity);
		et_mUn = machineFactory.MachineEditText(et_mUn, 0, 70, 1,
				"請輸入用戶名", 24, mLinearLayout, 0, 6, 0, 0);
		et_mUn.setTextColor(Color.BLACK);
		et_mUn.setBackgroundColor(Color.TRANSPARENT);

		ll_mDown = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDown, 40, MATCH_PARENT, mLinearLayout,
				3, 10);
		ll_mDown.setGravity(Gravity_CENTER);
		ll_mDown.setClickable(true);

		// username的下拉图片
		iv_mUn_down = new ImageView(mActivity);
		iv_mUn_down = (ImageView) machineFactory.MachineView(iv_mUn_down, 30,
				30, 0, mLinearLayout, 0, 0, 5, 0, 100);
		iv_mUn_down.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_down.png", mActivity));
		iv_mUn_down.setClickable(false);

		ll_mDown.addView(iv_mUn_down);

		// TODO
		ll_mUser.addView(iv_mUn_icon);
		ll_mUser.addView(et_mUn);
		ll_mUser.addView(ll_mDown);

		// 设置password的ll 注意背景的兼容性问题
		ll_mPassword = new LinearLayout(mActivity);
		ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
				MATCH_PARENT, 70, 0, "LinearLayout",35, 30, 35, 0, 100);

		ll_mPassword
		.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png",
						mActivity));

		ll_mPassword.setGravity(Gravity.CENTER);

		// password 的icon
		iv_mPs_icon = new ImageView(mActivity);
		iv_mPs_icon = (ImageView) machineFactory.MachineView(iv_mPs_icon, 35,
				35, 0, mLinearLayout, 20, 0, 0, 0, 100);
		iv_mPs_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_password.png", mActivity));

		// password的edtext
		et_mPs = new EditText(mActivity);
		et_mPs = machineFactory.MachineEditText(et_mPs, 0, MATCH_PARENT, 1,
				"請輸入密碼", 20, mLinearLayout, 0, 6, 0, 0);
		et_mPs.setBackgroundColor(Color.TRANSPARENT);
		et_mPs.setTextColor(Color.BLACK);
		et_mPs.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		// password显示的图片icon列
		ll_mPasswordicon = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPasswordicon, 40, MATCH_PARENT, mLinearLayout,
				3, 10);
		ll_mPasswordicon.setGravity(Gravity_CENTER);
		ll_mPasswordicon.setClickable(true);

		// password显示的图片icon
		iv_mPasswordicon = new ImageView(mActivity);
		iv_mPasswordicon = (ImageView) machineFactory.MachineView(iv_mPasswordicon, 35,
				35, 0, mLinearLayout, 0, 0, 5, 0, 100);
		iv_mPasswordicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya1_passworddisplay.png", mActivity));
		iv_mPasswordicon.setClickable(false);

		ll_mPasswordicon.addView(iv_mPasswordicon);


		// TODO
		ll_mPassword.addView(iv_mPs_icon);
		ll_mPassword.addView(et_mPs);
		ll_mPassword.addView(ll_mPasswordicon);

		ll_mBut = new LinearLayout(mActivity);
		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
				MATCH_PARENT, 78, 0, mLinearLayout, 35, 30, 35, 0, 100);


		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 40,
				MATCH_PARENT, mLinearLayout);
		// button的登录按钮
		bt_mlogin = new Button(mActivity);
		bt_mlogin = machineFactory.MachineButton(bt_mlogin, 0, WRAP_CONTENT, 1,
				"立  即  登  入", 32, mLinearLayout, 0, 0, 0, 0);
		bt_mlogin.setTextColor(Color.WHITE);
		bt_mlogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya1_loginbutton.9.png", "yaya1_loginbutton.9.png",
				mActivity));
		bt_mlogin.setGravity(Gravity_CENTER);

		//		ll_mBut.addView(bt_mPhonelogin);
		//		ll_mBut.addView(ll_zhanwei);
		ll_mBut.addView(bt_mlogin);


		// 忘记密码列
		rl_fogetpassword = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_fogetpassword, MATCH_PARENT,
				WRAP_CONTENT, 0, mLinearLayout, 20, 15, 0, 25, 100);
		tv_startregister = new TextView(mActivity);
		machineFactory.MachineTextView(tv_startregister, WRAP_CONTENT,
				WRAP_CONTENT, 0, "新用戶註冊", 24, mRelativeLayout, 0, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_startregister.setTextColor(Color.RED);

		tv_fogetpassword = new TextView(mActivity);
		machineFactory.MachineTextView(tv_fogetpassword, WRAP_CONTENT,
				WRAP_CONTENT, 0, "忘記密碼?", 24, mRelativeLayout, 0, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_LEFT);
		tv_fogetpassword.setTextColor(Color.parseColor("#acacac"));

		if (KgameSdk.sdktype==3) {

		}else {
			rl_fogetpassword.addView(tv_fogetpassword);
		}
		rl_fogetpassword.addView(tv_startregister);

		RelativeLayout rl_register = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_register, MATCH_PARENT, MATCH_PARENT, 0,
				mLinearLayout, 20, 15, 20, 0, 100);

		//登陆页
		//		ll_mBut = new LinearLayout(mActivity);
		//		ll_mBut = (LinearLayout) machineFactory.MachineView(ll_mBut,
		//				MATCH_PARENT, 78, 0, mLinearLayout, 35, 30, 35, 0, 100);

		// 横版手机登录按钮
		//		bt_mPhonelogin = new Button(mActivity);
		//		bt_mPhonelogin = machineFactory.MachineButton(bt_mPhonelogin, 0,
		//				MATCH_PARENT, 1, "注册", 32, mLinearLayout, 0, 0, 0, 0);
		//		bt_mPhonelogin.setTextColor(Color.WHITE);
		//		bt_mPhonelogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
		//				"yaya1_registerbutton.9.png", "yaya1_registerbutton.9.png", mActivity));
		//		bt_mPhonelogin.setGravity(Gravity_CENTER);

		//		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		//		ll_zhanwei = (LinearLayout) machineFactory.MachineView(ll_zhanwei, 40,
		//				MATCH_PARENT, mLinearLayout);
		//		// button的登录按钮
		//		bt_mlogin = new Button(mActivity);
		//		bt_mlogin = machineFactory.MachineButton(bt_mlogin, 0, MATCH_PARENT, 1,
		//				"登录", 32, mLinearLayout, 0, 0, 0, 0);
		//		bt_mlogin.setTextColor(Color.WHITE);
		//		bt_mlogin.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
		//				"yaya1_loginbutton.9.png", "yaya1_loginbutton.9.png",
		//				mActivity));
		//		bt_mlogin.setGravity(Gravity_CENTER);

		// TODO
		//		ll_mBut.addView(bt_mPhonelogin);
		//		ll_mBut.addView(ll_zhanwei);
		//		ll_mBut.addView(bt_mlogin);


		//谷歌+Facebook
		LinearLayout ll_gg_fb = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_gg_fb, WRAP_CONTENT, 65, mLinearLayout,4,0);
		//				machineFactory.MachineView(ll_gg_fb, WRAP_CONTENT,
		//						80, 0, mLinearLayout, 0, 0, 0, 0, 100);
		//				ll_gg_fb.setGravity(Gravity_CENTER);
		//				ll_gg_fb.setOrientation(LinearLayout.VERTICAL);

		// facebook
		iv_fb = new ImageView(mActivity);
		machineFactory.MachineView(iv_fb, 60, 60,
				mLinearLayout);
		//				machineFactory.MachineView(iv_fb, 60,
		//						60, 0, mLinearLayout, 0, 0, 0, 0, 100);
		iv_fb.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"facebook.png", mActivity));		

		LinearLayout ll_kongge = new LinearLayout(mActivity);
		ll_kongge = (LinearLayout) machineFactory.MachineView(ll_kongge, 40,
				MATCH_PARENT, mLinearLayout);

		iv_gg = new ImageView(mActivity);
		machineFactory.MachineView(iv_gg, 60, 60,
				mLinearLayout);
		//				machineFactory.MachineView(iv_gg, 60,
		//						60, 0, mLinearLayout, 0, 0, 0, 0, 100);
		iv_gg.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"google.png", mActivity));		
		ll_gg_fb.addView(iv_fb);
		ll_gg_fb.addView(ll_kongge);
		ll_gg_fb.addView(iv_gg);


		// 快速登陆列
		RelativeLayout	rl_zhuce = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_zhuce, MATCH_PARENT,
				40, 0, mLinearLayout, 20, 30, 35, 0, 100);

		tv_QQlogin = new TextView(mActivity);

		machineFactory.MachineTextView(tv_QQlogin, WRAP_CONTENT,
				WRAP_CONTENT, 0, "QQ登錄>", 30, mRelativeLayout, 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_QQlogin.setTextColor(Color.parseColor("#ffa429"));

		View v_lime = new View(mActivity);
		machineFactory.MachineView(v_lime, 1, MATCH_PARENT, 0, mRelativeLayout, 0, 9, 165, 5, RelativeLayout.ALIGN_PARENT_RIGHT);
		v_lime.setBackgroundColor(Color.parseColor("#acacac"));

		tv_register = new TextView(mActivity);
		machineFactory.MachineTextView(tv_register, WRAP_CONTENT,
				WRAP_CONTENT, 0, "遊客登陸 >", 30, mRelativeLayout, 0, 0, 0, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_register.setTextColor(Color.parseColor("#ffa429"));



		//如果是sdktpye为1的话，就隐藏背景
		if (KgameSdk.sdktype==1) {

		}else {
			rl_zhuce.addView(tv_QQlogin);
		}





		//联系客服
		rl_contactcustomerservice = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_contactcustomerservice, WRAP_CONTENT,
				WRAP_CONTENT, 0, mRelativeLayout, 0, 30, 30, 0, RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_contactcustomerservice = new TextView(mActivity);

		//如果是sdktpye为1的话，就隐藏背景
		if (KgameSdk.sdktype==1) {
			String qq=DeviceUtil.getGameInfo(mActivity, "kefuqq");
			machineFactory.MachineTextView(tv_contactcustomerservice, WRAP_CONTENT,
					WRAP_CONTENT, 0, "客服qq:"+qq, 24, mRelativeLayout, 20, 0, 0, 0,
					RelativeLayout.CENTER_IN_PARENT);

			Sputils.putSPstring("service_qq", qq, mActivity);
		}else {
			machineFactory.MachineTextView(tv_contactcustomerservice, WRAP_CONTENT,
					WRAP_CONTENT, 0, "聯系客服", 24, mRelativeLayout, 20, 0, 0, 0,
					RelativeLayout.CENTER_IN_PARENT);

		}


		tv_contactcustomerservice.setTextColor(Color.parseColor("#ffa429"));
		tv_contactcustomerservice.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
		//		rl_contactcustomerservice.addView(tv_contactcustomerservice);


		ll_content.addView(ll_title);
		ll_content.addView(ll_mUser);
		ll_content.addView(ll_mPassword);
		ll_content.addView(ll_mBut);

		//如果是sdktpye为1的话，就隐藏背景
		if (KgameSdk.sdktype==1) {
//			ll_content.addView(rl_fogetpassword);
		}else {
			ll_content.addView(rl_fogetpassword);
		}

		if (KgameSdk.sdktype==3) {
			ll_content.addView(ll_gg_fb);//新增登录
		}else {

		}


		ll_content.addView(rl_zhuce);
		//ll_content.addView(rl_contactcustomerservice);



		ll_content.addView(rl_register);



		// 下拉选择历史账户
		lv_mHistoryuser = new ListView(mActivity);
		machineFactory.MachineView(lv_mHistoryuser, 700, WRAP_CONTENT, 0,
				"RelativeLayout", 20, 200, 20, 0,
				RelativeLayout.CENTER_HORIZONTAL);
		lv_mHistoryuser.setVisibility(View.GONE);

		rl_content.addView(ll_content);
		rl_content.addView(rl_contactcustomerservice);

		rl_content.addView(lv_mHistoryuser);
		// ll_content.addView(chongzhihelp2);

		baselin.addView(rl_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 1f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	@Override
	public void dialogShow() {

		super.dialogShow();

	}


	public boolean etpasswordistext=false;
	/**
	 * 界面逻辑
	 */
	private void initlogic() {

		login_ho_dialog = this;

		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {

				if (AgentApp.mUser == null) {
					if (ViewConstants.TEMPLOGIN_HO != null) {
						return;
					}
					onCancel();
				}
			}
		});

		mNames = new ArrayList<String>();
		initDBData();
		// mNames

		// 忘记密码重设秘密
		tv_fogetpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ResetPassword_ho_dialog resetPassword_ho_dialog = new ResetPassword_ho_dialog(
						mActivity);
				resetPassword_ho_dialog.dialogShow();

			}
		});

		// 下拉点击
		ll_mDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (View.GONE == lv_mHistoryuser.getVisibility()) {
					iv_mUn_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_up.png", mActivity));

					lv_mHistoryuser.setVisibility(View.VISIBLE);
				} else {
					iv_mUn_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_down.png", mActivity));
					lv_mHistoryuser.setVisibility(View.GONE);
				}

			}
		});

		// 登陆
		bt_mlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取用户名密码,实现登陆
				mName = et_mUn.getText().toString().trim();
				mPassword = et_mPs.getText().toString().trim();
				if (mName.equals("")) {
					Toast.makeText(mActivity, "用戶名不能為空", Toast.LENGTH_SHORT)
					.show();
				} else if (mName.length() < 4) {
					Toast.makeText(mActivity, "用戶名不能小於4位", Toast.LENGTH_SHORT)
					.show();
				} else if (mName.length() > 20) {
					Toast.makeText(mActivity, "用戶名不能大於20位", Toast.LENGTH_SHORT)
					.show();
				} else {
					// 输入的用户名和密码符合要求
					// TODO
					if (TextUtils.isEmpty(mPassword)) {
						Toast.makeText(mActivity, "密碼不能為空,如忘記密碼,請點擊忘記密碼~",
								Toast.LENGTH_SHORT).show();
						return;
					}

					ViewConstants.logintype=1;
					LoginUtils loginUtils = new LoginUtils(mActivity,
							login_ho_dialog, 0);
					loginUtils.login(mName, mPassword);

				}
			}
		});

		//隐藏和显示密码按钮
		ll_mPasswordicon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etpasswordistext) {
					etpasswordistext=false;
					String tempstring=et_mPs.getText().toString();
					et_mPs.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					et_mPs.setText(tempstring);
					iv_mPasswordicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
							"yaya1_passworddisplay.png", mActivity));
				}else{
					etpasswordistext=true;
					String tempstring=et_mPs.getText().toString();
					et_mPs.setInputType(InputType.TYPE_CLASS_TEXT);
					et_mPs.setText(tempstring);
					iv_mPasswordicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
							"yaya1_passwordhide.png", mActivity));
				}

			}
		});

		//联系客服
		qqhao = Sputils.getSPstring("service_qq", "暫無", mActivity);
		if (CommonData.sdkid.contains("bufan")) {

			rl_contactcustomerservice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub



					//qqhao="3003569760";
					if (qqhao.equals("4000042115")) {
						qqhao="938189213";
					}
					if (qqhao.equals("暫無")) {

					}else {
						String url="mqqwpa://im/chat?chat_type=crm&uin="+qqhao+"&version=1&src_type=web&web_src=http:://wpa.b.qq.com";

						//String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqhao;
						mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					}
				}
			});

		}else {
			tv_contactcustomerservice.setText("QQ客服："+qqhao+"(點擊復制)");
			tv_contactcustomerservice.setTextSize(machSize(10));
			rl_contactcustomerservice.setOnClickListener(new OnClickListener() {

				@SuppressLint("NewApi") @Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String qqhao1 = Sputils.getSPstring("service_qq", "暂无", mActivity);
					// 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
					ClipboardManager cm = (ClipboardManager)mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本内容放到系统剪贴板里。
					cm.setText(qqhao1);
					Toast.makeText(mActivity, "復制成功", Toast.LENGTH_LONG).show();

				}
			});

		}


		// 注册
		tv_startregister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//如果是空sdk 则没有手机注册
				if (KgameSdk.sdktype==3) {

					AcountRegister_ho_dialog acountRegister_ho_dialog = new AcountRegister_ho_dialog(
							mActivity);
					acountRegister_ho_dialog.dialogShow();
				}else{
					ViewConstants.logintype=2;
					Register_ho_dialog register_ho_dialog = new Register_ho_dialog(
							mActivity);
					register_ho_dialog.dialogShow();

				}




			}
		});

		if (CommonData.sdkid.contains("bufan")) {

		}else {
			tv_QQlogin.setVisibility(View.GONE);
		}
		tv_QQlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_qqlogin = new ImageView(mActivity);
				iv_qqlogin.setImageDrawable(GetAssetsutils
						.getDrawableFromAssetsFile("yaya_qqlogin.png",
								mActivity));
				// Log.e("舍不得离我而去", "111");
				Intent intent = new Intent(mActivity,
						BaseLogin_Activity.class);
				intent.putExtra("url",
						""+ViewConstants.QQLOGINURL);
				intent.putExtra("type", 4);
				intent.putExtra("screen", 1);
				mActivity.startActivity(intent);
				//mActivity.finish();
				dialog.dismiss();
				ViewConstants.TEMPLOGIN_HO = dialog;
			}
		});

		iv_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewConstants.TEMPLOGIN_HO = dialog;
				dialog.dismiss();
//				Toast.makeText(mActivity, "Facebook登录未完成", Toast.LENGTH_SHORT).show();
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						LoginGF.fbLogin(mActivity);
					}

			});
			}
		});

		iv_gg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewConstants.TEMPLOGIN_HO = dialog;
				dialog.dismiss();
//				Toast.makeText(mActivity, "google登录未完成", Toast.LENGTH_SHORT).show();
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						LoginGF.ggLogin(mActivity);
					}
				});
			}
		});
	}

	/**
	 * 加载数据库历史用户记录
	 */
	private void initDBData() {
		// 每次从数据库获取数据时都清空下列表,否则会有很多重复的数据
		if (mNames != null && mNames.size() > 0) {
			mNames.clear();
		}
		mNames = UserDao.getInstance(mActivity).getUsers();

		System.out.println("wuuuuuuuuuuuuu"+mNames.size()+"mna:"+mNames.toString());
		// 默认选择列表中的第一项进行输入框填充
		if (mNames != null && mNames.size() > 0) {

			mSelectUser = mNames.get(0);
			mPassword = UserDao.getInstance(mActivity).getPassword(mSelectUser);

			String secret = UserDao.getInstance(mActivity).getSecret(
					mSelectUser);

			et_mUn.setText(mSelectUser);

			// 给一个填充密码
			if (TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(secret)) {
				et_mPs.setText("yayawan-zhang");
			} else {
				et_mPs.setText(mPassword);
			}
			// et_mPs.setText(mPassword);

		}

		UserListAdapter_jf userListAdapter = new UserListAdapter_jf(mActivity,
				mNames);
		lv_mHistoryuser.setAdapter(userListAdapter);

		if (mNames.size() < 4) {
			// lv_mHistoryuser.getLayoutParams().height = mNames.size() * 55;
			if (mNames.size() == 0) {
				et_mUn.setText("");
				et_mPs.setText("");
			}
			setListviewheight(mNames.size());
		} else {
			setListviewheight(4);
		}

		lv_mHistoryuser.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Log.e("正在点击我哦", "+++++++++++");
				mSelectUser = mNames.get(position);
				mPassword = UserDao.getInstance(mActivity).getPassword(
						mSelectUser);
				String secret = UserDao.getInstance(mActivity).getSecret(
						mSelectUser);

				et_mUn.setText(mSelectUser);
				// 给一个填充密码
				if (TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(secret)) {

					et_mPs.setText("yayawan-zhang");

				} else {

					et_mPs.setText(mPassword);
				}

				lv_mHistoryuser.setVisibility(View.GONE);
			}
		});

	}

	public void setListviewheight(int size) {
		lv_mHistoryuser.getLayoutParams().height = machSize((size * 100));
	}

	public class UserListAdapter_jf extends BaseAdapter {

		private ArrayList<String> mNames;

		private Context mActivity;

		class ViewHolder {

			TextView mName;
			ImageView mDelete;
		}

		public UserListAdapter_jf(Context context, ArrayList<String> names) {
			super();
			this.mActivity = context;
			this.mNames = names;
		}

		public int getCount() {
			return mNames.size();
		}

		public Object getItem(int position) {
			return mNames.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				Loginpo_listviewitem loginpo_listviewitem = new Loginpo_listviewitem(
						mActivity);
				convertView = loginpo_listviewitem.initViewxml();
				holder.mName = (TextView) loginpo_listviewitem.getTextView();
				holder.mDelete = (ImageView) loginpo_listviewitem
						.getImageView();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String name = mNames.get(position);
			holder.mName.setText(name);

			holder.mDelete.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mNames.remove(name);
					UserDao.getInstance(mActivity).removeUser(name);
					UserListAdapter_jf.this.notifyDataSetChanged();
					if (mNames.size() == 0) {
						et_mUn.setText("");
						et_mPs.setText("");
						lv_mHistoryuser.setVisibility(View.GONE);
					}
				}
			});

			return convertView;
		}

	}

	public Login_ho_dialog(Activity activity) {
		super(activity);
	}

	/**
	 * 查询数据库获取第一个有secret的账号
	 */
	public String[] getFirstuserSecreat() {

		ArrayList<String> users = UserDao.getInstance(mContext).getUsers();

		for (int i = 0; i < users.size(); i++) {
			String secret = UserDao.getInstance(mContext).getSecret(
					users.get(i));
			if (!TextUtils.isEmpty(secret)) {

				return new String[] { secret, users.get(i) };
			}
		}
		return null;
	}

	/**
	 * 微博登陆
	 * 
	 * @param ll_forgetpassword2
	 */
	private void setontoch2(LinearLayout ll_forgetpassword2) {
		ll_forgetpassword2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					iv_weibologin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_weibologin1.png",
									mActivity));
					break;
				case MotionEvent.ACTION_UP:

					iv_weibologin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_weibologin.png",
									mActivity));

					Intent intent = new Intent(mActivity,
							BaseLogin_Activity.class);
					intent.putExtra("url",
							ViewConstants.WEIBOLOGINURL);
					intent.putExtra("type", 4);
					intent.putExtra("screen", 1);
					ViewConstants.TEMPLOGIN_HO = dialog;
					//	mActivity.finish();
					dialog.dismiss();
					mActivity.startActivity(intent);

					break;

				default:
					break;
				}
				return true;
			}
		});
	}

	/**
	 * qq登录
	 * 
	 * @param ll_forgetpassword2
	 */
	private void setontoch3(LinearLayout ll_forgetpassword2) {
		ll_forgetpassword2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					iv_qqlogin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_qqlogin1.png",
									mActivity));
					break;
				case MotionEvent.ACTION_UP:
					iv_qqlogin.setImageDrawable(GetAssetsutils
							.getDrawableFromAssetsFile("yaya_qqlogin.png",
									mActivity));
					// Log.e("舍不得离我而去", "111");
					Intent intent = new Intent(mActivity,
							BaseLogin_Activity.class);
					intent.putExtra("url",
							"https://rest.yayawan.com/web/oauth/?type=qq&forward_url=sdk");
					intent.putExtra("type", 4);
					intent.putExtra("screen", 1);
					mActivity.startActivity(intent);
					//mActivity.finish();
					dialog.dismiss();
					ViewConstants.TEMPLOGIN_HO = dialog;

					break;

				default:
					break;
				}
				return true;

			}
		});
	}


}
