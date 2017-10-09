package com.yayawan.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.utils.DeviceUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.impl.qqhelper.QqYsdkHelp;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;

public class ChargerImpl implements YYWCharger {

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack callback) {

	}

	private Activity mactivity;

	
	private static int moneyrate=10;
	
	@Override
	public void pay(final Activity paramActivity, final YYWOrder order,
			YYWPayCallBack callback) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				mactivity = paramActivity;
				
				moneyrate=100/Integer.parseInt(DeviceUtil.getGameInfo(paramActivity, "moneyrate"));
				if (YYWMain.mUser != null) {
					// System.out.println("我要创建订单了");
					// 设置是第一次支付
					mactivity = paramActivity;
					Myconstants.ISFIRSTPAY = true;
					UserLoginRet ret = new UserLoginRet();
					int platform = YSDKApi.getLoginRecord(ret);

					Myconstants.mpayinfo.openKey = ret.getAccessToken();

					Myconstants.mpayinfo.qq_paytoken = ret.getPayToken();
					String openid = ret.open_id;
					// int flag = ret.flag;
					// String msg = ret.msg;
					Myconstants.mpayinfo.pf = YSDKApi.getPf();
					Myconstants.mpayinfo.pfKey = YSDKApi.getPfKey();

					ePlatform platform1 = QqYsdkHelp.getPlatform();
					String logintype = "";
					if (platform1 == ePlatform.QQ) {
						logintype = "qq";
						Myconstants.mpayinfo.openId = ret.open_id;
						Myconstants.mpayinfo.opentype = "qq";
						Myconstants.mpayinfo.openKey = ret.getPayToken();
					} else if (platform1 == ePlatform.WX) {
						logintype = "wx";
						Myconstants.mpayinfo.openId = ret.open_id;
						Myconstants.mpayinfo.opentype = "wx";
						Myconstants.mpayinfo.openKey = ret.getAccessToken();
					}

					System.out.println(Myconstants.mpayinfo.toString());

					createOrder(paramActivity);
				} else {
					System.out.println("meiyouuser");
				}
				// pay_run(mactivity);

			}
		});

	}

	String orderId = null;
	String token_id = null;
	String url_params = null;

	public void createOrder(final Activity paramActivity) {
		progress(paramActivity);
		HttpUtils httpUtil = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("uid", YYWMain.mUser.yywuid);
		requestParams.addBodyParameter("openid", Myconstants.mpayinfo.openId);
		requestParams.addBodyParameter("app_id",
				DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("openkey", Myconstants.mpayinfo.openKey);
		requestParams.addBodyParameter("pay_token",
				Myconstants.mpayinfo.qq_paytoken);
		requestParams.addBodyParameter("amount", "" + YYWMain.mOrder.money);
		requestParams.addBodyParameter("remark", YYWMain.mOrder.ext);
		requestParams.addBodyParameter("transid", YYWMain.mOrder.orderId);
		requestParams.addBodyParameter("username", YYWMain.mUser.userName);
		requestParams.addBodyParameter("pf", Myconstants.mpayinfo.pf);
		requestParams.addBodyParameter("pfkey", Myconstants.mpayinfo.pfKey);
		requestParams.addBodyParameter("zoneid", "1");
		requestParams.addBodyParameter("amt", "" + (YYWMain.mOrder.money /moneyrate));
		requestParams.addBodyParameter("opentype",
				Myconstants.mpayinfo.opentype);

		System.out.println(Myconstants.mpayinfo.toString());
		System.out.println(YYWMain.mOrder.toString());
		System.out.println(YYWMain.mUser.toString());
		System.out.println("payitem:" + "123456" + "*"
				+ (YYWMain.mOrder.money / 10) + "*" + "" + 1);
		System.out.println("goodsmeta:" + YYWMain.mOrder.goods + "*" + "道具");
		System.out
				.println("goodsurl:"
						+ YYWMain.mOrder.goods
						+ "*"
						+ "http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg");

		requestParams.addBodyParameter("payitem", "123456" + "*"
				+ (YYWMain.mOrder.money / 10) + "*" + "" + 1);
		requestParams.addBodyParameter("goodsmeta", YYWMain.mOrder.goods + "*"
				+ "道具");
		requestParams
				.addBodyParameter("goodsurl",
						"http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg");

		httpUtil.send(HttpMethod.POST, ViewConstants.unionmakeorder,
				requestParams, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						disprogress();
					}

					@Override
					public void onSuccess(final ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						disprogress();
						try {
							JSONObject obj = new JSONObject(arg0.result);
							int err_code = obj.optInt("err_code");
							System.out.println("支付回来的结果++++++++++"
									+ arg0.result);

							// err_code = 11020 的时候 余额不足
							if (err_code == 11020) {

								// 余额不足开启支付
								new Handler(Looper.getMainLooper())
										.post(new Runnable() {

											@Override
											public void run() {
												System.out.println("支付回来的结果"
														+ arg0.result);
												pay_run(paramActivity);

											}
										});
							} else if (err_code == 10021) {
								getmoneyTimer = 0;
								// pf值有问题
								ToastInmainthread(paramActivity,
										"登陆过期，请按返回键，注销登陆，重新登陆充值");
								payfail(paramActivity);

							} else if (err_code == 0) {
								paysucc(paramActivity);
							} else {
								payfail(paramActivity);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 
	 * 支付成功后发起扣款
	 * 
	 * @param paramActivity
	 */
	public void tokoukuan(final Activity paramActivity) {
		
		HttpUtils httpUtil = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("uid", YYWMain.mUser.yywuid);
		requestParams.addBodyParameter("openid", Myconstants.mpayinfo.openId);
		requestParams.addBodyParameter("app_id",
				DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("openkey", Myconstants.mpayinfo.openKey);
		requestParams.addBodyParameter("pay_token",
				Myconstants.mpayinfo.qq_paytoken);
		requestParams.addBodyParameter("amount", "" + YYWMain.mOrder.money);
		requestParams.addBodyParameter("remark", YYWMain.mOrder.ext);
		requestParams.addBodyParameter("transid", YYWMain.mOrder.orderId);
		requestParams.addBodyParameter("username", YYWMain.mUser.userName);
		requestParams.addBodyParameter("pf", Myconstants.mpayinfo.pf);
		requestParams.addBodyParameter("pfkey", Myconstants.mpayinfo.pfKey);
		requestParams.addBodyParameter("zoneid", "1");
		requestParams.addBodyParameter("amt", "" + (YYWMain.mOrder.money / moneyrate));
		requestParams.addBodyParameter("opentype",
				Myconstants.mpayinfo.opentype);

		/*
		 * System.out.println(Myconstants.mpayinfo.toString());
		 * System.out.println(YYWMain.mOrder.toString());
		 * System.out.println(YYWMain.mUser.toString());
		 * System.out.println("payitem:" + "123456" + "*" +
		 * (YYWMain.mOrder.money / 10) + "*" + "" + 1);
		 * System.out.println("goodsmeta:" + YYWMain.mOrder.goods + "*" + "道具");
		 * System.out .println("goodsurl:" + YYWMain.mOrder.goods + "*" +
		 * "http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg"
		 * );
		 */

		requestParams.addBodyParameter("payitem", "123456" + "*"
				+ (YYWMain.mOrder.money /moneyrate) + "*" + "" + 1);
		requestParams.addBodyParameter("goodsmeta", YYWMain.mOrder.goods + "*"
				+ "道具");
		requestParams
				.addBodyParameter("goodsurl",
						"http://img2.imgtn.bdimg.com/it/u=3188228834,2947524100&fm=21&gp=0.jpg");

		httpUtil.send(HttpMethod.POST, ViewConstants.unionmakeorder,
				requestParams, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(final ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						
						try {
							JSONObject obj = new JSONObject(arg0.result);
							int err_code = obj.optInt("err_code");
							System.out.println("支付回来的结果++++++++++"
									+ arg0.result);

							// err_code = 11020 的时候 余额不足
							if (err_code == 11020) {

								ToastInmainthread(paramActivity,
										"支付正在确认，请等待一分钟到账");
								delayToGetMoney(paramActivity);

								// pfkey不正确
							} else if (err_code == 10021) {

								getmoneyTimer = 0;
								// pf值有问题
								ToastInmainthread(paramActivity,
										"登陆过期，请按返回键，注销登陆，重新登陆充值");
								payfail(paramActivity);
							} else if (err_code == 0) {
								paysucc(paramActivity);
							} else {
								payfail(paramActivity);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	private void pay_run(final Activity paramActivity) {

		String zoneId = "1";
		String saveValue = "" + YYWMain.mOrder.money / moneyrate;
		System.out.println("money++++++:"+saveValue);
		boolean isCanChange = false;

		AssetManager assetManager = paramActivity.getAssets();
		InputStream istr = null;
		try {
			istr = assetManager.open("sample_yuanbao.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Bitmap bmp = BitmapFactory.decodeStream(istr);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] appResData = baos.toByteArray();

		String ysdkExt = "ysdkExt";
		YSDKApi.recharge(zoneId, saveValue, isCanChange, appResData, ysdkExt,
				new PayListener() {
					@Override
					public void OnPayNotify(PayRet ret) {
						if (PayRet.RET_SUCC == ret.ret) {
							// 支付流程成功
							switch (ret.payState) {
							// 支付成功
							case PayRet.PAYSTATE_PAYSUCC:
								System.out.println("用户支付成功，支付金额"
										+ ret.realSaveNum + ";" + "使用渠道："
										+ ret.payChannel + ";" + "发货状态："
										+ ret.provideState + ";" + "业务类型："
										+ ret.extendInfo + ";建议查询余额："
										+ ret.toString());
								new Handler(Looper.getMainLooper())
										.post(new Runnable() {

											@Override
											public void run() {
												
												tokoukuan(mactivity);
											}
										});

								break;
							// 取消支付
							case PayRet.PAYSTATE_PAYCANCEL:
								System.out.println("用户取消支付：" + ret.toString());
								payFail();
								break;
							// 支付结果未知
							case PayRet.PAYSTATE_PAYUNKOWN:
								System.out.println("用户支付结果未知，建议查询余额："
										+ ret.toString());
								payFail();
								break;
							// 支付失败
							case PayRet.PAYSTATE_PAYERROR:
								payFail();
								System.out.println("支付异常" + ret.toString());
								break;
							}
						} else {
							switch (ret.flag) {

							case eFlag.Pay_User_Cancle:
								// 用户取消支付
								payFail();
								System.out.println("用户取消支付：" + ret.toString());
								break;
							case eFlag.Pay_Param_Error:
								payFail();
								System.out.println("支付失败，参数错误" + ret.toString());
								break;
							case eFlag.Error:
							default:
								payFail();
								System.out.println("支付异常" + ret.toString());
								Toast.makeText(mactivity, "支付异常，请新登陆游戏进行支付", 0)
										.show();
								break;
							}
						}
					}
				});

	}

	static int getmoneyTimer = 0;// 轮询时间，超过次，就停止扣款

	/**
	 * 延迟10秒去扣款
	 */
	private void delayToGetMoney(final Activity mactivity) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mactivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (getmoneyTimer > 5) {
							ToastInmainthread(mactivity, "如果没有到账，请联系游戏客服进行处理");
							getmoneyTimer = 0;
							return;
						} else {
							getmoneyTimer = getmoneyTimer + 1;
							tokoukuan(mactivity);
						}

					}
				});
			}
		}, 10000);
	}

	private void ToastInmainthread(final Activity mactivity, final String msg) {
		mactivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(mactivity, msg, Toast.LENGTH_SHORT).show();

			}
		});
	}

	private void paysucc(Activity mactivity) {
		mactivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser,
						YYWMain.mOrder, "success");
			}
		});
	}

	private void payfail(Activity mactivity) {
		mactivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				YYWMain.mPayCallBack.onPayFailed(null, "success");
			}
		});
	}

	ProgressDialog progressDialog = null;

	private void progress(Activity paramActivity) {
		progressDialog = new ProgressDialog(paramActivity);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		// progressDialog.setTitle("提示");
		// 设置ProgressDialog 提示信息
		progressDialog.setMessage("订单处理中");
		// 设置ProgressDialog 标题图标
		// progressDialog.setIcon(R.drawable.a);
		// 设置ProgressDialog 的进度条是否不明确
		progressDialog.setIndeterminate(true);
		// 设置ProgressDialog 是否可以按退回按键取消
		progressDialog.setCancelable(false);
		// 设置ProgressDialog 的一个Button
		// progressDialog.setButton("确定", new SureButtonListener());
		// 让ProgressDialog显示
		try {
			progressDialog.show();
		} catch (Exception e) {

		}
	}

	private void disprogress() {
		if (progressDialog != null) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}
	}

	/*
	 * 支付成功
	 */
	public static void paySuce() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}
}
