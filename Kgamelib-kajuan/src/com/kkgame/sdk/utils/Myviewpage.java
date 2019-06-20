package com.kkgame.sdk.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class Myviewpage extends ViewPager{

	
	private boolean isCanScroll = true;  
	  
    public Myviewpage(Context context) {  
        super(context);  
    }  
  
    public Myviewpage(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setScanScroll(boolean isCanScroll){  
        this.isCanScroll = isCanScroll;  
    }  
  
  
   
        
}
