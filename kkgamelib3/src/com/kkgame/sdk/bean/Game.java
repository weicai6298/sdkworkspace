package com.kkgame.sdk.bean;

import java.io.Serializable;

public class Game implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;

	public String name;

	public String shortname;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getShortname() {
		return this.shortname;
	}

}
