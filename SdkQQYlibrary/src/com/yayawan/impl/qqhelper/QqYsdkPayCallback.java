package com.yayawan.impl.qqhelper;

import com.tencent.ysdk.module.pay.PayRet;

public interface QqYsdkPayCallback {

	 /**
	  * 支付成功
	  * @param ret   腾讯客户端支付成功返回的参数
	  */
    public abstract void onSuccess();
    
    /**
     * 支付失败
     * @param errtip    支付失败的可能原因
     * @param ret		支付失败腾讯返回的PayRet对象
     */
    public abstract void onPayFail(String errtip,PayRet ret);

}
