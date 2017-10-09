package com.kkgame.sdk.bean;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 保存用户登录后的用户信息
 * 
 * @author wjy
 * 
 */
public class User {
    // 用户名
    public String userName;
    // 真实姓名
    public String relname;
    // 用户id
    public BigInteger uid;
    
    // 单机用户id
    public String user_uid;
    // 登录成功后的授权信息
    public String token;
    //登陆状态
    public int success;
    //登陆返回状态信息
    
    public String body;
    //secret
    public String secret;
    //账号余额
    public String money;
    
    public String password;
    
    public String last_login;
    
    public String lasttime;
    
    public String icon;
    
    public String nick;
    
    public String phone;
    
    public int phoneActive = -1;
    
    public ArrayList<BankInfo> banks;
    public ArrayList<BankInfo> cashbanks;
    public User() {
    }


  

    public User(String userName, BigInteger uid, String token, int success,
            String body, String money) {
        super();
        this.userName = userName;
        this.uid = uid;
        this.token = token;
        this.success = success;
        this.body = body;
        this.money = money;
        
    }
    




    public User(int success, String body) {
        super();
        this.success = success;
        this.body = body;
    }




    public String getUserName() {
		return userName;
	}




	public String getRelname() {
		return relname;
	}




	public void setRelname(String relname) {
		this.relname = relname;
	}




	public String getSecret() {
		return secret;
	}




	public void setSecret(String secret) {
		this.secret = secret;
	}




	public void setUserName(String userName) {
		this.userName = userName;
	}




	public BigInteger getUid() {
		return uid;
	}




	public void setUid(BigInteger uid) {
		this.uid = uid;
	}




	public String getToken() {
		return token;
	}




	public void setToken(String token) {
		this.token = token;
	}




	public int getSuccess() {
		return success;
	}




	public void setSuccess(int success) {
		this.success = success;
	}




	public String getBody() {
		return body;
	}




	public void setBody(String body) {
		this.body = body;
	}




	public String getMoney() {
		return money;
	}




	public void setMoney(String money) {
		this.money = money;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	




	public String getLast_login() {
		return last_login;
	}




	public void setLast_login(String last_login) {
		this.last_login = last_login;
	}




	public String getIcon() {
		return icon;
	}




	public void setIcon(String icon) {
		this.icon = icon;
	}




	public String getNick() {
		return nick;
	}




	public void setNick(String nick) {
		this.nick = nick;
	}




	public String getPhone() {
		return phone;
	}




	public void setPhone(String phone) {
		this.phone = phone;
	}




	public int getPhoneActive() {
		return phoneActive;
	}




	public void setPhoneActive(int phoneActive) {
		this.phoneActive = phoneActive;
	}




	public ArrayList<BankInfo> getBanks() {
		return banks;
	}




	public void setBanks(ArrayList<BankInfo> banks) {
		this.banks = banks;
	}




	public ArrayList<BankInfo> getCashbanks() {
		return cashbanks;
	}




	public String getUser_uid() {
		return user_uid;
	}




	public void setUser_uid(String user_uid) {
		this.user_uid = user_uid;
	}




	public void setCashbanks(ArrayList<BankInfo> cashbanks) {
		this.cashbanks = cashbanks;
	}




	@Override
	public String toString() {
		return "User [userName=" + userName + ", uid=" + uid + ", token="
				+ token + ", success=" + success + ", body=" + body
				+ ", money=" + money + ", password=" + password
				+ ", last_login=" + last_login + ", icon=" + icon + ", nick="
				+ nick + ", phone=" + phone + ", phoneActive=" + phoneActive
				+ ", banks=" + banks + ", cashbanks=" + cashbanks + "]";
	}




	public String getLasttime() {
		return lasttime;
	}




	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}




	




    




  

  

}
