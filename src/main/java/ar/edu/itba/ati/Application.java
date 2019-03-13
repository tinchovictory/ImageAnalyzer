package ar.edu.itba.ati;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.sanselan.ImageReadException;
import javafx.scene.image.Image;


import java.io.File;
import java.io.IOException;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception, IOException, ImageReadException {




        MenuBar menuBar = getNewMenuBar();

        //BufferedImage image = ImageIO.read(new File("/Users/segundofarina/Downloads/zuck.jpg"));
        Button openImage =  new Button("Abrir una Imagen");
        FileChooser chooser = new FileChooser();
        openImage.setOnAction(e -> {
           File image =  chooser.showOpenDialog(stage);
           if(image != null){
               openNewImage(image);
           }

        });
        BorderPane container = new BorderPane();
        container.setTop(menuBar);
        container.setCenter(openImage);
        //container.getChildren().add(SwingFXUtils.toFXImage(capture, null););

        stage.setScene(new Scene(container, 800, 600));
        stage.show();
    }

    private void openNewImage (File image){
        Stage window = new Stage();
        final ImageView mv = new ImageView();

        MenuBar menuBar = getNewMenuBar();
        Image img = new Image(image.toURI().toString());
        mv.setImage(img);
        VBox container = new VBox();
        container.getChildren().add(menuBar);
        container.getChildren().add(mv);


        window.setScene(new Scene(container));
        window.show();
    }

    private MenuBar getNewMenuBar(){
        Menu options = new Menu("Opciones");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(options);
        return menuBar;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
