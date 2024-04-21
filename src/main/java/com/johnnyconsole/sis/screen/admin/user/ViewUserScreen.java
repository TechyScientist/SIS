package com.johnnyconsole.sis.screen.admin.user;

import com.johnnyconsole.sis.dialog.ErrorDialog;
import com.johnnyconsole.sis.screen.MainMenuScreen;
import com.johnnyconsole.sis.screen.SISScreen;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.johnnyconsole.sis.session.Session.connection;
import static javafx.geometry.HPos.CENTER;

public class ViewUserScreen extends SISScreen {

    private String errMsg;

    public ViewUserScreen() {
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("View User List");
        Button close = new Button("Close");
        close.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHalignment(title, CENTER);
        title.setFont(Font.font(20));

        close.setOnAction(__ -> {
            ps.close();
            new MainMenuScreen();
        });

        ps.setOnCloseRequest(__ -> close.fire());

        pane.add(title, 0, 0, 9, 1);
        pane.addRow(1, new Label("Username"), new Label("Last Name"),
                new Label("First Name"), new Label("User Type"), new Label("Student Number"),
                new Label("Program"), new Label("Status"));

        int row = 2;

        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM users;");
            while(rs.next()) {
                pane.addRow(row++,
                        new Label(rs.getString("username")),
                        new Label(rs.getString("lastName")),
                        new Label(rs.getString("firstName")),
                        new Label(rs.getString("userType")),
                        new Label(rs.getString("studentNumber") == null ? "" : rs.getString("studentNumber")),
                        new Label(rs.getString("studentProgram") == null ? "" : rs.getString("studentProgram")),
                        new Label(rs.getString("studentStatus") == null ? "" : rs.getString("studentStatus"))
                );
            }
        }
        catch(AssertionError e) {
            errMsg = "Could not connect to database";
            new ErrorDialog(errMsg);
        }
        catch(SQLException e) {
            errMsg = "Database error";
            new ErrorDialog(errMsg);
        }
        catch(Exception e) {
            errMsg = "Unknown error: " + e.getMessage();
            new ErrorDialog(errMsg);
        }
        finally {
            if(errMsg != null) {
                new ErrorDialog(errMsg);
            }
        }

        pane.add(close, 0, row, 9, 1);

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS Admin - View User List");
        ps.show();
    }
}
