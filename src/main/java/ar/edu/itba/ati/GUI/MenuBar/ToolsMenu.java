package ar.edu.itba.ati.GUI.MenuBar;

import ar.edu.itba.ati.GUI.ChannelBandsWindow;
import ar.edu.itba.ati.GUI.ContrastWindow;
import ar.edu.itba.ati.GUI.HistogramWindow;
import ar.edu.itba.ati.GUI.MainWindow;
import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.awt.*;
import java.io.IOException;

public class ToolsMenu extends Menu {

    Controller controller;

    public ToolsMenu(Controller controller){
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ToolsMenu.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showGetPixelModal(){
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

    @FXML
    private void showEditPixelModal(){
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
            controller.getMainWindow().refreshImage();


        });

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(first,second,modifyButton);
        stage.setScene(new Scene(container,400,120));

        stage.show();

    }

    @FXML
    private void showMeanModal(){
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

    @FXML
    private void showChannelBands(){

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

    }

    @FXML
    private void showHistogram(){

        Stage newStage =  new Stage();
        newStage.setScene(new Scene(new HistogramWindow(controller.getHisotgram())));

        newStage.show();

    }

    @FXML
    private void compressDynamicRange(){
        controller.compressDynamicRange();
        controller.getMainWindow().refreshImage();
    }

    @FXML
    private void turnNegative(){
        controller.getNegative();
        controller.getMainWindow().refreshImage();
    }

    @FXML
    private void modifyContrast(){
        Stage newStage = new Stage();
        newStage.setScene(new Scene( new ContrastWindow(controller)));
        newStage.show();

    }

    @FXML
    private void equalizeImage(){
        controller.equalizeImage();
        controller.getMainWindow().refreshImage();
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
