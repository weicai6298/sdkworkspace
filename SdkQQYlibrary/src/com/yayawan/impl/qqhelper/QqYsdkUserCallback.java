package com.yayawan.impl.qqhelper;

public interface QqYsdkUserCallback {

	 /*
     * 登录成功时的回调
     * @paramUser 登录成功后服务器返回的用户信息  
     */
    public abstract void onSuccess(QQUser paramUser, int paramInt);;
    /*
     * 登录失败时的回调
     */
    public abstract void onError(int paramInt);

}
