package com.god.damn;

import java.util.ArrayList;

public interface DatabaseManager {

    /**
     * Get user by Login
     */
    User getUser(String login);

    /**
     * Get user by ID
     */
    User getUser(int id);

    /**
     * Get role by ID
     */
    Role getRole(int id);

    /**
     * Get role by USER_ID
     */
    ArrayList<Role> getRoleList(int user_id);

    /**
     * Get all roles
     */
    ArrayList<Role> getRoleList();

    /** INSERTS */
    void insertUser(User user);

    void insertRole(Role role);

    void insertAccountingData(Accounting accounting);
}
