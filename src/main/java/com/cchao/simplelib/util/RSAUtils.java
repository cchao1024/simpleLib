package com.cchao.simplelib.util;

import android.util.Base64;

import com.cchao.simplelib.core.Logs;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @copy http://blog.csdn.net/bbld_/article/details/38777491
 * Created by tantom on 6/30/16.
 */
public class RSAUtils {

    private static String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC28VyLiVmY11lPBhuVdLJq/r7d" +
        "EWnJjW/A4DzdVHzLSkfrlI3FJ1s6YY1GPWlTZgYALV8c/xC6c3dR+ehlNw06VkBV" +
        "Xx9t23BEjnne3xV+SlDQiUbnMWvn9slXWl8LjpQDNZ9DRqY31AFTxS390f0LwVK2" +
        "UM9mzoJgRQFxpubhpQIDAQAB";


    private static PublicKey pkey = RSAUtils.loadPublicKey(PUCLIC_KEY);

    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            Logs.e(e);
            ExceptionCollect.logException(e);
            return null;
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, Base64.NO_WRAP);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Throwable e) {
            ExceptionCollect.logException(e);
            return null;
        }
    }
}
