package com.yayawan.proxy;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;

import android.app.Activity;
import android.content.Intent;

public abstract interface YYWGameProxy {

    /**
     * 登录
     *
     * @param paramActivity
     * @param userCallBack
     */
    public abstract void login(Activity paramActivity, YYWUserCallBack userCallBack);
    /**
     * 登出
     *
     * @param paramActivity
     * @param userCallBack
     */
    public abstract void logout(Activity paramActivity, YYWUserCallBack userCallBack);

    /**
     * 不定额支付
     *
     * @param paramActivity
     * @param order
     *            不需要传钱数
     * @param userCallBack
     */
    public abstract void charge(Activity paramActivity, YYWOrder order, YYWPayCallBack userCallBack);

    /**
     * 定额支付
     *
     * @param paramActivity
     * @param order
     * @param userCallBack
     */
    public abstract void pay(Activity paramActivity, YYWOrder order, YYWPayCallBack userCallBack);

    /**
     * 用户管理
     *
     * @param paramActivity
     */
    public abstract void manager(Activity paramActivity);

    /**
     * 初始化sdk
     *
     * @param paramActivity
     */
    public abstract void initSdk(Activity paramActivity);

    
    /**
     * 动画播放
     *
     * @param paramActivity
     */
    public abstract void anim(Activity paramActivity,  YYWAnimCallBack animCallBack);

    /**
     * 退出游戏
     *
     * @param paramActivity
     * @param exitCallBack
     */
    public abstract void exit(Activity paramActivity, YYWExitCallback exitCallBack);

    public abstract void setRoleData(Activity paramActivity, String roleId, String roleName, String roleLevel, String zoneId, String zoneName);

    
    
    
    
    public abstract void applicationInit(Activity paramActivity);

    public abstract void onCreate(Activity paramActivity);

    public abstract void onStop(Activity paramActivity);

    public abstract void onResume(Activity paramActivity);

    public abstract void onPause(Activity paramActivity);

    public abstract void onRestart(Activity paramActivity);

    public abstract void onDestroy(Activity paramActivity);

    public abstract void applicationDestroy(Activity paramActivity);

    public abstract void onActivityResult(Activity paramActivity, int paramInt1, int paramInt2, Intent paramIntent);

    public abstract void onNewIntent(Intent paramIntent);
}
