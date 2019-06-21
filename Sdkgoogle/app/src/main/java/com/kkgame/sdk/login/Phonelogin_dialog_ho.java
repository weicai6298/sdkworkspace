package com.kkgame.sdk.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
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
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.AuthNumReceiver;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CodeCountDown;
import com.kkgame.sdk.utils.CounterDown;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;



public class Phonelogin_dialog_ho extends Basedialogview {

	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private Button bt_mOk;
	private String mPhoneNum;
	private String mCode;
	private static final int AUTHCODE = 5;
	protected static final int ERROR = 11;
	protected static final int LOGINSECURITYRESULT = 8;

	private Handler mHandler = new Handler() {

		private CodeCountDown mCodeCountDown;

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			// TODO
			Utilsjf.stopDialog();
			switch (msg.what) {

			case AUTHCODE:
				Result loginResult = (Result) msg.obj;
				Toast.makeText(mContext, loginResult.body, Toast.LENGTH_LONG)
						.show();
				// 重新获取验证码倒计时
				/*if (mCodeCountDown == null) {
					mCodeCountDown = new CodeCountDown(60000,1000,
							bt_mGetsecurity);
				}*/
				//mCodeCountDown.start();
				mCountDown.startCounter();
				break;

			case LOGINSECURITYRESULT:
				User loginuser = (User) msg.obj;
				if (loginuser.success == 0) {
					AgentApp.mUser = loginuser;
					// 将base64加密的用户信息保存到数据库
					UserDao.getInstance(mContext).writeUser(loginuser.userName,
							loginuser.password, loginuser.secret);
					loginuser.password = "";
					// 开启悬浮窗服务
					//KgameSdk.init(mActivity);

					allDismiss();
					
					Login_success_dialog login_success_dialog = new Login_success_dialog(
							mActivity);
					login_success_dialog.dialogShow();
				} else if (loginuser.success == 1) {

				} else if (loginuser.success == 2) {
					AgentApp.mUser = loginuser;
					// 将base64加密的用户信息保存到数据库
					UserDao.getInstance(mContext).writeUser(loginuser.userName,
							loginuser.password, loginuser.secret);
					loginuser.password = "";
					// 开启悬浮窗服务
					//KgameSdk.init(mActivity);
					allDismiss();
					Login_success_dialog login_success_dialog = new Login_success_dialog(
							mActivity);
					login_success_dialog.dialogShow();
				}
				Toast.makeText(mContext, loginuser.body, Toast.LENGTH_SHORT)
						.show();

				break;
			case ERROR:
				Toast.makeText(mContext, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
		}

	};
	private KgameSdkUserCallback mUserCallback;
	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private AuthNumReceiver mAuthNumReceiver;
	private IntentFilter filter2;
	private BroadcastReceiver smsReceiver;
	private CounterDown mCountDown;

	public Phonelogin_dialog_ho(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 650;

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

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, with, height, mLinearLayout,2,25);
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
				"手机登陆", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mContext);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, 0, mLinearLayout, 20, 40, 20, 0, 100);

		// 手机号码输入框
		et_mPhone = new EditText(mContext);
		machineFactory.MachineEditText(et_mPhone, 360, MATCH_PARENT, 0,
				"请输入手机号", 32, mLinearLayout, 0, 0, 0, 0);
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

		// 验证码输入框
		et_mSecurity = new EditText(mContext);
		machineFactory.MachineEditText(et_mSecurity, MATCH_PARENT, 96, 0,
				"请输入验证码", 32, mLinearLayout, 20, 30, 20, 0);
		et_mSecurity.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mContext));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 确定按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 20, 70, 20, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		ll_content.addView(rl_title);
		// ll_content.addView(ll_deleline);
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

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		dialog.setCanceledOnTouchOutside(true);
		initlog();

	}

	private void initlog() {
		// System.out.println("我进来那短信了");
		// 设置短信填充
		onStart();

		mUserCallback = KgameSdk.mUserCallback;

		mCountDown = CounterDown.getInstance(mActivity);
		mCountDown.setView(bt_mGetsecurity);
		// 获取验证码
		bt_mGetsecurity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mPhoneNum = et_mPhone.getText().toString().trim();
				if (mPhoneNum.equals("")) {
					Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNum.length() < 11) {
					Toast.makeText(mContext, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在获取验证码...");
				/*	new Thread() {
						@Override
						public void run() {
							try {
								Result loginCodeResult = ObtainData
										.getLoginCode(mPhoneNum);
								Message message = new Message();
								message.obj = loginCodeResult;
								message.what = AUTHCODE;
								mHandler.sendMessage(message);
							} catch (Exception e) {
								mHandler.sendEmptyMessage(ERROR);
								e.printStackTrace();
							}
						}
					}.start();*/
				}
			}
		});

		// 获取到验证码后点击登录

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhoneNum = et_mPhone.getText().toString().trim();
				mCode = et_mSecurity.getText().toString().trim();
				if (mPhoneNum.equals("")) {
					Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNum.length() < 11) {
					Toast.makeText(mContext, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else if (mCode.equals("")) {
					Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 验证码登录
					// 网络访问要在线程中
					Utilsjf.creDialogpro(mActivity, "正在认证中...");
				/*	new Thread() {

						@Override
						public void run() {

							try {
								User user = ObtainData.loginSecurity(mActivity,
										mPhoneNum, mCode);
								Message message = new Message();
								message.obj = user;
								message.what = LOGINSECURITYRESULT;
								mHandler.sendMessage(message);
							} catch (Exception e) {
								mHandler.sendEmptyMessage(ERROR);
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
