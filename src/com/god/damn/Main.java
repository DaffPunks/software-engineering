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

        H2DataBaseManager dbm = new H2DataBaseManager("jdbc:h2:file:./db/SE", "sa", "");
        new AAA(dbm).execute(new Cli(args).parse());
    }
}
