package com.kkgame.sdk.callback;

import com.kkgame.sdk.bean.User;



/**
 * 注销账号操作的回调接口
 * @author wjy
 *
 */
public interface KgameSdkCallback {
    /*
     * 注销账号成功
     * @paramUser 登录成功后服务器返回的用户信息  
     */
    public abstract void onSuccess(User paramUser, int paramInt);;
    /*
     * 登录失败时的回调
     */
    public abstract void onError(int paramInt);
    /*
     * 取消登录时的回调
     */
    public abstract void onCancel();
    /*
     * 注销时的回调
     */
    public abstract void onLogout();
    
    
}
