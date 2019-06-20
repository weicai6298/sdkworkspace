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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.kkgame.sdk.utils.CodeCountDown;
import com.kkgame.sdk.utils.DialogUtil;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Loginpo_listviewitem;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;



public class Chuxuka_historypayDialog extends Basedialogview implements
		KgameSdkPaymentCallback {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button bt_mOk;
	private LinearLayout ll_mHelp;
	private LinearLayout ll_mDele;
	private EditText et_mCarnumber;
	private EditText et_mCarname;
	private EditText et_mCarphone;
	private EditText et_mUserid;
	private ListView lv_mlistview;
	private ListView lv_mHistorypay;

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
	private EditText mCode_his;
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
				break;
			case FIRSTRESULTKUAI:
				if (mFirstResult.success == 1) {
					DialogUtil.dismissDialog();
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
						DialogUtil.dismissDialog();
						// 第二次确认操作失败
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
									mHandler.sendEmptyMessage(DATAERROR);
								}
							}

						}.start();*/

					}
				}else{
					DialogUtil.dismissDialog();
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
							mCode_his = mCodeDialog.getEt_mPhone();
							mGetCode = mCodeDialog.getBt_mGetsecurity();
							mCodeDialog.setPayNow(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									String trim = mCode_his.getText().toString()
											.trim();
									if ("".equals(trim) || trim.length() == 0) {
										Toast.makeText(mContext, "请输入验证码",
												Toast.LENGTH_LONG).show();
									} else {
										mFirstResult.params
												.put("smscode", trim);
										DialogUtil.dismissDialog();
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
									cofirmPay();
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
						
						if (mCodeCountDown == null) {
							mCodeCountDown =  new CodeCountDown(60000,1000,
									mGetCode);
						}
						mCodeCountDown.start();
					}
				}else{
					
				}
				break;
			case BILLRESULT:
				DialogUtil.dismissDialog();
				if (mBillResult != null) {
					if (mBillResult.success == 1) {
						// 第二次确认操作失败

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
	private ImageView iv_mOther;


	public  Chuxuka_historypayDialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {
		// mCodeDialog

		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, 650, 620, "LinearLayout");
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
				mLinearLayout, 5, 20, 5, 0, 100);
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

		// 第二次直接进入历史支付
		ll_secondpay = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_secondpay, MATCH_PARENT, 400,
				mLinearLayout);
		ll_secondpay.setOrientation(LinearLayout.VERTICAL);
		ll_secondpay.setGravity(Gravity.CENTER_HORIZONTAL);

		// 历史支付记录
		lv_mHistorypay = new ListView(mActivity);
		machineFactory.MachineView(lv_mHistorypay, MATCH_PARENT, 100, 0,
				mLinearLayout, 20, 10, 20, 0, 100);
		lv_mHistorypay.setDivider(null);

		iv_mOther = new ImageView(mActivity);
		machineFactory.MachineView(iv_mOther, 80, 80, mLinearLayout, 2, 10);
		iv_mOther.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_chuxukaadd.png", mActivity));

		// button的确认
		bt_mOkkuai = new Button(mContext);
		bt_mOkkuai = machineFactory.MachineButton(bt_mOkkuai, MATCH_PARENT, 90,
				0, "确认", 30, mLinearLayout, 20, 30, 20, 0);
		bt_mOkkuai.setTextColor(Color.WHITE);
		bt_mOkkuai.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOkkuai.setGravity(Gravity_CENTER);

		// new TextView(context)
		ll_secondpay.addView(lv_mHistorypay);

		ll_secondpay.addView(iv_mOther);
		// ll_secondpay.addView(tv_mOther);
		// ll_secondpay.addView(bt_mOkkuai);
		// ll_secondpay.setVisibility(View.GONE);

		ll_content.addView(ll_deleline);
		// ll_content.addView(ll_firstpay);
		ll_content.addView(ll_secondpay);
		ll_content.addView(bt_mOkkuai);

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

		// 设置短信填充
		onStart();
		mBankList = AgentApp.mUser.cashbanks;
		// mBankList = new ArrayList<BankInfo>();
		mPaymentCallback = KgameSdk.mPaymentCallback;
		mCardNum = et_mCarnumber;
		mPhoneNum = et_mCarphone;
		mName = et_mCarname;
		mCardId = et_mUserid;

		mPaynow = bt_mOk;
		mPaynow_kuai = bt_mOkkuai;
		mBankInfo = ll_mHelp;

		mGridView = lv_mHistorypay;
		mWarning = ll_mHelp;
		mMorePay = tv_mOlderpay;
		/*
		 * ll_secondpay.setVisibility(View.VISIBLE); BankInfo bankInfo = new
		 * BankInfo(); bankInfo.bank_id = "1111"; bankInfo.bankname = "工商银行";
		 * bankInfo.lastno = "688"; bankInfo.bindvalid = "222"; bankInfo.id =
		 * "222"; BankInfo bankInfo2 = new BankInfo(); bankInfo2.bank_id =
		 * "1111"; bankInfo2.bankname = "中信银行"; bankInfo2.lastno = "688";
		 * bankInfo2.bindvalid = "222"; bankInfo2.id = "222";
		 *
		 * mBankList.add(bankInfo); mBankList.add(bankInfo2);
		 */
		// if (AgentApp.mUser.cashbanks != null
		// && AgentApp.mUser.cashbanks.size() != 0) {

		MoneyListAdapter_jf bankAdapter = new MoneyListAdapter_jf();

		mGridView.setAdapter(bankAdapter);

		if (mBankList.size() < 3) {
			// lv_mHistoryuser.getLayoutParams().height = mNames.size() *
			// 55;
			setListviewheight(mBankList.size());
		} else {
			setListviewheight(3);
		}

		// 默认选择第一间银行
		mBank = AgentApp.mUser.cashbanks.get(0);

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int count = parent.getCount();
				for (int i = 0; i < parent.getChildCount(); i++) {
					int childCount = parent.getChildCount();
					View view2 = parent.getChildAt(i);
					System.out
							.println("count:" + count + "  i:" + i
									+ "position:" + position + " childcount:"
									+ (i % 3));

					view2.setBackgroundDrawable(GetAssetsutils
							.get9DrawableFromAssetsFile(
									"yaya_kanoselect.9.png", mActivity));

				}

				view.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_kaselect.9.png",
								mActivity));
				selectpostion = position;
				mBank = AgentApp.mUser.cashbanks.get(position);
			}
		});

		bt_mOkkuai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBank == null) {
					Toast.makeText(mContext, "请选择银行", Toast.LENGTH_LONG).show();
					return;
				}
				// 进入支付流程
				DialogUtil.showDialog(mContext, "正在请求支付...");
				mPaynow_kuai.setEnabled(false);
				/*new Thread() {
					@Override
					public void run() {
						try {
							mFirstResult = ObtainData.payment(mContext,
									AgentApp.mPayOrder, AgentApp.mUser,
									AgentApp.mPayOrder.paytype);

							mHandler.sendEmptyMessage(FIRSTRESULTKUAI);
						} catch (Exception e) {
							mHandler.sendEmptyMessage(NETERROR);
						}
					}

				}.start();*/

			}
		});

		iv_mOther.setClickable(true);
		iv_mOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ChuxukapayDialog(mActivity).dialogShow();
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

	private int selectpostion = 0; // 控制listview哪个选中
	private AuthNumReceiver mAuthNumReceiver;

	public void setListviewheight(int size) {
		lv_mHistorypay.getLayoutParams().height = machSize((size * 100));
	}

	public class MoneyListAdapter_jf extends BaseAdapter {

		private Context mContext;

		class ViewHolder {

			TextView mName;
			ImageView mDelete;
			LinearLayout linearlayout;
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
				holder.linearlayout = loginpo_listviewitem.getLinearLayout();
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == selectpostion) {
				holder.linearlayout.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_kaselect.9.png",
								mActivity));

			} else {
				holder.linearlayout.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya_kanoselect.9.png",
								mActivity));
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
		if (mCodeDialog!=null) {
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
		
		if (mCodeDialog!=null) {
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
		if (mCodeDialog!=null) {
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
		DialogUtil.dismissDialog();
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
				if (mCode_his!=null) {
					mCode_his.setText(message);
				}
				
			}
		});
		// super.onStart();
	}
}
