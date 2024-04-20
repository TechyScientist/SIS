package com.johnnyconsole.sis.dialog;

import com.johnnyconsole.sis.screens.MainMenuScreen;
import com.johnnyconsole.sis.screens.SignOnScreen;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignOffConfirmDialog extends Application {

    private final MainMenuScreen caller;

    public SignOffConfirmDialog(MainMenuScreen caller) {
        this.caller = caller;
        caller.hide();
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label title = new Label("Are you sure you want to sign off?");
        Button yes = new Button("Yes"),
                no = new Button("No");

        yes.setMaxWidth(Double.MAX_VALUE);
        no.setMaxWidth(Double.MAX_VALUE);

        yes.setOnAction(__ -> {
            caller.close();
            ps.close();
            new SignOnScreen().start(new Stage());
        });

        no.setOnAction(__ -> {
            ps.close();
            caller.show();
        });

        ps.setOnCloseRequest(__ -> no.fire());

        pane.addColumn(0, title, yes, no);

        ps.setScene(new Scene(pane));
        ps.setTitle("Confirm Sign Off");
        ps.show();

    }
}
