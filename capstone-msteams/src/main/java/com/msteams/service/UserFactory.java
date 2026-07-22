package com.msteams.service;

import com.msteams.model.Student;
import com.msteams.model.Teacher;
import com.msteams.model.User;

// Factory Method pattern: centralizes creation of the correct User subtype based on role.
public class UserFactory {

    public static User createUser(int id, String name, String email, String password, String role) {
        if ("TEACHER".equals(role)) {
            return new Teacher(id, name, email, password);
        }
        return new Student(id, name, email, password);
    }
}
