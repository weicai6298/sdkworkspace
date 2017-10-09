package com.kkgame.sdk.pay;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.BankInfo;
import com.kkgame.sdk.bean.BillResult;
import com.kkgame.sdk.bean.ConfirmPay;
import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.PayResult;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.utils.AuthNumReceiver;
import com.kkgame.sdk.utils.AuthNumReceiver.MessageListener;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CardInfo;
import com.kkgame.sdk.utils.CardNumTextWatcher;
import com.kkgame.sdk.utils.CodeCountDown;
import com.kkgame.sdk.utils.DialogUtil;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Loginpo_listviewitem;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;

public class XinyongkapayDialog extends Basedialogview implements
		KgameSdkPaymentCallback {

	private LinearLayout ll_mHelp;
	private LinearLayout ll_mDele;
	private EditText et_mCarnumber;
	private EditText et_mCarname;
	private EditText et_mCarphone;
	private Button bt_mOk;
	private EditText et_mCarsafecode;
	private EditText et_mCardate;
	private LinearLayout ll_firstpay;
	private LinearLayout ll_secondpay;
	private ListView lv_mHistorypay;
	private TextView tv_mOther;
	private Button bt_mOkkuai;
	private ArrayList<BankInfo> mBankList;
	private KgameSdkPaymentCallback mPaymentCallback;
	private EditText mCardNum;
	private EditText mPhoneNum;
	private EditText mName;
	private Object mCardId;
	private TextView mMoreBank;
	private Button mPaynow;
	private EditText mValperiod;
	private EditText mIdenNum;
	private Button mPaynow_kuai;
	private LinearLayout mBankInfo;
	private ListView mGridView;
	private LinearLayout mWarning;
	private String mCardNumText;
	private String mPhoneNumText;
	private String mValperiodText;
	private String mIdenNumText;
	private BankInfo mBank;

	private TextView tv_mOlderpay;
	private TextView mMorePay;
	private PayResult mFirstResult;

	private static final long serialVersionUID = 1L;

	private WindowManager wWindowManager;

	private static final int FIRSTRESULT = 2;

	private static final int CONFIRMRESULT = 3;

	private static final int CONFIRMRESULTKUAI = 5;

	protected static final int FIRSTRESULTKUAI = 4;

	private static final int BILLRESULT = 6;

	private static final int DATAERROR = 17;

	private static final int NETERROR = 18;
	private static final int COUNTDOWN = 7;

	private static final int ERROR = 10;

	private BillResult mBillResult;
	private CodeDialog_jf mCodeDialog;
	private EditText mCode;
	private Button mGetCode;
	private CodeCountDown mCodeCountDown;

	public XinyongkapayDialog(Activity activity) {
		super(activity);
	}

	private ConfirmPay mConfirmPay;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			DialogUtil.dismissDialog();
			// mPaynow.setEnabled(true);
			// mPaynow_kuai.setEnabled(true);
			switch (msg.what) {
			case FIRSTRESULT:
				if (mFirstResult.success == 1) {
					// 操作失败
					onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果
					mFirstResult.params.put("cardno", mCardNumText);
					mFirstResult.params.put("validthru", mValperiodText);
					mFirstResult.params.put("cvv2", mIdenNumText);
					mFirstResult.params.put("mobile", mPhoneNumText);
					mFirstResult.params.put("uid", AgentApp.mUser.uid + "");
					mFirstResult.params.put("token", AgentApp.mUser.token);
					mFirstResult.params.put("app_id",
							DeviceUtil.getGameId(mContext));
					mFirstResult.params.put("ver", "1");
					System.out.println(mCardNumText);
					/*new Thread() {

						@Override
						public void run() {
							try {
								mConfirmPay = ObtainData
										.confirmPay(mFirstResult);
								mHandler.sendEmptyMessage(CONFIRMRESULT);
							} catch (Exception e) {
								mHandler.sendEmptyMessage(DATAERROR);
							}
						}
					}.start();*/
				}
				break;
			case FIRSTRESULTKUAI:
				if (mFirstResult.success == 1) {
					// 操作失败
					onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果
					cofirmPay();
				}
				break;
			case CONFIRMRESULT:
				if (mConfirmPay != null) {
					if (mConfirmPay.success == 1) {
						// 第二次确认操作失败
						onError(0);
						ToastUtil.showError(mContext, mConfirmPay.error_msg);
					} else if (mConfirmPay.success == 0) {
						DialogUtil.showDialog(mContext, "支付结果确认中...");
						AgentApp.mPayOrder.id = mFirstResult.params
								.get("p2_Order");
						// 查询订单状态
						/*new Thread() {

							@Override
							public void run() {
								try {
									Thread.sleep(6 * 1000);
									mBillResult = ObtainData.getBillResult(
											mContext, AgentApp.mUser,
											AgentApp.mPayOrder);
									if (mBillResult.error_code == 701) {

										Thread.sleep(5 * 1000);
										mBillResult = ObtainData.getBillResult(
												mContext, AgentApp.mUser,
												AgentApp.mPayOrder);
									}
									mHandler.sendEmptyMessage(BILLRESULT);
								} catch (Exception e) {
									e.printStackTrace();
									mHandler.sendEmptyMessage(DATAERROR);
								}
							}

						}.start();*/

					}
				}

				break;
			case CONFIRMRESULTKUAI:
				if (mConfirmPay != null) {
					if (mConfirmPay.success == 1) {
						// // 绑卡支付第二次一定失败
						// 弹出对话框输入验证码
						if (mCodeDialog == null) {
							mCodeDialog = new CodeDialog_jf(mActivity);
							mCodeDialog.setCancelable(false);
							mCodeDialog.setLoadText(mConfirmPay.error_msg);
							mCode = mCodeDialog.getEt_mPhone();
							mGetCode = mCodeDialog.getBt_mGetsecurity();
							mCodeDialog.setPayNow(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									String trim = mCode.getText().toString()
											.trim();
									if ("".equals(trim) || trim.length() == 0) {
										Toast.makeText(mContext, "请输入验证码",
												Toast.LENGTH_LONG).show();
									} else {
										mFirstResult.params
												.put("smscode", trim);
										DialogUtil.showDialog(mContext,
												"正在请求支付...");
										/*new Thread() {
											@Override
											public void run() {
												// 第三次,绑卡支付
												try {
													mConfirmPay = ObtainData
															.confirmPay(mFirstResult);
													mHandler.sendEmptyMessage(CONFIRMRESULT);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}
										}.start();*/
									}
								}
							});
							mCodeDialog.setGetCode(new OnClickListener() {

								@Override
								public void onClick(View v) {
									mCodeDialog.dialogDismiss();
									mCodeDialog = null;
									getCodeagain();
								}
							});
							mCodeDialog
									.setOnDismissListener(new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											mCodeCountDown.cancel();
										}
									});

						}
						mCodeDialog.dialogShow();
						;
						if (mCodeCountDown == null) {
							mCodeCountDown =  new CodeCountDown(60000,1000,
									mGetCode);
						}
						mCodeCountDown.start();
					}
					System.out.println("mconfi不等于1");
				}
				System.out.println("没有mconfi");
				break;
			case BILLRESULT:
				if (mBillResult != null) {
					if (mBillResult.success == 1) {
						// 第二次确认操作失败
						//System.out.println("第二次确认信用卡操作失败");
						onError(0);
						ToastUtil.showError(mContext, mBillResult.error_msg);
					} else if (mBillResult.success == 0) {
						//System.out.println("第二次确认信用卡操作成功");
						// 支付操作成功等待到账

						onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
						ToastUtil.showSuccess(mContext, mBillResult.body);

					}
				}

				break;
			case ERROR:
				ToastUtil.showError(mContext, "获取支付结果失败");

				break;

			case DATAERROR:
				Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();

				break;
			default:
				Toast.makeText(mContext, "数据异常，请再次支付。", Toast.LENGTH_LONG)
						.show();

				break;
			}
		}

	};
	private AuthNumReceiver mAuthNumReceiver;

	public void getCodeagain() {
		DialogUtil.showDialog(mContext, "正在请求验证码...");
		mFirstResult.params.put("cardno", mCardNumText);
		mFirstResult.params.put("validthru", mValperiodText);
		mFirstResult.params.put("cvv2", mIdenNumText);
		mFirstResult.params.put("mobile", mPhoneNumText);
		mFirstResult.params.put("uid", AgentApp.mUser.uid + "");
		mFirstResult.params.put("token", AgentApp.mUser.token);
		mFirstResult.params.put("app_id", DeviceUtil.getGameId(mContext));
		mFirstResult.params.put("ver", "1");
		/*new Thread() {

			@Override
			public void run() {
				try {
					mConfirmPay = ObtainData.confirmPay(mFirstResult);
					mHandler.sendEmptyMessage(CONFIRMRESULTKUAI);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();*/
	}

	@Override
	public void createDialog(final Activity mActivity) {

		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 630, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 620, 0, "LinearLayout", 20,
				20, 20, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 删除行
		LinearLayout ll_deleline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_deleline, MATCH_PARENT, 50, 0,
				mLinearLayout, 7, 20, 5, 0, 100);
		ll_deleline.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
				mLinearLayout);

		ll_mHelp = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mHelp, 70, MATCH_PARENT, mLinearLayout);
		ll_mHelp.setGravity(Gravity_CENTER);

		ImageView iv_help = new ImageView(mActivity);
		machineFactory.MachineView(iv_help, 50, 50, mLinearLayout, 3, 15);
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
		machineFactory.MachineView(iv_dele, 40, 40, mLinearLayout);
		iv_dele.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_leftpre.png", mActivity));
		iv_dele.setClickable(false);

		ll_mDele.addView(iv_dele);
		ll_mDele.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialogDismiss();
			}
		});

		// TODO
		ll_deleline.addView(ll_mDele);
		ll_deleline.addView(ll_zhanwei);
		ll_deleline.addView(ll_mHelp);

		// 首次支付
		ll_firstpay = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_firstpay, MATCH_PARENT, MATCH_PARENT, 0,
				mLinearLayout, 20, 0, 20, 0, 100);
		ll_firstpay.setOrientation(LinearLayout.VERTICAL);

		// 信用卡卡号
		et_mCarnumber = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarnumber, MATCH_PARENT, 100, 0,
				"信用卡卡号", 32, mLinearLayout, 0, 20, 0, 0);
		et_mCarnumber.setTextColor(Color.BLACK);
		et_mCarnumber.setSingleLine();
		et_mCarnumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		// 用户信息行
		LinearLayout ll_userline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_userline, MATCH_PARENT, 100,
				mLinearLayout, 2, 15);
		// 持卡人姓名
		et_mCarname = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarname, 0, 100, 1, "持卡人姓名", 32,
				mLinearLayout, 0, 0, 0, 0);
		et_mCarname.setTextColor(Color.BLACK);
		// 持卡人预留手机号
		et_mCarphone = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarphone, 0, 100, 1, "银行预留手机号", 32,
				mLinearLayout, 15, 0, 0, 0);
		et_mCarphone.setTextColor(Color.BLACK);

		// TODO
		ll_userline.addView(et_mCarname);
		ll_userline.addView(et_mCarphone);

		// 信用卡信息行
		LinearLayout ll_usercar = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_usercar, MATCH_PARENT, 100,
				mLinearLayout, 2, 15);
		// 有效期
		et_mCardate = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCardate, 0, 100, 1, "有效期(0421)", 32,
				mLinearLayout, 0, 0, 0, 0);
		et_mCardate.setTextColor(Color.BLACK);
		// 安全码
		et_mCarsafecode = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarsafecode, 0, 100, 1, "安全码(899)",
				32, mLinearLayout, 15, 0, 0, 0);
		et_mCarsafecode.setTextColor(Color.BLACK);

		// TODO
		ll_usercar.addView(et_mCardate);
		ll_usercar.addView(et_mCarsafecode);

		// 提示
		LinearLayout ll_tip = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_tip, MATCH_PARENT, 40, mLinearLayout, 1,
				10);

		LinearLayout ll_tip1 = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_tip1, 0, 40, 1, mLinearLayout, 0, 0, 0,
				0, 100);

		ImageView iv_tip1 = new ImageView(mActivity);
		machineFactory.MachineView(iv_tip1, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		iv_tip1.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_juxing.png", mActivity));

		TextView tv_tip1 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip1, WRAP_CONTENT, WRAP_CONTENT, 0,
				"卡正面有效期,月份/年份", 22, mLinearLayout, 0, 0, 0, 0);

		// TODO
		ll_tip1.addView(iv_tip1);
		ll_tip1.addView(tv_tip1);

		LinearLayout ll_tip2 = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_tip2, 0, 40, 1, mLinearLayout, 15, 0, 0,
				0, 100);

		ImageView iv_tip2 = new ImageView(mActivity);
		machineFactory.MachineView(iv_tip2, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		iv_tip2.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_juxing.png", mActivity));

		TextView tv_tip2 = new TextView(mActivity);
		machineFactory.MachineTextView(tv_tip2, WRAP_CONTENT, WRAP_CONTENT, 0,
				"卡背面签名栏后三位数", 22, mLinearLayout, 0, 0, 0, 0);

		// TODO
		ll_tip2.addView(iv_tip2);
		ll_tip2.addView(tv_tip2);

		// TODO
		ll_tip.addView(ll_tip1);
		ll_tip.addView(ll_tip2);

		// button的确认
		bt_mOk = new Button(mContext);
		bt_mOk = machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 90, 0,
				"确认", 30, mLinearLayout, 0, 30, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_firstpay.addView(et_mCarnumber);
		ll_firstpay.addView(ll_userline);
		ll_firstpay.addView(ll_usercar);
		ll_firstpay.addView(ll_tip);
		// ll_firstpay.addView(tv_mOlderpay);
		ll_firstpay.addView(bt_mOk);

		ll_content.addView(ll_deleline);
		ll_content.addView(ll_firstpay);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度

		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(true);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		// ap2.addRule(RelativeLayout.BELOW, );
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		ll_mHelp.setClickable(true);
		ll_mHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Xinyongka_help_dialog xinyongka_help_dialog = new Xinyongka_help_dialog(
						mActivity);
				xinyongka_help_dialog.dialogShow();
			}
		});
		// dialog.show();
		initlogic();
		//当窗口关闭时将支付控制器打开
				dialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						Yayapaymain_jf.payclickcontrol=false;
					}
				});
	}

	private void initlogic() {

		onStart();

		mBankList = AgentApp.mUser.banks;
		mPaymentCallback = KgameSdk.mPaymentCallback;

		mCardNum = et_mCarnumber;
		mPhoneNum = et_mCarphone;
		mName = et_mCarname;

		mValperiod = et_mCardate;
		mIdenNum = et_mCarsafecode;

		mMoreBank = tv_mOther;

		mPaynow = bt_mOk;

		mPaynow_kuai = bt_mOkkuai;
		mBankInfo = ll_mHelp;

		mGridView = lv_mHistorypay;
		mWarning = ll_mHelp;
		mMorePay = tv_mOlderpay;

		mCardNum.setText(AgentApp.mCardInfos.get(CardInfo.CREDITCARDNUM));
		mPhoneNum.setText(AgentApp.mCardInfos.get(CardInfo.CREDITCARDPHONE));
		mValperiod.setText(AgentApp.mCardInfos
				.get(CardInfo.CREDITCARDPVALPERIOD));
		mIdenNum.setText(AgentApp.mCardInfos.get(CardInfo.CREDITCARIDENNUM));
		mCardNum.addTextChangedListener(new CardNumTextWatcher(mCardNum));

		mPaynow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCardNumText = mCardNum.getText().toString().trim()
						.replace(" ", "");
				mPhoneNumText = mPhoneNum.getText().toString().trim()
						.replace(" ", "");
				mValperiodText = mValperiod.getText().toString().trim();
				mIdenNumText = mIdenNum.getText().toString().trim();

				if ("".equals(mCardNumText)) {
					Toast.makeText(mContext, "请输入卡号", Toast.LENGTH_SHORT)
							.show();
				} else if (mCardNumText.length() > 20) {
					Toast.makeText(mContext, "卡号不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mValperiodText)) {
					Toast.makeText(mContext, "请输入有效期", Toast.LENGTH_SHORT)
							.show();
				} else if (mValperiodText.length() > 20) {
					Toast.makeText(mContext, "有效期不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mIdenNumText)) {
					Toast.makeText(mContext, "请输入CVV2码", Toast.LENGTH_SHORT)
							.show();
				} else if (mIdenNumText.length() > 20) {
					Toast.makeText(mContext, "CVV2码不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if (mIdenNumText.length() < 3) {
					Toast.makeText(mContext, "CVV2码不能小于3位", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mPhoneNumText)) {
					Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNumText.length() < 11) {
					Toast.makeText(mContext, "手机号不能小于11位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPhoneNumText.length() > 20) {
					Toast.makeText(mContext, "手机号不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 全部输入正常,访问网络,提交数据,进入支付流程
					DialogUtil.showDialog(mContext, "正在请求支付...");
					mPaynow.setEnabled(false);
					AgentApp.mCardInfos.put(CardInfo.CREDITCARDNUM,
							mCardNumText);
					AgentApp.mCardInfos.put(CardInfo.CREDITCARDPHONE,
							mPhoneNumText);
					AgentApp.mCardInfos.put(CardInfo.CREDITCARDPVALPERIOD,
							mValperiodText);
					AgentApp.mCardInfos.put(CardInfo.CREDITCARIDENNUM,
							mIdenNumText);
					/*new Thread() {

						@Override
						public void run() {
							try {
								mFirstResult = ObtainData.creditCardPayment(
										mContext, AgentApp.mPayOrder,
										AgentApp.mUser, mPhoneNumText,
										AgentApp.mPayOrder.paytype);
								mHandler.sendEmptyMessage(FIRSTRESULT);

							} catch (Exception e) {
								mHandler.sendEmptyMessage(NETERROR);
							}
						}

					}.start();*/

				}

			}
		});

	}

	public class MoneyListAdapter_jf extends BaseAdapter {

		private Context mContext;

		class ViewHolder {

			TextView mName;
			ImageView mDelete;
		}

		public int getCount() {
			return mBankList.size();
		}

		public Object getItem(int position) {
			return mBankList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				Loginpo_listviewitem loginpo_listviewitem = new Loginpo_listviewitem(
						mActivity);
				convertView = loginpo_listviewitem.initViewxml();
				holder.mName = (TextView) loginpo_listviewitem.getTextView();
				holder.mDelete = (ImageView) loginpo_listviewitem
						.getImageView();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final String name = mBankList.get(position).bankname;
			holder.mName.setText(name + "(" + mBankList.get(position).lastno
					+ ")");
			holder.mDelete.setVisibility(View.GONE);

			return convertView;
		}

	}

	private void cofirmPay() {
		// 第一次操作成功,再次访问网络获取第二次结果
		mFirstResult.params.put("uid", AgentApp.mUser.uid + "");
		mFirstResult.params.put("token", AgentApp.mUser.token);
		mFirstResult.params.put("app_id", DeviceUtil.getGameId(mContext));
		mFirstResult.params.put("bind_id", mBank.id);
		DialogUtil.showDialog(mContext, "正在获取验证码...");
		/*new Thread() {
			@Override
			public void run() {
				try {
					mConfirmPay = ObtainData.confirmPay(mFirstResult);
					mHandler.sendEmptyMessage(CONFIRMRESULTKUAI);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(NETERROR);
				}
			}
		}.start();*/
	}

	@Override
	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
		}
		mPaymentCallback = null;
		if (mCodeDialog != null) {
			mCodeDialog.dialogDismiss();
		}
		dialogDismiss();
		mActivity.unregisterReceiver(mAuthNumReceiver);
		mActivity.finish();

	}

	@Override
	public void onError(int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		mPaymentCallback = null;
		if (mCodeDialog != null) {
			mCodeDialog.dialogDismiss();
		}
		dialogDismiss();
		mActivity.unregisterReceiver(mAuthNumReceiver);
		mActivity.finish();
	}

	@Override
	public void onCancel() {
		if (mPaymentCallback != null) {
			mPaymentCallback.onCancel();
		}
		mPaymentCallback = null;
		if (mCodeDialog != null) {
			mCodeDialog.dialogDismiss();
		}
		dialogDismiss();
		mActivity.unregisterReceiver(mAuthNumReceiver);
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
				// System.out.println("接收到的短信+++++++"+message);
				if (mCode != null) {
					mCode.setText(message);
				}
			}
		});
		// super.onStart();
	}

}
