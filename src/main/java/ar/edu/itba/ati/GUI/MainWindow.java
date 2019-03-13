package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.ImageManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;

public class MainWindow {


    private Stage stage;

    private ImageView imageView;

    public MainWindow(Stage stage) {
        this.stage = stage;
        imageView = new ImageView();
        BorderPane container = new BorderPane();

        MenuBar menuBar=  getMenuBar();
        container.setTop(menuBar);
        container.setCenter(imageView);


        stage.setScene(new Scene(container,800,600));

    }

    public MenuBar getMenuBar(){

        Menu file = new Menu("File");

        MenuItem openImage= new MenuItem("Open Image");
        openImage.setOnAction(e-> loadImage());

        MenuItem openImageRAW = new MenuItem("Open RAW Image");
        MenuItem saveImage = new MenuItem("Save Image");

        file.getItems().addAll(openImage,saveImage);


        Menu tools = new Menu("Tools");

        MenuItem getPixel = new MenuItem("Get Pixel value");
        getPixel.setOnAction(e->showEditPixelModal());

        MenuItem modifyPixel= new MenuItem("Edit pixel value");

        tools.getItems().addAll(getPixel,modifyPixel);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,tools);
        return menuBar;

    }

    public void loadImage(){
        FileChooser chooser = new FileChooser();

        chooser.showOpenDialog(stage);
        File image =  chooser.showOpenDialog(stage);
        ar.edu.itba.ati.model.Image image1 = null;
        if(image != null){
            try {
                image1 = ImageManager.loadImage(image);
            }catch (Exception e){

            }

            image1.getBufferdImage();
            Image img = SwingFXUtils.toFXImage(image1.getBufferdImage(), null);
            imageView.setImage(img);
        }


    }

    public void showEditPixelModal(){
        Stage stage = new Stage();

        HBox first = new HBox(20);

        HBox xBox = getCoordenateBox("X: ");



        HBox yBox = getCoordenateBox("Y: ");

        first.getChildren().addAll(xBox,yBox);
        first.setAlignment(Pos.CENTER);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first);
        stage.setScene(new Scene(container,400,100));

        stage.show();


    }

    private HBox getCoordenateBox(String label) {
        Label yLabel = new Label(label);
        TextField yField = new TextField();
        yField.setPrefWidth(60);
        HBox yBox = new HBox(10);
        yBox.getChildren().addAll(yLabel,yField);
        yBox.setAlignment(Pos.CENTER);
        return yBox;
    }


}
