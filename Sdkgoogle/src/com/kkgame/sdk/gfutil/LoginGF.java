package com.kkgame.sdk.gfutil;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.kkgame.sdk.gfutil.IabHelper.IabAsyncInProgressException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kkgame.sdk.bean.User;
import com.kkgame.sdk.login.Login_success_dialog;
import com.kkgame.sdkmain.AgentApp;
import com.kkgame.utils.DeviceUtil;
import com.kkgame.utils.Handle;
import com.kkgame.utils.JSONUtil;
import com.kkgame.utils.PermissionUtils;
import com.kkgame.utils.Yayalog;
import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest;
import com.yayawan.callback.YYWLoginHandleCallback;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class LoginGF {

    private static final int RC_SIGN_IN = 1010;

    private static CallbackManager callbackManager;

    private static GoogleSignInClient mGoogleSignInClient;

    private static IabHelper mHelper;

    static IabBroadcastReceiver mBroadcastReceiver;

    public static String token;

    private static Activity mActivity;

    /**
     * 初始化sdk
     */
    public static void inintsdk(Activity mactivity,String id ,String key) {
        mActivity = mactivity;
//        PermissionUtils.requestPermission(mactivity, "Manifest.permission.READ_PHONE_STATE",1);
        Boolean chaxun_imei = Sputils.contains(mactivity,"imei");
        Log.i("tag","查询imei = " + chaxun_imei);
        if(chaxun_imei == false){
            Random rand = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i <= 22; i++) {
                int randNum = rand.nextInt(9) + 1;
                String num = randNum + "";
                sb = sb.append(num);
            }
            sb = sb.append(System.currentTimeMillis() / 1000);
            String imei = String.valueOf(sb);
            Log.i("tag", "imei = " + imei);
            Sputils.putSPstring("imei", imei, mactivity);
        }
        ggInit(mactivity,id,key);
        fbInit(mactivity);
        Boolean chaxun = Sputils.contains(mactivity,"paytoken");
        Log.i("tag","查询 = " +chaxun);
        if(chaxun){
           String paytoken = Sputils.getSPstring("paytoken", "paytoken", mActivity);
           String paycode = Sputils.getSPstring("paycode", "paycode", mActivity);
           Log.i("tag","查询paycode = " +paycode);
           Log.i("tag","查询paytoken = " +paytoken);
           payhttp(paytoken,paycode);
        }
    }
    public static void ggLogin(Activity mactivity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mactivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public static void fbLogin(Activity mactivity) {
        LoginManager.getInstance().logInWithReadPermissions(mactivity, Arrays.asList("public_profile"));
    }
    public static void ggInit(final Activity mactivity,String id ,String key){
        //初始化gso，server_client_id为添加的客户端id
//        String id = "447881031922-n388upnbap2jkmn5eb0dksm81ab2k1h0.apps.googleusercontent.com";

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(id)
                .requestEmail()
                .build();
        //初始化Google登录实例,activity为当前activity
        mGoogleSignInClient = GoogleSignIn.getClient(mactivity, gso);
        //登录前可以查看是否已经授权，已经授权则可不必重复授权，如果返回的额account不为空则已经授权，同理activity为当前activity
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mactivity);

//       String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhmr+0kUQCrFt/FHlRiavgzSXXow9awVX+hnop6r1X4jImD1JbhhaH+DKtiaKW1MNtymtm/VgCJzLZMyX6FiM7Cl3yOhOnPz26JK4rpyCmMEkXLIJvbf/MKkzgbRTN/HLo5mdE9tTe/fS1GvdqqaMFWmcKG5JtpTryCdJz0mamifgCe1f0dDOLEQ0MYBg5Qs89dRkniJ/yGDZW+ahgirWntdiwexZZtC3iWmy5tk6+CTEd2bXChnZ3R74V6YsvImk2Xcb7e61JZS3RibjtPYUx8gjYQHmkOuf2PpW2FqoyVBfd42zz5RoU6aoMJJzR+0klGQyzT4up/AjpgP2T4EDMwIDAQAB";


        //支付初始化
        mHelper = new IabHelper(mactivity, key);

        mHelper.enableDebugLogging(true);

        Log.d("tag", "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d("tag", "Setup finished.= " +result);

                if (!result.isSuccess()) {
//                    complain("Problem setting up in-app billing: " + result);
                    Log.i("tag", "Setup fail");
                    return;
                }

                if (mHelper == null) {
                    Log.i("tag", "Setup fail.");
                    return;
                }
                Log.i("tag", "Setup success.");

                mBroadcastReceiver = new IabBroadcastReceiver(new IabBroadcastReceiver.IabBroadcastListener() {
                    @Override
                    public void receivedBroadcast() {
                        try {
                            mHelper.queryInventoryAsync(mGotInventoryListener);
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            e.printStackTrace();
                        }
                    }
                });
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                mactivity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d("tag", "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error querying inventory. Another async operation in progress.");
                }
            }
        });
    }
    public static void fbInit(Activity mactivity) {
        initFacebook(mactivity);
        FacebookSdk.sdkInitialize(mactivity);
        AppEventsLogger.activateApp(mactivity);
    }

    private static void initFacebook(final Activity mactivity) {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        String userId = accessToken.getUserId();
                        String token = accessToken.getToken();
                        login_http(mactivity,"395913303-"+userId ,"");
//                        Toast.makeText(mactivity, accessToken.getUserId() +"登录成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        loginFail();
//                        Toast.makeText(mactivity, "登陆取消", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            LoginManager.getInstance().logOut();
                            loginOut();
                        }
                        loginFail();
//                        Toast.makeText(mactivity, exception.getMessage() + "登陆失败", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    static String paycode;
    static final int RC_REQUEST = 10001;

    public static void pay(Activity mactivity ,String orderId){

        ////参数说明^act:调用的Activity,
        // sku:内购商品Id，
        // ITEM_TYPE_INAPP：购买类型,
        // requestCode:请求码，
        // listener：购买完成的监听，
        // extraData:上传至GooglePlay后台的唯一标识

        //"9999,bf_1253687,4569788,2,99,com.bfgamegg.mjzdlacha1"
//        String str = YYWMain.mOrder.ext;
//        String [] strs = str.split("[,]");
//        String SKU_GAS = strs[5];


        String SKU_GAS = YYWMain.mOrder.goods;
        paycode = SKU_GAS;
        Log.i("tag","code = " + paycode);
        try {
            mHelper.launchPurchaseFlow(mactivity, SKU_GAS, RC_REQUEST,
                    mPurchaseFinishedListener, orderId);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.i("tag","Error launching purchase flow. Another async operation in progress.");
            e.printStackTrace();
        }
    }

    private static IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.i("tag", "Purchase finished: " + result + ", purchase: " + purchase);

            if (result.isFailure()) {
                // Oh noes! pay fail
                Log.i("tag","Error purchasing: " + result);
                payFail();
                return;
            }
            String paytoken = purchase.getToken();
            Sputils.putSPstring("paytoken", paytoken, mActivity);
            Sputils.putSPstring("paycode", paycode, mActivity);
            payhttp(paytoken,paycode);
            Log.i("tag", "Purchase successful.");

        }
    };

    public static void payhttp(String paytoken,String paycode){
        HttpUtils httpUtil = new HttpUtils();
        String appid = DeviceUtil.getAppid(mActivity);
        String url = "https://api.sdk.75757.com/pay/notify/"+appid+"/";
        Log.i("tag","url = " +url);
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("g_token",paytoken);
        requestParams.addBodyParameter("g_productid", paycode);
        Log.i("tag","paytoken = " + paytoken);
        Log.i("tag","paycode = " + paycode);
        httpUtil.send(HttpRequest.HttpMethod.POST, url, requestParams,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Yayalog.loger("请求失败"+arg1.toString());
                        Log.i("tag","请求失败"+arg1.toString());
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        try {
                            Yayalog.loger("请求成功"+arg0.result);
                            Log.i("tag","请求成功"+arg0.result);
                            String msg = arg0.result;
                            Log.i("tag","msg = "+ msg);
                            if(msg.equals("success")){
                                Log.i("tag","pay - success");
                                paySuce();
                                Sputils.removeSPstring("paytoken",mActivity);
                                try {
                                    mHelper.queryInventoryAsync(mGotInventoryListener);
                                } catch (IabHelper.IabAsyncInProgressException e) {
                                    Log.i("tag","Error querying inventory. Another async operation in progress.");
                                }
                                  Sputils.removeSPstring("paycode",mActivity);
                                //FB事件统计
                                AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
                                logger.logEvent(AppEventsConstants.EVENT_NAME_PURCHASED);
//                                Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Log.i("tag","pay - fail");
                                payFail();
//                                Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static void onActivityResult(Activity paramActivity,int requestCode, int resultCode, Intent data) {
        if(callbackManager != null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        Log.i("tag", "requestCode = " + requestCode);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account;
            try {
                account = task.getResult(ApiException.class);
                String uid = account.getId();
                token = account.getIdToken();
//				Toast.makeText(mActivity, "uid = "+ uid +"token = "+token, Toast.LENGTH_SHORT).show();
                Log.i("tag", "uid = " + uid);
                Log.i("tag", "token = " + token);
//                Toast.makeText(paramActivity, uid + "登陆成功", Toast.LENGTH_SHORT).show();
                login_http(paramActivity,"2572311040-"+uid ,"");
            } catch (ApiException e) {
                Log.i("tag", "登陆失败 = " +e.getMessage());
                loginFail();
//              Toast.makeText(paramActivity, e.getMessage()+"登陆失败", Toast.LENGTH_SHORT).show();

            }
        }
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
//            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.i("tag", "onActivityResult handled by IABUtil.");
        }
//        if (requestCode == 1001) {
//            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
//            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
//            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
//            Log.i("tag","responseCode = " + responseCode);
//            if (responseCode == RESULT_OK) {
//                try {
//                    JSONObject jo = new JSONObject(purchaseData);
//                    String sku = jo.getString("productId");
//                    paySuce();
//                    Log.i("tag","You have bought the " + sku + ". Excellent choice, adventurer!");
//                }
//                catch (JSONException e) {
//                    Log.i("tag","Failed to parse purchase data.");
//                    e.printStackTrace();
//                }
//            }else{
//                Log.i("tag","Failed to pay");
//                payFail();
//            }
//        }
    }

    public static void login_http(final Activity mactivity,final String uid,String username){
        Log.i("tag", "login_http-uid = " + uid);
        Handle.login_handler(mactivity,uid,
                username,
                new YYWLoginHandleCallback() {

                    private YYWUser yywUser;

                    @Override
                    public void onSuccess(String response,
                                          String temp) {
                        // TODO Auto-generated method stub
                        Yayalog.loger("login_http-联合渠道登陆丫丫玩后返回数据："+response);
                        try {
                            JSONObject resjson = new JSONObject(
                                    response);
                            int err_code = resjson
                                    .optInt("err_code");
                            if (err_code == 0) {
                                JSONObject data = resjson.getJSONObject("data");
                                String kgameuid = data.optString("uid");
                                String kgameusername = data.optString("username");
                                String kgametoken = data.optString("token");
                                String is_register = data.optString("is_register");
                                Yayalog.loger("kgameuid："+kgameuid);
                                // 拼接返回给cp的user开始
                                yywUser = new YYWUser();
                                yywUser.uid = kgameuid;
                                yywUser.yywuid = kgameuid;
                                yywUser.userName = kgameusername;

                                User user = new User();
                                user.setToken(kgametoken);
                                user.setUserName(kgameusername);
                                user.setUid(new BigInteger(data.optString("uid")));
                                AgentApp.mUser = user;

                                Log.i("tag","is_register = " +is_register);
                                String FB = uid.substring(0,10);
                                Log.i("tag","FB = " +FB);
                                if(FB.equals("2572311040") && !is_register.equals("")) {
                                        Log.i("tag","iregister - suc");
                                        AppEventsLogger logger = AppEventsLogger.newLogger(mactivity);
                                        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
                                }
                                mactivity.runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                // TODO
                                                Yayalog.loger("login_http-联合渠道登陆成功："+yywUser.toString());
                                                Login_success_dialog login_success_dialog = new Login_success_dialog(
                                                        mactivity);
                                                login_success_dialog.dialogShow();
//                                                YYWMain.mUserCallBack.onLoginSuccess(yywUser, "success");
//                                                YYWMain.mUserCallBack.onLoginSuccess(yywUser, "onLoginSuccess");
//                                                userCallBack.onLoginSuccess(yywUser, "onLoginSuccess");

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
//                        userCallBack.onLoginFailed("登陆失败", "onFail");
                        loginFail();;
                    }
                });
    }

    /**查询库存的回调*/
    private static IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.i("tag", "Query inventory finished.");

            if (mHelper == null) return;

            if (result.isFailure()) {
                Log.i("tag", "Failed to query inventory: " + result);
                return;
            }
//            Log.i("tag", "Query inventory was successful." + inventory.getPurchase(mPayInfo.getProductId()));
//            if (inventory.hasPurchase(mPayInfo.getProductId())){
//                //库存存在用户购买的产品，先去消耗
//            }else{
//                //库存不存在
//            }
            Log.i("tag", "Query =" +paycode);
            Purchase gasPurchase = inventory.getPurchase(paycode);
            Log.i("tag", "gasPurchase = " +gasPurchase);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.i("tag", "We have gas. Consuming it.");
                try {
                    mHelper.consumeAsync(inventory.getPurchase(paycode), mConsumeFinishedListener);
                } catch (IabAsyncInProgressException e) {
                    Log.i("tag", "Error consuming gas. Another async operation in progress.");
                }
                return;
            }
        }
    };
   static boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    static IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.i("tag", "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.i("tag", "Consumption successful. Provisioning.");
//                mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
//                saveData();
//                alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            } else {
                Log.i("tag","Error while consuming: " + result);
            }
//            updateUi();
//            setWaitScreen(false);
            Log.i("tag","End consumption flow.");
        }
    };
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



    public static void onDestroy() {
        // very important:
        if (mBroadcastReceiver != null) {
            mActivity.unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.i("tag", "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }



}
