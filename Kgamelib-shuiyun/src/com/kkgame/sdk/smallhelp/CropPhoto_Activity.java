package com.kkgame.sdk.smallhelp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;


public class CropPhoto_Activity extends Activity {

	private File tempFile = new File(Environment.getExternalStorageDirectory(),
			getPhotoFileName());
	private File sdcardTempFile;

	private HttpUtils httpUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去标题头
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		TextView textView = new TextView(this);
		textView.setText("");
		setContentView(textView);
		// openDialog();

		// 判断内存卡是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

		} else {

			Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
			finish();
		}

		sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_wo_"
				+ SystemClock.currentThreadTimeMillis() + ".jpg");

		Intent intent = getIntent();

		int tool = intent.getIntExtra("tool", 0);

		switch (tool) {
		case 0:

			// 选择拍照
			Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(tempFile));
			startActivityForResult(cameraintent, 101);
			break;

		case 1:

			Intent intent1 = new Intent("android.intent.action.PICK");
			intent1.setDataAndType(
					MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
			intent1.putExtra("output", Uri.fromFile(sdcardTempFile));
			intent1.putExtra("crop", "true");
			intent1.putExtra("aspectX", 1);// 裁剪框比例
			intent1.putExtra("aspectY", 1);
			intent1.putExtra("outputX", 180);// 输出图片大小
			intent1.putExtra("outputY", 180);
			startActivityForResult(intent1, 100);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			Log.e("filelenth", "" + sdcardTempFile.length());
			if (sdcardTempFile.length() < 1000) {

				Toast.makeText(this, "请选择文件", Toast.LENGTH_LONG).show();
				finish();
			} else {
				getInfo();
				// uploadMethod(params, uploadHost);
				finish();
			}

		} else if (requestCode == 101) {

			startPhotoZoom(Uri.fromFile(tempFile));
		} else if (requestCode == 102) {

			if (sdcardTempFile.length() < 1000) {
				Toast.makeText(this, "请选择文件", Toast.LENGTH_LONG).show();
				finish();
			} else {
				// uploadMethod(params, uploadHost);

				getInfo();
			}

		}
	}

	private void getInfo() {

		User mUser = AgentApp.mUser;
		httpUtil = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", mUser.uid + "");
		params.addBodyParameter("app_id", DeviceUtil.getGameId(this));
		params.addBodyParameter("token", mUser.token);
		params.addBodyParameter("upfile", sdcardTempFile);

		// InputStream in = new InputStream(new
		// FileInputStream(sdcardTempFile));
		// params.addBodyParameter(key, infile, length);

		httpUtil.send(HttpMethod.POST,
				"http://passport.yayawan.com/api/useravater", params,
				new RequestCallBack<String>() {

					private int object = 0;

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

					//	System.out.println(responseInfo.result);
						try {
							JSONObject jsonObject = new JSONObject(
									responseInfo.result);
							object = (Integer) jsonObject.get("success");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(getApplicationContext(), "上传成功",
								Toast.LENGTH_LONG).show();

						finish();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// System.out.println(error);

						Toast.makeText(getApplicationContext(), "上传失败",
								Toast.LENGTH_LONG).show();
						finish();
					}
				});

	}

	private void upLoadphoto(String url) {
		RequestParams params1 = new RequestParams();

		params1.addBodyParameter("file", sdcardTempFile);
		httpUtil.send(HttpMethod.POST, url, params1,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Toast.makeText(getApplicationContext(), "上传成功",
								Toast.LENGTH_LONG).show();
						finish();
					}

					@Override
					public void onFailure(HttpException error, String msg) {

						Toast.makeText(getApplicationContext(), "上传失败",
								Toast.LENGTH_LONG).show();
						finish();
					}
				});
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		intent.putExtra("output", Uri.fromFile(sdcardTempFile));
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// Log.i("sdcardTempFile:", sdcardTempFile);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 180);
		intent.putExtra("outputY", 180);
		intent.putExtra("return-data", false);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 102);

	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

}
