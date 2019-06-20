package com.kkgame.sdk.bean;

import java.io.Serializable;

public class UploadPhoto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bucket;
	private String success;
	
	private String uid;
	private String filepath;
	
	private String filename;
	private String filesize;
	private String time;
	private String filetype;
	private String token;
	private String url;
	public String getBucket() {
		return bucket;
	}
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "UploadPhoto [bucket=" + bucket + ", success=" + success
				+ ", uid=" + uid + ", filepath=" + filepath + ", filename="
				+ filename + ", filesize=" + filesize + ", time=" + time
				+ ", filetype=" + filetype + ", token=" + token + ", url="
				+ url + "]";
	}
	
	
	
}
