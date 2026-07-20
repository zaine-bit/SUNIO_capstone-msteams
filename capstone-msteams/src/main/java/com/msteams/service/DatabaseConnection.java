package com.msteams.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Single place responsible for opening a connection to the XAMPP/MySQL database.
 * If your MySQL root user has a password set in XAMPP, put it in PASSWORD below.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/msteams_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // default XAMPP password is blank

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
