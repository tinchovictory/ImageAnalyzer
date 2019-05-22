package ar.edu.itba.ati.GUI.Windows;

import ar.edu.itba.ati.GUI.SelectableAreaFactory;
import ar.edu.itba.ati.Interface.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TrackingWindow extends VBox {

    Controller controller;

    @FXML
    Button selectObject;

    @FXML
    Button confirmObject;

    @FXML
    Button selectBackground;

    @FXML
    Button confirmBackground;

    @FXML
    Button startTracking;


    SelectableAreaFactory selectableAreaFactory;

    List<Point> pointsObject;

    List<Point> pointsBackgorund;

    private TrackingWindow(Controller controller){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("TrackingWindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch(IOException e) {
            System.out.println("Error opening TrackingWindow.fmxl");
            e.printStackTrace();
        }
        this.controller = controller;
        this.selectableAreaFactory = new SelectableAreaFactory(controller.getMainWindow());

    }

    public void init(){
        selectObject.setOnAction(e ->selectableAreaFactory.startSelection());

        confirmObject.setOnAction(e-> {
            this.pointsObject = selectableAreaFactory.getSelection();
            System.out.println(this.pointsObject);
            selectableAreaFactory.stopSelection();
        });

        selectBackground.setOnAction(e ->selectableAreaFactory.startSelection());

        confirmBackground.setOnAction(e-> {
            this.pointsBackgorund = selectableAreaFactory.getSelection();
            System.out.println(this.pointsBackgorund);
            selectableAreaFactory.stopSelection();
        });

        startTracking.setOnAction(e-> {

            controller.setTrackArea(pointsObject, pointsBackgorund);
            controller.getMainWindow().refreshImage();
        });

    }




    public static TrackingWindow openInNewWindow(Controller controller){
        Stage newStage = new Stage();
        TrackingWindow window =  new TrackingWindow(controller);
        newStage.setScene(new Scene(window ));
        window.init();
        newStage.show();
        return window;

    }




}
