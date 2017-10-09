package com.yayawan.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pyw.open.support.ISDKEventCode;
import com.pyw.open.support.ISDKEventExtraKey;
import com.pyw.open.support.OnSDKEventListener;
import com.pyw.open.support.User;


public class SDKEventListener implements OnSDKEventListener {
	private Context mContext;
	private Activity mActivity;

	public SDKEventListener(Context context,Activity activity) {
		mContext = context;
		mActivity = activity;
	}

	@Override
	public void onEvent(int eventCode, Bundle data) {
		switch (eventCode) {
		case ISDKEventCode.CODE_LOGIN_SUCCESS:
			// 登录成功通知，bundle中会带有user信息
			if (data != null) {
				User user = (User) data.getSerializable(ISDKEventExtraKey.EXTRA_USER);
				if (user != null) {
					String userId = user.getUserId(); //朋友玩为用户分配的唯一标识
					String token  = user.getToken();
					Log.d("SDKCallback", "用户ID：" + userId);
					YaYawanconstants.log("activity"+mActivity);
					YaYawanconstants.loginSuce(mActivity, userId, userId, token);
				}
			}
			// 发送登录SDK成功广播通知界面
			mContext.sendBroadcast(new Intent(YaYawanconstants.ACTION_LOGIN_SDK_SUCCESS));
			break;
		case ISDKEventCode.CODE_LOGIN_FAILD:
			String erroMsg = data.getString(ISDKEventExtraKey.EXTRA_ERRO);
			Log.d("SDKCallback", erroMsg);
			break;
		case ISDKEventCode.CODE_CHARGE_SUCCESS:
			// 充值成功
			Log.d("SDKCallback","充值成功:" + data.getString(ISDKEventExtraKey.EXTRA_ORDERID));
			YaYawanconstants.paySuce();
			YaYawanconstants.Toast("充值成功");
			break;
		case ISDKEventCode.CODE_CHARGE_FAIL:
			// 充值失败
			Log.d("SDKCallback","充值失败:" + data.getString(ISDKEventExtraKey.EXTRA_ORDERID));
			YaYawanconstants.payFail();
			YaYawanconstants.Toast("充值失败");
			break;
		case ISDKEventCode.CODE_CHARGE_CANCEL:
			// 取消支付
			Log.d("SDKCallback","取消支付:" + data.getString(ISDKEventExtraKey.EXTRA_ORDERID));
			YaYawanconstants.payFail();
			YaYawanconstants.Toast("充值失败");
			break;
		case ISDKEventCode.CODE_GET_ROLE_SUCCESS:
			// 上报角色信息成功
			break;
		case ISDKEventCode.CODE_GET_ROLE_FAIL:
			// 上报角色信息失败
			break;
		case ISDKEventCode.CODE_LOGOUT:
			// 注销
			// 发送通知，切换到开始登陆场景
			mContext.sendBroadcast(new Intent(YaYawanconstants.ACTION_TO_START_LOGIN));
			break;
		case ISDKEventCode.CODE_EXIT:
			// 退出
			mContext.sendBroadcast(new Intent(YaYawanconstants.ACTION_TO_EXIT_GAME));
			break;
		}
	}
}
