package com.yayawan.callback;

public interface YYWAnimCallBack {
    
    public abstract void onAnimSuccess(String paramString, Object paramObject);

    public abstract void onAnimFailed(String paramString, Object paramObject);

    public abstract void onAnimCancel(String paramString, Object paramObject);

}
