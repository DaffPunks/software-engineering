package com.god.damn;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.*;

import static java.util.Objects.hash;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();

    public Cli(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("login", "login", true, "your login.");
        options.addOption("pass", "password", true, "your password.");
        options.addOption("res", "resource", true, "Requested resource.");
        options.addOption("role", "role", true, "What you what to do with this resource.");
        options.addOption("ds", "datestart", true, "Using start date.");
        options.addOption("de", "dateend", true, "Using end date.");
        options.addOption("val", "value", true, "Value");

    }

    public void parse() {
        int allowInput = 0;
        CommandLineParser parser = new BasicParser();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")){
                help();
            }

            if (cmd.hasOption("login")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 1;
                }
            }
            if (cmd.hasOption("pass")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 2;
                }

            }
            if (cmd.hasOption("res")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 4;
                }

            }
            if (cmd.hasOption("role")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 8;
                }

            }
            if (cmd.hasOption("ds")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 16;
                }

            }
            if (cmd.hasOption("de")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 32;
                }

            }
            if (cmd.hasOption("val")) {
                String value = cmd.getOptionValue("login");
                if(!value.isEmpty()){
                    allowInput += 64;
                }

            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse comand line properties");
            help();
        }

        switch (allowInput){
            case 3:
            case 15:
            case 127:
                break;
            default:
                log.log(Level.SEVERE, "Not enough attributes");
        }
        System.out.println(allowInput);

    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(0);
    }
}
