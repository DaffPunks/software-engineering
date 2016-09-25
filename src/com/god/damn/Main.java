package com.god.damn;

import java.util.ArrayList;

import static com.god.damn.Permisions.*;

public class Main {

    public static void main(String[] args) {
        //Test users data
        ArrayList<User> UserList = new ArrayList<>();
        UserList.add(new User(1, "John Doe", "jdoe", "sup3rpaZZ"));
        UserList.add(new User(2, "Jane Row", "jrow", "Qweqrty12"));

        //Test roles data
        ArrayList<Role> RoleList = new ArrayList<>();
        RoleList.add(new Role(1, 1, READ.code(), "a"));
        RoleList.add(new Role(2, 1, WRITE.code(), "a.b"));
        RoleList.add(new Role(3, 2, EXECUTE.code(), "a.b.c"));
        RoleList.add(new Role(4, 1, EXECUTE.code(), "a.bc"));

        new AAA(UserList,RoleList).execute(new Cli(args).parse());

        System.out.println("Success");
        System.exit(0);
    }
}
