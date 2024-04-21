package com.johnnyconsole.sis.pane.admin.user;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class TermManagementPane extends GridPane {

    public TermManagementPane() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label title = new Label("Term Management");
        Button add = new Button("Add Term"),
                edit = new Button("Edit Term"),
                remove = new Button("Remove Term"),
                view = new Button("View Term List");

        title.setFont(Font.font(20));
        add.setMaxWidth(Double.MAX_VALUE);
        edit.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);
        view.setMaxWidth(Double.MAX_VALUE);

        addColumn(0, title, add, edit, remove, view);
    }
}
