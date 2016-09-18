package com.god.damn;
import java.security.SecureRandom;
import java.math.BigInteger;

import static com.god.damn.Secure.MD5;
import static com.god.damn.Secure.generateSalt;


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
        Salt = generateSalt();

        //Make hashed password
        Pass = MD5(MD5(pass) + Salt);
    }



}
