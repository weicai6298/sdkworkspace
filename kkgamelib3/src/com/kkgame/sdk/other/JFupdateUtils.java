package com.kkgame.sdk.other;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.CircleProgressView;
import com.kkgame.sdk.utils.ToastUtil;
import com.kkgame.sdk.utils.UpdateDialog;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class JFupdateUtils {

	private HttpUtils httpUtils;
	private Activity mActivity;
	private String create_time;
	private String full_size;
	private String full_url;
	private String game_id;
	private String note;
	private String simple_size;
	private String simple_url;
	private String status;
	private String success;
	private String union_id;
	private String update_time;
	private String versioncode;
	private String versionname;
	JFupdateUtils jFupdateUtils;
	private String apkpath = Environment.getExternalStorageDirectory()
			.getPath() + "/";
	private static String url;

	public JFupdateUtils(Activity mActivity) {
		this.mActivity = mActivity;
		jFupdateUtils = this;
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	private int getVersioncode() {

		PackageManager pm = mActivity.getPackageManager();

		PackageInfo pinfo = null;
		try {
			pinfo = pm.getPackageInfo(mActivity.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String versionCode = pinfo.versionName;
		if (pinfo == null) {
			return -1;
		} else {
			return pinfo.versionCode;
		}

	}

	// 所有游戏都有这个更新操作。包括丫丫玩。得到参数。先判断url是否有值。有值再执行更新弹框，通过判断status来确定是否强制更新
	public void startUpdate() {
		httpUtils = new HttpUtils();
		if (getVersioncode() == -1) {
			return;
		}
		String url = ViewConstants.updateurl;
		;
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("appid", DeviceUtil.getAppid(mActivity));
		requestParams.addBodyParameter("versioncode",
				DeviceUtil.getVersionCode(mActivity));
		Yayalog.loger("召唤神兽url:" + url);
		httpUtils.send(HttpMethod.POST, "" + url,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub\
						Yayalog.loger("gengxin" + responseInfo.toString());
						try {
							// System.out.println(responseInfo.result);
							if (responseInfo.result.contains("11001")) {

							} else {
								resolJson(responseInfo.result);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							// Yayalog.loger("解析出错"+e);
						}
						Yayalog.loger("解析完毕:" + jFupdateUtils.toString());
						if (err_code == 0 && !TextUtils.isEmpty(full_url)) {

							int k = full_url.lastIndexOf("/");
							String apkname = full_url.substring(k + 1);

							apkpath = apkpath + apkname;
							Updatedialog(full_url);
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						// System.out.println("下载失败" + msg);
						// ToastUtil.showError(mActivity, "下载失败,请检查网络");
					}
				});

	}

	private static int err_code;

	// 解析json数据
	private void resolJson(String ret) throws JSONException {

		Yayalog.loger("更新返回结果：" + ret);

		JSONObject jsonObject2 = new JSONObject(ret);

		err_code = jsonObject2.optInt("err_code");

		JSONObject jsonObject = jsonObject2.getJSONObject("data");

		create_time = (String) (jsonObject.isNull("create_time") ? ""
				: jsonObject.get("create_time"));
		full_size = (String) (jsonObject.isNull("full_size") ? "" : jsonObject
				.get("full_size"));
		game_id = (String) (jsonObject.isNull("game_id") ? "" : jsonObject
				.get("game_id"));
		full_url = (String) (jsonObject.isNull("full_url") ? "" : jsonObject
				.get("full_url"));
		// max_versioncode = (String) (jsonObject.isNull("max_versioncode") ? ""
		// : jsonObject.get("max_versioncode"));
		note = (String) (jsonObject.isNull("note") ? "" : jsonObject
				.get("note"));
		simple_size = (String) (jsonObject.isNull("simple_size") ? ""
				: jsonObject.get("simple_size"));
		simple_url = (String) (jsonObject.isNull("simple_url") ? ""
				: jsonObject.get("simple_url"));
		status = (String) (jsonObject.isNull("status") ? "" : jsonObject
				.getInt("status") + "");
		success = (String) (jsonObject.isNull("success") ? "" : jsonObject
				.getInt("success") + "");
		update_time = (String) (jsonObject.isNull("update_time") ? ""
				: jsonObject.get("update_time"));
		versioncode = (String) (jsonObject.isNull("versioncode") ? ""
				: jsonObject.getInt("versioncode") + "");
		versionname = (String) (jsonObject.isNull("versionname") ? ""
				: jsonObject.get("versionname"));
		// System.out.println(full_url);
	}

	private void Updatedialog(final String url) {
		final UpdateDialog updateDialog = new UpdateDialog(mActivity);
		updateDialog.setMessage(note);
		String cancletext = "";
		String submittext = "更新";

		// 2为强制更新
		if (status.equals("2")) {
			updateDialog.setCancelable(false);
			updateDialog.setCancle("退出游戏", new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mActivity.finish();
				}
			});
		} else {
			updateDialog.setCancelable(true);
			updateDialog.setCancle("取消", new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					updateDialog.dismiss();
				}
			});
		}

		updateDialog.setSubmit(submittext, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateDialog.dismiss();
				updatePro(url);

			}
		});
		updateDialog.show();
	}

	public void updatePro(String url) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(url, apkpath, new RequestCallBack<File>() {

			UpdateProgress_dialog updateProgress_dialog;
			long tempcurrent = 0;

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				// TODO Auto-generated method stub
				Yayalog.loger("下载成功");
				updateProgress_dialog.dialogDismiss();
				String fileName = apkpath;
				Uri uri = Uri.fromFile(new File(fileName));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				mActivity.startActivity(intent);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				Yayalog.loger("下载失败" + msg);
				ToastUtil.showError(mActivity, "下载失败,请检查网络");
				updateProgress_dialog.dialogDismiss();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
				// System.out.println("total" + total + "current" + current);
				CircleProgressView circleProgressView = updateProgress_dialog
						.getCircleProgressView();
				int pro = (int) (current * 100 / total);
				long rate = current - tempcurrent;
				tempcurrent = current;
				circleProgressView.setProgress(pro);
				circleProgressView.setmTxtHint1(rate / 1000 + "kb/s");
				circleProgressView.setmTxtHint2("大小:" + total / 1024 / 1024
						+ "M");
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				updateProgress_dialog = new UpdateProgress_dialog(mActivity);
				updateProgress_dialog.dialogShow();

			}
		});
	}

	@Override
	public String toString() {
		return "JFupdateUtils [httpUtils=" + httpUtils + ", mActivity="
				+ mActivity + ", create_time=" + create_time + ", full_size="
				+ full_size + ", full_url=" + full_url + ", game_id=" + game_id
				+ ", note=" + note + ", simple_size=" + simple_size
				+ ", simple_url=" + simple_url + ", status=" + status
				+ ", success=" + success + ", union_id=" + union_id
				+ ", update_time=" + update_time + ", versioncode="
				+ versioncode + ", versionname=" + versionname + ", apkpath="
				+ apkpath + "]";
	}

}
