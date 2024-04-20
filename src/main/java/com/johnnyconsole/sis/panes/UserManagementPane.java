package com.johnnyconsole.sis.panes;

import com.johnnyconsole.sis.screens.MainMenuScreen;
import com.johnnyconsole.sis.screens.admin.AddUserScreen;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class UserManagementPane extends GridPane {

    public UserManagementPane(MainMenuScreen menuScreen) {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label title = new Label("User Management");
        Button add = new Button("Add User"),
                edit = new Button("Edit User"),
                remove = new Button("Remove User"),
                view = new Button("View User List");

        title.setFont(Font.font(20));
        add.setMaxWidth(Double.MAX_VALUE);
        edit.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);
        view.setMaxWidth(Double.MAX_VALUE);

        add.setOnAction(__ -> {
            menuScreen.close();
            new AddUserScreen();
        });

        addColumn(0, title, add, edit, remove, view);
    }
}
