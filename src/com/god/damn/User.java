package com.god.damn;
import java.security.SecureRandom;
import java.math.BigInteger;


class User {
    public int      Id;
    public String   Login;
    public String   Pass;
    public String   Name;
    public String   Salt;

    public User(int id, String name, String login, String pass) {
        Id      = id;
        Login   = login;
        Name    = name;

        //Generate salt
        SecureRandom random = new SecureRandom();
        String salt = new BigInteger(130, random).toString(32);

        Salt = salt;
        //Make hashed password
        Pass = MD5(MD5(pass) + salt);
    }

    /**
     *  Encrypt string to MD5 Hash
     *
     * @param message That you want to encrypt
     * @return hash string
     */
    public static String MD5(String message) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(message.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        }
        catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
