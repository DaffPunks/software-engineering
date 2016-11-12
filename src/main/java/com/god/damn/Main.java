package com.god.damn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    public static void main(String[] args) {
        int port = 8080;
        /*
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // http://localhost:8080/func
        context.addServlet(new ServletHolder( new SrvltCalculator( ) ),"/func");
        */
        H2DataBaseManager dbm = new H2DataBaseManager("jdbc:h2:file:./db/SE", "sa", "");
        new AAA(dbm).execute(new Cli(args).parse());
    }
}
