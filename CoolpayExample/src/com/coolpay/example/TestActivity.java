package com.coolpay.example;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coolcloud.uac.android.api.Coolcloud;
import com.coolcloud.uac.android.api.ErrInfo;
import com.coolcloud.uac.android.api.OnResultListener;
import com.coolcloud.uac.android.common.Constants;
import com.coolcloud.uac.android.common.Params;
import com.coolcloud.uac.android.gameassistplug.GameAssistApi;
import com.coolcloud.uac.android.gameassistplug.GameAssistConfig;
import com.yulong.android.paysdk.api.CoolpayApi;
import com.yulong.android.paysdk.base.IPayResult;
import com.yulong.android.paysdk.base.common.CoolPayResult;
import com.yulong.android.paysdk.base.common.CoolYunAccessInfo;
import com.yulong.android.paysdk.base.common.PayInfo;

public class TestActivity extends Activity implements OnClickListener {
	// CP有服务端的建议将秘钥放在服务端，通过接口传递给客户端
	public static final String appid1 = "900002278";
	public static final String appkey1 = "98acc3078a37400e895b1961f770f739";
	public static final String payKey1 = "NzYxNUNDMDU1NTk3QzczOEZFRkE3NEEzNjkxRDJFOUQyQ0U4QkQ3Nk1UWTJOVE0yTWpnNU9UQTJOakV5T0RnM09Ea3JNakV3T1RrNE5UWTBOVFkxTXprME1UQTBNakk1TlRFd016ZzVOREUyTURJMk1UQXlNamN4";
//	public static final String appid1 = "900002278";
//	public static final String appkey1 = "98acc3078a37400e895b1961f770f739";
//	public static final String payKey1 = "NzYxNUNDMDU1NTk3QzczOEZFRkE3NEEzNjkxRDJFOUQyQ0U4QkQ3Nk1UWTJOVE0yTWpnNU9UQTJOakV5T0RnM09Ea3JNakV3T1RrNE5UWTBOVFkxTXprME1UQTBNakk1TlRFd016ZzVOREUyTURJMk1UQXlNamN4";
	public static final String appid3 = "900002413";
	public static final String appkey3 = "39de7aad752449288e34175671bd3791";
	public static final String payKey3 = "QUYyODAzMUYyRkE2Qzc4NDZEOTQ5NjA0MDgwMzBCNTI3MDVBRjM4QU1USTRORGsyTVRZek5USTJOamsyTnpFME1UY3JNVFEzT1RJd09UZzROalE1TVRBd09UZzBPVFF5TWpRNE1UWTFPREU0TWpVM01USTJORFU1";
	public static final String appid2 = "900002407";
	public static final String payKey2 = "QTg4OTUzOTdCNjVCMjhGNzg2RjgyODA0NDY1REE2N0QzRDNGREUwRU1UZ3pPVFE0TlRRd01UQTROVEUzTWpBd016Y3JNVGcwTmpJeE1qUXpOakl6T1RrMk56TTFOelV5TlRBMU5UVTBPRFF3TURVeE5UUXhOelV4";
	private String appid;
	private String paykey;
	private Button btn0, btn1, btn2, btn3, coolpay_style, pay_orientation_btn,
			test_btn;
	private Button LoginBtn;
	private Button logout_btn;
	private CoolpayApi api;
	private int chargePoint = 0;
	private EditText et;
	private int pay_style = CoolpayApi.PAY_STYLE_ACTIVITY;
	private int pay_orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER;
	private Dialog dialog;
	private RelativeLayout coolpad_demo_shadow;

	// 酷云账号信息
	private int rtnCode = -1;

	// 账户信息
	private CoolYunAccessInfo accountInfo;
	String openId = "";
	String accesstoken = "";

	// 酷云账号
	private Coolcloud mCoolcloud = null;
	private GameAssistApi mGameAssistApi;
	GameAssistConfig mGameAssistConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.coolpad_activity_test);
		Log.d("kiki", "TestActivity: oncreate()");
		init();
	}

	private void init() {

		// 初始化酷云账号

		// mGameAssistConfig = new GameAssistConfig();
		// mGameAssistConfig.setHideGift(true);
		// if (mCoolcloud != null) {
		// mGameAssistApi = (GameAssistApi) mCoolcloud.getGameAssistApi(this,
		// mGameAssistConfig);
		// mGameAssistApi
		// .addOnSwitchingAccountListen(new GameAssistApi.SwitchingAccount() {
		//
		// @Override
		// public void onSwitchingAccounts() { // 重要
		// // 切换账号
		// doSwitchAccount();
		// }
		// });
		// }

		btn0 = (Button) findViewById(R.id.test0_btn);
		test_btn = (Button) findViewById(R.id.test_btn);
		btn1 = (Button) findViewById(R.id.test1_btn);
		btn2 = (Button) findViewById(R.id.test2_btn);
		btn3 = (Button) findViewById(R.id.test3_btn);
		LoginBtn = (Button) findViewById(R.id.login_btn);
		logout_btn = (Button) findViewById(R.id.logout_btn);
		et = (EditText) findViewById(R.id.price_et);
		coolpay_style = (Button) findViewById(R.id.pay_style);
		pay_orientation_btn = (Button) findViewById(R.id.pay_orientation);
		coolpad_demo_shadow = (RelativeLayout) findViewById(R.id.coolpad_demo_shadow);
		appid = appid1;
		paykey = payKey1;
		test_btn.setText(appid);
		if (CoolpayApi.PAY_STYLE_ACTIVITY == pay_style) {
			coolpay_style.setText("全页面模式");
		} else {
			coolpay_style.setText("窗口模式");
		}
		if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == pay_orientation) {
			pay_orientation_btn.setText("竖屏模式");
		} else if (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE == pay_orientation) {
			pay_orientation_btn.setText("横屏模式");
		} else {
			pay_orientation_btn.setText("自动模式");
		}
		pay_orientation_btn.setOnClickListener(this);
		coolpay_style.setOnClickListener(this);

		test_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (appid.equals(appid1)) {
					appid = appid2;
					paykey = payKey2;
				} else if (appid.equals(appid2)) {
					appid = appid3;
					paykey = payKey3;
				} else if (appid.equals(appid3)) {
					appid = appid1;
					paykey = payKey1;
				}
				test_btn.setText(appid);
			}
		});
		coolpad_demo_shadow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		// 客户端发起登录
		LoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCoolcloud = Coolcloud.get(TestActivity.this, appid);
				if (null != mCoolcloud) {
					// showProcessDialog();
					final Bundle input = new Bundle();
					// 设置横屏显示
					input.putInt(Constants.KEY_SCREEN_ORIENTATION,
							pay_orientation);
					input.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
					// 设置登录方式，这里采用新建账户登录
					input.putString(Constants.KEY_RESPONSE_TYPE,
							Constants.RESPONSE_TYPE_CODE);
					mCoolcloud.login(TestActivity.this, input, new Handler(),
							coolyunListenser);

				} else {
					Log.d("ssqq", "coolcloudApi is null");
				}
			}
		});

		logout_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
	}

	/**
	 * 切换账号 飘浮窗和游戏中的切换账号都在这里边实现
	 */
	private void doSwitchAccount() {
		Bundle mInput = new Bundle();
		// 设置屏幕横竖屏默认为竖屏
		mInput.putInt(Constants.KEY_SCREEN_ORIENTATION, pay_orientation);
		// 设置获取类型
		mInput.putString(Constants.KEY_RESPONSE_TYPE,
				Constants.RESPONSE_TYPE_CODE);
		// 设置需要权限 一般都为get_basic_userinfo这个常量
		mInput.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
		mCoolcloud.loginNew(TestActivity.this, mInput, new Handler(),
				coolyunListenser);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// if (mGameAssistApi != null) {
		// mGameAssistApi.onResume();
		// }
	}

	@Override
	protected void onPause() {
		super.onPause();
		// if (mGameAssistApi != null) {
		// mGameAssistApi.onPause();
		// }
	};

	private OnResultListener coolyunListenser = new OnResultListener() {

		@Override
		public void onResult(Bundle paramBundle) {
			// TODO Auto-generated method stub
			String code = paramBundle.getString(Params.KEY_AUTHCODE);
			Log.d("kiki", "code[Login]:" + code);
			// 这里模拟CP登录过程，仅供参考
			new LoginTask().execute(code);
		}

		@Override
		public void onError(ErrInfo arg0) {
			// TODO Auto-generated method stub
			Log.d("zqll", "error:" + arg0.getMessage());
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
		}
	};

	/**
	 * 成功0；失败-1
	 */
	private IPayResult payResult = new IPayResult() {
		@Override
		public void onResult(CoolPayResult result) {
			coolpad_demo_shadow.setVisibility(View.GONE);
			if (null != result) {
				String resultStr = result.getResult();
				dismissDlaog();
				Log.d("ss77", "resultStr:" + resultStr);
				Log.d("swx", "ResultStatus:" + result.getResultStatus());
				Toast.makeText(TestActivity.this,
						resultStr + "[" + result.getResultStatus() + "]",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		api = CoolpayApi.createCoolpayApi(TestActivity.this, appid);
		PayInfo payInfo = new PayInfo();
		switch (v.getId()) {
		case R.id.test0_btn:
			chargePoint = 1;
			break;
		case R.id.test1_btn:
			chargePoint = 2;
			break;
		case R.id.test2_btn:
			chargePoint = 3;
			break;
		case R.id.test3_btn:
			// chargePoint = 7;
			chargePoint = 6;
			String priceStr = et.getText().toString();
			if (null == priceStr || "".equals(priceStr) || "0".equals(priceStr)) {
				Toast.makeText(this, "请输入正确的价格", Toast.LENGTH_SHORT).show();
				return;
			}
			int price = Integer.parseInt(priceStr);
			payInfo.setPrice(price);// 支付价格,单位为分
			break;
		case R.id.pay_style:
			if (CoolpayApi.PAY_STYLE_ACTIVITY == pay_style) {
				pay_style = CoolpayApi.PAY_STYLE_DIALOG;
				coolpay_style.setText("窗口模式");
			} else {
				pay_style = CoolpayApi.PAY_STYLE_ACTIVITY;
				coolpay_style.setText("全页面模式");
			}

			return;
		case R.id.pay_orientation:
			if (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == pay_orientation) {
				pay_orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
				pay_orientation_btn.setText("横屏模式");
			} else if (ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE == pay_orientation) {
				pay_orientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER;
				pay_orientation_btn.setText("自动模式");
			} else {
				pay_orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				pay_orientation_btn.setText("竖屏模式");
			}

			return;
		default:
			break;
		}
		payInfo.setAppId(appid);
		payInfo.setPayKey(paykey);
		payInfo.setCpPrivate("This is a test private. NEW");
		payInfo.setName("购买300钻石");
		payInfo.setPoint(chargePoint);// 计费点（商品的）ID
		payInfo.setQuantity(1);
		// 如果没有订单号（不可重复），可不设置
		// payInfo.setCpOrder("3623491_1654784857");
		/*
		 * 如果不使用酷云账号，accessInfo 设置为null即可
		 */
		Log.d("zqll", "rtnCode:" + rtnCode);
		Log.d("zqll", "openId:" + openId);
		Log.d("zqll", "accesstoken:" + accesstoken);

		Log.d("zqll", "accountInfo:" + accountInfo);
		if (0 == rtnCode) {
			accountInfo = new CoolYunAccessInfo();
			accountInfo.setAccessToken(accesstoken);
			accountInfo.setOpenId(openId);
		}
		api.startPay(TestActivity.this, payInfo, accountInfo, payResult,
				pay_style, pay_orientation);
		coolpad_demo_shadow.setVisibility(View.VISIBLE);
	}

	private void showAlert() {
		if (null == dialog) {
			dialog = new AlertDialog.Builder(TestActivity.this).setTitle("提示")
					.setMessage("正在支付...").create();
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
		}

		dialog.show();
	}

	private void dismissDlaog() {		if (null != dialog) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent data) {
		if (null != api) {
			Log.d("zqll", "requestCode | resultCode | data[text]:"
					+ requestCode + " | " + resultCode + " | " + data);
			api.onPayResult(requestCode, resultCode, data);
		}
	};

	// 这里模拟CP登录接口，需要CP自己实现,请参考账号接入服务端文档
	private String LoginToCPServer(final String authCode, final String appId) {
		// TODO Auto-generated method stub

		String URL_ACCESS = "http://pay-t.helongs.cn/cptestapi/login";
		String url = URL_ACCESS + "?appId=" + appId + "&authCode=" + authCode;
		Log.d("kiki", "url[Login]:" + url);
		try {
			HttpGet httpget = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpresponse = httpclient.execute(httpget);
			int statuCode = httpresponse.getStatusLine().getStatusCode();
			Log.d("kiki", "statuCode[Login]:" + statuCode);

			if (statuCode == HttpStatus.SC_OK) {
				String response = EntityUtils
						.toString(httpresponse.getEntity());
				Log.d("kiki", "response[Login]:" + response);
				return response;
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("kiki", "e[Login]:" + e.toString());
		}
		return null;
	}

	public CoolYunAccessInfo parseResponse(String response) {
		CoolYunAccessInfo cyai = null;
		if (null != response) {
			JSONObject obj;
			try {
				obj = new JSONObject(response);
				if (null != obj) {
					accountInfo = new CoolYunAccessInfo();
					if (obj.has("openid")) {
						openId = obj.getString("openid");
						Log.d("ss77", "openId:" + openId);
					}
					if (obj.has("access_token")) {
						accesstoken = obj.getString("access_token");
						Log.d("ss77", "accessToken:" + accesstoken);
					}
					if (obj.has("rtn_code")) {
						rtnCode = obj.getInt("rtn_code");
					}
					return cyai;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("ss77", "e[Account]:" + e.getMessage());
				e.printStackTrace();
			}
		}
		return null;

	}

	// 这个是模拟CP登录的方法，此方法只是示例，仅供参考
	class LoginTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return LoginToCPServer(params[0], appid);
		}

		@Override
		protected void onPostExecute(String response) {
			// TODO Auto-generated method stub

			// 将从酷云服务器获取的参数传递给支付SDK
			if (null == response) {
				Toast.makeText(TestActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
			} else {
				// 解析JSON的方法，CP可以按照自己的登录方法自行定义解析方法
				parseResponse(response);
				if (0 == rtnCode) {
					// 登录成功CP要保存酷云信息，之后传给支付SDK
					Toast.makeText(TestActivity.this, "登录成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Log.d("zqll", "rtnCode:" + rtnCode);
					Toast.makeText(TestActivity.this, "登录失败",
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(response);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// showProcessDialog();
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

}