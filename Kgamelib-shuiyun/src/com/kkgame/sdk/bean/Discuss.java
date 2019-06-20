package com.kkgame.sdk.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Discuss implements Serializable {
	private String at;

	private String count_c;

	private String create_time;

	private Game game;

	private String icon;

	private String id;

	private String img;

	// private String ip;

	private String like;

	private String message;

	private String tid;

	private String user;

	private String islike;

	private String parent;

	public void setAt(String at) {
		this.at = at;
	}

	public String getAt() {
		return this.at;
	}

	public void setCount_c(String count_c) {
		this.count_c = count_c;
	}

	public String getCount_c() {
		return this.count_c;
	}

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

	public void setImg(String img) {
		this.img = img;
	}

	public String getImg() {
		return this.img;
	}

	/*
	 * public void setIp(String ip){ this.ip = ip; } public String getIp(){
	 * return this.ip; }
	 */
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

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTid() {
		return this.tid;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return this.user;
	}

	@Override
	public String toString() {
		return "Discuss [at=" + at + ", count_c=" + count_c + ", create_time="
				+ create_time + ", game=" + game + ", icon=" + icon + ", id="
				+ id + ", img=" + img + ", ip=" + ", like=" + like
				+ ", message=" + message + ", tid=" + tid + ", user=" + user
				+ "]";
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getIslike() {
		return islike;
	}

	public void setIslike(String islike) {
		this.islike = islike;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}
