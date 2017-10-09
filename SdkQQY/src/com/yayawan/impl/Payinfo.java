package com.yayawan.impl;

public class Payinfo {

	public String opentype;
	
	public String offerId;
	public String openId;
	public String openKey;
	
	public String sessionId;
	public String sessionType;
	public String zoneId;
	
	public String pf;
	public String pfKey;
	public String tokenType;
	
	public String prodcutId;
	public String saveValue;
	
	public String goodsTokenUrl;
	public String isCanChange;
	public String resId;
	
	public String resData;
	
	public String qq_paytoken;

	public String wx_paytoken;

	@Override
	public String toString() {
		return "Payinfo [opentype=" + opentype + ", offerId=" + offerId
				+ ", openId=" + openId + ", openKey=" + openKey
				+ ", sessionId=" + sessionId + ", sessionType=" + sessionType
				+ ", zoneId=" + zoneId + ", pf=" + pf + ", pfKey=" + pfKey
				+ ", tokenType=" + tokenType + ", prodcutId=" + prodcutId
				+ ", saveValue=" + saveValue + ", goodsTokenUrl="
				+ goodsTokenUrl + ", isCanChange=" + isCanChange + ", resId="
				+ resId + ", resData=" + resData + ", qq_paytoken="
				+ qq_paytoken + "]";
	}
	
}
