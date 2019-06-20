package com.kkgame.sdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.kkgame.sdkmain.AgentApp;



public class AuthNumReceiver extends BroadcastReceiver {
    private static MessageListener mMessageListener;

    // 广播消息类型
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取intent参数
        Bundle bundle = intent.getExtras();
        // 判断bundle内容
        if (bundle != null) {
            // 取pdus内容,转换为Object[]
            Object[] pdus = (Object[]) bundle.get("pdus");
            // 解析短信
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                byte[] pdu = (byte[]) pdus[i];
                messages[i] = SmsMessage.createFromPdu(pdu);
            }
            // 解析完内容后分析具体参数
            for (SmsMessage msg : messages) {
                /*
                 * String str = "xxx第47297章33"; String regex = "\\d*"; Pattern p
                 * = Pattern.compile(regex);
                 *
                 * Matcher m = p.matcher(str);
                 *
                 * while (m.find()) { if (!"".equals(m.group()))
                 * System.out.println("come here:" + m.group()); }
                 */

                // 获取短信内容
                String content = msg.getMessageBody();

                //System.err.println(content);
                /*if (content.contains("YaYaWan.com")) {
                    String[] split = content.split("：");
                    String authNum = split[1].substring(0,
                            split[1].indexOf("【")).trim();
                    AgentApp.mAuthNum = authNum;
                    mMessageListener.onReceived(authNum);
                }*/
                if (content.contains("丫丫玩")) {
                    //String[] split = content.split("：");
                    //String authNum = split[1].substring(0,  split[1].indexOf("【")).trim();

                    Pattern  continuousNumberPattern = Pattern.compile("[0-9\\.]+");
                    Matcher m = continuousNumberPattern.matcher(content);
                    String dynamicPassword = "";
                    while(m.find()){
                        if(m.group().length() == 5) {
                            //System.out.print(m.group());
                            dynamicPassword = m.group();
                        }
                    }
                    AgentApp.mAuthNum = dynamicPassword;
                    mMessageListener.onReceived(dynamicPassword);
                    //msg.
                }
            }
        }
    }

    // 回调接口
    public interface MessageListener {
        public void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }
}
