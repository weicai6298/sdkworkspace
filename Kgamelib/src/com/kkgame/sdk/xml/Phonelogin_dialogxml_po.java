package com.kkgame.sdk.xml;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kkgame.sdk.bean.Result;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.AuthNumReceiver;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CodeCountDown;
import com.kkgame.sdk.utils.DialogUtil;
import com.kkgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;



public class Phonelogin_dialogxml_po extends Basedialogview {

	private LinearLayout ll_mHelp;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private ImageButton ib_mAgreedbox;
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
			DialogUtil.dismissDialog();
			switch (msg.what) {

			case AUTHCODE:
				Result loginResult = (Result) msg.obj;
				Toast.makeText(mContext, loginResult.body, Toast.LENGTH_LONG)
						.show();
				// 重新获取验证码倒计时
				if (mCodeCountDown == null) {
					mCodeCountDown = new CodeCountDown(60000,1000,
							bt_mGetsecurity);
				}
				mCodeCountDown.start();

				break;

			case LOGINSECURITYRESULT:
				User loginuser = (User) msg.obj;
				if (loginuser.success == 0) {
					AgentApp.mUser = loginuser;
					// 将base64加密的用户信息保存到数据库
					// UserDao.getInstance(mContext).writeUser(loginuser.userName,
					// loginuser.password);
					loginuser.password = "";
					// 开启悬浮窗服务
					//KgameSdk.init(mActivity);
					onSuccess(loginuser, 1);
					mActivity.finish();
				} else if (loginuser.success == 1) {

				} else if (loginuser.success == 2) {
					AgentApp.mUser = loginuser;
					// 将base64加密的用户信息保存到数据库
					UserDao.getInstance(mContext).writeUser(loginuser.userName,
							loginuser.password,loginuser.secret);
					loginuser.secret="";
					loginuser.password = "";
					// 开启悬浮窗服务
					//KgameSdk.init(mActivity);
					onSuccess(loginuser, 1);
					mActivity.finish();
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
	private AuthNumReceiver mAuthNumReceiver;

	public Phonelogin_dialogxml_po(Activity activity) {
		super(activity);
		
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 450, "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_content, 650, 620, mLinearLayout);
		rl_content.setBackgroundColor(Color.WHITE);
		// rl_content.setGravity(Gravity.CENTER_HORIZONTAL);
		// rl_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 620, 0, mRelativeLayout,
				20, 0, 20, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 删除行
		LinearLayout ll_deleline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_deleline, MATCH_PARENT, 50,
				mLinearLayout, 2, 9);
		ll_deleline.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
				mLinearLayout);

		ll_mHelp = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mHelp, 60, MATCH_PARENT, mLinearLayout);
		ll_mHelp.setGravity(Gravity_CENTER);

		ImageView iv_help = new ImageView(mActivity);
		machineFactory.MachineView(iv_help, 50, 50, mLinearLayout, 3, 0);
		iv_help.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_help.png", mActivity));

		// TODO
		ll_mHelp.addView(iv_help);

		ll_mDele = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mDele, 60, MATCH_PARENT, mLinearLayout,
				3, 0);
		ll_mDele.setGravity(Gravity_CENTER);
		ll_mDele.setClickable(true);

		ImageView iv_dele = new ImageView(mActivity);
		machineFactory.MachineView(iv_dele, 50, 50, mLinearLayout);
		iv_dele.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_dele.png", mActivity));
		iv_dele.setClickable(false);

		ll_mDele.addView(iv_dele);
		ll_mDele.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogDismiss();
			}
		});

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mContext);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout, 2, 20);

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
				"请输入验证码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mSecurity.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 确定按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 50, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils
				.crSelectordraw("yaya_yellowbutton.9.png",
						"yaya_yellowbutton1.9.png", mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_deleline.addView(ll_zhanwei);

		ll_deleline.addView(ll_mDele);

		ll_content.addView(ll_deleline);

		ll_content.addView(ll_phone);
		ll_content.addView(et_mSecurity);

		ll_content.addView(bt_mOk);

		rl_content.addView(ll_content);

		baselin.addView(rl_content);

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
		initlog();

	}

	private void initlog() {
		
		//System.out.println("开始注册短信广播");
		onStart();
		
		mUserCallback = KgameSdk.mUserCallback;

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
					DialogUtil.showDialog(mContext, "正在获取验证码...");
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

		// 获取到验证码后点击注册

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
					DialogUtil.showDialog(mContext, "正在登陆...");
					// 验证码登录
					/*new Thread() {
						@Override
						public void run() {

							try {
								User user = ObtainData.loginSecurity(mContext,
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

	public void onSuccess(User paramUser, int paramInt) {

		if (mUserCallback != null) {
			mUserCallback.onSuccess(paramUser, paramInt);
		}
		mUserCallback = null;
		mActivity.finish();

	}

	public void onError(int paramInt) {
		if (mUserCallback != null) {
			mUserCallback.onError(paramInt);
		}
		mUserCallback = null;
	}

	public void onCancel() {
		if (mUserCallback != null) {
			mUserCallback.onCancel();
		}
		mUserCallback = null;
		mActivity.finish();
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
            	//System.out.println("接收到的短信+++++++"+message);
            	et_mSecurity.setText(message);
            }
        });
		//super.onStart();
	}

}
