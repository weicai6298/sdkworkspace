package com.kkgame.sdk.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.kkgame.sdk.xml.GetAssetsutils;
import com.lidroid.jxutils.BitmapUtils;


/**
 * Created by Administrator on 2015/7/8.
 */
public class MybitmapUtils {

	private static BitmapUtils sHascaheBitmapUtils;

	private static MybitmapUtils sMybitmapUtils;

	private MybitmapUtils() {

	}

	public static <T extends View> void disPlayHascache(Activity mactivity,
			T container, String uri) {
		// if (sHascaheBitmapUtils == null) {
		BitmapUtils sHascaheBitmapUtils = new BitmapUtils(mactivity);
		// }
		Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(500);

		sHascaheBitmapUtils.configDefaultImageLoadAnimation(alphaAnimation);

		sHascaheBitmapUtils.display(container, uri);
	}

	/**
	 * 展示图片,
	 * 
	 * @param mactivity
	 *            环境
	 * @param container
	 *            容器
	 * @param uri
	 *            目标图片url
	 * @param defaultimage
	 *            加载失败默认图片
	 */
	public static <T extends View> void displayImage(Activity mActivity,
			T container, String uri, String defaultimage) {
		// if (sHascaheBitmapUtils == null) {
		BitmapUtils bitmapUtils = new BitmapUtils(mActivity);
		// }
		bitmapUtils.configDefaultLoadingImage(GetAssetsutils
				.getImageFromAssetsFile("yaya_defaultloading.png", mActivity));
		bitmapUtils.configDefaultLoadFailedImage(roundCornerBitmap(GetAssetsutils
				.getImageFromAssetsFileNo1080(defaultimage,
						mActivity)));
		Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(500);

		bitmapUtils.configDefaultImageLoadAnimation(alphaAnimation);

		bitmapUtils.display(container, uri);
	}
	
	public static Bitmap roundCornerBitmap(Bitmap bitmap) {

		//创建一个和传进来位图一样大小的位图
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		//新建一个画布
		Canvas canvas = new Canvas(output);

		int color = 0xff424242;

		//新建一个画笔
		Paint paint = new Paint();

		
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		RectF rectf = new RectF(rect);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);

		paint.setColor(color);
		//画一个圆形
		canvas.drawRoundRect(rectf, bitmap.getWidth() / 2,
				bitmap.getHeight() / 2, paint);
		
		//设置混合模式为srcin
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;

	}
	
	/**
	 * 保存一张用户密码图片到手机中
	 * @param password
	 * @param username
	 * @param mcontext
	 * @return
	 */
	public static Bitmap savePasswordtoBitmap(String password,String username, Context mcontext) {

		Bitmap copy = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(copy);

		String temppassword = password;
		Paint mPaint = new Paint();
		mPaint.setStrokeWidth(3);
		mPaint.setTextSize(40);
		mPaint.setColor(Color.RED);
		canvas.drawColor(Color.YELLOW);
		
		mPaint.setTextAlign(Align.LEFT);
		
		canvas.drawText(username, 5,
				60, mPaint);
		canvas.drawText(temppassword, 5,
				120, mPaint);
		File myCaptureFile = null;
		try {
			myCaptureFile = new File(Environment.getExternalStorageDirectory()
					+ "/游戏密码"+username + ".jpg");
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			copy.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		ToastUtil.showSuccess(mcontext, "密码保存在sd卡中");
		
		
		  // 其次把文件插入到系统图库
	    try {
	        MediaStore.Images.Media.insertImage(mcontext.getContentResolver(),
	        		myCaptureFile.getAbsolutePath(), "/游戏密码"+username + ".jpg", null);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    // 最后通知图库更新
	    String path = Environment.getExternalStorageDirectory().getPath();
	    mcontext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
	    ToastUtil.showSuccess(mcontext, "密码保存在sd卡中,请在相册中查看！");

		return copy;
	}
	
}
