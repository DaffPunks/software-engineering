package com.god.damn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    public static void main(String[] args) {

        H2DataBaseManager dbm = new H2DataBaseManager("jdbc:h2:file:./db/SE", "sa", "");
        new AAA(dbm).execute(new Cli(args).parse());
    }
}
