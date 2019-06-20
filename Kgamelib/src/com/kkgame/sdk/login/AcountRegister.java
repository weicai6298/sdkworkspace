package com.kkgame.sdk.login;

import java.math.BigInteger;
import java.util.UUID;
import java.util.zip.CRC32;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.db.UserDao;
import com.kkgame.sdk.utils.CryptoUtil;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;


public class AcountRegister {

	private String mName;
	private String mPassword;

	private User mUser;

	private Activity mActivity;

	public AcountRegister(Activity activity) {
		mActivity = activity;
	}



	protected static final int ERROR = 5;

	private void startlogin() {

		//
		Login_ho_dialog login_ho_dialog = new Login_ho_dialog(mActivity);
		login_ho_dialog.dialogShow();

	}

	public void acountRregister() {
		UUID uuid = UUID.randomUUID();
		CRC32 crc32 = new CRC32();
		crc32.update(uuid.toString().getBytes());
		mName = "kk" + crc32.getValue();
		mPassword = CryptoUtil.getSeed();
		// mPassword="123456";
		//密码以图片的形式保存起来
	
		Utilsjf.creDialogpro(mActivity, "正在注册...");
		
		RequestParams rps = new RequestParams();
		rps.addBodyParameter("app_id",
				DeviceUtil.getAppid(mActivity));
		rps.addBodyParameter("imei", DeviceUtil.getIMEI(mActivity));
		rps.addBodyParameter("username", mName);
		rps.addBodyParameter("password", mPassword);
		Yayalog.loger("url:"+ViewConstants.acountregister+"app_id:" + DeviceUtil.getAppid(mActivity)
				+ "imei" + DeviceUtil.getIMEI(mActivity)
				+ "username" + mName + "password" + mPassword);

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST,
				ViewConstants.acountregister, rps,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0,
							String arg1) {
						// TODO Auto-generated method stub
						Utilsjf.stopDialog();
						Toast.makeText(mActivity, "注册失败，请检查网络", 0)
								.show();
						Yayalog.loger(arg0.toString()+"错误信息"+arg1+"注册失败，请检查网络");
					}

					@Override
					public void onSuccess(
							ResponseInfo<String> result) {
						// TODO Auto-generated method stub
						Yayalog.loger("注册返回信息" + result.result);
						Utilsjf.stopDialog();
						User user = parserAcountRegisterResult(result.result);
						if (user != null) {
							
							AgentApp.mUser = user;
							Yayalog.loger(AgentApp.mUser.toString());
							// 将base64加密的用户信息保存到数据库
							UserDao.getInstance(mActivity)
									.writeUser(
											AgentApp.mUser
													.getUserName(),
											AgentApp.mUser.password,
											"123");
							com.kkgame.sdk.utils.MybitmapUtils.savePasswordtoBitmap(mPassword, mName,
									mActivity);
							AgentApp.mUser.password="";
							Yayalog.loger("登陆的uid："+user.toString());
							
							Login_success_dialog login_success_dialog = new Login_success_dialog(
									mActivity);
							login_success_dialog.dialogShow();

						}

					}
				});

	}
	
	/**
	 * 解析账号注册后的结果
	 * 
	 * @param result
	 */
	private User parserAcountRegisterResult(String result) {
		// TODO Auto-generated method stub[
		try {
			JSONObject jsonObject = new JSONObject(result);

			if (!result.contains("success")) {
				String errmsg = jsonObject.optString("err_msg");
				Toast.makeText(mActivity, errmsg, 0).show();
				return null;
			}
			JSONObject datas = jsonObject.getJSONObject("data");
			User user = new User();
			user.setPhone(datas.optString("mobile"));
			user.setUserName(datas.optString("username"));
			user.setToken(datas.optString("token"));
			user.setPassword(mPassword);
			user.setUid(new BigInteger(datas.optString("uid")));
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
