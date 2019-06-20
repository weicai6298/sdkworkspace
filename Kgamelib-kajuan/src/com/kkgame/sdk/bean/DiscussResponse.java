package com.kkgame.sdk.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiscussResponse implements Serializable {

	private List<Discuss> discuss ;

	private String maxtime;

	private String mintime;

	private String success;

	private String time;


	public List<Discuss> getDiscuss() {
		return discuss;
	}
	public void setDiscuss(List<Discuss> discuss) {
		this.discuss = discuss;
	}
	public void setMaxtime(String maxtime){
	this.maxtime = maxtime;
	}
	public String getMaxtime(){
	return this.maxtime;
	}
	public void setMintime(String mintime){
	this.mintime = mintime;
	}
	public String getMintime(){
	return this.mintime;
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
	@Override
	public String toString() {
		return "DiscussResponse [discuss=" + discuss + ", maxtime=" + maxtime
				+ ", mintime=" + mintime + ", success=" + success + ", time="
				+ time + "]";
	}
	

}
