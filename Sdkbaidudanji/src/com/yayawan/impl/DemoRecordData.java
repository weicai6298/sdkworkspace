package com.yayawan.impl;

public class DemoRecordData {

	private String propsId;
	private String price;
	private String type;
	private String recordNum;
	
	public DemoRecordData(){
		
	}
	
	public DemoRecordData(String mPropsId, String mPrice, String mType, String mRecordNum){
		propsId = mPropsId;
		price = mPrice;
		type = mType;
		recordNum = mRecordNum;
	}

	public String getPropsId() {
		return propsId;
	}

	public void setPropsId(String propsId) {
		this.propsId = propsId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
