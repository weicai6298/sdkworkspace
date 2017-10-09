package com.yayawan.impl;

import android.app.Activity;
import android.content.Intent;

import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.Handle;
import com.yayawan.proxy.YYWActivityStub;


public class ActivityStubImpl implements YYWActivityStub {

	
	
	public static Activity mactivity;

    public void applicationInit(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onCreate(Activity paramActivity) {
        // TODO Auto-generated method stub
    	GuangdiantongUtils.guangDiantongInit(paramActivity.getApplicationContext());
    	//广点通激活
		//GuangdiantongUtils.guangDiantongActi(paramActivity);
    	KgameSdk.initSdk(paramActivity);
    	Handle.active_handler(paramActivity);
    }

    public void onStop(Activity paramActivity) {
        // TODO Auto-generated method stub

    }

    public void onResume(Activity paramActivity) {
    	
    
    	
        KgameSdk.init(paramActivity);
    }

    public void onPause(Activity paramActivity) {
        KgameSdk.stop(paramActivity);
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
	
	 

}
