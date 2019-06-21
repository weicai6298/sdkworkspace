package com.kkgame.sdk.pay;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.bean.PayResult;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.Utilsjf;
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

public class GreenblueP {
	
	public static boolean isselectxiaomipay=false;
	
	private static final int WXPAY_NEWFIRSTRESULT = 35;// x宝通新支付方式
	private static final int NETERROR = 18;
	public Activity mContext;
	public Activity mActivity;
	private KgameSdkPaymentCallback mPaymentCallback;
	public static  int BLUEP=37;
	public static  int GREENP=36;
	public int mPaytype=1;
	/**
	 * 
	 * @param macti
	 * @param paramOrder
	 * @param paytype
	 * @param paramCallback
	 */
	public GreenblueP(Activity macti,Order paramOrder,int paytype,
			KgameSdkPaymentCallback paramCallback) {
		mPaytype=paytype;
		mPaymentCallback=paramCallback;
		AgentApp.mPayOrder=paramOrder;
		mContext = macti;
		mActivity = macti;
		// mContext
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case WXPAY_NEWFIRSTRESULT:

				// TODO
				Utilsjf.stopDialog();
				if (mWFirstResult.success == 0) {

					String greenpaction = mWFirstResult.action;
					AgentApp.mPayOrder.id = mWFirstResult.params
							.get("sdcustomno");
					// 进行支付
					PullWX(greenpaction);

				} else {
					// onError(0);
				}
				break;

			case NETERROR:
				 Utilsjf.stopDialog();
				Toast.makeText(mContext, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				mActivity.finish();

				break;

			default:
				break;
			}
		}

	};

	/**
	 * 调起微信方法
	 * 
	 * @param pay_str
	 *            调起串
	 */

	private void PullWX(String pay_str) {
		// Yayalog.loger(pay_str);
		if (isGreenAvilible()) {
			try {
				System.out.println(pay_str);

				GreenP_dialog greenp_dialog = new GreenP_dialog(
						mActivity);
				greenp_dialog.dialogShow();
				greenp_dialog.dialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						mActivity.finish();
					}
				});
				WebView webView = greenp_dialog.getWebview();
				webView.loadUrl(pay_str);
				webView.setWebViewClient(new WebViewClient() {

					@Override
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						Yayalog.loger("重复的url:" + url);

						if (url.startsWith("weixin://wap/pay?")) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(url));
							mActivity.startActivity(intent);

							return true;
						} else if (parseScheme(url)) {
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
								return true;
							}
						} else if (url.contains("success=0")) {
							onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
							return true;
						} else {
							return super.shouldOverrideUrlLoading(view, url);
						}
						
					}

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						// TODO Auto-generated method stub
						// Yayalog.loger("onPageStarted重复的url:"+url);
						super.onPageStarted(view, url, favicon);
					}

					public void onPageFinished(WebView view, String url) {
						// Yayalog.loger("onPageFinished重复的url:"+url);
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
	public boolean isGreenAvilible() {
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

	public boolean parseScheme(String url) {
		System.out.println("parseScheme的url：" + url);

		if (url.contains("platformapi/startApp")
				|| url.contains("platformapi/startapp")) {
			return true;
		} else if ((Build.VERSION.SDK_INT > 19)
				&& (url.contains("platformapi") && url.contains("startapp"))) {
			return true;
		} else {
			return false;
		}
	}

	public void greenP() {
		Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

		AgentApp.mentid = mPaytype;
		WxpaynewKuaipayNow();
	}

	private PayResult mWFirstResult;

	// 微信支付 35新多宝通支付 2.24更新
	private void WxpaynewKuaipayNow() {

		// 进入支付流程
		makeOrder(AgentApp.mentid);
		
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
						Utilsjf.stopDialog();

						Toast.makeText(mActivity, "下单失败，请检查网络是否畅通", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						Yayalog.loger("支付宝下单结果" + result.result);
						// TODO Auto-generated method stub
						Utilsjf.stopDialog();
						
						switch (paytype) {
						case 1:
							//解析支付宝下单结果
							Yayalog.loger("支付宝下单结果" + result.result);
							bluepayResult(result.result);
							break;
						case 2:
							Yayalog.loger("微信下单结果" + result.result);	
							bluepayResult(result.result);
							break;
						case 3:

							break;
						case 4:
							Yayalog.loger("微信下单结果" + result.result);
							bluepayResult(result.result);
							break;
						case 5:
							//解析支付宝下单结果
							bluepayResult(result.result);
							//bluepayResult(result.result);
							break;
						case 36:
							Yayalog.loger("微信下单结果" + result.result);
							bluepayResult(result.result);
							break;
						case 37:
							Yayalog.loger("微信下单结果" + result.result);
							bluepayResult(result.result);

						default:
							break;
						}
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
	
	// 支付宝支付结果
		private void bluepayResult(String result) {
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
					
					GreenP_dialog greenp_dialog = new GreenP_dialog(
							mActivity);
					greenp_dialog.dialogShow();
					WebView webView = greenp_dialog.getWebview();
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


}
