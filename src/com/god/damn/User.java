package com.god.damn;

class User {
    public int      Id;
    public String   Login;
    public String   Pass;
    public String   Name;
    public String   Salt;

    public User(int id, String login, String pass, String name, String salt) {
        Id = id;
        Login = login;
        Pass = pass;
        Name = name;
        Salt = salt;
    }
}
