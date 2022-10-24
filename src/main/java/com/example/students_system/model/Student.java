package com.example.students_system.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {

    private int st_id;
    private String first_name;
    private String last_name;
    private int age;
    private String email;
    private String password;

    public Student() {
    }

    public int getSt_id() {
        return st_id;
    }

    public Student setSt_id(int st_id) {
        this.st_id = st_id;
        return this;
    }

    public String getFirst_name() {
        return first_name;
    }

    public Student setFirst_name(String first_name) {
        this.first_name = first_name;
        return this;
    }

    public String getLast_name() {
        return last_name;
    }

    public Student setLast_name(String last_name) {
        this.last_name = last_name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Student setAge(int age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Student setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this;
    }
}
