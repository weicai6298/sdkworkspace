package com.yayawan.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import cn.nubia.componentsdk.constant.ConstantProgram;
import cn.nubia.componentsdk.constant.ErrorCode;
import cn.nubia.nbgame.sdk.GameSdk;
import cn.nubia.nbgame.sdk.entities.AppInfo;
import cn.nubia.nbgame.sdk.interfaces.CallbackListener;
import cn.nubia.nbgame.sdk.util.Constant;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.GameApi;

public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static final int REQUEST_STORAGE_PERMISSION_LOGIN = 100;
    private static final int REQUEST_STORAGE_PERMISSION_PAY = 101;
    

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
//		login(mactivity);
}

/**
 * application初始化
 */
public static void applicationInit(Context applicationContext) {
	AppInfo appInfo = new AppInfo();
	appInfo.setAppId(Integer.parseInt(DeviceUtil.getGameInfo(applicationContext, "nby_appid")));
	appInfo.setAppKey(DeviceUtil.getGameInfo(applicationContext, "nby_appkey")); 
	appInfo.setOrientation(DeviceUtil.isLandscape(applicationContext)?0:1); //0：横屏；1：竖屏
	GameSdk.initSdk(applicationContext, appInfo,new CallbackListener<Bundle>() {

		@Override
		public void callback(int responseCode, Bundle bundle){
			// 初始化成功或失败的结果由CP自己处理，Toast非必选，sdk会保存初始化状态
			// 初始化返回如下三个参数，一般cp接入用不到，这里只是给出如何获取
			String tempSessionId = bundle.getString(Constant.TEMP_SESSION_ID);
			String serverTime = bundle.getString(Constant.SERVER_TIME);
			String errorDescription = bundle.getString(Constant.ERROR_DESCRIPTION);
			String msg = "";
			if (responseCode == ErrorCode.SUCCESS) {
				isinit = true ;
				Log.i("tag","sdk初始化成功");
			} else {
				Log.i("tag","sdk初始化失败" + ErrorMsgUtil.translate(responseCode));
			}
		}
	});
}

/**
 * 登录
 */
public static void login(final Activity mactivity) {
	Yayalog.loger("YaYawanconstantssdk登录");
	if(isinit){
		GameSdk.openLoginActivity(mactivity, new CallbackListener<Bundle>() {
            @Override
            public void callback(int responseCode, Bundle bundle){
                switch (responseCode) {
                    case ErrorCode.SUCCESS:
					
                        // 登陆成功，拿uid和sessionId去CP服务器完成 通过Nubia提供的服务器接口校验合法性、角色创建或查询等操作
						// 服务器校验合法性，服务器接入详情请看服务端接入文档
						// 登陆成功后，每次都需要重新获取下面的信息，重新登陆后，因为Nubia服务器中gameId和sessionId是一一对应的，不能只更新gameId不更新sessionId
						String gameId = bundle.getString(Constant.GAME_ID);// Nubia账户登录后得到联运gameId，玩家用于游戏的账号，玩家身份的唯一标识，映射玩家游戏内账号
						String sessionId = bundle.getString(Constant.SESSION_ID);// Nubia账户登录成功后由SDK后台服务器分配的唯一身份标识码，与gameId存在对应关系，需调用相应接口校验。
						String uId = bundle.getString(Constant.UID);// 登录时获取的uid，该uid仅用于订单支付签名和少数验证场景，切勿使用uid与游戏内账号做映射，否则会导致玩家丢失账号；
						String nickName = bundle.getString(Constant.NICK_NAME);// 用户昵称
						String userName = bundle.getString(Constant.USER_NAME);//用户名
						String avatarPath = bundle.getString(Constant.AVATAR_PATH);//用户头像地址，可能为空，需要做非空判断

						// TODO: 2018/7/11 也可以使用sdk提供的如下方法快速获取

						// String gameId = GameSdk.getLoginGameId();// Nubia账户登录后得到联运gameId，玩家用于游戏的账号，玩家身份的唯一标识，映射玩家游戏内账号
						// String sessionId = GameSdk.getSessionId();// Nubia账户登录成功后由SDK后台服务器分配的唯一身份标识码，与gameId存在对应关系，需调用相应接口校验。
						// String uId = GameSdk.getLoginUid();// 登录时获取的uid，该uid仅用于订单支付签名和少数验证场景，切勿使用uid与游戏内账号做映射，否则会导致玩家丢失账号；
						// String nickName = GameSdk.getNickName();// 用户昵称
						// String userName = GameSdk.getLoginAccount();//用户名
						// String avatarPath = GameSdk.getAvatar();//用户头像地址，可能为空，需要做非空判断
						loginSuce(mactivity, gameId, uId, sessionId);
                        Log.i("tag", "login success");
                        break;
                    case ErrorCode.NO_PERMISSION:
                        // Android6.0没允许安装和更新所需权限，需要运行时请求，主要是存储权限
                        requestStoragePermission(REQUEST_STORAGE_PERMISSION_LOGIN);
                        Toast("登录需要安装努比亚联运中心服务，未获得安装和更新所需权限");
                        loginFail();
                        Log.i("tag", "login failure: " + "code=" + responseCode + ", message=未获得安装和更新所需权限");
                        break;
                    default:
                        // 登录失败，包含错误码和错误消息
                    	loginFail();
                        Toast("登录失败：" + ErrorMsgUtil.translate(responseCode));
                        Log.i("tag", "login failure: " + "code=" + responseCode + ", message=" + ErrorMsgUtil.translate(responseCode));
                        break;
                }
            }
        });
	}else{
		loginFail();
	}
}

/**
 * 支付
 * @param mactivity
 */
public static void pay(Activity mactivity, String morderid, String sign ,String data_timestamp ,String amount) {
	Yayalog.loger("YaYawanconstantssdk支付");
	 HashMap<String, String> map = new HashMap<String, String>();
     map.put(ConstantProgram.TOKEN_ID, GameSdk.getSessionId());
     map.put(ConstantProgram.UID, GameSdk.getLoginUid());
     map.put(ConstantProgram.APP_ID, DeviceUtil.getGameInfo(mactivity, "nby_appid"));
     map.put(ConstantProgram.APP_KEY, DeviceUtil.getGameInfo(mactivity, "nby_appkey"));
     map.put(ConstantProgram.AMOUNT, amount);
     map.put(ConstantProgram.PRICE, amount);
     map.put(ConstantProgram.NUMBER, "1");
     map.put(ConstantProgram.PRODUCT_NAME, YYWMain.mOrder.goods);
     map.put(ConstantProgram.PRODUCT_DES, morderid);
//     map.put(ConstantProgram.PRODUCT_ID, "A01");
//     map.put(ConstantProgram.PRODUCT_UNIT, "个");
     //由CP服务器返回的订单编号，订单号不能重复
     map.put(ConstantProgram.CP_ORDER_ID, morderid);
     // 为了安全性考虑，doSign最好在服务端执行, 时间戳在DATA_TIMESTAMP和签名两个地方传递的必须是一致的
     map.put(ConstantProgram.SIGN, sign);
     map.put(ConstantProgram.DATA_TIMESTAMP, data_timestamp);
//     map.put(ConstantProgram.CHANNEL_DIS, "1");
     map.put(ConstantProgram.GAME_ID, GameSdk.getLoginGameId());

     Yayalog.loger("支付请求参数：" + map.toString());
     GameSdk.doPay(mactivity, map, new CallbackListener<String>() {
         @Override
         public void callback(int responseCode, String message) {
             switch (responseCode) {
                 case 0:
                     // 支付完成
                	 paySuce();
                	 Toast("支付成功");
                     break;
                 case -1:
                     // 本次支付失败
                	 payFail();
                     Toast("支付失败");
                     Log.i("tag","支付失败 = "+ " + message + ");
                     break;
                 case 10001:
                     // 用户取消了本次支付
                	 payFail();
                     Toast("支付取消");
                     Log.i("tag","您已经取消本次支付 = " + message);
                     break;
                 case 10002:
                     // 网络异常
                	 payFail();
                     Toast("网络异常，请检查网络设置");
                     Log.i("tag","网络异常，请检查网络设置 = " + message);
                     break;
                 case 10003:
                     // 订单结果确认中，建议客户端向自己业务服务器校验支付结果
                	 payFail();
                     Toast("支付结果确认中，请稍后查看"+ "{" + message + "}");
                     Log.i("tag","支付结果确认中，请稍后查看 = " + message);
                     break;
                 case 10004:
                     // 支付服务正在升级
                	 payFail();
                     Toast("支付服务正在升级"+ "{" + message + "}");
                     Log.i("tag","支付服务正在升级 = " + message);
                     break;
                 case 10005:
                     // 支付组件安装失败或是未安装
                	 payFail();
                	 Toast("支付服务安装失败或未安装"+ "{" + message + "}");
                     Log.i("tag","支付服务安装失败或未安装 = " + message);
                     break;
                 case 10006:
                     // 订单信息有误
                	 payFail();
                	 Toast("订单信息有误"+ "{" + message + "}");
                     Log.i("tag","订单信息有误 = " + message);
                     break;
                 case 10007:
                     // 获取支付渠道失败
                	 payFail();
                	 Toast("获取支付渠道失败，请稍后重试"+ "{" + message + "}");
                     Log.i("tag","获取支付渠道失败，请稍后重试 = " + message);
                     break;
                 case 10008:
                     // Android6.0没允许相关权限，需要运行时请求，主要是存储权限
                	 payFail();
                	 Toast("未获得安装和更新所需权限");
                	 Log.i("tag","未获得安装和更新所需权限");
                     requestStoragePermission(REQUEST_STORAGE_PERMISSION_PAY);
                     break;
                 default:
                	 payFail();
                	 Toast("支付失败");
                     Log.i("tag","支付失败 =" + message);
                     break;
             }
         }
     });
}

/**
 * 退出
 * @param paramActivity
 * @param callback
 */
public static void exit(final Activity paramActivity,final YYWExitCallback callback) {
	Yayalog.loger("YaYawanconstantssdk退出");
paramActivity.runOnUiThread(new Runnable() {
	
	@Override
	public void run() {
	KgameSdk.Exitgame(paramActivity, new KgameSdkCallback() {

		@Override
		public void onSuccess(User arg0, int arg1) {
			callback.onExit();
		}

		@Override
		public void onLogout() {
		}

		@Override
		public void onError(int arg0) {
		}

		@Override
		public void onCancel() {
		}
	});
	}
});
}

/** 
 * 设置角色信息
 */
public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
	Yayalog.loger("YaYawanconstants设置角色信息");
	//角色创建时间
	//HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);

	//1为角色登陆成功  2为角色创建  3为角色升级
	if(Integer.parseInt(ext) == 1){

	}else if(Integer.parseInt(ext) == 2){

	}else if(Integer.parseInt(ext) == 3){

	}
}

public static void onResume(Activity paramActivity) {

}

public static void onPause(Activity paramActivity) {

}

public static void onDestroy(Activity paramActivity) {

}

public static void onActivityResult(Activity paramActivity, int paramInt1,int paramInt2, Intent paramIntent) {

}

public static void onNewIntent(Intent paramIntent) {

}

public static void onStart(Activity paramActivity) {

}

public static void onRestart(Activity paramActivity) {

}

public static void onCreate(Activity paramActivity) {

}

public static void onStop(Activity paramActivity) {

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
public static void loginSuce(Activity mactivity, String uid,String username, String session) {

	YYWMain.mUser = new YYWUser();

	YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
	if (username != null) {
		YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
				+ username + "";
	} else {
		YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
				+ uid + "";
	}

	//		 YYWMain.mUser.nick = data.getNickName();
	YYWMain.mUser.token = JSONUtil.formatToken(mactivity, session,
			YYWMain.mUser.userName);

	if (YYWMain.mUserCallBack != null) {
		YYWMain.mUserCallBack.onLoginSuccess(YYWMain.mUser, "success");
		Handle.login_handler(mactivity, YYWMain.mUser.uid,
				YYWMain.mUser.userName);
	}
}


/**
 * 登出
 */
public static void loginOut() {
	if (YYWMain.mUserCallBack != null) {
		YYWMain.mUserCallBack.onLogout(null);
	}
}
/**
 * 登录失败
 */
public static void loginFail() {
	if (YYWMain.mUserCallBack != null) {
		YYWMain.mUserCallBack.onLoginFailed(null, null);
	}
}

/**
 * 支付成功
 * 
 */
public static void paySuce() {
	if (YYWMain.mPayCallBack != null) {
		YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
				"success");
	}
}

/**
 * 支付失败
 * 
 */
public static void payFail() {
	if (YYWMain.mPayCallBack != null) {
		YYWMain.mPayCallBack.onPayFailed(null, null);
	}
}

/*
 * Toast提示
 */
public static void Toast(final String msg){
	mActivity.runOnUiThread(new Runnable() {

		@Override
		public void run() {
			Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();	
		}
	});
}

private static void requestStoragePermission(int requestCode) {
    ActivityCompat.requestPermissions(mActivity,
            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
}

//@Override
//public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    switch (requestCode) {
//        case REQUEST_STORAGE_PERMISSION_LOGIN:
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                doLogin();
//            }
//            break;
//        case REQUEST_STORAGE_PERMISSION_PAY:
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                doPay();
//            }
//            break;
//    }
//}
}
