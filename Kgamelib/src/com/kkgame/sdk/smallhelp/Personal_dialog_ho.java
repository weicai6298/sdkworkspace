package com.kkgame.sdk.smallhelp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent.OnFinished;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kkgame.sdk.utils.Basedialogview;
import com.kkgame.sdk.utils.Myviewpage;
import com.kkgame.sdk.xml.GetAssetsutils;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.sdk.xml.SmallHelp_xml;
import com.kkgame.sdk.xml.Weibologinxml_po;
import com.kkgame.sdk.xml.Yinlianpay_xml_po;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Yayalog;



/**
 * 个人中心主页面
 * @author zjf
 *
 */
public class Personal_dialog_ho extends Basedialogview{


	private ArrayList<BaseContentView> views;
	

	
	public Personal_dialog_ho(Activity activity) {
		super(activity);
	}

	RelativeLayout rl_mLoading;
	WebView wv_mWeiboview;
	
	@Override
	public void createDialog(final Activity mActivity) {
		Weibologinxml_po yinlianpay_xml_po = new Weibologinxml_po(mActivity);
		mActivity.setContentView(yinlianpay_xml_po.initViewxml());
		rl_mLoading = yinlianpay_xml_po.getRl_mLoading();
		wv_mWeiboview = yinlianpay_xml_po.getWv_mWeiboview();
		logic(mActivity);
	}
	
	@Override
	public void logic(Activity mctivity) {
		// TODO Auto-generated method stub
		super.logic(mActivity);
		//https://api.sdk.75757.com/web/profile/?uid=xxxxx&token=xxxxx&appid=xxxx
		String uid=AgentApp.mUser.uid+"";
		String token=AgentApp.mUser.token;
		String appid=DeviceUtil.getAppid(mctivity);
		String url="https://api.sdk.75757.com/web/profile/?uid="+uid+"&token="+token+"&appid="+appid;
		wv_mWeiboview.loadUrl(url);
		Yayalog.loger("url = "+url);
		Log.i("tag","url = " +url);
		//rl_mLoading.setVisibility(View.GONE);
		Yayalog.loger(url);
		wv_mWeiboview.setWebViewClient(new WebViewClient(){
					
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				System.out.println("加载成功了");
				//rl_mLoading.setVisibility(View.GONE);
			}
			
		});
	}

	public void onResume() {
		// TODO Auto-generated method stub
		
	}
	
@Override
public void dialogDismiss() {
	// TODO Auto-generated method stub
	super.dialogDismiss();
	
}

	
	
}
