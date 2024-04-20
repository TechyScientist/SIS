package com.johnnyconsole.sis.screens.admin;

import com.johnnyconsole.sis.screens.MainMenuScreen;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static javafx.geometry.HPos.*;

public class AddUserScreen extends Application {

    public AddUserScreen() {
        start(new Stage());
    }

    @Override public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("Add User");
        TextField tfUsername = new TextField(),
                tfFirstName = new TextField(),
                tfLastName = new TextField(),
                tfStudentNumber = new TextField(),
                tfStudentProgram = new TextField();
        PasswordField pfPassword = new PasswordField(),
                pfConfirm = new PasswordField();
        ComboBox<String> cbUserType = new ComboBox<>(),
                cbStudentStatus = new ComboBox<>();
        Button addUser = new Button("Add User"),
                cancel = new Button("Cancel");

        GridPane.setHalignment(title, CENTER);
        title.setFont(Font.font(20));
        cbUserType.getItems().addAll("Student", "Instructor", "Administrator");
        cbUserType.getSelectionModel().select(0);
        cbUserType.setMaxWidth(Double.MAX_VALUE);
        cbStudentStatus.getItems().addAll("Active", "Suspended");
        cbStudentStatus.getSelectionModel().select(0);
        cbStudentStatus.setMaxWidth(Double.MAX_VALUE);
        addUser.setMaxWidth(Double.MAX_VALUE);
        cancel.setMaxWidth(Double.MAX_VALUE);

        cancel.setOnAction(__ -> {
            ps.close();
            new MainMenuScreen();
        });

        ps.setOnCloseRequest(__ -> cancel.fire());

        pane.add(title, 0, 0, 2, 1);
        pane.addColumn(0, new Label("Username:"), new Label("Last Name:"),
                new Label("First Name:"), new Label("Password:"), new Label("Confirm:"),
                new Label("User Type:"), new Label("Student Number:"), new Label("Program:"),
                new Label("Status:"));
        pane.addColumn(1, tfUsername, tfLastName, tfFirstName, pfPassword, pfConfirm, cbUserType,
                tfStudentNumber, tfStudentProgram, cbStudentStatus);
        pane.addRow(10, cancel, addUser);

        for(Node node : pane.getChildren()) {
            if(node instanceof Label && node != title) {
                GridPane.setHalignment(node, RIGHT);
            }
        }

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS Admin - Add User");
        ps.show();
    }
}
