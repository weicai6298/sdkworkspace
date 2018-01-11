package com.kkgame.sdk.pay;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.AlipayResult;
import com.kkgame.sdk.bean.BillResult;
import com.kkgame.sdk.bean.ConfirmPay;
import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.login.BaseLogin_Activity;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.DialogUtil;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Toastxml_po;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;

public class Yayapaystart_dialog extends Basedialogview {

	private LinearLayout ll_title;
	private LinearLayout ll_mHome;
	private LinearLayout ll_mPersonal;
	private KgameSdkPaymentCallback mPaymentCallback;

	public Yayapaystart_dialog(Activity activity) {
		super(activity);
	}
	
	private static final int REQUESTCODE = 0;
	protected static final int RESULT = 1;

	protected static final int PAYRESULT = 2;

	protected static final int ERROR = -3;
	private static final int RQF_LOGIN = 5;
	private static final int BILLRESULT = 6;
	private static final int FIRSTRESULTKUAI = 7;
	private static final int ALIPAYERROR = 19;
	private static final int NETERROR = 18;
	private static final int DATAERROR = 17;
	private static final int FIRSTRESULT = 3;
	private ConfirmPay mYayapay;
	private YuepayDialog mYayaDialog;
	private BillResult mBillResult;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		
		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {

			case RESULT:

				/*
				 * if (Long.valueOf(AgentApp.mUser.money) >=
				 * AgentApp.mPayOrder.money && AgentApp.mPayOrder.paytype == 0)
				 * {
				 */

				if (Long.valueOf(AgentApp.mUser.money) >= AgentApp.mPayOrder.money
						&& AgentApp.mPayOrder.paytype == 0) {

					Log.e("我有多少钱~!", Long.valueOf(AgentApp.mUser.money) + "");

					dialogDismiss();
					mYayaDialog = new YuepayDialog(mActivity);
					mYayaDialog.dialog.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							onCancel();
							
						}
					});
					
					if (AgentApp.mPayOrder.money % 100 == 0) {

						mYayaDialog.getTv_mYue().setText(
								Long.valueOf(AgentApp.mUser.money)/100 + "丫丫币");
					} else {
						// 除数
						BigDecimal bd = new BigDecimal(AgentApp.mUser.money);
						// 被除数
						BigDecimal bd2 = new BigDecimal(100);
						// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
						BigDecimal result = bd.divide(bd2, 2,
								BigDecimal.ROUND_HALF_UP);
						mYayaDialog.getTv_mYue().setText(
								result.toString() + "丫丫币 ");
					}
					

					if (AgentApp.mPayOrder.money % 100 == 0) {

						mYayaDialog.getTv_mZhifu().setText(
								+(AgentApp.mPayOrder.money)/100 + "丫丫币 ");
					} else {
						// 除数
						BigDecimal bd = new BigDecimal(AgentApp.mPayOrder.money);
						// 被除数
						BigDecimal bd2 = new BigDecimal(100);
						// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
						BigDecimal result = bd.divide(bd2, 2,
								BigDecimal.ROUND_HALF_UP);
						mYayaDialog.getTv_mZhifu().setText(
								result.toString() + "丫丫币 ");
					}
					// mYayaDialog.setBtnText("立即付款");
					mYayaDialog.getBt_mOk().setOnClickListener(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									Utilsjf.creDialogpro(mActivity, "正在支付...");
									mYayaDialog.getBt_mOk().setEnabled(false);
									/*new Thread() {
										
										@Override
										public void run() {
											// 余额支付
											try {
												mYayapay = ObtainData.yayapay(
														mContext,
														AgentApp.mPayOrder,
														AgentApp.mUser);
												mHandler.sendEmptyMessage(PAYRESULT);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									}.start();*/
								}
							});
					mYayaDialog.dialogShow();
					

				} else {
					
					Intent intent = new Intent(mContext,
							BaseLogin_Activity.class);

					intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
					mActivity.startActivity(intent);
				
					dialogDismiss();
				}
				break;
			case PAYRESULT:
				if (mYayapay.success == 0) {
					ToastUtil.showSuccess(mContext, "付款成功");
					onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
					dialogDismiss();
				} else {
					ToastUtil.showError(mContext, "付款失败");
					onError(0);
					dialogDismiss();
				}
				break;
			case ERROR:
				Toast.makeText(mContext, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				dialogDismiss();
				//mActivity.finish();
				break;

			
			case RQF_LOGIN:
				AlipayResult result = new AlipayResult((String) msg.obj);
				if ("9000".equals(result.getResultStatus())) {
					AgentApp.mPayOrder.id = result.getOutTradeNo();
					DialogUtil.showDialog(mContext, "支付结果确认中...");
					// 查询订单状态
					/*new Thread() {

						

						@Override
						public void run() {
							try {
								Thread.sleep(6 * 1000);
								mBillResult = com.KgameSdk.sdk.payment.engine.ObtainData
										.getBillResult(mContext,
												AgentApp.mUser,
												AgentApp.mPayOrder);
								if (mBillResult.error_code == 701) {

									Thread.sleep(5 * 1000);
									mBillResult = com.KgameSdk.sdk.payment.engine.ObtainData
											.getBillResult(mContext,
													AgentApp.mUser,
													AgentApp.mPayOrder);

								}
								mHandler.sendEmptyMessage(BILLRESULT);
							} catch (Exception e) {
								mHandler.sendEmptyMessage(DATAERROR);
							}
						}

					}.start();*/
				} else {
					onError(0);
					ToastUtil.showError(mContext, result.getResult());
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

			case ALIPAYERROR:
				Toast.makeText(mContext, "支付宝快捷支付出现异常,请删除后重试",
						Toast.LENGTH_LONG).show();
				break;

			case DATAERROR:
				Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();
				// finish();
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Toastxml_po toastxml_po = new Toastxml_po(mActivity);
		View initViewxml = toastxml_po.initViewxml();
		ImageView iv_imageview = toastxml_po.getIv_imageview();
		TextView tv_message = toastxml_po.getTv_message();
		iv_imageview.setImageBitmap(GetAssetsutils.getImageFromAssetsFile("yaya_dunpai.png", mActivity));
		tv_message.setText("检测安全支付...");
		tv_message.setTextColor(Color.WHITE);
		

		
		
		dialog.setContentView(initViewxml);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		
		
	}

	@Override
	public void dialogShow() {
		// TODO Auto-generated method stub
		super.dialogShow();
		initlogic();
	}
	private void initlogic() {
		
		AgentApp.mPayMethods = DeviceUtil.getYayaWanMethod(mContext);
		
		mPaymentCallback = KgameSdk.mPaymentCallback;
		

		// 判断用户是否有yy币.如果有用yy币支付
		/*new Thread() {

			@Override
			public void run() {
				try {
					AgentApp.mUser = ObtainData.getMoneyInfo(mContext,
							AgentApp.mUser);
					mHandler.sendEmptyMessage(RESULT);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(ERROR);
					e.printStackTrace();
				}
			}

		}.start();*/
	}
	
	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
		}
		mPaymentCallback = null;
		mYayaDialog.dialogDismiss();
	}

	public void onError(int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		mPaymentCallback = null;
		mYayaDialog.dialogDismiss();
	}

	public void onCancel() {
		if (mPaymentCallback != null) {
			mPaymentCallback.onCancel();
		}
		mYayaDialog.dialogDismiss();
	}

}
