package com.kkgame.sdk.login;

import java.math.BigInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.AuthNumReceiver;
import com.kkgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CodeCountDown;
import com.kkgame.sdk.utils.CounterDown;
import com.kkgame.sdk.utils.ToastUtil;

import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class Register_ho_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private ImageButton ib_mAgreedbox;
	private Button bt_mOk;
	private TextView tv_mRegisterclick;
	private String mPhoneNum;
	private String mCode;

	private static final int AUTHCODE = 5;
	protected static final int ERROR = 11;
	protected static final int LOGINSECURITYRESULT = 8;

	private ImageButton ib_mNotAgreedbox;
	private AuthNumReceiver mAuthNumReceiver;
	private CounterDown mCountDown;
	private EditText et_mPassword;

	public Register_ho_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(final Activity mActivity) {

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
				"手机注册", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mActivity);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				660, MATCH_PARENT, 0, mLinearLayout, 0, 20, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout);

		// 手机号码输入框
		et_mPhone = new EditText(mActivity);
		machineFactory.MachineEditText(et_mPhone, 400, MATCH_PARENT, 0,
				"请输入手机号", 32, mLinearLayout, 0, 0, 0, 0);
		et_mPhone
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mPhone.setPadding(machSize(20), 0, 0, 0);

		// 获取验证码按钮
		bt_mGetsecurity = new Button(mActivity);
		bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 240,
				MATCH_PARENT, 0, "获取验证码", 32, mLinearLayout, 30, 0, 0, 0);
		bt_mGetsecurity.setTextColor(Color.WHITE);
		bt_mGetsecurity.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_bulebutton.9.png", "yaya_bulebutton1.9.png", mActivity));

		// TODO
		ll_phone.addView(et_mPhone);
		ll_phone.addView(bt_mGetsecurity);

		// 输入密码框
		et_mPassword = new EditText(mActivity);
		machineFactory.MachineEditText(et_mPassword, MATCH_PARENT, 86, 0,
				"请输入密码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mPassword
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mPassword.setPadding(machSize(20), 0, 0, 0);

		// 验证码输入框
		et_mSecurity = new EditText(mActivity);
		machineFactory.MachineEditText(et_mSecurity, MATCH_PARENT, 86, 0,
				"请输入验证码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mSecurity
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

		// 条款
		LinearLayout ll_clause = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_clause, MATCH_PARENT, 50, mLinearLayout,
				2, 20);
		ll_clause.setGravity(Gravity.CENTER_VERTICAL);

		// 同意服务条款
		ib_mAgreedbox = new ImageButton(mActivity);
		machineFactory.MachineView(ib_mAgreedbox, 40, 40, mLinearLayout, 2, 5);
		ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkedbox.png", mActivity));
		ib_mAgreedbox.setBackgroundDrawable(null);

		// 不同意服务条款
		ib_mNotAgreedbox = new ImageButton(mActivity);
		machineFactory.MachineView(ib_mNotAgreedbox, 40, 40, mLinearLayout, 2,
				5);
		ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkbox.png", mActivity));
		ib_mNotAgreedbox.setBackgroundDrawable(null);
		ib_mNotAgreedbox.setVisibility(View.GONE);

		TextView tv_agree = new TextView(mActivity);
		machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
				"同意YY玩服务条款协议", 30, mLinearLayout, 6, 0, 0, 0);
		tv_agree.setTextColor(Color.GRAY);
		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
		tv_agree.setClickable(true);
		tv_agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YYprotocol_ho_dialog yYprotocol_ho_dialog = new YYprotocol_ho_dialog(
						mActivity);
				yYprotocol_ho_dialog.dialogShow();
			}
		});

		// TODO
		ll_clause.addView(ib_mAgreedbox);
		ll_clause.addView(ib_mNotAgreedbox);
		ll_clause.addView(tv_agree);
		ib_mAgreedbox.setClickable(true);
		ib_mAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mAgreedbox.setVisibility(View.GONE);
				ib_mNotAgreedbox.setVisibility(View.VISIBLE);
			}
		});
		ib_mNotAgreedbox.setClickable(true);
		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mNotAgreedbox.setVisibility(View.GONE);
				ib_mAgreedbox.setVisibility(View.VISIBLE);
			}
		});

		// 确定按钮
		bt_mOk = new Button(mActivity);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 25, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// 账号注册
		LinearLayout ll_accountregist = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_accountregist, MATCH_PARENT, 50,
				mLinearLayout, 2, 30);
		ll_accountregist.setOrientation(LinearLayout.HORIZONTAL);

		ll_accountregist.setClickable(true);
		// 点击事件..点击打开账号注册窗口
		ll_accountregist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AcountRegister_ho_dialog acountRegister_ho_dialog = new AcountRegister_ho_dialog(
						mActivity);
				acountRegister_ho_dialog.dialogShow();
			}
		});

		TextView tv_accountregist1 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_accountregist1, WRAP_CONTENT,
				WRAP_CONTENT, 0, "未收到验证码,点击", 30, mLinearLayout, 0, 0, 0, 0);
		tv_accountregist1.setTextColor(Color.GRAY);

		// 账号注册点击textview
		tv_mRegisterclick = new TextView(mActivity);
		machineFactory.MachineTextView(tv_mRegisterclick, WRAP_CONTENT,
				WRAP_CONTENT, 0, "账号注册", 30, mLinearLayout, 0, 0, 0, 0);
		tv_mRegisterclick.setTextColor(Color.parseColor("#66c4ef"));

		// TODO
		ll_accountregist.addView(tv_accountregist1);
		ll_accountregist.addView(tv_mRegisterclick);

		// TODO
		ll_content1.addView(ll_phone);
		ll_content1.addView(et_mPassword);
		ll_content1.addView(et_mSecurity);
		// ll_content1.addView(ll_clause);
		ll_content1.addView(bt_mOk);
		ll_content1.addView(ll_accountregist);

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

		initLogic();
	}

	private String mPassword;

	private void initLogic() {

		onStart();
		mCountDown = CounterDown.getInstance();
		mCountDown.setView(bt_mGetsecurity);
		// 获取验证码
		bt_mGetsecurity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mPhoneNum = et_mPhone.getText().toString().trim();
				/*
				 * if (ib_mAgreedbox.getVisibility() == View.GONE) {
				 * Toast.makeText(mActivity, "请同意yy玩服务协议", Toast.LENGTH_SHORT)
				 * .show(); }
				 */
				if (mPhoneNum.equals("")) {
					Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNum.length() < 11) {
					Toast.makeText(mActivity, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else {

					Utilsjf.creDialogpro(mActivity, "正在获取验证码...");

					RequestParams rps = new RequestParams();
					rps.addBodyParameter("type", 2 + "");
					rps.addBodyParameter("mobile", mPhoneNum);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST, ViewConstants.getphonecode,
							rps, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "验证码发送失败，请检查网络",
											0).show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Yayalog.loger(result.result);
									Toast.makeText(mActivity, "验证码已经发送", 0)
											.show();

								}
							});

				}
			}
		});

		// 获取到验证码后点击注册

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPhoneNum = et_mPhone.getText().toString().trim();
				mCode = et_mSecurity.getText().toString().trim();
				mPassword = et_mPassword.getText().toString().trim();
				if (mPhoneNum.equals("")) {
					Toast.makeText(mActivity, "手机号不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNum.length() < 11) {
					Toast.makeText(mActivity, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else if (mCode.equals("")) {
					Toast.makeText(mActivity, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在登录...");
					// 验证码登录
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					rps.addBodyParameter("imei", DeviceUtil.getIMEI(mActivity));
					rps.addBodyParameter("mobile", mPhoneNum);
					rps.addBodyParameter("password", mPassword);
					rps.addBodyParameter("code", mCode);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.phoneregister, rps,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "注册失败，请检查网络是否畅通",
											0).show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									
									Intent mIntent = new Intent();
							    	mIntent.setClass(mActivity, zhuceClipBoardService.class);
									mActivity.startService(mIntent);

									Yayalog.loger("手机注册结果" + result.result);
									Toast.makeText(mActivity, "注册成功", 0).show();

									User user = parserPhoneRegisterResult(result.result);
									if (user == null) {
										return;
									}
									AgentApp.mUser = user;
									Yayalog.loger("手机注册结果"
											+ AgentApp.mUser.toString());
									// System.out.println("手机注册接口返回数据"+loginuser.secret);
									// 将base64加密的用户信息保存到数据库
									UserDao.getInstance(mActivity).writeUser(
											user.userName, user.password,
											user.secret);
									// System.out.println("手机得到的账号密码是0"+loginuser);
									user.password = "";
									user.secret = "";
									// 开启悬浮窗服务
									// YayaWan.init(mActivity);

									allDismiss();

									Login_success_dialog login_success_dialog = new Login_success_dialog(
											mActivity);
									login_success_dialog.dialogShow();
								}

							});
				}
			}
		});

	}

	/**
	 * 解析手机注册后的结果
	 * 
	 * @param result
	 */
	private User parserPhoneRegisterResult(String result) {
		// TODO Auto-generated method stub[
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!result.contains("success")) {
				String errmsg = jsonObject.optString("err_msg");
				Toast.makeText(mActivity, errmsg, 0).show();
				return null;
			}
			JSONObject datas = jsonObject.getJSONObject("data");
			User user = new User();
			user.setPhone(datas.optString("mobile"));
			user.setUserName(datas.optString("mobile"));
			user.setToken(datas.optString("token"));
			user.setPassword(mPassword);
			// new BigInteger(datas.optString("uid"));
			user.setUid(new BigInteger(datas.optString("uid")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
