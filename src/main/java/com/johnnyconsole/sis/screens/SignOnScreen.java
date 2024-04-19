package com.johnnyconsole.sis.screens;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static javafx.geometry.HPos.*;

public class SignOnScreen extends Application {

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("Sign On");
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        Button signOn = new Button("Sign On"),
                exit = new Button("Exit");

        title.setFont(Font.font(20));
        GridPane.setHalignment(title, CENTER);
        signOn.setMaxWidth(Double.MAX_VALUE);
        exit.setMaxWidth(Double.MAX_VALUE);

        pane.add(title, 0, 0, 2, 1);
        pane.addRow(1, new Label("Username:"), username);
        pane.addRow(2, new Label("Password:"), password);
        pane.addRow(3, exit, signOn);

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS - Sign On");
        ps.show();
    }
}
