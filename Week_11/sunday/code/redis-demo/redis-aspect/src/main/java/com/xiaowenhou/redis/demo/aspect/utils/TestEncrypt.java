package com.xiaowenhou.redis.demo.aspect.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

public class TestEncrypt {

    public static void main(String[] args) {
        String algorithm = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";
       // algorithm = "RSA";
       // algorithm = "AES/ECB/PKCS5Padding";
        try {

            Cipher c16 = Cipher.getInstance(algorithm);

             KeyFactory keyFactory = KeyFactory.getInstance(c16.getAlgorithm());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
