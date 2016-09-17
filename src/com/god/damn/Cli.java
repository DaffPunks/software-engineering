package com.god.damn;

import java.text.*;
import java.util.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;


public class Cli {
    private String[] args = null;
    private Options options = new Options();

    final static int AUTHENTICATION    = 1;
    final static int AUTHORIZATION     = 2;
    final static int ACCOUNTING        = 3;

    private ArrayList<User> UserList;
    private ArrayList<Role> RoleList;

    public Cli(String[] args, ArrayList<User> UserList, ArrayList<Role> RoleList) {

        this.args = args;
        this.UserList = UserList;
        this.RoleList = RoleList;

        options.addOption("h", "help", false, "show help.");
        options.addOption("login", "login", true, "your login.");
        options.addOption("pass", "password", true, "your password.");
        options.addOption("res", "resource", true, "Requested resource.");
        options.addOption("role", "role", true, "What you what to do with this resource.");
        options.addOption("ds", "datestart", true, "Using start date. [DD-MM-YYYY]");
        options.addOption("de", "dateend", true, "Using end date. [DD-MM-YYYY]");
        options.addOption("val", "value", true, "Value");

    }

    public void parse() {
        String  login   = null;
        String  pass    = null;
        String  res     = null;
        String  role    = null;
        String  ds      = null;
        String  de      = null;
        String  val     = null;

        int inputResult = AUTHENTICATION;

        User currentUser = null;
        Role currentRole = null;
        int allowInput = 0;

        CommandLineParser parser = new BasicParser();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")){
                help();
            }

            if (cmd.hasOption("login")) {
                allowInput += 1;
                login = cmd.getOptionValue("login");
            }

            if (cmd.hasOption("pass")) {
                allowInput += 2;
                pass = cmd.getOptionValue("pass");
            }

            if (cmd.hasOption("res")) {
                allowInput += 4;
                res = cmd.getOptionValue("res");
            }

            if (cmd.hasOption("role")) {
                allowInput += 8;
                role = cmd.getOptionValue("role");
            }

            if (cmd.hasOption("ds")) {
                allowInput += 16;
                ds = cmd.getOptionValue("ds");
            }

            if (cmd.hasOption("de")) {
                allowInput += 32;
                de = cmd.getOptionValue("de");
            }

            if (cmd.hasOption("val")) {
                allowInput += 64;
                val = cmd.getOptionValue("val");
            }

        }
        catch (ParseException e) {
            System.err.println("Failed to parse command line properties");
            help();
        }

        switch (allowInput){
            case 3:
                inputResult = AUTHENTICATION;
                break;
            case 15:
                inputResult = AUTHORIZATION;
                break;
            case 127:
                inputResult = ACCOUNTING;
                break;
            default:
                System.err.println("Not enough attributes");
                System.exit(6);
        }

        currentUser = authentication(login, pass);
        if( inputResult == AUTHORIZATION || inputResult == ACCOUNTING )
            currentRole = authorization(res, role, currentUser);
        if ( inputResult == ACCOUNTING )
            accounting(ds, de, val, currentRole);

        System.out.println("Success");
        System.exit(0);

    }


    /**
     * Authenticate user in system
     *
     * @param login
     * @param password
     * @return User
     */
    private User authentication(String login, String password){
        boolean finded = false;
        User user = null;

        for (int i = 0; i < UserList.size() && !finded; i++) {
            if( UserList.get(i).Login.equals(login) ) {
                finded = true;
                user = UserList.get(i);
            }
        }

        if (!finded) {
            System.err.println("Unknown login");
            System.exit(1);
        }

        String checkHash = Secure.MD5(Secure.MD5(password) + user.Salt);

        if( !checkHash.equals(user.Pass) ){
            System.err.println("Wrong password");
            System.exit(2);
        }

        return user;
    }


    /**
     * Authorize User in system
     *
     * @param res resource
     * @param role role
     * @param user User, who trying authorize in system
     * @return Role
     */
    private Role authorization(String res, String role, User user){
        boolean access = false;
        Role currentRole = null;
        String[] AvailableRoles = {"1", "2", "4"};

        //Role is exist?
        if( !Arrays.asList(AvailableRoles).contains(role) ){
            System.err.println("Unknown role");
            System.exit(3);
        }

        for( Role roles : RoleList )
            if( roles.User_id == user.Id && roles.Name == Integer.parseInt(role) )
                if( haveAccess(res, roles.Resource) ) {
                    currentRole = roles;
                    access = true;
                }

        if (!access){
            System.err.println("Access denied.");
            System.exit(4);
        }

        return currentRole;
    }


    /**
     * Conduct accounting
     *
     * @param ds Date Start
     * @param de Date End
     * @param val Value of resource
     * @param role Role, who accounting in system
     * @return Accounting
     */
    private Accounting accounting(String ds, String de, String val, Role role){
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Date datestart  = null;
        Date dateend    = null;
        int  value      = 0;

        try {
            datestart   = format.parse(ds);
            dateend     = format.parse(de);
        }
        catch (java.text.ParseException e){
            System.out.println("Incorrect activity (invalid date)");
            System.exit(5);
        }

        try {
            value = Integer.parseInt(val);
        }
        catch (java.lang.NumberFormatException e){
            System.out.println("Incorrect activity (invalid value)");
            System.exit(5);
        }

        return new Accounting(role.Id, value, datestart, dateend);
    }


    /**
     * Print help
     */
    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(6);
    }


    /**
     * Check, have calling Role access to requested resource
     *
     * @param requestResource
     * @param roleResource
     * @return bool
     */
    private boolean haveAccess(String requestResource, String roleResource){
        if( requestResource.regionMatches(0, roleResource, 0, roleResource.length()) ){
            if( requestResource.length() == roleResource.length() ){
                return true; //same resource
            }
            else{
                return requestResource.charAt( roleResource.length() ) == '.'; //parent resource
            }
        }
        else
            return false;
    }
}
