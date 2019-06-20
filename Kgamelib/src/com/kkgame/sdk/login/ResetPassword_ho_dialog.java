package com.kkgame.sdk.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.common.CommonData;
import com.kkgame.sdk.bean.User;
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
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class ResetPassword_ho_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mPhone;
	private Button bt_mGetsecurity;
	private EditText et_mSecurity;
	private EditText et_mNewpassword;
	private Button bt_mOk;
	private String mUserName;
	private String mCode;
	private String mResult;
	private String mNewPassword;
	private static final int AUTHCODE = 3;

	private static final int RESULT = 7;

	private static final int LOGINFINDRESULT = 8;

	protected static final int ERROR = 9;
	private boolean flag;
	private CodeCountDown mCodeCountDown;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			case AUTHCODE:
				String result = (String) msg.obj;
				bt_mGetsecurity.setEnabled(false);
				flag = true;
				Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
				/*
				 * if (mCodeCountDown == null) { mCodeCountDown = mCodeCountDown
				 * = new CodeCountDown(60000,1000, bt_mGetsecurity); }
				 */
				mCountDown.startCounter();
				// mCounterdown.startCounter();
				break;
			case RESULT:
				if ("操作成功".equals(mResult)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mActivity);
					builder.setMessage("您的新密码为:" + mNewPassword + ",请牢记");
					builder.setNegativeButton("取消",
							new AlertDialog.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}

							});
					builder.setPositiveButton("立即登录",
							new AlertDialog.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// 将修改后的数据写入到数据库
									UserDao.getInstance(mActivity).writeUser(
											mUserName, mNewPassword, "");
									Utilsjf.creDialogpro(mActivity, "正在登陆...");
									/*
									 * new Thread() {
									 * 
									 * @Override public void run() { try { User
									 * user = ObtainData.login( mActivity,
									 * mUserName, mNewPassword); Message message
									 * = new Message(); message.obj = user;
									 * message.what = LOGINFINDRESULT;
									 * mHandler.sendMessage(message); } catch
									 * (Exception e) { e.printStackTrace(); } }
									 * }.start();
									 */
								}
							});
					builder.create().show();

				} else {
					Toast.makeText(mActivity, mResult, Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case LOGINFINDRESULT:
				User user = (User) msg.obj;
				if (user != null) {
					if (user.success == 0) {
						// 登录成功
						/*
						 * Toast.makeText(mActivity, user.body,
						 * Toast.LENGTH_SHORT) .show();
						 */
						// 将用户信息保存到全局变量
						AgentApp.mUser = user;
						// 开启悬浮窗服务
						onSuccess(user, 1);
						// YayaWan.init(mActivity);
						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity);
						login_success_dialog.dialogShow();
					} else {
						Toast.makeText(mActivity, user.body, Toast.LENGTH_SHORT)
								.show();
					}
				}

				break;
			case ERROR:
				Toast.makeText(mActivity, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};
	private AuthNumReceiver mAuthNumReceiver;
	private CounterDown mCounterdown;
	private CounterDown mCountDown;
	private ImageView iv_mPassword_icon;
	private ImageView iv_mSecurity_icon;
	private ImageView iv_mUn_icon;
	private TextView tv_tip;
	private TextView tv_tip1;

	public ResetPassword_ho_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {

		//onStart();

		dialog = new Dialog(mActivity);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		int height = 560;
		int with = 630;

		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
		//machineFactory.MachineView(baselin, with, height, "LinearLayout");
		//baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);
		baselin.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya1_sdkbackground.9.png",mActivity));
		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout");
		
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_title,
				MATCH_PARENT, 78, 0, mLinearLayout, 35, 28, 35, 0, 100);
		rl_title.setBackgroundColor(Color.parseColor("#ffffff"));

		ll_mPre = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mPre, 46, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mActivity);
		machineFactory.MachineView(iv_mPre, 46, 46, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya1_pre.png", mActivity));
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
				"找回密码", 44, mLinearLayout, 0, 0, 0, 0);
//		tv_zhuce.setTextColor(Color.parseColor("#c05011"));
		tv_zhuce.setTextColor(Color.BLACK);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
				LinearLayout ll_content1 = new LinearLayout(mActivity);
				ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
						height, MATCH_PARENT, 0, mLinearLayout, 35, 0, 35, 0,
						LinearLayout.VERTICAL);
				ll_content1.setOrientation(LinearLayout.VERTICAL);
		
		// 手机号码输入列
		LinearLayout ll_phone = new LinearLayout(mActivity);
		ll_phone = (LinearLayout) machineFactory.MachineView(ll_phone,
				MATCH_PARENT, 65, 0, "LinearLayout", 0, 30, 0, 0, 100);

		ll_phone.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

		ll_phone.setGravity(Gravity.CENTER);

				// username 的icon
				iv_mUn_icon = new ImageView(mActivity);
				iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 30,
						30, 0, mLinearLayout, 20, 0, 0, 0, 100);
				iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
						"yaya1_phoneicon.png", mActivity));

				// username的edtext
				et_mPhone = new EditText(mActivity);
				et_mPhone = machineFactory.MachineEditText(et_mPhone, 0, MATCH_PARENT, 1,
						"请输入手机号", 22, mLinearLayout, 0, 4, 0, 0);
				et_mPhone.setTextColor(Color.BLACK);
				et_mPhone.setHintTextColor(Color.parseColor("#b4b4b4"));
				et_mPhone.setBackgroundColor(Color.TRANSPARENT);

			

				// TODO
				ll_phone.addView(iv_mUn_icon);
				ll_phone.addView(et_mPhone);
		
		
				//验证码输入列	
				LinearLayout ll_mSecurityandbutton = new LinearLayout(mActivity);
				ll_mSecurityandbutton = (LinearLayout) machineFactory.MachineView(ll_mSecurityandbutton,
						MATCH_PARENT, 65, 0, "LinearLayout", 0, 30, 0, 0, 100);

				// 设置验证码输入框和获取验证码button
				LinearLayout ll_mSecurity = new LinearLayout(mActivity);
				ll_mSecurity = (LinearLayout) machineFactory.MachineView(ll_mSecurity,
						270, 65, 0, "LinearLayout", 0, 0, 0, 0, 100);
				ll_mSecurity.setOrientation(LinearLayout.HORIZONTAL);
				
				ll_mSecurity.setBackgroundDrawable(GetAssetsutils
								.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

				ll_mSecurity.setGravity(Gravity.CENTER);

						// username 的icon
				iv_mSecurity_icon = new ImageView(mActivity);
				iv_mSecurity_icon = (ImageView) machineFactory.MachineView(iv_mSecurity_icon, 30,
								30, 0, mLinearLayout, 20, 0, 0, 0, 100);
				iv_mSecurity_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
								"yaya1_codeicon.png", mActivity));

						// username的edtext
						et_mSecurity = new EditText(mActivity);
						et_mSecurity = machineFactory.MachineEditText(et_mSecurity, 0, MATCH_PARENT, 1,
								"请输入验证码", 22, mLinearLayout, 0, 4, 0, 0);
						et_mSecurity.setTextColor(Color.BLACK);
						et_mSecurity.setHintTextColor(Color.parseColor("#b4b4b4"));
						et_mSecurity.setBackgroundColor(Color.TRANSPARENT);

						// TODO
						ll_mSecurity.addView(iv_mSecurity_icon);
						ll_mSecurity.addView(et_mSecurity);
						
						// 获取验证码按钮
						bt_mGetsecurity = new Button(mActivity);
//						bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 270,
//								MATCH_PARENT, 0, "获取验证码", 22, mLinearLayout, 20, 0, 0, 0);
//						bt_mGetsecurity.setTextColor(Color.WHITE);
						bt_mGetsecurity = machineFactory.MachineButton(bt_mGetsecurity, 200,
								MATCH_PARENT, 0, "获取验证码", 22, mLinearLayout, 20, 0, 0, 0);
						bt_mGetsecurity.setTextColor(Color.WHITE);
						bt_mGetsecurity.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
								"yaya1_registerbutton.9.png", "yaya1_registerbutton.9.png",
								mActivity));
						
						
						bt_mGetsecurity.setGravity(Gravity.CENTER);
						
				
						ll_mSecurityandbutton.addView(ll_mSecurity);
						ll_mSecurityandbutton.addView(bt_mGetsecurity);
			

						// 密码输入列
						LinearLayout ll_mPassword = new LinearLayout(mActivity);
						ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
								MATCH_PARENT, 65, 0, "LinearLayout", 0, 30, 0, 0, 100);

						ll_mPassword.setBackgroundDrawable(GetAssetsutils
										.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

						ll_mPassword.setGravity(Gravity.CENTER);

								// username 的icon
						iv_mPassword_icon = new ImageView(mActivity);
						iv_mPassword_icon = (ImageView) machineFactory.MachineView(iv_mPassword_icon, 30,
										30, 0, mLinearLayout, 20, 0, 0, 0, 100);
						iv_mPassword_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
										"yaya1_password.png", mActivity));

								// username的edtext
						et_mNewpassword = new EditText(mActivity);
						et_mNewpassword = machineFactory.MachineEditText(et_mNewpassword, 0, MATCH_PARENT, 1,
										"请设置新密码（6-20位字母或者数字）", 22, mLinearLayout, 0, 4, 0, 0);
						et_mNewpassword.setTextColor(Color.BLACK);
						et_mNewpassword.setHintTextColor(Color.parseColor("#b4b4b4"));
						et_mNewpassword.setBackgroundColor(Color.TRANSPARENT);

							

								// TODO
								ll_mPassword.addView(iv_mPassword_icon);
								ll_mPassword.addView(et_mNewpassword);
						

	
		
		tv_tip1 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip1, MATCH_PARENT,
				WRAP_CONTENT, 0, "自动注册的密码以图片形式保存在储存卡", 18, mLinearLayout, 0, 10, 35, 0,
				100);
//		tv_tip1.setTextColor(Color.parseColor("#ffa429"));
		tv_tip1.setTextColor(Color.BLACK);
		
		tv_tip = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip, MATCH_PARENT,
				WRAP_CONTENT, 0, "无绑定手机，请联系客服QQ："+CommonData.kefuqq, 18, mLinearLayout, 0, 10, 35, 0,
				100);
//		tv_tip.setTextColor(Color.parseColor("#ffa429"));
		tv_tip.setTextColor(Color.BLACK);
		
		
		// 确定按钮
		bt_mOk = new Button(mActivity);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 78, 0, "确定", 36,
				mLinearLayout, 0, 15, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya1_registerbutton.9.png", "yaya1_registerbutton.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_content1.addView(ll_phone);
		ll_content1.addView(ll_mSecurityandbutton);
		ll_content1.addView(ll_mPassword);
		
//		ll_content1.addView(tv_tip1);
		ll_content1.addView(tv_tip);
		
		ll_content1.addView(bt_mOk);

		ll_content.addView(rl_title);

		ll_content.addView(ll_content1);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 1f; // 透明度

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

	private void initlogic() {
		
		tv_tip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String qqhao = Sputils.getSPstring("service_qq", "暂无", mActivity);
				//qqhao="3003569760";
				if (qqhao.equals("4000042115")) {
					qqhao="938189213";
				}
				if (qqhao.equals("暂无")) {
					
				}else {
					 String url="mqqwpa://im/chat?chat_type=crm&uin="+qqhao+"&version=1&src_type=web&web_src=http:://wpa.b.qq.com";

					//String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqhao;
					mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				}
			}
		});
		
		String qqhao = Sputils.getSPstring("service_qq", "暂无", mActivity);
		if (qqhao.equals("暂无")) {
			
		}else {
			tv_tip.setText("无绑定手机，请联系QQ公众号："+qqhao);
			
		}
		mCountDown = CounterDown.getInstance(mActivity);
		mCountDown.setView(bt_mGetsecurity);
		
		bt_mGetsecurity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mUserName = et_mPhone.getText().toString().trim();
				if ("".equals(mUserName) || mUserName.length() == 0) {
					Toast.makeText(mActivity, "请输入用户名信息", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在获取验证码...");

					RequestParams rps = new RequestParams();
					rps.addBodyParameter("type", 1 + "");
					rps.addBodyParameter("mobile", mUserName);

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
									try {
										JSONObject jsonObject = new JSONObject(result.result);
										String errmsg = jsonObject.getString("err_msg");
										if (errmsg.equals("success")) {
											mCountDown.startCounter();
											Toast.makeText(mActivity, "验证码已经发送", 0)
											.show();
										}else{
											Toast.makeText(mActivity, errmsg, 0)
											.show();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
				}
			}
		});

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCode = et_mSecurity.getText().toString().trim();
				mUserName = et_mPhone.getText().toString().trim();
				if ("".equals(mUserName) || mUserName.length() < 0) {
					Toast.makeText(mActivity, "请输入用户名", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mCode) || mCode.length() < 0) {
					Toast.makeText(mActivity, "请输入验证码", Toast.LENGTH_SHORT)
							.show();
				} else {
					//
					mNewPassword = et_mNewpassword.getText().toString().trim();
					Utilsjf.creDialogpro(mActivity, "正在重设密码...");
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					/*
					 * rps.addBodyParameter("imei",
					 * DeviceUtil.getIMEI(mActivity));
					 */
					rps.addBodyParameter("mobile", mUserName);
					rps.addBodyParameter("password", mNewPassword);
					rps.addBodyParameter("code", mCode);
					Yayalog.loger("url:"+ViewConstants.resetpassword+"mobile:"+mUserName+"password:"+mNewPassword+"code:"+mCode);
					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.resetpassword, rps,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub

									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "请检查网络是否畅通", 0)
											.show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
							 //找回密码：{"err_code":0,"err_msg":"success"}

									Utilsjf.stopDialog();
									try {
										JSONObject json=new JSONObject(result.result);
										Yayalog.loger("找回密码：" + result.result);
										String errmsg=json.getString("err_msg");
										if (errmsg.equals("success")) {
											Toast.makeText(mActivity, "修改成功", 0).show();
										}else {
											Toast.makeText(mActivity, errmsg, 0).show();
										}
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									

								}

							});
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
