package com.kkgame.sdk.callback;

/**
 * 调用动画回调
 * @author wjy
 *
 */
public interface KgameSdkStartAnimationCallback {
    /*
     *动画调用成功 
     */
    public abstract void onSuccess();
    /*
     * 动画调用失败
     */
    public abstract void onError();
    /*
     * 动画调用退出
     */
    public abstract void onCancel();
}
