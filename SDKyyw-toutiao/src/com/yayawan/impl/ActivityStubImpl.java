package com.yayawan.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.ss.android.common.applog.TeaAgent;
import com.ss.android.common.applog.TeaConfigBuilder;
import com.yayawan.proxy.YYWActivityStub;


public class ActivityStubImpl implements YYWActivityStub {

	
	
	public static Activity mactivity;

    public void applicationInit(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onCreate(Activity paramActivity) {
        // TODO Auto-generated method stub
    	KgameSdk.initSdk(paramActivity);
    	Handle.active_handler(paramActivity);
    	//头条
    	TeaAgent.init(TeaConfigBuilder.create(paramActivity)
    			.setAppName(DeviceUtil.getGameInfo(paramActivity, "appname"))
    			.setChannel(DeviceUtil.getGameInfo(paramActivity, "Channel"))
    			.setAid(Integer.parseInt(DeviceUtil.getGameInfo(paramActivity, "aid")))
    			.createTeaConfig());
    }

    public void onStop(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onResume(Activity paramActivity) {
    	
        KgameSdk.init(paramActivity);
        closeAndroidPDialog();
        
        //头条
        TeaAgent.onResume(paramActivity);
    }

    public void onPause(Activity paramActivity) {
        KgameSdk.stop(paramActivity);
        
      //头条
        TeaAgent.onPause(paramActivity);
    }

    public void onRestart(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void applicationDestroy(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onActivityResult(Activity paramActivity, int paramInt1,
            int paramInt2, Intent paramIntent) {
        // TODO Auto-generated method stub

    }

    public void onNewIntent(Intent paramIntent) {
        // TODO Auto-generated method stub

    }

	public void initSdk(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStart(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchActivityOnCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchActivityonOnNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfigurationChanged() {
		// TODO Auto-generated method stub
		
	}

}
