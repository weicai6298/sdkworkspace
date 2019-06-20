package com.yayawan.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Handle;
import com.yayawan.proxy.YYWActivityStub;


public class ActivityStubImpl implements YYWActivityStub {

	
	
	public static Activity mactivity;

    public void applicationInit(Activity paramActivity) {

    }

    public void onCreate(Activity paramActivity) {
    	KgameSdk.initSdk(paramActivity);
    	Handle.active_handler(paramActivity);
    }

    public void onStop(Activity paramActivity) {

    }

    public void onResume(Activity paramActivity) {
        KgameSdk.init(paramActivity);
        closeAndroidPDialog();
    }

    public void onPause(Activity paramActivity) {
        KgameSdk.stop(paramActivity);
    }

    public void onRestart(Activity paramActivity) {

    }

    public void onDestroy(Activity paramActivity) {

    }

    public void applicationDestroy(Activity paramActivity) {

    }

    public void onActivityResult(Activity paramActivity, int paramInt1,
            int paramInt2, Intent paramIntent) {

    }

    public void onNewIntent(Intent paramIntent) {

    }

	public void initSdk(Activity arg0) {
		
	}

	public void onStart(Activity arg0) {
		
	}

	@Override
	public void launchActivityOnCreate(Activity paramActivity) {
		
	}

	@Override
	public void launchActivityonOnNewIntent(Intent paramIntent) {
		
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		
	}
	
	 
	private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void attachBaseContext(Context arg0) {
		
	}

	@Override
	public void onBackPressed() {
		
	}

	@Override
	public void onConfigurationChanged() {
		
	}

}
