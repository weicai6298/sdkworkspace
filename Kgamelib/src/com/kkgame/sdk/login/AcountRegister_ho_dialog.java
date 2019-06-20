package com.kkgame.sdk.login;

import java.math.BigInteger;
import java.util.UUID;
import java.util.zip.CRC32;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class AcountRegister_ho_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mUser;
	private EditText et_mPassword;
//	private ImageButton ib_mAgreedbox;
	private Button bt_mOk;
	private String mName;
	private String mPassword;

	private User mUser;
	private static final int REGISTER = 3;

	private static final int FETCHSMS = 4;

	protected static final int ERROR = 5;

//	private ImageButton ib_mNotAgreedbox;
	private Button bt_mPhoneRegister;
	private Button bt_mAccountRegister;
	private ImageView iv_mUn_icon;
	private ImageView iv_mPassword_icon;
	private ImageView iv_mRePassword_icon;
	private EditText et_mRePassword;

	public AcountRegister_ho_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(final Activity mActivity) {

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
				MATCH_PARENT, 78, 0, mLinearLayout, 35, 30, 35, 0, 100);
		rl_title.setBackgroundColor(Color.parseColor("#fffff3"));

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
		
		
		// 手机号注册
		bt_mPhoneRegister = new Button(mActivity);
		machineFactory.MachineButton(bt_mPhoneRegister, 232, 78, 0, "手机号注册", 28,
				mRelativeLayout, 70, 0, 0, 0);
		bt_mPhoneRegister.setTextColor(Color.WHITE);
		bt_mPhoneRegister.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya1_acountregisterbutton.9.png", "yaya1_acountregisterbutton.9.png",
				mActivity));
		bt_mPhoneRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		bt_mPhoneRegister.setGravity(Gravity_CENTER);
		
		// 用戶名注冊
		bt_mAccountRegister = new Button(mActivity);
		machineFactory.MachineButton(bt_mAccountRegister, 232, 78, 0, "用户名注册", 28,
				mRelativeLayout, 327, 0, 0, 0);
		bt_mAccountRegister.setTextColor(Color.WHITE);
		
		bt_mAccountRegister.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya1_registerbutton.9.png", "yaya1_registerbutton.9.png",
				mActivity));
		bt_mAccountRegister.setGravity(Gravity_CENTER);
	
// TODO
		rl_title.addView(ll_mPre);
		
		
		//如果是sdktpye为1的话，就隐藏背景
		if (KgameSdk.sdktype==1) {
			
		}else {
			rl_title.addView(bt_mPhoneRegister);
		}
		
		
		
		rl_title.addView(bt_mAccountRegister);
		

		
		

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mActivity);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				height, MATCH_PARENT, 0, mLinearLayout, 35, 0, 35, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		
		
		// 用户名输入列
				LinearLayout ll_mUser = new LinearLayout(mActivity);
				ll_mUser = (LinearLayout) machineFactory.MachineView(ll_mUser,
						MATCH_PARENT, 70, 0, "LinearLayout", 0, 30, 0, 0, 100);

				ll_mUser.setBackgroundDrawable(GetAssetsutils
								.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

				ll_mUser.setGravity(Gravity.CENTER);

						// username 的icon
						iv_mUn_icon = new ImageView(mActivity);
						iv_mUn_icon = (ImageView) machineFactory.MachineView(iv_mUn_icon, 30,
								30, 0, mLinearLayout, 20, 0, 0, 0, 100);
						iv_mUn_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
								"yaya1_username.png", mActivity));

						// username的edtext
						et_mUser = new EditText(mActivity);
						et_mUser = machineFactory.MachineEditText(et_mUser, 0, MATCH_PARENT, 1,
								"用户名：6-8位数字或者字母", 22, mLinearLayout, 0, 6, 0, 0);
						et_mUser.setTextColor(Color.BLACK);
						et_mUser.setHintTextColor(Color.parseColor("#b4b4b4"));
						et_mUser.setBackgroundColor(Color.TRANSPARENT);

					

						// TODO
						ll_mUser.addView(iv_mUn_icon);
						ll_mUser.addView(et_mUser);
		
				// 密码输入列
				LinearLayout ll_mPassword = new LinearLayout(mActivity);
				ll_mPassword = (LinearLayout) machineFactory.MachineView(ll_mPassword,
								MATCH_PARENT, 70, 0, "LinearLayout", 0, 30, 0, 0, 100);

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
				et_mPassword = new EditText(mActivity);
				et_mPassword = machineFactory.MachineEditText(et_mPassword, 0, MATCH_PARENT, 1,
										"密码：6-8位数字或者字母", 22, mLinearLayout, 0, 4, 0, 0);
				et_mPassword.setTextColor(Color.BLACK);
				et_mPassword.setHintTextColor(Color.parseColor("#b4b4b4"));
				et_mPassword.setBackgroundColor(Color.TRANSPARENT);

							

								// TODO
				ll_mPassword.addView(iv_mPassword_icon);
				ll_mPassword.addView(et_mPassword);
				
				// 重复密码输入列
				LinearLayout ll_mRePassword = new LinearLayout(mActivity);
				ll_mRePassword = (LinearLayout) machineFactory.MachineView(ll_mRePassword,
								MATCH_PARENT, 70, 0, "LinearLayout", 0, 30, 0, 0, 100);

				ll_mRePassword.setBackgroundDrawable(GetAssetsutils
										.get9DrawableFromAssetsFile("yaya1_biankuan.9.png", mActivity));

				ll_mRePassword.setGravity(Gravity.CENTER);

								// username 的icon
				iv_mRePassword_icon = new ImageView(mActivity);
				iv_mRePassword_icon = (ImageView) machineFactory.MachineView(iv_mRePassword_icon, 30,
										30, 0, mLinearLayout, 20, 0, 0, 0, 100);
				iv_mRePassword_icon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
										"yaya1_password.png", mActivity));

								// username的edtext
				et_mRePassword = new EditText(mActivity);
				et_mRePassword = machineFactory.MachineEditText(et_mRePassword, 0, MATCH_PARENT, 1,
										"请再次输入密码", 22, mLinearLayout, 0, 4, 0, 0);
				et_mRePassword.setTextColor(Color.BLACK);
				et_mRePassword.setHintTextColor(Color.parseColor("#b4b4b4"));
				et_mRePassword.setBackgroundColor(Color.TRANSPARENT);

							

								// TODO
				ll_mRePassword.addView(iv_mRePassword_icon);
				ll_mRePassword.addView(et_mRePassword);
		// et_mUser.setText("你懂得");

		

		
		// 条款
		LinearLayout ll_clause = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_clause, MATCH_PARENT, 50, mLinearLayout,
				2, 10);
		ll_clause.setGravity(Gravity.CENTER_VERTICAL);

		// 同意服务条款
//		ib_mAgreedbox = new ImageButton(mActivity);
//		machineFactory.MachineView(ib_mAgreedbox, 30, 30, mLinearLayout, 2, 0);
//		ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
//				"yaya_checkedbox.png", mActivity));
//		ib_mAgreedbox.setBackgroundDrawable(null);

		// 不同意服务条款
//		ib_mNotAgreedbox = new ImageButton(mActivity);
//		machineFactory.MachineView(ib_mNotAgreedbox, 30, 30, mLinearLayout, 2,
//				5);
//		ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
//				"yaya_checkbox.png", mActivity));
//		ib_mNotAgreedbox.setBackgroundDrawable(null);
//		ib_mNotAgreedbox.setVisibility(View.GONE);

//		TextView tv_agree = new TextView(mActivity);
//		machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
//				"同意协议", 22, mLinearLayout, 6, 0, 0, 0);
//		tv_agree.setTextColor(Color.parseColor("#b4b4b4"));
//		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
//		tv_agree.setClickable(true);
//		tv_agree.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				YYprotocol_ho_dialog yYprotocol_ho_dialog = new YYprotocol_ho_dialog(
//						mActivity);
//				yYprotocol_ho_dialog.dialogShow();
//			}
//		});

		// TODO
//		ll_clause.addView(ib_mAgreedbox);
//		ll_clause.addView(ib_mNotAgreedbox);
//		ll_clause.addView(tv_agree);
//		ib_mAgreedbox.setClickable(true);
//		ib_mAgreedbox.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ib_mAgreedbox.setVisibility(View.GONE);
//				ib_mNotAgreedbox.setVisibility(View.VISIBLE);
//			}
//		});
//		ib_mNotAgreedbox.setClickable(true);
//		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ib_mNotAgreedbox.setVisibility(View.GONE);
//				ib_mAgreedbox.setVisibility(View.VISIBLE);
//			}
//		});
		// 确定按钮
		bt_mOk = new Button(mActivity);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 78, 0, "注册", 36,
				mLinearLayout, 0, 15, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya1_registerbutton.9.png", "yaya1_registerbutton.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_content1.addView(ll_mUser);
		ll_content1.addView(ll_mPassword);
		ll_content1.addView(ll_mRePassword);
		//如果是sdktpye为1的话，就隐藏背景
				if (KgameSdk.sdktype==1) {
					
				}else {
//					 ll_content1.addView(ll_clause);
				}
				
		
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

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {
		UUID uuid = UUID.randomUUID();
		CRC32 crc32 = new CRC32();
		crc32.update(uuid.toString().getBytes());

		et_mUser.setText("kk" + crc32.getValue());
		// et_mPassword.setText(CryptoUtil.getSeed());

		bt_mOk.setOnClickListener(new OnClickListener() {

			private String mRePassword;

			@Override
			public void onClick(View v) {
				mName = et_mUser.getText().toString().trim();
				mPassword = et_mPassword.getText().toString().trim();
				mRePassword = et_mRePassword.getText().toString().trim();
				
//				  if (ib_mAgreedbox.getVisibility() == View.GONE) {
//				 Toast.makeText(mContext, "请同意协议", Toast.LENGTH_SHORT)
//				 .show(); return; 
//				 }
				 if(!mPassword.equals(mRePassword)){
					 Yayalog.loger(mPassword);
					 Yayalog.loger(mRePassword);
					 Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT)
						.show();
					 return;
				 }
				if (mName.equals("")) {
					Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.equals("")) {
					Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() < 6) {
					Toast.makeText(mContext, "用户名不能小于六位", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() > 20) {
					Toast.makeText(mContext, "用户名不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.length() < 6) {
					Toast.makeText(mContext, "密码不能小于六位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.length() > 20) {
					Toast.makeText(mContext, "密码不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在快速注册...");
					RequestParams rps = new RequestParams();
					rps.addBodyParameter("app_id",
							DeviceUtil.getAppid(mActivity));
					rps.addBodyParameter("imei", DeviceUtil.getIMEI(mActivity));
					rps.addBodyParameter("username", mName);
					rps.addBodyParameter("password", mPassword);
					Yayalog.loger("app_id:" + DeviceUtil.getAppid(mActivity)
							+ "imei" + DeviceUtil.getIMEI(mActivity)
							+ "username" + mName + "password" + mPassword);

					HttpUtils httpUtils = new HttpUtils();
					httpUtils.send(HttpMethod.POST,
							ViewConstants.acountregister, rps,
							new RequestCallBack<String>() {
							
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									Utilsjf.stopDialog();
									Toast.makeText(mActivity, "注册失败，请检查网络", 0)
											.show();
								}

								@Override
								public void onSuccess(
										ResponseInfo<String> result) {
									// TODO Auto-generated method stub
									Yayalog.loger("注册返回信息" + result.result);
									Utilsjf.stopDialog();
									User user = parserAcountRegisterResult(result.result);
									if (user != null) {
										
										AgentApp.mUser = user;
										Yayalog.loger(AgentApp.mUser.toString());
										// 将base64加密的用户信息保存到数据库
										UserDao.getInstance(mContext)
												.writeUser(
														AgentApp.mUser
																.getUserName(),
														AgentApp.mUser.password,
														"123");
										AgentApp.mUser.password="";
										Yayalog.loger("登陆的uid："+user.toString());
										allDismiss();
										Login_success_dialog login_success_dialog = new Login_success_dialog(
												mActivity);
										login_success_dialog.dialogShow();

									}

								}
							});
				}
			}
		});

	}

	/**
	 * 解析账号注册后的结果
	 * 
	 * @param result
	 */
	private User parserAcountRegisterResult(String result) {
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
			user.setUserName(datas.optString("username"));
			user.setToken(datas.optString("token"));
			user.setPassword(mPassword);
			user.setUid(new BigInteger(datas.optString("uid")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
