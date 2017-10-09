package com.kkgame.sdk.smallhelp;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.login.Aboutus_dialog;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Personalview_xml_ho;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.lidroid.jxutils.BitmapUtils;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;


public class PersonalView extends BaseContentView {

	private Personalview_xml_ho mThisview;
	private ImageView iv_mHead;
	private TextView tv_mUsename;
	private TextView tv_mLogintime;
	private TextView tv_mBalance;

	private TextView tv_fromcamera;
	private TextView tv_fromalbum;

	private HttpUtils httpUtils;
	private User mUser;
	private LinearLayout ll_mResetpassword;
	private LinearLayout ll_mBindphone;
	private LinearLayout ll_mBill;
	private LinearLayout ll_mGivebill;
	private LinearLayout ll_mQuesion;
	private LinearLayout ll_mAboutus;

	private Button bt_mSwitchuser;
	private LinearLayout ll_mChangehead;
	private TextView ll_mChongzhi;

	public PersonalView(Activity activity, Personal_dialog_ho personal_dialog_ho) {
		super(activity, personal_dialog_ho);
	}

	@Override
	public View initview() {
		mThisview = new Personalview_xml_ho(mActivity);

		return mThisview.initViewxml();
	}

	@Override
	public void initdata() {

		// 头像
		iv_mHead = mThisview.getIv_mHead();

		// 用户名
		tv_mUsename = mThisview.getTv_mUsename();

		// 最后登录
		tv_mLogintime = mThisview.getTv_mLogintime();

		// 余额
		tv_mBalance = mThisview.getTv_mBalance();

		// 重置密码
		ll_mResetpassword = mThisview.getLl_mResetpassword();

		// 绑定手机
		ll_mBindphone = mThisview.getLl_mBindphone();
		tv_bindphone = mThisview.getTv_bindphone();

		// 更换手机
		ll_mResetPhone = mThisview.getLl_mResetPhone();

		// 消费记录
		ll_mBill = mThisview.getLl_mBill();

		// 重置记录
		ll_mGivebill = mThisview.getLl_mGivebill();

		// 问题反馈
		ll_mQuesion = mThisview.getLl_mQuesion();

		// 关于我们
		ll_mAboutus = mThisview.getLl_mAboutus();

		// 切换账号
		bt_mSwitchuser = mThisview.getBt_mSwitchuser();

		// 打开更换头像窗口
		ll_mChangehead = mThisview.getLl_mChangehead();

		// 充值丫丫币
		ll_mChongzhi = mThisview.getTv_mChongzhi();

		
		//实名认证
		ll_mRealNameAuthentication = mThisview.getLl_mRealNameAuthentication();

		tv_mRealNameAuthentication = mThisview.getTv_mRealNameAuthentication();

		tv_fromcamera = mThisview.getTv_fromcamera();

		mUser = AgentApp.mUser;

		if (mUser.phoneActive == 1) {
			tv_bindphone.setText("已绑定");
			ll_mBindphone.setClickable(false);
		}

		// System.out.println("信息++++++"+mUser);
		tv_mLogintime.setText(mUser.last_login);

		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.clearDiskCache(mUser.icon + "?imageView/1/w/"
				+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
				+ Utilsjf.machSize(80, mActivity) + "/q/100");
		bitmapUtils.clearCache(mUser.icon + "?imageView/1/w/"
				+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
				+ Utilsjf.machSize(80, mActivity) + "/q/100");
		bitmapUtils.clearMemoryCache(mUser.icon + "?imageView/1/w/"
				+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
				+ Utilsjf.machSize(80, mActivity) + "/q/100");
		if ("".equals(mUser.nick) || mUser.nick == null) {
			tv_mUsename.setText(mUser.userName);
			// mAccountId.setText("");
		} else {
			tv_mUsename.setText(mUser.nick);
			// mAccountId.setText("(" + AgentApp.mUser.userName + ")");
		}
		if (mUser.money != null && !mUser.money.equals("")) {

			long money;
			try {
				money = Long.valueOf(mUser.money);
				if (money % 100 == 0) {
					tv_mBalance.setText((money / 100) + "");
				} else {
					// 除数
					BigDecimal bd = new BigDecimal(money);
					// 被除数
					BigDecimal bd2 = new BigDecimal(100);
					// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
					BigDecimal result = bd.divide(bd2, 2,
							BigDecimal.ROUND_HALF_UP);
					tv_mBalance.setText(result.toString());
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				tv_mBalance.setText("0");
				e.printStackTrace();
			}

		} else {
			tv_mBalance.setText(0 + "");
		}

		if (!TextUtils.isEmpty(mUser.icon)) {
			System.out.println("是这里");
			bitmapUtils
					.configDefaultLoadFailedImage(GetAssetsutils
							.getImageFromAssetsFile("yaya_default_head.png",
									mActivity));
			bitmapUtils.configDefaultLoadingImage(GetAssetsutils
					.getImageFromAssetsFile("yaya_defaultloading.png",
							mActivity));
			bitmapUtils.display(
					iv_mHead,
					mUser.icon + "?imageView/1/w/"
							+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
							+ Utilsjf.machSize(80, mActivity) + "/q/100");
			/*
			 * System.out.println("" + mUser.icon + "?imageView/1/w/" +
			 * Utilsjf.machSize(80, mActivity) + "/h/" + "" +
			 * Utilsjf.machSize(80, mActivity) + "/q/100");
			 */
		}

		//getUserdata();

		tv_fromcamera.setClickable(true);
		tv_fromcamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//
				Intent intent = new Intent(mContext, CropPhoto_Activity.class);
				intent.putExtra("tool", 0);
				mContext.startActivity(intent);
			}

		});

		// 从相册中上传图片
		tv_fromalbum = mThisview.getTv_fromalbum();
		tv_fromalbum.setClickable(true);
		tv_fromalbum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CropPhoto_Activity.class);
				intent.putExtra("tool", 1);
				mContext.startActivity(intent);
			}

		});

		// 重置密码
		ll_mResetpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ResetPassword_dialog resetPassword_ho_dialog = new ResetPassword_dialog(
						mActivity);
				resetPassword_ho_dialog.dialogShow();

			}
		});

		// 绑定手机
		ll_mBindphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Bindphone_dialog_ho bindphone_dialog_ho = new Bindphone_dialog_ho(
						mActivity, tv_bindphone, ll_mBindphone);
				bindphone_dialog_ho.dialogShow();
			}
		});

		// 消费记录
		ll_mBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bill_dialog_ho bill_dialog_ho = new Bill_dialog_ho(mActivity);
				bill_dialog_ho.dialogShow();
			}
		});

		// 更换绑定手机
		ll_mResetPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ResetPhone_dialog_ho resetPhone_dialog_ho = new ResetPhone_dialog_ho(
						mActivity, tv_bindphone, ll_mBindphone);
				resetPhone_dialog_ho.dialogShow();
			}
		});

		// 切换账号
		bt_mSwitchuser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AgentApp.mUser = null;
				// ViewConstants.HADLOGOUT = true;
				Sputils.putSPint("ischanageacount", 0,
						ViewConstants.mMainActivity);
				personal_dialog.onLogout();

			}
		});

		// 充值记录
		ll_mGivebill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Givebill_ho_dialog givebill_ho_dialog = new Givebill_ho_dialog(
						mActivity);
				givebill_ho_dialog.dialogShow();

			}
		});

		// 问题
		ll_mQuesion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Quesion_ho_dialog quesion_ho_dialog = new Quesion_ho_dialog(
						mActivity);
				quesion_ho_dialog.dialogShow();
			}
		});

		ll_mAboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Aboutus_dialog aboutus_dialog = new Aboutus_dialog(mActivity);
				aboutus_dialog.dialogShow();
			}
		});

		// 充值丫丫币
		ll_mChongzhi.setClickable(true);
		ll_mChongzhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*	Yayabipaydialog gamedata_ho_dialog = new Yayabipaydialog(
						mActivity);
				gamedata_ho_dialog.dialogShow();*/
			}
		});
		
		//实名认证
		ll_mRealNameAuthentication.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	new RealNameAuth_dialog(activity)
				RealNameAuth_dialog	 mRealNameAuth_dialog = new RealNameAuth_dialog(
						mActivity);
				mRealNameAuth_dialog.dialogShow();
			}
		});
	}

	private BitmapUtils bitmapUtils;
	private TextView tv_bindphone;
	private LinearLayout ll_mResetPhone;
	private LinearLayout ll_mRealNameAuthentication;
	private TextView tv_mRealNameAuthentication;

	/**
	 * 设置user的信息
	 */
	public void getUserdata() {

		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", mUser.uid + "");
		params.addBodyParameter("app_id", DeviceUtil.getGameId(mActivity));
		params.addBodyParameter("token", mUser.token);
		/*System.out.println("uid" + mUser.uid + "app_id"
				+ DeviceUtil.getGameId(mActivity) + "token" + mUser.token);
		System.out.println(ViewConstants.USERINFO_URL);*/
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, ViewConstants.loginurl, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// System.out.println("NIDONGDE" + responseInfo.result);
						User tempmUser = new User();
						try {
							
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
						//	Yayalog.loger("uid"+ mUser.uid+"token"+mUser.token + "app_id"+DeviceUtil.getGameId(mActivity)+"登陆获取++++++++++++++++++"+responseInfo.result);
							tempmUser.setLast_login(jsonObject
									.getString("last_login"));
							String relname = jsonObject.optString("relname");
						
							if (!TextUtils.isEmpty(relname)) {
								tempmUser.setRelname(relname);
							}
							tempmUser.setIcon(jsonObject.getString("icon"));
							tempmUser.setPhoneActive(jsonObject
									.getInt("phone_active"));
							tempmUser.setMoney(jsonObject
									.getString("amount"));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							// Toast.makeText(mContext, "请求数据失败,请检查网络",
							// 0).show();
						}
						setUserdata(tempmUser);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mContext, "请求数据失败,请检查网络", 0).show();
					}
				});

	}

	private void setUserdata(User tempmUser2) {

		//System.out.println("thatsme"+tempmUser2.toString());
		if (AgentApp.mUser == null) {
			return;
		}
		AgentApp.mUser.icon = tempmUser2.icon;
		AgentApp.mUser.last_login = tempmUser2.last_login;
		tv_mLogintime.setText(mUser.last_login);

		if (tempmUser2.phoneActive == 1) {
			tv_bindphone.setText("已绑定");
			ll_mBindphone.setClickable(false);
		}

		if (!TextUtils.isEmpty(tempmUser2.icon)) {

			bitmapUtils.clearDiskCache(tempmUser2.icon + "?imageView/1/w/"
					+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
					+ Utilsjf.machSize(80, mActivity) + "/q/100");
			bitmapUtils.clearCache(tempmUser2.icon + "?imageView/1/w/"
					+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
					+ Utilsjf.machSize(80, mActivity) + "/q/100");
			bitmapUtils.clearMemoryCache(tempmUser2.icon + "?imageView/1/w/"
					+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
					+ Utilsjf.machSize(80, mActivity) + "/q/100&123");

			bitmapUtils.configDefaultLoadingImage(GetAssetsutils
					.getImageFromAssetsFile("yaya_defaultloading.png",
							mActivity));
			bitmapUtils
					.configDefaultLoadFailedImage(GetAssetsutils
							.getImageFromAssetsFile("yaya_default_head.png",
									mActivity));
			bitmapUtils.display(
					iv_mHead,
					tempmUser2.icon + "?imageView/1/w/"
							+ Utilsjf.machSize(80, mActivity) + "/h/" + ""
							+ Utilsjf.machSize(80, mActivity) + "/q/100");
			/*
			 * System.out.println("" + tempmUser2.icon + "?imageView/1/w/" +
			 * Utilsjf.machSize(80, mActivity) + "/h/" + "" +
			 * Utilsjf.machSize(80, mActivity) + "/q/100");
			 */
		}

		if (tempmUser2.money!=mUser.money&&tempmUser2.money != null && !tempmUser2.money.equals("")) {

			long money;
			try {
				money = Long.valueOf(tempmUser2.money);
				if (money % 100 == 0) {
					tv_mBalance.setText((money / 100) + "");
				} else {
					// 除数
					BigDecimal bd = new BigDecimal(money);
					// 被除数
					BigDecimal bd2 = new BigDecimal(100);
					// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
					BigDecimal result = bd.divide(bd2, 2,
							BigDecimal.ROUND_HALF_UP);
					tv_mBalance.setText(result.toString());
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				tv_mBalance.setText("0");
				e.printStackTrace();
			}

		} else {
			tv_mBalance.setText(0 + "");
		}

		if (!TextUtils.isEmpty(tempmUser2.getRelname())) {
			tv_mRealNameAuthentication.setText("已认证");
			ll_mRealNameAuthentication.setClickable(false);
		}
	}

	@Override
	public void onResume() {
		getUserdata();
		ll_mChangehead.setVisibility(View.GONE);
		super.onResume();
	}

}
