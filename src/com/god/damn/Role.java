package com.god.damn;


public class Role {
    final static int READ       = 1;
    final static int WRITE      = 2;
    final static int EXECUTE    = 4;

    public int      Id;
    public int      User_id;
    public int      Name;
    public String   Resource;

    public Role(int id, int user_id, int name, String resource) {
        Id       = id;
        User_id  = user_id;
        Name     = name;
        Resource = resource;
    }
}
