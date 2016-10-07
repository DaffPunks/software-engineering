package com.god.damn;

import org.apache.commons.cli.*;

import java.util.BitSet;
import java.util.HashMap;


public class Cli {
    private String[] args = null;
    private Options options = new Options();

    public Cli(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("login", "login", true, "your login.");
        options.addOption("pass", "password", true, "your password.");
        options.addOption("res", "resource", true, "Requested resource.");
        options.addOption("role", "role", true, "What you what to do with this resource.");
        options.addOption("ds", "datestart", true, "Using start date. [DD-MM-YYYY]");
        options.addOption("de", "dateend", true, "Using end date. [DD-MM-YYYY]");
        options.addOption("val", "value", true, "Value");

    }

    public HashMap<String, String> parse() {
        BitSet allowInput = new BitSet(7);

        HashMap<String, String> Parameters = new HashMap<>();

        CommandLineParser parser = new BasicParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                help();
            }

            if (cmd.hasOption("login")) {
                allowInput.set(0);
                Parameters.put("login", cmd.getOptionValue("login"));
            }

            if (cmd.hasOption("pass")) {
                allowInput.set(1);
                Parameters.put("pass", cmd.getOptionValue("pass"));
            }

            if (cmd.hasOption("res")) {
                allowInput.set(2);
                Parameters.put("res", cmd.getOptionValue("res"));
            }

            if (cmd.hasOption("role")) {
                allowInput.set(3);
                Parameters.put("role", cmd.getOptionValue("role"));
            }

            if (cmd.hasOption("ds")) {
                allowInput.set(4);
                Parameters.put("ds", cmd.getOptionValue("ds"));
            }

            if (cmd.hasOption("de")) {
                allowInput.set(5);
                Parameters.put("de", cmd.getOptionValue("de"));
            }

            if (cmd.hasOption("val")) {
                allowInput.set(6);
                Parameters.put("val", cmd.getOptionValue("val"));
            }

        } catch (ParseException e) {
            System.err.println("Failed to parse command line properties");
            help();
        }
        try {
            switch (Long.toString(allowInput.toLongArray()[0], 2)) {
                case "11":
                case "1111":
                case "1111111":
                    break;
                default:
                    System.err.println("Not enough attributes");
                    help();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No attributes");
            help();
        }

        return Parameters;
    }

    /**
     * Print help
     */
    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(0);
    }

}
