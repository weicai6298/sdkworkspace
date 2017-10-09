package com.yayawan.impl;

public class PropsData {

	private String mPropsId;
	private int mDrawable;
	private int mTitle;
	private int mPrice;
	
	public PropsData(){
		
	}
	
	public PropsData(String propsId, int drawable, int title, int price){
		mPropsId = propsId;
		mDrawable = drawable;
		mTitle = title;
		mPrice = price;
	}

	public int getmDrawable() {
		return mDrawable;
	}

	public void setmDrawable(int mDrawable) {
		this.mDrawable = mDrawable;
	}

	public int getmTitle() {
		return mTitle;
	}

	public void setmTitle(int mTitle) {
		this.mTitle = mTitle;
	}

	public int getmPrice() {
		return mPrice;
	}

	public void setmPrice(int mPrice) {
		this.mPrice = mPrice;
	}

	public String getmPropsId() {
		return mPropsId;
	}

	public void setmPropsId(String mPropsId) {
		this.mPropsId = mPropsId;
	}
	
}
