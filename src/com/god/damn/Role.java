package com.god.damn;


public class Role {
    public int Id;
    public int User_id;
    public String Name;
    public String Resource;

    public Role(int id, int user_id, String name, String resource) {
        Id = id;
        User_id = user_id;
        Name = name;
        Resource = resource;
    }
}
