package com.god.damn;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import static com.god.damn.AAA.Ax3.*;
import static com.god.damn.Permissions.*;
import static com.god.damn.Secure.MD5;


public class AAA {
    public enum Ax3{AUTHENTICATE, AUTHORIZATION, ACCOUNTING};

    private enum ExitCodes{SUCCESS, WRONGLOGIN, WRONGPASS, UNKNOWNROLE, FORBIDDEN, INCORRECTACTIVITY};

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
        Ax3 inputResult = null;

        switch (Parameters.size()) {
            case 2:
                inputResult = AUTHENTICATE;
                break;
            case 4:
                inputResult = AUTHORIZATION;
                break;
            case 7:
                inputResult = ACCOUNTING;
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
                case AUTHENTICATE:
                    authentication(login, pass);
                    break;
                case AUTHORIZATION:
                    authentication(login, pass);
                    authorization(res, role, currentUser);
                    break;
                case ACCOUNTING:
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

        ArrayList<String> AvailableRoles = new ArrayList<>();
        for(Permissions p : Permissions.values()) {
            AvailableRoles.add(p.name());
            System.out.println(p.name());
        }

        //Role is exist?
        if (!AvailableRoles.contains(role)) {
            System.err.println("Unknown role");
            System.exit(ExitCodes.UNKNOWNROLE.ordinal());
        }

        for (Role roles : RoleList) {
            if (roles.User_id == user.Id && roles.Name.equals(role)) {
                if (haveAccess(res, roles.Resource)) {
                    this.currentRole = roles;
                    access = true;
                }
            }
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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
