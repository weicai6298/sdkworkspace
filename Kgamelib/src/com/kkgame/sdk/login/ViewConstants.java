package com.kkgame.sdk.login;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;

import com.kkgame.common.CommonData;
import com.kkgame.sdk.bean.PayMethod;
import com.kkgame.sdk.bean.PayResult;
import com.kkgame.utils.DeviceUtil;

public class ViewConstants {

//	5.5 修改了调试模式  增加了两个适配接口
//	5.0 加入空白sdk  在配置文件sdktype中配置
//	4.9 修复老微信支付
//	4.8 增加代金券支付
//	4.7 删除多余小助手代码
	public static final String SDKVERSION = "5.5";
	public static final int LOGIN_VIEW = 1;
	public static final int REGISTER_VIEW = 2;
	public static final int REGISTERACCOUNT_VIEW = 3;
	public static final int WEIBOLOGIN_VIEW = 4;
	public static final int QQLOGIN_VIEW = 5;
	public static final int YAYAPAYMAIN = 6;
	public static boolean ISKGAME = false;
	public static String  dbpath = CommonData.Dbpath;

	public static String baseurl = CommonData.BaseUrl;// 千其域名

	
	public static String smallhelp = baseurl + "web/profile";// 小助手个人中心
	public static String smallhelpgift = baseurl + "web/game_gift";// 小助手个人中心
	public static String smallhelpcustomer_service = baseurl + "web/customer_service";// 小助手个人中心
	
	public static String getphonecode = baseurl + "user/sendcode";// 手机验证码

	public static String phoneregister = baseurl + "user/mobile_register";// 手机注册

	public static String acountregister = baseurl + "user/register";// 账号注册

	public static String loginurl = baseurl + "user/login";// 登录接口
	//
	public static String resetpassword = baseurl + "user/forget";// 找回密码

	public static String activeurl = baseurl + "data/active_handler";// 激活回调

	public static String unionloginurl = baseurl + "data/login_handler";// 登陆回调

	public static String makeorder = baseurl + "pay/init_pay";// 下单

	public static String updateurl = baseurl + "data/update";// 更新接口

	public static String unionmakeorder = baseurl + "data/pay_handler";// 联合渠道下单
	
	public static String paytype = baseurl + "data/payinfo";  //在中间件中，支付前请求，支付方式

	public static String NOTICEURL = baseurl + "data/notice";

	public static String SETROLEDATAURL = baseurl + "user/roleinfo";
	
	public static String WEIBOLOGINURL = baseurl + "/web/oauth/?type=sina&forward_url=sdk";//第三方微博登陆
	
	public static String QQLOGINURL = baseurl + "/web/oauth/?type=testqq&forward_url=sdk";//第三方qq登陆
	
	//https://rest.yayawan.com/web/oauth/?type=testqq&forward_url=sdk
	
	public static final int YINLIANPAY_ACTIVITY = 12;
	// 支付宝第二次确认页面
	public static final int PAYMENT_JF = 7;
	// 重置密码界面
	public static final int RESETPASSWORD = 8;
	// 重置密码界面
	public static final int ACCOUNTMANAGER = 9;
	// 启动页
	public static final int STARTANIMATION = 11;
	// isfirstlogin是否第一次进来登录页面
	public static final int FIRSTLOGIN = -1;
	public static final int NOFIRSTLOGIN = 0;

	// 支付插件版本
	public static final int PLUINVERSIONCODE = 2;
	// dialog集合
	public static ArrayList<Dialog> mDialogs = new ArrayList<Dialog>();

	public static String shortname = null;

	// 控制第一次快速注册
	public static Boolean tempisFastregist = false;

	// 控制第一次快速登陆
	public static Boolean tempisFastlogin = true;

	// 临时的弹窗窗口
	public static Dialog TEMPLOGIN_HO = null;

	// 主界面activity
	public static Activity mMainActivity = null;

	// 是否弹出手机登陆
	public static Boolean OPENPHONELOGIN = true;

	// 控制是否手动退出
	public static Boolean HADLOGOUT = false;

	// 支付activity
	public static Activity mPayActivity = null;

	// 验证码倒计时
	public static long SENDMESSAGETIME = 60000;

	// 关闭小助手时候是否显示提示
	public static Boolean ISSHOWDISMISSHELP = true;

	// 是否为miui系统
	public static Boolean ismiui = false;

	// 是否不需要切换账号
	public static Boolean nochangeacount = false;

	// 用户是否打开了yywan兔子logo
	public static boolean iscloseyylog = false;

	public static boolean demoyayalogin = false;

	public static PayResult mPayResult;

	// 判断是注册还是登陆  今日头条用的  1为登陆 2 为注册
	public static long logintype = 1;
	// public static String USER_FIRST_PASSWORD_SAVE=;

	public static int getHoldActivityHeight(Context mContext) {
		int ho_height = 650;
		int po_height = 850;
		int height = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
		}
		return height;
	}

	public static int getHoldActivityWith(Context mContext) {
		int ho_with = 1080;

		int po_with = 650;

		int with = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {
		} else if ("landscape".equals(orientation)) {
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			with = po_with;
		}
		return with;
	}

	public static int getHoldDialogHeight(Context mContext) {
		int ho_height = 600;
		int po_height = 600;
		int height = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
		}
		return height;
	}

	public static int getHoldDialogWith(Context mContext) {
		int ho_with = 760;

		int po_with = 600;

		int with = 0;
		// 判断横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {
		} else if ("landscape".equals(orientation)) {
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			with = po_with;
		}
		return with;
	}

}
