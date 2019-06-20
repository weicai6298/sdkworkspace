package com.kkgame.sdk.bean;

public class ConfirmPay {

    public int success;
    
    public String body;
    
    public int error_code;
    public String error_msg;
    @Override
    public String toString() {
        return "ConfirmPay [success=" + success + ", body=" + body
                + ", error_code=" + error_code + ", error_msg=" + error_msg
                + "]";
    }
    
    
}
