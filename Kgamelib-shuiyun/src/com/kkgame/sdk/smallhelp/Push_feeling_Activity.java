package com.kkgame.sdk.smallhelp;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.utils.ImageCompress;
import com.kkgame.sdk.utils.Utilsjf;
import com.kkgame.sdk.xml.Push_feeling_xml;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.BitmapUtils;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

public class Push_feeling_Activity extends Activity {

	private Push_feeling_xml mThisview;
	private Button bt_mOk;
	private ImageView bt_mAadpic;
	private EditText et_mFeel;
	private LinearLayout ll_mPicture1;
	private LinearLayout ll_mPicture2;
	private File sdcardTempFile;
	private BitmapUtils bitmapUtils;
	private ArrayList<String> mPicpath1;
	private int photo;
	private HttpUtils httpUtils;
	private ArrayList<String> mUppicurls;
	private User mUser;
	private String feel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 去标题头
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(getApplicationContext());
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if ("portrait".equals(orientation)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		mThisview = new Push_feeling_xml(this);
		setContentView(mThisview.initViewxml());

		initlogic();

	}

	/**
	 * 逻辑处理
	 */
	private void initlogic() {

		bitmapUtils = new BitmapUtils(this);

		httpUtils = new HttpUtils();

		mUser = AgentApp.mUser;

		// 选择图片的集合
		mPicpath1 = new ArrayList<String>();

		bt_mOk = mThisview.getBt_mOk();

		bt_mAadpic = mThisview.getBt_mAadpic();
		bt_mAadpic.setClickable(true);

		et_mFeel = mThisview.getEt_mFeel();

		ll_mPicture1 = mThisview.getLl_mPicture1();

		ll_mPicture2 = mThisview.getLl_mPicture2();

		/*
		 * 设置点击事件从相册中获取图片
		 */
		bt_mAadpic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getPic();
			}

		});

		// 发表用户评论及信息
		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				feel = et_mFeel.getText().toString();
				if (feel.length() < 5) {
					Toast.makeText(getApplicationContext(), "不能少于五个字~!", 0)
							.show();
					return;
				}
				if (feel.length() > 140) {
					Toast.makeText(getApplicationContext(), "发表的内容太多啦~!", 0)
							.show();
					return;
				}

				sendMse();
			}

		});

	}

	/**
	 * 步骤先递归上传图片
	 */
	private void sendMse() {
		mUppicurls = new ArrayList<String>();
		photo = 0;

		if (mPicpath1.size() == 0
				&& TextUtils.isEmpty(et_mFeel.getText().toString())) {
			Toast.makeText(this, "别傲娇噢~!,写点东西再发表吧~!", Toast.LENGTH_LONG).show();
			return;
		}
		Utilsjf.creDialogpro(this, "正在玩命上传~~~");
		if (mPicpath1.size() > 0) {
			sendPhoto(photo);
		} else {
			sendAll();
		}

	}

	/**
	 * 递归上传图片
	 * 
	 * @param photo2
	 */
	private void sendPhoto(final int photo2) {

		RequestParams params = new RequestParams();
		// File file = new File(mPicpath1.get(photo2));
		// params.addBodyParameter("upfile", file);
		params.addBodyParameter("uid", mUser.uid + "");
		params.addBodyParameter("app_id", DeviceUtil.getGameId(this));
		params.addBodyParameter("token", mUser.token);
		httpUtils.send(HttpMethod.POST,
				"http://www.yayawan.com/discuss/upload_token", params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						JSONObject jsonp;
						try {
							Yayalog.loger("上传出错"+responseInfo.result);
							jsonp = new JSONObject(responseInfo.result);
							int success = (Integer) jsonp.get("success");
							String url = jsonp.getString("url");
							String filepath = jsonp.getString("filepath");
							if (success == 0) {
								// mUppicurls.add(url);
								doSendphoto(photo2, url, filepath);
								// System.out.println(url);
							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							Toast.makeText(getApplicationContext(), "发表失败", 0)
									.show();
							Utilsjf.stopDialog();
							finish();
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// Toast.makeText(this, "别傲娇噢~!,写点东西再发表吧~!",
						// Toast.LENGTH_LONG).show();
						Yayalog.loger("上传出错2"+msg);
						Toast.makeText(getApplicationContext(), "发表失败", 0)
								.show();
						Utilsjf.stopDialog();
						finish();
					}
				});

	}

	/**
	 * 
	 * 第二次获取图片地址
	 * 
	 * @param photo2
	 * @param urlp
	 * @param filepath
	 */
	private void doSendphoto(int photo2, String urlp, final String filepath) {
		RequestParams params = new RequestParams();
		File file = new File(mPicpath1.get(photo2));
		params.addBodyParameter("upfile", file);
		// ByteArrayInputStream getimage1 =
		// ImageCompress.getimage1(file.getAbsolutePath());
		// params.addBodyParameter("upfile", getimage1, 100*1024);
		httpUtils.send(HttpMethod.POST, urlp, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// System.out.println(responseInfo.result);

						String tempurl = "http://image.yayawan.com/" + filepath
								+ responseInfo.result;
						photo = photo + 1;
						// System.out.println("图片地址:"+tempurl);
						mUppicurls.add(tempurl);
						if (photo == mPicpath1.size()) {
							sendAll();
						} else {
							sendPhoto(photo);
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Yayalog.loger("上传出错3"+msg);
						Toast.makeText(getApplicationContext(), "发表失败", 0)
								.show();
						Utilsjf.stopDialog();
						finish();
					}
				});

	}

	/**
	 * 上传所有信息
	 */
	private void sendAll() {

		StringBuffer sb = new StringBuffer();
		if (mUppicurls != null) {

			for (int i = 0; i < mUppicurls.size(); i++) {

				if ((i + 1) == mUppicurls.size()) {
					sb.append(mUppicurls.get(i));
				} else {
					sb.append(mUppicurls.get(i));
					sb.append(",");
				}

			}
		}
		String imgurls = sb.toString();

		RequestParams params = new RequestParams();

		params.addBodyParameter("is_app", 1 + "");
		params.addBodyParameter("thread_id",
				"game" + DeviceUtil.getGameId(this));
		params.addBodyParameter("uid", mUser.uid + "");
		params.addBodyParameter("app_id", DeviceUtil.getGameId(this));
		params.addBodyParameter("token", mUser.token);

		params.addBodyParameter("discuss", feel);
		params.addBodyParameter("pid", 0 + "");
		params.addBodyParameter("oid", DeviceUtil.getGameId(this));
		params.addBodyParameter("type", 0 + "");
		params.addBodyParameter("img", imgurls + "");

		httpUtils.send(HttpMethod.POST,
				"http://www.yayawan.com/discuss/add_discuss", params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Utilsjf.stopDialog();
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(responseInfo.result);
							int success = jsonObject.getInt("success");
							if (success == 0) {
								Toast.makeText(getApplicationContext(), "发表成功",
										0).show();
								finish();
							} else {
								Toast.makeText(getApplicationContext(), "发表失败",
										0).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							
							Toast.makeText(getApplicationContext(), "发表失败", 0)
									.show();
							Utilsjf.stopDialog();
							finish();
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Yayalog.loger("上传出错4"+msg);
						Toast.makeText(getApplicationContext(), "发表失败", 0)
								.show();
						Utilsjf.stopDialog();
					}
				});

	}

	/**
	 * 从相册中获取图片
	 */
	private void getPic() {
		if (mPicpath1.size() > 4) {
			Toast.makeText(this, "您上传图片太多,太任性了~!", Toast.LENGTH_LONG).show();
			return;
		}
		// 判断内存卡是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

		} else {

			Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
			finish();
		}

		sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_wo_"
				+ SystemClock.currentThreadTimeMillis() + ".jpg");

		Intent intent1 = new Intent("android.intent.action.PICK");
		intent1.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
				"image/*");

		// intent1.putExtra("output", Uri.fromFile(sdcardTempFile));
		// intent1.putExtra("crop", "true");
		// intent1.putExtra("aspectX", 1);//裁剪框比例
		// intent1.putExtra("aspectY", 1);
		// intent1.putExtra("outputX",180);
		// 输出图片大小 intent1.putExtra("outputY", 180);

		startActivityForResult(intent1, 100);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 100 && data != null) {

			Uri data2 = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(data2, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String path = cursor.getString(column_index);
			Log.e("filelenth", "" + path);
			String tempfilepath1 = getTempfilepath();
			// 压缩图片
			Utilsjf.creDialogpro(this, "正在处理图片...");
			ImageCompress.write(ImageCompress.getimage1(path), tempfilepath1);
			Utilsjf.stopDialog();

			// ImageCompress.copyFile(path, tempfilepath2);
			if (sdcardTempFile.length() < 0) {
				Toast.makeText(this, "请选择文件", Toast.LENGTH_LONG).show();
			} else {

				if (mPicpath1.size() < 5) {
					mPicpath1.add(tempfilepath1);
					final ImageView imageview = mThisview.getImageview();
					imageview.setId(mPicpath1.size() - 1);
					bitmapUtils.display(imageview, path);
					imageview.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mPicpath1.remove(imageview.getId());
							resetView(imageview.getId());
						}

					});
					ll_mPicture1.addView(imageview);
				} else {
					mPicpath1.add(path);
					final ImageView imageview = mThisview.getImageview();
					imageview.setId(mPicpath1.size() - 1);
					bitmapUtils.display(imageview, path);
					ll_mPicture2.addView(imageview);
					imageview.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mPicpath1.remove(imageview.getId());
							resetView(imageview.getId());
						}
					});
				}

			}

		}

	}

	private void resetView(int id) {

		// mPicpath1.remove(id);
		ll_mPicture1.removeAllViews();
		// ll_mPicture2.removeAllViews();
		for (int i = 0; i < mPicpath1.size(); i++) {
			if (i < 5) {

				final ImageView imageview = mThisview.getImageview();
				imageview.setId(i);
				bitmapUtils.display(imageview, mPicpath1.get(i));
				imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPicpath1.remove(imageview.getId());
						resetView(imageview.getId());
					}

				});
				ll_mPicture1.addView(imageview);
			} else {

				final ImageView imageview = mThisview.getImageview();
				imageview.setId(i);
				bitmapUtils.display(imageview, mPicpath1.get(i));
				ll_mPicture2.addView(imageview);
				imageview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPicpath1.remove(imageview.getId());
						resetView(imageview.getId());

					}
				});
			}
		}

	}

	public String getTempfilepath() {
		return new File("/mnt/sdcard/", "tmp_pic_wo_"
				+ SystemClock.currentThreadTimeMillis() + ".jpg")
				.getAbsolutePath();
	}

	@Override
	protected void onDestroy() {
		for (int i = 0; i < mPicpath1.size(); i++) {
			File file = new File(mPicpath1.get(i));
			file.delete();
		}
		super.onDestroy();
	}
}
