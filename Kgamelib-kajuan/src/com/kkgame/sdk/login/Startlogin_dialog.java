package com.kkgame.sdk.login;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.HorizontalProgressBarWithNumber;
import com.kkgame.sdk.utils.LoginUtils;
import com.kkgame.sdk.utils.MD5;
import com.kkgame.sdk.utils.SIMCardUtil;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;

public class Startlogin_dialog extends Basedialogview {

	private ImageView iv_loading;
	private TextView tv_message;
	private ArrayList<String> mNames;
	private String mSelectUser;
	private String mPassword;
	private SmsManager mSmsManager;

	private String mUUID;
	private static final int FETCHSMS = 4;
	protected static final int FETCHSMS1 = 10;
	private static final int LOGINRESULT = 3; // 登陆返回
	protected static final int ERROR = 11;
	protected static final int SECRETLOGIN = 20;
	private static final String CMCC = "106900608888";

	private static final String TELECOM = "1069033301128";
	private static final int PROGRESS = 400;
	private User mUserLoading;

	private int processtime = 0;

	private boolean phonelogin;
	private String secretkey;
	private HorizontalProgressBarWithNumber pb_hori;

	public Startlogin_dialog(Activity activity) {
		super(activity);

	}

	/**
	 * 打开登录对话框
	 */
	private void startlogin() {
		dialogDismiss();
		Login_ho_dialog login_ho_dialog = new Login_ho_dialog(mActivity);
		login_ho_dialog.dialogShow();

	}

	@Override
	public void dialogShow() {
		// TODO Auto-generated method stub
		super.dialogShow();
		initlog();
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 450, 150, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 450, 150, 0, mLinearLayout, 0,
				0, 0, 0, 100);

		// ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_loginbut.9.png", mActivity));
		ll_content.setGravity(Gravity.CENTER_VERTICAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content2 = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content2, 450, 100, 0, mLinearLayout, 0,
				0, 0, 0, 100);
		ll_content2.setGravity(Gravity.CENTER);

		iv_loading = new ImageView(mActivity);
		machineFactory.MachineView(iv_loading, 100, 100, mLinearLayout, 1, 10);
		iv_loading.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_loading(1).png", mActivity));

		RotateAnimation rotateAnimation = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(-1);
		rotateAnimation.setDuration(1000);
		LinearInterpolator lin = new LinearInterpolator();
		rotateAnimation.setInterpolator(lin);

		iv_loading.setAnimation(rotateAnimation);
		iv_loading.startAnimation(rotateAnimation);

		tv_message = new TextView(mActivity);
		machineFactory.MachineTextView(tv_message, WRAP_CONTENT, WRAP_CONTENT,
				0, "", 36, mLinearLayout, 10, 0, 0, 0);
		tv_message.setTextColor(Color.parseColor("#666666"));

		pb_hori = new HorizontalProgressBarWithNumber(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		machineFactory.MachineView(pb_hori, MATCH_PARENT, 30, 0, mLinearLayout,
				30, 0, 30, 10, 100);
		pb_hori.setBackgroundColor(Color.WHITE);

		pb_hori.incrementProgressBy(0);

		pb_hori.setVisibility(View.GONE);

		// TODO
		ll_content2.addView(iv_loading);
		ll_content2.addView(tv_message);
		ll_content.addView(ll_content2);
		ll_content.addView(pb_hori);

		baselin.addView(ll_content);
		// baselin.addView(pb_hori);

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

		// dialog.setCanceledOnTouchOutside(true);

		// initlog();

	}

	private void initlog() {
		// mHandler.sendEmptyMessageDelayed(PROGRESS, 300);
		// 判断是否点击过切换账号或者
		if (ViewConstants.HADLOGOUT
				|| Sputils.getSPint("ischanageacount", 1, mActivity) == 0) {
			startlogin();
			return;
		}
		mNames = new ArrayList<String>();
		initDBData();

	}

	private void initDBData() {
		Yayalog.loger("初始化数据。。。。");
		// TODO Auto-generated method stub
		// 每次从数据库获取数据时都清空下列表,否则会有很多重复的数据
		if (mNames != null && mNames.size() > 0) {
			mNames.clear();
		}
		// 数据库添加一列
		UserDao.getInstance(mActivity).upDateclume();
		mNames = UserDao.getInstance(mActivity).getUsers();

		// 如果数据库里没有任何账号注册过.则进行快速注册
		if (mNames.size() == 0) {
			//startlogin();
			 startFirstregister();
			 tv_message.setText("尝试自动登录中...");
			return;
		}
		// 是否把切换账号取消了
		if (ViewConstants.nochangeacount) {
			startlogin();
			return;
		}

		if (mNames != null && mNames.size() > 0) {

			mSelectUser = mNames.get(0);
			mPassword = UserDao.getInstance(mActivity).getPassword(mSelectUser);

			// secretkey =
			// UserDao.getInstance(mActivity).getSecret(mSelectUser);

			if (!TextUtils.isEmpty(mPassword)
					&& !mPassword.equals("yayawan-zhang")) {
				Yayalog.loger("正在登陆。。。。");
				// 选择第一项进行登录
				LoginUtils loginUtils = new LoginUtils(mActivity, this,
						LoginUtils.STARTLOGIN);
				loginUtils.login(mSelectUser, mPassword);

				tv_message.setText("快速登录中...");
			}else {
				//Yayalog.loger("快速注册。。。。");
				//startFirstregister();
				startlogin();
			}
			Yayalog.loger("密码为空。。。。");

		}

	}

	// 快速注册
	private void startFirstregister() {

		dialogDismiss();
	
			new AcountRegister(mActivity).acountRregister();

		

	}

}
