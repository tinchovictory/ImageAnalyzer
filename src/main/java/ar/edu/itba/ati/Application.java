package ar.edu.itba.ati;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception {




        Menu menu = new Menu("Opciones");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        //BufferedImage image = ImageIO.read(new File("/Users/segundofarina/Downloads/zuck.jpg"));
        Button openImage =  new Button();
        FileChooser chooser = new FileChooser();
        openImage.setOnAction(e -> {
           File image =  chooser.showOpenDialog(stage);
           if(image != null){

           }

        });
        VBox container = new VBox();
        container.getChildren().add(menuBar);
        container.getChildren().add(openImage);
        //container.getChildren().add(SwingFXUtils.toFXImage(capture, null););

        stage.setScene(new Scene(container, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
