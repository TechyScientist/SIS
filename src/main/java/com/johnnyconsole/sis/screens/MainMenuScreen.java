package com.johnnyconsole.sis.screens;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static javafx.geometry.HPos.*;
import static com.johnnyconsole.sis.session.Session.*;

public class MainMenuScreen extends Application {

    public MainMenuScreen() {
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("Welcome, " + first + "!");
        Button signOff = new Button("Sign Off");
        Tab academicSummary = new Tab("Academic Summary"),
                registration = new Tab("Registration"),
                studentResources = new Tab("Resources"),
                myCourses = new Tab("My Courses"),
                facultyResources = new Tab("Resources"),
                userManagement = new Tab("User Management"),
                termManagement = new Tab("Term Management"),
                courseManagement = new Tab("Term Management"),
                registrationManagement = new Tab("Registration Management"),
                adminResources = new Tab("Resources");
        Tab[] studentTabs = {academicSummary, registration, studentResources},
                facultyTabs = {myCourses, facultyResources},
                adminTabs = {userManagement, termManagement, courseManagement, registrationManagement, adminResources};
        TabPane tabs = new TabPane();

        if(type.equals("Administrator")) {
            tabs.getTabs().addAll(adminTabs);
        }
        else if(type.equals("Instructor")) {
            tabs.getTabs().addAll(facultyTabs);
        }
        else {
            tabs.getTabs().addAll(studentTabs);
        }

        title.setFont(Font.font(20));
        GridPane.setHalignment(title, CENTER);
        signOff.setMaxWidth(Double.MAX_VALUE);

        pane.add(title, 0, 0);
        pane.add(tabs, 0, 1);
        pane.add(signOff, 0, 2);

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS - Main Menu");
        ps.show();
    }
}
