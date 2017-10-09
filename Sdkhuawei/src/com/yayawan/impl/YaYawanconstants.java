package com.yayawan.impl;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameEventHandler;
import com.huawei.gameservice.sdk.model.Result;
import com.huawei.gameservice.sdk.model.RoleInfo;
import com.huawei.gameservice.sdk.model.PayResult;
import com.huawei.gameservice.sdk.model.UserResult;
import com.huawei.gameservice.sdk.util.LogUtil;
import com.huawei.gb.huawei.net.ReqTask;
import com.huawei.gb.huawei.net.ReqTask.Delegate;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.callback.KgameSdkCallback;
import com.kkgame.sdkmain.KgameSdk;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class YaYawanconstants {

	private static HashMap<String, String> mGoodsid;

	private static Activity mActivity;

	private static boolean isinit=false;
	
	private static String buoyPrivateKey = null;
	
	private static String APP_ID;
	
	private static String sign;
	
	private static String companyname;
	
	private static String PAY_ID;
	
	private static String PAY_PRIVATE;
	
	private static String PAY_RSA_PUBLIC;
	
	private static String LOGIN_RSA_PUBLIC ;
	
	private static String BUOY_SECRET ;

	
	
	/**
	 * 调用浮标时需要使用浮标的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
	 */
	/**
	 * the server url for getting the buoy private key.The CP need to modify the
	 * value for the real server.
	 */
//	public static final String GET_BUOY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getBuoyPrivate";
	/**
	 * 生成签名时需要使用RSA的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
	 */
	/**
	 * the server url for getting the pay private key.The CP need to modify the
	 * value for the real server.
	 */
//	public static final String GET_PAY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getPayPrivate";
	/**
	 * 初始化sdk
	 */
	public static void inintsdk(Activity mactivity) {
		mActivity = mactivity;
		Yayalog.loger("YaYawanconstants初始化sdk");
		
		ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate() {

			@Override
			public void execute(String result) {
				/**
				 * 从服务端获取的浮标私钥，由于没有部署最终的服务端，所以返回值写死一个值，CP需要从服务端获取，服务端代码参考华为Demo
				 * The demo app did not deployed the server, so the return value
				 * is written fixed.For real app,the CP need to get the buoy key
				 * from server.
				 */
				BUOY_SECRET = ""+DeviceUtil.getGameInfo(mActivity, "BUOY_SECRET");
				Log.i("tag","BUOY_SECRET="+BUOY_SECRET);
				buoyPrivateKey = BUOY_SECRET;

				// SDK初始化
				// SDK initialization
				APP_ID = ""+DeviceUtil.getGameInfo(mActivity, "APP_ID");
				PAY_ID = ""+DeviceUtil.getGameInfo(mActivity, "PAY_ID");
				companyname = ""+DeviceUtil.getGameInfo(mActivity, "companyname");
				Log.i("tag","APP_ID="+APP_ID);
				Log.i("tag","PAY_ID="+PAY_ID);
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
				GameServiceSDK.init(mActivity, APP_ID, PAY_ID,
						"", new GameEventHandler() {

							@Override
							public void onResult(Result result) {
								Log.i("tag","SDK初始化result="+result);
								if (result.rtnCode != Result.RESULT_OK) {
//									handleError("init the game service SDK failed:" + result.rtnCode);
									Log.i("tag","init the game service SDK failed="+ result.rtnCode);
									return;
								}
								isinit = true;
								checkUpdate();
							}

							@Override
							public String getGameSign(String appId, String cpId, String ts) {
								return createGameSign(appId + cpId + ts);
//								Log.i("tag","appId="+appId);
//								Log.i("tag","cpId="+cpId);
//								Log.i("tag","ts="+ts);
							}
						});
					}
				});
			}
		}, null, null);
		getBuoyPrivate.execute();
		
		
		Log.i("tag","初始化结束");
	}
	
	/**
	 * 生成游戏签名 generate the game sign
	 */
	private static String createGameSign(String data) {

		// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
		// For safety, buoy key put into the server and use the https way to get
		// down into the client's memory.
		// By the way CP can also use their safe approach.

		String str = data;
		try {
			Log.i("tag","buoyPrivateKey="+buoyPrivateKey);
			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), buoyPrivateKey);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	/**
	 * application初始化
	 */
	public static void applicationInit(Context applicationContext) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 登录
	 */
	public static void login(final Activity mactivity) {
		Yayalog.loger("YaYawanconstantssdk登录");
		Log.i("tag","登录");
		if(isinit){
		GameServiceSDK.login(mactivity, new GameEventHandler() {
			
			@Override
			public void onResult(Result result) {
				Log.i("tag","登录result="+result);
				// TODO Auto-generated method stub
				if (result.rtnCode != Result.RESULT_OK) {
					// 提示用户登录失败
					Log.i("tag","login failed="+ result.toString());
					Log.i("tag","登录失败");
					loginFail();
					Log.i("tag","登录失败1");
					} else {
					UserResult userResult = (UserResult) result;
					// 通知鉴权签名校验
					Log.i("tag","userResult.isAuth="+userResult.isAuth);
					if (userResult.isAuth != null && userResult.isAuth == 1) {
					boolean checkResult = checkSign(APP_ID + userResult.ts +
					userResult.playerId, userResult.gameAuthSign);
					Log.i("tag","APP_ID="+APP_ID);
					Log.i("tag","userResult.ts="+userResult.ts);
					Log.i("tag","userResult.playerId="+userResult.playerId);
					Log.i("tag","userResult.gameAuthSign="+userResult.gameAuthSign);
					Log.i("tag","checkResult="+checkResult);
					if (checkResult) {
						Log.i("tag","登录成功");
						getPlayerLevel();
						loginSuce(mActivity, userResult.playerId, userResult.playerId, "");
						Log.i("tag","登录成功1");
						Log.i("tag","userResult.toString()="+userResult.toString());
					} else {
						Log.i("tag","登录失败");
						loginFail();
						Log.i("tag","登录失败1");
						Log.i("tag","login auth failed check game auth sign error");
					}
					// 通知帐号变换
					} else if (userResult.isChange != null && userResult.isChange == 1) {
					login(mactivity);
					// 登录成功
					} else {
//					handleError("login success:" + userResult.toString());
						Log.i("tag","userResult.toString()="+userResult.toString());
					}
					}
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				// TODO Auto-generated method stub
				return null;
			}
		}, 1);
		}else{
			inintsdk(mactivity);
		}
	}

	/**
	 * 支付
	 * 
	 * @param mactivity
	 */
	public static void pay(Activity mactivity, String morderid,String privatekey) {

		Yayalog.loger("YaYawanconstantssdk支付");
		Log.i("tag","zhifu1");
		PAY_PRIVATE = privatekey;
		startPay(mactivity, YYWMain.mOrder.money/100+".00", YYWMain.mOrder.goods, YYWMain.mOrder.goods, morderid, payHandler);
		
		Log.i("tag","zhifu2");
	}


	/**
     * 支付回调handler
     */
    /**
     * pay handler
     */
    private static GameEventHandler payHandler = new GameEventHandler() {
		@Override
		public String getGameSign(String appId, String cpId, String ts) {
			return null;
		}
		
        @Override
        public void onResult(Result result){
 
        	Log.i("tag","result="+result);
            Map<String, String> payResp = ((PayResult)result).getResultMap();
//            String pay = getString(R.string.pay_result_failed);
            String pay = "Payment failed";
            Log.i("tag","payResp="+payResp);
            // 支付成功，进行验签
            // payment successful, then check the response value
            if ("0".equals(payResp.get("returnCode"))){
                if ("success".equals(payResp.get("errMsg"))){
                    // 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
                	// If the response value contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
                	if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode")))
                    {
                        payResp.remove("isCheckReturnCode");
                        
                    }
                	// 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
                	// If the response value does not contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
                	else
                    {
                        payResp.remove("isCheckReturnCode");
                        payResp.remove("returnCode");
                    }
                    // 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
                	// remove the param "sign" from response
                    String sign = payResp.remove("sign");
                    
                    String noSigna = getSignData(payResp);
                    
                    // 使用公钥进行验签
                    // check the sign using RSA public key
                    PAY_RSA_PUBLIC = ""+DeviceUtil.getGameInfo(mActivity, "PAY_RSA_PUBLIC");
                    Log.i("tag","PAY_RSA_PUBLIC="+PAY_RSA_PUBLIC);
                    boolean s = RSAUtil.doCheck(noSigna, sign, PAY_RSA_PUBLIC);
                    
                    if (s)
                    {
//                        pay = getString(R.string.pay_result_success);
                    	pay = "successful";
                    	paySuce();
                    	Log.i("tag","支付成功1");
                    }
                    else
                    {
                        pay = "Payment successful, but signature verification failed.";
                    	paySuce();
                    	Log.i("tag","支付成功2");
                    }
                }
               
            }else if ("30002".equals(payResp.get("returnCode"))){
                pay = "Payment has timed out.";
                Log.i("tag","支付失败");
                payFail();
                Log.i("tag","支付失败1");
            }
//            Toast.makeText(mActivity, pay, Toast.LENGTH_SHORT).show();
            
            // 重新生成订单号，订单编号不能重复，所以使用时间的方式，CP可以根据实际情况进行修改，最长30字符
            // generate the pay ID using the date format, and it can not be repeated. 
            // CP can generate the pay ID according to the actual situation, a maximum of 30 characters
            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
            String requestId = format.format(new Date());
//            ((TextView)findViewById(R.id.requestId)).setText(requestId);
            

        }
    };
	
	/**
	 * 退出
	 * 
	 * @param paramActivity
	 * @param callback
	 */
	public static void exit(Activity paramActivity,
			final YYWExitCallback callback) {
		Yayalog.loger("YaYawanconstantssdk退出");

		paramActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				KgameSdk.Exitgame(mActivity, new KgameSdkCallback() {
					
					@Override
					public void onSuccess(User arg0, int arg1) {
						// TODO Auto-generated method stub
						callback.onExit();
						
					}
					
					@Override
					public void onLogout() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError(int arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				});				
			}
		});
		//

	}
	
	/**
	 * 检测游戏更新 check the update for game
	 */
	private static void checkUpdate() {
		GameServiceSDK.checkUpdate(mActivity, new GameEventHandler() {

			@Override
			public void onResult(Result result) {
				if (result.rtnCode != Result.RESULT_OK) {
					Log.i("tag" , "result.rtnCode"+ result.rtnCode);
				}
			}

			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return null;
			}

		});
	}

//	private static void handleError(String errorMsg) {
//		Message msg = new Message();
//		Bundle data = new Bundle();
//		String strMsg = errorMsg;
//		data.putString("errorMsg", strMsg);
//		msg.setData(data);
////		uiHandler.sendMessage(msg);
//	}
	
	/**
	 * 校验签名 check the auth sign
	 */
	protected static boolean checkSign(String data, String gameAuthSign) {

		/*
		 * 建议CP获取签名后去游戏自己的服务器校验签名，公钥值请参考开发指导书5.1 登录鉴权签名的验签公钥
		 */
		/*
		 * The CP need to deployed a server for checking the sign.
		 */
		try {
			LOGIN_RSA_PUBLIC = ""+DeviceUtil.getGameInfo(mActivity, "LOGIN_RSA_PUBLIC");
			Log.i("tag","LOGIN_RSA_PUBLIC="+LOGIN_RSA_PUBLIC);
			return RSAUtil.verify(data.getBytes("UTF-8"), LOGIN_RSA_PUBLIC, gameAuthSign);
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 设置角色信息
	 * 
	 * @param arg0
	 */
	public static void setData(Activity paramActivity, String roleId, String roleName,String roleLevel, String zoneId, String zoneName, String roleCTime,String ext){
		// TODO Auto-generated method stub
		Yayalog.loger("YaYawanconstants设置角色信息");
		if (Integer.parseInt(ext) == 1){
			HashMap<String, String> playerInfo = new HashMap<String, String>();
			/**
			 * 将用户的等级等信息保存起来，必须的参数为RoleInfo.GAME_RANK(等级)/RoleInfo.GAME_ROLE(角色名称)
			 * /RoleInfo.GAME_AREA(角色所属区)/RoleInfo.GAME_SOCIATY(角色所属公会名称)
			 * 全部使用String类型存放
			 */
			/**
			 * the CP save the level, role, area and sociaty of the game player into
			 * the SDK
			 */
			playerInfo.put(RoleInfo.GAME_RANK, roleLevel);
			playerInfo.put(RoleInfo.GAME_ROLE, roleName);
			playerInfo.put(RoleInfo.GAME_AREA, zoneId);
			GameServiceSDK.addPlayerInfo(paramActivity, playerInfo, new GameEventHandler() {
				
				@Override
				public void onResult(Result result) {
					// TODO Auto-generated method stub
					if (result.rtnCode != Result.RESULT_OK) {
//						handleError("add player info failed:" + result.rtnCode);
						Toast.makeText(mActivity, "add player info failed:"+ result.rtnCode, Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public String getGameSign(String appId, String cpId, String ts) {
					// TODO Auto-generated method stub
					return null;
				}
			});
		}
	}
	public static void onResume(Activity paramActivity) {
		// TODO Auto-generated method stub
         GameServiceSDK.showFloatWindow(paramActivity);
	}

	public static void onPause(Activity paramActivity) {
		// TODO Auto-generated method stub
        GameServiceSDK.hideFloatWindow(paramActivity);
	}

	public static void onDestroy(Activity paramActivity) {
		// TODO Auto-generated method stub
        GameServiceSDK.destroy(paramActivity);
	}
	
	public static void onActivityResult(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onNewIntent(Intent paramIntent) {
		// TODO Auto-generated method stub
		
	}

	public static void onStart(Activity mActivity2) {
		// TODO Auto-generated method stub
		
	}

	public static void onRestart(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onCreate(Activity paramActivity) {
		// TODO Auto-generated method stub
		
	}

	public static void onStop(Activity paramActivity) {
		// TODO Auto-generated method stub
		
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
		;
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

	
	private static void getPlayerLevel() {

		GameServiceSDK.getPlayerLevel(mActivity, new GameEventHandler() {

			@Override
			public void onResult(Result result) {
				if (result.rtnCode != Result.RESULT_OK) {
//					handleError("get player level failed:" + result.rtnCode);
					Log.i("tag","get player level failed="+result.rtnCode);
					return;
				}
				UserResult userResult = (UserResult) result;
//				handleError("player level:" + userResult.playerLevel);
				Log.i("tag","player level="+userResult.playerLevel);
			}

			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return null;
			}

		});
	}

	 public static String getSignData(Map<String, String> params)
	    {
	      StringBuffer content = new StringBuffer();
	      
	      List keys = new ArrayList(params.keySet());
	      Collections.sort(keys);
	      for (int i = 0; i < keys.size(); i++)
	      {
	        String key = (String)keys.get(i);
	        if (!"sign".equals(key))
	        {
	          String value = (String)params.get(key);
	          if (value != null) {
	            content.append((i == 0 ? "" : "&") + key + "=" + value);
	          }
	        }
	      }
	      return content.toString();
	    }
	 
	 public static void startPay(final Activity activity, final String price, final String productName, final String productDesc,
	            final String requestId, final GameEventHandler handler) {
		 Log.i("tag","startPay-PAY_RSA_PRIVATE="+PAY_PRIVATE);
	        if ("".equals(PAY_PRIVATE)){
	            ReqTask getPayPrivate = new ReqTask(new Delegate(){

	                public void execute(String privateKey){
	                    /**
	                     * 从服务端获取的支付私钥，由于没有部署最终的服务端，所以返回值写死一个值，CP需要从服务端获取， 服务端代码参考华为Demo 请查阅 华为游戏中心SDK开发指导书.docx 的2.5节
	                     */
	                	/**
	                	 * The demo app did not deployed the server, so the return value is written fixed.For real app,the CP need to get the  key from server.
	                	 */
	                    privateKey = "";
	                    PAY_PRIVATE = privateKey;
	                    Log.i("tag","zhifu3");
	                    pay(activity, price, productName, productDesc, requestId, handler);
	                }
	            }, null, BUOY_SECRET);
	            getPayPrivate.execute();
	        }else{
	        	Log.i("tag","zhifu4");
	        	Log.i("tag","price="+price);
	        	Log.i("tag","productName="+productName);
	        	Log.i("tag","productDesc="+productDesc);
	        	Log.i("tag","requestId="+requestId);
	            pay(activity, price, productName, productDesc, requestId, handler);
	        }
	        
	    }

	 @SuppressWarnings("unchecked")
	public static void pay(
		        final Activity activity,
		        final String price,
		        final String productName,
		        final String productDesc,
		        final String requestId,
		            final GameEventHandler handler){
		 Map<String, String> params = new HashMap<String, String>();
	        // 必填字段，不能为null或者""，请填写从联盟获取的支付ID
	        // the pay ID is required and can not be null or "" 
	        params.put("userID", PAY_ID);
	        // 必填字段，不能为null或者""，请填写从联盟获取的应用ID
	        // the APP ID is required and can not be null or "" 
	        params.put("applicationID", APP_ID);
	        // 必填字段，不能为null或者""，单位是元，精确到小数点后两位，如1.00
	        // the amount (accurate to two decimal places) is required
	        params.put("amount", price);
	        // 必填字段，不能为null或者""，道具名称
	        // the product name is required and can not be null or "" 
	        params.put("productName", productName);
	        // 必填字段，不能为null或者""，道具描述
	        // the product description is required and can not be null or "" 
	        params.put("productDesc", productDesc);
	        // 必填字段，不能为null或者""，最长30字节，不能重复，否则订单会失败
	        // the request ID is required and can not be null or "". Also it must be unique.
	        params.put("requestId", requestId);

	        final String noSign = getSignData(params);
	        LogUtil.d("startPay", "noSign：" + noSign);
	        
	        // CP必须把参数传递到服务端，在服务端进行签名，然后把sign传递下来使用；服务端签名的代码和客户端一致
	        // the CP need to send the params to the server and sign the params on the server , 
	        // then the server passes down the sign to client;
//	        new Thread(new Runnable(){
//
//				@Override
//				public void run() {
//					
//						try {
//				HttpPost httpPost = new HttpPost("https://api.sdk.75757.com/data/get_uid/");
//				 String app_id = ""+DeviceUtil.getGameInfo(mActivity, "app_id");
//				 Log.i("tag","app_id="+app_id);
//				 List<NameValuePair> pm = new ArrayList<NameValuePair>(); 
//			        pm.add(new BasicNameValuePair("app_id", app_id)); 
//			        pm.add(new BasicNameValuePair("nosign", noSign)); 
//			        
//			        Log.i("tag","params="+pm);
//			        try { 
//			            // 设置httpPost请求参数 
//			        	Log.i("tag","httpPost1");
//			        	httpPost.setEntity(new UrlEncodedFormEntity(pm, HTTP.UTF_8)); 
//			            Log.i("tag","httpPost2");
//			            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);  
//			            Log.i("tag","httpPost3");
//			            if(httpResponse.getStatusLine().getStatusCode() == 200){
//			            	String re = EntityUtils.toString(httpResponse.getEntity());
//			            	JSONObject js = new JSONObject(re);
//			            	Log.i("tag","js="+js);
//			            	sign = js.getString("sign");
//			            	Log.i("tag","sign="+sign);
//			            	
//			            }
//			        }catch(ClientProtocolException e){
//			        	e.printStackTrace();
//			        }
//
//			}catch(Exception e) {
//				e.printStackTrace();
//			}	
//				}
//	        }).start();
//	        Log.i("tag", "sign： " + sign);
	        sign = RSAUtil.sign(noSign, PAY_PRIVATE);
//	        Log.i("tag", "sign1： " + sign);
	        LogUtil.d("startPay", "sign： " + sign);


	        Map<String, Object> payInfo = new HashMap<String, Object>();
	        // 必填字段，不能为null或者""
	        // the amount is required and can not be null or "" 
	        payInfo.put("amount", price);
	        // 必填字段，不能为null或者""
	        // the product name is required and can not be null or ""
	        payInfo.put("productName", productName);
	        // 必填字段，不能为null或者""
	        // the request ID is required and can not be null or ""
	        payInfo.put("requestId", requestId);
	        // 必填字段，不能为null或者""
	        // the product description is required and can not be null or ""
	        payInfo.put("productDesc", productDesc);
	        // 必填字段，不能为null或者""，请填写自己的公司名称
	        // the user name is required and can not be null or "". Input the company name of CP.
	        payInfo.put("userName", companyname);
	        // 必填字段，不能为null或者""
	        // the APP ID is required and can not be null or "". 
	        payInfo.put("applicationID", APP_ID);
	        // 必填字段，不能为null或者""
	        // the user ID is required and can not be null or "". 
	        payInfo.put("userID", PAY_ID);
	        // 必填字段，不能为null或者""
	        // the sign is required and can not be null or "".
	        Log.i("tag","sign=="+sign);
	        payInfo.put("sign", sign);
	        // 选填字段，建议使用RSA256
	        // recommended to use RSA256.
	        payInfo.put("signType", "RSA256");
	        
	        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
	        {
	        	Boolean isLandscape = DeviceUtil.isLandscape(mActivity)?true:false;
	    		Log.i("tag","isLandscape="+isLandscape);
	    		if(isLandscape){
	    			payInfo.put("screentOrient",2);
	    		}
	        }else{
	            payInfo.put("screentOrient",1);
	        }
	        Log.i("tag","zhifu5");
	        GameServiceSDK.startPay(activity, payInfo, handler);
	        Log.i("tag","zhifu6");
	 }
}
