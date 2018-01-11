package com.yayawan.callback;



public interface YYWLoginHandleCallback {

	/**
	 * 发送数据成功
	 * @param response
	 * @param temp
	 */
	 public abstract void onSuccess(String response,String temp);
	 
	 
	 /**
	  * 发送数据失败
	  * @param erro
	  * @param temp
	  */
	 public abstract void onFail(String erro,String temp);
}
