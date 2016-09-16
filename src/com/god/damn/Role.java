package com.god.damn;

/**
 * Created by 1 on 16.09.2016.
 */
public class Role {//role, aga
    public int Id;
    public int User_Id;
    public String Name;
    public String Resource; //хз, там не стринг, наверное, но пока пусть так

    public Role(int id, int user_Id, String name, String resource) {
        Id = id;
        User_Id = user_Id;
        Name = name;
        Resource = resource;
    }
}
