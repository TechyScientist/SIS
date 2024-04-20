package com.johnnyconsole.sis.dialog;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.*;

public class ErrorDialog extends Application {

    private final String message;

    public ErrorDialog(String message) {
        this.message = message;
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label errorLabel = new Label("Error: " + message);
        Button dismiss = new Button("Dismiss");

        dismiss.setMaxWidth(Double.MAX_VALUE);
        dismiss.setOnAction(__ -> ps.close());
        pane.setOnKeyPressed(key -> {
            if(key.getCode() == ENTER || key.getCode() == ESCAPE)
                dismiss.fire();
        });
        pane.addColumn(0, errorLabel, dismiss);

        ps.setScene(new Scene(pane));
        ps.setTitle("Error");
        ps.show();

    }
}
