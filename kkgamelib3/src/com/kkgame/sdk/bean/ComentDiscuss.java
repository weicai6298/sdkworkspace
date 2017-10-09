package com.kkgame.sdk.bean;

public class ComentDiscuss {

	private String create_time;

	private String icon;

	private String id;

	private String ip;

	private String like;

	private String message;

	private String parent;

	private String user;

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreate_time() {
		return this.create_time;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setLike(String like) {
		this.like = like;
	}

	public String getLike() {
		return this.like;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParent() {
		return this.parent;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

	@Override
	public String toString() {
		return "ComentDiscuss [create_time=" + create_time + ", icon=" + icon
				+ ", id=" + id + ", ip=" + ip + ", like=" + like + ", message="
				+ message + ", parent=" + parent + ", user=" + user + "]";
	}
	
	
}
