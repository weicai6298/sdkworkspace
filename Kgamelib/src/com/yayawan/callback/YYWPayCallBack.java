package com.yayawan.callback;

import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;





public interface YYWPayCallBack {
    public abstract void onPaySuccess(YYWUser paramUser, YYWOrder paramOrder,
            Object paramObject);

    public abstract void onPayFailed(String paramString, Object paramObject);

    public abstract void onPayCancel(String paramString, Object paramObject);

}
