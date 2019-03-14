package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Interface.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.awt.*;
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
        this.controller = controller;


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
                image1 = controller.loadImage(image);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Load Image failed");
                return;
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
        stage.setTitle("Edit pixel value");
        HBox first = new HBox(20);

        CoordenateBox xBox = new CoordenateBox("X: ");
        CoordenateBox yBox = new CoordenateBox("Y: ");

        first.getChildren().addAll(xBox.getBox(),yBox.getBox());
        first.setAlignment(Pos.CENTER);


        HBox second = new HBox(10);
        Label label = new Label("New Value");

        CoordenateBox rBox = new CoordenateBox("R: ");
        CoordenateBox gBox = new CoordenateBox("G: ");
        CoordenateBox bBox = new CoordenateBox("B: ");

        second.getChildren().addAll(label,rBox.getBox(),gBox.getBox(),bBox.getBox());
        second.setAlignment(Pos.CENTER);

        Button modifyButton = new Button("Modify");

        modifyButton.setOnAction(e->{
            int x = Integer.parseInt(xBox.getField().getCharacters().toString());
            int y = Integer.parseInt(yBox.getField().getCharacters().toString());

            int r = Integer.parseInt(rBox.getField().getCharacters().toString());
            int g = Integer.parseInt(gBox.getField().getCharacters().toString());
            int b = Integer.parseInt(bBox.getField().getCharacters().toString());
            controller.editPixelValue(x,y,new Color(r,g,b));
            refreshImage();


        });

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first,second,modifyButton);
        stage.setScene(new Scene(container,400,120));

        stage.show();


    }

    public void showGetPixelModal(){
        Stage stage = new Stage();
        stage.setTitle("Get pixel value");
        HBox first = new HBox(20);

        CoordenateBox xBox = new CoordenateBox("X: ");
        CoordenateBox yBox = new CoordenateBox("Y: ");

        HBox colors = new HBox(20);

        Label rLabel = new Label("R: ");
        Label gLabel = new Label("G: ");
        Label bLabel = new Label("B: ");


        colors.getChildren().addAll(rLabel,gLabel,bLabel);
        colors.setAlignment(Pos.CENTER);


        Button getValue = new Button("Get Value");


        getValue.setOnAction(e-> {
            int x = Integer.parseInt(xBox.getField().getCharacters().toString());
            int y = Integer.parseInt(yBox.getField().getCharacters().toString());

            Color value = controller.getPixelValue(x,y);
            rLabel.setText("R: "+value.getRed());
            gLabel.setText("G: "+value.getGreen());
            bLabel.setText("B: "+value.getBlue());

        });


        first.getChildren().addAll(xBox.getBox(),yBox.getBox());
        first.setAlignment(Pos.CENTER);




        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first,getValue,colors);
        stage.setScene(new Scene(container,400,400));

        stage.show();


    }

    public void refreshImage(){
        imageView.setImage(SwingFXUtils.toFXImage(controller.getImage(),null));
    }

    private class CoordenateBox{
        Label yLabel;
        TextField yField;
        HBox yBox;

        public CoordenateBox(String label){
             yLabel = new Label(label);
             yField = new TextField();
            yField.setPrefWidth(60);
             yBox = new HBox(10);
            yBox.getChildren().addAll(yLabel,yField);
            yBox.setAlignment(Pos.CENTER);
        }


        public Label getLabel() {
            return yLabel;
        }

        public TextField getField() {
            return yField;
        }

        public HBox getBox() {
            return yBox;
        }
    }


}
