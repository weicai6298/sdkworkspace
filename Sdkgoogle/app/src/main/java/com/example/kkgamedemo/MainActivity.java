package com.example.kkgamedemo;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;

public class MainActivity extends AppCompatActivity {

	private TextView tv_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("oncreate");

		Kgame.getInstance().initSdk(this);
		Kgame.getInstance().onCreate(this);

		final LinearLayout mLinearLayout = new LinearLayout(this);

		mLinearLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setPadding(10, 10, 10, 10);

		Button animButton = new Button(this);
		animButton.setText("anim");
		animButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				anim(mLinearLayout);

			}
		});

		mLinearLayout.addView(animButton, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button updateButton = new Button(this);
		updateButton.setText("update");
		updateButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				upDate();
			}

		});

		Button lgoinButton = new Button(this);
		lgoinButton.setText("login");
		lgoinButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				login(mLinearLayout);

			}
		});
		mLinearLayout.addView(lgoinButton, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				pay(mLinearLayout);

			}
		});
		mLinearLayout.addView(payButton, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button extButton = new Button(this);
		extButton.setText("exit");
		extButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				exit(mLinearLayout);

			}
		});
		mLinearLayout.addView(extButton, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(mLinearLayout);

		Button versionButton = new Button(this);
		versionButton.setText("获取sdk版本号");
		versionButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				getversion(mLinearLayout);

			}

		});

		Button inintButton = new Button(this);
		inintButton.setText("无闪屏时的init接口");
		inintButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				initsdk(mLinearLayout);

			}

		});
		mLinearLayout.addView(versionButton, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button logoutButton = new Button(this);
		logoutButton.setText("注销账号");
		logoutButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Logout();

			}

		});
		mLinearLayout.addView(logoutButton, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		tv_view = new TextView(this);
		// logoutButton.setText("注销账号");
		mLinearLayout.addView(tv_view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		setContentView(mLinearLayout);

	}

	public void Demologin() {

	}

	/**
	 * 注销账号...使用场景,如果游戏内有注销账号的功能按钮,则点击按钮后,.调用此方法注销账号...
	 * 回调成功后在onlogout中进行回到登录页面的操作
	 */
	protected void Logout() {

		Kgame.getInstance().logout(null, new YYWUserCallBack() {

			public void onLoginSuccess(YYWUser paramUser, Object paramObject) {

			}

			public void onLoginFailed(String paramString, Object paramObject) {

			}

			public void onLogout(Object paramObject) {

			}

			public void onCancel() {

			}

		});
	}

	/**
	 * 更新接口
	 */
	public void upDate() {

	}

	public void anim(View v) {

		Kgame.getInstance().setData(this, "5533766", "奇葩啊婆", "2", "1", "test", "1551187439", "1");

		Kgame.getInstance().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {

				Toast.makeText(MainActivity.this, "播放动画回调", Toast.LENGTH_SHORT)
						.show();

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
		Kgame.getInstance().login(this, new YYWUserCallBack() {

			@Override
			public void onLogout(Object arg0) {
				Toast.makeText(MainActivity.this, "退出", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onLoginSuccess(YYWUser user, Object arg1) {
				System.out.println("登录成功" + user);
				Toast.makeText(MainActivity.this, "登录成功" + user,
						Toast.LENGTH_SHORT).show();
				// textxinx=user.toString()+"/n/r";
				// tv_view.setText(textxinx);
				// 登录成功后设置角色数据
//				Kgame.getInstance().setData(MainActivity.this, user.uid,
//						user.userName, "11", "1", "无尽之海",
//						System.currentTimeMillis() / 1000 + "", "1");
			}

			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				System.out.println("失败");
				Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onCancel() {
				System.out.println("取消");
				Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public void pay(View v) {

		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "com.bfgamegp.mjzdlacha1",
				600l, "9999,bf_1253687,4569788,2,99,com.bfgamegg.mjzdlacha1");

		Kgame.getInstance().pay(this, order, new YYWPayCallBack() {
			@Override
			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				Toast.makeText(MainActivity.this, "充值成功回调", Toast.LENGTH_SHORT)
						.show();
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

	/**
	 * 获取sdk版本号
	 * 
	 * @param mLinearLayout
	 */
	private void getversion(LinearLayout mLinearLayout) {

	}

	/**
	 * 退出接口
	 * 
	 * @param v
	 */
	public void exit(View v) {
		System.out.println("退出");

		Kgame.getInstance().exit(this, new YYWExitCallback() {

			@Override
			public void onExit() {
				Toast.makeText(MainActivity.this, "退出回调", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * 无法接闪屏时的init接口
	 * 
	 * @param mLinearLayout
	 */
	private void initsdk(LinearLayout mLinearLayout) {
		Kgame.getInstance().initSdk(this);
	}

	// 游戏中调出sdk小助手可选
	public void accountManage(View v) {
		Kgame.getInstance().manager(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		System.out.println("onrestart");
		Kgame.getInstance().onRestart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onresume");
		Kgame.getInstance().onResume(this);
		// Kgame.getInstance().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Kgame.getInstance().onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		Kgame.getInstance().onStop(this);
	}

	@Override
	protected void onDestroy() {
		Kgame.getInstance().onDestroy(this);
		super.onDestroy();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivityResult");
		// Yayalog.loger("demoonActivityResult");
		Kgame.getInstance().onActivityResult(this, requestCode, resultCode,
				data);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Kgame.getInstance().onNewIntent(intent);
	}
	@Override
	protected void onStart() {
		super.onStart();
		Kgame.getInstance().onStart(this);
	}

    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	Kgame.getInstance().onBackPressed();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Kgame.getInstance().attachBaseContext(newBase);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Kgame.getInstance().onConfigurationChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Kgame.getInstance().onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
