package com.kkgame.sdk.smallhelp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.Result;
import com.kkgame.sdk.utils.AuthNumReceiver;
import com.kkgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CodeCountDown;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;



public class Bindphone_dialog_ho extends Basedialogview {

	protected static final int GETAUTHCODE = 3;

	protected static final int BINDPHONERESULT = 4;

	protected static final int AUTOAUTH = 5;

	private static final int AUTHCODE = 6;

	private static final int COUNTDOWN = 7;

	private LinearLayout ll_mHelp;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private Button bt_mOk;
	private EditText mPhone;
	private EditText mAuthNumber;
	private Button mAuthNumber_btn;
	private Button mSubmit;
	private String mPhoneText;
	private Result mResult;
	private CodeCountDown mCodeCountDown;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			case GETAUTHCODE:
				if (mResult.success == 0) {

				}
				break;
			case BINDPHONERESULT:
				//System.out.println(mResult.success);
				if (mResult.success == 0) {
					tv_bindtext.setText("已绑定");
					ll_bindphone.setClickable(false);
					Toast.makeText(mContext, mResult.body, Toast.LENGTH_SHORT)
							.show();
					dialog.dismiss();
				} else if (mResult.success == 1) {
					tv_bindtext.setText("已绑定");
					ll_bindphone.setClickable(false);
					Toast.makeText(mContext, mResult.body, Toast.LENGTH_SHORT)
							.show();
					dialog.dismiss();
				} else {
					Toast.makeText(mContext, mResult.body, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			// case AUTOAUTH:
			// mAuthNumber.setText(AgentApp.mAuthNum);
			// break;
			case AUTHCODE:
				// String result = (String) msg.obj;
				mAuthNumber_btn.setEnabled(false);
				Toast.makeText(mContext, mResult.body, Toast.LENGTH_LONG)
						.show();

				if (mCodeCountDown == null) {
					mCodeCountDown =  new CodeCountDown(60000,1000,
							mAuthNumber_btn);
				}
				mCodeCountDown.start();
				break;
			default:
				break;
			}
		}

	};

	private LinearLayout ll_mPre;

	private ImageButton iv_mPre;
	private TextView tv_bindtext;
	private LinearLayout ll_bindphone;

	private AuthNumReceiver mAuthNumReceiver;

	public Bindphone_dialog_ho(Activity activity) {
		super(activity);
	}

	public Bindphone_dialog_ho(Activity activity, TextView tv_bindtext,
			LinearLayout ll_bindphone) {
		super(activity);
		this.tv_bindtext = tv_bindtext;
		this.ll_bindphone = ll_bindphone;
		initlog();
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 700;

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

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, with, height, mLinearLayout, 2,
				25);
		rl_content.setBackgroundColor(Color.WHITE);
		// rl_content.setGravity(Gravity.CENTER_HORIZONTAL);
		// rl_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, 0,
				mRelativeLayout, 0, 0, 0, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 80, mLinearLayout);
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
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"绑定手机", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mContext);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, 0, mLinearLayout, 20, 50, 20, 0, 100);

		// 手机号码输入框
		et_mPhone = new EditText(mContext);
		machineFactory.MachineEditText(et_mPhone, 360, MATCH_PARENT, 0,
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
				"请输入验证码", 32, mLinearLayout, 20, 20, 20, 0);
		et_mSecurity.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang2.9.png", mContext));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 确定按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 20, 50, 20, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO

		ll_content.addView(rl_title);
		ll_content.addView(ll_phone);
		ll_content.addView(et_mSecurity);
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

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		dialog.setCanceledOnTouchOutside(true);

	}

	private void initlog() {

		onStart();

		mPhone = et_mPhone;
		mAuthNumber = et_mSecurity;
		mAuthNumber_btn = bt_mGetsecurity;
		mSubmit = bt_mOk;

		bt_mGetsecurity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhoneText = mPhone.getText().toString().trim();
				if (mPhoneText.length() == 0) {
					Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneText.length() != 11) {
					Toast.makeText(mContext, "手机号必须是11位", Toast.LENGTH_SHORT)
							.show();
				} else {

					Utilsjf.creDialogpro(mActivity, "正在获取验证码...");
				/*	new Thread() {

						@Override
						public void run() {
							try {
								mResult = ObtainData.sendSms(mContext,
										AgentApp.mUser, mPhoneText);
								mHandler.sendEmptyMessage(AUTHCODE);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();*/
				}
			}
		});

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String authNum = mAuthNumber.getText().toString().trim();
				mPhoneText = mPhone.getText().toString().trim();
				if (authNum.length() == 0) {
					Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneText.length() == 0) {
					Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 进入绑定流程

					Utilsjf.creDialogpro(mActivity, "正在绑定手机...");
					/*new Thread() {
						@Override
						public void run() {
							try {
								mResult = ObtainData.bindPhone(mContext,
										AgentApp.mUser, mPhoneText, authNum);
								mHandler.sendEmptyMessage(BINDPHONERESULT);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}.start();*/
				}
			}
		});

	}

	/**
	 * 注册接收短信广播
	 */
	public void onStart() {
		// 生成广播处理
		mAuthNumReceiver = new AuthNumReceiver();

		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(Integer.MAX_VALUE);
		// 注册广播
		mActivity.registerReceiver(mAuthNumReceiver, intentFilter);

		mAuthNumReceiver.setOnReceivedMessageListener(new MessageListener() {

			@Override
			public void onReceived(String message) {
				// System.out.println("接收到的短信+++++++"+message);
				et_mSecurity.setText(message);
			}
		});
		// super.onStart();
	}

}
