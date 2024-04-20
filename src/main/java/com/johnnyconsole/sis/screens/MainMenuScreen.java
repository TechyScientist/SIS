package com.johnnyconsole.sis.screens;

import com.johnnyconsole.sis.dialog.SignOffConfirmDialog;
import com.johnnyconsole.sis.panes.CourseManagementPane;
import com.johnnyconsole.sis.panes.RegistrationManagementPane;
import com.johnnyconsole.sis.panes.TermManagementPane;
import com.johnnyconsole.sis.panes.UserManagementPane;
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
import static javafx.scene.input.KeyCode.*;
import static com.johnnyconsole.sis.session.Session.*;

public class MainMenuScreen extends Application {

    private Stage stage;

    public MainMenuScreen() {
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        stage = ps;
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
courseManagement = new Tab("Course Management"),
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

        //academicSummary.setContent(new AcademicSummaryPane());
        //registration.setContent(new RegistrationPane());
        //studentResources.setContent(new StudentResourcesPane();
        //myCourses.setContent(new MyCoursesPane());
        //facultyResources.setContent(new FacultyResourcesPane());
        userManagement.setContent(new UserManagementPane());
        termManagement.setContent(new TermManagementPane());
        courseManagement.setContent(new CourseManagementPane());
        registrationManagement.setContent(new RegistrationManagementPane());
        //adminResources.setContent(new AdministratorResourcesPane());

        for(Tab tab : tabs.getTabs()) {
            tab.setClosable(false);
        }

        title.setFont(Font.font(20));
        GridPane.setHalignment(title, CENTER);
        signOff.setMaxWidth(Double.MAX_VALUE);
        signOff.setOnAction(__ -> new SignOffConfirmDialog(this));

        ps.setOnCloseRequest(__ -> signOff.fire());

        pane.setOnKeyPressed(key -> {
            if (key.getCode() == ESCAPE)
signOff.fire();
        });

        pane.add(title, 0, 0);
        pane.add(tabs, 0, 1);
        pane.add(signOff, 0, 2);

        ps.setScene(new Scene(pane));
        ps.setTitle("SIS - Main Menu");
        ps.show();
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public void close() {
        stage.close();
    }
}
