package com.msteams.model;

import java.io.Serializable;

/**
 * Abstract base class shared by Student and Teacher, matching the "User" table.
 * userId is 0 until the row is actually inserted and the DB assigns a real id.
 * Implements Serializable so a logged-in User can be written to the session file.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private final String name;
    private final String email;
    private final String password;

    public User(int userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return name;
    }
}
