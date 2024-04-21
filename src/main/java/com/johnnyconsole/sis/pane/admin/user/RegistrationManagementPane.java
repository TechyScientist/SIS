package com.johnnyconsole.sis.pane.admin.user;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class RegistrationManagementPane extends GridPane {

    public RegistrationManagementPane() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label title = new Label("User Management");
        Button add = new Button("Enroll Student"),
                remove = new Button("Unenroll Student"),
                view = new Button("View Student Registration");

        title.setFont(Font.font(20));
        add.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);
        view.setMaxWidth(Double.MAX_VALUE);

        addColumn(0, title, add, remove, view);
    }
}
