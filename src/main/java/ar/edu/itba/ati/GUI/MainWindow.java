package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.ImageManager;
import ar.edu.itba.ati.Interface.Controller;
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

import java.awt.image.BufferedImage;
import java.io.File;

public class MainWindow {


    private Stage stage;

    private ImageView imageView;

     private Controller controller;

    public MainWindow(Stage stage, Controller controller) {
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
        openImage.setOnAction(e->loadImage());

        MenuItem openImageRAW = new MenuItem("Open RAW Image");
        MenuItem saveImage = new MenuItem("Save Image");
        saveImage.setOnAction(e-> saveFile());

        file.getItems().addAll(openImage,saveImage);


        Menu tools = new Menu("Tools");

        MenuItem getPixel = new MenuItem("Get Pixel value");
        getPixel.setOnAction(e->showGetPixelModal());

        MenuItem modifyPixel= new MenuItem("Modify pixel value");
        modifyPixel.setOnAction(e-> showEditPixelModal());
        tools.getItems().addAll(getPixel,modifyPixel);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,tools);
        return menuBar;

    }

    private void loadImage(){
        FileChooser chooser = new FileChooser();

        File image =  chooser.showOpenDialog(stage);
        BufferedImage image1 = null;
        if(image != null){
            try {
                image1 = controller.loadImage(image));
            }catch (Exception e){

            }

            Image img = SwingFXUtils.toFXImage(image1, null);
            imageView.setImage(img);
        }


    }

    public void saveFile(){
        FileChooser chooser = new FileChooser();

        File file = chooser.showSaveDialog(stage);


        controller.saveImage(file);

    }

    public void showEditPixelModal(){
        Stage stage = new Stage();

        HBox first = new HBox(20);

        HBox xBox = getCoordenateBox("X: ");



        HBox yBox = getCoordenateBox("Y: ");

        first.getChildren().addAll(xBox,yBox);
        first.setAlignment(Pos.CENTER);


        HBox second = new HBox(10);
        Label label = new Label("New Value");
        TextField field = new TextField();
        field.setPrefWidth(60);


        second.getChildren().addAll(label,field);
        second.setAlignment(Pos.CENTER);

        Button modifyButton = new Button("Modify");

        //modifyButton.setOnAction();

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first,second,modifyButton);
        stage.setScene(new Scene(container,400,120));

        stage.show();


    }

    public void showGetPixelModal(){
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
