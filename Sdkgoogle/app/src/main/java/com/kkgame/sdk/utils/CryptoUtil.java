package com.kkgame.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.annotation.SuppressLint;
import android.util.Base64;



public class CryptoUtil {

	public final static String TYPE = "AES";
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	@SuppressLint("TrulyRandom")
    public static byte[] AESencode(byte[] data, String key) {
		try {
			byte[] raw = key.getBytes("UTF-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, TYPE);
			Cipher cipher = Cipher.getInstance(TYPE);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String AESencode(String data, String key) {
		byte[] srcData = data.getBytes();
		byte[] endata = AESencode(srcData, key);
		return HEX.toHex(endata);
	}
	
	public static byte[] AESdecode(byte[] data, String key) {
		try {
			byte[] raw = key.getBytes("UTF-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, TYPE);
			Cipher cipher = Cipher.getInstance(TYPE);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String AESdecode(String data, String key) {
		byte[] endata = data.getBytes();
		byte[] dedata = AESdecode(endata, key);
		return new String(dedata);
	}
	
	public static String SHA1(String data) {
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(data.getBytes("UTF-8"));
			byte[] tmp = md.digest();
			s = HEX.toHex(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public static String getKey(String seed) {//用户�?
		return SHA1(seed).substring(0, 6);
	}

	public static String getSeed() {//密码
		Random rand = new Random(System.currentTimeMillis());				
		return Integer.toString(1000 + rand.nextInt(10000000));
	}
	
	
	public static String md5(String data) {
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes());
			byte[] tmp = md.digest();
			s = HEX.toHex(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	
	
	/** 
     * BASE64 加密 
     * @param str 
     * @return 
     */  
    public static  String encryptBASE64(String str) {  
        if (str == null || str.length() == 0) {  
            return null;  
        }  
        try {  
            byte[] encode = str.getBytes("UTF-8");  
            return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");  
  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
      
    /** 
     * BASE64 解密
     * @param str 
     * @return 
     */  
    public static  String decryptBASE64(String str) {  
        if (str == null || str.length() == 0) {  
            return null;  
        }  
        try {  
            byte[] encode = str.getBytes("UTF-8");  
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");  
  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
  
        return null;  
    }  
	
    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data, DIGITS_LOWER));
       }
     
     //字节数组转16进制
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }    
	
	
	
	
	
}
