package com.johnnyconsole.sis.screen.admin.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.sis.dialog.ErrorDialog;
import com.johnnyconsole.sis.dialog.SuccessDialog;
import com.johnnyconsole.sis.screen.MainMenuScreen;
import com.johnnyconsole.sis.screen.SISScreen;
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

import static com.johnnyconsole.sis.session.Session.connection;
import static javafx.geometry.HPos.CENTER;
import static javafx.geometry.HPos.RIGHT;

public class EditUserScreen extends SISScreen {

    private String errMsg;

    public EditUserScreen() {
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        this.ps = ps;
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("Edit User");
        TextField tfUsername = new TextField(),
                tfFirstName = new TextField(),
                tfLastName = new TextField(),
                tfStudentNumber = new TextField(),
                tfStudentProgram = new TextField();
        PasswordField pfPassword = new PasswordField(),
                pfConfirm = new PasswordField();
        ComboBox<String> cbUserType = new ComboBox<>(),
                cbStudentStatus = new ComboBox<>();
        Button editSelectUser = new Button("Select User"),
                cancel = new Button("Cancel");

        GridPane.setHalignment(title, CENTER);
        title.setFont(Font.font(20));
        cbUserType.getItems().addAll("Student", "Instructor", "Administrator");
        cbUserType.setMaxWidth(Double.MAX_VALUE);
        cbStudentStatus.getItems().addAll("Active", "Suspended", "Program Complete");
        cbStudentStatus.setMaxWidth(Double.MAX_VALUE);
        editSelectUser.setMaxWidth(Double.MAX_VALUE);
        cancel.setMaxWidth(Double.MAX_VALUE);

        editSelectUser.setOnAction(__ -> {
            if(editSelectUser.getText().contains("Select")) {
                String[] info = getUserInfo(tfUsername.getText());
                if(info == null) {
                    new ErrorDialog(errMsg);
                }
                else {
                    tfLastName.setText(info[0]);
                    tfFirstName.setText(info[1]);
                    cbUserType.getSelectionModel().select(info[2]);
                    cbStudentStatus.getSelectionModel().select(info[5]);
                    tfStudentNumber.setText(info[3]);
                    tfStudentProgram.setText(info[4]);
                    editSelectUser.setText("Edit User");

                    if(cbUserType.getSelectionModel().getSelectedItem().equals("Student")) {
                        tfStudentProgram.setDisable(false);
                        cbStudentStatus.setDisable(false);
                    }

                    tfLastName.setDisable(false);
                    tfFirstName.setDisable(false);
                    pfPassword.setDisable(false);
                    pfConfirm.setDisable(false);

                }
                return;
            }
            if(editUser(tfUsername.getText(), tfLastName.getText(), tfFirstName.getText(),
                    pfPassword.getText(), pfConfirm.getText(), tfStudentProgram.getText(),
                    cbStudentStatus.getSelectionModel().getSelectedItem())) {
                new SuccessDialog(this, "Changes Saved Successfully");
            }
            else {
                new ErrorDialog(errMsg);
            }
        });

        cancel.setOnAction(__ -> {
            if(editSelectUser.getText().contains("Edit")) {
                tfUsername.clear();
                tfLastName.clear();
                tfFirstName.clear();
                tfStudentNumber.clear();
                tfStudentProgram.clear();
                pfPassword.clear();
                pfConfirm.clear();
                cbUserType.getSelectionModel().select(null);
                cbStudentStatus.getSelectionModel().select(null);

                for (Node node : pane.getChildren()) {
                    if ((node instanceof TextField && !node.equals(tfUsername)) || node instanceof ComboBox) {
                        node.setDisable(true);
                    }
                }
                editSelectUser.setText("Select User");
            }
            else {
                ps.close();
                new MainMenuScreen();
            }
        });

        ps.setOnCloseRequest(__ -> {
            ps.close();
            new MainMenuScreen();
        });

        pane.add(title, 0, 0, 2, 1);
        pane.addColumn(0, new Label("Username:"), new Label("Last Name:"),
                new Label("First Name:"), new Label("Password:"), new Label("Confirm:"),
                new Label("User Type:"), new Label("Student Number:"), new Label("Program:"),
                new Label("Status:"));
        pane.addColumn(1, tfUsername, tfLastName, tfFirstName, pfPassword, pfConfirm, cbUserType,
                tfStudentNumber, tfStudentProgram, cbStudentStatus);
        pane.addRow(10, cancel, editSelectUser);

        for(Node node : pane.getChildren()) {
            if((node instanceof TextField && !node.equals(tfUsername)) || node instanceof ComboBox) {
                node.setDisable(true);
            }
            else if(node instanceof Label && node != title) {
                GridPane.setHalignment(node, RIGHT);
            }
        }

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS Admin - Edit User");
        ps.show();
    }

    private String[] getUserInfo(String username) {
        try {
            if(username.isEmpty()) {
                errMsg = "Empty fields";
                return null;
            }
            assert connection != null;
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username=?;");
            stmt.setString(1, username.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                errMsg = "No user found";
                return null;
            }

            return new String[] {
                    rs.getString("lastName"),
                    rs.getString("firstName"),
                    rs.getString("userType"),
                    rs.getString("studentNumber"),
                    rs.getString("studentProgram"),
                    rs.getString("studentStatus"),
            };


        } catch(AssertionError e) {
            errMsg = "Could not connect to database";
            return null;
        }
        catch(SQLException e) {
            errMsg = "Database Error";
            return null;
        }
        catch(NumberFormatException e) {
            errMsg = "Invalid Student Number: Must be a 9-digit number";
            return null;
        }
        catch(Exception e) {
            errMsg = "Unknown Error: " + e.getMessage();
            return null;
        }
    }

    private boolean editUser(String username, String lastName, String firstName,
                            String password, String confirm, String program, String status) {
        try {
            if(username.isEmpty() || lastName.isEmpty() || firstName.isEmpty() || (!password.isEmpty() && confirm.isEmpty())) {
                errMsg = "Empty fields";
                return false;
            }
            assert connection != null;

            String sql = "UPDATE users SET lastName=?, firstName=?, studentProgram=?, studentStatus=?";

            if(!password.isEmpty() && !password.equals(confirm)) {
                errMsg = "Passwords don't match";
                return false;
            }
            else if(!password.isEmpty()) {
                sql += ", passwordHash=?";
            }
            sql += " WHERE username=?;";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, lastName);
            stmt.setString(2, firstName);
            stmt.setString(3, program);
            stmt.setString(4, status);
            if(!password.isEmpty()) {
                stmt.setString(5, BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(16, password.toCharArray()));
            }
            stmt.setString((!password.isEmpty() ? 6 : 5), username.toLowerCase());

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
        catch(Exception e) {
            errMsg = "Unknown Error: " + e.getMessage();
            return false;
        }
    }

}
