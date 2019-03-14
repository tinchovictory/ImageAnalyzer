package ar.edu.itba.ati;

import ar.edu.itba.ati.GUI.MainWindow;
import javafx.stage.Stage;
import org.apache.sanselan.ImageReadException;


import java.io.IOException;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception, IOException, ImageReadException {


        Controller controller = new Controller();
        MainWindow mainWindow = new MainWindow(stage,controller);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
