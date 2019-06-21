package com.kkgame.sdk.utils;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ShuoMClickableSpan extends ClickableSpan {
	
	String string;
	Context context;
	public TextOnClickListener onClickListener;
	public String urltext;
	public ShuoMClickableSpan(String str,Context context){
		super();
		this.string = str;
		this.context = context;
	}
	
	
	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(Color.BLUE);
	}


	@Override
	public void onClick(View widget) {
		onClickListener.onclick();
		
	}
	
	
	public void setOnclickListener(TextOnClickListener onClickListener){
		this.onClickListener=onClickListener;
	}
	
	

	
}


