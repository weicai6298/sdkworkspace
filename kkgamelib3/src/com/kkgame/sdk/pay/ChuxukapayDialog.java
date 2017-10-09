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
import com.kkgame.sdk.utils.PhoneNumTextWatcher;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Loginpo_listviewitem;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;



public class ChuxukapayDialog extends Basedialogview implements
		KgameSdkPaymentCallback {

	private Button bt_mOk;
	private LinearLayout ll_mHelp;
	private LinearLayout ll_mDele;
	private EditText et_mCarnumber;
	private EditText et_mCarname;
	private EditText et_mCarphone;
	private EditText et_mUserid;
	private ListView lv_mlistview;
	private ListView lv_mHistorypay;
	private TextView tv_mOther;
	private Button bt_mOkkuai;
	private EditText mCardNum;
	private EditText mPhoneNum;
	private EditText mName;
	private EditText mCardId;
	private TextView mMoreBank;
	private Button mPaynow;
	private Button mPaynow_kuai;
	private LinearLayout mBankInfo;
	private ListView mGridView;
	private LinearLayout mWarning;
	private TextView tv_mOlderpay;
	private TextView mMorePay;
	private LinearLayout ll_firstpay;
	private LinearLayout ll_secondpay;
	private BankInfo mBank;
	private String mCardNumText;
	private PayResult mFirstResult;
	private String mNameText;
	private String mCardIdText;
	private String mPhoneNumText;

	private ConfirmPay mConfirmPay;
	private BillResult mBillResult;
	private CodeDialog_jf mCodeDialog;
	private EditText mCode;
	private Button mGetCode;
	private CodeCountDown mCodeCountDown;
	private boolean flag;
	private ArrayList<BankInfo> mBankList;

	private static final int FIRSTRESULT = 2;

	private static final int CONFIRMRESULT = 3;

	private static final int CONFIRMRESULTKUAI = 5;

	protected static final int FIRSTRESULTKUAI = 4;

	private static final int BILLRESULT = 6;

	private static final int DATAERROR = 17;

	private static final int NETERROR = 18;

	private static final int COUNTDOWN = 7;

	private static final int ERROR = 10;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			
			// mPaynow.setEnabled(true);
			// mPaynow_kuai.setEnabled(true);
			switch (msg.what) {
			case FIRSTRESULT:
				if (mFirstResult.success == 1) {
					DialogUtil.dismissDialog();
					// 操作失败
					onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果
					DialogUtil.dismissDialog();
					getCodeagain();
				}
				break;
			case FIRSTRESULTKUAI:
				if (mFirstResult.success == 1) {
					DialogUtil.dismissDialog();
					// 操作失败
					onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果
					DialogUtil.dismissDialog();
					cofirmPay();
				}
				break;
			case CONFIRMRESULT:
				if (mConfirmPay != null) {
					if (mConfirmPay.success == 1) {
						// 第二次确认操作失败
						DialogUtil.dismissDialog();
						onError(0);
						ToastUtil.showError(mContext, mConfirmPay.error_msg);
					} else if (mConfirmPay.success == 0) {
						DialogUtil.dismissDialog();
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
									
								}
							}

						}.start();*/

					}
				}else{
					mHandler.sendEmptyMessage(DATAERROR);
				}

				break;
			case CONFIRMRESULTKUAI:
				DialogUtil.dismissDialog();
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
													mHandler.sendEmptyMessage(DATAERROR);
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
									mCodeDialog=null;
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
				}
				break;
			case BILLRESULT:
				DialogUtil.dismissDialog();
				if (mBillResult != null) {
					if (mBillResult.success == 1) {
						// 第二次确认操作失败
						//System.out.println("第二次确认操作失败");
						ToastUtil.showError(mContext, mBillResult.error_msg);
						onError(0);
					} else if (mBillResult.success == 0) {

						// 支付操作成功等待到账

						ToastUtil.showSuccess(mContext, mBillResult.body);
						onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);

					}
				}

				break;
				
			case COUNTDOWN:
				DialogUtil.dismissDialog();
				int count = (Integer) msg.obj;
				if (mGetCode != null) {
					if (count > 0) {
						mGetCode.setText("重新获取(" + count + ")");
					} else {
						mGetCode.setText("获取验证码");
						mGetCode.setEnabled(true);
						flag = false;
					}
				}

				break;
			case ERROR:
				DialogUtil.dismissDialog();
				ToastUtil.showError(mContext, "获取支付结果失败");
				break;
			case DATAERROR:
				DialogUtil.dismissDialog();
				Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();
				onError(0);
				break;
			default:
				DialogUtil.dismissDialog();
				Toast.makeText(mContext, "数据异常，请再次支付。", Toast.LENGTH_LONG)
						.show();
				onError(0);
				break;
			}
		}

	};
	private KgameSdkPaymentCallback mPaymentCallback;
	private AuthNumReceiver mAuthNumReceiver;

	
	
	
	public ChuxukapayDialog(Activity activity) {
		super(activity);
		
	}
	
	private void getCodeagain() {
		// TODO Auto-generated method stub
		DialogUtil.showDialog(mContext, "正在请求验证码...");
		mFirstResult.params.put("cardno", mCardNumText);
		mFirstResult.params.put("password", "");
		mFirstResult.params.put("realname", mNameText);
		mFirstResult.params.put("phone", mPhoneNumText);
		mFirstResult.params.put("idno", mCardIdText);
		mFirstResult.params.put("uid", AgentApp.mUser.uid + "");
		mFirstResult.params.put("token", AgentApp.mUser.token);
		mFirstResult.params.put("app_id",
				DeviceUtil.getGameId(mContext));
		mFirstResult.params.put("ver", "1");
		/*new Thread() {

			@Override
			public void run() {
				try {
					mConfirmPay = ObtainData
							.confirmPay(mFirstResult);
					mHandler.sendEmptyMessage(CONFIRMRESULTKUAI);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(NETERROR);
				}
			}
		}.start();*/
	}

	@Override
	public void createDialog(Activity mActivity) {
		// mCodeDialog

		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 720, 650, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 650, 620, 0, "LinearLayout", 20,
				25, 20, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 删除行
		LinearLayout ll_deleline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_deleline, MATCH_PARENT, 50, 0,
				mLinearLayout, 10, 20, 5, 0, 100);
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
		machineFactory.MachineView(ll_mDele, 60, MATCH_PARENT, mLinearLayout);
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
				mLinearLayout, 20, 5, 20, 0, 100);
		ll_firstpay.setOrientation(LinearLayout.VERTICAL);

		// 储蓄卡卡号
		et_mCarnumber = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarnumber, MATCH_PARENT, 100, 0,
				"储蓄卡卡号", 32, mLinearLayout, 0, 20, 0, 0);
		et_mCarnumber.setTextColor(Color.BLACK);
		et_mCarnumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		et_mCarnumber.setSingleLine();

		// 用户信息行
		LinearLayout ll_userline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_userline, MATCH_PARENT, 100,
				mLinearLayout, 2, 15);
		// 持卡人姓名
		et_mCarname = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarname, 0, 100, 1, "持卡人姓名", 32,
				mLinearLayout, 0, 0, 0, 0);
		et_mCarname.setTextColor(Color.BLACK);
		et_mCarname.setSingleLine();

		// 持卡人预留手机号
		et_mCarphone = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarphone, 0, 100, 2, "银行预留手机号", 32,
				mLinearLayout, 15, 0, 0, 0);
		et_mCarphone.setTextColor(Color.BLACK);
		et_mCarphone.setSingleLine();

		// TODO
		ll_userline.addView(et_mCarname);
		ll_userline.addView(et_mCarphone);

		// 持卡人身份证号码
		et_mUserid = new EditText(mActivity);
		machineFactory.MachineEditText(et_mUserid, MATCH_PARENT, 100, 0,
				"持卡人身份证号码", 32, mLinearLayout, 0, 15, 0, 0);
		et_mUserid.setTextColor(Color.BLACK);
		et_mUserid.setSingleLine();

		// button的确认
		bt_mOk = new Button(mContext);
		bt_mOk = machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 90, 0,
				"确认", 30, mLinearLayout, 0, 60, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_firstpay.addView(et_mCarnumber);
		ll_firstpay.addView(ll_userline);
		ll_firstpay.addView(et_mUserid);
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

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
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
		mBankList = AgentApp.mUser.cashbanks;
		mPaymentCallback = KgameSdk.mPaymentCallback;
		mCardNum = et_mCarnumber;
		mPhoneNum = et_mCarphone;
		mName = et_mCarname;
		mCardId = et_mUserid;
		mMoreBank = tv_mOther;

		mPaynow = bt_mOk;
		mPaynow_kuai = bt_mOkkuai;
		mBankInfo = ll_mHelp;

		mGridView = lv_mHistorypay;
		mWarning = ll_mHelp;
		mMorePay = tv_mOlderpay;

		// 监听输入框变动,按照格式加空格
		mCardNum.addTextChangedListener(new CardNumTextWatcher(mCardNum));
		mCardId.addTextChangedListener(new PhoneNumTextWatcher(mPhoneNum));

		mCardNum.setText(AgentApp.mCardInfos.get(CardInfo.CASHCARDNUM));
		mPhoneNum.setText(AgentApp.mCardInfos.get(CardInfo.CASHCARDPHONE));
		mName.setText(AgentApp.mCardInfos.get(CardInfo.CASHCARDNAME));
		mCardId.setText(AgentApp.mCardInfos.get(CardInfo.CASHCARDID));

		mPaynow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCardNumText = mCardNum.getText().toString().trim()
						.replace(" ", "");
				mNameText = mName.getText().toString().trim();
				mCardIdText = mCardId.getText().toString().trim();
				mPhoneNumText = mPhoneNum.getText().toString().trim()
						.replace(" ", "");

				if ("".equals(mCardNumText)) {
					Toast.makeText(mContext, "请输入卡号", Toast.LENGTH_SHORT)
							.show();
				} else if (mCardNumText.length() > 20) {
					Toast.makeText(mContext, "卡号不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mNameText)) {
					Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mCardIdText)) {
					Toast.makeText(mContext, "请输入身份证号", Toast.LENGTH_SHORT)
							.show();
				} else if (mCardIdText.length() < 16) {
					Toast.makeText(mContext, "身份证号不能小于18位", Toast.LENGTH_SHORT)
							.show();
				} else if (mCardIdText.length() > 20) {
					Toast.makeText(mContext, "身份证号不能大于20位", Toast.LENGTH_SHORT)
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
					mPaynow.setEnabled(false);

					AgentApp.mCardInfos.put(CardInfo.CASHCARDNUM, mCardNumText);
					AgentApp.mCardInfos.put(CardInfo.CASHCARDPHONE,
							mPhoneNumText);
					AgentApp.mCardInfos.put(CardInfo.CASHCARDNAME, mNameText);
					AgentApp.mCardInfos.put(CardInfo.CASHCARDID, mCardIdText);

					// 全部输入正常,访问网络,提交数据,进入支付流程
					DialogUtil.showDialog(mContext, "正在请求支付...");
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

		// 弹出帮助对话框

		ll_mHelp.setClickable(true);
		ll_mHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Chuxuka_help_dialog chuxuka_help_dialog = new Chuxuka_help_dialog(
						mActivity);
				chuxuka_help_dialog.dialogShow();
			}
		});

	}

	public void setListviewheight(int size) {
		lv_mHistorypay.getLayoutParams().height = machSize((size * 100));
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
