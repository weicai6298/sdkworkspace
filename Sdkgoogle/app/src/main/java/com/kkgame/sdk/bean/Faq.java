package com.kkgame.sdk.bean;
/**
 * 反馈
 * @author wjy
 *
 */
public class Faq {

    public String content;
    public int type; //类型,1为客服,2为用户
    @Override
    public String toString() {
        return "Faq [content=" + content + ", type=" + type + "]";
    }
    
    
}
