package com.kkgame.sdk.other;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.kkgame.sdk.login.Announcement_dialog;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;
import com.kkgame.utils.Yibuhttputils;


public class JFnoticeUtils {
	
	 String data;

	/**
	 * 获取公告信息，如果有公告则弹出公告，如果无公告就啥事都没有，一定要在主线程运行
	 * 
	 * @param mActicity
	 */
	public void getNotice(final Activity mActicity) {

		Yayalog.loger("获取公告：" + ViewConstants.NOTICEURL);
		try {
			Yibuhttputils yibuhttputils = new Yibuhttputils() {

				@Override
				public void sucee(final String responseInfo) {
					// TODO Auto-generated method stub
					Yayalog.loger("获取公告返回：" + responseInfo);
					// TODO Auto-generated method stub
					if (responseInfo.contains("success")) {
						try {
							JSONObject jsonObject = new JSONObject(responseInfo);
							 data = jsonObject.optString("data");

							mActicity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									new Announcement_dialog(mActicity,
											data).dialogShow();
								}
							});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Yayalog.loger("获取公告失败：" + e);
						}
					}

				}

				@Override
				public void faile(String err, String rescode) {
					// TODO Auto-generated method stub
					Yayalog.loger("获取公告失败：" + err);
				}
			};
			String pingjie = "app_id=" + DeviceUtil.getAppid(mActicity);
			yibuhttputils.runHttp(ViewConstants.NOTICEURL + "/?" + pingjie, "",
					Yibuhttputils.GETMETHOD, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Yayalog.loger("获取公告发生异常：" + e.toString());
			// e.printStackTrace();
		}

	}
}
