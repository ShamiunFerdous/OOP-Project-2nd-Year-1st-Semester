package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connnectionDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/admin_db", "root", "amivatkhai12345678");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
