package com.god.damn;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import static com.god.damn.AAA.Ax3.*;
import static com.god.damn.Secure.MD5;


public class AAA {

    Logger log = LogManager.getLogger(AAA.class.getName());

    public enum Ax3 {AUTHENTICATE, AUTHORIZATION, ACCOUNTING}

    private enum ExitCodes {SUCCESS, WRONGLOGIN, WRONGPASS, UNKNOWNROLE, FORBIDDEN, INCORRECTACTIVITY}

    private DatabaseManager databaseManager;

    private int currentUserID;
    private int currentRoleID;

    public AAA(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
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
                log.error("Wrong Parameters: " + Parameters.size());
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
                    authorization(res, role);
                    break;
                case ACCOUNTING:
                    authentication(login, pass);
                    authorization(res, role);
                    accounting(ds, de, val);
                    break;
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Wrong MD5 Hashing");
            log.error("Wrong MD5 Hashing");
            e.printStackTrace();
        }

        System.out.println("Success");
        log.info("Success:" +
                " Login: "  + login +
                " Pass: "   + pass +
                " Res: "    + res +
                " Role: "   + role +
                " Ds: "     + ds +
                " De: "     + de +
                " Val: "      + val
        );
        System.exit(0);
    }

    /**
     * Authenticate user in system
     */
    private void authentication(String login, String password) throws java.security.NoSuchAlgorithmException {
        User user = databaseManager.getUser(login);

        if (user == null) {
            System.err.println("Unknown login " + login);
            log.error("Unknown login " + login);
            System.exit(ExitCodes.WRONGLOGIN.ordinal());
        }

        String checkHash = MD5(MD5(password) + user.Salt);

        if (!checkHash.equals(user.Pass)) {
            System.err.println("Wrong password");
            log.error("Wrong password " + login + " Pass: " + password);
            System.exit(ExitCodes.WRONGPASS.ordinal());
        }
        this.currentUserID = user.Id;
    }

    /**
     * Authorize User in system
     */
    private void authorization(String res, String role) {
        boolean access = false;

        ArrayList<String> AvailableRoles = new ArrayList<>();
        for (Permissions p : Permissions.values()) {
            AvailableRoles.add(p.name());
        }

        //Role is exist?
        if (!AvailableRoles.contains(role)) {
            System.err.println("Unknown role");
            log.error("Unknown role: " + role);
            System.exit(ExitCodes.UNKNOWNROLE.ordinal());
        }

        ArrayList<Role> roleList = databaseManager.getRoleList(currentUserID);

        for (Role userRole : roleList) {
            if (userRole.Name.equals(role)) {
                if (haveAccess(res, userRole.Resource)) {
                    this.currentRoleID = userRole.Id;
                    access = true;
                }
            }
        }

        if (!access) {
            System.err.println("Access denied.");
            log.error("Access denied.");
            System.exit(ExitCodes.FORBIDDEN.ordinal());
        }
    }

    /**
     * Conduct accounting
     */
    private void accounting(String ds, String de, String val) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate dateStart = null;
        LocalDate dateEnd = null;

        int value = 0;

        try {
            dateStart = LocalDate.parse(ds, formatter);
            dateEnd = LocalDate.parse(de, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Incorrect activity (invalid date)" + e);
            log.error("Incorrect activity (invalid date) startdate: " + ds + " enddate: " + de);
            System.exit(ExitCodes.INCORRECTACTIVITY.ordinal());
        }

        try {
            value = Integer.parseInt(val);
        } catch (java.lang.NumberFormatException e) {
            System.out.println("Incorrect activity (invalid value)");
            log.error("Incorrect activity (invalid value): " + val);
            System.exit(ExitCodes.INCORRECTACTIVITY.ordinal());
        }

        databaseManager.insertAccountingData(new Accounting(currentRoleID, value, dateStart, dateEnd));

    }

    /**
     * Check, have calling Role access to requested resource
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
