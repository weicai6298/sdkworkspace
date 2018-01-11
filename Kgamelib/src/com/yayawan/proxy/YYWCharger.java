package com.yayawan.proxy;

import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;

import android.app.Activity;
import android.content.Context;

public interface YYWCharger {

    public abstract void charge(Activity paramActivity, YYWOrder order,
            YYWPayCallBack callback);

    public abstract void pay(Activity paramActivity, YYWOrder order,
            YYWPayCallBack callback);
}
