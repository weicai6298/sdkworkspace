package com.kkgame.sdk.xml;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;

import com.kkgame.utils.DeviceUtil;

public class GetAssetsutils {

	private static int widthPx;

	/**
	 * 从资源文件中获取图片并且根据屏幕大小进行1080适配
	 * @param fileName
	 * @param mContext
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(String fileName,
			Activity mContext) {
		fileName = changeName(fileName,mContext);
		
		Bitmap image = null;
		AssetManager am = mContext.getResources().getAssets();

		try {
			InputStream is = am.open("yayaassets/"+fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
	/**
	 * 从资源文件中获取图片不适配1080
	 * @param fileName
	 * @param mContext
	 * @return
	 */
	public static Bitmap getImageFromAssetsFileNo1080(String fileName,
			Activity mContext) {
		//fileName = changeName(fileName,mContext);
		
		Bitmap image = null;
		AssetManager am = mContext.getResources().getAssets();

		try {
			InputStream is = am.open("yayaassets/"+fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}

	public static Drawable getDrawableFromAssetsFile(String fileName,
			Activity mActivity) {
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(
				GetAssetsutils.getImageFromAssetsFile(fileName, mActivity));
		return drawable;
	}

	public static Drawable get9DrawableFromAssetsFile(String fileName,
			Context mconContext) {
		InputStream stream = null;
		try {
			stream = mconContext.getAssets().open("yayaassets/"+fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Bitmap bitmap = BitmapFactory.decodeStream(stream);
		byte[] chunk = bitmap.getNinePatchChunk();
		boolean bResult = NinePatch.isNinePatchChunk(chunk);
		
		NinePatchDrawable patchy = null;
		try {
			patchy = new NinePatchDrawable(bitmap, chunk,
					new Rect(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return patchy;
	}

	public static Drawable crSelectordraw(String nofocuname, String focuname,
			Activity mContext) {
		
		//nofocuname = changeName(nofocuname,mContext);
		// focuname = changeName(focuname,mContext);
		 
		
		StateListDrawable drawable = new StateListDrawable();
		// Non focused states
		drawable.addState(
				new int[] { -android.R.attr.state_focused,
						-android.R.attr.state_selected,
						-android.R.attr.state_pressed },
				get9DrawableFromAssetsFile(nofocuname, mContext));
		drawable.addState(new int[] { -android.R.attr.state_focused,
				android.R.attr.state_selected, -android.R.attr.state_pressed },
				get9DrawableFromAssetsFile(nofocuname, mContext));
		// Focused states
		drawable.addState(
				new int[] { android.R.attr.state_focused,
						-android.R.attr.state_selected,
						-android.R.attr.state_pressed },
				get9DrawableFromAssetsFile(focuname, mContext));
		drawable.addState(new int[] { android.R.attr.state_focused,
				android.R.attr.state_selected, -android.R.attr.state_pressed },
				get9DrawableFromAssetsFile(focuname, mContext));
		// Pressed
		drawable.addState(new int[] { android.R.attr.state_selected,
				android.R.attr.state_pressed },
				get9DrawableFromAssetsFile(focuname, mContext));
		drawable.addState(new int[] { android.R.attr.state_pressed },
				get9DrawableFromAssetsFile(focuname, mContext));

		return drawable;
	}

	public static Drawable crno9Selectordraw(String nofocuname,
			String focuname, Activity mContext) {
		
		
		StateListDrawable drawable = new StateListDrawable();
		// Non focused states
		drawable.addState(
				new int[] { -android.R.attr.state_focused,
						-android.R.attr.state_selected,
						-android.R.attr.state_pressed },
				getDrawableFromAssetsFile(nofocuname, mContext));
		drawable.addState(new int[] { -android.R.attr.state_focused,
				android.R.attr.state_selected, -android.R.attr.state_pressed },
				getDrawableFromAssetsFile(nofocuname, mContext));
		// Focused states
		drawable.addState(
				new int[] { android.R.attr.state_focused,
						-android.R.attr.state_selected,
						-android.R.attr.state_pressed },
				getDrawableFromAssetsFile(focuname, mContext));
		drawable.addState(new int[] { android.R.attr.state_focused,
				android.R.attr.state_selected, -android.R.attr.state_pressed },
				getDrawableFromAssetsFile(focuname, mContext));
		// Pressed
		drawable.addState(new int[] { android.R.attr.state_selected,
				android.R.attr.state_pressed },
				getDrawableFromAssetsFile(focuname, mContext));
		drawable.addState(new int[] { android.R.attr.state_pressed },
				getDrawableFromAssetsFile(focuname, mContext));

		return drawable;
	}

	public static Drawable crno9Selectordraw1(String nofocuname,
			String focuname, Activity mContext) {
		
		StateListDrawable drawable = new StateListDrawable();
		// Non focused states
		drawable.addState(
				new int[] { -android.R.attr.state_checked,
						-android.R.attr.state_selected,
						-android.R.attr.state_pressed },
				getDrawableFromAssetsFile(nofocuname, mContext));
		drawable.addState(new int[] { -android.R.attr.state_focused,
				android.R.attr.state_selected, -android.R.attr.state_pressed },
				getDrawableFromAssetsFile(nofocuname, mContext));
		// Pressed
		drawable.addState(new int[] { android.R.attr.state_checked,
				android.R.attr.state_pressed },
				getDrawableFromAssetsFile(focuname, mContext));
		drawable.addState(new int[] { android.R.attr.state_pressed },
				getDrawableFromAssetsFile(focuname, mContext));

		return drawable;
	}

	public static String changeName(String name, Activity mcontext) {
		// 判断屏幕放向
		String orientation = DeviceUtil.getOrientation(mcontext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {

			widthPx = DisplayUtils.getHeightPx(mcontext);
		} else if ("portrait".equals(orientation)) {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			widthPx = DisplayUtils.getWidthPx(mcontext);

		}

		// Log.e("前size", size+"++++++++++++++");
		// Log.e("widthPx", widthPx + "++++++++++++++");
		if (widthPx > 720&&name.endsWith(".png")) {
			name = name.substring(0, name.indexOf(".png")) + "1080.png";

		}

		return name;
	}

}
