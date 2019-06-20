package com.yayawan.proxy;

import com.yayawan.callback.YYWUserCallBack;

import android.app.Activity;

public interface YYWLoginer {

    public abstract void login(Activity paramActivity,
            YYWUserCallBack userCallBack, String paramString);

    public abstract void relogin(Activity paramActivity,
            YYWUserCallBack userCallBack, String paramString);

}
