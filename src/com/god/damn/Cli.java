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
        options.addOption("l", "login", true, "your login.");
        options.addOption("p", "password", true, "your password.");

    }

    public void parse() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h"))
                help();

            if (cmd.hasOption("l")) {
                System.out.println("Login is: " + cmd.getOptionValue("l"));

            }
            if (cmd.hasOption("p")) {
                System.out.println("Pass is: " + cmd.getOptionValue("l"));

            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse comand line properties");
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();

        formater.printHelp("Main", options);
        System.exit(0);
    }
}
