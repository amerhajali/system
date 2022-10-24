package com.example.students_system.model;

import javafx.concurrent.Task;
import javafx.fxml.FXML;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5432/students_db";
    String user="postgres";
    String pass="0000";

    private static final String INSERT_STUDENT_QUERY = "INSERT INTO student" +
            "  (first_name ,last_name, age, email, password) VALUES " +
            " (?, ?, ?, ?, ?)";
    private static final String QUERY_SELECT_LOGIN = "SELECT email,password FROM student" +
            "  WHERE email = ? ";
    public static final String QUERY_SELECT_STUDENTS="SELECT * FROM student";
    private static final String QUERY_UPDATE_STUDENT = "UPDATE student SET" +
            "  (age, first_name, last_name) = (?, ?, ?)" +
            " WHERE email =?;";
    public static final String QUERY_DELETE= "DELETE FROM student"+
            " WHERE email = ?;";


    private Connection conn;

    private PreparedStatement queryRegisterStudent;
    private PreparedStatement queryLogin;
    private PreparedStatement queryStudents;
    private PreparedStatement queryUpdate;
    private PreparedStatement queryDelete;

    private static Datasource instance = new Datasource();

    private Datasource() {

    }
    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING,user,pass);
            queryRegisterStudent= conn.prepareStatement(INSERT_STUDENT_QUERY);
            queryLogin= conn.prepareStatement(QUERY_SELECT_LOGIN);
            queryStudents= conn.prepareStatement(QUERY_SELECT_STUDENTS);
            queryUpdate= conn.prepareStatement(QUERY_UPDATE_STUDENT);
            queryDelete= conn.prepareStatement(QUERY_DELETE);

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (queryRegisterStudent != null) {
                queryRegisterStudent.close();
            }
            if (queryLogin != null) {
                queryLogin.close();
            }
            if (queryStudents != null) {
                queryStudents.close();
            }
            if (queryUpdate != null) {
                queryUpdate.close();
            }
            if (queryDelete != null) {
                queryDelete.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public boolean register(String fName, String lName, String age, String email,String pass){
        try {
            queryRegisterStudent.setString(1,fName);
            queryRegisterStudent.setString(2,lName);
            queryRegisterStudent.setInt(3,Integer.parseInt(age));
            queryRegisterStudent.setString(4,email);
            queryRegisterStudent.setString(5,pass);
            queryRegisterStudent.executeUpdate();
            System.out.println("successfully");
            return true;

        } catch (SQLException e) {

            System.out.println("failed inserted "+e.getMessage());
            return false;
        }
    }
    public boolean login(String email, String pass){
        try {
            queryLogin.setString(1,email);
            ResultSet results = queryLogin.executeQuery();
            if (results.next()){
                System.out.println(results.getString("password")+pass);
                if(results.getString("password").equals(pass)){
                    System.out.println("successfully login");
                    return true;
                }else {
                    System.out.println("password false");
                    return false;
                }
            }else{
                System.out.println("Email not exists");
                return false;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return false;

        }
    }
    public List<Student> getQueryStudents(){
        try(ResultSet results=queryStudents.executeQuery()){

            List<Student> students = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Student student=new Student();
                student.setSt_id(results.getInt("st_id"));
                student.setFirst_name(results.getString("first_name"));
                student.setLast_name(results.getString("last_name"));
                student.setAge(results.getInt("age"));
                student.setEmail(results.getString("email"));
                student.setPassword(results.getString("password"));
                students.add(student);
            }
            return students;
        }catch (SQLException E){
            System.out.println("failed"+ E.getMessage());
            return null;
        }

    }


    public Boolean updateStd(String email, String age, String fname, String lname) {
        try{
            queryUpdate.setInt(1, Integer.parseInt(age));
            queryUpdate.setString(2,fname);
            queryUpdate.setString(3,lname);
            queryUpdate.setString(4,email);
            int res=queryUpdate.executeUpdate();
            if(res==1){
                System.out.println("success");
                return true;
            }else return false;

        } catch (SQLException e) {
            System.out.println("failed "+e.getMessage());
            return false;
        }
    }

    public Boolean delete(String email) {
        try {
            queryDelete.setString(1,email);
            int res=queryDelete.executeUpdate();
            if(res==1){
                System.out.println("success");
                return true;}
            else{
                System.out.println("failed d");
                return false;}
        } catch (SQLException e) {
            System.out.println("failed "+e.getMessage());
            return false;
        }
    }
}
