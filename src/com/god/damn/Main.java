package com.god.damn;


import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //Test users data
        ArrayList<User> UserList = new ArrayList<User>();
        UserList.add(new User(1, "John Doe", "jdoe", "sup3rpaZZ"));
        UserList.add(new User(2, "Jane Row", "jrow", "Qweqrty12"));

        //Test roles data
        ArrayList<Role> RoleList = new ArrayList<Role>();
        RoleList.add(new Role(1, 1, Role.READ, "a"));
        RoleList.add(new Role(2, 1, Role.WRITE, "a.b"));
        RoleList.add(new Role(3, 2, Role.EXECUTE, "a.b.c"));
        RoleList.add(new Role(4, 1, Role.EXECUTE, "a.bc"));

        new Cli(args, UserList, RoleList).parse();
    }
}
