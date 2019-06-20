package com.example.h5demo;


import java.util.UUID;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YaYaWan;
import com.yayawan.proxy.GameProxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class BrowserTempActivity extends Activity {

	private Activity paramActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		paramActivity=this;
		GameProxy.getInstent().onCreate(this);
		//setContentView(R.layout.sdk_activity_main);

		final LinearLayout mLinearLayout = new LinearLayout(this);

		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setPadding(10, 10, 10, 10);

		Button animButton = new Button(this);
		animButton.setText("anim");
		animButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				anim(mLinearLayout);

			}
		});
		mLinearLayout.addView(animButton, new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


		Button lgoinButton = new Button(this);
		lgoinButton.setText("login");
		lgoinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login(mLinearLayout);

			}
		});
		mLinearLayout.addView(lgoinButton, new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pay(mLinearLayout);

			}
		});
		mLinearLayout.addView(payButton, new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


		Button extButton = new Button(this);
		extButton.setText("exit");
		extButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit(mLinearLayout);

			}
		});
		mLinearLayout.addView(extButton, new LinearLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(mLinearLayout);


	}

	public void anim(View v) {
        System.out.println("登录");
        GameProxy.getInstent().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				Toast.makeText(BrowserTempActivity.this, "播放动画回调", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAnimFailed(String arg0, Object arg1) {

			}

			@Override
			public void onAnimCancel(String arg0, Object arg1) {

			}
		});
    }

	public void login(View v) {
        System.out.println("登录");
        GameProxy.getInstent().login(this, new YYWUserCallBack() {

            @Override
            public void onLogout(Object arg0) {
                System.out.println("登出");

            }

            @Override
            public void onLoginSuccess(YYWUser user, Object arg1) {
                System.out.println(user);
                Toast.makeText(BrowserTempActivity.this, "登录回调" + user, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginFailed(String arg0, Object arg1) {

            }

			@Override
			public void onCancel() {
				
			}
        });
    }

	 public void pay(View v) {
	        YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "霜之哀伤", 600l,  "xxxx");
	       
	        GameProxy.getInstent().pay(this, order, new YYWPayCallBack() {

	            @Override
	            public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
	            	Toast.makeText(BrowserTempActivity.this, "充值成功回调", Toast.LENGTH_SHORT).show();
	            }

	            @Override
	            public void onPayFailed(String arg0, Object arg1) {
	                System.out.println("支付失败");
	            }

	            @Override
	            public void onPayCancel(String arg0, Object arg1) {
	                System.out.println("支付退出");
	            }
	        });
	 }



	 public void exit(View v) {
	        System.out.println("登录");
	        GameProxy.getInstent().exit(this, new YYWExitCallback() {

				@Override
				public void onExit() {
					Toast.makeText(BrowserTempActivity.this, "退出回调", Toast.LENGTH_SHORT).show();
				}
			});
	  }

	 public void accountManage(View v) {
	        GameProxy.getInstent().manager(this);
	 }

	 @Override
	    protected void onRestart() {
	        super.onRestart();
	        GameProxy.getInstent().onRestart(this);
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();
	        GameProxy.getInstent().onResume(this);
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        GameProxy.getInstent().onPause(this);
	    }

	    @Override
	    protected void onStop() {
	        super.onStop();
	        GameProxy.getInstent().onStop(this);
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        GameProxy.getInstent().onDestroy(this);
	    }



	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	super.onActivityResult(requestCode, resultCode, data);
	    	GameProxy.getInstent().onActivityResult(this, requestCode, resultCode, data);
	    }

	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	// 如果是返回键,直接返回到桌面
	    	if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
	              YaYaWan.getInstance().exit(paramActivity, new YYWExitCallback() {
					
					@Override
					public void onExit() {
						paramActivity.finish();
					}
				});
	    	}
	     
	    	return super.onKeyDown(keyCode, event);
	    }
}