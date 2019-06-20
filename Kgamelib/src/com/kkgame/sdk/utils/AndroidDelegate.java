package com.kkgame.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;


import com.kkgame.sdk.login.SmallHelpActivity;
import com.kkgame.utils.Yayalog;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiafan on 2018/6/13.
 */

public class AndroidDelegate {
   
    private SmallHelpActivity mActivity;
    public AndroidDelegate (SmallHelpActivity mactivity){
        mActivity=mactivity;
    }
   
    @JavascriptInterface
    public boolean GoToQQ(String qq){
        Yayalog.loger("qq号:"+qq+"...token:");

        if(isQQClientAvailable(mActivity)){
        	if (qq.equals("4000042115")) {
        		qq="938189213";
			}
			if (qq.equals("暂无")) {
				
			}else {
				 String url="mqqwpa://im/chat?chat_type=crm&uin="+qq+"&version=1&src_type=web&web_src=http:://wpa.b.qq.com";

				//String url="mqqwpa://im/chat?chat_type=wpa&uin="+qqhao;
				mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
        }else{
            Toast.makeText(mActivity,"请安装QQ客户端",Toast.LENGTH_SHORT).show();
        }
        //mActivity.toTakePhoto(uid,token);

        return  true;
    }
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
    //window.androidDelegate.ClickImg(this.url);
 

   

    /**
     * 获取SDCard的目录路径功能
     */
    private String getSDCardPath() {
        File sdcardDir = null;
        // 判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {
            sdcardDir = Environment.getExternalStorageDirectory();
        }
        return sdcardDir.toString();
    }
}
