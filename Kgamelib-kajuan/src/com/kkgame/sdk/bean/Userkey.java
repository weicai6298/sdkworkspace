package com.kkgame.sdk.bean;

import java.io.Serializable;

public class Userkey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1903864067434383729L;
	private String username;
	private String userkey;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserkey() {
		return userkey;
	}
	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}
	
	
}
