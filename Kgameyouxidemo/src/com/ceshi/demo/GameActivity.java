package com.ceshi.demo;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kkgamedemo.R;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.Kgame;

/**
 * 接入只需九步哦...复制粘贴几步搞点
 * 1.sdk初始化方法。
 * 
 * Kgame.getInstance().initSdk(mActivity);
 * 
 * 2.复写生命周期方法,里面调用
 * Kgame.getInstance().onCreate(this);
 * Kgame.getInstance().onRestart(this);
 * Kgame.getInstance().onResume(this);
 * Kgame.getInstance().onPause(this);
 * Kgame.getInstance().onStop(this);
 * Kgame.getInstance().onDestroy(this);
 * Kgame.getInstance().onActivityResult(this, requestCode, resultCode, data);
 * Kgame.getInstance().onNewIntent(intent);
 * 
 * 3.闪屏动画,在游戏启动时的第一个页面出现..只要保证游戏画面出来前调用即可
 * Kgame.getInstance().anim(this, new YYWAnimCallBack(){})
 * 注意:闪屏回调有三种情况..不管闪屏失败还是成功.都应该进入游戏画面..也就是说,不管闪屏失败还是成功.游戏继续进行
 * 
 * 4.登录接口.
 * Kgame.getInstance().login(this, new YYWUserCallBack()} 
 * 如下面的例子:
 * 注意:登录有三个回调..第一个成功,第二个取消...注意第三个是登出..登出则是用户点击了sdk中的切换账号按钮,游戏应该注销账户
 * 回到游戏的登录页面..
 * 
 * 5.数据上报接口
 * Kgame.getInstance().setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext) 
 * 注意:在角色登录成功后调用,上报角色信息
 * 
 * 6.支付接口
 * Kgame.getInstance().pay(this, order, new YYWPayCallBack())
 * 看例子:
 * 
 * 7.退出接口
 * Kgame.getInstance().exit(this, new YYWExitCallback() )
 * 
 *
 * 
 * 8.游戏内注销按钮...(可选)应用场景,游戏内有注销账号的按钮..点击按钮后游戏自己退出游戏，在退出之前调用这个接口（这个接口无任何现象，仅仅是将sdk内账号清空）
 * Kgame.getInstance().logout(GameActivity.this);
 * 
 * 
 * 9.复制拷贝androidmanifest文件配置
 * 
 * @author zjf
 *
 */
public class GameActivity extends Activity {

	public Button animview, loginview, payview, exitview,logoutview;

	public LinearLayout llbackground;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demomain);
		yaya_init();
		Kgame.getInstance().onCreate(this);
		
		initView();
		anim();

	}

	private void initView() {
		// TODO Auto-generated method stub
		animview = (Button) findViewById(R.id.amin);
		loginview = (Button) findViewById(R.id.login);
		payview = (Button) findViewById(R.id.pay);
		exitview = (Button) findViewById(R.id.exit);
		llbackground = (LinearLayout) findViewById(R.id.background);
		logoutview= (Button) findViewById(R.id.logout);
		animview.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				anim();
			}
		});

		loginview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login();
			}
		});

		payview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pay();
			}
		});
		exitview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exit();
			}
		});
		
		logoutview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Kgame.getInstance().logout(GameActivity.this);
				loginView();
			}
		});

	}

	/**
	 * 开始游戏
	 */
	public void loginView() {

		llbackground.setBackgroundResource(R.drawable.gamelogin);
		animview.setVisibility(View.VISIBLE);
		loginview.setVisibility(View.VISIBLE);
		payview.setVisibility(View.GONE);
		exitview.setVisibility(View.GONE);
		logoutview.setVisibility(View.GONE);
	}

	/**
	 * 游戏画面
	 */
	public void gameView() {
		llbackground.setBackgroundResource(R.drawable.gameplay);

		animview.setVisibility(View.GONE);
		loginview.setVisibility(View.GONE);
		payview.setVisibility(View.VISIBLE);
		exitview.setVisibility(View.VISIBLE);
		logoutview.setVisibility(View.VISIBLE);
	}

	/**
	 * 闪屏
	 */
	public void anim() {
		System.out.println("闪屏");

		Kgame.getInstance().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(GameActivity.this, "播放动画回调", Toast.LENGTH_SHORT)
						.show();
				loginView();
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

	/**
	 * 登陆
	 */
	public void login() {
		System.out.println("登录");
		Kgame.getInstance().login(this, new YYWUserCallBack() {

			@Override
			public void onLogout(Object arg0) {
				Toast.makeText(GameActivity.this, "账号退出，回到登陆页面",
						Toast.LENGTH_SHORT).show();
				loginView();
			}

			@Override
			public void onLoginSuccess(YYWUser user, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("登录成功" + user);
				Toast.makeText(GameActivity.this, "登录成功" + user,
						Toast.LENGTH_SHORT).show();
				String uid=user.uid;
				String username=user.userName;
				String token=user.token;
						
				// 登录成功后设置角色数据
				Kgame.getInstance().setRoleData(GameActivity.this, user.uid,
						user.userName, "11", "1", "无尽之海");

				// 登录成功设置角色数据方法

				// Kgame.getInstance().setData(paramActivity, roleId,
				// roleName, roleLevel, zoneId, zoneName, roleCTime, ext);
				// 参数介绍 角色id 角色name 等级 区服id 区服名字 角色创建时间 附加信息
				// 角色创建时间为秒 距离1970年的秒数，其中java可以用这个方法获取 long
				// startPaintLogoTime=System.currentTimeMillis()/1000;
				Kgame.getInstance().setData(GameActivity.this, user.uid,
						user.userName, "11", "1", "无尽之海",
						"" + System.currentTimeMillis() / 1000, "1");
				// 进入到游戏中
				gameView();
			}

			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("失败");
				Toast.makeText(GameActivity.this, "失败", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				System.out.println("取消");
				Toast.makeText(GameActivity.this, "取消", Toast.LENGTH_SHORT)
						.show();

			}

		});
	}

	/**
	 * 支付
	 */
	public void pay() {

		//订单三个参数，第一个 订单号，第二个商品名字，第三个金额，单位分。第四个参数，附加参数，回传给cp
		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "霜之哀伤", 50l,
				"etc");
		

		Kgame.getInstance().pay(this, order, new YYWPayCallBack() {
			@Override
			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(GameActivity.this, "充值成功回调", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付失败");
			}

			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		// YYWMain.mUserCallBack.onLogout(null);
		// Toast.makeText(this, Kgame.getSdkversion(), 0).show();
	}

	/**
	 * 退出接口
	 */
	public void exit() {
		System.out.println("登录");
		Kgame.getInstance().exit(this, new YYWExitCallback() {

			@Override
			public void onExit() {
				Toast.makeText(GameActivity.this, "退出回调", Toast.LENGTH_SHORT)
						.show();
				
				//这里执行退出游戏的操作，比如关闭游戏
				finish();
			}
		});
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
		super.onDestroy();
		Kgame.getInstance().onDestroy(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Kgame.getInstance().onActivityResult(this, requestCode, resultCode,
				data);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Kgame.getInstance().onNewIntent(intent);
	}

	public  void yaya_init() {
		 Kgame.getInstance().initSdk(GameActivity.this);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Kgame.getInstance().exit(GameActivity.this, new YYWExitCallback() {

				@Override
				public void onExit() {
					Toast.makeText(GameActivity.this, "退出回调", Toast.LENGTH_SHORT)
							.show();
					finish();
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}
}
