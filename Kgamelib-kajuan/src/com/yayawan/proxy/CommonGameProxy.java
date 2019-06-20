package com.yayawan.proxy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import com.kkgame.sdk.login.ViewConstants;
import com.kkgame.sdk.other.JFnoticeUtils;
import com.kkgame.sdk.other.JFupdateUtils;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.GameTestUtils;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.Sputils;
import com.kkgame.utils.Yayalog;
import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWLoginHandleCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWRole;
import com.yayawan.domain.YYWUser;
import com.yayawan.impl.ActivityStubImpl;
import com.yayawan.impl.AnimationImpl;
import com.yayawan.impl.ChargerImpl;
import com.yayawan.impl.LoginImpl;
import com.yayawan.impl.UserManagerImpl;
import com.yayawan.implyy.ChargerImplyylianhe;
import com.yayawan.main.YYWMain;

public class CommonGameProxy implements YYWGameProxy {

	private YYWLoginer mLogin;

	private YYWCharger mCharger;

	private YYWActivityStub mStub;

	private YYWUserManager mUserManager;

	private YYWAnimation mAnimation;

	private Activity mActivity;

	private int templevel;

	public CommonGameProxy() {

		this(new LoginImpl(), new ActivityStubImpl(), new UserManagerImpl(),
				new ChargerImpl());

		setAnimation(new AnimationImpl());

	}

	public CommonGameProxy(YYWLoginer login, YYWActivityStub stub,
			YYWUserManager userManager, YYWCharger charger) {
		super();
		// new CommonGameProxy();
		this.mLogin = login;
		this.mStub = stub;
		this.mUserManager = userManager;
		this.mCharger = charger;
	}

	public void setLogin(YYWLoginer login) {
		this.mLogin = login;

	}

	public void setCharger(YYWCharger charger) {
		this.mCharger = charger;
	}

	public void setStub(YYWActivityStub stub) {
		this.mStub = stub;
	}

	public void setUserManager(YYWUserManager userManager) {
		this.mUserManager = userManager;
	}

	public void setAnimation(YYWAnimation animation) {
		this.mAnimation = animation;
	}

	public static boolean ISNEWPAY = false;// 是否使用只新登陆,用于控制第三种支付方式

	@Override
	public void login(final Activity paramActivity,
			final YYWUserCallBack userCallBack) {
		mActivity = paramActivity;
		// YYWMain.mUserCallBack=userCallBack;
		Yayalog.logerlife("login");
		Yayalog.loger("commmonGameproxylogin");
		
		if (ViewConstants.ISKGAME) {
			Yayalog.loger("Kgamelogin");
			YYWMain.mUserCallBack = userCallBack;
			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
		} else {
			
			Yayalog.loger("UNIONlogin");
			YYWMain.mUserCallBack = new YYWUserCallBack() {

				@Override
				public void onLogout(Object paramObject) {
					// TODO Auto-generated method stub
					userCallBack.onLogout(paramObject);
				}

				@Override
				public void onLoginSuccess(final YYWUser paramUser,
						Object paramObject) {
					Yayalog.loger("联合渠道登陆成功："+paramUser.toString());
					// TODO Auto-generated method stub
					Handle.login_handler(mActivity, YYWMain.mUser.uid,
							YYWMain.mUser.userName,
							new YYWLoginHandleCallback() {

								private YYWUser yywUser;

								@Override
								public void onSuccess(String response,
										String temp) {
									// TODO Auto-generated method stub
									Yayalog.loger("联合渠道登陆丫丫玩后返回数据："+response);
									try {
										JSONObject resjson = new JSONObject(
												response);
										int err_code = resjson
												.optInt("err_code");
										if (err_code == 0) {
											JSONObject data = resjson
													.getJSONObject("data");
											String kgameuid = data
													.optString("uid");
											String kgameusername = data
													.optString("username");
											String kgametoken = data
													.optString("token");
											Yayalog.loger("kgameuid："+kgameuid);
											// 拼接返回给cp的user开始
											yywUser = new YYWUser();
											yywUser.uid = kgameuid;
											yywUser.userName = kgameusername;
											yywUser.yywtoken = kgametoken;
											yywUser.token = JSONUtil
													.formatToken(paramActivity,
															paramUser.token,
															paramUser.uid,
															paramUser.userName,
															yywUser.userName);
											// 拼接给cp的user结束
											Yayalog.loger("yywUser.uid："+yywUser.uid);
											// 拼接渠道的user，当调用渠道的支付，一定使用到渠道的YYWMain.mUser.uid
											YYWMain.mUser.uid = paramUser.uid;
											
											YYWMain.mUser.userName = paramUser.userName;
											YYWMain.mUser.yywuid = yywUser.uid;
											
											Yayalog.loger("YYWMain.mUser.yywuid："+YYWMain.mUser.yywuid);
											YYWMain.mUser.yywusername = yywUser.userName;
											YYWMain.mUser.yywtoken = yywUser.yywtoken;
											Yayalog.loger("+++++++++++++token"
													+ YYWMain.mUser.token);
											Yayalog.loger("+++++++++++++联合渠道登陆："
													+ YYWMain.mUser.toString());

											mActivity
													.runOnUiThread(new Runnable() {

														@Override
														public void run() {
															// TODO
															Yayalog.loger("联合渠道登陆成功："+yywUser.toString());
															userCallBack
																	.onLoginSuccess(
																			yywUser,
																			"onLoginSuccess");
															
														}
													});

										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								@Override
								public void onFail(String erro, String temp) {
									// TODO Auto-generated method stub
									userCallBack.onLoginFailed("登陆失败", "onFail");
								}
							});
				}

				@Override
				public void onLoginFailed(String paramString, Object paramObject) {
					// TODO Auto-generated method stub
					userCallBack.onLoginFailed("登陆失败", "onLoginFailed");
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					userCallBack.onCancel();
				}
			};

			this.mLogin.login(paramActivity, YYWMain.mUserCallBack, "login");
		}

		// 检测是否调用类

	}

	@Override
	public void logout(Activity paramActivity, YYWUserCallBack userCallBack) {
		YYWMain.mUserCallBack = userCallBack;
		Yayalog.logerlife("logout");
		//this.mLogin.relogin(paramActivity, userCallBack, "relogin");
	}

	public void logout(Activity paramActivity) {
		Yayalog.logerlife("logout");
		this.mUserManager.logout(paramActivity, null, null);
	}

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		YYWMain.mPayCallBack = payCallBack;
		Yayalog.logerlife("charge");
		YYWMain.mOrder = order;
		this.mCharger.charge(paramActivity, order, payCallBack);
	}

	@Override
	public void pay(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		Yayalog.logerlife("pay");
		YYWMain.mPayCallBack = payCallBack;
		YYWMain.mOrder = order;

		int login_type = Sputils.getSPint("login_type", 0, paramActivity);
		int login_pay_level = Sputils.getSPint("login_pay_level", 0,
				paramActivity);
		Yayalog.loger("CommonGameProxy：login_pay_level:" + "" + login_pay_level);
		switch (login_type) {
		case 0:
			Yayalog.loger("CommonGameProxy:" + "kgame支付");
			this.mCharger.pay(paramActivity, YYWMain.mOrder,
					YYWMain.mPayCallBack);
			break;

		case 1:

			if (compareLevel(templevel + "", login_pay_level + "")) {
				Yayalog.loger("CommonGameProxy1:" + "kgamelianhe支付");
				this.mCharger = new ChargerImplyylianhe();
				this.mCharger.pay(paramActivity, YYWMain.mOrder,
						YYWMain.mPayCallBack);
			} else {
				Yayalog.loger("CommonGameProxy1:" + "kgame支付");
				this.mCharger.pay(paramActivity, YYWMain.mOrder,
						YYWMain.mPayCallBack);
			}
			break;
		case 2:

			break;

		default:
			break;
		}

		// 获取配置文件的参数，指定切换支付的支付方式
		// this.mCharger.pay(paramActivity, YYWMain.mOrder,
		// YYWMain.mPayCallBack);
	}

	@Override
	public void manager(Activity paramActivity) {
		Yayalog.logerlife("manager");
		this.mUserManager.manager(paramActivity);

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback exitCallBack) {
		Yayalog.logerlife("exit");
		YYWMain.mExitCallback = exitCallBack;
		this.mUserManager.exit(paramActivity, exitCallBack);
	}

	@Override
	public void anim(Activity paramActivity, YYWAnimCallBack animCallback) {
		Yayalog.logerlife("anim");
		YYWMain.mAnimCallBack = animCallback;
		this.mAnimation.anim(paramActivity);

	}

	@Override
	public void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub

		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName);
		// 设置临时的角色等级。用作支付时候判断是否切换支付
		templevel = Integer.parseInt(roleLevel);
		this.mUserManager.setRoleData(paramActivity);
	}

	// 3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId, String roleName,
			String roleLevel, String zoneId, String zoneName, String roleCTime,
			String ext) {
		Yayalog.loger("调用了commongameproxy中setData");
		
		// 设置临时的角色等级。用作支付时候判断是否切换支付
		templevel = Integer.parseInt(roleLevel);
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName, roleCTime, ext);
		Yayalog.logerlife("setData"+ext+":"+YYWMain.mRole.toString());
		//Yayalog.loger("调用了commongameproxy中setData:"+YYWMain.mRole.toString());
		//Yayalog.logerlife("anim:"+YYWMain.mRole.toString());

		this.mUserManager.setData(paramActivity, roleId, roleName, roleLevel,
				zoneId, zoneName, roleCTime, ext);

	}

	@Override
	public void applicationInit(Activity paramActivity) {
		this.mStub.applicationInit(paramActivity);
	}

	boolean newactive = true;

	private int loca_login_type;

	@Override
	public void onCreate(final Activity paramActivity) {
		// 进行检查更新
		
		GameTestUtils.setData();
		
		mActivity = paramActivity;
		Yayalog.setCanlog(DeviceUtil.isDebug(paramActivity));//设置是否打log
		System.out.println("是否可以打印yayalog："+Yayalog.canlog);
		
		Yayalog.logerlife("onCreate:");
		
		// 获取公告
		new JFnoticeUtils().getNotice(paramActivity);
		// 获取更新
		new JFupdateUtils(paramActivity).startUpdate();

		mStub.onCreate(paramActivity);
		

	}

	@Override
	public void onStop(Activity paramActivity) {
		Yayalog.logerlife("onStop:");
		this.mStub.onStop(paramActivity);
		
	}

	@Override
	public void onResume(Activity paramActivity) {
		Yayalog.logerlife("onResume:");

		this.mStub.onResume(paramActivity);

	}

	@Override
	public void onPause(Activity paramActivity) {
		
		Yayalog.logerlife("onPause:");


		this.mStub.onPause(paramActivity);

	}

	@Override
	public void onRestart(Activity paramActivity) {
		Yayalog.logerlife("onRestart:");

		this.mStub.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		Yayalog.logerlife("onDestroy:");
		this.mStub.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		Yayalog.logerlife("applicationDestroy:");
		this.mStub.applicationDestroy(paramActivity);
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		Yayalog.logerlife("onActivityResult:");
		this.mStub.onActivityResult(paramActivity, paramInt1, paramInt2,
				paramIntent);

	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		if (mActivity != null) {
		}
		Yayalog.logerlife("onNewIntent:");
		this.mStub.onNewIntent(paramIntent);
	}

	@Override
	public void initSdk(Activity paramActivity) {
		Yayalog.logerlife("initSdk:");
		// TODO Auto-generated method stub
		// Class.forName("ActivityStubImpl").
		// 为了兼容老sdk判断是否有初始化方法再执行
		this.mStub.initSdk(paramActivity);
	}

	/**
	 * 判断本地的等级与线上设定的等级谁大
	 * 
	 * @return
	 */
	public static boolean compareLevel(String templevel, String xianshanglevel) {
		int xian = Integer.parseInt(xianshanglevel);
		Yayalog.loger("xian" + xianshanglevel);
		if (xian < 1) {
			return true;
		}
		try {
			int templ = Integer.parseInt(templevel);

			if (templ > xian) {
				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			// TODO: handle exception
			Yayalog.loger("判断等级" + e.toString());
		}

		return false;
	}

}
