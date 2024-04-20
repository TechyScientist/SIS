package com.johnnyconsole.sis.panes;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class CourseManagementPane extends GridPane {

    public CourseManagementPane() {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        Label title = new Label("Course Management");
        Button add = new Button("Add Course"),
                edit = new Button("Edit Course"),
                remove = new Button("Remove Course"),
                view = new Button("View Course List");

        title.setFont(Font.font(20));
        add.setMaxWidth(Double.MAX_VALUE);
        edit.setMaxWidth(Double.MAX_VALUE);
        remove.setMaxWidth(Double.MAX_VALUE);
        view.setMaxWidth(Double.MAX_VALUE);

        addColumn(0, title, add, edit, remove, view);
    }
}
