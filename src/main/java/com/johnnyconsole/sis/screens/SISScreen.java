package com.johnnyconsole.sis.screens;

import javafx.application.Application;
import javafx.stage.Stage;

public abstract class SISScreen extends Application {

    protected Stage ps;

    public void show() {
        ps.show();
    }

    public void hide() {
        ps.hide();
    }

    public void close() {
        ps.close();
    }

}
