package com.johnnyconsole.sis.screens.admin;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.sis.dialog.ErrorDialog;
import com.johnnyconsole.sis.dialog.SuccessDialog;
import com.johnnyconsole.sis.screens.MainMenuScreen;
import com.johnnyconsole.sis.screens.SISScreen;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.geometry.HPos.*;
import static com.johnnyconsole.sis.session.Session.*;

public class AddUserScreen extends SISScreen {

    private String errMsg;

    public AddUserScreen() {
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        this.ps = ps;
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

        cbUserType.getSelectionModel().selectedItemProperty().addListener((__, ___, newValue) -> {
            if(newValue.equals("Student")) {
                tfStudentNumber.setDisable(false);
                tfStudentProgram.setDisable(false);
                cbStudentStatus.setDisable(false);
                cbStudentStatus.getSelectionModel().select(0);
            }
            else {
                tfStudentNumber.setDisable(true);
                tfStudentProgram.setDisable(true);
                cbStudentStatus.setDisable(true);
                cbStudentStatus.getSelectionModel().select(0);
            }
        });

        addUser.setOnAction(__ -> {
            if(addUser(tfUsername.getText(), tfLastName.getText(),
                    tfFirstName.getText(), pfPassword.getText(), pfConfirm.getText(),
                    cbUserType.getSelectionModel().getSelectedItem(),tfStudentNumber.getText(),
                    tfStudentProgram.getText(), cbStudentStatus.getSelectionModel().getSelectedItem())) {
                new SuccessDialog(this, "User Added Successfully");
            }
            else {
                new ErrorDialog(errMsg);
            }
        });

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

    private boolean addUser(String username, String lastName, String firstName,
                            String password, String confirm, String userType,
                            String studentNumber, String program, String status) {
        try {
            if(username.isEmpty() || lastName.isEmpty() || firstName.isEmpty() || password.isEmpty() || confirm.isEmpty() || (userType.equals("Student") && (studentNumber.isEmpty() || program.isEmpty()))) {
                errMsg = "Empty fields";
                return false;
            }
            assert connection != null;
            PreparedStatement stmt = connection.prepareStatement("SELECT username FROM users WHERE username=?;");
                    stmt.setString(1, username.toLowerCase());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        errMsg = "User already exists";
                        return false;
                    }
                    if(!password.equals(confirm)) {
                        errMsg = "Passwords do not match";
                        return false;
                    }
                    if(userType.equals("Student") && studentNumber.length() != 9) throw new NumberFormatException();

                    stmt = connection.prepareStatement("INSERT INTO users(username, passwordHash, lastName, firstName, userType, studentNumber, studentProgram, studentStatus) VALUES(?,?,?,?,?,?,?,?);");
                    stmt.setString(1, username.toLowerCase());
                    stmt.setString(2, BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(16, password.toCharArray()));
                    stmt.setString(3, lastName);
                    stmt.setString(4, firstName);
                    stmt.setString(5, userType);
                    if(!userType.equals("Student")) {
                        stmt.setString(6, null);
                        stmt.setString(7, null);
                        stmt.setString(8, null);
                    }
                    else {
                        stmt.setInt(6, Integer.parseInt(studentNumber));
                        stmt.setString(7, program);
                        stmt.setString(8, status);
                    }
                    stmt.execute();
                    return true;

        } catch(AssertionError e) {
            errMsg = "Could not connect to database";
            return false;
        }
        catch(SQLException e) {
            errMsg = "Database Error";
            return false;
        }
        catch(NumberFormatException e) {
            errMsg = "Invalid Student Number: Must be a 9-digit number";
        }
        catch(Exception e) {
            errMsg = "Unknown Error: " + e.getMessage();
            return false;
        }
        return false;
    }

}
