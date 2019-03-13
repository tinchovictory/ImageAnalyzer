package ar.edu.itba.ati;

import ar.edu.itba.ati.GUI.MainWindow;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.io.File;

public class Application extends javafx.application.Application {


    @Override
    public void start(Stage stage) throws Exception {

//        Button openImage =  new Button("Abrir una Imagen");
//        Button grey = new Button("Mostrar degradee de grises");
//        Button colors = new Button("Mostrar degradee de colores");
//        FileChooser chooser = new FileChooser();
//        openImage.setOnAction(e -> {
//           File image =  chooser.showOpenDialog(stage);
//           if(image != null){
//               openNewImage(image);
//           }
//
//        });
//        VBox container = new VBox();
//        container.getChildren().addAll(openImage,grey,colors);
//        container.setAlignment(Pos.CENTER);
//        container.setSpacing(20.0);
//        //container.getChildren().add(SwingFXUtils.toFXImage(capture, null););
//
//        stage.setScene(new Scene(container, 800, 600));

        MainWindow mainWindow = new MainWindow(stage);
        stage.show();
    }

    private void openNewImage (File image){
        Stage window = new Stage();
        final ImageView mv = new ImageView();

        MenuBar menuBar = getNewMenuBar();
        Image img = new Image(image.toURI().toString());
        mv.setImage(img);

        BorderPane container = new BorderPane();
        container.setTop(menuBar);
        container.setCenter(mv);


        window.setScene(new Scene(container));
        window.show();
    }

    private MenuBar getNewMenuBar(){
        Menu options = new Menu("Opciones");
        MenuItem saveImage= new MenuItem("Guardar Imagen");
        MenuItem modifyPixel= new MenuItem("Modificar un pixel");
        MenuItem getPixel = new MenuItem("Obtener el valor de un pixel");
        options.getItems().addAll(saveImage,modifyPixel,getPixel);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(options);
        return menuBar;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
