package com.yayawan.sdktemplate.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.iqiyi.sdk.platform.GamePlatform;
import com.kkgame.utils.DeviceUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    public static String APP_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("GAMELOG", "WXEntryActivity onCreate");
        APP_ID = DeviceUtil.getGameInfo(this, "aiqiyi_gameid");
        if (TextUtils.isEmpty(APP_ID)) {
            Log.w("GAMELOG", "WXEntryActivity APP_ID IS NULL");
        }
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        try {
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        GamePlatform.getInstance().weChatOauthResp(resp);
        finish();
    }
}