package com.kkgame.sdk.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class Mylinerlayout extends LinearLayout {

	

	public Mylinerlayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	public void chageView(){
		View view1 = this.getChildAt(0);
		View view2 = this.getChildAt(1);
		if (view1.getVisibility()==View.GONE) {
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.GONE);
		}else {
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.VISIBLE);
		}
	}
	

}
