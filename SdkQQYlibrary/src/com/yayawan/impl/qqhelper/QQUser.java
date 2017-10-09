package com.yayawan.impl.qqhelper;

import com.tencent.ysdk.module.user.UserRelationRet;

public class QQUser {
	
		private int platform;
		private String accessToken;
		private String payToken;
		private String openid;
		private String msg;
		private String pf;
		private String pf_key;
		private String nickName;
		private UserRelationRet relationRet;
		public int getPlatform() {
			return platform;
		}
		public void setPlatform(int platform) {
			this.platform = platform;
		}
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public String getPayToken() {
			return payToken;
		}
		public void setPayToken(String payToken) {
			this.payToken = payToken;
		}
		public String getOpenid() {
			return openid;
		}
		public void setOpenid(String openid) {
			this.openid = openid;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getPf() {
			return pf;
		}
		public void setPf(String pf) {
			this.pf = pf;
		}
		public String getPf_key() {
			return pf_key;
		}
		public void setPf_key(String pf_key) {
			this.pf_key = pf_key;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public UserRelationRet getRelationRet() {
			return relationRet;
		}
		public void setRelationRet(UserRelationRet relationRet) {
			this.relationRet = relationRet;
		}
		@Override
		public String toString() {
			return "QQUser [platform=" + platform + ", accessToken="
					+ accessToken + ", payToken=" + payToken + ", openid="
					+ openid + ", msg=" + msg + ", pf=" + pf + ", pf_key="
					+ pf_key + ", nickName=" + nickName + ", relationRet="
					+ relationRet + "]";
		}
		
}
