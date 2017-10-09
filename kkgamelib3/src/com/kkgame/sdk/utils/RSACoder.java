package com.kkgame.sdk.utils;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import javax.crypto.Cipher;



public class RSACoder {
  //非对称密钥算法  
    public static final String KEY_ALGORITHM="RSA";  
    /** 
     * 密钥长度，DH算法的默认密钥长度是1024 
     * 密钥长度必须是64的倍数，在512到65536位之间 
     * */  
    @SuppressWarnings("unused")
    private static final int KEY_SIZE=1024;  
    //-----BEGIN PUBLIC KEY----- -----END PUBLIC KEY-----
    //公钥  
    public static final String PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDT/H/l3HtiIFMJpBSl7rtzsA7hH2KtWIISPwQCsBWT61TZOmDTikRjntfixueqyAXMMSGDory7S4ChwQQWgEERQGp722ilo52JzquSWoJY6L+qbYXmobyisxhwIf7Gu6k4csK27m3V8MDdB3mAv15CVPI4sAEpCXHyvSBP0A9zQIDAQAB";  
      
    //私钥  
    private static final String PRIVATE_KEY="sdfdf";
    private static String modulus = "C34FF1FF9771ED88814C26905297BAEDCEC03B847D8AB5620848FC100AC0564FAD5364E9834E29118E7B5F8B1B9EAB201730C4860E8AF2ED2E028704105A01044501A9EF6DA2968E76273AAE496A0963A2FEA9B6179A86F28ACC61C087FB1AEEA4E1CB0ADBB9B757C303741DE602FD790953C8E2C004A425C7CAF4813F403DCD"; 
    //getpublickey
    public static PublicKey getPublicKey(String modulus,String publicExponent) throws Exception {   
        BigInteger m = new BigInteger(modulus, 16);
        BigInteger e = new BigInteger(publicExponent);   
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m,e);   
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");     
        PublicKey publicKey = keyFactory.generatePublic(keySpec);   
        return publicKey;   
  }
    
    
    /** 
     * 私钥加密 
     * @param data待加密数据 
     * @param key 密钥 
     * @return byte[] 加密数据 
     * */  
    public static byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception{  
          
        //取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);  
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);  
        //生成私钥  
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);  
        //数据加密  
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        return cipher.doFinal(data);  
    }  
    
    /** 
     * 公钥加密 
     * @param data待加密数据 
     * @param key 密钥 
     * @return byte[] 加密数据 
     * */  
    public static byte[] encryptByPublicKey(byte[] data) throws Exception{  
        try {           
            PublicKey publicKey = getPublicKey(modulus, "65537");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
     
            //加密
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024  
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小  
            int leavedSize = data.length % blockSize;  
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1  
                    : data.length / blockSize;  
            byte[] raw = new byte[outputSize * blocksSize];  
            int i = 0;  
            while (data.length - i * blockSize > 0) {  
                if (data.length - i * blockSize > blockSize)  
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i  
                            * outputSize);  
                else  
                    cipher.doFinal(data, i * blockSize, data.length - i  
                            * blockSize, raw, i * outputSize);   
                i++;  
            }  
            return raw;  
        } catch (Exception e) {  
            throw new Exception(e.getMessage());  
        }
        
    }  
    /** 
     * 私钥解密 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密数据 
     * */  
    public static byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{  
        //取得私钥  
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);  
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);  
        //生成私钥  
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec);  
        //数据解密  
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        return cipher.doFinal(data);  
    }  
    
    /** 
     * 公钥解密 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密数据 
     * */  
    public static byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception{  
          
        //实例化密钥工厂  
        KeyFactory keyFactory=KeyFactory.getInstance(KEY_ALGORITHM);  
        //初始化公钥  
        //密钥材料转换  
        X509EncodedKeySpec x509KeySpec=new X509EncodedKeySpec(key);
        //产生公钥  
        PublicKey pubKey=keyFactory.generatePublic(x509KeySpec);  
        //数据解密  
        Cipher cipher=Cipher.getInstance(keyFactory.getAlgorithm());  
        cipher.init(Cipher.DECRYPT_MODE, pubKey);  
        return cipher.doFinal(data);  
    }  
    
    /** 
     * 取得私钥 
     * @param keyMap 密钥map 
     * @return byte[] 私钥 
     * */  
    public static byte[] getPrivateKey(Map<String,Object> keyMap){  
        Key key=(Key)keyMap.get(PRIVATE_KEY);  
        return key.getEncoded();  
    }  
    /** 
     * 取得公钥 
     * @param keyMap 密钥map 
     * @return byte[] 公钥 
     * */  
    public static byte[] getPublicKey(Map<String,Object> keyMap) throws Exception{  
        Key key=(Key) keyMap.get(PUBLIC_KEY); 
        return key.getEncoded();  
    }  
}
