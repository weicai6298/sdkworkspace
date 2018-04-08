package com.kkgame.sdkmain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.callback.KgameSdkStartAnimationCallback;
import com.kkgame.sdk.callback.KgameSdkUpdateCallback;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.login.BaseLogin_Activity;
import com.kkgame.sdk.login.SmallHelpActivity;
import com.kkgame.sdk.login.Startlogin_dialog;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.utils.LogoWindow;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;

/**
 * sdk调用入口
 * @author zhangjiafan
 *
 */
public class KgameSdk {

	public static KgameSdkUserCallback mUserCallback; // 登录的回调

	public static KgameSdkPaymentCallback mPaymentCallback; // 支付回调

	public static KgameSdkStartAnimationCallback mStartAnimationCallback; // 开始动画回调

	public static KgameSdkCallback mCallback;

	public static KgameSdkUpdateCallback mUpdateCallback;

	public static Order mPayOrder; // 订单

	private static Intent intent;

	/**
	 * 动画的调用
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void animation(Activity paramActivity,
			KgameSdkStartAnimationCallback paramCallback) {
		mStartAnimationCallback = paramCallback;

		Yayalog.loger("kgameanim");
		mStartAnimationCallback.onSuccess();
		/*
		 * Intent intent = new Intent(paramActivity.getApplicationContext(),
		 * BaseLogin_Activity.class);
		 * 
		 * intent.putExtra("type", ViewConstants.STARTANIMATION);
		 * paramActivity.startActivityForResult(intent, 10200);
		 */

	}

	/**
	 * 登录接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void login(Activity paramActivity,
			KgameSdkUserCallback paramCallback) {

		Yayalog.loger("kgamesdklogin");
		mUserCallback = paramCallback;
		ViewConstants.mMainActivity = paramActivity;

		Startlogin_dialog startlogin_dialog = new Startlogin_dialog(
				paramActivity);

		startlogin_dialog.dialogShow();

	}

	/**
	 * demo登陸接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void loginDemo(Activity paramActivity,
			KgameSdkUserCallback paramCallback) {

	}

	/**
	 * 支付接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void payment(Activity paramActivity, Order paramOrder,
			Boolean issinglepay, KgameSdkPaymentCallback paramCallback) {

		Yayalog.loger("kgamesdk:payment");
		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mPaymentCallback = paramCallback;

		mPayOrder = paramOrder;
		AgentApp.mPayOrder = paramOrder;
		intent = new Intent(paramActivity, BaseLogin_Activity.class);
		intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
		paramActivity.startActivity(intent);

	}



	/**
	 * 个人中心
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void accountManager(Activity paramActivity) {
		if (AgentApp.mUser == null) {
			Toast.makeText(paramActivity.getApplicationContext(), "请先登录",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(paramActivity.getApplicationContext(),
				SmallHelpActivity.class);

		intent.putExtra("type", ViewConstants.ACCOUNTMANAGER);
		paramActivity.startActivityForResult(intent,10020);

	}

	/**
	 * 设置角色id
	 * 
	 * @param paramActivity
	 * @param roleId
	 * @param roleName
	 * @param roleLevel
	 * @param zoneId
	 * @param zoneName
	 */
	public static void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub

		Yayalog.loger("kgamesdksetRoleData");
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("token", AgentApp.mUser.token+"");
		requestParams.addBodyParameter("uid", AgentApp.mUser.uid+"");
		requestParams.addBodyParameter("role_id", roleId);
		requestParams.addBodyParameter("role_name",roleName);
		requestParams.addBodyParameter("role_level",roleLevel);
		requestParams.addBodyParameter("zone_id",zoneId);
		requestParams.addBodyParameter("zone_name", zoneName);
		
		httpUtils.send(HttpMethod.POST, ViewConstants.SETROLEDATAURL, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳遊戲數據成功");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	/**
	 * 初始化sdk
	 * 
	 * @param activity
	 */
	public static void initSdk(Activity activity) {

		// 工具类初始化，在支付安装插件时候用到
		ViewConstants.ISKGAME=true;
	}

	/**
	 * 开启悬浮窗
	 * 
	 * @param activity
	 */
	public static void init(Activity activity) {

		LogoWindow.getInstants(activity).start();

	}

	/**
	 * 关闭悬浮窗
	 * 
	 * @param activity
	 */
	public static void stop(Activity activity) {

		LogoWindow.getInstants(activity).Stop();

	}

	/**
	 * 更新
	 * 
	 * @param activity
	 */
	public static void update(Activity activity,
			KgameSdkUpdateCallback updateCallback) {
		mUpdateCallback = updateCallback;
//		 UpdateUtil.isUpdate(activity);

	}

	/**
	 * 注销账号
	 * 
	 * @param activity
	 */
	public static void logout(Activity activity) {
		Yayalog.loger("yayasdk注销");
		Sputils.putSPint("ischanageacount", 0, ViewConstants.mMainActivity);
		// KgameSdk.mUserCallback.onLogout();

	}

	/**
	 * 设置渠道id
	 * 
	 * @param activity
	 */
	public static void setSourceID(String sourceId) {

		AgentApp.mSourceId = sourceId;
	}

	public static String getSdkversion() {

		return ViewConstants.SDKVERSION;
	}

	/**
	 * 退出登录
	 * 
	 * @param activitiy
	 * @param onexit
	 */
	public static void Exitgame(Activity activitiy,
			final KgameSdkCallback onexit) {
		Dialog dialog = new AlertDialog.Builder(activitiy).setTitle("退出游戏提示")

				.setMessage("是否退出游戏？")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								onexit.onSuccess(null, which);
								
							}
						})
						.setNeutralButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								onexit.onCancel();
							}
						}). create();

				dialog.show();
	}
}
