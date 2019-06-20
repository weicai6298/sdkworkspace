package com.yayawan.impl;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Yayalog;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWUser;
import com.yayawan.impl.qqhelper.QQUser;
import com.yayawan.impl.qqhelper.QqYsdkHelp;
import com.yayawan.impl.qqhelper.QqYsdkUserCallback;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWLoginer;


public class LoginImpl implements YYWLoginer {

	private Dialog dialog;
	private LinearLayout baselin;

	private Activity mActivity;
	private ProgressDialog mAutoLoginWaitingDlg;

	private LoginImpl mloimpl;

	@Override
	public void login(final Activity paramActivity,
			YYWUserCallBack userCallBack, String paramString) {

		mloimpl = this;
		Myconstants.dialog = dialog;

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			private BroadcastReceiver mReceiver;

			@Override
			public void run() {
				mActivity = paramActivity;
				
				
				QqYsdkHelp.Login(paramActivity, new QqYsdkUserCallback() {
					
					@Override
					public void onSuccess(QQUser paramUser, int paramInt) {
						// TODO Auto-generated method stub
						//YSDKCallback.loginSuce(paramActivity, Myconstants.openId, Myconstants.nickname, Myconstants.accessToken);
						if (DeviceUtil.getGameInfo(paramActivity, "NO_USERNAME").equals("yes")) {
							Log.i("tag", "NO_USERNAME-openid"+paramUser.getOpenid());
							Log.i("tag", "NO_USERNAME-token ="+paramUser.getAccessToken());
							loginSuce(paramActivity, paramUser.getOpenid(),  paramUser.getOpenid(),paramUser.getAccessToken());
						}else {
							Log.i("tag", "openid ="+paramUser.getOpenid());
							Log.i("tag", "name ="+paramUser.getNickName());
							Log.i("tag", "token ="+paramUser.getAccessToken());
							loginSuce(paramActivity, paramUser.getOpenid(),  paramUser.getNickName(),paramUser.getAccessToken());
						}
						
						Yayalog.loger("qq登陆成功"+paramUser.getOpenid());
					}
					
					@Override
					public void onError(int paramInt) {
						// TODO Auto-generated method stub
						loginFail();
						Log.i("tag","登录失败 = " +paramInt);
					}
				});
			/*	if (Sputils.getSPstring("logout", "no", mActivity).equals("yes")) {
					YSDKApi.logout();
					logIn(paramActivity);
					return;
				}
				
				if (!TextUtils.isEmpty(Myconstants.openId)) {
					YSDKCallback.loginSuce(paramActivity, Myconstants.openId, Myconstants.nickname, Myconstants.accessToken);
					
				}else if(getPlatform() == ePlatform.QQ){
					YSDKApi.queryUserInfo(ePlatform.QQ);
				}else if (getPlatform() == ePlatform.WX) {
					System.out.println("进来微信查询");
					YSDKApi.queryUserInfo(ePlatform.WX);
				}else{
					logIn(paramActivity);
				}*/
				

				// System.err.println("login sttart");

			}

		});

	}

	 // 获取当前登录平台
    public ePlatform getPlatform() {
        UserLoginRet ret = new UserLoginRet();
        YSDKApi.getLoginRecord(ret);
        if (ret.flag == eFlag.Succ) {
            return ePlatform.getEnum(ret.platform);
        }
        return ePlatform.None;
    }

	public void logIn(final Activity paramActivity) {
		createDialog(paramActivity);
		YYWMain.mUser=null;
		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Utilsjf.creDialogpro(paramActivity, "正在启动登陆...");
				
				YSDKApi.login(ePlatform.QQ);
				Logutils.sys("qq登陆的类型值"+ePlatform.QQ);
				Myconstants.platform="qq";
				Myconstants.mpayinfo.opentype = "qq";
				dialog.dismiss();
			}
		});

		weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Utilsjf.creDialogpro(paramActivity, "正在启动登陆...");
				// TODO Auto-generated method stub
				// System.out.println("我要微信登录了");
				YSDKApi.login(ePlatform.WX);
				Logutils.sys("微信登陆的类型值"+ePlatform.WX);
				Myconstants.platform="wx";
				Myconstants.mpayinfo.opentype = "wx";
				dialog.dismiss();
				
			}
		});

		dialog.show();
	}

	public void createDialog(Activity mContext) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mContext);
		machineFactory.MachineView(baselin, 400, 300, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, 400, 300, 0, "LinearLayout", 0,
				0, 0, 0, 100);
		ll_content.setOrientation(LinearLayout.VERTICAL);
		ll_content.setGravity(Gravity.CENTER_VERTICAL);
		ll_content.setBackgroundColor(Color.TRANSPARENT);

		qq = new ImageView(mContext);
		machineFactory.MachineView(qq, 360, 110, "LinearLayout");
		// qq.setImageResource(R.drawable.qq);
		qq.setImageBitmap(GetAssetsutils.getImageFromAssetsFile("qq.png",
				mContext));
		// qq.setGravity(Gravity.CENTER);
		qq.setClickable(true);

		weixin = new ImageView(mContext);

		machineFactory.MachineView(weixin, 360, 110, 0, "LinearLayout", 0, 60,
				0, 0, 100);
		// weixin.setImageResource(R.drawable.weixin);
		weixin.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"weixin.png", mContext));

		// weixin.setGravity(Gravity.CENTER);
		weixin.setClickable(true);

		ll_content.addView(qq);
		ll_content.addView(weixin);

		baselin.addView(ll_content);
		dialog.setContentView(baselin);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.6f; // 设置背景色对比度

		// lp.y = 60;

		dialogWindow.setAttributes(lp);

		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				Log.i("tag","登录失败1");
				// TODO Auto-generated method stub
				if (YYWMain.mUserCallBack != null) {
					YYWMain.mUserCallBack.onLoginFailed(null, null);

				}
			}
		});

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

	}

	@Override
	public void relogin(Activity paramActivity, YYWUserCallBack userCallBack,
			String paramString) {

		System.err.println("relogin");
	}


	ProgressDialog progressDialog = null;
	private ImageView qq;
	private ImageView weixin;

	private void progress(Activity paramActivity) {
		progressDialog = new ProgressDialog(paramActivity);
		// 设置进度条风格，风格为圆形，旋转的
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// 设置ProgressDialog 标题
		// progressDialog.setTitle("提示");
		// 设置ProgressDialog 提示信息
		progressDialog.setMessage("获取游戏数据");
		// 设置ProgressDialog 标题图标
		// progressDialog.setIcon(R.drawable.a);
		// 设置ProgressDialog 的进度条是否不明确
		progressDialog.setIndeterminate(true);
		// 设置ProgressDialog 是否可以按退回按键取消
		progressDialog.setCancelable(false);
		// 设置ProgressDialog 的一个Button
		// progressDialog.setButton("确定", new SureButtonListener());
		// 让ProgressDialog显示
		try {
			progressDialog.show();
		} catch (Exception e) {

		}
	}

	private void disprogress() {
		if (progressDialog != null) {
			if (progressDialog.isShowing())
				progressDialog.dismiss();
		}
	}
	
	/**
	 * 登录成功调用
	 * 
	 * @param mactivity
	 * @param uid
	 *            唯一id
	 * @param username
	 *            用户名..如果用户名为空.则拿uid作为用户名
	 * @param session
	 *            token验证码
	 */
	public static void loginSuce(Activity mactivity, String uid,
			String username, String session) {

		
		
		YYWMain.mUser = new YYWUser();
		if (TextUtils.isEmpty(uid)) {
			return;
		}
		YYWMain.mUser.uid = DeviceUtil.getAppid(mactivity) + "-" + uid + "";
		;
		
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getAppid(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getAppid(mactivity) + "-"
					+ uid + "";
		}

		// YYWMain.mUser.nick = data.getNickName();
		YYWMain.mUser.token = JSONUtil.formatToken(mactivity, session,
				YYWMain.mUser.userName);

		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
			Handle.login_handler(mactivity, YYWMain.mUser.uid,
					YYWMain.mUser.userName);
		}
	}

	/**
	 * 登录失败
	 */
	public static void loginFail() {
		if (YYWMain.mUserCallBack != null) {
			YYWMain.mUserCallBack.onLoginFailed("1232", "123");

		}
	}


}
