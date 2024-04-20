package com.johnnyconsole.sis.dialog;

import com.johnnyconsole.sis.screens.MainMenuScreen;
import com.johnnyconsole.sis.screens.SISScreen;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.ESCAPE;

public class SuccessDialog extends Application {

    private final SISScreen caller;
    private final String message;

    public SuccessDialog(SISScreen caller, String message) {
        this.caller = caller;
        this.message = message;
        start(new Stage());
    }

    @Override
    public void start(Stage ps) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setHgap(10);
        pane.setVgap(10);

        Label label = new Label(message);
        Button dismiss = new Button("Dismiss");

        dismiss.setMaxWidth(Double.MAX_VALUE);
        dismiss.setOnAction(__ -> {
            ps.close();
            caller.close();
            new MainMenuScreen();
        });

        pane.setOnKeyPressed(key -> {
            if(key.getCode() == ENTER || key.getCode() == ESCAPE)
                dismiss.fire();
        });
        pane.addColumn(0, label, dismiss);

        ps.setScene(new Scene(pane));
        ps.setTitle("Success");
        ps.show();

    }
}
