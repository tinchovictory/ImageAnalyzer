package ar.edu.itba.ati.GUI;

import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
import java.io.IOException;

public class MainWindow {


    private Stage stage;

    private ImageView imageView;

     private Controller controller;

    public MainWindow(Stage stage, Controller controller) {
        this.stage = stage;
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        BorderPane container = new BorderPane();

        container.setPrefHeight(600);
        container.setPrefWidth(800);

        imageView.fitHeightProperty().bind(container.heightProperty());
        imageView.fitWidthProperty().bind(container.widthProperty());
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
        openImageRAW.setOnAction(e-> openRawImage());
        MenuItem saveImage = new MenuItem("Save Image");
        saveImage.setOnAction(e-> saveFile());

        file.getItems().addAll(openImage,openImageRAW,saveImage);



        Menu tools = new Menu("Tools");

        MenuItem getPixel = new MenuItem("Get Pixel value");
        getPixel.setOnAction(e->showGetPixelModal());

        MenuItem modifyPixel= new MenuItem("Modify pixel value");
        modifyPixel.setOnAction(e-> showEditPixelModal());

        MenuItem getMean = new MenuItem("Get mean");
        getMean.setOnAction(e-> showMeanModal());

        MenuItem channelBands = new MenuItem("Show channel bands");
        channelBands.setOnAction(e-> showChannelBands());

        tools.getItems().addAll(getPixel,modifyPixel,getMean,channelBands);


        Menu create = new Menu("Generate");
        MenuItem circle = new MenuItem("Circle");
        circle.setOnAction(e-> {
            controller.createCircle();
            refreshImage();
        });
        MenuItem square = new MenuItem("Square");
        square.setOnAction(e->{
            controller.createSquare();
            refreshImage();
        });

        MenuItem colorGradient = new MenuItem("Color gradient");
        colorGradient.setOnAction(e-> {
            controller.createColorGradient();
            refreshImage();
        });
        MenuItem greyGradient = new MenuItem("Grey gradient");
        greyGradient.setOnAction(e ->{
            controller.createGreyGradient();
            refreshImage();
        });



        create.getItems().addAll(circle,square,colorGradient,greyGradient);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,create,tools);
        return menuBar;

    }

    private void openRawImage(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("OpenRawImage.fxml"));
        Stage rawStage = new Stage();
        Parent root = null;
        try {
            root = loader.load();
            rawStage.setScene(new Scene(root));
        }catch (IOException e ){
            System.out.println("Failed to load FXML");
            e.printStackTrace();
        }

        OpenRawImage rawImageController = loader.getController();
        rawImageController.initData(this,rawStage);


    }

    public void openRawImage(int width, int height,File image){
        controller.loadRawImage(image,height,width);
        refreshImage();
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
            refreshImage();
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

    public void showChannelBands(){

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ImageBands.fxml"));
        Stage stage = new Stage();
        try {
            Parent root1 = (Parent) fxmlLoader.load();
            ChannelBandsWindow cb =  fxmlLoader.getController();
            cb.setController(controller);
            cb.setStage(stage);
            stage.setScene(new Scene(root1));
            stage.show();
            cb.setImages();
        }catch (IOException e){
            e.printStackTrace();
        }


        //new ChannelBandsWindow(controller,stage);

    }

    public ImageView getImage(BufferedImage bufferedImage){
        ImageView iv = new ImageView();
         iv.setImage(SwingFXUtils.toFXImage(bufferedImage,null));
         return iv;
    }

    public void showMeanModal(){
        Stage stage = new Stage();
        stage.setTitle("Get mean");

        CoordenateBox p1x = new CoordenateBox("X: ");
        CoordenateBox p1y = new CoordenateBox("Y: ");
        HBox p1 = new HBox(20);
        p1.getChildren().addAll(p1x.getBox(),p1y.getBox());
        p1.setAlignment(Pos.CENTER);

        CoordenateBox p2x = new CoordenateBox("X: ");
        CoordenateBox p2y = new CoordenateBox("Y: ");
        HBox p2 = new HBox(20);
        p2.getChildren().addAll(p2x.getBox(),p2y.getBox());
        p2.setAlignment(Pos.CENTER);

        Button getMean = new Button("Get mean");

        Label colorsLabel = new Label("");

        getMean.setOnAction(e-> {
           Color colors =  controller.getPixelsMean(new Point(p1x.parseInt(),p1y.parseInt()), new Point(p2x.parseInt(),p2y.parseInt()));
           colorsLabel.setText("R: "+colors.getRed()+" G: "+colors.getGreen()+" B: "+colors.getBlue());
        });

        VBox container = new VBox(20);
        container.getChildren().addAll(p1,p2,getMean,colorsLabel);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));

        stage.setScene(new Scene(container));
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
        stage.setScene(new Scene(container,400,200));

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

        public int parseInt(){
           return Integer.parseInt(getField().getCharacters().toString());
        }
    }


}
