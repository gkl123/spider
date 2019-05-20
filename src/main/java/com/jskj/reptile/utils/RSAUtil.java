package com.jskj.reptile.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import com.alibaba.fastjson.JSONObject;


/**
 * @desc SHA1WithRSA算法
 * @author GKL
 * @Date 2019年5月20日
 */
public class RSAUtil {

    public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
    
    public static final String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKbMkPtzhAagbJrUgboUIbiYulcYpN9nO0H92VPnlo3Oij+C3DAyc5pa+6m4uTj4gK7GGU9uGhD3319/6jKH/pdhF1hN4/NKbyUdK0cvjIyQXWN9cnzIMwT1Ao541H5qO5L3THc4H41wgaeearyMeHj5lo19mfxWSm6TxzCS1NxPAgMBAAECgYBa+Z5mF3Y+KwexVxC+jjZqOZsA4Cck1mhCsRL1KY47IBjVVSmFfC8DVHaI9oDH6vo82T16zkGXWarSqrDxCK25vKg8LUNFYL/1jFVM7JMXltuOXRULcMs5GyRejuuQ8qqnaCT4vks5TKsdPfFLnpT4I1vDyKNszwsktbDkpFzdaQJBAPQGBZ6Z01fYCyEKew/9Vj56yEZJpZCHZYIj2mPXlZBnWjdUPY0uPIs6XMeuSVkF+VCLJWgat5nT+aT4nKlE29MCQQCu/EaI4Jyp7H1wc6pI02nJA6KYQcDEKNOtaiHe9Uanr0QcKUq2BbHAzN5UUK6H0RKiHqgWIg+kYLcWg9lHtVwVAkA+mTzfzr40jqtsMnAXTfnN1VmDDml2hgU4OVJQwoEPpjOydd3EVdQaM0KRxtfyCmJGkytpZVEsGQC1nUEy+wMZAkBOhoOYtUNYtfns1tsZ51XXkTwMG6XH4ZJshe5BQcy9P1fPU3BlktSb8tPq/nH0HQzBmARSOmLOn2BKSEzfczGpAkA5tZhoIzO7Ix6Rvym7r9F13j4ToiKgIh5figA+HaOCjDtq9ixcwvFXku7L45pTX/mZSRK6e2wzKmpYo+vQakE7";
    
    public static final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmzJD7c4QGoGya1IG6FCG4mLpXGKTfZztB/dlT55aNzoo/gtwwMnOaWvupuLk4+ICuxhlPbhoQ999ff+oyh/6XYRdYTePzSm8lHStHL4yMkF1jfXJ8yDME9QKOeNR+ajuS90x3OB+NcIGnnmq8jHh4+ZaNfZn8Vkpuk8cwktTcTwIDAQAB";
     
    /**
    * RSA签名
    * @param content 待签名数据
    * @param privateKey 商户私钥
    * @return 签名值
    */
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
 
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
 
            signature.initSign(priKey);
            signature.update(content.getBytes("utf-8"));
 
            byte[] signed = signature.sign();
             
            return Base64.getEncoder().encodeToString(signed);
        } catch(Exception e) {
            e.printStackTrace();
        }
         
        return null;
    }
     
    /**
    * RSA验签名检查
    * @param content 待签名数据
    * @param sign 签名值
    * @param publicKey 凤金服务器公钥
    * @return 布尔值
    */
    public static boolean verify(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.getDecoder().decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
 
         
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
         
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
         
            boolean bverify = signature.verify(Base64.getDecoder().decode(sign));
            return bverify;
             
        } catch(Exception e) {
            e.printStackTrace();
        }
         
        return false;
    }
     
    
    
    /**
    * 密文请求报文解密
    * @param content 密文
    * @param privateKey 商户私钥
    * @return 解密后的字符串
    */
    public static String decrypt(String content, String privateKey) throws Exception {
        PrivateKey prikey = getPrivateKey(privateKey);
 
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);
 
        InputStream ins = new ByteArrayInputStream(Base64.getDecoder().decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;
 
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;
 
            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }
 
            writer.write(cipher.doFinal(block));
        }
 
        return new String(writer.toByteArray(), "utf-8");
    }
    
    
    /**
    * 明文请求报文加密
    * @param content 明文
    * @param private_key 服务器公钥
    * @return 加密后的字符串
    */
    public static String encrypt(String content, String publicKey) throws Exception {
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.getDecoder().decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
 
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
 
        byte[] bytesEncrypt = cipher.doFinal(content.getBytes());//产生的是乱码,需要用Base64进行转码
        //Base64编码
        byte[] encodeBase64 = Base64.getEncoder().encode(bytesEncrypt);
        return new String(encodeBase64, "utf-8");
    }
 
     
    /**
    * 得到私钥
    * @param key 密钥字符串（经过base64编码）
    * @throws Exception
    */
    public static PrivateKey getPrivateKey(String key) throws Exception {
 
        byte[] keyBytes;
         
        keyBytes = Base64.getDecoder().decode(key);
         
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
         
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
         
        return privateKey;
    }
    
    
    
    public static void main(String[] args) {
    	JSONObject jsonStr = new JSONObject();
    	jsonStr.put("f", "getauthurl");
    	jsonStr.put("loanid", "{67A04134-4622-4742-9BB7-596CDEA25AB7}");
    	String contents = jsonStr.toJSONString();
    	System.out.println(jsonStr.toJSONString());
    	
		String sign = sign(jsonStr.toJSONString(), privateKey);
		System.out.println(sign);
		
		Boolean content = null;
		String encryptStr = null;
		String decryptStr = null;
		try {
			content = verify(jsonStr.toJSONString(), sign, publicKey);
			encryptStr = encrypt(contents, publicKey);
			decryptStr = decrypt(encryptStr, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(content);
		System.out.println("---------------密文字符串 :" + encryptStr);
		System.out.println("---------------明文字符串 :" + decryptStr);

		
	}
}


