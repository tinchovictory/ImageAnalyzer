package ar.edu.itba.ati;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new Pane(new Label("Hola")), 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
