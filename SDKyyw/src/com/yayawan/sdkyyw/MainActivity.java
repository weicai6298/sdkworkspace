package com.yayawan.sdkyyw;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Toast.makeText(this, "退出回调"+this.hasWindowFocus(),
		// Toast.LENGTH_SHORT).show();

		GameProxy.getInstent().onCreate(this);
		System.out.println("onstart");
		// setContentView(R.layout.sdk_activity_main);
		final LinearLayout mLinearLayout = new LinearLayout(this);

		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
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
		mLinearLayout.addView(animButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button lgoinButton = new Button(this);
		lgoinButton.setText("login");
		lgoinButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				login(mLinearLayout);

			}
		});
		mLinearLayout.addView(lgoinButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				pay(mLinearLayout);

			}
		});
		mLinearLayout.addView(payButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button extButton = new Button(this);
		extButton.setText("exit");
		extButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				exit(mLinearLayout);

			}
		});
		mLinearLayout.addView(extButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(mLinearLayout);

	}

	public void anim(View v) {
		System.out.println("登录");

		GameProxy.getInstent().anim(this, new YYWAnimCallBack() {

			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "播放动画回调", Toast.LENGTH_SHORT)
						.show();
			}

			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}

			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void login(View v) {
		System.out.println("登录");
		System.out.println("登录");
		YaYaWan.getInstance().login(this, new YYWUserCallBack() {

			public void onLogout(Object arg0) {
				Toast.makeText(MainActivity.this, "退出", Toast.LENGTH_SHORT)
						.show();

			}

			public void onLoginSuccess(YYWUser user, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("登录成功" + user);
				Toast.makeText(MainActivity.this, "登录成功" + user,
						Toast.LENGTH_SHORT).show();
				// 登录成功后设置角色数据

				YaYaWan.getInstance().setData(MainActivity.this, user.uid,
						user.userName, "11", "1", "无尽之海",
						System.currentTimeMillis() / 1000 + "", "1");
			}

			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("失败");
				Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT)
						.show();
			}

			public void onCancel() {
				// TODO Auto-generated method stub
				System.out.println("取消");
				Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT)
						.show();

			}

		});
		// GameProxy.getInstent().login(this, new YYWUserCallBack() {
		//
		// @Override
		// public void onLogout(Object arg0) {
		// System.out.println("登出");
		//
		// }
		//
		// @Override
		// public void onLoginSuccess(YYWUser user, Object arg1) {
		// // TODO Auto-generated method stub
		// System.out.println(user);
		// Toast.makeText(MainActivity.this, "登录成功" + user,
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void onLoginFailed(String arg0, Object arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onCancel() {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	public void pay(View v) {
		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "霜之哀伤",
				1l, "");
		GameProxy.getInstent().pay(this, order, new YYWPayCallBack() {

			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "充值成功回调", Toast.LENGTH_SHORT)
						.show();
			}

			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付失败");
			}

			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付退出");
			}
		});
	}

	public void exit(View v) {
		System.out.println("登录");
		GameProxy.getInstent().exit(this, new YYWExitCallback() {

			public void onExit() {
				Toast.makeText(MainActivity.this, "退出回调", Toast.LENGTH_SHORT)
						.show();
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
		System.out.println("onresume");
		// Toast.makeText(MainActivity.this, "退出回调"+this.hasWindowFocus(),
		// Toast.LENGTH_SHORT).show();

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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		GameProxy.getInstent().onActivityResult(this, requestCode, resultCode, data);
	}

}