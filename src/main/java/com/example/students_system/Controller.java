package com.example.students_system;

import com.example.students_system.model.Datasource;
import com.example.students_system.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private TextField emailLogin;
    @FXML
    private PasswordField passwordLogin;
    @FXML
    private TextField firstNameRegister;
    @FXML
    private TextField lastNameRegister;
    @FXML
    private TextField ageRegister;
    @FXML
    private TextField emailRegister;
    @FXML
    private PasswordField passwordRegister;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;
    @FXML
    private TableView studentsTable;
    @FXML
    private TextField editAge;
    @FXML
    private TextField txtFName;
    @FXML
    private TextField txtLName;



    @FXML
    public void registerStd() {
       boolean result= Datasource.getInstance().register(firstNameRegister.getText(),
                lastNameRegister.getText(),ageRegister.getText(),emailRegister.getText(),passwordRegister.getText());
       if(result){
           btnRegister.setOnMouseClicked((event) -> {
               try {
                   FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("students.fxml"));
                   Parent root= fxmlLoader.load();
                   Controller controller= fxmlLoader.getController();
                   controller.listStudents();
                   Scene scene = new Scene(root, 600, 400);
                   Stage stage = new Stage();
                   stage.setTitle("Students page");
                   stage.setScene(scene);
                   stage.show();
               } catch (IOException e) {
                   System.out.println("Failed to create new Window."+ e) ;
               }
           });
       }
    }

    @FXML
    public void loginStd() {
       boolean result= Datasource.getInstance().login(emailLogin.getText(),passwordLogin.getText());
       if(result) {
           btnLogin.setOnMouseClicked((event) -> {
               try {
                   FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("students.fxml"));
                   Parent root= fxmlLoader.load();
                   Controller controller= fxmlLoader.getController();
                   controller.listStudents();
                   Scene scene = new Scene(root, 600, 400);
                   Stage stage = new Stage();
                   stage.setTitle("Students page");
                   stage.setScene(scene);
                   stage.show();
               } catch (IOException e) {
                   System.out.println("Failed to create new Window."+ e) ;
               }
           });
       }
    }


    @FXML
    public void listStudents() {
        Task<ObservableList<Student>> task = new GetAllStudentsTask();
        studentsTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();
    }
    @FXML
    public void updateStd() {
        final Student student = (Student) studentsTable.getSelectionModel().getSelectedItem();
        if(student == null) {
            System.out.println("NO STUDENT SELECTED");
            return;
        }
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().updateStd(student.getEmail(), editAge.getText(),
                        txtFName.getText(),txtLName.getText());
            }
        };
        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                student.setAge(Integer.parseInt(editAge.getText()));
                student.setFirst_name(txtFName.getText());
                student.setLast_name(txtLName.getText());
                studentsTable.refresh();
            }
        });
        new Thread(task).start();
    }
    @FXML
    public void deleteStd() {
        final Student student = (Student) studentsTable.getSelectionModel().getSelectedItem();
        if(student == null) {
            System.out.println("NO STUDENT SELECTED");
            return;
        }
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().delete(student.getEmail());
            }
        };
        task.setOnSucceeded(e -> {
            if(task.valueProperty().get()) {
                studentsTable.getItems().remove(student);
                studentsTable.refresh();
            }
        });
        new Thread(task).start();
    }
@FXML
    public void fillData(MouseEvent mouseEvent) {
        final Student student = (Student) studentsTable.getSelectionModel().getSelectedItem();
        if(student==null){
            System.out.println("select any student");
        }else{
            studentsTable.setOnMouseClicked((event)->{ editAge.setText(String.valueOf(student.getAge()));
                txtFName.setText(student.getFirst_name());
                txtLName.setText(student.getLast_name());
            });
        }

    }
}
class GetAllStudentsTask extends Task {

    @Override
    public ObservableList<Student> call()  {
        return FXCollections.observableArrayList
                (Datasource.getInstance().getQueryStudents());
    }
}