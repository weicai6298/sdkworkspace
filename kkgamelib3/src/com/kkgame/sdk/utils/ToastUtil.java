package com.kkgame.sdk.utils;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.Toastxml_po;

public class ToastUtil {

	/**
	 * 显示成功对话框
	 * 
	 * @param context
	 */
	public static void showSuccess(Context context, String message) {

		Toast toast = new Toast(context);
		Activity co = (Activity) context;
		Toastxml_po toastxml_po = new Toastxml_po(co);

		View view = toastxml_po.initViewxml();
		
		TextView mess = toastxml_po.getTv_message();
			ImageView iv_imageview = toastxml_po.getIv_imageview();
		iv_imageview.setImageBitmap(GetAssetsutils.getImageFromAssetsFile("yaya_chenggong.png", co));
		mess.setTextColor(Color.WHITE);
		mess.setText(message);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();

	}

	/**
	 * 显示失败对话框
	 * 
	 * @param context
	 */
	public static void showError(Context context, String message) {

		Toast toast = new Toast(context);
		Activity co = (Activity) context;
		Toastxml_po toastxml_po = new Toastxml_po(co);

		View view = toastxml_po.initViewxml();
		
		TextView mess = toastxml_po.getTv_message();
			ImageView iv_imageview = toastxml_po.getIv_imageview();
		iv_imageview.setImageBitmap(GetAssetsutils.getImageFromAssetsFile("yaya_shibai.png", co));
		mess.setTextColor(Color.WHITE);
		mess.setText(message);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();

	}

}
