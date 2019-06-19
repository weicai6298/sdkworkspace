package com.kkgame.sdkmain;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kkgame.sdk.bean.Order;
import com.kkgame.sdk.callback.ExitdialogCallBack;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdk.callback.KgameSdkPaymentCallback;
import com.kkgame.sdk.callback.KgameSdkStartAnimationCallback;
import com.kkgame.sdk.callback.KgameSdkUpdateCallback;
import com.kkgame.sdk.callback.KgameSdkUserCallback;
import com.kkgame.sdk.login.BaseLogin_Activity;
import com.kkgame.sdk.login.Exit_dialog;
import com.kkgame.sdk.login.SmallHelpActivity;
import com.kkgame.sdk.login.Startlogin_dialog;
import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.pay.XiaoMipayActivity;
import com.kkgame.sdk.utils.LogoWindow;
import com.kkgame.sdk.xml.MachineFactory;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.PermissionUtils;
import com.kkgame.utils.PermissionUtils.PermissionCheckCallBack;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.SuperDialog;
import com.kkgame.utils.SuperDialog.onDialogClickListener;
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
	
	
	public static int  sdktype=0;

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
		
		
		String gameInfo = DeviceUtil.getGameInfo(paramActivity, "sdktype");
		if (gameInfo.equals("1")) {
			paramCallback.onSuccess();
		}else {
			Intent intent = new Intent(paramActivity.getApplicationContext(),
			BaseLogin_Activity.class);
			intent.putExtra("type", ViewConstants.STARTANIMATION);
		    paramActivity.startActivityForResult(intent, 10200);
		}
		

	}

	/**
	 * 登录接口
	 * 
	 * @param paramActivity
	 * @param paramCallback
	 */
	public static void login(final Activity paramActivity,
			KgameSdkUserCallback paramCallback) {

		
		if (!(PermissionUtils.checkPermission(paramActivity, Manifest.permission.READ_EXTERNAL_STORAGE)&&PermissionUtils.checkPermission(paramActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
			SuperDialog superDialog = new SuperDialog(paramActivity);
			superDialog.setTitle("亲爱的玩家");
			superDialog.setContent("密码将永久加密保存在sd中\r\n请授予sd卡读写权限").setListener(new onDialogClickListener() {
				
				@Override
				public void click(boolean isButtonClick, int position) {
					// TODO Auto-generated method stub
					Yayalog.loger("请求权限对话框按钮按下");
					PermissionUtils.checkMorePermissions(paramActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},new PermissionCheckCallBack() {
						
						@Override
						public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
							// TODO Auto-generated method stub
							// 用户之前已拒绝过权限申请
							//
							PermissionUtils.requestMorePermissions(paramActivity, permission, PermissionUtils.READ_EXTERNAL_STORAGE);
						}
						
						@Override
						public void onUserHasAlreadyTurnedDown(String... permission) {
							// TODO Auto-generated method stub
							// 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
							PermissionUtils.toAppSetting(paramActivity);
						}
						
						@Override
						public void onHasPermission() {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}).show();
		}
		
		
		
		
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
			Toast.makeText(paramActivity.getApplicationContext(), "請先登錄",
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
			Toast.makeText(paramActivity.getApplicationContext(), "請先登錄",
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

		Yayalog.loger("kgamesdksetRoleData"+AgentApp.mUser.token+"--"+AgentApp.mUser.uid);
		
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
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳遊戲數據成功:"+arg0.result);
			}
		});
		
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
			String roleName, String roleLevel, String zoneId, String zoneName,String token,String uid) {
		// TODO Auto-generated method stub

		Yayalog.loger("设置角色信息token："+token+"--"+uid);
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams requestParams = new RequestParams();
		requestParams.addBodyParameter("app_id", DeviceUtil.getAppid(paramActivity));
		requestParams.addBodyParameter("token", token+"");
		requestParams.addBodyParameter("uid", uid+"");
		requestParams.addBodyParameter("role_id", roleId);
		requestParams.addBodyParameter("role_name",roleName);
		requestParams.addBodyParameter("role_level",roleLevel);
		requestParams.addBodyParameter("zone_id",zoneId);
		requestParams.addBodyParameter("zone_name", zoneName);
		
		httpUtils.send(HttpMethod.POST, ViewConstants.SETROLEDATAURL, requestParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Yayalog.loger("kgamesdk上傳遊戲數據成功:"+arg0.result);
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
		
		String gameInfo = DeviceUtil.getGameInfo(activity, "sdktype");
		
		sdktype=Integer.parseInt(gameInfo);
	}

	/**
	 * 开启悬浮窗
	 * 
	 * @param activity
	 */
	public static void init(Activity activity) {

		if (sdktype==1) {
			
		}else {
			LogoWindow.getInstants(activity).start();

		}
		
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
		// UpdateUtil.isUpdate(activity);

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
	public static void ExitgameShowDialog(Activity activitiy, final KgameSdkCallback onexit) {
		if (DeviceUtil.isDebug(activitiy)) {
			Exitgame(activitiy, onexit);
			return;
		}
		
		if (KgameSdk.sdktype==1) {
			onexit.onSuccess(null, 1);
		}else {
			 Exit_dialog exit_dialog = new Exit_dialog(activitiy, "这个废弃",new ExitdialogCallBack() {
					
					@Override
					public void goExit() {
						// TODO Auto-generated method stub
						onexit.onSuccess(null, 1);
						//this.dialogDismiss();
					}
				});
				exit_dialog.dialogShow();
		}
	}
	/**
	 * 退出登录
	 * 
	 * @param activitiy
	 * @param onexit
	 */
	public static void Exitgame(Activity activitiy,
			final KgameSdkCallback onexit) {
		
		
//		if (KgameSdk.sdktype==1) {
			//onexit.onSuccess(null, 1);
			
			Dialog dialog = new AlertDialog.Builder(activitiy).setTitle("退出游戏提示")

					.setMessage("是否退出游戏？")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									onexit.onSuccess(null, 1);
									
								}
							})
							.setNeutralButton("取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}). create();

					dialog.show();
			
//		}else {
//			 Exit_dialog exit_dialog = new Exit_dialog(activitiy, "这个废弃",new ExitdialogCallBack() {
//
//					@Override
//					public void goExit() {
//						// TODO Auto-generated method stub
//						onexit.onSuccess(null, 1);
//						//this.dialogDismiss();
//					}
//				});
//				exit_dialog.dialogShow();
//		}
		
	}
	
	/**
	 * 功能
	 * @param paramActivity
	 * @param paramOrder
	 * @param paramCallback
	 */
	public static void GreenblueP(Activity paramActivity, Order paramOrder,int paytype,
			KgameSdkPaymentCallback paramCallback){
		mPaymentCallback = paramCallback;
		AgentApp.mPayOrder = paramOrder;
		
		KgameSdk.mPayOrder=paramOrder;
		Intent intent = new Intent(paramActivity,
				XiaoMipayActivity.class);

		//intent.putExtra("type", ViewConstants.YAYAPAYMAIN);
		paramActivity.startActivity(intent);
	}

	public static int managertype=1;
	/**
	 * 
	 * @param mactivity
	 * @param i
	 */
	public static void accountManager(Activity mactivity, int i) {
		// TODO Auto-generated method stub
		if (AgentApp.mUser == null) {
			Toast.makeText(mactivity.getApplicationContext(), "請先登錄",
					Toast.LENGTH_SHORT).show();
			return;
		}
		managertype=i;
		Intent intent = new Intent(mactivity.getApplicationContext(),
				SmallHelpActivity.class);

		intent.putExtra("type", ViewConstants.ACCOUNTMANAGER);
		mactivity.startActivityForResult(intent,10020);
	}
	
	/**
	 * 
	 * @param mactivity
	 * @param i
	 */
	public static void addPaomadengView(Activity mactivity) {
		// TODO Auto-generated method stub
		ViewGroup rootview =(ViewGroup) mactivity.findViewById(android.R.id.content);
		TextView textView = new TextView(mactivity);
		MachineFactory machineFactory = new MachineFactory(mactivity);
		machineFactory.MachineView(textView,-1 , 200, "LinearLayout");
		textView.setText("hhahahhhhhhhhhhhhfdsaaaaaaaaaaaaaaaaaaaaaaaaaaaaahhhhhhhhhhhhhh");
		textView.setTextColor(Color.RED );
		rootview.addView(textView);
	}
}
