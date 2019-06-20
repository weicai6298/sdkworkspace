package com.kkgame.sdk.smallhelp;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.AuthNumReceiver;
import com.kkgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CodeCountDown;
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

/**
 * 实名认证弹框
 * 
 * @author zjf
 * 
 */
public class RealNameAuth_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;

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

				break;

			default:
				break;
			}
		}

	};

	private AuthNumReceiver mAuthNumReceiver;
	private EditText mOldPassword;
	private EditText mNewPassword;

	private TextView tv_fogetpassword;

	public RealNameAuth_dialog(Activity activity) {
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
				"防沉迷实名认证", 38, mLinearLayout, 0, 0, 0, 0);
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
				WRAP_CONTENT, 0, "必须填入真实姓名和身份证号，认证防沉迷", 22, mRelativeLayout, 0,
				0, 20, 0, RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_fogetpassword.setTextColor(Color.RED);
		ll_content1.addView(tv_fogetpassword);

		// 手机号码输入行
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 96, mLinearLayout);

		// 手机号码输入框
		et_mPhone = new EditText(mActivity);
		machineFactory.MachineEditText(et_mPhone, MATCH_PARENT, MATCH_PARENT,
				0, "请输入姓名", 32, mLinearLayout, 0, 0, 0, 0);
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
				"请输入身份证号", 32, mLinearLayout, 0, 20, 0, 0);
		et_mSecurity
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_biankuang2.9.png",
								mActivity));
		et_mSecurity.setPadding(machSize(20), 0, 0, 0);

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

	private String name;
	private String authnumber;

	private Result mResult;

	private void initlogic() {

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = et_mPhone.getText().toString().trim();
				authnumber = et_mSecurity.getText().toString().trim();

				if (isIDCard(authnumber)) {
					// 进入修改流程
					Utilsjf.creDialogpro(mActivity, "正在实名认证中....");
					HttpUtils httpUtils = new HttpUtils();
					String data = "";
					try {
						// data = ObtainData.encryptModifyRealAuth(mActivity,
						// AgentApp.mUser, name, authnumber);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					RequestParams requestParams = new RequestParams();
					requestParams.addBodyParameter("data", data);
					Yayalog.loger("data" + data + "name" + name + "authnumber"
							+ authnumber);
					httpUtils.send(HttpMethod.POST, ViewConstants.loginurl,
							requestParams, new RequestCallBack<String>() {

								@Override
								public void onSuccess(
										ResponseInfo<String> responseInfo) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									// Yayalog.loger("实名认证返回结果"+responseInfo.result);
									JSONObject jsonObject;
									try {
										jsonObject = new JSONObject(
												responseInfo.result);
										int optInt = jsonObject
												.optInt("success");
										if (optInt != 1) {
											Toast.makeText(mActivity, "认证成功", 0)
													.show();
											dialogDismiss();
										} else {
											Toast.makeText(mActivity, "认证失败", 0)
													.show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

								@Override
								public void onFailure(HttpException error,
										String msg) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									// Yayalog.loger("实名认证返回结果"+msg);
								}
							});

				} else {
					Toast.makeText(mActivity, "请输入正确的身份证号码", 0).show();
				}

			}
		});

	}

	/**
	 * 正则表达式：验证身份证
	 */
	public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

	/**
	 * 校验身份证
	 * 
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		return Pattern.matches(REGEX_ID_CARD, idCard);
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
