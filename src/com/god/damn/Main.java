package com.god.damn;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.god.damn.Permissions.*;
import static com.god.damn.Secure.*;

public class Main {

    public static void main(String[] args) {
        //Test users data
        /*
        ArrayList<User> UserList = new ArrayList<>();
        try {
            String salt = generateSalt();
            UserList.add(new User(1, "jdoe", MD5(MD5("sup3rpaZZ") + salt), "John Doe", salt));
            salt = generateSalt();
            UserList.add(new User(2, "jrow", MD5(MD5("Qweqrty12") + salt), "Jane Row", salt));
        } catch (NoSuchAlgorithmException e){
            System.err.println("Get MD5 Hash failed");
        }

        //Test roles data
        ArrayList<Role> RoleList = new ArrayList<>();
        RoleList.add(new Role(1, 1, READ.name(), "a"));
        RoleList.add(new Role(2, 1, WRITE.name(), "a.b"));
        RoleList.add(new Role(3, 2, EXECUTE.name(), "a.b.c"));
        RoleList.add(new Role(4, 1, EXECUTE.name(), "a.bc"));

        new AAA(UserList,RoleList).execute(new Cli(args).parse());
        */

        String str = "1986-04-08";

        //LocalDate dateTime = LocalDate.parse(str, formatter);


        LocalDate today = LocalDate.now();
        //System.out.println(dateTime);

        H2DataBaseManager dbm = new H2DataBaseManager("jdbc:h2:file:./db/SE", "sa", "");
        new AAA(dbm).execute(new Cli(args).parse());

        System.out.println("Success");
        System.exit(0);
    }
}
