package com.god.damn;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by User on 17.09.2016.
 */
public class Secure {

    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
    /**
     *  Encrypt string to MD5 Hash
     *
     * @param message That you want to encrypt
     * @return hash string
     */
    public static String MD5(String message)throws java.security.NoSuchAlgorithmException {
         {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(message.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        }

    }
}
