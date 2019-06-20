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
import com.kkgame.utils.DeviceUtil;



public class ResetPassword_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private EditText et_mNewpassword;
	private Button bt_mOk;
	private String mUserName;
	private String mCode;

	protected static final int MODIFYPASSRESULT = 4;
	private boolean flag;
	private CodeCountDown mCodeCountDown;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			case MODIFYPASSRESULT:
				if (mResult.success == 0) {
					// 修改成功
					Toast.makeText(mContext, mResult.body, Toast.LENGTH_SHORT)
							.show();
					// 将base64加密的用户信息保存到数据库
					//UserDao.getInstance(mContext).writeUser(
					//		AgentApp.mUser.userName, mNewPasswordText, "");
					dialog.dismiss();
				} else {
					dialog.dismiss();
					Toast.makeText(mContext, mResult.body, Toast.LENGTH_SHORT)
							.show();
				}
				break;

			default:
				break;
			}
		}

	};

	private AuthNumReceiver mAuthNumReceiver;
	private EditText mOldPassword;
	private EditText mNewPassword;
	private EditText mNewPasswordRe;
	private TextView tv_fogetpassword;

	public ResetPassword_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {

		onStart();

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

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

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout", 2,
				25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mActivity);
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
		TextView tv_zhuce = new TextView(mActivity);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"密码重设", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		
		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mActivity);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				660, MATCH_PARENT, 0, mLinearLayout, 0, 40, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		
		tv_fogetpassword = new TextView(mActivity);
		machineFactory.MachineTextView(tv_fogetpassword, WRAP_CONTENT,
				WRAP_CONTENT, 0, "快速登录初始密码保存在sd"
						+ "卡中，文件名为:账号.jpg", 22, mRelativeLayout, 0, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_fogetpassword.setTextColor(Color.RED);
		ll_content1.addView(tv_fogetpassword);
		
		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout);

		// 手机号码输入框
		et_mPhone = new EditText(mActivity);
		machineFactory.MachineEditText(et_mPhone, MATCH_PARENT, MATCH_PARENT,
				0, "请输原密码", 32, mLinearLayout, 0, 0, 0, 0);
		et_mPhone
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mPhone.setPadding(machSize(20), 0, 0, 0);

		// TODO
		ll_phone.addView(et_mPhone);

		// 验证码输入框
		et_mSecurity = new EditText(mActivity);
		machineFactory.MachineEditText(et_mSecurity, MATCH_PARENT, 96, 0,
				"请输入密码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mSecurity
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 验证码输入框
		et_mNewpassword = new EditText(mActivity);
		machineFactory.MachineEditText(et_mNewpassword, MATCH_PARENT, 96, 0,
				"请再次输入密码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mNewpassword
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mNewpassword.setPadding(machSize(20), 0, 0, 0);

		// 确定按钮
		bt_mOk = new Button(mActivity);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 30, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_content1.addView(ll_phone);
		ll_content1.addView(et_mSecurity);
		ll_content1.addView(et_mNewpassword);

		ll_content1.addView(bt_mOk);

		ll_content.addView(rl_title);

		ll_content.addView(ll_content1);

		baselin.addView(ll_content);

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

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private String mOldPasswordText;
	private String mNewPasswordText;
	private String mNewPasswordReText;
	private Result mResult;

	private void initlogic() {
		mOldPassword = et_mPhone;
		mNewPassword = et_mSecurity;
		mNewPasswordRe = et_mNewpassword;

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOldPasswordText = mOldPassword.getText().toString().trim();
				mNewPasswordText = mNewPassword.getText().toString().trim();
				mNewPasswordReText = mNewPasswordRe.getText().toString().trim();

				if ("".equals(mOldPasswordText)) {
					Toast.makeText(mContext, "请输入原密码", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mNewPasswordText)) {
					Toast.makeText(mContext, "请输入新密码", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mNewPasswordReText)) {
					Toast.makeText(mContext, "请输入确认新密码", Toast.LENGTH_SHORT)
							.show();
				} else if (mOldPasswordText.length() < 6) {
					Toast.makeText(mContext, "原密码不能小于6位", Toast.LENGTH_SHORT)
							.show();
				} else if (mOldPasswordText.length() > 20) {
					Toast.makeText(mContext, "原密码不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if (mNewPasswordReText.length() < 6) {
					Toast.makeText(mContext, "新密码不能小于6位", Toast.LENGTH_SHORT)
							.show();
				} else if (mNewPasswordReText.length() > 20) {
					Toast.makeText(mContext, "新密码不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if (mOldPasswordText.equals(mNewPasswordText)) {
					Toast.makeText(mContext, "新密码与原密码不能一样", Toast.LENGTH_SHORT)
							.show();
				} else if (!mNewPasswordText.equals(mNewPasswordReText)) {
					Toast.makeText(mContext, "两次密码输入不一样", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 进入修改流程
					Utilsjf.creDialogpro(mActivity, "正在修改密码....");
					/*new Thread() {

						@Override
						public void run() {
							try {
								mResult = ObtainData.modifyPassword(mContext,
									AgentApp.mUser, mOldPasswordText,
										mNewPasswordText);
								mHandler.sendEmptyMessage(MODIFYPASSRESULT);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}.start();*/
				}
			}
		});

	}

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
				et_mSecurity.setText(message);
			}
		});

	}

}
