package com.md5;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * MD5加密工具�?
 * <p/>
 * 作�?�：余天�? on 16/5/9 上午11:46
 */
public class MD5Util {

    /**
     * 加密
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public final static String  encrypt(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法?MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        System.out.println("hex.toString()："+hex.toString());
        return hex.toString();
    }
    /**
     * md5测试
     * @param args
     */
    public static void main(String[] args) {
//    	https://game.lzgame.top/game/play?gameid=72
    	//appid 72
    	//appkey fb5b89e6ddc5755ddbfbf05ae73e521d
//    	登录回调：http://pulsdk.7724.com/bufanyouxi/loginback/game/jstl
//    	支付回调: http://pulsdk.7724.com/bufanyouxi/paynotify/game/jstl
    	
//    	Private Key的算法为：QHOPENSDK_PRIVATEKEY = MD5(appSecret + "#" + appKey)，格式为32位小写
//    	appid 203732981
//    	appkey448c52fa8c6d5b6c94222deb38dda313
//    	secret93214aae92295fea603b0ea1567833c1
        String appSecret="93214aae92295fea603b0ea1567833c1";
        String appKey="448c52fa8c6d5b6c94222deb38dda313";
        System.out.println("appSecret="+appSecret);
        System.out.println("appKey="+appKey);

        String QHOPENSDK_PRIVATEKEY=MD5Util.encrypt(appSecret+"#"+appKey);
        String QHOPENSDK_PRIVATEKEY1=md5(appSecret+"#"+appKey);
        System.out.println("QHOPENSDK_PRIVATEKEY："+QHOPENSDK_PRIVATEKEY);
        System.out.println("QHOPENSDK_PRIVATEKEY1："+QHOPENSDK_PRIVATEKEY1);
    }
}