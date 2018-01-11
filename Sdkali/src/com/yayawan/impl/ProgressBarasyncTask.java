package com.yayawan.impl;

import android.os.AsyncTask;

@SuppressWarnings("rawtypes")
public abstract class ProgressBarasyncTask extends AsyncTask{
	private String uid;
	private String token;
	private String orderid;

	public ProgressBarasyncTask(String uid, String token, String orderid) {
		this.uid = uid;
		this.token = token;
		this.orderid = orderid;
	}
	//该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
    protected Integer doInBackground(Integer... params) {
		for (int i = 0; i < 6; i++) {
			YaYawanconstants.HttpPost(uid, token, orderid);
		}
		return null;
	}
	//该方法运行在Ui线程内，可以对UI线程内的控件设置和修改其属性
	@Override
    protected void onPreExecute() {
//        tv.setText("开始执行异步操作！");
    }
	
	 //在doInBackground方法当中，每次调用publishProgrogress()方法之后，都会触发该方法
    protected void onProgressUpdate(Integer... values) {
    }
    //在doInBackground方法执行结束后再运行，并且运行在UI线程当中
    protected void onPostExecute(String result) {
//        tv.setText("异步操作执行结束"+result);
    }
    
//  @Override
//  protected void onCancelled() {
//      
//  }
}
