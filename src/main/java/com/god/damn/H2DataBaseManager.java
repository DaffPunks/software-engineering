package com.god.damn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

import static com.god.damn.Secure.MD5;
import static com.god.damn.Secure.generateSalt;


class H2DataBaseManager implements DatabaseManager {

    Logger log = LogManager.getLogger(H2DataBaseManager.class.getName());


    private Connection conn;

    private String connection;
    private String user;
    private String pass;

    public H2DataBaseManager(String connection, String user, String pass) {
        this.connection = connection;
        this.user = user;
        this.pass = pass;

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("H2 Driver not found: " + e);
            log.error("H2 Driver not found: " + e);
        }

        Flyway flyway = new Flyway();
        flyway.setDataSource(connection, user, pass);
        flyway.migrate();
    }

    @Override
    public User getUser(String login) {

        String getUser = "SELECT * FROM USERS WHERE LOGIN = '" + login + "';";
        User user;

        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(getUser);

            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.first();
                user = new User(
                        resultSet.getInt("ID"),
                        resultSet.getString("LOGIN"),
                        resultSet.getString("PASS"),
                        resultSet.getString("NAME"),
                        resultSet.getString("SALT")
                );
            }

            conn.close();
            return user;

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);

        }
        return null;

    }

    @Override
    public User getUser(int id) {
        String getUser = "SELECT * FROM USERS WHERE LOGIN = " + id;
        User user;

        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(getUser);

            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.first();
                user = new User(
                        resultSet.getInt("ID"),
                        resultSet.getString("LOGIN"),
                        resultSet.getString("PASS"),
                        resultSet.getString("NAME"),
                        resultSet.getString("SALT")
                );
            }

            conn.close();
            return user;

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);
        }
        return null;
    }

    @Override
    public Role getRole(int id) {
        String getRole = "SELECT * FROM ROLES WHERE ID = " + id;
        Role role;

        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(getRole);

            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                resultSet.first();
                role = new Role(
                        resultSet.getInt("ID"),
                        resultSet.getInt("USER_ID"),
                        resultSet.getString("PERMISSION"),
                        resultSet.getString("RESOURCE")
                );
            }

            conn.close();
            return role;

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception" + e);
        }
        return null;
    }

    @Override
    public ArrayList<Role> getRoleList(int id) {
        String getRoles = "SELECT * FROM ROLES WHERE USER_ID = " + id;
        ArrayList<Role> list = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(getRoles);

            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                while (resultSet.next()) {
                    list.add(new Role(
                            resultSet.getInt("ID"),
                            resultSet.getInt("USER_ID"),
                            resultSet.getString("PERMISSION"),
                            resultSet.getString("RESOURCE")
                    ));
                }
            }

            conn.close();
            return list;

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);
        }
        return null;
    }

    @Override
    public ArrayList<Role> getRoleList() {
        String getRoles = "SELECT * FROM ROLES";
        ArrayList<Role> list = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(getRoles);

            if (!resultSet.isBeforeFirst()) {
                return null;
            } else {
                while (resultSet.next()) {
                    list.add(new Role(
                            resultSet.getInt("ID"),
                            resultSet.getInt("USER_ID"),
                            resultSet.getString("PERMISSION"),
                            resultSet.getString("RESOURCE")
                    ));
                }
            }

            conn.close();
            return list;

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);
        }
        return null;
    }

    @Override
    public void insertUser(User user) {
        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            String salt = generateSalt();
            String insertUserSQL = "INSERT INTO USERS"
                    + "(LOGIN, PASS, NAME, SALT)" + "VALUES"
                    + "('" + user.Login + "', '" + MD5(MD5(user.Pass) + salt) + "', '" + user.Name + "', '" + salt + "');";

            Statement st = conn.createStatement();
            st.execute(insertUserSQL);

            conn.close();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5 Exception: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void insertRole(Role role) {
        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);

            String insertUserSQL = "INSERT INTO ROLES"
                    + "(PERMISSION, RESOURCE, USER_ID)" + "VALUES"
                    + "('" + role.Name + "', '" + role.Resource + "', '" + role.User_id + "');";

            Statement st = conn.createStatement();
            st.execute(insertUserSQL);

            conn.close();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void insertAccountingData(Accounting accounting) {

        try {
            conn = DriverManager.getConnection(this.connection, this.user, this.pass);


            String insertUserSQL = "INSERT INTO ACCOUNTING"
                    + "(ROLE_ID, AMOUNT, DATE_START, DATE_END)" + "VALUES"
                    + "('" + accounting.Role_Id + "', '" + accounting.Amount + "', '" + accounting.Date_st + "', '" + accounting.Date_end + "');";

            Statement st = conn.createStatement();
            st.execute(insertUserSQL);

            conn.close();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e);
            log.error("SQL Exception: " + e);
            e.printStackTrace();
        }
    }
}
