package com.msteams.model;

/**
 * Student extends User. Matches role = 'STUDENT' in the users table.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;


    public Student(int userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }
}
