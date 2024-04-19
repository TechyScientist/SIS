package com.johnnyconsole.sis.screens;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.sis.database.Database;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static javafx.geometry.HPos.*;
import static javafx.scene.input.KeyCode.*;
import static com.johnnyconsole.sis.session.Session.*;

public class SignOnScreen extends Application {

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("Sign On");
        TextField tfUsername = new TextField();
        PasswordField tfPassword = new PasswordField();
        Button signOn = new Button("Sign On"),
                exit = new Button("Exit");

        title.setFont(Font.font(20));
        GridPane.setHalignment(title, CENTER);
        signOn.setMaxWidth(Double.MAX_VALUE);
        exit.setMaxWidth(Double.MAX_VALUE);

        signOn.setOnAction(__ -> {
            if(authenticate(tfUsername.getText(), tfPassword.getText())) {
                ps.close();
                System.out.println("OK");
            }
            else {
                System.out.println("Error");
            }
        });

        exit.setOnAction(__ -> ps.close());

        pane.setOnKeyPressed(key -> {
            if(key.getCode() == ENTER) {
                signOn.fire();
            }
            else if(key.getCode() == ESCAPE) {
                exit.fire();
            }
        });

        pane.add(title, 0, 0, 2, 1);
        pane.addRow(1, new Label("Username:"), tfUsername);
        pane.addRow(2, new Label("Password:"), tfPassword);
        pane.addRow(3, exit, signOn);

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS - Sign On");
        ps.show();

        if(connection == null)
            connection = Database.connect();

    }

    public boolean authenticate(String txtUsername, String txtPassword) {
        try {
            assert connection != null;
            char[] passwordBytes = txtPassword.toCharArray();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            stmt.setString(1, txtUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                char[] hashBytes = rs.getString("passwordHash").toCharArray();
                return BCrypt.verifyer().verify(passwordBytes, hashBytes).verified;
            }

        } catch(Exception | AssertionError e) {
            return false;
        }
        return false;
    }
}
