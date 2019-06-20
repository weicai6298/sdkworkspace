package com.yayawan.impl;

import java.util.ArrayList;
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
import android.util.Log;
import android.widget.Toast;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.tygrm.sdk.TYRT;
import com.tygrm.sdk.bean.TYRLoginBean;
import com.tygrm.sdk.bean.TYRPayBean;
import com.tygrm.sdk.bean.TYRPayGameParams;
import com.tygrm.sdk.bean.TYRUploadInfo;
import com.tygrm.sdk.cb.ITYRBackCallBack;
import com.tygrm.sdk.cb.TYRSDKListener;
import com.tygrm.sdk.constan.TYRCode;
import com.tygrm.sdk.constan.UserUploadType;
import com.tygrm.sdk.core.TYRSDK;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;

@SuppressWarnings("deprecation")
public class YaYawanconstants {

	private static Activity mActivity;

	private static boolean isinit = false;
	
	private static boolean islogin = false;
	
	private static String role_id = "123";
	private static String role_name = "123";
	private static String role_level = "123";
	private static String zone_id = "123";
	private static String zone_name = "123";

	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		init(mactivity);
	}

	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {


	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		if(isinit){
			TYRSDK.getInstance().login();
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid) {
		Yayalog.loger("YaYawanconstantssdk支付");
		//如果没有对应参数 ,应该传入空字符串或者写死某一值.不能传入null
		TYRPayGameParams params = new TYRPayGameParams();
		params.setRoleid(role_id);                //角色id
		params.setCporder_sn(morderid);  //游戏生成的订单编号
//		params.setAmount(Double.parseDouble(YYWMain.mOrder.money/100+".0"));                 //购买物品的总价   单位为元
		params.setAmount(YYWMain.mOrder.money/100);                 //购买物品的总价   单位为元
		params.setProduct_name(YYWMain.mOrder.goods);          //购买物品的名称
		params.setBuyNum(1);                    //购买的数量
		params.setCoinNum("0");               //当前玩家身上拥有的游戏币数量
		params.setProduct_id("s2");            //购买物品的id
		params.setProduct_des(YYWMain.mOrder.goods);  //购买物品的描述
		params.setRoleLevel(role_level);               //玩家等级
		params.setRolename(role_name);              //玩家名字
		params.setServerId(zone_id);           //服务器id
		params.setServerName(zone_name);        //服务器名
		params.setVip("0");                     //角色vip等级
		TYRSDK.getInstance().pay(params);

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
		TYRSDK.getInstance().onKeyDown(paramActivity,new ITYRBackCallBack(){

            @Override
            public void onChannelExcit() {
            	if(islogin){
            		TYRUploadInfo info = new TYRUploadInfo();
            		info.setRoleid(role_id);              //角色id
            		info.setRolename(role_name);           //角色名称
            		info.setRolelevel(role_level);           //角色等级
            		info.setZoneid(zone_id);             //区服id
            		info.setZonename(zone_name);       //区服名
            		info.setBalance("");           //帐户余额
            		info.setVip("");                  //vip等级
            		info.setPartyname("无帮派");        //帮派信息  如果没有帮派信息 则传入-->无帮派
            		info.setAttach("原始数据");         //cp传入android 端的所有数据 如果是json 应该转义
//            		UNDEFINED,//未定义
//            		JOIN_SERVER, //进入服务器
//            		CREATE_ROLE, //创建角色
//            		UPGRADE, //角色升级
//            		FACTION, //加入公会 或者帮派
//            		EXIT,        //退出游戏
//            		OTHER       //其他情况

            		//1为角色登陆成功  2为角色创建  3为角色升级
            		if (islogin){
            			info.setType(UserUploadType.EXIT);
            			TYRSDK.getInstance().reportedRoleInfo(info);
            		}
            	}
            	callback.onExit();
            	islogin = false;
            }

            @Override
            public void onGameExcit() {
            	Log.i("tag", "22222222");
            }
        }); 
			}
		});
	}

	
	/** 
	 * 设置角色信息
	 * 
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		Yayalog.loger("YaYawanconstants设置角色信息");
		//角色创建时间
//		HttpPost(roleId,roleName,roleLevel,zoneId,zoneName,roleCTime);
		role_id = roleId;
		role_name = roleName;
		role_level = roleLevel;
		zone_id = zoneId;
		zone_name = zoneName;
		TYRUploadInfo info = new TYRUploadInfo();
		info.setRoleid(role_id);              //角色id
		info.setRolename(role_name);           //角色名称
		info.setRolelevel(role_level);           //角色等级
		info.setZoneid(zone_id);             //区服id
		info.setZonename(zone_name);       //区服名
		info.setBalance("");           //帐户余额
		info.setVip("");                  //vip等级
		info.setPartyname("无帮派");        //帮派信息  如果没有帮派信息 则传入-->无帮派
		info.setAttach("原始数据");         //cp传入android 端的所有数据 如果是json 应该转义
//		UNDEFINED,//未定义
//		JOIN_SERVER, //进入服务器
//		CREATE_ROLE, //创建角色
//		UPGRADE, //角色升级
//		FACTION, //加入公会 或者帮派
//		EXIT,        //退出游戏
//		OTHER       //其他情况

		//1为角色登陆成功  2为角色创建  3为角色升级
		if (Integer.parseInt(ext) == 1){
			info.setType(UserUploadType.JOIN_SERVER);
			TYRSDK.getInstance().reportedRoleInfo(info);
		}else if (Integer.parseInt(ext) == 2){
			info.setType(UserUploadType.CREATE_ROLE);
			TYRSDK.getInstance().reportedRoleInfo(info);
		}else if (Integer.parseInt(ext) == 3){
			info.setType(UserUploadType.UPGRADE);
			TYRSDK.getInstance().reportedRoleInfo(info);
		}

	}

	public static void onResume(Activity paramActivity) {
		TYRSDK.getInstance().onResume(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		TYRSDK.getInstance().onPause(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		TYRSDK.getInstance().onDestroy(paramActivity);
	}

	public static void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		TYRSDK.getInstance().onActivityResult(paramInt1, paramInt2, paramIntent,paramActivity);
	}

	public static void onNewIntent(Intent paramIntent) {
		TYRSDK.getInstance().onNewIntent(paramIntent,mActivity);
	}

	public static void onStart(Activity paramActivity) {
		TYRSDK.getInstance().onStart(paramActivity);
	}

	public static void onRestart(Activity paramActivity) {

	}

	public static void onCreate(Activity paramActivity) {

	}

	public static void onStop(Activity paramActivity) {
		TYRSDK.getInstance().onStop(paramActivity);
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

		YYWMain.mUser.uid = DeviceUtil.getGameId(mactivity) + "-" + uid + "";
		if (username != null) {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
					+ username + "";
		} else {
			YYWMain.mUser.userName = DeviceUtil.getGameId(mactivity) + "-"
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
	private static void init(final Activity mactivity) {
		TYRSDK.getInstance().TYRInit(mactivity, new TYRSDKListener() {
		    @Override
		    public void onSwitchAccount(TYRLoginBean  data ) {
		       //data 如果为null,则说明sdk调用了注销,此时游戏应该清空登录信息,并重新唤起登录
		//data 如果不为null,则说明sdk内部调用了注销,并重新登录成功,需返回游戏区服界面，重新登录认证，重新加载新用户对应的角色数据
		    }
		    @Override
		    public void onInitChange(int code, String msg) {
		        switch (code) {
		            case TYRCode.CODE_INIT_SUCCESS:
		                Log.i("tag", "初始化成功" + msg);
		            	isinit = true ;
		                break;
		            case TYRCode.CODE_INIT_FAIL:
		                TYRT.show(mactivity, "初始化失败" + msg);
		                break;
		        }
		    }
		    @Override
		    public void onLoginChange(int code, TYRLoginBean tyrLoginBean) {
		        switch (code) {
		            case TYRCode.CODE_LOGIN_SUCCESS:
//		                TYRT.show(this, "登录成功");
		            	String uid = tyrLoginBean.getsID();
		            	String token = tyrLoginBean.getToken();
		            	Log.i("tag", "uid =" + uid);
		            	Log.i("tag", "token =" + token);
		            	loginSuce(mactivity, uid, uid, token);
		            	islogin = true;
		                break;
		            case TYRCode.CODE_LOGIN_FAIL:
		                //注意:失败时 data 可能是 null
//		                TYRT.show(this, "登录失败" );
		            	loginFail();
		                break;
		        }
		    }
		  
		    @Override
		    public void onLogout() {
		        //SDK 主动登出触发该回调，游戏调用 logout()接口不会收到该回调
		        //用户登出回调（需要收到该回调需要返回游戏登录界面，并调用 login 接口，打开 SDK 登录界面）
		    	mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						loginOut();
					}
				});
		    }
			@Override
			public void onPayResult(int code, TYRPayBean tyrPayBean) {
				 String s = "";
			        if (tyrPayBean != null) {
			            s = tyrPayBean.getPayID() + "          " + tyrPayBean.getMoney();
			        }
			        switch (code) {
			            case TYRCode.CODE_PAY_SUCCESS:
			                Log.i("tag","支付成功" + s);
			                paySuce();
			                break;
			            case TYRCode.CODE_PAY_FAIL:
					String msg = tyrPayBean.getMsg();
			                Log.i("tag","支付失败"+msg);
			                payFail();
			                break;
			            case TYRCode.CODE_PAY_CANCEL:
			                TYRT.show(mactivity, "支付取消");
			                payFail();
			                break;
			            case TYRCode.CODE_PAY_UNKNOWN:
			                TYRT.show(mactivity, "未知错误");
			                payFail();
			                break;
			        }
			}
		});

	}
}
