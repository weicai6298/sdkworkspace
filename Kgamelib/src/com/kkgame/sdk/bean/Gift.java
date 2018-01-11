package com.kkgame.sdk.bean;

public class Gift {

	public String id;		//id
	public String name;		//礼包名字
	public String cdkey;	//礼包序列号
	public int is_success;		//状态码
	public String body;			//状态描述信息
	public String release_time;		//过期时间
	public String get_time;			//获取时间
	public Gift() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Gift(String id, String cdkey, int is_success, String release_time,
			String get_time) {
		super();
		this.id = id;
		this.cdkey = cdkey;
		this.is_success = is_success;
		this.release_time = release_time;
		this.get_time = get_time;
	}
	
	
	
	public Gift(String id, String name, String cdkey, String release_time,
			String get_time) {
		super();
		this.id = id;
		this.name = name;
		this.cdkey = cdkey;
		this.release_time = release_time;
		this.get_time = get_time;
	}
	@Override
	public String toString() {
		return "Gift [id=" + id + ", cdkey=" + cdkey + ", is_success="
				+ is_success + ", release_time=" + release_time + ", get_time="
				+ get_time + "]";
	}
	
	
}
