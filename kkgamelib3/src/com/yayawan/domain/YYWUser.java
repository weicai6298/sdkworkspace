package com.yayawan.domain;


/**
 * 保存用户登录后的用户信息
 *
 * @author wjy
 *
 */
public class YYWUser {
    // 用户名
    public String userName;

    // 用户id
    public String uid;

    // 登录成功后的授权信息
    public String token;
    
    // 登陆成功丫丫玩token
    public String yywtoken;
    
    // 登陆成功丫丫玩uid
    public String yywuid;
    
    // 登陆成功丫丫玩username
    public String yywusername;


    // 登陆状态
    public int success;

    // 登陆返回状态信息
    public String body;

    // 账号余额
    public String money;

    public String password;

    public String lasttime;

    public String icon;

    public String nick;

    public int phoneActive = -1;

    public YYWUser() {
    }

    public YYWUser(String userName, String uid, String token, int success, String body, String money) {
        super();
        this.userName = userName;
        this.uid = uid;
        this.token = token;
        this.success = success;
        this.body = body;
        this.money = money;
    }

    public YYWUser(int success, String body) {
        super();
        this.success = success;
        this.body = body;
    }

	@Override
	public String toString() {
		return "YYWUser [userName=" + userName + ", uid=" + uid + ", token="
				+ token + ", yywtoken=" + yywtoken + ", yywuid=" + yywuid
				+ ", yywusername=" + yywusername + ", success=" + success
				+ ", body=" + body + ", money=" + money + ", password="
				+ password + ", lasttime=" + lasttime + ", icon=" + icon
				+ ", nick=" + nick + ", phoneActive=" + phoneActive + "]";
	}

   

}
