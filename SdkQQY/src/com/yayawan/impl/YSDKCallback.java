package com.yayawan.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.user.PersonInfo;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

/**
 * TODO GAME 游戏需要根据自己的逻辑实现自己的YSDKCallback对象。
 * YSDK通过UserListener抽象类中的方法将授权或查询结果回调给游戏。 游戏根据回调结果调整UI等。只有设置回调，游戏才能收到YSDK的响应。
 * 这里是Java层回调(设置了Java层回调会优先调用Java层回调, 如果要使用C++层回调则不能设置Java层回调)
 */
public class YSDKCallback implements UserListener, BuglyListener {
	public static Activity mainActivity;
	
	

	public YSDKCallback(Activity activity) {
		mainActivity = activity;
	}

	public void OnLoginNotify(UserLoginRet ret) {

		Logutils.sys("OnLoginNotify:ret"+ret.toString());
		
		
		
		switch (ret.flag) {
		
		
		case eFlag.Succ:
			
			UserLoginRet ret1 = new UserLoginRet();
			int platform = YSDKApi.getLoginRecord(ret1);
			if (platform==1) {
				Myconstants.platform="qq";
				Myconstants.mpayinfo.opentype = "qq";
			}else if(platform==2){
				Myconstants.platform="wx";
				Myconstants.mpayinfo.opentype = "wx";
			}
			
			System.out.println("准备查询");
			if (Myconstants.platform.equals("qq")) {
				System.out.println("进来qq查询");
				YSDKApi.queryUserInfo(ePlatform.QQ);
			} else {
				System.out.println("进来微信查询");
				YSDKApi.queryUserInfo(ePlatform.WX);
			}

			//
			break;
		// 游戏逻辑，对登陆失败情况分别进行处理
		case eFlag.QQ_NetworkErr:
			Toast.makeText(mainActivity, "网络连接失败，请检查网络", 0).show();
			loginFail();
			break;
		case eFlag.WX_UserCancel:
			Toast.makeText(mainActivity, "取消登录", 0).show();
			loginFail();
			break;
		case eFlag.WX_NotInstall:
			Toast.makeText(mainActivity, "未安装微信", 0).show();
			loginFail();
			break;
		case eFlag.WX_NotSupportApi:
			Toast.makeText(mainActivity, "不支持微信登录", 0).show();
			loginFail();
		
			break;
		case eFlag.WX_LoginFail:
			Toast.makeText(mainActivity, "微信登录失败", 0).show();
			loginFail();
			break;
		case eFlag.QQ_LoginFail:
			Toast.makeText(mainActivity, "手q登录失败", 0).show();
			loginFail();
			break;
		case eFlag.Login_TokenInvalid:
		
			loginFail();
			break;
		default:
			// 显示登陆界面
			loginFail();
			// //mainActivity.letUserLogout();
			break;
		}
	}

	public void OnWakeupNotify(WakeupRet ret) {
		Log.d("yaya", "called");
		Log.d("yaya", ret.toString() + ":flag:" + ret.flag);
		Log.d("yaya", ret.toString() + "msg:" + ret.msg);
		Log.d("yaya", ret.toString() + "platform:" + ret.platform);
		// //mainActivity.platform = ret.platform;
		// TODO GAME 游戏需要在这里增加处理异账号的逻辑
		if (eFlag.Succ == ret.flag || eFlag.Login_TokenInvalid == ret.flag) {
			// eFlag_AccountRefresh代表 刷新微信票据成功
			Log.d("yaya", "login success flag:" + ret.flag);
			// //mainActivity.letUserLogin();
		}  else if (ret.flag == eFlag.Login_TokenInvalid) {
			// 异账号时，游戏需要弹出提示框让用户选择需要登陆的账号
			Log.d("yaya", "diff account");
			// mainActivity.showDiffLogin();
		} else if (ret.flag == eFlag.Login_TokenInvalid) {
			// 没有有效的票据，登出游戏让用户重新登录
			if (YYWMain.mUserCallBack!=null) {
				YYWMain.mUserCallBack.onLogout("");
			}
			Log.d("yaya", "need login");
			// mainActivity.letUserLogout();
		} else {
			Log.d("yaya", "logout");
			// mainActivity.letUserLogout();
		}
	}

	@Override
	public void OnRelationNotify(UserRelationRet relationRet) {
		
		//String result = relationRet.toString();
		// relationRet.persons
		System.out.println("OnRelationNotify:登录成功");
		PersonInfo mPersonInfo = (PersonInfo) relationRet.persons.get(0);
		Log.d("yaya登陆成功等到的结果",
				"OnRelationNotify" + relationRet.persons.toString());
		
		System.out.println("得到的openKey"+Myconstants.mpayinfo.openKey);
		
		UserLoginRet ret1 = new UserLoginRet();
		int platform = YSDKApi.getLoginRecord(ret1);
		if (platform==1) {
			Myconstants.platform="qq";
			Myconstants.mpayinfo.opentype = "qq";
		}else if(platform==2){
			Myconstants.platform="wx";
			Myconstants.mpayinfo.opentype = "wx";
		}
		String accessToken = ret1.getAccessToken();
		//token = ret.getAccessToken();
		String payToken = ret1.getPayToken();
		// 支付需要的参数	
		// 从手Q登录态或微信登录态中获取的openid的值
		Myconstants.mpayinfo.openId = mPersonInfo.openId;
		// 从手Q登录态或微信登录态中获取的access_token 的值
		Myconstants.mpayinfo.openKey = accessToken;
		// 从手Q登录态中获取的pay_token的值; 微信登录时特别注意该参数传空。
		Myconstants.mpayinfo.qq_paytoken = ret1.getPayToken();
		// 登录获取的pf值
		Myconstants.mpayinfo.pf = ret1.pf;
		// 登录获取的pfkey值
		Myconstants.mpayinfo.pfKey = ret1.pf_key;
		Myconstants.mpayinfo.sessionId = mPersonInfo.openId;
		//每次刷新数据存起来。等调用login的时候判断是否有数据。没有数据再进行授权登陆
		Myconstants.openId=mPersonInfo.openId;
		Myconstants.nickname=mPersonInfo.nickName;
		Myconstants.accessToken=payToken;
		
		//后台运行几分钟，到前台有可能刷新票据
		if (YYWMain.mUser!=null) {
					return;
				}
		loginSuce(mainActivity, Myconstants.openId, mPersonInfo.nickName, payToken);
		Sputils.putSPstring("logout", "no", mainActivity);
		// 发送结果到结果展示界面
		// mainActivity.sendResult(result);
	}

	@Override
	public String OnCrashExtMessageNotify() {
		// 此处游戏补充crash时上报的额外信息
		Log.d("yaya",
				String.format(Locale.CHINA, "OnCrashExtMessageNotify called"));
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "new Upload extra crashing message for bugly on "
				+ time.format(nowTime);
	}

	@Override
	public byte[] OnCrashExtDataNotify() {
		// TODO Auto-generated method stub
		return null;
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
		YYWMain.mUser.uid = DeviceUtil.getUnionid(mactivity) + "-" + uid + "";
		;
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getUnionid(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getUnionid(mactivity) + "-"
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
			YYWMain.mUserCallBack.onLoginFailed("", "");

		}
	}

	/*
	 * 支付成功
	 */
	public static void paySuce() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
					"success");
		}
	}

	public static void payFail() {
		// 支付成功
		if (YYWMain.mPayCallBack != null) {
			YYWMain.mPayCallBack.onPayFailed(null, null);
		}
	}

	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub

	}

}
