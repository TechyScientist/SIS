package com.johnnyconsole.sis.screen;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.sis.dialog.ErrorDialog;
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
import java.sql.SQLException;

import static javafx.geometry.HPos.*;
import static javafx.scene.input.KeyCode.*;
import static com.johnnyconsole.sis.session.Session.*;

public class SignOnScreen extends Application {
    
    private String errMsg;
    
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
                new MainMenuScreen();
            }
            else {
                new ErrorDialog(errMsg);
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
        connect();
    }

    public boolean authenticate(String txtUsername, String txtPassword) {
        try {
            if (connection == null) throw new NullPointerException();
            if(txtUsername.isEmpty() || txtPassword.isEmpty()) throw new IllegalArgumentException();

            char[] passwordBytes = txtPassword.toCharArray();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            stmt.setString(1, txtUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                char[] hashBytes = rs.getString("passwordHash").toCharArray();
                if(BCrypt.verifyer().verify(passwordBytes, hashBytes).verified) {
                    username = txtUsername;
                    last = rs.getString("lastName");
                    first = rs.getString("firstName");
                    type = rs.getString("userType");
                    studentNumber = rs.getInt("studentNumber");
                    program = rs.getString("studentProgram");
                    status = rs.getString("studentStatus");
                    return true;
                }
                else {
                    errMsg = "Invalid Credentials";
                    return false;
                }
            }
            else {
                errMsg = "Invalid Credentials";
                return false;
            }

        } catch(SQLException e) {
            errMsg = "Database Error";
            return false;
        } catch (NullPointerException e) {
            errMsg = "Could not connect to database";
            return false;
        }
        catch(IllegalArgumentException e) {
           errMsg = "Missing Username or Password";
           return false;
        } catch(Exception e) {
            errMsg = "Unknown Error: " + e.getMessage();
            return false;
        }
    }
}
