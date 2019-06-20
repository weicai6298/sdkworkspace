package com.yayawan.proxy;

import android.app.Activity;
import android.content.Intent;

public interface YYWActivityStub {

    public abstract void applicationInit(Activity paramActivity);

    public abstract void onCreate(Activity paramActivity);
    
    public abstract void onStart(Activity paramActivity);

    public abstract void onStop(Activity paramActivity);

    public abstract void onResume(Activity paramActivity);

    public abstract void onPause(Activity paramActivity);

    public abstract void onRestart(Activity paramActivity);

    public abstract void onDestroy(Activity paramActivity);

    public abstract void applicationDestroy(Activity paramActivity);

    public abstract void onActivityResult(Activity paramActivity, int paramInt1, int paramInt2, Intent paramIntent);

    public abstract void onNewIntent(Intent paramIntent);
    public abstract void initSdk(Activity paramActivity);
}
