package com.kkgame.sdk.bean;

import java.util.List;

public class ComentRespose {

	
	private List<ComentDiscuss> discusss ;

	private String success;

	private String time;

	public void setDiscuss(List<ComentDiscuss> discuss){
	this.discusss = discuss;
	}
	public List<ComentDiscuss> getDiscuss(){
	return this.discusss;
	}
	public void setSuccess(String success){
	this.success = success;
	}
	public String getSuccess(){
	return this.success;
	}
	public void setTime(String time){
	this.time = time;
	}
	public String getTime(){
	return this.time;
	}
}
