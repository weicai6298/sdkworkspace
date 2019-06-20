package com.kkgame.sdk.bean;

import java.io.Serializable;
import java.util.HashMap;
/**
 * 第一次支付的结果
 * @author wjy
 *
 */
public class PayResult implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public String action;   //地址
    public String method;   //访问方法
    public int success;     //成功标示值
    
    public int error_code;  //错误码
    public String error_msg;    //错误信息
    
    public HashMap<String,String> params;   //访问参数

    @Override
    public String toString() {
        return "PayResult [action=" + action + ", method=" + method
                + ", success=" + success + ", error_code=" + error_code
                + ", error_msg=" + error_msg + ", params=" + params + "]";
    }
    
    
    
    
}
