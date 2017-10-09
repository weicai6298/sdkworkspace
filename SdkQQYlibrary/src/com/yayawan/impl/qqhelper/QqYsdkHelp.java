package com.yayawan.impl.qqhelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.library.R;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.UserLoginRet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 
 * @author qqysdk登陆支付帮助类
 * 
 */
public class QqYsdkHelp {

	public static boolean islogin = false;
	public static QqYsdkUserCallback mQqusercallback;
	public static QqYsdkPayCallback mQqpaycallback;
	private static Dialog dialog;
	private static LinearLayout ysdk_llogin;
	private static ProgressBar ysdkpro;

	public static void Login(Activity mActivity,
			QqYsdkUserCallback mqqusercallback) {
		mQqusercallback = mqqusercallback;
		// 设置个判断条件，防止腾讯刷新数据时候进行登陆操作
		islogin = true;
		ePlatform platform = getPlatform();

		if (platform == ePlatform.QQ) {
			System.out.println("历史登陆为qq");
			Log.e("ysdk", "历史登陆为qq");
			loginDialog(mActivity, true);
			YSDKApi.queryUserInfo(ePlatform.QQ);
		} else if (platform == ePlatform.WX) {
			System.out.println("历史登陆为wx");
			loginDialog(mActivity, true);
			Log.e("ysdk", "历史登陆为wx");
			YSDKApi.queryUserInfo(ePlatform.WX);
		} else {
			// 登陆窗口
			loginDialog(mActivity, false);

		}

	}

	/**
	 * 支付 1.不可改变金额 2. 默认游戏大区为1区 3.默认使用asset文件夹下的sample_yuanbao.png元宝图片
	 * 
	 * @param money
	 *            钱 单位角
	 * @param ext
	 *            附加参数
	 */
	public static void pay(Activity macticity, final String money, String ext,
			final String notifyurl, QqYsdkPayCallback mqqysdkpaycallback) {

		mQqpaycallback = mqqysdkpaycallback;

		final String zoneId = "1";
		boolean isCanChange = false;
		AssetManager assetManager = macticity.getAssets();
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

		YSDKApi.recharge(zoneId, money, isCanChange, appResData, ext,
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
								paySuccNotifyNet(zoneId, money, notifyurl);

								break;
							// 取消支付
							case PayRet.PAYSTATE_PAYCANCEL:
								System.out.println("用户取消支付：" + ret.toString());
								payFail("用户取消支付：" + ret.toString(), ret);

								break;
							// 支付结果未知
							case PayRet.PAYSTATE_PAYUNKOWN:
								System.out.println("用户支付结果未知，建议查询余额："
										+ ret.toString());
								payFail("用户支付结果未知，建议查询余额：" + ret.toString(),
										ret);
								break;
							// 支付失败
							case PayRet.PAYSTATE_PAYERROR:
								payFail("支付异常" + ret.toString(), ret);
								System.out.println("支付异常" + ret.toString());
								break;
							}
						} else {
							switch (ret.flag) {

							case eFlag.Pay_User_Cancle:
								// 用户取消支付
								payFail("用户取消支付：" + ret.toString(), ret);
								System.out.println("用户取消支付：" + ret.toString());
								break;
							case eFlag.Pay_Param_Error:
								payFail("支付失败，参数错误" + ret.toString(), ret);
								System.out.println("支付失败，参数错误" + ret.toString());
								break;
							case eFlag.Error:
							default:
								payFail("支付异常" + ret.toString(), ret);
								System.out.println("支付异常" + ret.toString());

								break;
							}
						}
					}
				});
	}

	private static final OkHttpClient client = new OkHttpClient();

	/**
	 * 支付成功通知服务端
	 * 
	 * @param zoneid
	 * @param notifyurl2
	 * @param money
	 */
	public static void paySuccNotifyNet(String zoneid, String money,
			String notifyurl2) {
		UserLoginRet ret1 = new UserLoginRet();
		int platform = YSDKApi.getLoginRecord(ret1);
		String accessToken = ret1.getAccessToken();
		String payToken = ret1.getPayToken();
		String openid = ret1.open_id;
		int flag = ret1.flag;
		String msg = ret1.msg;
		String pf = ret1.pf;
		String pf_key = ret1.pf_key;

		ePlatform platform1 = getPlatform();
		String logintype="";
		if (platform1 == ePlatform.QQ) {
			logintype="qq";
		} else if (platform1 == ePlatform.WX) {
			logintype="wx";
		}
		
		FormBody.Builder formBody = new FormBody.Builder()
		.add("accessToken", accessToken)
		.add("payToken", payToken)
		.add("openid", openid)
		.add("pf", pf)
		.add("pf_key", pf_key)
		.add("logintype", logintype)
		.add("accessToken", accessToken);
		
		/*Request request = new Request.Builder().url(
				notifyurl2).post(formBody)
			      .build();*/
/*
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO Auto-generated method stub
				if (!response.isSuccessful())
					throw new IOException("Unexpected code " + response);

				Headers responseHeaders = response.headers();
				for (int i = 0; i < responseHeaders.size(); i++) {
					System.out.println(responseHeaders.name(i) + ": "
							+ responseHeaders.value(i));
				}

				System.out.println(response.body().string());
			}
		});
*/
	}

	/**
	 * 初始化，在oncreate后调用
	 * 
	 * @param paramActivity
	 */
	public static void inintsdk(Activity paramActivity) {
		YSDKApi.setUserListener(new YSDKCallback(paramActivity));
		YSDKApi.setBuglyListener(new YSDKCallback(paramActivity));
	}

	/**
	 * 登陆窗口 可制定
	 * 
	 * @param mActivity
	 */
	public static void loginDialog(Activity mActivity, boolean islogding) {
		// 登陆弹窗设置开始
		dialog = new Dialog(mActivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.logindialog);
		ysdk_llogin = (LinearLayout) dialog.findViewById(R.id.ysdk_login);
		ImageView qq_login = (ImageView) dialog.findViewById(R.id.qq_login);
		ImageView wx_login = (ImageView) dialog.findViewById(R.id.wx_login);
		ysdkpro = (ProgressBar) dialog.findViewById(R.id.ysdk_pro);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.alpha = 0.9f; // 透明度
		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
		dialog.show();
		// 登陆弹窗结束
		// 如果只是loding把登陆隐藏
		if (islogding) {
			ysdk_llogin.setVisibility(View.GONE);
			ysdkpro.setVisibility(View.VISIBLE);
		}

		// 登陆按钮监听
		qq_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				YSDKApi.login(ePlatform.QQ);
				ysdk_llogin.setVisibility(View.GONE);
				ysdkpro.setVisibility(View.VISIBLE);
			}
		});
		wx_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				YSDKApi.login(ePlatform.WX);
				ysdk_llogin.setVisibility(View.GONE);
				ysdkpro.setVisibility(View.VISIBLE);
			}
		});

	}

	// 获取当前登录平台
	public static ePlatform getPlatform() {
		UserLoginRet ret = new UserLoginRet();
		YSDKApi.getLoginRecord(ret);
		if (ret.flag == eFlag.Succ) {
			return ePlatform.getEnum(ret.platform);
		}
		return ePlatform.None;
	}

	/**
	 * 登录失败
	 */
	public static void loginFail() {
		islogin = false;
		if (QqYsdkHelp.mQqusercallback != null) {
			QqYsdkHelp.mQqusercallback.onError(0);
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * 登录成功
	 * 
	 * @param qqUser
	 */
	public static void loginSuccess(QQUser qqUser) {
		islogin = false;
		if (QqYsdkHelp.mQqusercallback != null) {
			QqYsdkHelp.mQqusercallback.onSuccess(qqUser, 1);
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}

	/*
	 * 支付成功
	 */
	public static void paySuce() {

		if (QqYsdkHelp.mQqpaycallback != null) {
			QqYsdkHelp.mQqpaycallback.onSuccess();
		}
	}

	/**
	 * 支付失败
	 * 
	 * @param errtip
	 * @param ret
	 */
	public static void payFail(String errtip, PayRet ret) {

		if (QqYsdkHelp.mQqpaycallback != null) {
			QqYsdkHelp.mQqpaycallback.onPayFail(errtip, ret);
		}
	}

	/**
	 * 注销登陆
	 */
	public static void logout() {
		YSDKApi.logout();
	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		YSDKApi.onCreate(paramActivity);

	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		YSDKApi.onStop(paramActivity);
	}

	public static void onResume(Activity paramActivity) {
		YSDKApi.onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
		YSDKApi.onPause(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		YSDKApi.onRestart(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		YSDKApi.onDestroy(paramActivity);
	}

	public static void onActivityResult(int paramInt1, int paramInt2,
			Intent paramIntent) {
		// TODO Auto-generated method stub
		YSDKApi.onActivityResult(paramInt1, paramInt2, paramIntent);
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		YSDKApi.handleIntent(paramIntent);
	}

}
