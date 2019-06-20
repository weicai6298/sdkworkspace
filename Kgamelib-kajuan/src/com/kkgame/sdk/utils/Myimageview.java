package com.kkgame.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Myimageview extends ImageView {

	
	private Context mContext;
	public Myimageview(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		bm = roundCornerBitmap(bm);
		super.setImageBitmap(bm);
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
	
		super.setImageDrawable(drawable);
	}

	public Myimageview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public Myimageview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Bitmap roundCornerBitmap(Bitmap bitmap) {

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
	    * Drawable转化为Bitmap  
	    */    
	    public static Bitmap drawableToBitmap(Drawable drawable) {    
	       int width = drawable.getIntrinsicWidth();    
	       int height = drawable.getIntrinsicHeight();    
	       Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);    
	       Canvas canvas = new Canvas(bitmap);    
	       drawable.setBounds(0, 0, width, height);    
	       drawable.draw(canvas);    
	       return bitmap;    
	        
	    }    
	    /** 
	     * Bitmap to Drawable 
	     * @param bitmap 
	     * @param mcontext 
	     * @return 
	     */  
	    public static Drawable bitmapToDrawble(Bitmap bitmap,Context mcontext){  
	        Drawable drawable = new BitmapDrawable(bitmap);  
	        return drawable;  
	    } 
}
