package com.god.damn;
import java.security.SecureRandom;
import java.math.BigInteger;
/**
 * Created by 1 on 16.09.2016.
 */
public class User {
    public int Id;
    public String Login;
    public String Pass;
    public String Name;

    public User( int id, String login, String pass,String name) {

        Id = id;
        Login = login;
        Name = name;
        // Pass = pass;
        SecureRandom random = new SecureRandom();
        String Salt = new BigInteger(130, random).toString(32);
        Pass=MD5(MD5(pass)+Salt);
    }
    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }



}
