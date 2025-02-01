package com.example.demo;

import java.util.Date;

public class EmployeeData {
    private String username;
    private String password;
    private String fullName;
    private String gender;
    private Date date;

    EmployeeData(String username, String password, String fullName, String gender, Date date) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.gender = gender;
        this.date = date;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFullName() {
        return fullName;
    }
    public String getGender() {
        return gender;
    }
    public Date getDate() {
        return date;
    }
}
