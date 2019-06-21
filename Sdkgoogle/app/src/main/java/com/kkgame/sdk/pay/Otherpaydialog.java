package com.kkgame.sdk.pay;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.BillResult;
import com.kkgame.sdk.bean.ConfirmPay;
import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.PayResult;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.CardInfo;
import com.kkgame.sdk.utils.CardNumTextWatcher;
import com.kkgame.sdk.utils.DialogUtil;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Loginpo_listviewitem;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;

public class Otherpaydialog extends Basedialogview implements
		KgameSdkPaymentCallback {

	private LinearLayout ll_mMoney;
	private EditText et_mMoney;
	private ImageView iv_mMoney_down;
	private RelativeLayout baserl;
	private EditText et_mCarnumber;
	private EditText et_mCarpassword;
	private LinearLayout ll_mHelp;
	private BillResult mBillResult;
	private ListView lv_selectmoney;
	private ArrayList<String> mNames;
	private Button bt_mOk;
	private EditText mCardNum;
	private EditText mCardPassword;
	private Button mPaynow;
	private String mCardNumText;
	private String mCardPassText;
	private PayResult mFirstResult;
	private ConfirmPay mConfirmPay;
	private String mCurrentMoney;
	private static final int FIRSTRESULT = 3;

	private static final int CONFIRMRESULT = 4;

	private static final int BILLRESULT = 5;

	private static final int DATAERROR = 17;

	private static final int NETERROR = 18;
	private static final int ERROR = 10;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			DialogUtil.dismissDialog();
			switch (msg.what) {
			case FIRSTRESULT:
				if (mFirstResult.success == 1) {
					// 操作失败
					onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果
					mFirstResult.params.put("pa7_cardAmt", mCurrentMoney);
					mFirstResult.params.put("pa8_cardNo", mCardNumText);
					mFirstResult.params.put("pa9_cardPwd", mCardPassText);
					mFirstResult.params.put("uid", AgentApp.mUser.uid + "");
					mFirstResult.params.put("token", AgentApp.mUser.token);
					mFirstResult.params.put("app_id",
							DeviceUtil.getGameId(mContext));
					/// System.out.println("这是支付面值:"+mCurrentMoney);
					// System.out.println("这是支付账号值:"+mCardNumText);
					//.out.println("这是支父密码:"+ mCardPassText);
					/*new Thread() {
						@Override
						public void run() {
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
									mHandler.sendEmptyMessage(DATAERROR);
									e.printStackTrace();
								}
							}

						}.start();*/

					}
				}

				break;
			case BILLRESULT:
				if (mBillResult != null) {
					if (mBillResult.success == 1) {
						// 第二次确认操作失败
						onError(0);
						ToastUtil.showError(mContext, mBillResult.error_msg);
					} else if (mBillResult.success == 0) {

						// 支付操作成功等待到账

						onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
						ToastUtil.showSuccess(mContext, mBillResult.body);
					}
				}

				break;
			case ERROR:
				ToastUtil.showError(mContext, "获取支付结果失败");
				mActivity.finish();
				break;
			case DATAERROR:
				Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();
				mActivity.finish();
				break;
			default:
				Toast.makeText(mContext, "数据异常，请再次支付。", Toast.LENGTH_LONG)
						.show();
				mActivity.finish();
				break;
			}
		}

	};
	private KgameSdkPaymentCallback mPaymentCallback;

	public Otherpaydialog(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDialog(final Activity mActivity) {
		dialog = new Dialog(mActivity);
		// mActivity
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mActivity);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
		machineFactory.MachineView(baselin, 720, 450, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		RelativeLayout rl_content = new RelativeLayout(mActivity);
		machineFactory.MachineView(rl_content, ViewConstants.getHoldDialogWith(mActivity), ViewConstants.getHoldDialogHeight(mActivity), mLinearLayout,2,25);
		rl_content.setBackgroundColor(Color.WHITE);
		// rl_content.setGravity(Gravity.CENTER_HORIZONTAL);
		// rl_content.setOrientation(LinearLayout.VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_content, ViewConstants.getHoldDialogWith(mActivity), ViewConstants.getHoldDialogHeight(mActivity), 0, mRelativeLayout,
				20, 0, 20, 0, 100);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 删除行
		LinearLayout ll_deleline = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_deleline, MATCH_PARENT, 50, 0,
				mLinearLayout, 0, 20, 0, 0, 100);
		ll_deleline.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout ll_zhanwei = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_zhanwei, 0, MATCH_PARENT, 1,
				mLinearLayout);

		ll_mHelp = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_mHelp, 70, MATCH_PARENT, mLinearLayout);
		ll_mHelp.setGravity(Gravity_CENTER);

		ImageView iv_help = new ImageView(mActivity);
		machineFactory.MachineView(iv_help, 50, 50, mLinearLayout, 3, 0);
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

		// 设置充值卡支付面额的ll 注意背景的兼容性问题
		ll_mMoney = new LinearLayout(mActivity);
		ll_mMoney = (LinearLayout) machineFactory.MachineView(ll_mMoney,
				MATCH_PARENT, 100, 0, "LinearLayout", 0, 20, 0, 0, 100);

		ll_mMoney
				.setBackgroundDrawable(GetAssetsutils
						.get9DrawableFromAssetsFile("yaya1_biankuan.9.png",
								mActivity));
		ll_mMoney.setClickable(true);
		ll_mMoney.setGravity(Gravity.CENTER);

		// 选择钱item
		et_mMoney = new EditText(mActivity);
		et_mMoney = machineFactory.MachineEditText(et_mMoney, 0, MATCH_PARENT,
				1, "请选择充值卡面额", 28, mLinearLayout, 0, 6, 0, 0);
		et_mMoney.setBackgroundColor(Color.TRANSPARENT);
		et_mMoney.setClickable(false);
		et_mMoney.setFocusable(false);
		et_mMoney.setText("1元");

		LinearLayout ll_down = new LinearLayout(mActivity);
		machineFactory.MachineView(ll_down, 50, MATCH_PARENT, mLinearLayout, 3,
				10);
		ll_down.setGravity(Gravity_CENTER);

		// 选择钱的下拉图片
		iv_mMoney_down = new ImageView(mActivity);
		iv_mMoney_down = (ImageView) machineFactory.MachineView(iv_mMoney_down,
				40, 40, 0, mLinearLayout, 0, 0, 0, 0, 100);
		iv_mMoney_down.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_down.png", mActivity));

		ll_mMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (View.VISIBLE == lv_selectmoney.getVisibility()) {
					iv_mMoney_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_down.png", mActivity));
					lv_selectmoney.setVisibility(View.GONE);
				} else {
					iv_mMoney_down.setImageBitmap(GetAssetsutils
							.getImageFromAssetsFile("yaya_up.png", mActivity));
					lv_selectmoney.setVisibility(View.VISIBLE);
				}
			}
		});

		// TODO
		ll_down.addView(iv_mMoney_down);
		ll_mMoney.addView(et_mMoney);
		ll_mMoney.addView(ll_down);

		et_mCarnumber = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarnumber, MATCH_PARENT, 100, 0,
				"请输入您的充值卡卡号", 32, mLinearLayout, 0, 20, 0, 0);
		et_mCarnumber.setTextColor(Color.BLACK);
		et_mCarnumber.setSingleLine();
		//et_mCarnumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		et_mCarpassword = new EditText(mActivity);
		machineFactory.MachineEditText(et_mCarpassword, MATCH_PARENT, 100, 0,
				"请输入您的充值卡密码", 32, mLinearLayout, 0, 20, 0, 0);
		et_mCarpassword.setTextColor(Color.BLACK);
		et_mCarpassword.setSingleLine();
		et_mCarpassword.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		// button的确认
		bt_mOk = new Button(mActivity);
		bt_mOk = machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 90, 0,
				"确认", 30, mLinearLayout, 0, 70, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		ll_content.addView(ll_deleline);
		ll_content.addView(ll_mMoney);
		ll_content.addView(et_mCarnumber);
		ll_content.addView(et_mCarpassword);
		ll_content.addView(bt_mOk);

		mNames = new ArrayList<String>();
		mNames.add("1元");
		mNames.add("5元");
		mNames.add("10元");
		mNames.add("20元");
		mNames.add("30元");
		mNames.add("50元");
		mNames.add("100元");
		mNames.add("200元");
		mNames.add("500元");
		lv_selectmoney = new ListView(mActivity);
		machineFactory.MachineView(lv_selectmoney, MATCH_PARENT, 300, 0,
				mRelativeLayout, 20, 180, 20, 0, 100);
		MoneyListAdapter_jf moneyListAdapter_jf = new MoneyListAdapter_jf();
		lv_selectmoney.setAdapter(moneyListAdapter_jf);
		lv_selectmoney.setVisibility(View.GONE);

		lv_selectmoney.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				et_mMoney.setText(mNames.get(arg2));
				iv_mMoney_down.setImageBitmap(GetAssetsutils
						.getImageFromAssetsFile("yaya_down.png", mActivity));
				lv_selectmoney.setVisibility(View.GONE);
			}
		});

		rl_content.addView(ll_content);
		rl_content.addView(lv_selectmoney);

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
		dialog.setCanceledOnTouchOutside(true);
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

		RelativeLayout.LayoutParams ap2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		// ap2.addRule(RelativeLayout.BELOW, );
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
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
		mPaymentCallback = KgameSdk.mPaymentCallback;
		mCardNum = et_mCarnumber;
		mCardPassword = et_mCarpassword;
		mPaynow = bt_mOk;

		
		mCardNum.addTextChangedListener(new CardNumTextWatcher(mCardNum));
		mCardPassword.addTextChangedListener(new CardNumTextWatcher(mCardPassword));
		
		if (AgentApp.mentid == 16) {
			mCardNum.setHint("请输入骏网卡一卡通卡号");
			mCardPassword.setHint("请输入骏网一卡通密码");
			mCardNum.setText(AgentApp.mCardInfos.get(CardInfo.JUNWANGCARDNUM));
			mCardPassword.setText(AgentApp.mCardInfos
					.get(CardInfo.JUNWANGCARDPASS));
		} else if (AgentApp.mentid == 13) {
			mCardNum.setHint("请输入盛大充值卡卡号");
			mCardPassword.setHint("请输入盛大充值卡密码");
			mCardNum.setText(AgentApp.mCardInfos.get(CardInfo.SHENGDACARDNUM));
			mCardPassword.setText(AgentApp.mCardInfos
					.get(CardInfo.SHENGDACARDPASS));
		} else if (AgentApp.mentid == 20) {
			mCardNum.setHint("请输入Q币卡卡号");
			mCardPassword.setHint("请输入Q币卡密码");
			mCardNum.setText(AgentApp.mCardInfos.get(CardInfo.QQCARDNUM));
			mCardPassword.setText(AgentApp.mCardInfos.get(CardInfo.QQCARDPASS));
		}
		mPaynow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCardNumText = mCardNum.getText().toString().trim()
						.replace(" ", "");
				mCardPassText = mCardPassword.getText().toString().trim().replace(" ", "");
				mCurrentMoney=et_mMoney.getText().toString().trim().replace(" ", "").replace("元", "");
				if ("".equals(mCardNumText)) {
					Toast.makeText(mContext, "请输入卡号", Toast.LENGTH_SHORT)
							.show();
				} else if (AgentApp.mentid == 16 && mCardNumText.length() < 16) {
					Toast.makeText(mContext, "卡号不能小于16位", Toast.LENGTH_SHORT)
							.show();
				} else if (AgentApp.mentid == 13 && mCardNumText.length() < 15) {
					Toast.makeText(mContext, "卡号不能小于15位", Toast.LENGTH_SHORT)
							.show();
				} else if (AgentApp.mentid == 20 && mCardNumText.length() < 9) {
					Toast.makeText(mContext, "卡号不能小于9位", Toast.LENGTH_SHORT)
							.show();
				} else if ("".equals(mCardPassText)) {
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT)
							.show();
				} else if (AgentApp.mentid == 16 && mCardPassText.length() < 16) {
					Toast.makeText(mContext, "密码不能小于16位", Toast.LENGTH_SHORT)
							.show();
				} else if (AgentApp.mentid == 13 && mCardPassText.length() < 8) {
					Toast.makeText(mContext, "密码不能小于8位", Toast.LENGTH_SHORT)
							.show();
				} else if (AgentApp.mentid == 20 && mCardPassText.length() < 12) {
					Toast.makeText(mContext, "密码不能小于12位", Toast.LENGTH_SHORT)
							.show();
				} else {

					// 进入支付流程
					DialogUtil.showDialog(mContext, "正在请求支付...");
					mPaynow.setEnabled(false);
					if (AgentApp.mentid == 16) {
						AgentApp.mCardInfos.put(CardInfo.JUNWANGCARDNUM,
								mCardNumText);
						AgentApp.mCardInfos.put(CardInfo.JUNWANGCARDPASS,
								mCardPassText);
					} else if (AgentApp.mentid == 13) {
						AgentApp.mCardInfos.put(CardInfo.SHENGDACARDNUM,
								mCardNumText);
						AgentApp.mCardInfos.put(CardInfo.SHENGDACARDPASS,
								mCardPassText);
					} else if (AgentApp.mentid == 20) {
						AgentApp.mCardInfos.put(CardInfo.QQCARDNUM,
								mCardNumText);
						AgentApp.mCardInfos.put(CardInfo.QQCARDPASS,
								mCardPassText);
					}
					/*new Thread() {
						@Override
						public void run() {
							try {
								mFirstResult = ObtainData.payment(mContext,
										AgentApp.mPayOrder, AgentApp.mUser,
										AgentApp.mPayOrder.paytype);
								//System.out.println(AgentApp.mPayOrder.money+"这是支付的钱1");
								mHandler.sendEmptyMessage(FIRSTRESULT);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}.start();*/

				}
			}
		});

	}

	public class MoneyListAdapter_jf extends BaseAdapter {

		class ViewHolder {

			TextView mName;
			ImageView mDelete;
		}

		public int getCount() {
			return mNames.size();
		}

		public Object getItem(int position) {
			return mNames.get(position);
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
			final String name = mNames.get(position);
			holder.mName.setText(name);
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
		mActivity.finish();

	}

	@Override
	public void onError(int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		mPaymentCallback = null;
		mActivity.finish();
	}

	@Override
	public void onCancel() {
		if (mPaymentCallback != null) {
			mPaymentCallback.onCancel();
		}
		mPaymentCallback = null;
		mActivity.finish();
	}

	public LinearLayout getLl_mHelp() {
		return ll_mHelp;
	}

	public void setLl_mHelp(LinearLayout ll_mHelp) {
		this.ll_mHelp = ll_mHelp;
	}

}
