package com.god.damn;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

//import static com.god.damn.AAA.Ax3.AUTHENTICATE;
import static com.god.damn.Permisions.*;
import static com.god.damn.Secure.MD5;
//import static com.god.damn.AAA.ExitCodes.*;

import static jdk.nashorn.tools.Shell.SUCCESS;

public class AAA {
//    private final int AUTHENTICATE = 1;
//    private final int AUTHORIZATION = 2;
//    private final int ACCOUNTING = 3;
private enum Ax3{AUTHENTICATE, AUTHORIZATION, ACCOUNTING};

//    private final int SUCCESS = 0;
//    private final int WRONGLOGIN = 1;
//    private final int WRONGPASS = 2;
//    private final int UNKNOWNROLE = 3;
//    private final int FORBIDDEN = 4;
//    private final int INCORRECTACTIVITY = 5;
   private enum ExitCodes{SUCCESS, WRONGLOGIN, WRONGPASS, UNKNOWNROLE, FORBIDDEN, INCORRECTACTIVITY};
    //if(*err.*==.SUCCESS) {выходит ехит код и сообщение об ошибке}

    private ArrayList<User> UserList;
    private ArrayList<Role> RoleList;

    private User currentUser;
    private Role currentRole;
    private Accounting currentAccount;

    public AAA(ArrayList<User> UserList, ArrayList<Role> RoleList) {
        this.UserList = UserList;
        this.RoleList = RoleList;
    }

    public void execute(HashMap<String, String> Parameters) {
        int inputResult = 0;

        switch (Parameters.size()) {
            case 2:
                inputResult = Ax3.AUTHENTICATE.ordinal()+1;
                break;
            case 4:
                inputResult = Ax3.AUTHORIZATION.ordinal()+1;
                break;
            case 7:
                inputResult = Ax3.ACCOUNTING.ordinal()+1;
                break;
            default: {
                System.err.println("Wrong Parameters");
                //final int EC = SUCCESS.ordinal();
                System.exit(ExitCodes.SUCCESS.ordinal());
            }
        }

        String login = Parameters.get("login");
        String pass = Parameters.get("pass");
        String res = Parameters.get("res");
        String role = Parameters.get("role");
        String ds = Parameters.get("ds");
        String de = Parameters.get("de");
        String val = Parameters.get("val");

        try {
            switch (inputResult) {
                case 1/*Ax3.AUTHENTICATE.ordinal()+1*/:        //а че если сюда тупо числа поставить? или это чему то противоречит?
                    authentication(login, pass);
                    break;
                case 2/*Ax3.AUTHORIZATION.ordinal()+1*/:
                    authentication(login, pass);
                    authorization(res, role, currentUser);
                    break;
                case 3/*Ax3.ACCOUNTING.ordinal()+1*/:   //я мб и мудак, что так сделал, но оно не компилилось
                    authentication(login, pass);
                    authorization(res, role, currentUser);
                    accounting(ds, de, val, currentRole);
                    break;
            }
        }catch(java.security.NoSuchAlgorithmException e) {
            System.out.println("Wrong MD5 Hashing");
            e.printStackTrace();
        }
    }

    /**
     * Authenticate user in system
     *
     * @param login
     * @param password
     * @return User
     */
    private void authentication(String login, String password) throws java.security.NoSuchAlgorithmException {
        boolean finded = false;
        User user = null;

        for (int i = 0; i < UserList.size() && !finded; i++) {
            if (UserList.get(i).Login.equals(login)) {
                finded = true;
                user = UserList.get(i);
            }
        }

        if (!finded) {
            System.err.println("Unknown login");
            System.exit(ExitCodes.WRONGLOGIN.ordinal());
        }

        String checkHash = MD5(MD5(password) + user.Salt);

        if (!checkHash.equals(user.Pass)) {
            System.err.println("Wrong password");
            System.exit(ExitCodes.WRONGPASS.ordinal());
        }
        this.currentUser = user;
    }

    /**
     * Authorize User in system
     *
     * @param res  resource
     * @param role role
     * @param user User, who trying authorize in system
     * @return Role
     */
    private void authorization(String res, String role, User user) {
        boolean access = false;

        String read = Integer.toString(READ.code());
        String write = Integer.toString(WRITE.code());
        String execute = Integer.toString(EXECUTE.code());

        String[] AvailableRoles = {read, write, execute};

        //Role is exist?
        if (!Arrays.asList(AvailableRoles).contains(role)) {
            System.err.println("Unknown role");
            System.exit(ExitCodes.UNKNOWNROLE.ordinal());
        }

        for (Role roles : RoleList)
            if (roles.User_id == user.Id && roles.Name == Integer.parseInt(role))
                if (haveAccess(res, roles.Resource)) {
                    this.currentRole = roles;
                    access = true;
                }

        if (!access) {
            System.err.println("Access denied.");
            System.exit(ExitCodes.FORBIDDEN.ordinal());
        }
    }

    /**
     * Conduct accounting
     *
     * @param ds   Date Start
     * @param de   Date End
     * @param val  Value of resource
     * @param role Role, who accounting in system
     * @return Accounting
     */
    private void accounting(String ds, String de, String val, Role role) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Date datestart = null;
        Date dateend = null;
        int value = 0;

        try {
            datestart = format.parse(ds);
            dateend = format.parse(de);
        } catch (java.text.ParseException e) {
            System.out.println("Incorrect activity (invalid date)");
            System.exit(ExitCodes.INCORRECTACTIVITY.ordinal());
        }

        try {
            value = Integer.parseInt(val);
        } catch (java.lang.NumberFormatException e) {
            System.out.println("Incorrect activity (invalid value)");
            System.exit(ExitCodes.INCORRECTACTIVITY.ordinal());
        }

        currentAccount = new Accounting(role.Id, value, datestart, dateend);
    }

    public Accounting getCurrentAccount() {
        return currentAccount;
    }

    /**
     * Check, have calling Role access to requested resource
     *
     * @param requestResource
     * @param roleResource
     * @return bool
     */
    private boolean haveAccess(String requestResource, String roleResource) {
        if (requestResource.regionMatches(0, roleResource, 0, roleResource.length())) {     //equals begin of requestRsrc with whole roleRsrc
            if (requestResource.length() == roleResource.length()) {    //if Resources equals
                return true; //same resource
            } else {
                return requestResource.charAt(roleResource.length()) == '.';  //next symbol should be '.' or false
            }
        } else
            return false;
    }
}
