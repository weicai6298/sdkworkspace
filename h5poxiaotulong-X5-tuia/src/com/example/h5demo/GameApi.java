package com.example.h5demo;


import org.json.JSONException;
import org.json.JSONObject;

import com.kkgame.utils.Yayalog;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;
import android.app.Activity;
import android.webkit.JavascriptInterface;

public class GameApi {

	
	public Activity mActivity;
	
	public X5WebView mWebView;
	
	public GameApi(Activity mactivity,X5WebView webview){
		this.mActivity=mactivity;
		this.mWebView=webview;
	}
	
	@JavascriptInterface
	public void anmi() {
		Kgame.getInstance().anim(mActivity, new YYWAnimCallBack() {
			
			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	

	
	@JavascriptInterface
	public void login() {
		WebViewMainActivity.mHandler.sendEmptyMessage(1);
		Kgame.getInstance().login(mActivity, new YYWUserCallBack() {
			
			@Override
			public void onLogout(Object arg0) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.loginOut()");
			}
			
			@Override
			public void onLoginSuccess(YYWUser user, Object arg1) {
				System.out.println("登陆成功");
				JSONObject userjson = new JSONObject();
				try {
					
					userjson.put("uid", user.uid);
					userjson.put("username", user.userName);
					userjson.put("token", user.token);
				
					Yayalog.loger("Gameapi登陆成功："+userjson.toString());
					mWebView.loadUrl("javascript:GameCallBack.loginSuc("+userjson.toString()+")");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.loginFail()");
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.loginFail()");
			}
		});
	}
	
	
	@JavascriptInterface
	public void ceshi() {
	
	}
	
	@JavascriptInterface
	public void pay(String orderid,String goods,String money,String ext) {
	
		YYWOrder order = new YYWOrder(orderid, goods,
				Long.parseLong(money), ext);
		Kgame.getInstance().pay(mActivity, order, new YYWPayCallBack() {
			
			@Override
			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.paySuc()");
			}
			
			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.payFail()");
			}
			
			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.payFail()");
			}
		});
		
	}
	
	@JavascriptInterface
	public void exit() {
		Kgame.getInstance().exit(mActivity, new YYWExitCallback() {
			
			@Override
			public void onExit() {
				// TODO Auto-generated method stub
				mWebView.loadUrl("javascript:GameCallBack.exitSuc()");
			}
		});
	}
	
	
	@JavascriptInterface
	public void setdata( String roleId, String roleName,String roleLevel,String zoneId,String zoneName,String roleCTime,String ext) {
		Yayalog.loger("gameapisetdata:");
		Kgame.getInstance().setData(mActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime, ext);
	}
}
