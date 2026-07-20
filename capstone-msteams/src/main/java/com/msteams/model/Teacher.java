package com.msteams.model;

/**
 * Teacher extends User. Matches role = 'TEACHER' in the users table.
 */
public class Teacher extends User {
    private static final long serialVersionUID = 1L;


    public Teacher(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }
}
