package com.yayawan.sdktemplate.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;

public class WXEntryActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        // 检查下intent有没有异常的内容，发生异常直接关掉组件，不继续执行
        // 为了解决android的“安卓通用型拒绝服务漏洞”
        try {
        	Matrix.wxLoginCallback(getIntent());
            intent.getStringExtra("try");
            intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_HANDEL_WEIXIN_CALLBACK);
            Matrix.execute(this, intent, null);
        } catch (Throwable tr) {
            finish();
            return;
        }
        finish();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	try {
    		//接入微信登录相关调用
    		Matrix.wxLoginCallback(getIntent());
		} catch (Exception e) {
		}
    	finish();
    }

}
