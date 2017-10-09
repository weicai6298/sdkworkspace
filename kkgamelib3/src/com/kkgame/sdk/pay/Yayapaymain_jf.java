package com.kkgame.sdk.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.PayMethod;
import com.kkgame.sdk.bean.PayResult;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.login.BaseView;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.utils.Utilsjf.PayQuesionCallBack;
import com.kkgame.sdk.xml.Yayapay_mainxml_po;
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

public class Yayapaymain_jf extends BaseView {

	private Yayapay_mainxml_po mThisview;
	private RelativeLayout rl_mAlipay;

	private KgameSdkPaymentCallback mPaymentCallback;

	private ArrayList<PayMethod> mMethods;
	private TextView tv_mMoney;
	private TextView tv_mYuanbao;
	private TextView tv_mHelp;
	private LinearLayout ll_mPre;
	private RelativeLayout rl_mWxpluin;

	private static int ALIPAY = 5;
	private static int WEIXINPAY = 2;
	private static int YINLIANPAY = 3;
	private static int WEIXINH5PAY = 4;

	public Yayapaymain_jf(Activity mContext) {
		super(mContext);

	}

	@Override
	public View initRootview() {
		// 初始化支付页面
		mThisview = new Yayapay_mainxml_po(mActivity);
		return mThisview.initViewxml();

	}

	@Override
	public void initView() {

		// 获取页面的所有视图实例对象
		rl_mAlipay = mThisview.getRl_mAlipay();
		rl_mYinlianpay = mThisview.getRl_mYinlianpay();

		ll_mPre = mThisview.getLl_mPre();
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCancel();
			}
		});
		tv_mMoney = mThisview.getTv_mMoney();
		Order mPayOrder = AgentApp.mPayOrder;
		if (AgentApp.mPayOrder.money % 100 == 0) {
			tv_mMoney.setText("" + (AgentApp.mPayOrder.money) / 100);
		} else {
			// 除数
			BigDecimal bd = new BigDecimal(AgentApp.mPayOrder.money);
			// 被除数
			BigDecimal bd2 = new BigDecimal(100);
			// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
			BigDecimal result = bd.divide(bd2, 2, BigDecimal.ROUND_HALF_UP);
			tv_mMoney.setText(result.toString());
		}

		tv_mYuanbao = mThisview.getTv_mYuanbao();
		tv_mYuanbao.setText("" + mPayOrder.goods);

		// 微信支付
		rl_mWxpay = mThisview.getRl_mWxpay();
		// 微信插件支付
		rl_mWxpluin = mThisview.getRl_mWxpluin();
		// 先把视图隐藏，后面逻辑部分会进行有选择显示
		tv_mHelp = mThisview.getTv_mHelp();
		tv_mHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Help_dialog help_dialog = new Help_dialog(mActivity);
				help_dialog.dialogShow();
			}
		});
	}

	public static boolean payclickcontrol = false;// 点击支付条目控制器

	@Override
	public void logic() {
		payclickcontrol = false;
		mPaymentCallback = KgameSdk.mPaymentCallback;

		// TODO 支付宝支付
		rl_mAlipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;

				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

				//创建订单
				makeOrder(ALIPAY);
			}

		});

		// 微信支付 支付
		rl_mWxpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				Yayalog.loger("微信支付开始");
				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");
				//创建订单
				makeOrder(WEIXINH5PAY);

			}
		});

		
		//隐藏银联支付
		rl_mYinlianpay.setVisibility(View.GONE);
		
		// 银联支付
		rl_mYinlianpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewConstants.mPayActivity = mActivity;
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				CeshiYinlian();
			}

		});

		rl_mWxpluin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ViewConstants.mPayActivity = mActivity;
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;

				weiXinPay2();
			}
		});
	}

	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
		}
		mPaymentCallback = null;
		mActivity.finish();
	}

	public void onError(int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		mPaymentCallback = null;

		// ToastUtils.showLongToast("支付出错，请尝试重新支付");
		mActivity.finish();
	}

	public void onCancel() {
		if (mPaymentCallback != null) {
			mPaymentCallback.onCancel();
		}
		mActivity.finish();
	}
	
	

	private void makeOrder(final int paytype) {

		// 进入支付流程
		RequestParams rps = new RequestParams();
		
		rps.addBodyParameter("app_id", DeviceUtil.getAppid(mActivity));
		rps.addBodyParameter("uid", AgentApp.mUser.uid + "");
		rps.addBodyParameter("token", AgentApp.mUser.token);
		rps.addBodyParameter("amount", KgameSdk.mPayOrder.money + "");
		rps.addBodyParameter("pay_type", paytype + "");
		rps.addBodyParameter("ext", AgentApp.mPayOrder.ext);
		rps.addBodyParameter("orderid", AgentApp.mPayOrder.orderId);
		
		Yayalog.loger("app_id", DeviceUtil.getAppid(mActivity));
		Yayalog.loger("uid", AgentApp.mUser.uid + "");
		Yayalog.loger("token", AgentApp.mUser.token);
		Yayalog.loger("amount", KgameSdk.mPayOrder.money + "");
		Yayalog.loger("pay_type", paytype + "");
		Yayalog.loger("ext", AgentApp.mPayOrder.ext);
		Yayalog.loger("orderid", AgentApp.mPayOrder.orderId);
		Yayalog.loger("orderid", ViewConstants.makeorder);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ViewConstants.makeorder, rps,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						payclickcontrol=false;
						Utilsjf.stopDialog();

						Toast.makeText(mActivity, "注册失败，请检查网络是否畅通", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						// TODO Auto-generated method stub
						Utilsjf.stopDialog();
						payclickcontrol=false;
						
						switch (paytype) {
						case 1:
							//解析支付宝下单结果
							Yayalog.loger("支付宝下单结果" + result.result);
							alipayResult(result.result);
							break;
						case 2:
							Yayalog.loger("微信下单结果" + result.result);	
							alipayResult(result.result);
							break;
						case 3:

							break;
						case 4:
							Yayalog.loger("微信下单结果" + result.result);
							alipayResult(result.result);
							break;
						case 5:
							//解析支付宝下单结果
							alipayResult(result.result);
							//alipayResult(result.result);
							break;

						default:
							break;
						}
					}

				});

	}

	// 支付宝支付结果
	private void alipayResult(String result) {
		// TODO Auto-generated method stub
		System.out.println(result);
		JSONObject jsonstr = null;
		try {
			jsonstr = new JSONObject(result);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int err_code=jsonstr.optInt("err_code");
		String pay_str=jsonstr.optString("url");
		if(err_code==0){
			try {
				System.out.println(pay_str);
				
				Weixinpay_dialog weixinpay_dialog = new Weixinpay_dialog(
						mActivity);
				weixinpay_dialog.dialogShow();
				WebView webView = weixinpay_dialog.getWebview();
				webView.loadUrl(pay_str);
				webView.setWebViewClient(new WebViewClient(){
					
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						Yayalog.loger("重复的url:"+url);
						
						if (url.startsWith("weixin://wap/pay?")) {
		                    try {
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(url));
								mActivity.startActivity(intent);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Yayalog.loger("未安装微信");
								e.printStackTrace();
							}

		                    return true;
		                }else if (parseScheme(url)) {
		                    try {
		                    	
		                        Intent intent;
		                        intent = Intent.parseUri(url,
		                                Intent.URI_INTENT_SCHEME);
		                        intent.addCategory("android.intent.category.BROWSABLE");
		                        intent.setComponent(null);
		                        // intent.setSelector(null);
		                        mActivity.startActivity(intent);
		//
		                        return true;
		                    } catch (Exception e) {
		                        e.printStackTrace();
		                    }
		                }else if (url.contains("paysuccess")) {
		                	onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
		                }else {
		                	view.loadUrl(url);
						}
					    return super.shouldOverrideUrlLoading(view, url);
					    }
					
					@Override
					public void onPageStarted(WebView view, String url, Bitmap favicon) {
						// TODO Auto-generated method stub
						//Yayalog.loger("onPageStarted重复的url:"+url);
						super.onPageStarted(view, url, favicon);
					}
					public void onPageFinished(WebView view, String url) {
						//Yayalog.loger("onPageFinished重复的url:"+url);
					};
					
					
					});
			
			} catch (Exception e) {
				System.out.println(e.toString());
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mActivity.getApplicationContext(),
								"网络出错，请重新支付", Toast.LENGTH_LONG).show();
					}
				});
			}
		}
	
		
	}

	private String mhtml;
	private RelativeLayout rl_mYinlianpay;
	private RelativeLayout rl_mWxpay;

	public void CeshiYinlian() {

		AgentApp.mentid = 21;
		Utilsjf.creDialogpro(mActivity, "启动银联支付...");

	}

	// 丫丫玩的微信支付id 10 不是多宝通的 丫丫玩插件支付
	private void weiXinPay2() {
		// TODO Auto-generated method stub
		// 查看是否安装插件，插件是否为最新版本
		Boolean checkIsPluin = checkIsPluin();

		if (checkIsPluin) {
			Utilsjf.creDialogpro(mActivity, "启动微信支付...");
			AgentApp.mentid = 10;
			// 进入支付流程

		}

	}
	
	

	/**
	 * 查看是否安装插件
	 * 
	 */
	private Boolean checkIsPluin() {
		// TODO Auto-generated method stub
		// AppInfo appInfo = AppUtils.getAppInfo(mActivity,
		// "com.yyw.weixinpay");

		return true;
	}



	// 丫丫玩

	
	private void AlipaypayNow() {
		// 进入支付流程
		/*
		 * new Thread() {
		 * 
		 * @Override public void run() { try { mFirstResult =
		 * com.KgameSdk.sdk.payment.engine.ObtainData .payment(mContext,
		 * AgentApp.mPayOrder, AgentApp.mUser, AgentApp.mPayOrder.paytype);
		 * 
		 * mHandler.sendEmptyMessage(FIRSTRESULT); } catch (Exception e) {
		 * mHandler.sendEmptyMessage(NETERROR); } } }.start();
		 */
	}

	private void startAlipay() {
		Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

	}

	@Override
	public void startActivityForResult(Intent data, int re) {
		// System.out.println(requestCode);
		super.startActivityForResult(data, re);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) {
			return;
		}
		String respCode = data.getExtras().getString("respCode");
		// String respMsg = data.getExtras().getString("respMsg");

		if (respCode == null) {
			Yayalog.loger("rescode为空");
			return;
		}
		if (respCode.equals("00")) {
			Yayalog.loger("微信支付成功");
			onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
		}

		if (respCode.equals("02")) {
			onError(0);
		}

		if (respCode.equals("01")) {
			onError(0);
		}

		if (respCode.equals("03")) {
			onError(0);
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "正在确认支付结果，请稍候查询到账情况",
							Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	@Override
	public void onResume() {
		// System.out.println("mele");
	}

	/**
	 * 调起微信方法
	 * 
	 * @param pay_str
	 *            调起串
	 */

	private void PullWX(String pay_str) {
		Yayalog.loger(pay_str);
		if (isWeixinAvilible()) {
			try {
				System.out.println(pay_str);
				Uri uri = Uri.parse(pay_str);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);

				mActivity.startActivity(intent);
			} catch (Exception e) {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mActivity.getApplicationContext(),
								"网络出错，请重新支付", Toast.LENGTH_LONG).show();
					}
				});
			}
		} else {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity.getApplicationContext(), "微信未安装",
							Toast.LENGTH_LONG).show();
				}
			});

		}

	}

	// 是否安装微信
	public boolean isWeixinAvilible() {
		final PackageManager packageManager = mActivity.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}

	// 新版多宝通支付成功后的弹框确认
	public void weixinNewPayCallback() {
		Utilsjf.creQuestionDialog(mActivity, new PayQuesionCallBack() {

			@Override
			public void onPaySuccess() {
				// TODO Auto-generated method stub
				Utilsjf.stopDialog();
				// 检查订单是否支付成功
				checkOrder();
			}

			@Override
			public void onPayCancel() {
				// TODO Auto-generated method stub
				Utilsjf.stopDialog();
			}
		});
	}

	/**
	 * 检测当前订单是否支付成功
	 */
	public void checkOrder() {
		Utilsjf.creDialogpro(mActivity, "验证支付结果...");
		// 查询订单状态

	}
	
	
	public boolean parseScheme(String url) {
		System.out.println("parseScheme的url："+url);
		
	    if (url.contains("platformapi/startApp")||url.contains("platformapi/startapp")) {
	        return true;
	    } else if ((Build.VERSION.SDK_INT > 19)
	            && (url.contains("platformapi") && url.contains("startapp"))) {
	        return true;
	    } else {
	        return false;
	    }
	}

}
