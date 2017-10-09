package com.yayawan.impl.qqhelper;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.PersonInfo;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TODO GAME 游戏需要根据自己的逻辑实现自己的YSDKCallback对象。
 * YSDK通过UserListener抽象类中的方法将授权或查询结果回调给游戏。 游戏根据回调结果调整UI等。只有设置回调，游戏才能收到YSDK的响应。
 * 这里是Java层回调(设置了Java层回调会优先调用Java层回调, 如果要使用C++层回调则不能设置Java层回调)
 */
public class YSDKCallback implements UserListener, BuglyListener, PayListener {
	public static Activity mainActivity;

	public YSDKCallback(Activity activity) {
		mainActivity = (Activity) activity;
	}

	public void OnLoginNotify(UserLoginRet ret) {
		
		String result = "";
		// mainActivity.stopWaiting();
		switch (ret.flag) {
		case eFlag.Succ:
			// mainActivity.letUserLogin();
			UserLoginRet ret1 = new UserLoginRet();
			int platform = YSDKApi.getLoginRecord(ret1);

			// loginSuce(mainActivity, openid, openid, payToken);
			 showToastTips("登陆成功");
			if (platform == 1) {
				YSDKApi.queryUserInfo(ePlatform.QQ);
			} else {
				YSDKApi.queryUserInfo(ePlatform.WX);
			}

			break;
		// 游戏逻辑，对登录失败情况分别进行处理
		case eFlag.QQ_UserCancel:
			showToastTips("用户取消授权，请重试");
			loginFail();
			// mainActivity.letUserLogout();
			break;
		case eFlag.QQ_LoginFail:
			loginFail();
			showToastTips("QQ登录失败，请重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.QQ_NetworkErr:
			loginFail();
			showToastTips("QQ登录异常，请重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.QQ_NotInstall:
			loginFail();
			showToastTips("手机未安装手Q，请安装后重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.QQ_NotSupportApi:
			loginFail();
			showToastTips("手机手Q版本太低，请升级后重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.WX_NotInstall:
			loginFail();
			showToastTips("手机未安装微信，请安装后重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.WX_NotSupportApi:
			loginFail();
			showToastTips("手机微信版本太低，请升级后重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.WX_UserCancel:
			loginFail();
			showToastTips("用户取消授权，请重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.WX_UserDeny:
			loginFail();
			showToastTips("用户拒绝了授权，请重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.WX_LoginFail:
			loginFail();
			showToastTips("微信登录失败，请重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.Login_TokenInvalid:
			loginFail();
			//showToastTips("您尚未登录或者之前的登录已过期，请重试");
			// mainActivity.letUserLogout();
			break;
		case eFlag.Login_NotRegisterRealName:
			// 显示登录界面
			loginFail();
			showToastTips("您的账号没有进行实名认证，请实名认证后重试");
			// mainActivity.letUserLogout();
			break;
		default:
			// 显示登录界面
			// mainActivity.letUserLogout();
			break;
		}
	}

	private void loginFail() {
		// TODO Auto-generated method stub
		QqYsdkHelp.loginFail();
	}

	public void OnWakeupNotify(WakeupRet ret) {
		// TODO GAME 游戏需要在这里增加处理异账号的逻辑
		if (eFlag.Wakeup_YSDKLogining == ret.flag) {
			// 用拉起的账号登录，登录结果在OnLoginNotify()中回调
		} else if (ret.flag == eFlag.Wakeup_NeedUserSelectAccount) {
			// 异账号时，游戏需要弹出提示框让用户选择需要登录的账号
			// Log.d(//mainActivity.LOG_TAG,"diff account");
			showToastTips("您的账号异常，请退出重新登陆");
			// mainActivity.showDiffLogin();
		} else if (ret.flag == eFlag.Wakeup_NeedUserLogin) {
			// 没有有效的票据，登出游戏让用户重新登录
			showToastTips("登陆过期，请退出重新登陆");
			// Log.d(//mainActivity.LOG_TAG,"need//Login");
			// mainActivity.letUserLogout();
		} else {
			// Log.d(//mainActivity.LOG_TAG,"logout");
			// mainActivity.letUserLogout();
		}
	}

	@Override
	public void OnRelationNotify(UserRelationRet relationRet) {
		String result = "";
		result = result + "flag:" + relationRet.flag + "\n";
		result = result + "msg:" + relationRet.msg + "\n";
		result = result + "platform:" + relationRet.platform + "\n";
		if (relationRet.persons != null && relationRet.persons.size() > 0) {
			PersonInfo personInfo = (PersonInfo) relationRet.persons
					.firstElement();
			StringBuilder builder = new StringBuilder();
			builder.append("UserInfoResponse json: \n");
			builder.append("nick_name: " + personInfo.nickName + "\n");
			builder.append("open_id: " + personInfo.openId + "\n");
			builder.append("userId: " + personInfo.userId + "\n");
			builder.append("gender: " + personInfo.gender + "\n");
			builder.append("picture_small: " + personInfo.pictureSmall + "\n");
			builder.append("picture_middle: " + personInfo.pictureMiddle + "\n");
			builder.append("picture_large: " + personInfo.pictureLarge + "\n");
			builder.append("provice: " + personInfo.province + "\n");
			builder.append("city: " + personInfo.city + "\n");
			builder.append("country: " + personInfo.country + "\n");		
			result = result + builder.toString();
			System.out.println("我在获取资料这里");
			if (QqYsdkHelp.islogin && QqYsdkHelp.mQqusercallback != null) {
				UserLoginRet ret1 = new UserLoginRet();
				int platform = YSDKApi.getLoginRecord(ret1);
				String accessToken = ret1.getAccessToken();
				String payToken = ret1.getPayToken();
				String openid = ret1.open_id;
				int flag = ret1.flag;
				String msg = ret1.msg;
				String pf = ret1.pf;
				String pf_key = ret1.pf_key;
				QQUser qqUser = new QQUser();
				qqUser.setAccessToken(accessToken);
				qqUser.setMsg(msg);
				qqUser.setNickName(personInfo.nickName);
				qqUser.setOpenid(openid);
				qqUser.setPayToken(payToken);
				qqUser.setPf(pf);
				qqUser.setPf_key(pf_key);
				qqUser.setPlatform(platform);
				qqUser.setRelationRet(relationRet);
				QqYsdkHelp.loginSuccess(qqUser);
				
			}

		} else {
			result = result + "relationRet.persons is bad";
		}
		// Log.d(//mainActivity.LOG_TAG,"OnRelationNotify" + result);

		// 发送结果到结果展示界面
		// mainActivity.sendResult(result);
	}

	@Override
	public String OnCrashExtMessageNotify() {
		// 此处游戏补充crash时上报的额外信息
		// Log.d(//mainActivity.LOG_TAG,String.format(Locale.CHINA,
		// "OnCrashExtMessageNotify called"));
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return "new Upload extra crashing message for bugly on "
				+ time.format(nowTime);
	}

	@Override
	public byte[] OnCrashExtDataNotify() {
		return null;
	}

	@Override
	public void OnPayNotify(PayRet ret) {
		// Log.d(//mainActivity.LOG_TAG,ret.toString());
		if (PayRet.RET_SUCC == ret.ret) {
			// 支付流程成功
			switch (ret.payState) {
			// 支付成功
			case PayRet.PAYSTATE_PAYSUCC:
				// mainActivity.sendResult(
				// "用户支付成功，支付金额"+ret.realSaveNum+";" +
				// "使用渠道："+ret.payChannel+";" +
				// "发货状态："+ret.provideState+";" +
				// "业务类型："+ret.extendInfo+";建议查询余额："+ret.toString());
				break;
			// 取消支付
			case PayRet.PAYSTATE_PAYCANCEL:
				// mainActivity.sendResult("用户取消支付："+ret.toString());
				break;
			// 支付结果未知
			case PayRet.PAYSTATE_PAYUNKOWN:
				// mainActivity.sendResult("用户支付结果未知，建议查询余额："+ret.toString());
				break;
			// 支付失败
			case PayRet.PAYSTATE_PAYERROR:
				// mainActivity.sendResult("支付异常"+ret.toString());
				break;
			}
		} else {
			switch (ret.flag) {
			case eFlag.Login_TokenInvalid:
				// 用户取消支付
				// mainActivity.sendResult("登录态过期，请重新登录："+ret.toString());
				// mainActivity.letUserLogout();
				break;
			case eFlag.Pay_User_Cancle:
				// 用户取消支付
				// mainActivity.sendResult("用户取消支付："+ret.toString());
				break;
			case eFlag.Pay_Param_Error:
				// mainActivity.sendResult("支付失败，参数错误"+ret.toString());
				break;
			case eFlag.Error:
			default:
				// mainActivity.sendResult("支付异常"+ret.toString());
				break;
			}
		}
	}

	public void showToastTips(final String tips) {
		mainActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(mainActivity, tips, Toast.LENGTH_LONG).show();
			}
		});

	}

	

	// /*
	// * 支付成功
	// */
	// public static void paySuce() {
	// // 支付成功
	// if (YYWMain.mPayCallBack != null) {
	// YYWMain.mPayCallBack.onPaySuccess(YYWMain.mUser, YYWMain.mOrder,
	// "success");
	// }
	// }
	//
	// public static void payFail() {
	// // 支付成功
	// if (YYWMain.mPayCallBack != null) {
	// YYWMain.mPayCallBack.onPayFailed(null, null);
	// }
	// }

}
